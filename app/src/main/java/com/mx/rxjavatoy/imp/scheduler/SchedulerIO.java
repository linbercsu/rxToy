package com.mx.rxjavatoy.imp.scheduler;

import com.mx.rxjavatoy.Disposable;
import com.mx.rxjavatoy.Scheduler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SchedulerIO extends Scheduler {

    private ExecutorService executorService;

    public SchedulerIO() {
        executorService = Executors.newCachedThreadPool();
    }

    @Override
    public Disposable submit(Runnable runnable) {
        return new FutureDisposable(executorService.submit(runnable));
    }

    private static class FutureDisposable implements Disposable {

        final Future future;

        public FutureDisposable(Future future) {
            this.future = future;
        }

        @Override
        public void dispose() {
            future.cancel(true);
        }

        @Override
        public boolean isDisposed() {
            return future.isCancelled();
        }
    }
}
