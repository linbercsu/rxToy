package com.mx.rxjavatoy.imp;

import com.mx.rxjavatoy.Disposable;
import com.mx.rxjavatoy.Function;
import com.mx.rxjavatoy.Observable;
import com.mx.rxjavatoy.Observer;

import java.util.HashSet;
import java.util.Set;

public class ObservableFlatMap<R, T> extends Observable<R> {

    private Observable<T> source;
    private Function<? super T, ? extends Observable<? extends R>> mapper;

    public ObservableFlatMap(Observable<T> source, Function<? super T, ? extends Observable<? extends R>> mapper) {
        this.source = source;
        this.mapper = mapper;
    }

    @Override
    protected void subscribeActual(Observer<? super R> observer) {
        FlatMapDisposable flatMapDisposable = new FlatMapDisposable(observer);
        source.subscribe(flatMapDisposable);
    }

    private class FlatMapDisposable implements Disposable, Observer<T> {

        Observer<? super R> observer;
        Disposable sourceDisposable;
        Set<Disposable> innerDisposables;
        Set<Observer> unCompletedObservers;
        private boolean disposed;
        private boolean error;
        private boolean completed;

        public FlatMapDisposable(Observer<? super R> observer) {
            this.observer = observer;
            innerDisposables = new HashSet<>();
            unCompletedObservers = new HashSet<>();
        }

        @Override
        public void dispose() {
            disposed = true;
            if (!sourceDisposable.isDisposed()) {
                sourceDisposable.dispose();
            }

            for (Disposable disposable : innerDisposables) {
                if (!disposable.isDisposed()) {
                    disposable.dispose();
                }
            }
        }

        @Override
        public boolean isDisposed() {
            return disposed;
        }

        @Override
        public void onSubscribe(Disposable d) {
            sourceDisposable = d;
            observer.onSubscribe(this);
        }

        @Override
        public void onNext(T t) {
            try {
                Observable<? extends R> apply = mapper.apply(t);
                InnerObserver innerObserver = new InnerObserver();
                unCompletedObservers.add(innerObserver);
                apply.subscribe(innerObserver);
            } catch (Exception e) {
                error = true;
                observer.onError(e);
            }
        }

        @Override
        public void onError(Throwable e) {
            if (error) {
                return;
            }

            observer.onError(e);
        }

        @Override
        public void onComplete() {
            completed = true;

            if (error) {
                return;
            }

            if (unCompletedObservers.isEmpty()) {
                observer.onComplete();
            }
        }

        private class InnerObserver implements Observer<R> {

            @Override
            public void onSubscribe(Disposable d) {
                innerDisposables.add(d);
            }

            @Override
            public void onNext(R r) {
                if (!isDisposed() || !error) {
                    observer.onNext(r);
                }
            }

            @Override
            public void onError(Throwable e) {
                if (!isDisposed() || !error) {
                    error = true;

                    observer.onError(e);
                }
            }

            @Override
            public void onComplete() {
                unCompletedObservers.remove(this);

                if (!isDisposed() || !error) {
                    if (completed) {
                        observer.onComplete();
                    }
                }
            }
        }
    }
}
