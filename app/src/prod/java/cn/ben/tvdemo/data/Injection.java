package cn.ben.tvdemo.data;

import android.content.Context;

import cn.ben.tvdemo.constant.Constants;
import cn.ben.tvdemo.data.tvchannel.source.TVChannelsRepository;
import cn.ben.tvdemo.data.tvchannel.source.local.TVChannelsLocalDataSource;
import cn.ben.tvdemo.data.tvchannel.source.remote.TVChannelsRemoteDataSource;
import cn.ben.tvdemo.data.tvshow.source.TVShowsRepository;
import cn.ben.tvdemo.data.tvshow.source.local.TVShowsLocalDataSource;
import cn.ben.tvdemo.data.tvshow.source.remote.TVShowsRemoteDataSource;
import cn.ben.tvdemo.data.tvtype.source.TVTypesRepository;
import cn.ben.tvdemo.data.tvtype.source.local.TVTypesLocalDataSource;
import cn.ben.tvdemo.data.tvtype.source.remote.TVTypesRemoteDataSource;
import cn.ben.tvdemo.util.schedulers.BaseSchedulerProvider;
import cn.ben.tvdemo.util.schedulers.SchedulerProvider;

public class Injection {
    public static TVTypesRepository provideTVTypesRepository(Context context) {
        return TVTypesRepository.getInstance(
                TVTypesRemoteDataSource.getInstance(),
                TVTypesLocalDataSource.getInstance(),
                context.getSharedPreferences(Constants.SP_KEY_REPO_LAST_SAVE_LOCAL_DATE, Context.MODE_PRIVATE)
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

    public static TVShowsRepository provideTVShowsRepository() {
        return TVShowsRepository.getInstance(
                TVShowsRemoteDataSource.getInstance(),
                TVShowsLocalDataSource.getInstance()
        );
    }
}
