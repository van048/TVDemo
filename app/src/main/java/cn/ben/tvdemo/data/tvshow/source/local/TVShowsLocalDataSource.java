package cn.ben.tvdemo.data.tvshow.source.local;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import cn.ben.tvdemo.data.DBOpenHelper;
import cn.ben.tvdemo.data.tvchannel.TVChannels;
import cn.ben.tvdemo.data.tvshow.TVShows;
import cn.ben.tvdemo.data.tvshow.source.TVShowsDataSource;
import io.reactivex.Observable;

// TODO: 2017/3/5
public class TVShowsLocalDataSource implements TVShowsDataSource {
    private volatile static TVShowsLocalDataSource instance = null;
    private final DBOpenHelper mDbHelper;
    private final Executor mExecutor;

    private final List<TVChannels.TVChannel> mGetResultTmp = new ArrayList<>();
    private volatile boolean mGetDone = false;

    public static TVShowsLocalDataSource getInstance() {
        if (instance == null) {
            synchronized (TVShowsLocalDataSource.class) {
                if (instance == null) {
                    instance = new TVShowsLocalDataSource();
                }
            }
        }
        return instance;
    }

    private TVShowsLocalDataSource() {
        mDbHelper = DBOpenHelper.getInstance();
        mExecutor = Executors.newSingleThreadExecutor();
    }

    @Override
    public Observable<List<TVShows.TVShow>> getTVShowsWithCodeAndDate(String code, String date) {
        return Observable.empty();
    }

    @Override
    public Observable<List<TVShows.TVShow>> getFavTVShows() {
        return null;
    }

    @Override
    public void cleanup() {
        mDbHelper.close();
        instance = null;
    }
}
