package cn.ben.tvdemo.data.tvtype.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import cn.ben.tvdemo.data.tvtype.TVTypes;
import cn.ben.tvdemo.data.tvtype.source.TVTypesDataSource;

import static cn.ben.tvdemo.data.tvtype.source.local.TVTypesPersistenceContract.TVTypeEntry.COLUMN_ID;
import static cn.ben.tvdemo.data.tvtype.source.local.TVTypesPersistenceContract.TVTypeEntry.COLUMN_NAME;
import static cn.ben.tvdemo.data.tvtype.source.local.TVTypesPersistenceContract.TVTypeEntry.TABLE_NAME;
import static com.google.common.base.Preconditions.checkNotNull;

public class TVTypesLocalDataSource implements TVTypesDataSource {
    private volatile static TVTypesLocalDataSource instance = null;
    private final TVTypesDbHelper mDbHelper;

    private final HandlerThread mDBThread;
    private final Handler mDBHandler;
    private final Handler mMainHandler;

    public static TVTypesLocalDataSource getInstance(@NonNull Context context) {
        if (instance == null) {
            synchronized (TVTypesLocalDataSource.class) {
                if (instance == null) {
                    instance = new TVTypesLocalDataSource(context);
                }
            }
        }
        return instance;
    }

    private TVTypesLocalDataSource(@NonNull Context context) {
        checkNotNull(context);
        mDbHelper = new TVTypesDbHelper(context);

        mDBThread = new HandlerThread(TVTypesLocalDataSource.class.getName());
        mDBThread.start();
        mDBHandler = new Handler(mDBThread.getLooper());
        mMainHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void getTVTypes(@NonNull final LoadTVTypesCallback loadTVTypesCallback) {
        mDBHandler.post(new Runnable() {
            @Override
            public void run() {
                final List<TVTypes.TVType> tvTypes = new ArrayList<>();
                SQLiteDatabase sqLiteDatabase = mDbHelper.getReadableDatabase();

                String[] projection = {
                        COLUMN_ID,
                        COLUMN_NAME
                };

                Cursor cursor = sqLiteDatabase.query(TABLE_NAME, projection, null, null, null, null, null);
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        String id = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID));
                        String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
                        TVTypes.TVType tvType = new TVTypes.TVType(id, name);
                        tvTypes.add(tvType);
                    }
                }
                if (cursor != null) {
                    cursor.close();
                }
                sqLiteDatabase.close();

                mMainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (tvTypes.isEmpty()) {
                            loadTVTypesCallback.onDataNotAvailable("No Entries In Local DB");
                        } else {
                            loadTVTypesCallback.onTVTypesLoaded(tvTypes);
                        }
                    }
                });
            }
        });
    }

    @Override
    public void deleteAllTVTypes() {
        mDBHandler.post(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase sqLiteDatabase = mDbHelper.getWritableDatabase();
                sqLiteDatabase.delete(TABLE_NAME, null, null);
                sqLiteDatabase.close();
            }
        });
    }

    @Override
    public void saveTVType(@NonNull final TVTypes.TVType tvType) {
        checkNotNull(tvType);

        mDBHandler.post(new Runnable() {
            @Override
            public void run() {
                // make sure only each entry has unique ID
                SQLiteDatabase sqLiteDatabase = mDbHelper.getReadableDatabase();
                Cursor cursor = sqLiteDatabase.query(
                        TABLE_NAME,
                        new String[]{COLUMN_ID},
                        COLUMN_ID + "=?",
                        new String[]{tvType.getId()}, null, null, null);
                if (cursor != null && cursor.getCount() > 1) {
                    cursor.close();
                    sqLiteDatabase.close();
                    throw new AssertionError("Duplicated TV Type ID");
                }
                if (cursor != null) {
                    cursor.close();
                }
                sqLiteDatabase.close();

                sqLiteDatabase = mDbHelper.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put(COLUMN_ID, tvType.getId());
                contentValues.put(COLUMN_NAME, tvType.getName());

                sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
                sqLiteDatabase.close();
            }
        });
    }

    @Override
    public void cleanup() {
        mDBThread.quitSafely();
        mDbHelper.close();
        instance = null;
    }
}
