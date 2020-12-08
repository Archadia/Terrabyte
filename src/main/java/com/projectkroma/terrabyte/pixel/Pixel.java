package com.projectkroma.terrabyte.pixel;

import java.awt.Color;

/**
 * Basically a unit of color, which can convert forms.
 */
public class Pixel
{
	private Color color;
	
	/**
	 * Using ARGB format.
	 * @param color
	 */
	public Pixel(int color)
	{
		int r = (color & 0x00FF0000) >> 16;
		int b = color & 0x000000FF;
		this.color = new Color((color & 0xFF00FFFF & 0xFFFFFF00) | (b << 16) | r);
	}
	
	public Pixel(char[] color)
	{
		this.color = new Color(color[1], color[2], color[3], color[0]);
	}
	
	public Pixel(String color)
	{
		String useColor = new String(color);
		if(color.startsWith("#"))
		{
			useColor = useColor.substring(1);
		}
		this.color = new Color((int) Long.parseLong(useColor, 16));	
	}
	
	public char[] toBytes()
	{
		char[] arr = new char[4];
		arr[0] = (char) color.getAlpha();
		arr[1] = (char) color.getRed();
		arr[2] = (char) color.getGreen();
		arr[3] = (char) color.getBlue();
		return arr;
	}
	
	public int toInt32()
	{
		return color.getRGB();
	}
	
	public String toHex()
	{
		return String.format("#%08X", (0xFFFFFFFF & toInt32()));
	}
}
