package cn.ben.tvdemo.tvchannels;

import android.support.annotation.NonNull;

import java.util.List;

import cn.ben.tvdemo.data.tvchannel.TVChannels;
import cn.ben.tvdemo.data.tvchannel.source.TVChannelsRepository;
import cn.ben.tvdemo.util.schedulers.BaseSchedulerProvider;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import static com.google.common.base.Preconditions.checkNotNull;

public class TVChannelsPresenter implements TVChannelsContract.Presenter {
    private final String mTVTypeId;
    private final TVChannelsRepository mTVChannelsRepository;
    private final TVChannelsContract.View mTVChannelsView;
    private final BaseSchedulerProvider mSchedulerProvider;
    private final CompositeDisposable mDisposables;

    public TVChannelsPresenter(@NonNull String id,
                               @NonNull TVChannelsRepository repository,
                               @NonNull TVChannelsContract.View view,
                               @NonNull BaseSchedulerProvider schedulerProvider) {
        mTVTypeId = checkNotNull(id);
        mTVChannelsRepository = checkNotNull(repository);
        mTVChannelsView = checkNotNull(view);
        mSchedulerProvider = checkNotNull(schedulerProvider);
        mDisposables = new CompositeDisposable();
    }

    @Override
    public void onUserVisible() {
        loadTVChannels();
    }

    private void loadTVChannels() {
        mDisposables.clear();
        mTVChannelsRepository
                .getTVChannelsWithPID(mTVTypeId)
                .subscribeOn(mSchedulerProvider.io())
                .observeOn(mSchedulerProvider.ui())
                .subscribe(new Observer<List<TVChannels.TVChannel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposables.add(d);
                    }

                    @Override
                    public void onNext(List<TVChannels.TVChannel> value) {
                        if (value.isEmpty()) {
                            mTVChannelsView.showTips("No TV Channels");
                        } else {
                            mTVChannelsView.showTVChannels(value);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mTVChannelsView.showTips(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Override
    public void onUserInvisible() {
        mDisposables.clear();
        mTVChannelsView.stopRefreshing();
    }
}
