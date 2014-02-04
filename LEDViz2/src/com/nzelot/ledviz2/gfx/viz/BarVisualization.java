package com.nzelot.ledviz2.gfx.viz;

import com.nzelot.ledviz2.gfx.LED;
import com.nzelot.ledviz2.gfx.LEDMatrix;

public class BarVisualization implements Visualization {

    private static final int HISTORY_SIZE = 10;

    private float[][] fftHistory;

    private double minValue;

    private int rotator;

    @Override
    public void init(int minValue) {
	this.minValue = minValue;
    }

    @Override
    public void aplyFFTData(LEDMatrix matrix, float[]... newData) {
	if(fftHistory == null)
	    initData(matrix.getMatrix()[0].length);

	float y = 0;

	//Logarithmic, acumulate & average bins
	if(newData != null && newData.length > 0 && newData[0] != null){
	    
	    int b0 = 0;
	    int cols = matrix.getMatrix()[0].length;
	    int rowsV = matrix.getMatrix().length;

	    for(int x = 0; x < cols; x++) {
		int b1 = (int)Math.pow(2, x*10.0/(cols-1));
		if(b1 > 1023) {
		    b1 = 1023;
		}
		if(b1 <= b0) {
		    b1 = b0+1;		//Make sure it uses at least 1 FFT bin
		}

		int sc = 10+b1-b0;

		float sum = 0;
		for(; b0 < b1; b0++) {
		    sum += newData[0][1+b0]-minValue;
		}

		y = (float)( (Math.sqrt(sum/Math.log10(sc))*1.5f*rowsV) );	//Scale it

		if(y < 0)
		    y = 0;
		if(y > rowsV) {
		    y = rowsV;//Cap it
		}

		fftHistory[rotator][x] = 2*y;

		setBarValue(matrix, x, average(x));
		
		rotator++;
		
		if(rotator >= HISTORY_SIZE)
		    rotator = 0;
	    }
	}else{
	    for(int i = 0; i < matrix.getMatrix()[0].length; i++){
		setBarValue(matrix, i, average(i));
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
