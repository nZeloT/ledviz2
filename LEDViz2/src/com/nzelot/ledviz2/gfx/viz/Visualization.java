package com.nzelot.ledviz2.gfx.viz;

import org.json.JSONObject;

import com.nzelot.ledviz2.gfx.LEDMatrix;

public interface Visualization {
    public void aplyFFTData(LEDMatrix matrix, float[]... newData);
    public void initData(int sizeX, int sizeY, JSONObject specific);
}
