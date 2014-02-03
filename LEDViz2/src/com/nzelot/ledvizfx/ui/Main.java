package com.nzelot.ledvizfx.ui;

import java.awt.Color;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import com.nzelot.ledvizfx.config.Settings;
import com.nzelot.ledvizfx.gfx.ColorGenerator;
import com.nzelot.ledvizfx.gfx.LED;
import com.nzelot.ledvizfx.gfx.LEDMatrix;
import com.nzelot.ledvizfx.gfx.res.ResourceManager;
import com.nzelot.ledvizfx.gfx.res.loader.CLASSLoader;
import com.nzelot.ledvizfx.gfx.res.loader.JAVALoader;
import com.nzelot.ledvizfx.gfx.res.loader.PNGLoader;
import com.nzelot.ledvizfx.gfx.res.loader.TTFLoader;
import com.nzelot.ledvizfx.gfx.viz.BarVisualization;
import com.nzelot.ledvizfx.gfx.viz.Visualization;
import com.nzelot.ledvizfx.sound.Player;
import com.nzelot.ledvizfx.sound.meta.METAData;
import com.nzelot.ledvizfx.sound.player.BASSPlayer;
import com.nzelot.ledvizfx.ui.elements.FileBrowser;
import com.nzelot.ledvizfx.ui.elements.PopOver;
import com.nzelot.ledvizfx.ui.elements.ProgressBar;
import com.nzelot.ledvizfx.ui.elements.Rect;
import com.nzelot.ledvizfx.ui.elements.Text;
import com.nzelot.ledvizfx.ui.fx.Painter;

public class Main{

    public static void main(String[] args) throws Exception {

	final int MATRIX_WIDTH   = Integer.parseInt(Settings.getItem("MatrixWidth"));
	final int MATRIX_HEIGTH  = Integer.parseInt(Settings.getItem("MatrixHeigth"));
	final int LEDSize        = Integer.parseInt(Settings.getItem("LEDSize"));
	final boolean FULL_SREEN = Boolean.parseBoolean(Settings.getItem("Fullscreen"));
	final Color LEDBASECOLOR = parseColor();

	LWJGL.init(MATRIX_WIDTH*LEDSize, MATRIX_HEIGTH*LEDSize, FULL_SREEN);

	Painter painter = new Painter();

	//Init Resources
	ResourceManager.addLoader("png", new PNGLoader());
	ResourceManager.addLoader("ttf", new TTFLoader());
	ResourceManager.addLoader("java", new JAVALoader());
	ResourceManager.addLoader("class", new CLASSLoader());

	ResourceManager.loadResources();

	//Create 1 LED and draw it
	LEDMatrix matrix = new LEDMatrix(MATRIX_WIDTH, MATRIX_HEIGTH, LEDSize, LEDBASECOLOR);
	matrix.setOverallValue(LED.ANIM_COUNT-5);

	Rect shadow = new Rect(0, Display.getHeight()-60, Display.getWidth(), 60, new Color[]{new Color(0, 0, 0, 0), new Color(0,0,0,0), Color.BLACK, Color.BLACK});
	ProgressBar progress = new ProgressBar(0, Display.getHeight()-Math.min(LEDSize, 10), Display.getWidth(), Math.min(LEDSize, 10));
	Text text = new Text(10, Display.getHeight()-35, 20);
	FileBrowser list = new FileBrowser(10, 10, 300, Display.getHeight()-80, "F:/Musik");
	PopOver overlay = new PopOver(590, 310, 100, 100, 60, "textures/pause");

	Visualization viz = new BarVisualization();
	viz.init(0);

	painter.addElements(matrix, shadow, progress, text, list, overlay);

	Player player = new BASSPlayer();
	player.init(1024, 25);

	progress.setBgColor(matrix.getMatrix()[matrix.getMatrix().length-1][matrix.getMatrix()[0].length/2].getCol().darker());
	list.setBgColor(progress.getBgColor());

	player.play();

	boolean run = true;
	int inputDelay = 0;

	while(run){
	    if (Display.isCloseRequested() || Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
		run = false;
	    }

	    if(Keyboard.isKeyDown(Keyboard.KEY_SPACE) && inputDelay == 0){
		if(player.isPlaying()){
		    player.pause();
		    overlay.reset();
		    overlay.setTex("textures/pause");
		    overlay.popOut();
		    inputDelay = 3;
		}else{
		    player.play();
		    overlay.reset();
		    overlay.setTex("textures/play");
		    overlay.popOut();
		    inputDelay = 3;
		}
	    }

	    if(Keyboard.isKeyDown(Keyboard.KEY_DOWN) && inputDelay == 0){
		list.increseSelectedIdx();
		inputDelay = 3;
	    }
	    
	    if(Keyboard.isKeyDown(Keyboard.KEY_UP) && inputDelay == 0){
		list.decreaseSelectedIdx();
		inputDelay = 3;
	    }

	    if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT) && inputDelay == 0){
		if(list.isFileSelected()){
		    player.load(list.getSelection());
		    METAData meta = player.getMetaData();
		    text.setText(  (meta.getTitle().isEmpty() ? meta.getFileName() : (meta.getTitle() + (meta.getArtist().isEmpty() ? "" : (" by " + meta.getArtist() + (meta.getAlbum().isEmpty() ? "" : " from " + meta.getAlbum()) ) ) ))  );
		    
		    if(meta.getAlbumCover() != null){
			matrix.setColor(ColorGenerator.generate(MATRIX_WIDTH, MATRIX_HEIGTH, meta.getAlbumCover()));
		    }else{
			matrix.setColor(LEDBASECOLOR);
		    }

		    progress.setBgColor(matrix.getMatrix()[matrix.getMatrix().length-1][matrix.getMatrix()[0].length/2].getCol().darker());
		    list.setBgColor(progress.getBgColor());
		}else{
		    list.enterDir();
		}
		inputDelay = 3;
	    }

	    viz.aplyFFTData(matrix, player.hasNewData() ? player.getSpectrumData() : null);

	    progress.setProgress((player.getPosition()+0d) / player.getDuration());

	    painter.repaint();

	    Display.update();
	    Display.sync(60);

	    if(inputDelay > 0)
		inputDelay--;
	}

	player.stop();
	Display.destroy();
    }

    public static Color parseColor(){
	String[] in = Settings.getItem("LEDBaseColor").split(",");
	return new Color(Integer.parseInt(in[0]), Integer.parseInt(in[1]), Integer.parseInt(in[2]));	
    }
}
