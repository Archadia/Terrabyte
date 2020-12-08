package com.projectkroma.terrabyte.opengl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.projectkroma.terrabyte.engine.TerrabyteEngine;

public class Image
{

	public int width;
	public int height;
	public byte[] pixels;

	public Image(byte[] data, int width, int height)
	{
		this.width = width;
		this.height = height;
		this.pixels = data;
	}

	public Image(String filename) throws FileNotFoundException, IOException
	{
		File file = new File(filename);
		if (!file.exists())
		{
			System.err.println(TerrabyteEngine.LOG_PREFIX + " " + filename + " cannot be found.");
			throw new FileNotFoundException("Unable to find " + filename);
		}
		BufferedImage image = ImageIO.read(file);
		this.width = image.getWidth();
		this.height = image.getHeight();
		this.pixels = new byte[width * height * 4];
		for (int i = 0; i < width * height; i++)
		{
			int argb = image.getRGB(i % width, (int) Math.floor(i / width));

			int a = (argb & 0xff000000) >> 24;
			int r = (argb & 0xff0000) >> 16;
			int g = (argb & 0xff00) >> 8;
			int b = (argb & 0xff);
			pixels[(i * 4) + 0] = (byte) r;
			pixels[(i * 4) + 1] = (byte) g;
			pixels[(i * 4) + 2] = (byte) b;
			pixels[(i * 4) + 3] = (byte) a;
		}
	}
}
