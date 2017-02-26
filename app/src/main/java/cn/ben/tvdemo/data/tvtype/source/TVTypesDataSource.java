package cn.ben.tvdemo.data.tvtype.source;

import java.util.List;

import cn.ben.tvdemo.data.tvtype.TVTypes;
import io.reactivex.Observable;

public interface TVTypesDataSource {

    Observable<List<TVTypes.TVType>> getTVTypes();

    void deleteAllTVTypes();

    void saveTVType(TVTypes.TVType tvType);

    void cleanup();
}
