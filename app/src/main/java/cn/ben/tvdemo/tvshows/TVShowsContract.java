package cn.ben.tvdemo.tvshows;

import cn.ben.tvdemo.BasePresenter;
import cn.ben.tvdemo.BaseView;

class TVShowsContract {
    interface View extends BaseView<Presenter> {
    }

    interface Presenter extends BasePresenter {
    }
}
