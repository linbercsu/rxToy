package com.mx.rxjavatoy.imp;

import com.mx.rxjavatoy.Disposable;
import com.mx.rxjavatoy.Observable;
import com.mx.rxjavatoy.Observer;

public class ObservableJust<T> extends Observable<T> {

    private final T value;

    public ObservableJust(T value) {
        this.value = value;
    }

    @Override
    protected void subscribeActual(Observer<? super T> observer) {

        SimpleDisposable disposable = new SimpleDisposable();
        observer.onSubscribe(disposable);

        if (!disposable.isDisposed()) {
            observer.onNext(value);
        }

        if (!disposable.isDisposed()) {
            observer.onComplete();
        }
    }

    private class SimpleDisposable implements Disposable {

        private boolean disposed;

        @Override
        public void dispose() {
            disposed = true;
        }

        @Override
        public boolean isDisposed() {
            return disposed;
        }
    }
}
