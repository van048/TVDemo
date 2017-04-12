package cn.ben.tvdemo.data.tvshow.source.remote;

import java.util.List;

import cn.ben.tvdemo.constant.Constants;
import cn.ben.tvdemo.data.tvshow.TVShows;
import cn.ben.tvdemo.data.tvshow.source.TVShowsDataSource;
import io.reactivex.Observable;
import io.reactivex.functions.Function;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

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
        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(LOAD_TV_SHOWS_BASE_URL)
                .build();
        TVShowsService tvShowsService = retrofit.create(TVShowsService.class);
        Observable<TVShows> tvShows = tvShowsService.getTVShows(Constants.API_KEY, code, date);
        return tvShows.map(new Function<TVShows, List<TVShows.TVShow>>() {
            @Override
            public List<TVShows.TVShow> apply(TVShows tvShows) throws Exception {
                if (tvShows.getError_code() != 0) {
                    throw new Exception(tvShows.getReason());
                } else {
                    return tvShows.getResult();
                }
            }
        });
    }

    @Override
    public Observable<List<TVShows.TVShow>> getFavTVShows() {
        // TODO: 2017/3/12 no server, always empty
        return Observable.empty();
    }

    @Override
    public void cleanup() {
        instance = null;
    }

    @Override
    public void update(String code, String date, List<TVShows.TVShow> shows) {
    }

    @SuppressWarnings("SameParameterValue")
    interface TVShowsService {
        @GET("TVlist")
        Observable<TVShows> getTVShows(@Query("key") String key, @Query("code") String code, @Query("date") String date);
    }
}
