package cn.ben.tvdemo.data.tvchannel.source.remote;

import java.util.List;

import cn.ben.tvdemo.constant.Constants;
import cn.ben.tvdemo.data.tvchannel.TVChannels;
import cn.ben.tvdemo.data.tvchannel.source.TVChannelsDataSource;
import io.reactivex.Observable;
import io.reactivex.functions.Function;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class TVChannelsRemoteDataSource implements TVChannelsDataSource {
    private static final String LOAD_TV_CHANNELS_BASE_URL = "http://api.avatardata.cn/TVTime/";
    private volatile static TVChannelsRemoteDataSource instance = null;

    public static TVChannelsRemoteDataSource getInstance() {
        if (instance == null) {
            synchronized (TVChannelsRemoteDataSource.class) {
                if (instance == null) {
                    instance = new TVChannelsRemoteDataSource();
                }
            }
        }
        return instance;
    }

    private TVChannelsRemoteDataSource() {
    }

    @Override
    public Observable<List<TVChannels.TVChannel>> getTVChannelsWithPID(String pId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(LOAD_TV_CHANNELS_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        TVChannelsService service = retrofit.create(TVChannelsService.class);
        Observable<TVChannels> call = service.getTVChannels(Constants.API_KEY, pId);

        return call
                .map(new Function<TVChannels, List<TVChannels.TVChannel>>() {
                    @Override
                    public List<TVChannels.TVChannel> apply(TVChannels tvChannels) throws Exception {
                        if (tvChannels.getError_code() != 0)
                            throw new Exception(tvChannels.getReason());
                        return tvChannels.getResult();
                    }
                });
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

    @SuppressWarnings("SameParameterValue")
    interface TVChannelsService {
        @GET("LookUp")
        Observable<TVChannels> getTVChannels(@Query("key") String key, @Query("pId") String pId);
    }
}
