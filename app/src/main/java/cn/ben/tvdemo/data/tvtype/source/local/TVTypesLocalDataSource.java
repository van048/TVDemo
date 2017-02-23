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

import static cn.ben.tvdemo.data.tvtype.source.local.TVTypesPersistenceContract.TVTypeEntry.COLUMN_ID;
import static cn.ben.tvdemo.data.tvtype.source.local.TVTypesPersistenceContract.TVTypeEntry.COLUMN_NAME;
import static cn.ben.tvdemo.data.tvtype.source.local.TVTypesPersistenceContract.TVTypeEntry.TABLE_NAME;
import static com.google.common.base.Preconditions.checkNotNull;

public class TVTypesLocalDataSource implements TVTypesDataSource {
    private volatile static TVTypesLocalDataSource instance = null;
    private TVTypesDbHelper mDbHelper;

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
    }

    @Override
    public void getTVTypes(@NonNull LoadTVTypesCallback loadTVTypesCallback) {
        List<TVTypes.TVType> tvTypes = new ArrayList<>();
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
        if (tvTypes.isEmpty()) {
            loadTVTypesCallback.onDataNotAvailable("No Entries In Local DB");
        } else {
            loadTVTypesCallback.onTVTypesLoaded(tvTypes);
        }
    }

    @Override
    public void deleteAllTVTypes() {
        SQLiteDatabase sqLiteDatabase = mDbHelper.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_NAME, null, null);
        sqLiteDatabase.close();
    }

    @Override
    public void saveTVType(@NonNull TVTypes.TVType tvType) {
        checkNotNull(tvType);

        SQLiteDatabase sqLiteDatabase = mDbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, tvType.getId());
        contentValues.put(COLUMN_NAME, tvType.getName());

        sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        sqLiteDatabase.close();
    }
}
