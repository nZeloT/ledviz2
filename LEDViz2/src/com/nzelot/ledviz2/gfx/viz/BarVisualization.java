package com.nzelot.ledviz2.gfx.viz;

import java.io.FileInputStream;
import java.util.Properties;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nzelot.ledviz2.LEDViz2;
import com.nzelot.ledviz2.gfx.LED;
import com.nzelot.ledviz2.gfx.LEDMatrix;

public class BarVisualization implements Visualization {

	private final Logger l = LoggerFactory.getLogger(LEDViz2.class);

	private static final int HISTORY_SIZE = 10;

	private static final double MIN_DB_VALUE = -90;
	private static final double MAX_DB_VALUE = 0;
	private static final double DB_SCALE = (MAX_DB_VALUE - MIN_DB_VALUE);

	// 0 = Linear Bar dist. // 1 = log Bar dist
	private static int barIndexDistribution;
	// 0 = Decibel scale // 1 = sqrt scale // 2 = linear scale
	private static int barScaleType;

	private float[][] fftHistory;

	private int rotator;

	private int cols;
	private int rowsV;

	private int[] barIndexMax;

	@Override
	public void aplyFFTData(LEDMatrix matrix, float[]... newData) {		
		double fftBucketHeight = 0f;
		int barHeight = 0;
		int barIndex = 0;
		
		for(int i = 0; i < newData[0].length; i++){

			switch(barScaleType){
			
			case 0:
				//Decibel
				double dbValue = 20 * Math.log10((double)newData[0][i]);
				fftBucketHeight = ((dbValue - MIN_DB_VALUE) / DB_SCALE) * rowsV;
				break;

			case 1:
				//Sqrt
				fftBucketHeight = (((Math.sqrt(newData[0][i])) * 2) * rowsV);
				break;
				
			case 2: default:
				//Linear
				fftBucketHeight = (newData[0][i] * 9) * rowsV;
				break;
			}


			if (barHeight < fftBucketHeight)
				barHeight = (int)fftBucketHeight;
			if (barHeight < 0f)
				barHeight = 0;

			if(i == barIndexMax[barIndex]){

				if(barHeight > rowsV)
					barHeight = rowsV;
				if(barHeight < 0)
					barHeight = 0;

				fftHistory[rotator][barIndex] = barHeight;
				setBarValue(matrix, barIndex, average(barIndex));

				rotator = ++rotator % HISTORY_SIZE;

				barHeight = 0;
				barIndex++;
			}

		}
	}

	private void setBarValue(LEDMatrix matrix, int bar, double v){
		int ySize = matrix.getMatrix().length;
		for(int j = 0; j < ySize; j++){
			if(j < ySize-v)
				matrix.getMatrix()[j][bar].setValue(LED.ANIM_COUNT-5);
			else
				matrix.getMatrix()[j][bar].setValue(0);
		}
	}

	private int average(int c){
		float val = 0f;

		for (int i = 0; i < fftHistory.length; i++) {
			val += fftHistory[i][c];
		}

		return Math.round( (val/fftHistory.length) );
	}

	public void initData(int barCount, int rowCount, JSONObject specific){
		Properties s = new Properties();

		try {
			s.load(new FileInputStream("settings.properties"));
		} catch (Exception e) {
			l.error("Could not load Settingsfile!", e);
		}

		barIndexDistribution = specific.optInt("barIndexDistribution", 1);
		barScaleType		 = specific.optInt("barScaleType", 1);

		if(barIndexDistribution > 1 || barIndexDistribution < 0)
			barIndexDistribution = 1;

		if(barScaleType > 2 || barScaleType < 0)
			barScaleType = 1;

		fftHistory = new float[HISTORY_SIZE][barCount];
		for(int i = 0; i < HISTORY_SIZE; i++)
			for(int j = 0; j < barCount; j++)
				fftHistory[i][j] = 0;

		rotator = 0;

		rowsV = rowCount;
		cols = barCount;
		barIndexMax = new int[cols];

		for(int i = 1; i < barIndexMax.length; i++){
			if(barIndexDistribution == 1){
				//Liear Bars
				barIndexMax[i-1] = (int) ((1024.0f/cols) * i);
			}else{
				//Logarithmic Bars
				barIndexMax[i] = (int)((1 - Math.log(cols - i)/Math.log(cols)) * 1024);
			}
		}
	}
}
