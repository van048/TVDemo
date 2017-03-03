package cn.ben.tvdemo.data;

import cn.ben.tvdemo.data.tvchannel.source.TVChannelsRepository;
import cn.ben.tvdemo.data.tvchannel.source.local.TVChannelsLocalDataSource;
import cn.ben.tvdemo.data.tvchannel.source.remote.TVChannelsRemoteDataSource;
import cn.ben.tvdemo.data.tvtype.source.TVTypesRepository;
import cn.ben.tvdemo.data.tvtype.source.local.TVTypesLocalDataSource;
import cn.ben.tvdemo.data.tvtype.source.remote.TVTypesRemoteDataSource;
import cn.ben.tvdemo.util.schedulers.BaseSchedulerProvider;
import cn.ben.tvdemo.util.schedulers.SchedulerProvider;

public class Injection {
    public static TVTypesRepository provideTVTypesRepository() {
        return TVTypesRepository.getInstance(
                TVTypesRemoteDataSource.getInstance(),
                TVTypesLocalDataSource.getInstance()
        );
    }

    public static BaseSchedulerProvider provideSchedulerProvider() {
        return SchedulerProvider.getInstance();
    }

    public static TVChannelsRepository provideTVChannelsRepository() {
        return TVChannelsRepository.getInstance(
                TVChannelsRemoteDataSource.getInstance(),
                TVChannelsLocalDataSource.getInstance()
        );
    }
}
