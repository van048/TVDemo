package cn.ben.tvdemo.tvshows;

import android.support.annotation.NonNull;

import java.util.List;

import cn.ben.tvdemo.data.tvshow.TVShows;
import cn.ben.tvdemo.data.tvshow.source.TVShowsRepository;
import cn.ben.tvdemo.util.TimeUtil;
import cn.ben.tvdemo.util.schedulers.BaseSchedulerProvider;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import static com.google.common.base.Preconditions.checkNotNull;

public class TVShowsPresenter implements TVShowsContract.Presenter {
    private final String mTVChannelCode;
    private final int mInc;
    private final TVShowsRepository mTVShowsRepository;
    private final TVShowsContract.View mTVShowsView;
    private final BaseSchedulerProvider mSchedulerProvider;
    private final CompositeDisposable mDisposables;

    TVShowsPresenter(@NonNull String code,
                     int inc,
                     @NonNull TVShowsRepository repository,
                     @NonNull TVShowsContract.View view,
                     @NonNull BaseSchedulerProvider schedulerProvider) {
        mTVChannelCode = checkNotNull(code);
        mInc = inc;
        mTVShowsRepository = checkNotNull(repository);
        mTVShowsView = checkNotNull(view);
        mSchedulerProvider = checkNotNull(schedulerProvider);
        mDisposables = new CompositeDisposable();
    }

    @Override
    public void onUserVisible() {
        loadTVShows();
    }

    private void loadTVShows() {
        mTVShowsView.showLoadingUI();
        mTVShowsRepository
                .getTVShowsWithCodeAndDate(mTVChannelCode, TimeUtil.plusOnCurrentDate(mInc, TimeUtil.FORMAT_YEAR_MONTH_DAY))
                .subscribeOn(mSchedulerProvider.io())
                .observeOn(mSchedulerProvider.ui())
                .subscribe(new Observer<List<TVShows.TVShow>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposables.add(d);
                    }

                    @Override
                    public void onNext(List<TVShows.TVShow> value) {
                        mTVShowsView.showTVShows(value);
                        mTVShowsView.stopLoadingUI();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mTVShowsView.showTips("Load Failed: " + e.getMessage());
                        mTVShowsView.stopLoadingUI();
                    }

                    @Override
                    public void onComplete() {
                        mTVShowsView.stopLoadingUI();
                    }
                });
    }

    @Override
    public void onUserInvisible() {
        mDisposables.clear();
    }

    @Override
    public void switchFavState(TVShows.TVShow tvShow) {

    }

    @Override
    public void refreshTVShows() {
        mTVShowsRepository.invalidCache(mTVChannelCode, TimeUtil.plusOnCurrentDate(mInc, TimeUtil.FORMAT_YEAR_MONTH_DAY));
        mTVShowsRepository
                .getTVShowsWithCodeAndDate(mTVChannelCode, TimeUtil.plusOnCurrentDate(mInc, TimeUtil.FORMAT_YEAR_MONTH_DAY))
                .subscribeOn(mSchedulerProvider.io())
                .observeOn(mSchedulerProvider.ui())
                .subscribe(new Observer<List<TVShows.TVShow>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposables.add(d);
                    }

                    @Override
                    public void onNext(List<TVShows.TVShow> value) {
                        mTVShowsView.showTVShows(value);
                        mTVShowsView.stopRefreshing();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mTVShowsView.showTips("Refresh Failed: " + e.getMessage());
                        mTVShowsView.stopRefreshing();
                    }

                    @Override
                    public void onComplete() {
                        mTVShowsView.stopRefreshing();
                    }
                });
    }
}
