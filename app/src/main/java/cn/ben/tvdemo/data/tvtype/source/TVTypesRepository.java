package cn.ben.tvdemo.data.tvtype.source;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.content.SharedPreferencesCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ben.tvdemo.data.tvtype.TVTypes;
import cn.ben.tvdemo.util.TimeUtil;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

import static com.google.common.base.Preconditions.checkNotNull;

public class TVTypesRepository implements TVTypesDataSource {
    private volatile static TVTypesRepository instance = null;

    private final TVTypesDataSource mTVTypesRemoteDataSource;
    private final TVTypesDataSource mTVTypesLocalDataSource;
    private final SharedPreferences mRepoLastSaveLocalDateSP;
    private final Predicate<List<TVTypes.TVType>> FILTER_EMPTY_LIST_PREDICATE = new Predicate<List<TVTypes.TVType>>() {
        @Override
        public boolean test(List<TVTypes.TVType> tvTypes) throws Exception {
            return !tvTypes.isEmpty();
        }
    };
    private Map<String, TVTypes.TVType> mCachedTVTypes;
    private boolean mCacheIsDirty = true;

    private TVTypesRepository(@NonNull TVTypesDataSource tvTypesRemoteDataSource,
                              @NonNull TVTypesDataSource tvTypesLocalDataSource,
                              @NonNull SharedPreferences repoLastSaveLocalDateSP) {
        mTVTypesRemoteDataSource = checkNotNull(tvTypesRemoteDataSource);
        mTVTypesLocalDataSource = checkNotNull(tvTypesLocalDataSource);
        mRepoLastSaveLocalDateSP = checkNotNull(repoLastSaveLocalDateSP);
    }

    public static TVTypesRepository getInstance(TVTypesDataSource remote, TVTypesDataSource local, SharedPreferences sharedPreferences) {
        if (instance == null) {
            synchronized (TVTypesRepository.class) {
                if (instance == null) {
                    instance = new TVTypesRepository(remote, local, sharedPreferences);
                }
            }
        }
        return instance;
    }

    public static void destroyInstance() {
        if (instance != null) {
            instance.cleanup();
            instance = null;
        }
    }

    @Override
    public Observable<List<TVTypes.TVType>> getTVTypes() {
        if (mCachedTVTypes != null && !mCacheIsDirty) {
            // cache is valid, get it and return
            List<TVTypes.TVType> mList = new ArrayList<>(mCachedTVTypes.values());
            return Observable
                    .just(mList)
                    .filter(FILTER_EMPTY_LIST_PREDICATE)
                    .doOnNext(new Consumer<List<TVTypes.TVType>>() {
                        @Override
                        public void accept(List<TVTypes.TVType> tvTypes) throws Exception {
                            Log.d("ben", "from cache");
                        }
                    });
        }

        Observable<List<TVTypes.TVType>> remoteTVTypes = getAndSaveRemoteTVTypes();

        String dateStr = mRepoLastSaveLocalDateSP.getString(TVTypesRepository.class.getSimpleName(), "");
        Date lastSaveLocalDate = dateStr.isEmpty() ? TimeUtil.plusOnCurrentDate(-1) : TimeUtil.string2Date(dateStr, TimeUtil.FORMAT_YEAR_MONTH_DAY);
        if (TimeUtil.areSameDay(lastSaveLocalDate, new Date())) {
            Log.d("ben", "same day");
            // read local first
            return getAndCacheLocalTVTypes()
                    .switchIfEmpty(remoteTVTypes);
        }
        Log.d("ben", "diff day");

        if (mCacheIsDirty) {
            return remoteTVTypes.onErrorResumeNext(getAndCacheLocalTVTypes());
        } else {
            // abnormal state
            return getAndCacheLocalTVTypes()
                    .switchIfEmpty(remoteTVTypes);
        }
    }

    private Observable<List<TVTypes.TVType>> getAndCacheLocalTVTypes() {
        return mTVTypesLocalDataSource
                .getTVTypes()
                .filter(FILTER_EMPTY_LIST_PREDICATE)
                .doOnNext(new Consumer<List<TVTypes.TVType>>() {
                    @Override
                    public void accept(List<TVTypes.TVType> tvTypes) throws Exception {
                        Log.d("ben", "from local");
                        refreshCache(tvTypes);
                    }
                });
    }

    private Observable<List<TVTypes.TVType>> getAndSaveRemoteTVTypes() {
        return mTVTypesRemoteDataSource
                .getTVTypes()
                .filter(FILTER_EMPTY_LIST_PREDICATE)
                .doOnNext(new Consumer<List<TVTypes.TVType>>() {
                    @Override
                    public void accept(List<TVTypes.TVType> tvTypes) throws Exception {
                        Log.d("ben", "from remote");
                        refreshLocalDataSource(tvTypes);
                        refreshCache(tvTypes);
                    }
                });
    }

    @Override
    public void deleteAllTVTypes() {
        mTVTypesLocalDataSource.deleteAllTVTypes();
        mTVTypesRemoteDataSource.deleteAllTVTypes();
        if (mCachedTVTypes == null) {
            mCachedTVTypes = new HashMap<>();
        }
        mCachedTVTypes.clear();
    }

    @Override
    public void saveTVType(@NonNull TVTypes.TVType tvType) {
        checkNotNull(tvType);
        mTVTypesLocalDataSource.saveTVType(tvType);
        mTVTypesRemoteDataSource.saveTVType(tvType);
        if (mCachedTVTypes == null) {
            mCachedTVTypes = new HashMap<>();
        }
        mCachedTVTypes.put(tvType.getId(), tvType);
    }

    @Override
    public void cleanup() {
        mTVTypesLocalDataSource.cleanup();
        mTVTypesRemoteDataSource.cleanup();
    }

    private void refreshCache(List<TVTypes.TVType> tvTypes) {
        if (mCachedTVTypes == null) {
            mCachedTVTypes = new HashMap<>();
        }
        mCachedTVTypes.clear();
        for (TVTypes.TVType tvType : tvTypes) {
            mCachedTVTypes.put(tvType.getId(), tvType);
        }
        mCacheIsDirty = false;
    }

    private void refreshLocalDataSource(List<TVTypes.TVType> tvTypes) {
        mTVTypesLocalDataSource.deleteAllTVTypes();
        for (TVTypes.TVType tvType : tvTypes) {
            mTVTypesLocalDataSource.saveTVType(tvType);
        }

        // write sp
        SharedPreferences.Editor editor = mRepoLastSaveLocalDateSP.edit().putString(TVTypesRepository.class.getSimpleName(), TimeUtil.date2String(new Date(), TimeUtil.FORMAT_YEAR_MONTH_DAY));
        SharedPreferencesCompat.EditorCompat.getInstance().apply(editor);
    }

    public void invalidCache() {
        mCacheIsDirty = true;
    }
}
