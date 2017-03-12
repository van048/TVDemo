package cn.ben.tvdemo.data.tvshow.source.local;

import android.provider.BaseColumns;

import static android.provider.BaseColumns._ID;
import static cn.ben.tvdemo.constant.Constants.DATABASE_BOOLEAN_TYPE;
import static cn.ben.tvdemo.constant.Constants.DATABASE_COMMA_SEP;
import static cn.ben.tvdemo.constant.Constants.DATABASE_DATE_TYPE;
import static cn.ben.tvdemo.constant.Constants.DATABASE_TEXT_TYPE;
import static cn.ben.tvdemo.data.tvshow.source.local.TVShowsPersistenceContract.TVShowEntry.COLUMN_CHANNEL_CODE;
import static cn.ben.tvdemo.data.tvshow.source.local.TVShowsPersistenceContract.TVShowEntry.COLUMN_CHANNEL_NAME;
import static cn.ben.tvdemo.data.tvshow.source.local.TVShowsPersistenceContract.TVShowEntry.COLUMN_FAV;
import static cn.ben.tvdemo.data.tvshow.source.local.TVShowsPersistenceContract.TVShowEntry.COLUMN_SHOW_NAME;
import static cn.ben.tvdemo.data.tvshow.source.local.TVShowsPersistenceContract.TVShowEntry.COLUMN_TIME;
import static cn.ben.tvdemo.data.tvshow.source.local.TVShowsPersistenceContract.TVShowEntry.COLUMN_URL;
import static cn.ben.tvdemo.data.tvshow.source.local.TVShowsPersistenceContract.TVShowEntry.TABLE_NAME;

public final class TVShowsPersistenceContract {
    private TVShowsPersistenceContract() {
    }

    static abstract class TVShowEntry implements BaseColumns {
        static final String TABLE_NAME = "tv_show";
        static final String COLUMN_CHANNEL_CODE = "channelCode";
        static final String COLUMN_CHANNEL_NAME = "channelName";
        static final String COLUMN_SHOW_NAME = "showName";
        static final String COLUMN_URL = "url";
        static final String COLUMN_TIME = "time";
        static final String COLUMN_FAV = "isFav";
    }

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + DATABASE_TEXT_TYPE + " PRIMARY KEY," +
                    COLUMN_CHANNEL_CODE + DATABASE_TEXT_TYPE + DATABASE_COMMA_SEP +
                    COLUMN_CHANNEL_NAME + DATABASE_TEXT_TYPE + DATABASE_COMMA_SEP +
                    COLUMN_SHOW_NAME + DATABASE_TEXT_TYPE + DATABASE_COMMA_SEP +
                    COLUMN_URL + DATABASE_TEXT_TYPE + DATABASE_COMMA_SEP +
                    COLUMN_TIME + DATABASE_DATE_TYPE + DATABASE_COMMA_SEP +
                    COLUMN_FAV + DATABASE_BOOLEAN_TYPE + ")";
}
