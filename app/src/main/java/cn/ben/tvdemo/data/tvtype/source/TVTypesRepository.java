package cn.ben.tvdemo.data.tvtype.source;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ben.tvdemo.data.tvtype.TVTypes;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

import static com.google.common.base.Preconditions.checkNotNull;

public class TVTypesRepository implements TVTypesDataSource {
    private volatile static TVTypesRepository instance = null;

    private final TVTypesDataSource mTVTypesRemoteDataSource;
    private final TVTypesDataSource mTVTypesLocalDataSource;
    private Map<String, TVTypes.TVType> mCachedTVTypes;

    private boolean mCacheIsDirty;
    private final Predicate<List<TVTypes.TVType>> NON_EMPTY_PREDICATE = new Predicate<List<TVTypes.TVType>>() {
        @Override
        public boolean test(List<TVTypes.TVType> tvTypes) throws Exception {
            return !tvTypes.isEmpty();
        }
    };
    private final List<TVTypes.TVType> EMPTY_RETURNED_LIST = new ArrayList<>();

    private TVTypesRepository(@NonNull TVTypesDataSource tvTypesRemoteDataSource,
                              @NonNull TVTypesDataSource tvTypesLocalDataSource) {
        mTVTypesRemoteDataSource = checkNotNull(tvTypesRemoteDataSource);
        mTVTypesLocalDataSource = checkNotNull(tvTypesLocalDataSource);
    }

    public static TVTypesRepository getInstance(TVTypesDataSource remote, TVTypesDataSource local) {
        if (instance == null) {
            synchronized (TVTypesRepository.class) {
                if (instance == null) {
                    instance = new TVTypesRepository(remote, local);
                }
            }
        }
        return instance;
    }

    public static void destroyInstance() {
        instance.cleanup();
        instance = null;
    }

    @Override
    public Observable<List<TVTypes.TVType>> getTVTypes() {
        if (mCachedTVTypes != null && !mCacheIsDirty) {
            List<TVTypes.TVType> mList = new ArrayList<>(mCachedTVTypes.values());
            return Observable.just(mList);
        }

        Observable<List<TVTypes.TVType>> remoteTVTypes = getAndSaveRemoteTVTypes();

        if (mCacheIsDirty) {
            return remoteTVTypes;
        } else {
            // mCachedTVTypes null, use local
            // if error, use remote instead
            return getAndCacheLocalTVTypes()
                    .onErrorResumeNext(remoteTVTypes);
        }
    }

    private Observable<List<TVTypes.TVType>> getAndCacheLocalTVTypes() {
        return mTVTypesLocalDataSource
                .getTVTypes()
                .doOnNext(new Consumer<List<TVTypes.TVType>>() {
                    @Override
                    public void accept(List<TVTypes.TVType> tvTypes) throws Exception {
                        refreshCache(tvTypes);
                    }
                });
    }

    private Observable<List<TVTypes.TVType>> getAndSaveRemoteTVTypes() {
        return mTVTypesRemoteDataSource
                .getTVTypes()
                .doOnNext(new Consumer<List<TVTypes.TVType>>() {
                    @Override
                    public void accept(List<TVTypes.TVType> tvTypes) throws Exception {
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
    }

    public void invalidCache() {
        mCacheIsDirty = true;
    }
}
