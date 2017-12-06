package com.mx.rxjavatoy.imp;

import com.mx.rxjavatoy.Disposable;
import com.mx.rxjavatoy.Observable;
import com.mx.rxjavatoy.Observer;
import com.mx.rxjavatoy.Scheduler;

import java.util.HashSet;
import java.util.Set;

public class ObservableObserveOn<T> extends Observable<T> {

    private final Observable<T> source;
    private final Scheduler scheduler;

    public ObservableObserveOn(Observable<T> source, Scheduler scheduler) {
        this.source = source;
        this.scheduler = scheduler;
    }

    @Override
    protected void subscribeActual(Observer<? super T> observer) {
        ObserveOnDisposable disposable = new ObserveOnDisposable(scheduler, observer);
        source.subscribe(disposable);
    }

    private class ObserveOnDisposable implements Disposable, Observer<T> {

        private final Scheduler scheduler;
        private final Observer<? super T> observer;
        private Disposable sourceDisposable;
        private Set<Disposable> schedulerDisposables;
        private volatile boolean disposed;

        public ObserveOnDisposable(Scheduler scheduler, Observer<? super T> observer) {
            this.scheduler = scheduler;
            this.observer = observer;
            schedulerDisposables = new HashSet<>();
        }

        @Override
        public void dispose() {
            if (!disposed) {
                disposed = true;

                sourceDisposable.dispose();

                for (Disposable disposable : schedulerDisposables) {
                    if (!disposable.isDisposed()) {
                        disposable.dispose();
                    }
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
        public void onNext(final T t) {
            Disposable disposable = scheduler.submit(new Runnable() {
                @Override
                public void run() {
                    if (isDisposed())
                        return;

                    observer.onNext(t);
                }
            });

            schedulerDisposables.add(disposable);
        }

        @Override
        public void onError(final Throwable e) {
            Disposable disposable = scheduler.submit(new Runnable() {
                @Override
                public void run() {
                    if (isDisposed())
                        return;

                    observer.onError(e);
                }
            });

            schedulerDisposables.add(disposable);
        }

        @Override
        public void onComplete() {
            Disposable disposable = scheduler.submit(new Runnable() {
                @Override
                public void run() {
                    if (isDisposed())
                        return;

                    observer.onComplete();
                }
            });

            schedulerDisposables.add(disposable);
        }
    }
}
