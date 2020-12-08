package com.projectkroma.terrabyte.engine.descriptors;

import com.projectkroma.terrabyte.observer.SubjectDescriptor;

public class WindowResizeDescriptor implements SubjectDescriptor
{

	public final int width, height;

	public WindowResizeDescriptor(int width, int height)
	{
		this.width = width;
		this.height = height;
	}
}
