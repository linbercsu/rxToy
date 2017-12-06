package com.mx.rxjavatoy.imp.scheduler;

import android.os.Handler;
import android.os.Looper;

import com.mx.rxjavatoy.Disposable;
import com.mx.rxjavatoy.Scheduler;

public class SchedulerMainThread extends Scheduler {

    private final Handler handler;

    public SchedulerMainThread() {
        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public Disposable submit(Runnable runnable) {
        return new MainThreadDisposable(runnable);
    }

    private class MainThreadDisposable implements Disposable {

        final Runnable runnable;
        boolean disposed;

        public MainThreadDisposable(Runnable runnable) {
            this.runnable = runnable;
            handler.post(runnable);
        }

        @Override
        public void dispose() {
            disposed = true;
            handler.removeCallbacks(runnable);
        }

        @Override
        public boolean isDisposed() {
            return disposed;
        }
    }

}
