package cn.ben.tvdemo.mainpage.shows;

import java.util.List;

import cn.ben.tvdemo.BasePresenter;
import cn.ben.tvdemo.BaseView;
import cn.ben.tvdemo.data.tvtype.TVTypes;

class ShowsContract {
    interface View extends BaseView<Presenter> {

        void showTVTypes(List<TVTypes.TVType> tvTypes);

        void showTips(String reason);

        void stopRefreshing();

        void showLoadingUI();

        void stopLoadingUI();
    }

    interface Presenter extends BasePresenter {
        void refreshTVTypes();
    }
}
