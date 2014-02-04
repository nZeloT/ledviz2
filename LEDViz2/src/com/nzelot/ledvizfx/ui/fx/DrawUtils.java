package com.nzelot.ledvizfx.ui.fx;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_ENV;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_ENV_MODE;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTexEnvi;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import com.nzelot.ledvizfx.gfx.res.ResourceManager;

public class DrawUtils {
    
    public static void drawAlphaTextureRect(int x, int y, int width, int heigth, float alpha, String tex){
	glEnable(GL_TEXTURE_2D);
	//Texture combination
	glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);
	GL11.glBindTexture(GL_TEXTURE_2D, ResourceManager.getResource(tex).<Integer>getData());
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
	glDisable(GL_TEXTURE_2D);
    }

    public static void drawColoredTextureRect(int x, int y, int width, int heigth, Color c, String tex){
	glEnable(GL_TEXTURE_2D);
	//Texture combination
	glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL11.GL_DECAL);
	GL11.glBindTexture(GL_TEXTURE_2D, ResourceManager.getResource(tex).<Integer>getData());
	glLoadIdentity();
	glTranslatef(x, y, 0);
	glColor4f(c.getRed()/255.0f, c.getGreen()/255.0f, c.getBlue()/255.0f, c.getAlpha()/255.0f);
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
	glDisable(GL_TEXTURE_2D);
    }

    public static void drawColoredTextureRect(int x, int y, int width, int heigth, Color[] c, String tex){
	glEnable(GL_TEXTURE_2D);
	glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL11.GL_DECAL);
	GL11.glBindTexture(GL_TEXTURE_2D, ResourceManager.getResource(tex).<Integer>getData());
	glLoadIdentity();
	glTranslatef(x, y, 0);
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
	glDisable(GL_TEXTURE_2D);
    }

    public static void drawColorRect(int x, int y, int width, int heigth, Color c){
	glLoadIdentity();
	glTranslatef(x, y, 0);
	glColor4f(c.getRed()/255.0f, c.getGreen()/255.0f, c.getBlue()/255.0f, c.getAlpha()/255.0f);
	glBegin(GL_QUADS);
	{
	    glVertex2f(0, 0);
	    glVertex2f(width, 0);
	    glVertex2f(width, heigth);
	    glVertex2f(0, heigth);
	}
	glEnd();
	glLoadIdentity();
    }

    public static void drawColorRect(int x, int y, int width, int heigth, Color[] c){
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
    }
}
