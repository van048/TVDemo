package cn.ben.tvdemo.settings;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import cn.ben.tvdemo.BaseFragment;
import cn.ben.tvdemo.R;

import static com.google.common.base.Preconditions.checkNotNull;

public class SettingsFragment extends BaseFragment implements SettingsContract.View {
    @BindView(R.id.ringing_switch)
    SwitchCompat mSwitchCompat;

    private SettingsContract.Presenter mPresenter;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void setPresenter(@NonNull SettingsContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.settings_frag, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @Override
    public void onVisibilityChangedToUser(boolean isVisibleToUser, boolean isHappenedInSetUserVisibleHintMethod) {
        if (isVisibleToUser) {
            mPresenter.onUserVisible();
        } else {
            mPresenter.onUserInvisible();
        }
    }

    @Override
    public void setSwitchUI(boolean remindEnabled) {
        mSwitchCompat.setChecked(remindEnabled);
    }

    @OnCheckedChanged(R.id.ringing_switch)
    public void onCheckedChanged(boolean isChecked) {
        mPresenter.onRemindEnabledChanged(isChecked);
    }
}
