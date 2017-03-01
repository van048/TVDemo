package cn.ben.tvdemo.settings;

import cn.ben.tvdemo.BasePresenter;
import cn.ben.tvdemo.BaseView;

class SettingsContract {
    interface View extends BaseView<Presenter> {

        void setSwitchUI(boolean remindEnabled);
    }

    interface Presenter extends BasePresenter {

        void onRemindEnabledChanged(boolean checked);
    }
}
