package com.nzelot.ledviz2.ui.elements.browser;

import java.util.List;

import org.bff.javampd.playlist.Playlist;
import org.bff.javampd.song.MPDSong;
import org.json.JSONObject;

import com.nzelot.ledviz2.sound.meta.METAData;
import com.nzelot.ledviz2.ui.elements.Browser;
import com.nzelot.mpd.MPD;

public class MPDBrowser extends Browser {

	List<MPDSong> data;

	@Override
	public void init(JSONObject specific) {
		this.filter = Filter.NO_FILTER;
		heading.setText("Queue");
		enterSelection();
	}

	@Override
	public String getSelection() {
		return "" + content.getSelectedIdx();
	}

	@Override
	public void enterSelection() {
		Playlist p = MPD.getMPD().getPlaylist();

		try {
			data = p.getSongList();


			content.getEntries().clear();
			for(MPDSong s : data){
				content.addEntries(s.getTitle() + " by " + s.getArtistName());
			}
			
			content.setSelectedIdx(data.indexOf(p.getCurrentSong()));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void updateSelection(METAData d) {
		enterSelection();
	}
	
	@Override
	public boolean isPlayableSelected() {
		return true;
	}

}
