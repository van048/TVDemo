package cn.ben.tvdemo.shows;

import cn.ben.tvdemo.BasePresenter;
import cn.ben.tvdemo.BaseView;

class ShowsContract {
    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {

    }
}
