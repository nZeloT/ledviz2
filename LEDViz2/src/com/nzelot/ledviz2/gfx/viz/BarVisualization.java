package com.nzelot.ledviz2.gfx.viz;

import com.nzelot.ledviz2.gfx.LED;
import com.nzelot.ledviz2.gfx.LEDMatrix;

public class BarVisualization implements Visualization {

	private static final int HISTORY_SIZE = 10;

	private float[][] fftHistory;

	private int rotator;

	@Override
	public void aplyFFTData(LEDMatrix matrix, float[]... newData) {		
		if(fftHistory == null)
			initData(matrix.getMatrix()[0].length);
		
		int rowsV = matrix.getMatrix().length;
		int cols = matrix.getMatrix()[0].length;
		double fftBucketHeight = 0f;
        int barHeight = 0;
        final double minDBValue = -90;
        final double maxDBValue = 0;
        final double dbScale = (maxDBValue - minDBValue);
        int[] barIndexMax = new int[cols];
        int barIndex = 0;
        int binsPerBar = (int) ((newData[0].length+0d)/cols);

        for(int i = 1; i < barIndexMax.length; i++)
        	//Liear Bars
//        	barIndexMax[i-1] = binsPerBar * i;
        	
        	//Logarithmic Bars
        	barIndexMax[i] = (int)((1 - Math.log(cols - i)/Math.log(cols)) * newData[0].length);
        
		for(int i = 0; i < newData[0].length; i++){
			
			//Decibel
//			double dbValue = 20 * Math.log10((double)newData[0][i]);
//			fftBucketHeight = ((dbValue - minDBValue) / dbScale) * rowsV;
			
			//Sqrt
			fftBucketHeight = (((Math.sqrt(newData[0][i])) * 2) * rowsV);
			
			//Linear
//			fftBucketHeight = (newData[0][i] * 9) * rowsV;
			
            
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

	private void initData(int barCount){
		fftHistory = new float[HISTORY_SIZE][barCount];
		for(int i = 0; i < HISTORY_SIZE; i++)
			for(int j = 0; j < barCount; j++)
				fftHistory[i][j] = 0;
		rotator = 0;
	}

}
