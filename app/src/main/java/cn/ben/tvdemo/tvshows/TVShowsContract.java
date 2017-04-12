package cn.ben.tvdemo.tvshows;

import android.content.Context;
import android.support.v7.app.AlertDialog;

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

        void openUrl(String pUrl);

        void addReminder(TVShows.TVShow tvShow);
    }

    interface Presenter extends BasePresenter {
        void refreshTVShows();

        void setupClickAlertBuilder(final Context context, AlertDialog.Builder builder, TVShows.TVShow tvShow);
    }
}
