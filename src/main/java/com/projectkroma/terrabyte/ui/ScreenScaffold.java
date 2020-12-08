package com.projectkroma.terrabyte.ui;

import com.projectkroma.terrabyte.engine.Display;

/**
 * The ScreenScaffold is a {@link ScreenElement} which is always as big as the
 * screen and has no parent. It is to be considered the base of all other
 * ScreenElements.
 */
public class ScreenScaffold extends ScreenElement
{

	private Display display;

	public ScreenScaffold(Display display)
	{
		super(null, ScreenSpace.getAbsolute(0, 0), ScreenSpace.getRelative(1f, 1f));
		this.display = display;
	}

	public int getWidth()
	{
		return display.getWindowSize().x;
	}

	public int getHeight()
	{
		return display.getWindowSize().y;
	}

	public int getX()
	{
		return 0;
	}

	public int getY()
	{
		return 0;
	}
}
