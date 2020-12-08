package com.projectkroma.terrabyte.exceptions;

@SuppressWarnings("serial")
public class DimensionMismatchException extends Exception
{
	public DimensionMismatchException(String message)
	{
		super(message);
	}
}
