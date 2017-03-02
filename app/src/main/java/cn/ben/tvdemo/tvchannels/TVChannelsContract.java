package cn.ben.tvdemo.tvchannels;

import cn.ben.tvdemo.BasePresenter;
import cn.ben.tvdemo.BaseView;

class TVChannelsContract {
    interface View extends BaseView<Presenter> {
    }

    interface Presenter extends BasePresenter {
    }
}
