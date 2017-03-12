package cn.ben.tvdemo.data.tvshow.source.local;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import cn.ben.tvdemo.data.DBOpenHelper;
import cn.ben.tvdemo.data.tvchannel.TVChannels;
import cn.ben.tvdemo.data.tvshow.TVShows;
import cn.ben.tvdemo.data.tvshow.source.TVShowsDataSource;
import io.reactivex.Observable;

// TODO: 2017/3/5
public class TVShowsLocalDataSource implements TVShowsDataSource {
    private volatile static TVShowsLocalDataSource instance = null;
    private final DBOpenHelper mDbHelper;
    private final Executor mExecutor;

    private final List<TVChannels.TVChannel> mGetResultTmp = new ArrayList<>();
    private volatile boolean mGetDone = false;

    public static TVShowsLocalDataSource getInstance() {
        if (instance == null) {
            synchronized (TVShowsLocalDataSource.class) {
                if (instance == null) {
                    instance = new TVShowsLocalDataSource();
                }
            }
        }
        return instance;
    }

    private TVShowsLocalDataSource() {
        mDbHelper = DBOpenHelper.getInstance();
        mExecutor = Executors.newSingleThreadExecutor();
    }

    @Override
    public Observable<List<TVShows.TVShow>> getTVShowsWithCodeAndDate(String code, String date) {
        return Observable.empty();
    }

    @Override
    public Observable<List<TVShows.TVShow>> getFavTVShows() {
        return null;
    }

    @Override
    public void cleanup() {
        mDbHelper.close();
        instance = null;
    }

    @Override
    public void update(final String code, final String date, final List<TVShows.TVShow> shows) {
        final String next = TimeUtil.plusOnDate(date, 1, TimeUtil.FORMAT_YEAR_MONTH_DAY, TimeUtil.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE);
        Log.d("ben", "date: " + date + " next: " + next);
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase writableDatabase = mDbHelper.getWritableDatabase();
                writableDatabase.delete(TABLE_NAME, COLUMN_CHANNEL_CODE + "=? and datetime(" + COLUMN_TIME + ")>=datetime('" + date + "') and datetime(" + COLUMN_TIME + ")<datetime('" + next + "')", new String[]{code});

                for (TVShows.TVShow tvShow : shows) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(COLUMN_CHANNEL_CODE, code);
                    contentValues.put(COLUMN_CHANNEL_NAME, tvShow.getCName());
                    contentValues.put(COLUMN_SHOW_NAME, tvShow.getPName());
                    contentValues.put(COLUMN_URL, tvShow.getPUrl());
                    contentValues.put(COLUMN_TIME, tvShow.getTime());
                    contentValues.put(COLUMN_FAV, tvShow.isFav() ? 1 : 0);
                    writableDatabase.insert(TABLE_NAME, null, contentValues);
                }

                writableDatabase.close();
            }
        });
    }
}
