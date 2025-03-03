package edu.ucsd.cse110.habitizer.app.util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import java.util.List;

import edu.ucsd.cse110.observables.Observer;
import edu.ucsd.cse110.observables.Subject;

public class LiveDataSubjectAdapter<T> implements Subject<T> {
    private final LiveData<T> adaptee;

    public LiveDataSubjectAdapter(LiveData<T> adaptee) {
        this.adaptee = adaptee;
    }

    @Nullable
    @Override
    public T getValue() {
        return adaptee.getValue();
    }

    @Override
    public Observer<? super T> observe(@NonNull Observer<? super T> observer) {
        adaptee.observeForever(observer::onChanged);
        return observer::onChanged;
    }

    @Override
    public void removeObserver(@NonNull Observer<? super T> observer) {
        adaptee.removeObserver(observer::onChanged);
    }

    @Override
    public boolean hasObservers() {
        return adaptee.hasObservers();
    }

    @Override
    public boolean isInitialized() {
        return adaptee.isInitialized();
    }

    @Override
    public void removeObservers() {

    }

    @Override
    public List<Observer<? super T>> getObservers() {
        return null;
    }
}
