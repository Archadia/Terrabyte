package com.projectkroma.terrabyte.math;

public class Maths
{

	public static float cosf(float radians)
	{
		return (float) Math.cos(radians);
	}

	public static float sinf(float radians)
	{
		return (float) Math.sin(radians);
	}

	public static float radiansf(float degrees)
	{
		return (float) (degrees * Math.PI / 180.0);
	}

	public static float degreesf(float radians)
	{
		return (float) (radians * 180.0 / Math.PI);
	}

	public static float lerp(float v0, float v1, float t)
	{
		return (1 - t) * v0 + t * v1;
	}
}
