package com.projectkroma.terrabyte.observer;

import java.util.ArrayList;
import java.util.List;

public class Subject<S extends SubjectDescriptor>
{
	private List<Observer<S>> observers;

	public Subject()
	{
		this.observers = new ArrayList<Observer<S>>();
	}

	public void add(Observer<S> observer)
	{
		observers.add(observer);
	}

	public void remove(Observer<S> observer)
	{
		observers.remove(observer);
	}

	public void tell(S description)
	{
		for (Observer<S> obs : observers)
		{
			obs.tell(description);
		}
	}
}
