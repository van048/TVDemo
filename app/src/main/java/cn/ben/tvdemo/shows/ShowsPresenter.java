package cn.ben.tvdemo.shows;

import android.support.annotation.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

public class ShowsPresenter implements ShowsContract.Presenter {
    private final ShowsContract.View mShowsView;

    public ShowsPresenter(@NonNull ShowsContract.View showsView) {
        mShowsView = checkNotNull(showsView);
        mShowsView.setPresenter(this);
    }

    @Override
    public void start() {

    }
}
