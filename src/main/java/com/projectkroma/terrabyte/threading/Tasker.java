package com.projectkroma.terrabyte.threading;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Tasker<R, T extends Task<R>>
{

	private String label;

	private List<T> tasks;

	private int tasksPerTick;

	private Consolidation<R> consolidationTask;

	public Tasker(String label, int tasksPerTick, Consolidation<R> consolidation)
	{
		this.label = label;
		this.tasksPerTick = tasksPerTick;
		this.tasks = new ArrayList<T>();
		this.consolidationTask = consolidation;
	}

	public Tasker(int tasksPerTick, Consolidation<R> consolidation)
	{
		this("untitled", tasksPerTick, consolidation);
	}

	public void addTask(T task)
	{
		tasks.add(task);
	}

	public void tick()
	{
		Iterator<T> iterator = tasks.iterator();

		int tasksCompleted = 0;

		while (tasksCompleted < tasksPerTick && iterator.hasNext())
		{
			T task = iterator.next();
			if (task.getFuture().isDone())
			{
				try
				{
					R result = task.getFuture().get();
					consolidationTask.run(result);
					iterator.remove();
					tasksCompleted++;
				} catch (InterruptedException e)
				{
					System.err.println("Tasker[" + label + "] experienced an interruption while attempting consolidate a task.");
				} catch (ExecutionException e)
				{
					StringBuilder string = new StringBuilder();
					string.append("Tasker[" + label + "] experienced an error while executing a task: " + e.getMessage() + "\n");
					for (StackTraceElement st : e.getStackTrace())
					{
						string.append("> " + st.toString() + "\n");
					}
					System.err.println(string.toString());
				}
			}

		}
	}

}
