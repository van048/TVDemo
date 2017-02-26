package cn.ben.tvdemo.favorite;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.ben.tvdemo.R;

import static com.google.common.base.Preconditions.checkNotNull;

public class FavoriteFragment extends Fragment implements FavoriteContract.View {
    private FavoriteContract.Presenter mPresenter;

    public static FavoriteFragment newInstance() {
        return new FavoriteFragment();
    }

    @Override
    public void setPresenter(@NonNull FavoriteContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unSubscribe();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // TODO: 2017/2/19
        return inflater.inflate(R.layout.fav_frag, container, false);
    }
}
