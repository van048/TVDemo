package cn.ben.tvdemo.tvshows;

import java.util.List;

import cn.ben.tvdemo.BasePresenter;
import cn.ben.tvdemo.BaseView;
import cn.ben.tvdemo.data.tvshow.TVShows;

class TVShowsContract {
    interface View extends BaseView<Presenter> {
        void showTips(String message);

        void showLoadingUI();

        void stopLoadingUI();

        void showTVShows(List<TVShows.TVShow> shows);

        void stopRefreshing();
    }

    interface Presenter extends BasePresenter {
        void switchFavState(TVShows.TVShow tvShow);

        void refreshTVShows(String code, int inc);
    }
}
