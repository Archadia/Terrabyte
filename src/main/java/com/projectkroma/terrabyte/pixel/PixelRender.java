package com.projectkroma.terrabyte.pixel;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

/*
 * TODO: Needs alpha fixing.
 */
/**
 * Pixel based rendering
 */
public class PixelRender
{
	private int width;
	private int height;
	
	private int pixelWidth;
	private int pixelHeight;
	
	private int[] prevData;
	private int[] data;

	private int texture;
	
	public int getTextureWidth()
	{
		return width / pixelWidth;
	}
	
	public int getTextureHeight()
	{
		return height / pixelHeight;
	}
		
	public PixelRender(int width, int height, int pixelWidth, int pixelHeight)
	{
		this.width = width;
		this.height = height;
		this.pixelWidth = pixelWidth;
		this.pixelHeight = pixelHeight;
		
		this.data = new int[getTextureWidth() * getTextureHeight()];
		this.texture = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
	}
	
	public void updateSize(int width, int height)
	{
		this.width = width;
		this.height = height;
		this.data = new int[getTextureWidth() * getTextureHeight()];
	}
	
	public void clearBuffer()
	{
		this.data = new int[getTextureWidth() * getTextureHeight()];
	}
	
	private void updateTexture()
	{
		if(!data.equals(prevData))
		{
			GL15.glTexImage2D(GL15.GL_TEXTURE_2D, 0, GL15.GL_RGBA, getTextureWidth(), getTextureHeight(), 0, GL15.GL_RGBA, GL15.GL_UNSIGNED_BYTE, data);
			this.prevData = data;			
		}
	}
	
	public void draw(int x, int y, Pixel pixel)
	{
		if(x >= 0 && x < getTextureWidth())
		{
			if(y >= 0 && y < getTextureHeight())
			{
				this.data[(y * getTextureWidth() + x)] = pixel.toInt32();
			}
		}
	}
	
	public void drawRect(int x, int y, int w, int h, Pixel pixel)
	{
		drawLine(x, y, x + w, y, pixel);
		drawLine(x + w, y, x + w, y + h, pixel);
		drawLine(x, y + h, x + w, y + h, pixel);
		drawLine(x, y, x, y + h, pixel);
	}
	
	public void fillRect(int x, int y, int w, int h, Pixel pixel)
	{
		for(int i = x; i < x + w; i++)
		{
			for(int j = y; j < y + h; j++)
			{
				draw(i, j, pixel);
			}
		}
	}
	
	// Bresenham's Line Drawing
	public void drawLine(int x1, int y1, int x2, int y2, Pixel pixel)
	{	
		int dx = Math.abs(x2 - x1);
		int sx = x1 < x2 ? 1 : -1;
		int dy = -Math.abs(y2 - y1);
		int sy = y1 < y2 ? 1 : -1;
		int err = dx + dy;

        while(true)
        {
        	draw(x1, y1, pixel);
            if(x1 == x2 && y1 == y2) break;
            int e2 = 2*err;
            if (e2 >= dy)
            {
                err += dy;
                x1 += sx;
            }
            if(e2 <= dx)
            {
                err += dx;
                y1 += sy;
            }
        }
	}
	
	public void drawTriangle(int x1, int y1, int x2, int y2, int x3, int y3, Pixel pixel)
	{
		drawLine(x1, y1, x2, y2, pixel);
		drawLine(x2, y2, x3, y3, pixel);
		drawLine(x3, y3, x1, y1, pixel);
	}

	/*
	 * Requires backwards compatibility.
	 */
	public void render()
	{
		GL11.glPushMatrix();
		GL11.glViewport(0, 0, width, height);
		GL11.glOrtho(0, width, height, 0, 0.0f, 10.0f);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		updateTexture();
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(0, 0);
			GL11.glVertex2f(0, 0);

			GL11.glTexCoord2f(1, 0);
			GL11.glVertex2f(width, 0);

			GL11.glTexCoord2f(1, 1);
			GL11.glVertex2f(width, height);

			GL11.glTexCoord2f(0, 1);
			GL11.glVertex2f(0, height);
		GL11.glEnd();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GL11.glPopMatrix();
	}
}
