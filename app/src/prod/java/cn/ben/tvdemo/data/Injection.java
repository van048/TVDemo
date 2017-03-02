package cn.ben.tvdemo.data;

import android.content.Context;

import cn.ben.tvdemo.data.tvtype.source.TVTypesRepository;
import cn.ben.tvdemo.data.tvtype.source.local.TVTypesLocalDataSource;
import cn.ben.tvdemo.data.tvtype.source.remote.TVTypesRemoteDataSource;
import cn.ben.tvdemo.util.schedulers.BaseSchedulerProvider;
import cn.ben.tvdemo.util.schedulers.SchedulerProvider;

public class Injection {
    public static TVTypesRepository provideTVTypesRepository(Context context) {
        return TVTypesRepository.getInstance(
                TVTypesRemoteDataSource.getInstance(),
                TVTypesLocalDataSource.getInstance(context)
        );
    }

    public static BaseSchedulerProvider provideSchedulerProvider() {
        return SchedulerProvider.getInstance();
    }
}
