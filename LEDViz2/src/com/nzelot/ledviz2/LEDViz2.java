package com.nzelot.ledviz2;

import java.awt.Color;
import java.io.FileInputStream;
import java.util.Properties;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nzelot.ledviz2.gfx.LED;
import com.nzelot.ledviz2.gfx.LEDMatrix;
import com.nzelot.ledviz2.gfx.core.ColorUtils;
import com.nzelot.ledviz2.gfx.core.LWJGL;
import com.nzelot.ledviz2.gfx.core.Painter;
import com.nzelot.ledviz2.gfx.res.ResourceManager;
import com.nzelot.ledviz2.gfx.res.loader.CLASSLoader;
import com.nzelot.ledviz2.gfx.res.loader.JAVALoader;
import com.nzelot.ledviz2.gfx.res.loader.PNGLoader;
import com.nzelot.ledviz2.gfx.res.loader.TTFLoader;
import com.nzelot.ledviz2.gfx.viz.BarVisualization;
import com.nzelot.ledviz2.gfx.viz.Visualization;
import com.nzelot.ledviz2.sound.Player;
import com.nzelot.ledviz2.sound.meta.METAData;
import com.nzelot.ledviz2.sound.player.BASSPlayer;
import com.nzelot.ledviz2.ui.UI;
import com.nzelot.ledviz2.ui.elements.FileBrowser;
import com.nzelot.ledviz2.ui.elements.PopOver;
import com.nzelot.ledviz2.ui.elements.ProgressBar;
import com.nzelot.ledviz2.ui.elements.Rect;
import com.nzelot.ledviz2.ui.elements.Text;
import com.nzelot.ledviz2.utils.FPSCounter;

public class LEDViz2{
    
    private final Logger l = LoggerFactory.getLogger(LEDViz2.class);

    private int MATRIX_WIDTH;
    private int MATRIX_HEIGTH;
    private boolean FULL_SCREEN;

    private int LEDSize;
    private Color LEDBASECOLOR;

    private int TARGET_FPS;

    private int KEY_TIME_OUT;

    private String START_PATH;


    private int inputDelay;

    private Player player;

    private Painter painter;
    private LEDMatrix matrix;
    private UI ui;
    private ProgressBar progress;
    private Text text;
    private FileBrowser list;
    private PopOver overlay;
    private Visualization viz;

    private FPSCounter fps;

    public static void main(String[] args) throws Exception {
	
	System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "TRACE");

	ResourceManager.addLoader("png", new PNGLoader());
	ResourceManager.addLoader("ttf", new TTFLoader());
	ResourceManager.addLoader("java", new JAVALoader());
	ResourceManager.addLoader("class", new CLASSLoader());

	new LEDViz2();
    }

    public LEDViz2(){
	l.debug("Init...");
	init();

	l.debug("Entering game loop ...");
	boolean run = true;
	while(run){
	    if (Display.isCloseRequested() || Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
		run = false;
	    }

	    update();

	    draw();

	    Display.setTitle(fps.updateFPS());

	    Display.update();
	    Display.sync(TARGET_FPS);
	}

	l.debug("Exiting ...");
	player.exit();
	Display.destroy();
    }

    private void init(){
	initVariables();

	LWJGL.init(MATRIX_WIDTH*LEDSize, MATRIX_HEIGTH*LEDSize, FULL_SCREEN);
	ResourceManager.loadResources();

	painter = new Painter();

	//Create 1 LED and draw it
	matrix = new LEDMatrix(MATRIX_WIDTH, MATRIX_HEIGTH, LEDSize, LEDBASECOLOR);
	matrix.setOverallValue(LED.ANIM_COUNT-5);

	viz = new BarVisualization();
	viz.init(0);

	initUI();

	painter.addElements(matrix, ui);

	player = new BASSPlayer();
	player.init(1024, 25);

	updateColor();

	fps = new FPSCounter();

	inputDelay = 0;
	
	Keyboard.enableRepeatEvents(true);
    }

    private void initVariables(){
	Properties s = new Properties();
	
	try {
	    s.load(new FileInputStream("settings.properties"));
	} catch (Exception e) {
	    l.error("Could not load Settingsfile!", e);
	}
	
	MATRIX_WIDTH   = Integer.parseInt(s.getProperty("org.nZeloT.ledviz2.MatrixWidth", "64"));
	MATRIX_HEIGTH  = Integer.parseInt(s.getProperty("org.nZeloT.ledviz2.MatrixHeigth", "36"));
	LEDSize        = Integer.parseInt(s.getProperty("org.nZeloT.ledviz2.LEDSize", "20"));
	FULL_SCREEN    = Boolean.parseBoolean(s.getProperty("org.nZeloT.ledviz2.Fullscreen", "false"));
	String[] in    = s.getProperty("org.nZeloT.ledviz2.LEDBaseColor", "255,165,0").split(",");
	LEDBASECOLOR   = new Color(Integer.parseInt(in[0]), Integer.parseInt(in[1]), Integer.parseInt(in[2]));
	TARGET_FPS     = Integer.parseInt(s.getProperty("org.nZeloT.ledviz2.TargetFPS", "60"));
	KEY_TIME_OUT   = Integer.parseInt(s.getProperty("org.nZeloT.ledviz2.KeyTimeOut", "5"));
	START_PATH     = s.getProperty("org.nZeloT.ledviz2.StartPath", "F:/Musik");
	
    }

    private void initUI(){
	ui = new UI();

	Rect shadow = new Rect(0, Display.getHeight()-60, Display.getWidth(), 60, new Color[]{new Color(0, 0, 0, 0), new Color(0,0,0,0), Color.BLACK, Color.BLACK});
	progress = new ProgressBar(0, Display.getHeight()-Math.min(LEDSize, 10), Display.getWidth(), Math.min(LEDSize, 10));
	text = new Text(10, Display.getHeight()-35, 20);
	list = new FileBrowser(10, 10, 350, Display.getHeight()-80, START_PATH);
	overlay = new PopOver((Display.getWidth()-100)/2, (Display.getHeight()-100)/2, 100, 100, 60, "res/textures/pause");

	ui.addElements(shadow, progress, text, list, overlay);
    }

    private void handleInput(){
	if(inputDelay == 0){
	    while(Keyboard.next()){
		int next = Keyboard.getEventKey();
		if(Keyboard.getEventKeyState()){
		    switch(next){
		    case Keyboard.KEY_SPACE:
			if(player.isPlaying()){
			    player.pause();
			    overlay.reset();
			    overlay.setTex("res/textures/pause");
			    overlay.popOut();
			}else{
			    player.play();
			    overlay.reset();
			    overlay.setTex("res/textures/play");
			    overlay.popOut();
			}
			break;

		    case Keyboard.KEY_DOWN:
			list.increseSelectedIdx();
			break;

		    case Keyboard.KEY_UP:
			list.decreaseSelectedIdx();
			break;

		    case Keyboard.KEY_RIGHT:
			if(list.isFileSelected()){
			    player.load(list.getSelection());
			    METAData meta = player.getMetaData();
			    text.setText(  (meta.getTitle().isEmpty() ? meta.getFileName() : (meta.getTitle() + (meta.getArtist().isEmpty() ? "" : (" by " + meta.getArtist() + (meta.getAlbum().isEmpty() ? "" : " from " + meta.getAlbum()) ) ) ))  );

			    if(meta.getAlbumCover() != null){
				matrix.setColor(ColorUtils.generate(MATRIX_WIDTH, MATRIX_HEIGTH, meta.getAlbumCover()));
			    }else{
				matrix.setColor(LEDBASECOLOR);
			    }

			    updateColor();
			}else{
			    list.enterDir();
			}
			break;

		    case Keyboard.KEY_TAB:
			ui.setVisible(!ui.isVisible());
			break;
		    }

		    inputDelay = KEY_TIME_OUT;
		}
	    }
	}

	if(inputDelay > 0)
	    inputDelay--;
    }

    private void updateColor(){
	progress.setBgColor(matrix.getMatrix()[matrix.getMatrix().length-1][matrix.getMatrix()[0].length/2].getCol().darker());
	list.setBgColor(progress.getBgColor());
    }

    private void update(){
	handleInput();

	if(player.hasNewData())
	    viz.aplyFFTData(matrix, player.getSpectrumData());

	progress.setProgress((player.getPosition()+0d) / player.getDuration());
    }

    private void draw(){
	painter.repaint();
    }
}
