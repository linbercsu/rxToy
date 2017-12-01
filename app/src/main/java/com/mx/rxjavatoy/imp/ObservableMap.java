package com.mx.rxjavatoy.imp;

import com.mx.rxjavatoy.Disposable;
import com.mx.rxjavatoy.Function;
import com.mx.rxjavatoy.Observable;
import com.mx.rxjavatoy.Observer;

public class ObservableMap<R, T> extends Observable<R> {

    private final Observable<T> source;
    private final Function<? super T, ? extends R> mapper;

    public ObservableMap(Observable<T> source, Function<? super T, ? extends R> mapper) {
        this.source = source;
        this.mapper = mapper;
    }

    @Override
    protected void subscribeActual(Observer<? super R> observer) {
        MapDisposable mapDisposable = new MapDisposable(observer);
        source.subscribe(mapDisposable);
    }

    private class MapDisposable implements Disposable, Observer<T> {

        private boolean disposed;
        private Disposable inner;
        private Observer<? super R> observer;
        private boolean error;

        public MapDisposable(Observer<? super R> observer) {
            this.observer = observer;
        }

        @Override
        public void dispose() {
            disposed = true;

            if (inner != null && !inner.isDisposed()) {
                inner.dispose();
            }
        }

        @Override
        public boolean isDisposed() {
            return disposed;
        }

        @Override
        public void onSubscribe(Disposable d) {
            inner = d;
            observer.onSubscribe(this);
        }

        @Override
        public void onNext(T t) {
            if (disposeIfNeeded()) return;

            try {
                R apply = mapper.apply(t);
                observer.onNext(apply);
            } catch (Exception e) {
                error = true;

                inner.dispose();

                observer.onError(e);
            }
        }

        private boolean disposeIfNeeded() {
            if (isDisposed()) {
                if (inner != null && !inner.isDisposed()) {
                    inner.dispose();
                }
                return true;
            }
            return false;
        }

        @Override
        public void onError(Throwable e) {
            if (error || disposeIfNeeded()) return;

            observer.onError(e);
        }

        @Override
        public void onComplete() {
            if (error || disposeIfNeeded()) return;

            observer.onComplete();
        }
    }
}
