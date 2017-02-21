package cn.ben.tvdemo.data.tvtype.source.local;

import android.provider.BaseColumns;

public final class TVTypesPersistenceContract {
    private TVTypesPersistenceContract() {
    }
    public static abstract class TVTypeEntry implements BaseColumns {
        public static final String TABLE_NAME = "tv_type";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
    }
}
