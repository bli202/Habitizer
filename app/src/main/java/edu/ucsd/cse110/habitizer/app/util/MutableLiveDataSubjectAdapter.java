package edu.ucsd.cse110.habitizer.app.util;

import androidx.lifecycle.MutableLiveData;

import edu.ucsd.cse110.observables.MutableSubject;

public class MutableLiveDataSubjectAdapter<T>
        extends LiveDataSubjectAdapter<T>
        implements MutableSubject<T> {
    private final MutableLiveData<T> mutableAdaptee;

    public MutableLiveDataSubjectAdapter(MutableLiveData<T> adaptee) {
        super(adaptee);
        this.mutableAdaptee = adaptee;
    }

    @Override
    public void setValue(T value) {
        mutableAdaptee.setValue(value);
    }
}
