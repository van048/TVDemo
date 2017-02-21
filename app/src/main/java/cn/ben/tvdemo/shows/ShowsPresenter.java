package cn.ben.tvdemo.shows;

import android.support.annotation.NonNull;

import java.util.List;

import cn.ben.tvdemo.data.tvtype.TVTypes;
import cn.ben.tvdemo.data.tvtype.source.TVTypesDataSource;
import cn.ben.tvdemo.data.tvtype.source.TVTypesRepository;

import static com.google.common.base.Preconditions.checkNotNull;

public class ShowsPresenter implements ShowsContract.Presenter {
    private final ShowsContract.View mShowsView;
    private final TVTypesRepository mRepository;

    public ShowsPresenter(@NonNull TVTypesRepository repository, @NonNull ShowsContract.View showsView) {
        mShowsView = checkNotNull(showsView);
        mRepository = checkNotNull(repository);

        mShowsView.setPresenter(this);
    }

    @Override
    public void start() {
        mShowsView.changeLoadingUI(true);
        mRepository.getTVTypes(new TVTypesDataSource.LoadTVTypesCallback() {
            @Override
            public void onTVTypesLoaded(List<TVTypes.TVType> tvTypes) {
                mShowsView.showTVTypes(tvTypes);
                mShowsView.changeLoadingUI(false);
            }

            @Override
            public void onDataNotAvailable(String reason) {
                mShowsView.changeLoadingUI(false);
                mShowsView.showErrorUI(reason);
            }
        });
    }
}
