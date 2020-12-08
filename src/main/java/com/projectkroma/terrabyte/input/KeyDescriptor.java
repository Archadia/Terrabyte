package com.projectkroma.terrabyte.input;

import com.projectkroma.terrabyte.observer.SubjectDescriptor;

public class KeyDescriptor implements SubjectDescriptor
{

	public int key;

	public KeyDescriptor(int key)
	{
		this.key = key;
	}
}
