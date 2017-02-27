package cn.ben.tvdemo.util.schedulers;

import android.support.annotation.NonNull;

import io.reactivex.Scheduler;

public interface BaseSchedulerProvider {
    @NonNull
    Scheduler computation();

    @NonNull
    Scheduler io();

    @NonNull
    Scheduler ui();

    @NonNull
    rx.Scheduler io_1();
}
