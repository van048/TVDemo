package cn.ben.tvdemo.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.content.SharedPreferencesCompat;

import static com.google.common.base.Preconditions.checkNotNull;

public class SettingsPresenter implements SettingsContract.Presenter {
    private static final String SETTINGS_SP = "settings";
    private static final String REMIND_ME_SP_KEY = "remind";

    private final SettingsContract.View mSettingsView;
    private final Context mContext;

    public SettingsPresenter(@NonNull SettingsContract.View settingsView,
                             @NonNull Context context) {
        mSettingsView = checkNotNull(settingsView);
        mContext = checkNotNull(context);

        mSettingsView.setPresenter(this);
    }

    @Override
    public void onUserVisible() {
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
}
