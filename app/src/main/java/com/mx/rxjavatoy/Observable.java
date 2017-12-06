package com.mx.rxjavatoy;

import com.mx.rxjavatoy.imp.ObservableFlatMap;
import com.mx.rxjavatoy.imp.ObservableJust;
import com.mx.rxjavatoy.imp.ObservableMap;
import com.mx.rxjavatoy.imp.ObservableObserveOn;

public abstract class Observable<T> {

    public static <T> Observable<T> just(T item) {
        return new ObservableJust<>(item);
    }

    public final <R> Observable<R> map(Function<? super T, ? extends R> mapper) {
        return new ObservableMap<>(this, mapper);
    }

    public final <R> Observable<R> flatMap(Function<? super T, ? extends Observable<? extends R>> mapper) {
        return new ObservableFlatMap<>(this, mapper);
    }

    public final Observable<T> observeOn(Scheduler scheduler) {
        return new ObservableObserveOn<>(this, scheduler);
    }

    public final void subscribe(Observer<? super T> observer) {
        subscribeActual(observer);
    }

    protected abstract void subscribeActual(Observer<? super T> observer);
}
