package com.projectkroma.terrabyte.threading;

import java.util.concurrent.Future;

public interface Task<R>
{

	public Future<R> getFuture();
}
