package cn.ben.tvdemo.data.tvtype.source.remote;

import android.support.annotation.NonNull;

import cn.ben.tvdemo.constant.Constants;
import cn.ben.tvdemo.data.tvtype.TVTypes;
import cn.ben.tvdemo.data.tvtype.source.TVTypesDataSource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;

class TVTypesRemoteDataSource implements TVTypesDataSource {
    private static final String LOAD_TV_TYPE_BASE_URL = "http://api.avatardata.cn/TVTime/";
    private static TVTypesRemoteDataSource instance = null;

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
    public void getTVTypes(@NonNull final LoadTVTypesCallback loadTVTypesCallback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(LOAD_TV_TYPE_BASE_URL)
                .build();
        TVTypesService service = retrofit.create(TVTypesService.class);
        Call<TVTypes> call = service.getTVTypes(Constants.API_KEY);
        call.enqueue(new Callback<TVTypes>() {
            @Override
            public void onResponse(Call<TVTypes> call, Response<TVTypes> response) {
                if (response.isSuccessful()) {
                    TVTypes tvTypes = response.body();
                    if (tvTypes != null) {
                        int error_code = tvTypes.getError_code();
                        if (error_code == 0) {
                            loadTVTypesCallback.onTVTypesLoaded(tvTypes.getResult());
                        } else {
                            loadTVTypesCallback.onDataNotAvailable(tvTypes.getReason());
                        }
                    } else {
                        loadTVTypesCallback.onDataNotAvailable("Response Error");
                    }
                } else {
                    loadTVTypesCallback.onDataNotAvailable("Response Not Successful");
                }
            }

            @Override
            public void onFailure(Call<TVTypes> call, Throwable t) {
                loadTVTypesCallback.onDataNotAvailable(t.getLocalizedMessage());
            }
        });
    }

    @Override
    public void deleteAllTVTypes() {
    }

    @Override
    public void saveTVType(TVTypes.TVType tvType) {
    }

    interface TVTypesService {
        @GET("Query")
        Call<TVTypes> getTVTypes(@Query("key") String key);
    }
}
