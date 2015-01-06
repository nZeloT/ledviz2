package com.nzelot.ledviz2.controller.listener;

import org.json.JSONObject;

import com.nzelot.ledviz2.controller.ChangeListener;
import com.nzelot.ledviz2.controller.ControllerEventType;

public class DummyListener extends ChangeListener {

	@Override
	public void run() {
		fireEvent(ControllerEventType.CET_START);
	}

	@Override
	protected boolean init(JSONObject specific) {
		return true;
	}

}
