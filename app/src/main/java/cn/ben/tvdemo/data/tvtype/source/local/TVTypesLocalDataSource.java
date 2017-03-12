package cn.ben.tvdemo.data.tvtype.source.local;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import cn.ben.tvdemo.data.DBOpenHelper;
import cn.ben.tvdemo.data.tvtype.TVTypes;
import cn.ben.tvdemo.data.tvtype.source.TVTypesDataSource;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

import static cn.ben.tvdemo.data.tvtype.source.local.TVTypesPersistenceContract.TVTypeEntry.COLUMN_ID;
import static cn.ben.tvdemo.data.tvtype.source.local.TVTypesPersistenceContract.TVTypeEntry.COLUMN_NAME;
import static cn.ben.tvdemo.data.tvtype.source.local.TVTypesPersistenceContract.TVTypeEntry.TABLE_NAME;
import static com.google.common.base.Preconditions.checkNotNull;

public class TVTypesLocalDataSource implements TVTypesDataSource {
    private volatile static TVTypesLocalDataSource instance = null;
    private final DBOpenHelper mDbHelper;
    private final ExecutorService mExecutor;

    private final List<TVTypes.TVType> mGetResultTmp = new ArrayList<>();
    private volatile boolean mGetDone = false;

    public static TVTypesLocalDataSource getInstance() {
        if (instance == null) {
            synchronized (TVTypesLocalDataSource.class) {
                if (instance == null) {
                    instance = new TVTypesLocalDataSource();
                }
            }
        }
        return instance;
    }

    private TVTypesLocalDataSource() {
        mDbHelper = DBOpenHelper.getInstance();
        mExecutor = Executors.newSingleThreadExecutor();
    }

    @Override
    public Observable<List<TVTypes.TVType>> getTVTypes() {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mGetResultTmp.clear();

                String[] projection = {
                        COLUMN_ID,
                        COLUMN_NAME
                };

                SQLiteDatabase readableDatabase = mDbHelper.getReadableDatabase();
                Cursor cursor = readableDatabase.query(TABLE_NAME, projection, null, null, null, null, null);
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        String id = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID));
                        String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
                        TVTypes.TVType tvType = new TVTypes.TVType(id, name);
                        mGetResultTmp.add(tvType);
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
                .create(new ObservableOnSubscribe<List<TVTypes.TVType>>() {
                    @Override
                    public void subscribe(ObservableEmitter<List<TVTypes.TVType>> e) throws Exception {
                        while (true) {
                            if (mGetDone) break;
                        }
                        mGetDone = false;

                        List<TVTypes.TVType> tvTypes = new ArrayList<>(mGetResultTmp);
                        e.onNext(tvTypes);
                    }
                });
    }

    @Override
    public void deleteAllTVTypes() {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase writableDatabase = mDbHelper.getWritableDatabase();
                writableDatabase.delete(TABLE_NAME, null, null);
                writableDatabase.close();
            }
        });
    }

    @Override
    public void saveTVType(@NonNull final TVTypes.TVType tvType) {
        checkNotNull(tvType);

        mExecutor.execute(new Runnable() {
            @Override
            public void run() {// make sure only each entry has unique ID
                SQLiteDatabase readableDatabase = mDbHelper.getReadableDatabase();
                Cursor cursor = readableDatabase.query(
                        TABLE_NAME,
                        new String[]{COLUMN_ID},
                        COLUMN_ID + "=?",
                        new String[]{tvType.getId()}, null, null, null);
                if (cursor != null && cursor.getCount() > 1) {
                    cursor.close();
                    readableDatabase.close();
                    throw new AssertionError("Duplicated TV Type ID");
                }
                if (cursor != null) {
                    cursor.close();
                }
                readableDatabase.close();

                ContentValues contentValues = new ContentValues();
                contentValues.put(COLUMN_ID, tvType.getId());
                contentValues.put(COLUMN_NAME, tvType.getName());
                SQLiteDatabase writableDatabase = mDbHelper.getWritableDatabase();
                writableDatabase.insert(TABLE_NAME, null, contentValues);
                writableDatabase.close();
            }
        });
    }

    @Override
    public void cleanup() {
        mDbHelper.close();
        try {
            mExecutor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        instance = null;
    }
}
