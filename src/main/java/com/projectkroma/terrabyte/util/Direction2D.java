package com.projectkroma.terrabyte.util;

public enum Direction2D
{

	NORTH(0, -1), EAST(1, 0), SOUTH(0, 1), WEST(-1, 0);

	public final int xoff, yoff;

	Direction2D(int xoff, int yoff)
	{
		this.xoff = xoff;
		this.yoff = yoff;
	}
}
