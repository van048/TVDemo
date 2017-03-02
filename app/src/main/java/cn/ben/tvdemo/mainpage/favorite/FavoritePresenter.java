package cn.ben.tvdemo.mainpage.favorite;

import android.support.annotation.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

public class FavoritePresenter implements FavoriteContract.Presenter {
    private final FavoriteContract.View mFavoriteView;

    public FavoritePresenter(@NonNull FavoriteContract.View favoriteView) {
        mFavoriteView = checkNotNull(favoriteView);
        mFavoriteView.setPresenter(this);
    }

    @Override
    public void onUserVisible() {

    }

    @Override
    public void onUserInvisible() {

    }
}
