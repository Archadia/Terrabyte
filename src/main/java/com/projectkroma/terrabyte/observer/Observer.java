package com.projectkroma.terrabyte.observer;

public interface Observer<S extends SubjectDescriptor>
{

	void tell(S description);
}
