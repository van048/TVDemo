package cn.ben.tvdemo.data.tvtype.source.remote;

import java.util.List;

import cn.ben.tvdemo.constant.Constants;
import cn.ben.tvdemo.data.tvtype.TVTypes;
import cn.ben.tvdemo.data.tvtype.source.TVTypesDataSource;
import io.reactivex.Observable;
import io.reactivex.functions.Function;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class TVTypesRemoteDataSource implements TVTypesDataSource {
    private static final String LOAD_TV_TYPES_BASE_URL = "http://api.avatardata.cn/TVTime/";
    private volatile static TVTypesRemoteDataSource instance = null;

    public static TVTypesRemoteDataSource getInstance() {
        if (instance == null) {
            synchronized (TVTypesRemoteDataSource.class) {
                if (instance == null) {
                    instance = new TVTypesRemoteDataSource();
                }
            }
        }
        return instance;
    }

    private TVTypesRemoteDataSource() {
    }

    @Override
    public Observable<List<TVTypes.TVType>> getTVTypes() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(LOAD_TV_TYPES_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        TVTypesService service = retrofit.create(TVTypesService.class);
        Observable<TVTypes> call = service.getTVTypes(Constants.API_KEY);
        return call
                .map(new Function<TVTypes, List<TVTypes.TVType>>() {
                    @Override
                    public List<TVTypes.TVType> apply(TVTypes tvTypes) throws Exception {
                        if (tvTypes.getError_code() != 0)
                            throw new Exception(tvTypes.getReason());
                        return tvTypes.getResult();
                    }
                });
    }

    @Override
    public void deleteAllTVTypes() {
    }

    @Override
    public void saveTVType(TVTypes.TVType tvType) {
    }

    @Override
    public void cleanup() {
        instance = null;
    }

    interface TVTypesService {
        @GET("Query")
        Observable<TVTypes> getTVTypes(@Query("key") String key);
    }
}
