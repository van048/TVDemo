package cn.ben.tvdemo.tvchannels;

import java.util.List;

import cn.ben.tvdemo.BasePresenter;
import cn.ben.tvdemo.BaseView;
import cn.ben.tvdemo.data.tvchannel.TVChannels;

class TVChannelsContract {
    interface View extends BaseView<Presenter> {
        void showTips(String m);

        void showTVChannels(List<TVChannels.TVChannel> tvChannels);

        void stopRefreshing();
    }

    interface Presenter extends BasePresenter {
    }
}
