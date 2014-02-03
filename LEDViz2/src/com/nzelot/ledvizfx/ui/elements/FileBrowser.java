package com.nzelot.ledvizfx.ui.elements;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;

public class FileBrowser extends UIElement {

    private List<String> content;

    private Text currentDir;

    private String curDirPath;

    public FileBrowser(int x, int y, int width, int heigth, String startPath) {
	super(x, y, width, heigth);

	content = new List<String>(x, y+20, width, heigth-20, 15);
	currentDir = new Text(x, y, 18);

	enterDir(startPath);
    }

    public void increseSelectedIdx(){
	content.increseSelectedIdx();
    }

    public void decreaseSelectedIdx(){
	content.decreaseSelectedIdx();
    }

    public String getSelection(){
	return curDirPath + "/" + content.getSelectedEntry();
    }

    public void enterDir(){
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

	    for(String s : dirs)
		content.addEntries(s);

	    for(String s : files)
		content.addEntries(s);

	    currentDir.setText(dir.replace("\\", "/"));
	    curDirPath = f.getAbsolutePath();
	}else{
	    //Root
	    for(File s : File.listRoots())
		content.addEntries(s.getAbsolutePath());
	    currentDir.setText("Root");
	    curDirPath = "";
	}

	content.setSelectedIdx(0);
    }

    @Override
    public void setBgColor(Color bgColor) {
	super.setBgColor(bgColor);
	if(content != null)
	    content.setBgColor(bgColor);
    }

    public boolean isFileSelected(){
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
    public void draw() {
	currentDir.draw();
	content.draw();
    }
}
