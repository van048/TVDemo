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
    public Observable<List<TVShows.TVShow>> getTVShowsWithCodeAndDate(final String code, final String date) {
        final String next = TimeUtil.plusOnDate(date, 1, TimeUtil.FORMAT_YEAR_MONTH_DAY, TimeUtil.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE);
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mGetResultTmp.clear();

                SQLiteDatabase readableDatabase = mDbHelper.getReadableDatabase();
                Cursor cursor = readableDatabase.query(
                        TABLE_NAME, null, COLUMN_CHANNEL_CODE + "=" + code + " and datetime(" + COLUMN_TIME + ")>=datetime('" + date + "') and datetime(" + COLUMN_TIME + ")<datetime('" + next + "')", null, null, null, null);
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        String channelName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CHANNEL_NAME));
                        String showName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SHOW_NAME));
                        String url = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_URL));
                        String time = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIME));
                        boolean fav = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_FAV)) == 1;
                        TVShows.TVShow tvShow = new TVShows.TVShow(code, channelName, showName, url, time, fav);
                        mGetResultTmp.add(tvShow);
                    }
                }
                if (cursor != null) {
                    cursor.close();
                }
                readableDatabase.close();

                mGetDone = true;
            }
        });

        return Observable
                .create(new ObservableOnSubscribe<List<TVShows.TVShow>>() {
                    @Override
                    public void subscribe(ObservableEmitter<List<TVShows.TVShow>> e) throws Exception {
                        while (true) {
                            if (mGetDone) break;
                        }
                        mGetDone = false;

                        List<TVShows.TVShow> tvShows = new ArrayList<>(mGetResultTmp);
                        e.onNext(tvShows);
                    }
                });
    }

    @Override
    public Observable<List<TVShows.TVShow>> getFavTVShows() {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mFavResultTmp.clear();

                SQLiteDatabase readableDatabase = mDbHelper.getReadableDatabase();
                Cursor cursor = readableDatabase.query(
                        TABLE_NAME, null, COLUMN_FAV + "=1", null, null, null, null);
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        String channelCode = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CHANNEL_CODE));
                        String channelName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CHANNEL_NAME));
                        String showName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SHOW_NAME));
                        String url = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_URL));
                        String time = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIME));
                        boolean fav = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_FAV)) == 1;
                        TVShows.TVShow tvShow = new TVShows.TVShow(channelCode, channelName, showName, url, time, fav);
                        mFavResultTmp.add(tvShow);
                    }
                }
                if (cursor != null) {
                    cursor.close();
                }
                readableDatabase.close();

                mFavDone = true;
            }
        });

        return Observable
                .create(new ObservableOnSubscribe<List<TVShows.TVShow>>() {
                    @Override
                    public void subscribe(ObservableEmitter<List<TVShows.TVShow>> e) throws Exception {
                        while (true) {
                            if (mFavDone) break;
                        }
                        mFavDone = false;

                        List<TVShows.TVShow> tvShows = new ArrayList<>(mFavResultTmp);
                        e.onNext(tvShows);
                    }
                });
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
