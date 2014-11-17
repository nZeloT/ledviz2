package com.nzelot.ledviz2.sound.player.attatched.provider;

import static jouvieje.bass.Bass.BASS_ChannelGetData;
import static jouvieje.bass.Bass.BASS_Free;
import static jouvieje.bass.Bass.BASS_GetVersion;
import static jouvieje.bass.Bass.BASS_Init;
import static jouvieje.bass.Bass.BASS_RecordFree;
import static jouvieje.bass.Bass.BASS_RecordInit;
import static jouvieje.bass.Bass.BASS_RecordStart;
import static jouvieje.bass.defines.BASS_DATA.BASS_DATA_FFT2048;
import static jouvieje.bass.defines.BASS_SAMPLE.BASS_SAMPLE_FLOAT;
import static jouvieje.bass.utils.BufferUtils.newByteBuffer;
import static jouvieje.bass.utils.SizeOfPrimitive.SIZEOF_FLOAT;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Timer;
import java.util.TimerTask;

import jouvieje.bass.BassInit;
import jouvieje.bass.callbacks.RECORDPROC;
import jouvieje.bass.exceptions.BassException;
import jouvieje.bass.structures.HRECORD;
import jouvieje.bass.utils.Pointer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nzelot.ledviz2.sound.player.attatched.AttatchedSoundProvider;

public class BASSAttachedSoundProvider implements AttatchedSoundProvider {

	protected boolean loaded;

	protected boolean hasNewData;

	protected ByteBuffer buffer;
	protected int bufferSize;

	protected int updInterval;
	protected Timer timer;

	private RECORDPROC duffRecording = new RECORDPROC(){
		@Override
		public boolean RECORDPROC(HRECORD handle, ByteBuffer buffer, int length, Pointer user) {
			return true;
		}
	};

	private final Logger l = LoggerFactory.getLogger(BASSAttachedSoundProvider.class);

	private int chan;

	public boolean init(int bands, int updateInterval) {

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

		bufferSize = bands * SIZEOF_FLOAT;
		buffer = newByteBuffer(bufferSize);

		updInterval = updateInterval;
		loaded = false;
		hasNewData = false;

		l.debug("Initialized BASSPlayer!");

		return true;
	}

	public boolean load() {

		if(!BASS_Init(-1, 44100, 0, null, null)) {
			l.error("Can't initialize device");
			exit();
			return false;
		}

		if(!BASS_RecordInit(-1)) {
			exit();
		}

		HRECORD stream;
		if((stream = BASS_RecordStart(44100, 1, BASS_SAMPLE_FLOAT, duffRecording, null)) == null) {
			l.error("Can't load Stream");
			return false; // Can't load the file
		}

		loaded = true;
		chan = stream.asInt();
		
		l.debug("Loaded ...");
		
		l.debug("Playing ...");
		timer = new Timer("BASS Spectrum Update Timer");

		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				update();
			}
		}, updInterval, updInterval);

		return true;
	}

	public boolean exit() {
		if(loaded)
			timer.cancel();
		
		l.debug("Stop");
		loaded = false;
		BASS_RecordFree();
		BASS_Free();
		
		return true;
	}

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

	private void update(){
		if(!loaded)
			return;

		synchronized (buffer) {
			BASS_ChannelGetData(chan, buffer, BASS_DATA_FFT2048);	//Get the FFT data
			hasNewData = true;
		}
	}

	public boolean hasNewData(){
		return hasNewData;
	}

	public boolean isLoaded() {
		return loaded;
	}
}
