package cn.ben.tvdemo.data.tvshow.source.remote;

import java.util.List;

import cn.ben.tvdemo.data.tvshow.TVShows;
import cn.ben.tvdemo.data.tvshow.source.TVShowsDataSource;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

// TODO: 2017/3/5
public class TVShowsRemoteDataSource implements TVShowsDataSource {
    private static final String LOAD_TV_SHOWS_BASE_URL = "http://api.avatardata.cn/TVTime/";
    private volatile static TVShowsRemoteDataSource instance = null;

    public static TVShowsRemoteDataSource getInstance() {
        if (instance == null) {
            synchronized (TVShowsRemoteDataSource.class) {
                if (instance == null) {
                    instance = new TVShowsRemoteDataSource();
                }
            }
        }
        return instance;
    }

    private TVShowsRemoteDataSource() {
    }

    @Override
    public Observable<List<TVShows.TVShow>> getTVShowsWithCodeAndDate(String code, String date) {
        return null;
    }

    @Override
    public Observable<List<TVShows.TVShow>> getFavTVShows() {
        return null;
    }

    @Override
    public void cleanup() {
        instance = null;
    }

    @SuppressWarnings("SameParameterValue")
    interface TVShowsService {
        @GET("TVlist")
        Observable<TVShows> getTVShows(@Query("key") String key, @Query("code") String code, @Query("date") String date);
    }
}
