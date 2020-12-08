package com.projectkroma.terrabyte.ui;

public class ScreenSpace
{

	private ScreenSpaceType policy;
	private float a;
	private float b;

	private ScreenSpace(ScreenSpaceType policy, float a, float b) throws NullPointerException
	{
		if (policy == null)
			throw new NullPointerException("The policy provided is null.");
		this.policy = policy;
		this.a = a;
		this.b = b;
	}

	public ScreenSpaceType getPolicy()
	{
		return policy;
	}

	public float getA()
	{
		return a;
	}

	public float getB()
	{
		return b;
	}

	public static ScreenSpace getRelative(float a, float b)
	{
		return new ScreenSpace(ScreenSpaceType.RELATIVE, a, b);
	}

	public static ScreenSpace getAbsolute(float a, float b)
	{
		return new ScreenSpace(ScreenSpaceType.ABSOLUTE, a, b);
	}
}