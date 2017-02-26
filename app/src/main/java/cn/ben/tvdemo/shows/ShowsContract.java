package cn.ben.tvdemo.shows;

import java.util.List;

import cn.ben.tvdemo.BasePresenter;
import cn.ben.tvdemo.BaseView;
import cn.ben.tvdemo.data.tvtype.TVTypes;

class ShowsContract {
    interface View extends BaseView<Presenter> {

        void showTVTypes(List<TVTypes.TVType> tvTypes);

        void showErrorUI(String reason);

        void stopRefreshing();
    }

    interface Presenter extends BasePresenter {
        void refreshTVTypes();
    }
}
