package cn.ben.tvdemo.util.schedulers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SchedulerProvider implements BaseSchedulerProvider {
    @Nullable
    private static volatile SchedulerProvider INSTANCE;

    private SchedulerProvider() {
    }

    @Nullable
    public static SchedulerProvider getINSTANCE() {
        if (INSTANCE == null) {
            synchronized (SchedulerProvider.class) {
                if (INSTANCE == null) {
                    INSTANCE = new SchedulerProvider();
                }
            }
        }
        return INSTANCE;
    }

    @NonNull
    @Override
    public Scheduler computation() {
        return Schedulers.computation();
    }

    @NonNull
    @Override
    public Scheduler io() {
        return Schedulers.io();
    }

    @NonNull
    @Override
    public Scheduler ui() {
        return AndroidSchedulers.mainThread();
    }
}
