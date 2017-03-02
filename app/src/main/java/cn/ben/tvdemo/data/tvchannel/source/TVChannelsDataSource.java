package cn.ben.tvdemo.data.tvchannel.source;

import java.util.List;

import cn.ben.tvdemo.data.tvchannel.TVChannels;
import io.reactivex.Observable;

public interface TVChannelsDataSource {

    Observable<List<TVChannels.TVChannel>> getTVChannelsWithPID(String pId);

    void deleteAllTVChannelsWithPID(String pId);

    void saveTVChannel(TVChannels.TVChannel tvChannel);

    void cleanup();
}
