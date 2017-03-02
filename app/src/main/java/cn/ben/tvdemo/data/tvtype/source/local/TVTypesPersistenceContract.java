package cn.ben.tvdemo.data.tvtype.source.local;

import android.provider.BaseColumns;

import static android.provider.BaseColumns._ID;
import static cn.ben.tvdemo.constant.Constants.DATABASE_COMMA_SEP;
import static cn.ben.tvdemo.constant.Constants.DATABASE_TEXT_TYPE;
import static cn.ben.tvdemo.data.tvtype.source.local.TVTypesPersistenceContract.TVTypeEntry.COLUMN_ID;
import static cn.ben.tvdemo.data.tvtype.source.local.TVTypesPersistenceContract.TVTypeEntry.COLUMN_NAME;
import static cn.ben.tvdemo.data.tvtype.source.local.TVTypesPersistenceContract.TVTypeEntry.TABLE_NAME;

public final class TVTypesPersistenceContract {
    private TVTypesPersistenceContract() {
    }

    static abstract class TVTypeEntry implements BaseColumns {
        static final String TABLE_NAME = "tv_type";
        static final String COLUMN_ID = "id";
        static final String COLUMN_NAME = "name";
    }

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + DATABASE_TEXT_TYPE + " PRIMARY KEY," +
                    COLUMN_ID + DATABASE_TEXT_TYPE + DATABASE_COMMA_SEP +
                    COLUMN_NAME + DATABASE_TEXT_TYPE + ")";
}
