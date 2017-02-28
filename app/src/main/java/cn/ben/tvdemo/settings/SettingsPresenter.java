package cn.ben.tvdemo.settings;

import android.support.annotation.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

public class SettingsPresenter implements SettingsContract.Presenter {
    private final SettingsContract.View mSettingsView;

    public SettingsPresenter(@NonNull SettingsContract.View settingsView) {
        mSettingsView = checkNotNull(settingsView);
        mSettingsView.setPresenter(this);
    }

    @Override
    public void onVisible() {

    }

    @Override
    public void onInvisible() {

    }
}
