package com.projectkroma.terrabyte.engine.descriptors;

import com.projectkroma.terrabyte.observer.SubjectDescriptor;

public class MouseActionDescriptor implements SubjectDescriptor
{

	public final int button, action;

	public MouseActionDescriptor(int button, int action)
	{
		this.button = button;
		this.action = action;
	}
}
