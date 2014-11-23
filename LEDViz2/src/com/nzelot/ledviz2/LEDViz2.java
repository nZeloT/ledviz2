package com.nzelot.ledviz2;

import org.json.JSONObject;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nzelot.ledviz2.config.ConfigParser;
import com.nzelot.ledviz2.gfx.LED;
import com.nzelot.ledviz2.gfx.LEDMatrix;
import com.nzelot.ledviz2.gfx.core.ColorUtils;
import com.nzelot.ledviz2.gfx.core.GLFont;
import com.nzelot.ledviz2.gfx.core.LWJGL;
import com.nzelot.ledviz2.gfx.core.Painter;
import com.nzelot.ledviz2.gfx.core.TextureLoader;
import com.nzelot.ledviz2.gfx.res.ResourceManager;
import com.nzelot.ledviz2.gfx.res.loader.CLASSLoader;
import com.nzelot.ledviz2.gfx.res.loader.JAVALoader;
import com.nzelot.ledviz2.gfx.res.loader.PNGLoader;
import com.nzelot.ledviz2.gfx.res.loader.TTFLoader;
import com.nzelot.ledviz2.gfx.viz.Visualization;
import com.nzelot.ledviz2.sound.Player;
import com.nzelot.ledviz2.sound.meta.METAData;
import com.nzelot.ledviz2.ui.Layer;
import com.nzelot.ledviz2.ui.UI;
import com.nzelot.ledviz2.ui.elements.FileBrowser;
import com.nzelot.ledviz2.ui.elements.ImageButton;
import com.nzelot.ledviz2.ui.elements.PopOver;
import com.nzelot.ledviz2.ui.elements.ProgressBar;
import com.nzelot.ledviz2.ui.elements.Text;

public class LEDViz2{

	private final Logger l = LoggerFactory.getLogger(LEDViz2.class);

	private int DISPLAY_WIDTH;
	private int DISPLAY_HEIGHT;
	private boolean FULL_SCREEN;

	private int TARGET_FPS;

	private int KEY_TIME_OUT;
	private int MOUSE_KEY_OUT;
	private int MOUSE_HIDE_OUT;

	private String START_PATH;

	private int keyboardDelay;

	private int mouseHideDelay;

	private Player player;
	private Visualization viz;

	private Painter painter;
	private LEDMatrix matrix;
	private UI ui;
	private ProgressBar progress;
	private Text text;
	private FileBrowser list;
	private PopOver overlay;
	private ImageButton btnPlay;

	private boolean ligthsOn;

	public static void main(String[] args) throws Exception {

		System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "INFO");

		ResourceManager.addLoader("png", new PNGLoader());
		ResourceManager.addLoader("ttf", new TTFLoader());
		ResourceManager.addLoader("java", new JAVALoader());
		ResourceManager.addLoader("class", new CLASSLoader());

		new LEDViz2();
	}

	public LEDViz2(){
		l.debug("Init...");
		
		try {
			init();
		} catch (Exception e) {
			l.error(e.toString());
			e.printStackTrace();
		}

		l.debug("Entering game loop ...");
		boolean run = true;
		while(run){
			if (Display.isCloseRequested() || Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
				run = false;
			}

			update();

			draw();

			Display.update();
			Display.sync(TARGET_FPS);
		}

		l.debug("Exiting ...");
		player.exit();
		GLFont.unloadFonts();
		TextureLoader.unloadTextures();
		Display.destroy();
	}

	private void init() throws Exception{
		ConfigParser.get().init();
		ClassLoader cLoader = LEDViz2.class.getClassLoader();
		
		JSONObject specific = null;
		String className = "";
		Class<?> clazz = null;
		
		DISPLAY_WIDTH	= ConfigParser.get().visualization().getJSONObject("renderer").optInt("resX", 1024);
		DISPLAY_HEIGHT	= ConfigParser.get().visualization().getJSONObject("renderer").optInt("resY", 720);
		FULL_SCREEN		= ConfigParser.get().visualization().getJSONObject("renderer").optBoolean("fullscreen", false);
		TARGET_FPS		= ConfigParser.get().overall().optInt("targetFPS", 60);
		KEY_TIME_OUT	= ConfigParser.get().overall().getJSONObject("timeOuts").optInt("key",10);
		MOUSE_KEY_OUT	= ConfigParser.get().overall().getJSONObject("timeOuts").optInt("mouse",10);
		MOUSE_HIDE_OUT	= ConfigParser.get().overall().getJSONObject("timeOuts").optInt("mouseHide",420);
		
//		START_PATH     = s.getProperty("org.nZeloT.ledviz2.StartPath", System.getProperty("user.dir"));
		
		//Config walkthrough
		//1. Visualization
		//1.1 renderer		
		LWJGL.init(DISPLAY_WIDTH, DISPLAY_HEIGHT, FULL_SCREEN);
		Display.setTitle("LEDViz2");
		ResourceManager.loadResources();
		
		painter = new Painter();

		specific			= 	ConfigParser.get().visualization().getJSONObject("renderer").getJSONObject("specific");
		matrix = new LEDMatrix(DISPLAY_WIDTH, DISPLAY_HEIGHT, specific);
		matrix.setOverallValue(LED.ANIM_COUNT-5);
		
		//1.2 visualizer
		className			=	ConfigParser.get().visualization().getJSONObject("visualizer").getString("class");
		specific			=	ConfigParser.get().visualization().getJSONObject("visualizer").getJSONObject("specific");
		clazz				= 	cLoader.loadClass(className);
		viz = (Visualization)clazz.newInstance();
		viz.initData(matrix.getMatrix()[0].length, matrix.getMatrix().length, specific);
		
		
		//2. player
		className			=	ConfigParser.get().player().getString("class");
		specific			=	ConfigParser.get().player().getJSONObject("specific");
		clazz				= 	cLoader.loadClass(className);
		player = (Player)clazz.newInstance();
		player.init(specific);

		//3. UI
		ui = new UI();

		//	Rect shadow = new Rect(0, Display.getHeight()-60, Display.getWidth(), 60, new Color[]{new Color(0, 0, 0, 0), new Color(0,0,0,0), Color.BLACK, Color.BLACK});
		progress = new ProgressBar(0, (int) (Display.getHeight()-65), Display.getWidth(), 10);
		text = new Text(10, Display.getHeight()-60, 20);
		overlay = new PopOver((Display.getWidth()-100)/2, (Display.getHeight()-100)/2, 100, 100, 60, "res/textures/pause");
		btnPlay = new ImageButton((int) (Display.getWidth()/2.0f-25), Display.getHeight()-70-25, 50, 50, "res/textures/play_alpha");

//		className			=	ConfigParser.get().ui().getJSONObject("list").getString("class");
//		specific			=	ConfigParser.get().ui().getJSONObject("list").getJSONObject("specific");
//		clazz				= 	cLoader.loadClass(className);
		list = new FileBrowser(10, 5, 350, Display.getHeight()-100, START_PATH, new FileBrowser.FileFilter("mp3", "mp4", "m4a"));
//		list = (Browser)clazz.newInstance();
//		list.init(10, 5, 350, Display.getHeight()-100, specific);
		
		
		ui.addElements(new Layer(progress, btnPlay, text, overlay), new Layer(list));

		painter.addElements(matrix, ui);

		updateColor();

		keyboardDelay = 0;

		ligthsOn = false;

		Keyboard.enableRepeatEvents(true);
	}

	private void handleInput(){

		if(Mouse.getDX() < 3 && Mouse.getDY() < 3){
			mouseHideDelay++;

			if(mouseHideDelay == MOUSE_HIDE_OUT){
				mouseHideDelay = 0;
				Mouse.setGrabbed(true);
				ui.setVisible(false);
			}
		}else{
			mouseHideDelay = 0;
			Mouse.setGrabbed(false);
			ui.setVisible(true);
		}

		if(keyboardDelay == 0){
			while(Keyboard.next()){
				int next = Keyboard.getEventKey();
				if(Keyboard.getEventKeyState()){
					ui.setVisible(true);
					switch(next){
					case Keyboard.KEY_SPACE:
						if(player.isPlaying()){
							player.pause();
							overlay.reset();
							overlay.setTex("res/textures/pause");
							btnPlay.setTex("res/textures/play_alpha");
							overlay.popOut();
						}else{
							player.play();
							overlay.reset();
							overlay.setTex("res/textures/play");
							btnPlay.setTex("res/textures/pause_alpha");
							overlay.popOut();
						}
						break;

					case Keyboard.KEY_DOWN:
						if(ui.getLayer(1).isVisible())
							list.increseSelectedIdx();
						break;

					case Keyboard.KEY_UP:
						if(ui.getLayer(1).isVisible())
							list.decreaseSelectedIdx();
						break;

					case Keyboard.KEY_RIGHT:
						if(ui.getLayer(1).isVisible() && list.isFileSelected()){
							player.load(list.getSelection());
							METAData meta = player.getMetaData();
							text.setText(  (meta.getTitle().isEmpty() ? meta.getFileName() : (meta.getTitle() + (meta.getArtist().isEmpty() ? "" : (" by " + meta.getArtist() + (meta.getAlbum().isEmpty() ? "" : " from " + meta.getAlbum()) ) ) ))  );

							if(meta.getAlbumCover() != null){
								matrix.setColor(ColorUtils.generate(matrix.getMatrix()[0].length, matrix.getMatrix().length, meta.getAlbumCover()));
							}else{
								matrix.setColorToDef();
							}

							updateColor();
						}else{
							list.enterDir();
						}
						break;

					case Keyboard.KEY_TAB:
						ui.getLayer(1).setVisible(!ui.getLayer(1).isVisible());
						break;

					case Keyboard.KEY_L:
						ligthsOn = !ligthsOn;
						if(ligthsOn)
							matrix.setOverallValue(0);
						else
							matrix.setOverallValue(LED.ANIM_COUNT-5);
						break;
					}

					keyboardDelay = KEY_TIME_OUT;
					mouseHideDelay = 0;
				}
			}
		}

		if(keyboardDelay > 0)
			keyboardDelay--;
	}

	private void updateColor(){
		progress.setBgColor(matrix.getMatrix()[matrix.getMatrix().length-1][matrix.getMatrix()[0].length/2].getCol().darker());
		list.setBgColor(progress.getBgColor());
		btnPlay.setBgColor(progress.getBgColor());
	}

	private void update(){
		handleInput();

		if(player.hasNewData() && !ligthsOn)
			viz.aplyFFTData(matrix, player.getSpectrumData());

		progress.setProgress((player.getPosition()+0d) / player.getDuration());
	}

	private void draw(){
		painter.repaint();
	}
}
