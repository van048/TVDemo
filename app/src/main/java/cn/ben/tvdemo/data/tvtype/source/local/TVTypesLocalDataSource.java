package cn.ben.tvdemo.data.tvtype.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.List;

import cn.ben.tvdemo.data.tvtype.TVTypes;
import cn.ben.tvdemo.data.tvtype.source.TVTypesDataSource;
import cn.ben.tvdemo.data.tvtype.source.TVTypesRepository;
import cn.ben.tvdemo.util.schedulers.BaseSchedulerProvider;
import hu.akarnokd.rxjava.interop.RxJavaInterop;
import io.reactivex.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

import static cn.ben.tvdemo.data.tvtype.source.local.TVTypesPersistenceContract.TVTypeEntry.COLUMN_ID;
import static cn.ben.tvdemo.data.tvtype.source.local.TVTypesPersistenceContract.TVTypeEntry.COLUMN_NAME;
import static cn.ben.tvdemo.data.tvtype.source.local.TVTypesPersistenceContract.TVTypeEntry.TABLE_NAME;
import static com.google.common.base.Preconditions.checkNotNull;

public class TVTypesLocalDataSource implements TVTypesDataSource {

    @Nullable
    private volatile static TVTypesLocalDataSource instance = null;

    @NonNull
    private final BaseSchedulerProvider mSchedulerProvider;

    @NonNull
    private final BriteDatabase mDatabaseHelper;

    @NonNull
    private final HandlerThread mDBThread;

    @NonNull
    private final Handler mDBHandler;

    @NonNull
    private final Func1<Cursor, TVTypes.TVType> mTVTypeMapperFunction = new Func1<Cursor, TVTypes.TVType>() {
        @Override
        public TVTypes.TVType call(Cursor cursor) {
            String id = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
            return new TVTypes.TVType(id, name);
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
        mSchedulerProvider = checkNotNull(schedulerProvider);

        TVTypesDbHelper dbHelper = new TVTypesDbHelper(context);
        SqlBrite sqlBrite = new SqlBrite.Builder().build();
        mDatabaseHelper = sqlBrite.wrapDatabaseHelper(dbHelper, mSchedulerProvider.io_1());

        mDBThread = new HandlerThread(TVTypesRepository.class.getName());
        mDBThread.start();
        mDBHandler = new Handler(mDBThread.getLooper());
    }

    @Override
    public Observable<List<TVTypes.TVType>> getTVTypes() {
        String[] projection = {
                COLUMN_ID,
                COLUMN_NAME
        };
        String sql = String.format("SELECT %s FROM %s", TextUtils.join(",", projection), TABLE_NAME);
        return RxJavaInterop.toV2Observable(
                mDatabaseHelper
                        .createQuery(TABLE_NAME, sql)
                        .mapToList(mTVTypeMapperFunction)
        );
    }

    @Override
    public void deleteAllTVTypes() {
        mDBHandler.post(new Runnable() {
            @Override
            public void run() {
                mDatabaseHelper.delete(TABLE_NAME, null);
            }
        });
    }

    @Override
    public void saveTVType(@NonNull final TVTypes.TVType tvType) {
        checkNotNull(tvType);

        // make sure only each entry has unique ID
        String sql = String.format("SELECT * FROM %s WHERE %s=?", TABLE_NAME, COLUMN_ID);
        mDatabaseHelper.
                createQuery(TABLE_NAME, sql, tvType.getId())
                .mapToList(mTVTypeMapperFunction)
                .observeOn(mSchedulerProvider.io_1())
                .subscribe(new Action1<List<TVTypes.TVType>>() {
                    @Override
                    public void call(List<TVTypes.TVType> tvTypes) {
                        if (!tvTypes.isEmpty()) {
                            throw new AssertionError("Duplicated TV Type ID");
                        } else {
                            ContentValues contentValues = new ContentValues();
                            contentValues.put(COLUMN_ID, tvType.getId());
                            contentValues.put(COLUMN_NAME, tvType.getName());
                            mDatabaseHelper.insert(TABLE_NAME, contentValues);
                        }
                    }
                });
    }

    @Override
    public void cleanup() {
        mDBThread.quitSafely();
        mDatabaseHelper.close();
        instance = null;
    }
}
