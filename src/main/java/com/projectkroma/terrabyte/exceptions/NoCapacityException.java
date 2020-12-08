package com.projectkroma.terrabyte.exceptions;

public class NoCapacityException extends Exception
{

	private static final long serialVersionUID = -7813870265545026607L;

	public NoCapacityException(String message)
	{
		super(message);
	}
}
