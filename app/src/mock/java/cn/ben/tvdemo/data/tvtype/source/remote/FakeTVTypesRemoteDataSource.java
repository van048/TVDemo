package cn.ben.tvdemo.data.tvtype.source.remote;

import com.google.gson.Gson;

import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.ben.tvdemo.data.tvtype.TVTypes;
import cn.ben.tvdemo.data.tvtype.source.TVTypesDataSource;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class FakeTVTypesRemoteDataSource implements TVTypesDataSource {
    private volatile static FakeTVTypesRemoteDataSource instance = null;

    public static FakeTVTypesRemoteDataSource getInstance() {
        if (instance == null) {
            synchronized (FakeTVTypesRemoteDataSource.class) {
                if (instance == null) {
                    instance = new FakeTVTypesRemoteDataSource();
                }
            }
        }
        return instance;
    }

    private FakeTVTypesRemoteDataSource() {
    }

    @Override
    public Observable<List<TVTypes.TVType>> getTVTypes() {
        final TVTypes tvTypes = new Gson().fromJson("{\n" +
                "    \"result\": [\n" +
                "        {\n" +
                "            \"id\": \"1\",\n" +
                "            \"name\": \"央视\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": \"2\",\n" +
                "            \"name\": \"卫视\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": \"3\",\n" +
                "            \"name\": \"数字\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": \"4\",\n" +
                "            \"name\": \"城市\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": \"5\",\n" +
                "            \"name\": \"CETV\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": \"6\",\n" +
                "            \"name\": \"原创\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"error_code\": 0,\n" +
                "    \"reason\": \"Success\"\n" +
                "}", TVTypes.class);
        return Observable
                .timer(3, TimeUnit.SECONDS)
                .map(new Function<Long, List<TVTypes.TVType>>() {
                    @Override
                    public List<TVTypes.TVType> apply(Long aLong) throws Exception {
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
}
