package cn.ben.tvdemo.data.tvtype.source.local;

import android.provider.BaseColumns;

final class TVTypesPersistenceContract {
    private TVTypesPersistenceContract() {
    }
    static abstract class TVTypeEntry implements BaseColumns {
        static final String TABLE_NAME = "tv_type";
        static final String COLUMN_ID = "id";
        static final String COLUMN_NAME = "name";
    }
}
