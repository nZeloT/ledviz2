package com.nzelot.ledviz2.sound.player.standalone;

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

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nzelot.ledviz2.sound.meta.METADataFetcher;
import com.nzelot.ledviz2.sound.meta.fetcher.JAudioTaggerFetcher;
import com.nzelot.ledviz2.sound.player.StandalonePlayer;

public class BASSPlayer extends StandalonePlayer {

	private final Logger l = LoggerFactory.getLogger(BASSPlayer.class);

	private int chan;

	@Override
	public boolean init(JSONObject specific) {
		
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

		if(!BASS_Init(-1, 44100, 0, null, null)) {
			l.error("Can't initialize device");
			exit();
			return false;
		}

		bufferSize = 1024 * SIZEOF_FLOAT;
		buffer = newByteBuffer(bufferSize);

		loaded = false;
		playing = false;
		hasNewData = false;
		updInterval = specific.optInt("updInterval", 25);

		l.debug("Initialized BASSPlayer!");

		return true;
	}

	public boolean exit(){
		stop();
		BASS_Free();
		l.debug("Exited");
		return true;
	}

	@Override
	protected boolean load() {
		if(loaded)
			stop();

		HSTREAM stream = null; 
		HMUSIC music = null;

		if((stream = BASS_StreamCreateFile(false, path, 0, 0, BASS_SAMPLE_LOOP)) == null
				&&  (music = BASS_MusicLoad(false, path, 0, 0, BASS_MUSIC_RAMP | BASS_SAMPLE_LOOP, 0)) == null) {
			l.error("Can't play file");
			return false; // Can't load the file
		}

		chan = (stream != null) ? stream.asInt() : ((music != null) ?  music.asInt() : 0);

		loaded = true;

		l.debug("Loaded " + path);

		return true;
	}

	@Override
	protected void startPlayback() {
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
	protected void pausePlayback() {
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
