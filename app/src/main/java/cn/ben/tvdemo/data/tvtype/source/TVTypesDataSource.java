package cn.ben.tvdemo.data.tvtype.source;

import android.support.annotation.NonNull;

import java.util.List;

import cn.ben.tvdemo.data.tvtype.TVTypes;

public interface TVTypesDataSource {
    interface LoadTVTypesCallback {
        void onTVTypesLoaded(List<TVTypes.TVType> tvTypes);

        void onDataNotAvailable(String reason);
    }

    void getTVTypes(@NonNull LoadTVTypesCallback loadTVTypesCallback);

    void deleteAllTVTypes();

    void saveTVType(TVTypes.TVType tvType);
}
