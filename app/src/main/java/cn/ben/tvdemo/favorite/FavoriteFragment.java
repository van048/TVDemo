package cn.ben.tvdemo.favorite;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.ben.tvdemo.BaseFragment;
import cn.ben.tvdemo.R;

import static com.google.common.base.Preconditions.checkNotNull;

public class FavoriteFragment extends BaseFragment implements FavoriteContract.View {
    private FavoriteContract.Presenter mPresenter;

    public static FavoriteFragment newInstance() {
        return new FavoriteFragment();
    }

    @Override
    public void setPresenter(@NonNull FavoriteContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fav_frag, container, false);
    }

    @Override
    public void onVisibilityChangedToUser(boolean isVisibleToUser, boolean isHappenedInSetUserVisibleHintMethod) {
        if (isVisibleToUser) {
            mPresenter.onUserVisible();
        } else {
            mPresenter.onUserInvisible();
        }
    }
}
