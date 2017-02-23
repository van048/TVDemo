package cn.ben.tvdemo.data.tvtype.source;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ben.tvdemo.data.tvtype.TVTypes;

import static com.google.common.base.Preconditions.checkNotNull;

public class TVTypesRepository implements TVTypesDataSource {
    private volatile static TVTypesRepository instance = null;
    private final TVTypesDataSource mTVTypesRemoteDataSource;
    private final TVTypesDataSource mTVTypesLocalDataSource;

    private Map<String, TVTypes.TVType> mCachedTVTypes;
    private boolean mCacheIsDirty = false;

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
        instance = null;
    }

    @Override
    public void getTVTypes(@NonNull final LoadTVTypesCallback callback) {
        checkNotNull(callback);

        if (mCachedTVTypes != null && !mCacheIsDirty) {
            callback.onTVTypesLoaded(new ArrayList<>(mCachedTVTypes.values()));
            return;
        }

        if (mCacheIsDirty) {
            getTVTypesFromRemoteDataSource(callback);
        } else {
            mTVTypesLocalDataSource.getTVTypes(new LoadTVTypesCallback() {
                @Override
                public void onTVTypesLoaded(List<TVTypes.TVType> tvTypes) {
                    refreshCache(tvTypes);
                    callback.onTVTypesLoaded(new ArrayList<>(mCachedTVTypes.values()));
                }

                @Override
                public void onDataNotAvailable(String reason) {
                    getTVTypesFromRemoteDataSource(callback);
                }
            });
        }
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

    private void getTVTypesFromRemoteDataSource(@NonNull final LoadTVTypesCallback callback) {
        mTVTypesRemoteDataSource.getTVTypes(new LoadTVTypesCallback() {
            @Override
            public void onTVTypesLoaded(List<TVTypes.TVType> tvTypes) {
                refreshCache(tvTypes);
                refreshLocalDataSource(tvTypes);
                callback.onTVTypesLoaded(new ArrayList<>(mCachedTVTypes.values()));
            }

            @Override
            public void onDataNotAvailable(String reason) {
                callback.onDataNotAvailable(reason);
            }
        });
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
