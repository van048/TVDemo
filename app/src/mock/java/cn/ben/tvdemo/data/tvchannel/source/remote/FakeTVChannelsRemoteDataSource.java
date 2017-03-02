package cn.ben.tvdemo.data.tvchannel.source.remote;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.ben.tvdemo.data.tvchannel.TVChannels;
import cn.ben.tvdemo.data.tvchannel.source.TVChannelsDataSource;
import io.reactivex.Observable;

public class FakeTVChannelsRemoteDataSource implements TVChannelsDataSource {
    private volatile static FakeTVChannelsRemoteDataSource instance = null;

    public static FakeTVChannelsRemoteDataSource getInstance() {
        if (instance == null) {
            synchronized (FakeTVChannelsRemoteDataSource.class) {
                if (instance == null) {
                    instance = new FakeTVChannelsRemoteDataSource();
                }
            }
        }
        return instance;
    }

    private FakeTVChannelsRemoteDataSource() {
    }

    @Override
    public Observable<List<TVChannels.TVChannel>> getTVChannelsWithPID(String pId) {
        List<TVChannels.TVChannel> list = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < random.nextInt(20) + 1; i++) {
            list.add(new TVChannels.TVChannel(pId, pId + "_channelName_" + i, pId + "_rel_" + i, pId + "_url" + i));
        }
        return Observable.just(list);
    }

    @Override
    public void deleteAllTVChannelsWithPID(String pId) {
    }

    @Override
    public void saveTVChannel(TVChannels.TVChannel tvChannel) {
    }

    @Override
    public void cleanup() {
        instance = null;
    }
}
