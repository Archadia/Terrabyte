package com.projectkroma.terrabyte.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Hints implements Iterable<Integer>
{
	private Map<Integer, Integer> hints;

	private Hints()
	{
		this.hints = new HashMap<Integer, Integer>();
	}

	public static Hints with(int hint, int value)
	{
		return new Hints().and(hint, value);
	}
	
	public static Hints none()
	{
		return new Hints();
	}

	public Hints and(int hint, int value)
	{
		hints.put(hint, value);
		return this;
	}

	public int get(int hint)
	{
		return hints.get(hint);
	}

	@Override
	public Iterator<Integer> iterator()
	{
		return hints.keySet().iterator();
	}
}
