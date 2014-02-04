package com.nzelot.ledviz2.gfx.core;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class ColorUtils {

    public static Color[][] generate(int w, int h, BufferedImage img){
	Color[][] ret = new Color[h][w];
	
	int reqW = 5 * w;
	int reqH = 5 * h;
	
	float ratio = Math.max((reqW+0.0f) / img.getWidth(), (reqH+0.0f) / img.getHeight());
	
	img = resizeImage(img, (int) (img.getWidth()*ratio), (int) (img.getHeight()*ratio));
	
	int xFill = (int) Math.max(0, (img.getWidth() - reqW)/2.0f);
	int yFill = (int) Math.max(0, (img.getHeight() - reqH)/2.0f);
	
	for(int y = 0; y < h; y++){
	    for(int x = 0; x < w; x++){
		int r = 0;
		int g = 0;
		int b = 0;
		
		for(int i = 0; i < 5; i++){
		    for(int j = 0; j < 5; j++){
			Color c = new Color(img.getRGB(xFill+x*5+j, yFill+y*5+i));
			r += c.getRed();
			g += c.getGreen();
			b += c.getBlue();
		    }
		}
		
		r /= 25;
		g /= 25;
		b /= 25;
		
		if(r < 20 && g < 20 && b < 20){
		    r = 20;
		    g = 20;
		    b = 20;
		}
		
		ret[y][x] = new Color(r, g, b);
		
	    }
	}
	
	return ret;
    }
    
    private static BufferedImage resizeImage(BufferedImage originalImage, int imgWidth, int imgHeigth){
	int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
	
	BufferedImage resizedImage = new BufferedImage(imgWidth, imgHeigth, type);
	Graphics2D g = resizedImage.createGraphics();
	g.drawImage(originalImage, 0, 0, imgWidth, imgHeigth, null);
	g.dispose();
 
	return resizedImage;
    }
    
}
