package com.nzelot.ledviz2.gfx.core;
import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * A class to render Text
 * @author nZeloT
 *
 */
public class GLFont {

	public enum Style {
		PLAIN(0),
		ITALIC(1),
		BOLD(2),
		BOLD_ITALIC(3);

		private Style(int n) {
			this.n = n;
		}

		public int getN() {
			return n;
		}

		public static Style fromAWTStyle(int awtStyle){
			switch(awtStyle){
			case java.awt.Font.PLAIN:
				return Style.BOLD;

			case java.awt.Font.ITALIC:
				return Style.ITALIC;

			case java.awt.Font.BOLD:
				return Style.BOLD;

			case java.awt.Font.BOLD | java.awt.Font.ITALIC:
				return Style.BOLD_ITALIC;

			default:
				return Style.PLAIN;
			}
		}

		public static int toAWTStyle(Style s){
			switch(s){
			case PLAIN:
				return java.awt.Font.PLAIN;

			case ITALIC:
				return java.awt.Font.ITALIC;

			case BOLD:
				return java.awt.Font.BOLD;

			case BOLD_ITALIC:
				return java.awt.Font.BOLD | java.awt.Font.ITALIC;

			default:
				return java.awt.Font.PLAIN;
			}
		}

		private final int n;
	}

	private static HashMap<FontCharacteristics, GLFont> fonts = new HashMap<FontCharacteristics, GLFont>();

	public static GLFont getFont(java.awt.Font f, Color col){
		FontCharacteristics tmp = new GLFont().new FontCharacteristics();
		tmp.name  = f.getName();
		tmp.c     = col;
		tmp.size  = f.getSize();
		tmp.style = Style.fromAWTStyle(f.getStyle());

		if(fonts.containsKey(tmp)){
			return fonts.get(tmp);
		}else{

			GLFont font = load(f, col);
			fonts.put(tmp, font);
			return font;
		}
	}

	public static GLFont getFont(File font, float size, Style style, Color col){
		java.awt.Font f = null;
		try {
			f = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, font);
		} catch (Exception e) {
			e.printStackTrace();
		}

		f = f.deriveFont(size);
		f = f.deriveFont(Style.toAWTStyle(style));

		return getFont(f, col);
	}

	public static GLFont getFont(String fontName, int size, Style style, Color col){
		java.awt.Font f = new java.awt.Font(fontName, Style.toAWTStyle(style), size);

		return getFont(f, col);
	}

	public static void unloadFonts(){
		fonts.clear();
	}

	private static GLFont load(java.awt.Font f, Color col){
		GLFont ret = new GLFont();
		ret.chars.clear();

		ret.characteristics.name  = f.getName();
		ret.characteristics.size  = f.getSize();
		ret.characteristics.style = Style.fromAWTStyle(f.getStyle());
		ret.characteristics.c     = col;

		BufferedImage tmp = new BufferedImage(512, 512, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = tmp.createGraphics();
		g.setFont(f);

		int descent = g.getFontMetrics(f).getMaxDescent();
		int maxWidth = g.getFontMetrics(f).getMaxAdvance();
		ret.heigth = g.getFontMetrics(f).getHeight();

		int imgSize = Math.max(maxWidth, ret.heigth);

		HashMap<Integer, BufferedImage> imgs = new HashMap<Integer, BufferedImage>();

		for (char c = 0x00; c <= 0xFF; c++){
			if (f.canDisplay(c)){
				Rectangle2D rect = f.getStringBounds("" + c, g.getFontRenderContext());

				if(rect.getWidth() > 0){
					BufferedImage img = new BufferedImage(imgSize, imgSize, BufferedImage.TYPE_INT_ARGB);
					Graphics2D imgGc = img.createGraphics();
					imgGc.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					imgGc.setColor(col);
					imgGc.setFont(f);
					imgGc.drawString("" + c, 0, (int) rect.getHeight() - descent);

					Character cha = ret.new Character();
					cha.c = c;
					cha.width = imgGc.getFontMetrics(f).charWidth(c);

					ret.chars.put((int) c, cha);
					imgs.put((int)c, img);
				}
			}
		}

		//----------------------------------
		int dimNeeded = (int) (Math.sqrt(ret.chars.size())+1);


		//Generate Textures with a base of 2 e.g. 32, 64, 128, 256, 512, 1024, ...
		int base = 32;
		while(dimNeeded*imgSize > base)
			base *= 2;

		int x = 0;
		int y = 0;
		int n = 0;

		BufferedImage result = new BufferedImage(base, base, BufferedImage.TYPE_INT_ARGB);
		Graphics2D resGc = result.createGraphics();

		for(Entry<Integer, BufferedImage> i : imgs.entrySet()){
			resGc.drawImage(i.getValue(), x*imgSize, y*imgSize, null);

			ret.chars.get(i.getKey()).n = n;

			x++;
			n++;
			if(x >= dimNeeded){
				x = 0;
				y++;
			}
		}

		Texture t = TextureLoader.loadTexture(result);
		ret.s = new Sprite(t, dimNeeded, dimNeeded, imgSize);

		return ret;
	}

	/**
	 * Draw the given String of text at the specified x and y coordinates
	 * @param x the given start x-Value
	 * @param y the given start y-Value (measured from the top)
	 * @param text the text to draw on screen
	 */
	public void drawString(int x, int y, String text){
		glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
		glColor4f(characteristics.c.getRed()/255.0f, characteristics.c.getGreen()/255.0f, characteristics.c.getBlue()/255.0f, characteristics.c.getAlpha()/255.0f);
		s.beginRender();
		int xPos = x;
		for(int i = 0; i < text.length(); i++){
			if(chars.containsKey((int)text.charAt(i))){
				Character c = chars.get((int)text.charAt(i));
				s.renderFrame(xPos, y+heigth, c.n);
				xPos += c.width;
			}else{
				System.out.println("Unknown Char: " + text.charAt(i) + " at Index " + i);
			}
		}
		s.endRender();
		glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_DECAL);
	}

	/**
	 * Used to determine the space the given text would need to render
	 * @param text The text to determine the bounds for
	 * @return the strings render bounds p.x = x-Dimension; p.y = y-Dimension = Font heigth
	 */
	public Point getStringBounds(String text){

		Point dim = new Point(0, 0);
		for(int i = 0; i < text.length(); i++){
			if(chars.containsKey((int)text.charAt(i))){
				dim.x += chars.get((int)text.charAt(i)).width;
			}
		}
		dim.y = heigth;

		return dim;
	}

	/**
	 * @return the Font size in pt != render size
	 */
	public int getSize() {
		return characteristics.size;
	}

	/**
	 * @return the Fonts style e.g. Plain, Bold, ...
	 */
	public Style getStyle() {
		return characteristics.style;
	}

	/**
	 * @return the Fonts render heigth != font size
	 */
	public int getHeigth() {
		return heigth;
	}

	/**
	 * @return the Fonts color
	 */
	public Color getColor() {
		return characteristics.c;
	}

	/**
	 * @return the Fonts name
	 */
	public String getName() {
		return characteristics.name;
	}

	/**
	 * Used to determine the specific values of a certain character
	 */
	private HashMap<Integer, Character> chars;

	/**
	 * The sprite holding the Font Texture, used to draw the Text on screen
	 */
	private Sprite s;

	/**
	 * The heigth of the Font when rendered != Font Size
	 */
	private int heigth;

	/**
	 * Some certein Font specific Characterisitics
	 */
	private FontCharacteristics characteristics;

	/**
	 * Private Constructor, Use getFont instead
	 */
	private GLFont() {
		chars = new HashMap<Integer, Character>();
		characteristics = new FontCharacteristics();
	}

	/**
	 * Class used to identify a Font by its characteristics
	 * @author nZeloT
	 *
	 */
	class FontCharacteristics {
		public String name;
		public GLFont.Style style;
		public int size;
		public Color c;

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((c == null) ? 0 : c.hashCode());
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			result = prime * result + size;
			result = prime * result + ((style == null) ? 0 : style.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}

			if (obj == null || !(obj instanceof FontCharacteristics)) {
				return false;
			}

			FontCharacteristics other = (FontCharacteristics) obj;

			if (!c.equals(other.c)
					|| !name.equals(other.name)
					|| size != other.size
					|| style != other.style) {

				return false;
			}

			return true;
		}

		@Override
		public String toString() {
			return "FontCharacteristics [name=" + name + ", style=" + style
					+ ", size=" + size + ", c=" + c + "]";
		}
	}

	/**
	 * Class used to specify certain character specific values
	 * @author nZeloT
	 *
	 */
	private class Character {
		public char c;
		public int width;
		public int n;

		@Override
		public String toString() {
			return "Character [c=" + c + ", width=" + width + ", n=" + n + "]";
		}
	}
}