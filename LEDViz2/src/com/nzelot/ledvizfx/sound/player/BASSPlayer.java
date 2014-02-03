package com.nzelot.ledvizfx.sound.player;

import static jouvieje.bass.Bass.BASS_ChannelGetData;
import static jouvieje.bass.Bass.BASS_ChannelPause;
import static jouvieje.bass.Bass.BASS_ChannelPlay;
import static jouvieje.bass.Bass.BASS_ChannelStop;
import static jouvieje.bass.Bass.BASS_Free;
import static jouvieje.bass.Bass.BASS_GetVersion;
import static jouvieje.bass.Bass.BASS_Init;
import static jouvieje.bass.Bass.BASS_MusicLoad;
import static jouvieje.bass.Bass.BASS_StreamCreateFile;
import static jouvieje.bass.defines.BASS_DATA.BASS_DATA_FFT2048;
import static jouvieje.bass.defines.BASS_MUSIC.BASS_MUSIC_RAMP;
import static jouvieje.bass.defines.BASS_SAMPLE.BASS_SAMPLE_LOOP;
import static jouvieje.bass.utils.BufferUtils.newByteBuffer;
import static jouvieje.bass.utils.SizeOfPrimitive.SIZEOF_FLOAT;

import java.nio.FloatBuffer;
import java.util.Timer;
import java.util.TimerTask;

import jouvieje.bass.Bass;
import jouvieje.bass.BassInit;
import jouvieje.bass.defines.BASS_POS;
import jouvieje.bass.exceptions.BassException;
import jouvieje.bass.structures.HMUSIC;
import jouvieje.bass.structures.HSTREAM;

import com.nzelot.ledvizfx.sound.Player;
import com.nzelot.ledvizfx.sound.PlayerType;
import com.nzelot.ledvizfx.sound.meta.METADataFetcher;
import com.nzelot.ledvizfx.sound.meta.fetcher.JAudioTaggerFetcher;

public class BASSPlayer extends Player {

    private int chan;

    public BASSPlayer() {
	type = PlayerType.STANDALONE;
    }

    @Override
    public boolean init(int bands, int updateInterval) {

	/*
	 * NativeBass Init
	 */
	try {
	    BassInit.loadLibraries();
	} catch(BassException e) {
	    e.printStackTrace();
	    System.exit(1);
	}

	/*
	 * Checking NativeBass version
	 */
	if(BassInit.NATIVEBASS_LIBRARY_VERSION() != BassInit.NATIVEBASS_JAR_VERSION()) {
	    System.out.println("Different BASS Versions!");
	    System.exit(1);
	}

	// check the correct BASS was loaded
	if(((BASS_GetVersion() & 0xFFFF0000) >> 16) != BassInit.BASSVERSION()) {
	    System.out.println("An incorrect version of BASS.DLL was loaded");
	    return false;
	}

	if(!BASS_Init(-1, 44100, 0, null, null)) {
	    System.out.println("Can't initialize device");
	    exit();
	    return false;
	}

	bufferSize = bands * SIZEOF_FLOAT;
	buffer = newByteBuffer(bufferSize);

	updInterval = updateInterval;
	loaded = false;
	playing = false;
	hasNewData = false;

	return true;
    }

    public boolean exit(){
	stop();
	BASS_Free();
	return true;
    }

    @Override
    protected boolean load() {
	if(loaded){
	    stop();
	}
	
	HSTREAM stream = null; 
	HMUSIC music = null;

	if((stream = BASS_StreamCreateFile(false, path, 0, 0, BASS_SAMPLE_LOOP)) == null
		&&  (music = BASS_MusicLoad(false, path, 0, 0, BASS_MUSIC_RAMP | BASS_SAMPLE_LOOP, 0)) == null) {
	    System.out.println("Can't play file");
	    return false; // Can't load the file
	}

	chan = (stream != null) ? stream.asInt() : ((music != null) ?  music.asInt() : 0);

	loaded = true;
	return true;
    }

    @Override
    public void play() {
	if(playing || !loaded)
	    return;

	BASS_ChannelPlay(chan, false);
	playing = true;

	timer = new Timer("BASS Spectrum Update Timer");

	timer.schedule(new TimerTask() {
	    @Override
	    public void run() {
		update();
	    }
	}, updInterval, updInterval);
    }

    @Override
    public void pause() {
	if(!playing || !loaded)
	    return;

	timer.cancel();
	playing = false;
	BASS_ChannelPause(chan);
    }

    @Override
    public void stop() {
	if(playing)
	    pause();
	if(loaded){
	    loaded = false;
	    BASS_ChannelStop(chan);
	}
    }

    @Override
    public float[] getSpectrumData() {
	float[] tmp = null;
	synchronized (buffer) {
	    FloatBuffer fb = buffer.asFloatBuffer();
	    tmp = new float[fb.remaining()];
	    fb.get(tmp);
	    hasNewData = false;
	}
	return tmp;
    }

    @Override
    protected long duration() {
	return Bass.BASS_ChannelGetLength(chan, BASS_POS.BASS_POS_BYTE);
    }

    @Override
    protected long position() {
	return Bass.BASS_ChannelGetPosition(chan, BASS_POS.BASS_POS_BYTE);
    }

    @Override
    protected METADataFetcher getFetcher() {
	return new JAudioTaggerFetcher();
    }

    private void update(){
	if(!playing)
	    return;

	synchronized (buffer) {
	    BASS_ChannelGetData(chan, buffer, BASS_DATA_FFT2048);	//Get the FFT data
	    hasNewData = true;
	}
    }
}
