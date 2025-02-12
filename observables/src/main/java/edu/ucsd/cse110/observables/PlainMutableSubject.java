package edu.ucsd.cse110.observables;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A plain old LiveData-like subject that can be observed, but with no Android dependencies, and
 * NOT lifecycle aware!
 * <p>
 * This is the implementation you actually want to use in your tests.
 * <p>
 * The implementation here is substantially more advanced than the one used in labs. It covers
 * a bunch of weird edge cases that come into play when we start mixing our {@link Subject}s with
 * Android LiveData, so that you don't have to worry about them.
 * <p>
 * You do not need to understand the code below, but if you're curious, feel free to ask me in
 * office hours. –Dylan
 *
 * @param <T> The type of the value that the subject holds.
 */
public class PlainMutableSubject<T> implements MutableSubject<T> {
    private final AtomicReference<Optional<T>> value = new AtomicReference<>(Optional.empty());
    private final ConcurrentLinkedQueue<Observer<? super T>> observers = new ConcurrentLinkedQueue<>();

    public PlainMutableSubject() {
    }

    public PlainMutableSubject(T initialValue) {
        this.value.getAndSet(Optional.of(initialValue));
    }

    protected void notifyObservers() {
        T newValue = value.get().orElse(null);
        observers.forEach(observer -> observer.onChanged(newValue));
    }

    @Override
    @Nullable
    public T getValue() {
        return value.get().orElse(null);
    }

    @Override
    public void setValue(T newValue) {
        value.set(Optional.of(newValue));
        notifyObservers();
    }

    @Override
    public boolean hasObservers() {
        return !observers.isEmpty();
    }

    @Override
    public boolean isInitialized() {
        return value.get().isPresent();
    }

    @Override
    public Observer<? super T> observe(@NonNull Observer<? super T> observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
            if (isInitialized()) observer.onChanged(getValue());
        }
        return observer;
    }


    @Override
    public void removeObserver(@NonNull Observer<? super T> observer) {
        observers.removeIf(o -> o.equals(observer));
    }

    @Override
    public void removeObservers() {
        observers.clear();
    }

    @Override
    @VisibleForTesting
    public List<Observer<? super T>> getObservers() {
        return List.copyOf(observers);
    }
}
