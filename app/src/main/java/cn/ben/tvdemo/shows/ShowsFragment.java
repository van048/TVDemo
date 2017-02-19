package cn.ben.tvdemo.shows;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.ben.tvdemo.R;

import static com.google.common.base.Preconditions.checkNotNull;

public class ShowsFragment extends Fragment implements ShowsContract.View {
    private ShowsContract.Presenter mPresenter;

    @Override
    public void setPresenter(@NonNull ShowsContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    public static ShowsFragment newInstance() {
        return new ShowsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // TODO: 2017/2/19
        return inflater.inflate(R.layout.shows_frag, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }
}
