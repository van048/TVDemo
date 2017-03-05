package cn.ben.tvdemo.data.tvshow.source;

import java.util.List;

import cn.ben.tvdemo.data.tvshow.TVShows;
import io.reactivex.Observable;

public interface TVShowsDataSource {

    Observable<List<TVShows.TVShow>> getTVShowsWithCodeAndDate(String code, String date);

    Observable<List<TVShows.TVShow>> getFavTVShows();

    void cleanup();
}
