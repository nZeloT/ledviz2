package com.nzelot.ledviz2.ui.elements.browser;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nzelot.ledviz2.sound.meta.METAData;
import com.nzelot.ledviz2.ui.elements.Browser;

public class FileBrowser extends Browser {

	private String curDirPath;

	public void init(JSONObject specific){
		
		String startPath	=	specific.getString("path");
		JSONArray filter	=	specific.getJSONArray("filter");
		
		this.filter = new Filter();
		for(int i = 0; i < filter.length(); i++){
			String end = filter.getString(i);
			if(!end.equals("NO_FILTER"))
				this.filter.addEnd(end);
			else{
				this.filter = Filter.NO_FILTER;
				break;
			}
		}
		
		enterDir(startPath);
	}

	public String getSelection(){
		return curDirPath + "/" + content.getSelectedEntry();
	}

	public void enterSelection(){
		String selDir = content.getSelectedEntry();

		//is it a ".." entry?
		if(selDir.equals("..")){
			File f = new File(curDirPath);
			if(f.getParentFile() != null)
				enterDir(f.getParent());
			else
				enterDir("");
		}else{
			File f = new File(curDirPath + "/" + selDir);
			if(f.isDirectory())
				enterDir(curDirPath + "/" + selDir);
		}
	}

	private void enterDir(String dir){
		//0. clear all entries
		content.getEntries().clear();

		//1. Does the new Dir have a Parent?
		if(!dir.isEmpty()){
			File f = new File(dir);
			content.addEntries("..");

			//2. Add all files
			File[] list = f.listFiles();
			ArrayList<String> dirs = new ArrayList<String>();
			ArrayList<String> files = new ArrayList<String>();

			for(File s : list){
				if(s.isHidden())
					continue;
				if(s.isDirectory())
					dirs.add(s.getName());
				else
					files.add(s.getName());
			}

			content.addEntries(dirs.toArray(new String[0]));

			for(String s : files)
				if(filter.check(s.substring(s.lastIndexOf(".")+1)))
					content.addEntries(s);

			heading.setText(dir.replace("\\", "/"));
			curDirPath = f.getAbsolutePath();
		}else{
			//Root
			for(File s : File.listRoots())
				content.addEntries(s.getAbsolutePath());
			heading.setText("Root");
			curDirPath = "";
		}

		content.setSelectedIdx(0);
	}

	public boolean isPlayableSelected(){
		String selDir = content.getSelectedEntry();

		if(selDir.equals("..")){
			return false;
		}else{
			File f = new File(curDirPath +"/"+ selDir);
			if(f.isDirectory())
				return false;
			else
				return true;
		}
	}
	
	@Override
	public void updateSelection(METAData d) {
		//Not required here
		//at least at the moment
	}
	
}
