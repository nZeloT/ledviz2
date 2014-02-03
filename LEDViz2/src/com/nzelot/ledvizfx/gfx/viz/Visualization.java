package com.nzelot.ledvizfx.gfx.viz;

import com.nzelot.ledvizfx.gfx.LEDMatrix;

public interface Visualization {
    public void init(int minValue);
    public void aplyFFTData(LEDMatrix matrix, float[]... newData);
}
