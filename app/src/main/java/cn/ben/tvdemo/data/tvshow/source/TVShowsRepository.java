package cn.ben.tvdemo.data.tvshow.source;


import android.util.Log;

import java.util.HashMap;
import java.util.List;

import cn.ben.tvdemo.data.tvshow.TVShows;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

public class TVShowsRepository implements TVShowsDataSource {
    private volatile static TVShowsRepository instance = null;
    private final TVShowsDataSource mTVShowsRemoteDataSource;
    private final TVShowsDataSource mTVShowsLocalDataSource;

    private final HashMap<String, HashMap<String, List<TVShows.TVShow>>> mShowsCache;
    private final HashMap<String, HashMap<String, Boolean>> mCacheIsDirtyMap;
    private final Predicate<List<TVShows.TVShow>> FILTER_NON_EMPTY_PREDICATE = new Predicate<List<TVShows.TVShow>>() {
        @Override
        public boolean test(List<TVShows.TVShow> shows) throws Exception {
            return !shows.isEmpty();
        }
    };

    private TVShowsRepository(TVShowsDataSource remote, TVShowsDataSource local) {
        mTVShowsRemoteDataSource = remote;
        mTVShowsLocalDataSource = local;
        mShowsCache = new HashMap<>();
        mCacheIsDirtyMap = new HashMap<>();
    }

    public static TVShowsRepository getInstance(TVShowsDataSource remote,
                                                TVShowsDataSource local) {
        if (instance == null) {
            synchronized (TVShowsRepository.class) {
                if (instance == null) {
                    instance = new TVShowsRepository(remote, local);
                }
            }
        }
        return instance;
    }

    @Override
    public Observable<List<TVShows.TVShow>> getTVShowsWithCodeAndDate(final String code, final String date) {
        Observable<List<TVShows.TVShow>> remote =
                mTVShowsRemoteDataSource
                        .getTVShowsWithCodeAndDate(code, date)
                        .filter(FILTER_NON_EMPTY_PREDICATE)
                        .doOnNext(new Consumer<List<TVShows.TVShow>>() {
                            @Override
                            public void accept(List<TVShows.TVShow> shows) throws Exception {
                                refreshLocalDataSource(code, date, shows);
                                refreshCache(code, date, shows);
                                Log.d("ben", "shows from remote");
                            }
                        });
        Observable<List<TVShows.TVShow>> local =
                mTVShowsLocalDataSource
                        .getTVShowsWithCodeAndDate(code, date)
                        .filter(FILTER_NON_EMPTY_PREDICATE)
                        .doOnNext(new Consumer<List<TVShows.TVShow>>() {
                            @Override
                            public void accept(List<TVShows.TVShow> shows) throws Exception {
                                refreshCache(code, date, shows);
                                Log.d("ben", "shows from local");
                            }
                        });
        HashMap<String, Boolean> codeDirtyMap = mCacheIsDirtyMap.get(code);
        if (codeDirtyMap == null) {
            // code all dirty
            return remote;
        }
        Boolean dateDirty = codeDirtyMap.get(date);
        if (dateDirty == null || dateDirty) {
            return remote;
        }
        // now says cache is valid
        HashMap<String, List<TVShows.TVShow>> codeCacheMap = mShowsCache.get(code);
        if (codeCacheMap == null) {
            return local.switchIfEmpty(remote);
        }
        List<TVShows.TVShow> list = codeCacheMap.get(date);
        if (list == null) {
            return local.switchIfEmpty(remote);
        }
        return Observable
                .just(list)
                .filter(FILTER_NON_EMPTY_PREDICATE)
                .doOnNext(new Consumer<List<TVShows.TVShow>>() {
                    @Override
                    public void accept(List<TVShows.TVShow> shows) throws Exception {
                        Log.d("ben", "shows from cache");
                    }
                });
    }

    private void refreshCache(String code, String date, List<TVShows.TVShow> shows) {
        HashMap<String, List<TVShows.TVShow>> codeCacheMap = mShowsCache.get(code);
        if (codeCacheMap == null) {
            codeCacheMap = new HashMap<>();
        }
        codeCacheMap.put(date, shows);
        mShowsCache.put(code, codeCacheMap);

        HashMap<String, Boolean> codeDirtyMap = mCacheIsDirtyMap.get(code);
        if (codeDirtyMap == null) {
            codeDirtyMap = new HashMap<>();
        }
        codeDirtyMap.put(date, false);
        mCacheIsDirtyMap.put(code, codeDirtyMap);
    }

    private void refreshLocalDataSource(String code, String date, List<TVShows.TVShow> shows) {
        // TODO: 2017/3/5
    }

    @Override
    public Observable<List<TVShows.TVShow>> getFavTVShows() {
        // TODO: 2017/3/5
        return null;
    }

    @Override
    public void cleanup() {
        instance = null;
    }

    public void invalidCache(String code, String date) {
        HashMap<String, Boolean> codeDirtyMap = mCacheIsDirtyMap.get(code);
        if (codeDirtyMap == null) {
            codeDirtyMap = new HashMap<>();
        }
        codeDirtyMap.put(date, true);
        mCacheIsDirtyMap.put(code, codeDirtyMap);
    }
}
