package cn.ben.tvdemo.data.tvtype.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import cn.ben.tvdemo.data.tvtype.TVTypes;
import cn.ben.tvdemo.data.tvtype.source.TVTypesDataSource;
import cn.ben.tvdemo.util.schedulers.BaseSchedulerProvider;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;

import static cn.ben.tvdemo.data.tvtype.source.local.TVTypesPersistenceContract.TVTypeEntry.COLUMN_ID;
import static cn.ben.tvdemo.data.tvtype.source.local.TVTypesPersistenceContract.TVTypeEntry.COLUMN_NAME;
import static cn.ben.tvdemo.data.tvtype.source.local.TVTypesPersistenceContract.TVTypeEntry.TABLE_NAME;
import static com.google.common.base.Preconditions.checkNotNull;

public class TVTypesLocalDataSource implements TVTypesDataSource {
    private volatile static TVTypesLocalDataSource instance = null;
    private final TVTypesDbHelper mDbHelper;
    private final SQLiteDatabase mReadableDatabase;
    private final SQLiteDatabase mWritableDatabase;
    private final BaseSchedulerProvider mSchedulerProvider;

    private static final Consumer<? super Object> EMPTY_CONSUMER = new Consumer<Object>() {
        @Override
        public void accept(Object o) throws Exception {
        }
    };

    public static TVTypesLocalDataSource getInstance(@NonNull Context context,
                                                     @NonNull BaseSchedulerProvider schedulerProvider) {
        if (instance == null) {
            synchronized (TVTypesLocalDataSource.class) {
                if (instance == null) {
                    instance = new TVTypesLocalDataSource(context, schedulerProvider);
                }
            }
        }
        return instance;
    }

    private TVTypesLocalDataSource(@NonNull Context context,
                                   @NonNull BaseSchedulerProvider schedulerProvider) {
        checkNotNull(context);

        mDbHelper = new TVTypesDbHelper(context);
        mReadableDatabase = mDbHelper.getReadableDatabase();
        mWritableDatabase = mDbHelper.getWritableDatabase();
        mSchedulerProvider = checkNotNull(schedulerProvider);
    }

    @Override
    public Observable<List<TVTypes.TVType>> getTVTypes() {
        return Observable
                .create(new ObservableOnSubscribe<List<TVTypes.TVType>>() {
                    @Override
                    public void subscribe(ObservableEmitter<List<TVTypes.TVType>> e) throws Exception {
                        final List<TVTypes.TVType> tvTypes = new ArrayList<>();

                        String[] projection = {
                                COLUMN_ID,
                                COLUMN_NAME
                        };

                        Cursor cursor = mReadableDatabase.query(TABLE_NAME, projection, null, null, null, null, null);
                        if (cursor != null) {
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

                        e.onNext(tvTypes);
                    }
                });
    }

    @Override
    public void deleteAllTVTypes() {
        Observable
                .create(new ObservableOnSubscribe<Object>() {
                    @Override
                    public void subscribe(ObservableEmitter<Object> e) throws Exception {
                        mWritableDatabase.delete(TABLE_NAME, null, null);
                    }
                })
                .subscribeOn(mSchedulerProvider.io())
                .subscribe(EMPTY_CONSUMER);
    }

    @Override
    public void saveTVType(@NonNull final TVTypes.TVType tvType) {
        checkNotNull(tvType);

        Observable
                .create(new ObservableOnSubscribe<Object>() {
                    @Override
                    public void subscribe(ObservableEmitter<Object> e) throws Exception {
                        // make sure only each entry has unique ID
                        Cursor cursor = mReadableDatabase.query(
                                TABLE_NAME,
                                new String[]{COLUMN_ID},
                                COLUMN_ID + "=?",
                                new String[]{tvType.getId()}, null, null, null);
                        if (cursor != null && cursor.getCount() > 1) {
                            cursor.close();
                            mReadableDatabase.close();
                            throw new AssertionError("Duplicated TV Type ID");
                        }
                        if (cursor != null) {
                            cursor.close();
                        }

                        ContentValues contentValues = new ContentValues();
                        contentValues.put(COLUMN_ID, tvType.getId());
                        contentValues.put(COLUMN_NAME, tvType.getName());
                        mWritableDatabase.insert(TABLE_NAME, null, contentValues);
                    }
                })
                .subscribeOn(mSchedulerProvider.io())
                .subscribe(EMPTY_CONSUMER);
    }

    @Override
    public void cleanup() {
        mReadableDatabase.close();
        mWritableDatabase.close();
        mDbHelper.close();
        instance = null;
    }
}
