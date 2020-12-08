package com.projectkroma.terrabyte.engine.descriptors;

import com.projectkroma.terrabyte.observer.SubjectDescriptor;

public class MouseMoveDescriptor implements SubjectDescriptor
{

	public final double x, y;

	public MouseMoveDescriptor(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
}
