package com.projectkroma.terrabyte.engine;

import java.lang.reflect.InvocationTargetException;

import org.lwjgl.opengl.GL11;

import com.projectkroma.terrabyte.util.Hints;

public class TerrabyteEngine
{
	public static final String LOG_PREFIX = "[TerrabyteEngine]";
	public static final String LOG_INTERNAL = "[Internal]";
	
	private Class<? extends Frame> frameClass;
	private Display display;
	private Frame frame;
	
	private int pixelWidth;
	private int pixelHeight;
	
	public TerrabyteEngine(Class<? extends Frame> frameClass, String title, int initWidth, int initHeight)
	{
		this(frameClass, title, initWidth, initHeight, Hints.none());
	}
	
	public TerrabyteEngine(Class<? extends Frame> frameClass, String title, int initWidth, int initHeight, Hints hints)
	{
		this(frameClass, title, initWidth, initHeight, 1, 1, hints);
	}
	
	public TerrabyteEngine(Class<? extends Frame> frameClass, String title, int initWidth, int initHeight, int pixelWidth, int pixelHeight)
	{
		this(frameClass, title, initWidth, initHeight, pixelWidth, pixelHeight, Hints.none());
	}
	
	public TerrabyteEngine(Class<? extends Frame> frameClass, String title, int initWidth, int initHeight, int pixelWidth, int pixelHeight, Hints hints)
	{
		this.frameClass = frameClass;
		this.display = new Display(hints);
		this.pixelWidth = pixelWidth;
		this.pixelHeight = pixelHeight;
		display.init();
		display.createWindow(initWidth, initHeight, title, 0, 0, pixelWidth, pixelHeight);
	}
	
	public void start()
	{
		try
		{
			this.frame = frameClass.getDeclaredConstructor().newInstance();
			display.startRunLoop(frame);
		} catch (IllegalAccessException e)
		{
			System.err.println(LOG_PREFIX + " Unable to access the program's constructor: ");
			System.err.println("\t" + e.getLocalizedMessage());
		} catch(IllegalArgumentException e)
		{
			System.err.println(LOG_PREFIX + " " + LOG_INTERNAL + " Unable to construct program: " + e.getMessage());
		} catch (NoSuchMethodException e)
		{
			System.err.println(LOG_PREFIX + " " + LOG_INTERNAL + " Unable to construct program: " + e.getMessage());
		} catch (InstantiationException e)
		{
			System.err.println(LOG_PREFIX + " Unable to construct class, is it an interface or abstract class?");
		} catch (InvocationTargetException e)
		{
			System.err.println(LOG_PREFIX + " " + LOG_INTERNAL + " Unable to construct program: " + e.getMessage());
		}
	}
	
	public Display getDisplay()
	{
		return this.display;
	}
	
	public int getPixelWidth()
	{
		return this.pixelWidth;
	}
	
	public int getPixelHeight()
	{
		return this.pixelHeight;
	}

	public int getDisplayWidth()
	{
		return this.getDisplay().getWindowSize().x;
	}
	
	public int getDisplayHeight()
	{
		return this.getDisplay().getWindowSize().y;
	}
	
	public Frame getFrame()
	{
		return this.frame;
	}
	
	public <T extends Frame> T getFrame(Class<T> cls)
	{
		return cls.cast(this.frame);
	}
	
	public void enableStandardBlending()
	{
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}
}
