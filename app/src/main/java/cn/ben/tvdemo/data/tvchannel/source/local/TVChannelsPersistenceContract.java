package cn.ben.tvdemo.data.tvchannel.source.local;

import android.provider.BaseColumns;

import static android.provider.BaseColumns._ID;
import static cn.ben.tvdemo.constant.Constants.DATABASE_COMMA_SEP;
import static cn.ben.tvdemo.constant.Constants.DATABASE_TEXT_TYPE;
import static cn.ben.tvdemo.data.tvchannel.source.local.TVChannelsPersistenceContract.TVChannelEntry.COLUMN_CHANNEL_NAME;
import static cn.ben.tvdemo.data.tvchannel.source.local.TVChannelsPersistenceContract.TVChannelEntry.COLUMN_PID;
import static cn.ben.tvdemo.data.tvchannel.source.local.TVChannelsPersistenceContract.TVChannelEntry.COLUMN_REL;
import static cn.ben.tvdemo.data.tvchannel.source.local.TVChannelsPersistenceContract.TVChannelEntry.COLUMN_URL;
import static cn.ben.tvdemo.data.tvchannel.source.local.TVChannelsPersistenceContract.TVChannelEntry.TABLE_NAME;

public final class TVChannelsPersistenceContract {
    private TVChannelsPersistenceContract() {
    }

    static abstract class TVChannelEntry implements BaseColumns {
        static final String TABLE_NAME = "tv_channel";
        static final String COLUMN_PID = "pId";
        static final String COLUMN_CHANNEL_NAME = "channelName";
        static final String COLUMN_REL = "rel";
        static final String COLUMN_URL = "url";
    }

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + DATABASE_TEXT_TYPE + " PRIMARY KEY," +
                    COLUMN_PID + DATABASE_TEXT_TYPE + DATABASE_COMMA_SEP +
                    COLUMN_CHANNEL_NAME + DATABASE_TEXT_TYPE + DATABASE_COMMA_SEP +
                    COLUMN_REL + DATABASE_TEXT_TYPE + DATABASE_COMMA_SEP +
                    COLUMN_URL + DATABASE_TEXT_TYPE + ")";
}
