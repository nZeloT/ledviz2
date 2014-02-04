package com.nzelot.ledviz2.ui.elements;

import java.awt.Color;

import com.nzelot.ledviz2.gfx.core.DrawUtils;
import com.nzelot.ledviz2.gfx.core.Texture;
import com.nzelot.ledviz2.gfx.res.ResourceManager;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.EXTTextureRectangle.*;

public class RenderUtils {

    public static void drawColoredRectangle(int x, int y, int width, int heigth, Color c){
	glDisable(GL_TEXTURE_RECTANGLE_EXT);

	DrawUtils.drawQuad(x, y, width, heigth, c);

	glEnable(GL_TEXTURE_RECTANGLE_EXT);
    }

    public static void drawColoredRactangle(int x, int y, int width, int heigth, Color[] c){
	glDisable(GL_TEXTURE_RECTANGLE_EXT);

	DrawUtils.drawQuad(x, y, width, heigth, c);

	glEnable(GL_TEXTURE_RECTANGLE_EXT);
    }

    public static void drawAlphaTextureRectangle(int x, int y, int width, int heigth, float alpha, String tex){
	Texture.unbind();
	glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
	Texture t = ResourceManager.getResource(tex).<Texture>getData();
	t.bind();
	DrawUtils.drawQuad(x, y, width, heigth, new Color(1f, 1f, 1f, alpha), new int[][]{{0, t.getWidth()},{0, t.getHeigth()}});
	glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_DECAL);
	Texture.unbind();
    }
}
