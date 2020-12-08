package com.projectkroma.terrabyte.input;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.glfw.GLFW;

import com.projectkroma.terrabyte.engine.Display;
import com.projectkroma.terrabyte.observer.Observer;
import com.projectkroma.terrabyte.observer.Subject;

public class InputManager
{

	private Subject<KeyDescriptor> keyboardPress;
	private Subject<KeyDescriptor> keyboardRelease;
	private Subject<KeyDescriptor> keyboardHold;

	private Subject<KeyDescriptor> mouseClick;

	private Map<Integer, Boolean> keyDownMap;

	public InputManager(Display display)
	{
		this.keyDownMap = new HashMap<Integer, Boolean>();

		this.keyboardPress = new Subject<KeyDescriptor>();
		this.keyboardRelease = new Subject<KeyDescriptor>();
		this.keyboardHold = new Subject<KeyDescriptor>();

		this.mouseClick = new Subject<KeyDescriptor>();

		display.keyActionListener.add((descriptor) ->
		{
			if (descriptor.action == GLFW.GLFW_PRESS)
			{
				keyboardPress.tell(new KeyDescriptor(descriptor.key));
				keyDownMap.put(descriptor.key, true);
			}
			if (descriptor.action == GLFW.GLFW_RELEASE)
			{
				keyboardRelease.tell(new KeyDescriptor(descriptor.key));
				keyDownMap.put(descriptor.key, false);
			}
		});
		display.mouseActionListener.add((descriptor) ->
		{
			if (descriptor.action == GLFW.GLFW_PRESS)
			{
				mouseClick.tell(new KeyDescriptor(descriptor.button));
			}
		});
	}

	public void onKeyboardPress(Observer<KeyDescriptor> obs)
	{
		keyboardPress.add(obs);
	}

	public void onKeyboardRelease(Observer<KeyDescriptor> obs)
	{
		keyboardRelease.add(obs);
	}

	public void onKeyboardHold(Observer<KeyDescriptor> obs)
	{
		keyboardHold.add(obs);
	}

	public void onMouseClick(Observer<KeyDescriptor> obs)
	{
		mouseClick.add(obs);
	}

	public void update(double dt)
	{
		for (int key : keyDownMap.keySet())
		{
			if (keyDownMap.get(key))
			{
				keyboardHold.tell(new KeyDescriptor(key));
			}
		}
	}
}
