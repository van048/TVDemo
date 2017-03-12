package cn.ben.tvdemo.data.tvchannel.source.local;

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
import cn.ben.tvdemo.data.tvchannel.TVChannels;
import cn.ben.tvdemo.data.tvchannel.source.TVChannelsDataSource;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

import static cn.ben.tvdemo.data.tvchannel.source.local.TVChannelsPersistenceContract.TVChannelEntry.COLUMN_CHANNEL_NAME;
import static cn.ben.tvdemo.data.tvchannel.source.local.TVChannelsPersistenceContract.TVChannelEntry.COLUMN_PID;
import static cn.ben.tvdemo.data.tvchannel.source.local.TVChannelsPersistenceContract.TVChannelEntry.COLUMN_REL;
import static cn.ben.tvdemo.data.tvchannel.source.local.TVChannelsPersistenceContract.TVChannelEntry.COLUMN_URL;
import static cn.ben.tvdemo.data.tvchannel.source.local.TVChannelsPersistenceContract.TVChannelEntry.TABLE_NAME;
import static com.google.common.base.Preconditions.checkNotNull;

public class TVChannelsLocalDataSource implements TVChannelsDataSource {
    private volatile static TVChannelsLocalDataSource instance = null;
    private final DBOpenHelper mDbHelper;
    private final ExecutorService mExecutor;

    private final List<TVChannels.TVChannel> mGetResultTmp = new ArrayList<>();
    private volatile boolean mGetDone = false;

    public static TVChannelsLocalDataSource getInstance() {
        if (instance == null) {
            synchronized (TVChannelsLocalDataSource.class) {
                if (instance == null) {
                    instance = new TVChannelsLocalDataSource();
                }
            }
        }
        return instance;
    }

    private TVChannelsLocalDataSource() {
        mDbHelper = DBOpenHelper.getInstance();
        mExecutor = Executors.newSingleThreadExecutor();
    }

    @Override
    public Observable<List<TVChannels.TVChannel>> getTVChannelsWithPID(final String pId) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mGetResultTmp.clear();

                String[] projection = {
                        COLUMN_PID,
                        COLUMN_CHANNEL_NAME,
                        COLUMN_REL,
                        COLUMN_URL
                };

                SQLiteDatabase readableDatabase = mDbHelper.getReadableDatabase();
                Cursor cursor = readableDatabase.query(
                        TABLE_NAME, projection, COLUMN_PID + "=?", new String[]{pId}, null, null, null);
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        String id = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PID));
                        String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CHANNEL_NAME));
                        String rel = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REL));
                        String url = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_URL));
                        TVChannels.TVChannel tvChannel = new TVChannels.TVChannel(id, name, rel, url);
                        mGetResultTmp.add(tvChannel);
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
                .create(new ObservableOnSubscribe<List<TVChannels.TVChannel>>() {
                    @Override
                    public void subscribe(ObservableEmitter<List<TVChannels.TVChannel>> e) throws Exception {
                        while (true) {
                            if (mGetDone) break;
                        }
                        mGetDone = false;

                        List<TVChannels.TVChannel> tvChannels = new ArrayList<>(mGetResultTmp);
                        e.onNext(tvChannels);
                    }
                });
    }

    @Override
    public void deleteAllTVChannelsWithPID(final String pId) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase writableDatabase = mDbHelper.getWritableDatabase();
                writableDatabase.delete(TABLE_NAME, COLUMN_PID + "=?", new String[]{pId});
                writableDatabase.close();
            }
        });
    }

    @Override
    public void saveTVChannel(@NonNull final TVChannels.TVChannel tvChannel) {
        checkNotNull(tvChannel);

        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                ContentValues contentValues = new ContentValues();
                contentValues.put(COLUMN_PID, tvChannel.getPId());
                contentValues.put(COLUMN_CHANNEL_NAME, tvChannel.getChannelName());
                contentValues.put(COLUMN_REL, tvChannel.getRel());
                contentValues.put(COLUMN_URL, tvChannel.getUrl());
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
