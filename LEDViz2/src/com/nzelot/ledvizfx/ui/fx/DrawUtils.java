package com.nzelot.ledvizfx.ui.fx;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.awt.Color;

import org.newdawn.slick.opengl.Texture;

import com.nzelot.ledvizfx.gfx.res.ResourceManager;

public class DrawUtils {

    public static void drawTextureRect(int x, int y, int size, String tex){
	drawTextureRect(x, y, size, size, 1, tex);
    }

    public static void drawTextureRect(int x, int y, int width, int heigth, String tex){
	drawTextureRect(x, y, width, heigth, 1, tex);
    }
    
    public static void drawTextureRect(int x, int y, int width, int heigth, float alpha, String tex){
	ResourceManager.getResource(tex).<Texture>getData().bind();
	glLoadIdentity();
	glTranslatef(x, y, 0);
	glColor4f(1f, 1f, 1f, alpha);
	{
	    glBegin(GL_QUADS);
	    glTexCoord2f(0, 0);
	    glVertex2f(0 , 0);
	    glTexCoord2f(1, 0);
	    glVertex2f(width, 0);
	    glTexCoord2f(1, 1);
	    glVertex2f(width, heigth);
	    glTexCoord2f(0 ,1);
	    glVertex2f(0, heigth);
	}
	glEnd();
	glLoadIdentity();
    }

    public static void drawColorRect(int x, int y, int size, Color c){
	drawColorRect(x, y, size, size, new Color[]{c, c, c, c});
    }

    public static void drawColorRect(int x, int y, int width, int heigth, Color c){
	drawColorRect(x, y, width, heigth, new Color[]{c, c, c, c});
    }
    
    public static void drawColorRect(int x, int y, int width, int heigth, Color[] c){
	glDisable(GL_TEXTURE_2D);
	glLoadIdentity();
	glTranslatef(x, y, 0);
	glBegin(GL_QUADS);
	{
	    glColor4f(c[0].getRed()/255.0f, c[0].getGreen()/255.0f, c[0].getBlue()/255.0f, c[0].getAlpha()/255.0f);
	    glVertex2f(0, 0);
	    glColor4f(c[1].getRed()/255.0f, c[1].getGreen()/255.0f, c[1].getBlue()/255.0f, c[1].getAlpha()/255.0f);
	    glVertex2f(width, 0);
	    glColor4f(c[2].getRed()/255.0f, c[2].getGreen()/255.0f, c[2].getBlue()/255.0f, c[2].getAlpha()/255.0f);
	    glVertex2f(width, heigth);
	    glColor4f(c[3].getRed()/255.0f, c[3].getGreen()/255.0f, c[3].getBlue()/255.0f, c[3].getAlpha()/255.0f);
	    glVertex2f(0, heigth);
	}
	glEnd();
	glLoadIdentity();
	glEnable(GL_TEXTURE_2D);
    }
}
