package cn.ben.tvdemo.favorite;

import android.support.annotation.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

public class FavoritePresenter implements FavoriteContract.Presenter {
    private final FavoriteContract.View mFavoriteView;

    public FavoritePresenter(@NonNull FavoriteContract.View favoriteView) {
        mFavoriteView = checkNotNull(favoriteView);
        mFavoriteView.setPresenter(this);
    }

    @Override
    public void onVisible() {

    }

    @Override
    public void onInvisible() {

    }
}
