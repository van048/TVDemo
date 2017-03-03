package cn.ben.tvdemo.mainpage.shows;

import android.support.annotation.NonNull;

import java.util.List;

import cn.ben.tvdemo.data.tvtype.TVTypes;
import cn.ben.tvdemo.data.tvtype.source.TVTypesRepository;
import cn.ben.tvdemo.util.schedulers.BaseSchedulerProvider;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import static com.google.common.base.Preconditions.checkNotNull;

public class ShowsPresenter implements ShowsContract.Presenter {

    @NonNull
    private final TVTypesRepository mTVTypesRepository;

    @NonNull
    private final ShowsContract.View mShowsView;

    @NonNull
    private final BaseSchedulerProvider mSchedulerProvider;

    @NonNull
    private final CompositeDisposable mDisposables;

    public ShowsPresenter(@NonNull TVTypesRepository tvTypesRepository,
                          @NonNull ShowsContract.View showsView,
                          @NonNull BaseSchedulerProvider schedulerProvider) {
        mShowsView = checkNotNull(showsView);
        mTVTypesRepository = checkNotNull(tvTypesRepository);
        mSchedulerProvider = checkNotNull(schedulerProvider);

        mDisposables = new CompositeDisposable();
    }

    @Override
    public void refreshTVTypes() {
        mDisposables.clear();
        mTVTypesRepository.invalidCache();
        mTVTypesRepository
                .getTVTypes()
                .subscribeOn(mSchedulerProvider.io())
                .observeOn(mSchedulerProvider.ui())
                .subscribe(new Observer<List<TVTypes.TVType>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposables.add(d);
                    }

                    @Override
                    public void onNext(List<TVTypes.TVType> value) {
                        mShowsView.showTVTypes(value);
                        mShowsView.showTips("Refresh Done");
                        mShowsView.stopRefreshing();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mShowsView.showTips("Refresh Failed: " + e.getLocalizedMessage());
                        mShowsView.stopRefreshing();
                    }

                    @Override
                    public void onComplete() {
                        mShowsView.stopRefreshing();
                    }
                });
    }

    private void loadTVTypes() {
        mDisposables.clear();
        mTVTypesRepository
                .getTVTypes()
                .subscribeOn(mSchedulerProvider.io())
                .observeOn(mSchedulerProvider.ui())
                .subscribe(new Observer<List<TVTypes.TVType>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposables.add(d);
                    }

                    @Override
                    public void onNext(List<TVTypes.TVType> value) {
                        mShowsView.showTVTypes(value);
                        mShowsView.stopLoadingUI();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mShowsView.showTips("Load Failed: " + e.getLocalizedMessage());
                        mShowsView.stopLoadingUI();
                    }

                    @Override
                    public void onComplete() {
                        mShowsView.stopLoadingUI();
                    }
                });
    }

    @Override
    public void onUserVisible() {
        mShowsView.showLoadingUI();
        loadTVTypes();
    }

    @Override
    public void onUserInvisible() {
        mDisposables.clear();
        mShowsView.stopRefreshing();
    }
}
