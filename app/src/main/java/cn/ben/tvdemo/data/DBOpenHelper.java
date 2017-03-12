package cn.ben.tvdemo.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import cn.ben.tvdemo.constant.Constants;
import cn.ben.tvdemo.data.tvchannel.source.local.TVChannelsPersistenceContract;
import cn.ben.tvdemo.data.tvshow.source.local.TVShowsPersistenceContract;
import cn.ben.tvdemo.data.tvtype.source.local.TVTypesPersistenceContract;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class DBOpenHelper extends SQLiteOpenHelper {

    private volatile static DBOpenHelper instance = null;

    public static DBOpenHelper getInstance() {
        checkNotNull(instance);
        return instance;
    }

    public static void initialize(Context context) {
        instance = new DBOpenHelper(context);
    }

    private DBOpenHelper(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TVTypesPersistenceContract.SQL_CREATE_ENTRIES);
        db.execSQL(TVChannelsPersistenceContract.SQL_CREATE_ENTRIES);
        db.execSQL(TVShowsPersistenceContract.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
