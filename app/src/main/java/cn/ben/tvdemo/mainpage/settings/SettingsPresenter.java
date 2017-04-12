package cn.ben.tvdemo.mainpage.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.content.SharedPreferencesCompat;

import static com.google.common.base.Preconditions.checkNotNull;

class SettingsPresenter implements SettingsContract.Presenter {
    private static final String SETTINGS_SP = "settings";
    private static final String REMIND_ME_SP_KEY = "remind";

    private final SettingsContract.View mSettingsView;
    private final Context mContext;
    private boolean mPendingForPermission = false;

    SettingsPresenter(@NonNull SettingsContract.View settingsView,
                      @NonNull Context context) {
        mSettingsView = checkNotNull(settingsView);
        mContext = checkNotNull(context);
    }

    @Override
    public void onUserVisible() {
        if (mPendingForPermission) {
            // return from permission request
            if (mSettingsView.hasCalendarPermission()) {
                // continue pending action
                onRemindEnabledChanged(true);
            } else {
                mSettingsView.showTips("Need permission to work!");
            }
            mPendingForPermission = false;
        }

        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SETTINGS_SP, Context.MODE_PRIVATE);
        boolean remindEnabled = sharedPreferences.getBoolean(REMIND_ME_SP_KEY, false);

        mSettingsView.setSwitchUI(remindEnabled);

        // TODO: 2017/3/1 Notification Manager
    }

    @Override
    public void onUserInvisible() {

    }

    @Override
    public void onRemindEnabledChanged(boolean checked) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SETTINGS_SP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(REMIND_ME_SP_KEY, checked);
        SharedPreferencesCompat.EditorCompat.getInstance().apply(editor);

        // TODO: 2017/3/1 Notification Manager
    }

    @Override
    public void pendingForPermission() {
        mPendingForPermission = true;
    }
}
