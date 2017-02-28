package cn.ben.tvdemo.settings;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.ben.tvdemo.R;

import static com.google.common.base.Preconditions.checkNotNull;

public class SettingsFragment extends Fragment implements SettingsContract.View {
    private SettingsContract.Presenter mPresenter;
    private boolean mStarted = false;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void setPresenter(@NonNull SettingsContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (mPresenter != null) {
            if (getUserVisibleHint()) {
                mStarted = true;
                mPresenter.onVisible();
            } else {
                if (mStarted) {
                    // actually leave this page
                    mStarted = false;
                    mPresenter.onInvisible();
                }
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // TODO: 2017/2/19
        return inflater.inflate(R.layout.settings_frag, container, false);
    }
}
