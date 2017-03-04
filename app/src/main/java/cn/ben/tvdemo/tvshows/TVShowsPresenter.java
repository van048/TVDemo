package cn.ben.tvdemo.tvshows;

import android.support.annotation.NonNull;

import cn.ben.tvdemo.util.schedulers.BaseSchedulerProvider;
import io.reactivex.disposables.CompositeDisposable;

import static com.google.common.base.Preconditions.checkNotNull;

public class TVShowsPresenter implements TVShowsContract.Presenter {
    private final String mTVChannelCode;
    // TODO: 2017/3/3  
//    private final TVShowsRepository mTVShowsRepository;
    private final TVShowsContract.View mTVShowsView;
    private final BaseSchedulerProvider mSchedulerProvider;
    private final CompositeDisposable mDisposables;

    public TVShowsPresenter(@NonNull String code,
//                            @NonNull TVShowsRepository repository,
                            @NonNull TVShowsContract.View view,
                            @NonNull BaseSchedulerProvider schedulerProvider) {
        mTVChannelCode = checkNotNull(code);
//        mTVShowsRepository = checkNotNull(repository);
        mTVShowsView = checkNotNull(view);
        mSchedulerProvider = checkNotNull(schedulerProvider);
        mDisposables = new CompositeDisposable();
    }

    @Override
    public void onUserVisible() {
        // TODO: 2017/3/3
    }

    @Override
    public void onUserInvisible() {
        mDisposables.clear();
    }
}
