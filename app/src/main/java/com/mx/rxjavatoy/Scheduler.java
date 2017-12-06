package com.mx.rxjavatoy;


import com.mx.rxjavatoy.imp.scheduler.SchedulerIO;
import com.mx.rxjavatoy.imp.scheduler.SchedulerMainThread;

public abstract class Scheduler {

    private static final SchedulerIO SCHEDULER_IO = new SchedulerIO();
    private static final SchedulerMainThread SCHEDULER_MAIN_THREAD = new SchedulerMainThread();

    public static Scheduler IO() {
        return SCHEDULER_IO;
    }

    public static Scheduler mainThread() {
        return SCHEDULER_MAIN_THREAD;
    }

    public abstract Disposable submit(Runnable runnable);
}
