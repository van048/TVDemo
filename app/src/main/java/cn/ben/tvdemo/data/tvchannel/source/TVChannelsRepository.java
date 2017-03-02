package cn.ben.tvdemo.data.tvchannel.source;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ben.tvdemo.data.tvchannel.TVChannels;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;

public class TVChannelsRepository implements TVChannelsDataSource {
    private volatile static TVChannelsRepository instance = null;

    private final TVChannelsDataSource mTVChannelsRemoteDataSource;
    private final TVChannelsDataSource mTVChannelsLocalDataSource;
    private Map<String, List<TVChannels.TVChannel>> mCachedTVChannels;

    private boolean mCacheIsDirty;

    private TVChannelsRepository(@NonNull TVChannelsDataSource tvChannelsRemoteDataSource,
                                 @NonNull TVChannelsDataSource tvChannelsLocalDataSource) {
        mTVChannelsRemoteDataSource = checkNotNull(tvChannelsRemoteDataSource);
        mTVChannelsLocalDataSource = checkNotNull(tvChannelsLocalDataSource);
    }

    public static TVChannelsRepository getInstance(TVChannelsDataSource remote, TVChannelsDataSource local) {
        if (instance == null) {
            synchronized (TVChannelsRepository.class) {
                if (instance == null) {
                    instance = new TVChannelsRepository(remote, local);
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
    public Observable<List<TVChannels.TVChannel>> getTVChannelsWithPID(String pId) {
        if (mCachedTVChannels != null && !mCacheIsDirty) {
            List<TVChannels.TVChannel> mList = mCachedTVChannels.get(pId);
            if (mList == null) {
                mList = new ArrayList<>();
            }
            mCachedTVChannels.put(pId, mList);

            return Observable.just(mList).doOnNext(new Consumer<List<TVChannels.TVChannel>>() {
                @Override
                public void accept(List<TVChannels.TVChannel> tvChannels) throws Exception {
                    Log.d("ben", "from cache");
                }
            });
        }

        Observable<List<TVChannels.TVChannel>> remoteTVTypes = getAndSaveRemoteTVChannels(pId);

        if (mCacheIsDirty) {
            return remoteTVTypes;
        } else {
            // mCachedTVChannels null, use local
            // if error, use remote instead
            return getAndCacheLocalTVChannels(pId)
                    .onErrorResumeNext(remoteTVTypes);
        }
    }

    private Observable<List<TVChannels.TVChannel>> getAndCacheLocalTVChannels(final String pId) {
        return mTVChannelsLocalDataSource
                .getTVChannelsWithPID(pId)
                .doOnNext(new Consumer<List<TVChannels.TVChannel>>() {
                    @Override
                    public void accept(List<TVChannels.TVChannel> tvChannels) throws Exception {
                        Log.d("ben", "from local");
                        refreshCacheWithPID(pId, tvChannels);
                    }
                });
    }

    private Observable<List<TVChannels.TVChannel>> getAndSaveRemoteTVChannels(final String pId) {
        return mTVChannelsRemoteDataSource
                .getTVChannelsWithPID(pId)
                .doOnNext(new Consumer<List<TVChannels.TVChannel>>() {
                    @Override
                    public void accept(List<TVChannels.TVChannel> tvChannels) throws Exception {
                        Log.d("ben", "from remote");
                        refreshLocalDataSourceWithPID(pId, tvChannels);
                        refreshCacheWithPID(pId, tvChannels);
                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mCacheIsDirty = false;
                    }
                });
    }

    @Override
    public void deleteAllTVChannelsWithPID(String pId) {
        mTVChannelsLocalDataSource.deleteAllTVChannelsWithPID(pId);
        mTVChannelsRemoteDataSource.deleteAllTVChannelsWithPID(pId);
        if (mCachedTVChannels == null) {
            mCachedTVChannels = new HashMap<>();
        }
        mCachedTVChannels.remove(pId);
    }

    @Override
    public void saveTVChannel(@NonNull TVChannels.TVChannel tvChannel) {
        checkNotNull(tvChannel);
        mTVChannelsLocalDataSource.saveTVChannel(tvChannel);
        mTVChannelsRemoteDataSource.saveTVChannel(tvChannel);
        if (mCachedTVChannels == null) {
            mCachedTVChannels = new HashMap<>();
        }
        List<TVChannels.TVChannel> channels = mCachedTVChannels.get(tvChannel.getPId());
        if (channels == null) {
            channels = new ArrayList<>();
        }
        channels.add(tvChannel);
        mCachedTVChannels.put(tvChannel.getPId(), channels);
    }

    @Override
    public void cleanup() {
        mTVChannelsLocalDataSource.cleanup();
        mTVChannelsRemoteDataSource.cleanup();
    }

    private void refreshCacheWithPID(String pId, List<TVChannels.TVChannel> tvChannels) {
        if (mCachedTVChannels == null) {
            mCachedTVChannels = new HashMap<>();
        }
        mCachedTVChannels.remove(pId);
        mCachedTVChannels.put(pId, tvChannels);
        mCacheIsDirty = false;
    }

    private void refreshLocalDataSourceWithPID(String pId, List<TVChannels.TVChannel> tvChannels) {
        mTVChannelsLocalDataSource.deleteAllTVChannelsWithPID(pId);
        for (TVChannels.TVChannel tvType : tvChannels) {
            mTVChannelsLocalDataSource.saveTVChannel(tvType);
        }
    }

    public void invalidCache() {
        mCacheIsDirty = true;
    }
}
