package com.mx.rxjavatoy.imp;

import com.mx.rxjavatoy.Observable;
import com.mx.rxjavatoy.Observer;

public class ObservableJust<T> extends Observable<T> {

    private final T value;

    public ObservableJust(T value) {
        this.value = value;
    }

    @Override
    protected void subscribeActual(Observer<? super T> observer) {

    }
}
