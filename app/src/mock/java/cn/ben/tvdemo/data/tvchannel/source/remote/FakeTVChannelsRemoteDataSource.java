package cn.ben.tvdemo.data.tvchannel.source.remote;

import com.google.gson.Gson;

import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.ben.tvdemo.data.tvchannel.TVChannels;
import cn.ben.tvdemo.data.tvchannel.source.TVChannelsDataSource;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class FakeTVChannelsRemoteDataSource implements TVChannelsDataSource {
    private volatile static FakeTVChannelsRemoteDataSource instance = null;

    public static FakeTVChannelsRemoteDataSource getInstance() {
        if (instance == null) {
            synchronized (FakeTVChannelsRemoteDataSource.class) {
                if (instance == null) {
                    instance = new FakeTVChannelsRemoteDataSource();
                }
            }
        }
        return instance;
    }

    private FakeTVChannelsRemoteDataSource() {
    }

    @Override
    public Observable<List<TVChannels.TVChannel>> getTVChannelsWithPID(String pId) {
        TVChannels tvChannels = new Gson().fromJson("{\n" +
                "  \"result\": [\n" +
                "    {\n" +
                "      \"channelName\": \"CCTV-1 综合\",\n" +
                "      \"pId\": \"1\",\n" +
                "      \"rel\": \"cctv1\",\n" +
                "      \"url\": \"http://tv.cntv.cn/live/cctv1\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"channelName\": \"CCTV-2 财经\",\n" +
                "      \"pId\": \"1\",\n" +
                "      \"rel\": \"cctv2\",\n" +
                "      \"url\": \"http://tv.cntv.cn/live/cctv2\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"channelName\": \"CCTV-3 综艺\",\n" +
                "      \"pId\": \"1\",\n" +
                "      \"rel\": \"cctv3\",\n" +
                "      \"url\": \"http://tv.cntv.cn/live/cctv3\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"channelName\": \"CCTV-4 (亚洲)\",\n" +
                "      \"pId\": \"1\",\n" +
                "      \"rel\": \"cctv4\",\n" +
                "      \"url\": \"http://tv.cntv.cn/live/cctv4\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"channelName\": \"CCTV-4 (欧洲)\",\n" +
                "      \"pId\": \"1\",\n" +
                "      \"rel\": \"cctveurope\",\n" +
                "      \"url\": \"http://tv.cntv.cn/live/cctveurope\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"channelName\": \"CCTV-4 (美洲)\",\n" +
                "      \"pId\": \"1\",\n" +
                "      \"rel\": \"cctvamerica\",\n" +
                "      \"url\": \"http://tv.cntv.cn/live/cctvamerica\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"channelName\": \"CCTV-5 体育\",\n" +
                "      \"pId\": \"1\",\n" +
                "      \"rel\": \"cctv5\",\n" +
                "      \"url\": \"http://tv.cntv.cn/live/cctv5\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"channelName\": \"CCTV-6 电影\",\n" +
                "      \"pId\": \"1\",\n" +
                "      \"rel\": \"cctv6\",\n" +
                "      \"url\": \"http://tv.cntv.cn/live/cctv6\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"channelName\": \"CCTV-7 军事农业\",\n" +
                "      \"pId\": \"1\",\n" +
                "      \"rel\": \"cctv7\",\n" +
                "      \"url\": \"http://tv.cntv.cn/live/cctv7\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"channelName\": \"CCTV-8 电视剧\",\n" +
                "      \"pId\": \"1\",\n" +
                "      \"rel\": \"cctv8\",\n" +
                "      \"url\": \"http://tv.cntv.cn/live/cctv8\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"channelName\": \"CCTV-9 纪录\",\n" +
                "      \"pId\": \"1\",\n" +
                "      \"rel\": \"cctvjilu\",\n" +
                "      \"url\": \"http://tv.cntv.cn/live/cctvjilu\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"channelName\": \"CCTV-9 纪录(英)\",\n" +
                "      \"pId\": \"1\",\n" +
                "      \"rel\": \"cctvdoc\",\n" +
                "      \"url\": \"http://tv.cntv.cn/live/cctvdoc\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"channelName\": \"CCTV-10 科教\",\n" +
                "      \"pId\": \"1\",\n" +
                "      \"rel\": \"cctv10\",\n" +
                "      \"url\": \"http://tv.cntv.cn/live/cctv10\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"channelName\": \"CCTV-11 戏曲\",\n" +
                "      \"pId\": \"1\",\n" +
                "      \"rel\": \"cctv11\",\n" +
                "      \"url\": \"http://tv.cntv.cn/live/cctv11\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"channelName\": \"CCTV-12 社会与法\",\n" +
                "      \"pId\": \"1\",\n" +
                "      \"rel\": \"cctv12\",\n" +
                "      \"url\": \"http://tv.cntv.cn/live/cctv12\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"channelName\": \"CCTV-13 新闻\",\n" +
                "      \"pId\": \"1\",\n" +
                "      \"rel\": \"cctv13\",\n" +
                "      \"url\": \"http://tv.cntv.cn/live/cctv13\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"channelName\": \"CCTV-14 少儿\",\n" +
                "      \"pId\": \"1\",\n" +
                "      \"rel\": \"cctvchild\",\n" +
                "      \"url\": \"http://tv.cntv.cn/live/cctvchild\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"channelName\": \"CCTV-15 音乐\",\n" +
                "      \"pId\": \"1\",\n" +
                "      \"rel\": \"cctv15\",\n" +
                "      \"url\": \"http://tv.cntv.cn/live/cctv15\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"channelName\": \"CCTV-NEWS\",\n" +
                "      \"pId\": \"1\",\n" +
                "      \"rel\": \"cctv9\",\n" +
                "      \"url\": \"http://tv.cntv.cn/live/cctv9\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"channelName\": \"CCTV-Français\",\n" +
                "      \"pId\": \"1\",\n" +
                "      \"rel\": \"cctvfrench\",\n" +
                "      \"url\": \"http://tv.cntv.cn/live/cctvfrench\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"channelName\": \"CCTV-Español\",\n" +
                "      \"pId\": \"1\",\n" +
                "      \"rel\": \"cctvxiyu\",\n" +
                "      \"url\": \"http://tv.cntv.cn/live/cctvxiyu\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"channelName\": \"CCTV-العربية\",\n" +
                "      \"pId\": \"1\",\n" +
                "      \"rel\": \"cctvarabic\",\n" +
                "      \"url\": \"http://tv.cntv.cn/live/cctvarabic\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"channelName\": \"CCTV-Русский\",\n" +
                "      \"pId\": \"1\",\n" +
                "      \"rel\": \"cctvrussian\",\n" +
                "      \"url\": \"http://tv.cntv.cn/live/cctvrussian\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"channelName\": \"CCTV体育赛事\",\n" +
                "      \"pId\": \"1\",\n" +
                "      \"rel\": \"cctv5plus\",\n" +
                "      \"url\": \"http://tv.cntv.cn/live/cctv5plus\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"error_code\": 0,\n" +
                "  \"reason\": \"Success\"\n" +
                "}", TVChannels.class);
        final List<TVChannels.TVChannel> list = tvChannels.getResult();
        for (TVChannels.TVChannel tvChannel : list) {
            tvChannel.setPId(pId);
            tvChannel.setChannelName(pId + "_" + tvChannel.getChannelName());
            tvChannel.setRel(pId + "_" + tvChannel.getRel());
        }
        return Observable
                .timer(3, TimeUnit.SECONDS)
                .map(new Function<Long, List<TVChannels.TVChannel>>() {
                    @Override
                    public List<TVChannels.TVChannel> apply(Long aLong) throws Exception {
                        return list;
                    }
                });
    }

    @Override
    public void deleteAllTVChannelsWithPID(String pId) {
    }

    @Override
    public void saveTVChannel(TVChannels.TVChannel tvChannel) {
    }

    @Override
    public void cleanup() {
        instance = null;
    }
}
