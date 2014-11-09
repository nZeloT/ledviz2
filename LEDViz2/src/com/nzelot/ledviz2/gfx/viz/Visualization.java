package com.nzelot.ledviz2.gfx.viz;

import com.nzelot.ledviz2.gfx.LEDMatrix;

public interface Visualization {
    public void aplyFFTData(LEDMatrix matrix, float[]... newData);
}
