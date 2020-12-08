package com.projectkroma.terrabyte.engine.descriptors;

import org.lwjgl.glfw.GLFW;

import com.projectkroma.terrabyte.observer.SubjectDescriptor;

public class KeyActionDescriptor implements SubjectDescriptor
{
	public static final int KEY_RELEASED = GLFW.GLFW_RELEASE;
	public static final int KEY_PRESS = GLFW.GLFW_PRESS;
	
	public final int key, action;

	public KeyActionDescriptor(int key, int action)
	{
		this.key = key;
		this.action = action;
	}
}
