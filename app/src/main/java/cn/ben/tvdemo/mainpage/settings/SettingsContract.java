package cn.ben.tvdemo.mainpage.settings;

import cn.ben.tvdemo.BasePresenter;
import cn.ben.tvdemo.BaseView;

class SettingsContract {
    interface View extends BaseView<Presenter> {

        void setSwitchUI(boolean remindEnabled);

        boolean hasCalendarPermission();

        void showTips(String m);
    }

    interface Presenter extends BasePresenter {

        void onRemindEnabledChanged(boolean checked);

        void pendingForPermission();
    }
}
