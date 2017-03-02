package cn.ben.tvdemo.data.tvtype.source.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import cn.ben.tvdemo.constant.Constants;

import static cn.ben.tvdemo.constant.Constants.*;
import static cn.ben.tvdemo.data.tvtype.source.local.TVTypesPersistenceContract.TVTypeEntry.*;

class TVTypesDbHelper extends SQLiteOpenHelper {
    @SuppressWarnings("FieldCanBeLocal")
    private final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + DATABASE_TEXT_TYPE + " PRIMARY KEY," +
                    COLUMN_ID + DATABASE_TEXT_TYPE + DATABASE_COMMA_SEP +
                    COLUMN_NAME + DATABASE_TEXT_TYPE + ")";

    TVTypesDbHelper(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
