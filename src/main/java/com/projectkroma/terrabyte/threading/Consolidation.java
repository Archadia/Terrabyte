package com.projectkroma.terrabyte.threading;

@FunctionalInterface
public interface Consolidation<R>
{

	public void run(R r);
}
