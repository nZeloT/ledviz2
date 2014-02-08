package com.nzelot.ledviz2.sound.player;

import static jouvieje.bass.Bass.BASS_ChannelGetData;
import static jouvieje.bass.Bass.BASS_Free;
import static jouvieje.bass.Bass.BASS_GetVersion;
import static jouvieje.bass.Bass.BASS_RecordFree;
import static jouvieje.bass.Bass.BASS_RecordInit;
import static jouvieje.bass.Bass.BASS_RecordStart;
import static jouvieje.bass.defines.BASS_DATA.BASS_DATA_FFT2048;
import static jouvieje.bass.utils.BufferUtils.newByteBuffer;
import static jouvieje.bass.utils.SizeOfPrimitive.SIZEOF_FLOAT;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jouvieje.bass.Bass;
import jouvieje.bass.BassInit;
import jouvieje.bass.callbacks.RECORDPROC;
import jouvieje.bass.defines.BASS_RECORD;
import jouvieje.bass.defines.BASS_SAMPLE;
import jouvieje.bass.exceptions.BassException;
import jouvieje.bass.structures.HRECORD;
import jouvieje.bass.utils.Pointer;

import com.apple.itunes.com.ClassFactory;
import com.apple.itunes.com.IiTunes;
import com.nzelot.ledviz2.sound.Player;
import com.nzelot.ledviz2.sound.PlayerType;
import com.nzelot.ledviz2.sound.meta.METADataFetcher;
import com.nzelot.ledviz2.sound.meta.fetcher.ITunesTagFetcher;

public class ITunesPlayer extends Player {
    
    private final Logger l = LoggerFactory.getLogger(ITunesPlayer.class);

    private IiTunes itunes;

    private int chan;

    private RECORDPROC duffRecording = new RECORDPROC(){
	@Override
	public boolean RECORDPROC(HRECORD handle, ByteBuffer buffer, int length, Pointer user) {
	    return true;
	}
    };
    
    public ITunesPlayer() {
	type = PlayerType.ATTACHED;
    }

    @Override
    public boolean init(int bands, int updateInterval) {
	itunes = ClassFactory.createiTunesApp();

	/*
	 * NativeBass Init
	 */
	try {
	    BassInit.loadLibraries();
	} catch(BassException e) {
	    l.error("Could not load Libraries!", e);
	    System.exit(1);
	}

	/*
	 * Checking NativeBass version
	 */
	if(BassInit.NATIVEBASS_LIBRARY_VERSION() != BassInit.NATIVEBASS_JAR_VERSION()) {
	    l.error("Different BASS Versions!");
	    System.exit(1);
	}

	// check the correct BASS was loaded
	if(((BASS_GetVersion() & 0xFFFF0000) >> 16) != BassInit.BASSVERSION()) {
	    l.error("An incorrect version of BASS.DLL was loaded");
	    return false;
	}

	// initialize BASS recording (default device)
	if(!BASS_RecordInit(-1)) {
	    l.error("Can't initialize device");
	    stop();
	}

	bufferSize = bands * SIZEOF_FLOAT;
	if(buffer == null || buffer.capacity() < bufferSize) {
	    buffer = newByteBuffer(bufferSize);
	}

	updInterval = updateInterval;
	loaded = false;
	playing = false;
	hasNewData = false;
	
	l.debug("Initialized ITunesPlayer!");

	return true;
    }

    @Override
    public boolean exit() {
	stop();
	loaded = false;
	BASS_RecordFree();
	BASS_Free();
	return true;
    }

    @Override
    protected boolean load() {
	HRECORD c = BASS_RecordStart(44100, 1, BASS_SAMPLE.BASS_SAMPLE_FLOAT | BASS_RECORD.BASS_RECORD_PAUSE, duffRecording, null);
	if(c == null) {
	    l.error("Can't start recording");
	    stop();
	}
	
	chan = c.asInt();
	loaded = true;
	
	return true;
    }

    @Override
    public void play() {
	if(playing || !loaded)
	    return;

	itunes.play();
	Bass.BASS_ChannelPlay(chan, true);
	playing = true;

	timer = new Timer("iTunes Spectrum Update Timer");

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
	itunes.pause();
	Bass.BASS_ChannelPause(chan);
    }

    @Override
    public void stop() {
	if(playing)
	    pause();
	if(loaded){
	    loaded = false;
	    itunes.stop();
	    Bass.BASS_ChannelStop(chan);
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
        return itunes.currentTrack().duration();
    }
    
    @Override
    protected long position() {
        return itunes.playerPosition();
    }
    
    @Override
    protected METADataFetcher getFetcher() {
        return new ITunesTagFetcher();
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
