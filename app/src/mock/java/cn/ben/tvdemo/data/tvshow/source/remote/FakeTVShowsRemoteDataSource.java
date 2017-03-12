package cn.ben.tvdemo.data.tvshow.source.remote;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.ben.tvdemo.data.tvshow.TVShows;
import cn.ben.tvdemo.data.tvshow.source.TVShowsDataSource;
import cn.ben.tvdemo.util.TimeUtil;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class FakeTVShowsRemoteDataSource implements TVShowsDataSource {
    private volatile static FakeTVShowsRemoteDataSource instance = null;

    public static FakeTVShowsRemoteDataSource getInstance() {
        if (instance == null) {
            synchronized (FakeTVShowsRemoteDataSource.class) {
                if (instance == null) {
                    instance = new FakeTVShowsRemoteDataSource();
                }
            }
        }
        return instance;
    }

    @Override
    public Observable<List<TVShows.TVShow>> getTVShowsWithCodeAndDate(String code, String date) {
        TVShows tvShows = new Gson().fromJson("{\n" +
                "  \"result\": [\n" +
                "    {\n" +
                "      \"cName\": \"CCTV-5 体育\",\n" +
                "      \"pName\": \"2016年欧洲足球锦标赛-决赛阶段抽签仪式\",\n" +
                "      \"pUrl\": \"http://tv.cntv.cn/live/cctv5?date=2015-12-13&index=0\",\n" +
                "      \"time\": \"2015-12-13 00:30\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"cName\": \"CCTV-5 体育\",\n" +
                "      \"pName\": \"2015/2016赛季花滑大奖赛总决赛-女单自由滑等\",\n" +
                "      \"pUrl\": \"http://tv.cntv.cn/live/cctv5?date=2015-12-13&index=1\",\n" +
                "      \"time\": \"2015-12-13 02:15\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"cName\": \"CCTV-5 体育\",\n" +
                "      \"pName\": \"赛事集锦-2015/2016赛季英格兰足球超级联赛第15轮比赛集锦\",\n" +
                "      \"pUrl\": \"http://tv.cntv.cn/live/cctv5?date=2015-12-13&index=2\",\n" +
                "      \"time\": \"2015-12-13 05:00\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"cName\": \"CCTV-5 体育\",\n" +
                "      \"pName\": \"健身动起来\",\n" +
                "      \"pUrl\": \"http://tv.cntv.cn/live/cctv5?date=2015-12-13&index=3\",\n" +
                "      \"time\": \"2015-12-13 06:00\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"cName\": \"CCTV-5 体育\",\n" +
                "      \"pName\": \"体育晨报\",\n" +
                "      \"pUrl\": \"http://tv.cntv.cn/live/cctv5?date=2015-12-13&index=4\",\n" +
                "      \"time\": \"2015-12-13 06:30\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"cName\": \"CCTV-5 体育\",\n" +
                "      \"pName\": \"实况录像-2015年国际乒联职业巡回赛总决赛\",\n" +
                "      \"pUrl\": \"http://tv.cntv.cn/live/cctv5?date=2015-12-13&index=5\",\n" +
                "      \"time\": \"2015-12-13 08:35\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"cName\": \"CCTV-5 体育\",\n" +
                "      \"pName\": \"2015/2016赛季NBA常规赛-勇士-雄鹿\",\n" +
                "      \"pUrl\": \"http://tv.cntv.cn/live/cctv5?date=2015-12-13&index=6\",\n" +
                "      \"time\": \"2015-12-13 09:30\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"cName\": \"CCTV-5 体育\",\n" +
                "      \"pName\": \"体坛快讯\",\n" +
                "      \"pUrl\": \"http://tv.cntv.cn/live/cctv5?date=2015-12-13&index=7\",\n" +
                "      \"time\": \"2015-12-13 12:00\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"cName\": \"CCTV-5 体育\",\n" +
                "      \"pName\": \"2015年世界职业拳王争霸赛-47\",\n" +
                "      \"pUrl\": \"http://tv.cntv.cn/live/cctv5?date=2015-12-13&index=8\",\n" +
                "      \"time\": \"2015-12-13 12:30\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"cName\": \"CCTV-5 体育\",\n" +
                "      \"pName\": \"2015年国际足联俱乐部世界杯1/4决赛1-（墨西哥美洲队-中国广州恒大队）\",\n" +
                "      \"pUrl\": \"http://tv.cntv.cn/live/cctv5?date=2015-12-13&index=9\",\n" +
                "      \"time\": \"2015-12-13 14:00\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"cName\": \"CCTV-5 体育\",\n" +
                "      \"pName\": \"2015年国际泳联花样游泳大奖赛-集体自由自选决赛等\",\n" +
                "      \"pUrl\": \"http://tv.cntv.cn/live/cctv5\",\n" +
                "      \"time\": \"2015-12-13 17:00\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"cName\": \"CCTV-5 体育\",\n" +
                "      \"pName\": \"体育新闻\",\n" +
                "      \"pUrl\": \"\",\n" +
                "      \"time\": \"2015-12-13 18:00\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"cName\": \"CCTV-5 体育\",\n" +
                "      \"pName\": \"实况录像-2015/2016赛季短道速滑世界杯上海站（录播）\",\n" +
                "      \"pUrl\": \"\",\n" +
                "      \"time\": \"2015-12-13 18:30\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"cName\": \"CCTV-5 体育\",\n" +
                "      \"pName\": \"2015/2016赛季中国男子篮球职业联赛-江苏肯帝亚-新疆天山农商银行\",\n" +
                "      \"pUrl\": \"\",\n" +
                "      \"time\": \"2015-12-13 19:30\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"cName\": \"CCTV-5 体育\",\n" +
                "      \"pName\": \"2015年国际乒联职业巡回赛-总决赛-男女单打半决赛\",\n" +
                "      \"pUrl\": \"\",\n" +
                "      \"time\": \"2015-12-13 21:30\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"error_code\": 0,\n" +
                "  \"reason\": \"Success\"\n" +
                "}", TVShows.class);
        final List<TVShows.TVShow> shows = tvShows.getResult();

        Calendar calendar = Calendar.getInstance();
        Date newDate = TimeUtil.string2Date(date, TimeUtil.FORMAT_YEAR_MONTH_DAY);
        calendar.setTime(newDate);
        int newYear = calendar.get(Calendar.YEAR);
        int newMonth = calendar.get(Calendar.MONTH);
        int newDay = calendar.get(Calendar.DAY_OF_MONTH);

        for (int i = 0; i < shows.size(); i++) {
            TVShows.TVShow show = shows.get(i);
            show.setChannelCode(code);
            show.setCName(code + "_" + show.getCName()); // no use
            show.setPName(code + "_" + show.getPName());
            // url no use

            String oldTime = show.getTime();
            Date oldDate = TimeUtil.string2Date(oldTime, TimeUtil.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE);
            calendar.setTime(oldDate);
            calendar.set(Calendar.YEAR, newYear);
            calendar.set(Calendar.MONTH, newMonth);
            calendar.set(Calendar.DAY_OF_MONTH, newDay);
            show.setTime(TimeUtil.date2String(calendar.getTime(), TimeUtil.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE));

            // consistent fav
            // always real cctv5's tomorrow first show
            if (i == 0 && code.equals("1_cctv5")) {
                calendar.setTime(new Date());
                calendar.add(Calendar.DATE, 1);
                if (date.equals(TimeUtil.date2String(calendar.getTime(), TimeUtil.FORMAT_YEAR_MONTH_DAY))) {
                    show.setFav(true);
                }
            }
        }

        return Observable
                .timer(3, TimeUnit.SECONDS)
                .map(new Function<Long, List<TVShows.TVShow>>() {
                    @Override
                    public List<TVShows.TVShow> apply(Long aLong) throws Exception {
                        return shows;
                    }
                });
    }

    @Override
    public Observable<List<TVShows.TVShow>> getFavTVShows() {
        // always real cctv5's tomorrow first show
        TVShows tvShows = new Gson().fromJson("{\n" +
                "  \"result\": [\n" +
                "    {\n" +
                "      \"cName\": \"CCTV-5 体育\",\n" +
                "      \"pName\": \"2016年欧洲足球锦标赛-决赛阶段抽签仪式\",\n" +
                "      \"pUrl\": \"http://tv.cntv.cn/live/cctv5?date=2015-12-13&index=0\",\n" +
                "      \"time\": \"2015-12-13 00:30\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"cName\": \"CCTV-5 体育\",\n" +
                "      \"pName\": \"2015/2016赛季花滑大奖赛总决赛-女单自由滑等\",\n" +
                "      \"pUrl\": \"http://tv.cntv.cn/live/cctv5?date=2015-12-13&index=1\",\n" +
                "      \"time\": \"2015-12-13 02:15\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"cName\": \"CCTV-5 体育\",\n" +
                "      \"pName\": \"赛事集锦-2015/2016赛季英格兰足球超级联赛第15轮比赛集锦\",\n" +
                "      \"pUrl\": \"http://tv.cntv.cn/live/cctv5?date=2015-12-13&index=2\",\n" +
                "      \"time\": \"2015-12-13 05:00\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"cName\": \"CCTV-5 体育\",\n" +
                "      \"pName\": \"健身动起来\",\n" +
                "      \"pUrl\": \"http://tv.cntv.cn/live/cctv5?date=2015-12-13&index=3\",\n" +
                "      \"time\": \"2015-12-13 06:00\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"cName\": \"CCTV-5 体育\",\n" +
                "      \"pName\": \"体育晨报\",\n" +
                "      \"pUrl\": \"http://tv.cntv.cn/live/cctv5?date=2015-12-13&index=4\",\n" +
                "      \"time\": \"2015-12-13 06:30\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"cName\": \"CCTV-5 体育\",\n" +
                "      \"pName\": \"实况录像-2015年国际乒联职业巡回赛总决赛\",\n" +
                "      \"pUrl\": \"http://tv.cntv.cn/live/cctv5?date=2015-12-13&index=5\",\n" +
                "      \"time\": \"2015-12-13 08:35\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"cName\": \"CCTV-5 体育\",\n" +
                "      \"pName\": \"2015/2016赛季NBA常规赛-勇士-雄鹿\",\n" +
                "      \"pUrl\": \"http://tv.cntv.cn/live/cctv5?date=2015-12-13&index=6\",\n" +
                "      \"time\": \"2015-12-13 09:30\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"cName\": \"CCTV-5 体育\",\n" +
                "      \"pName\": \"体坛快讯\",\n" +
                "      \"pUrl\": \"http://tv.cntv.cn/live/cctv5?date=2015-12-13&index=7\",\n" +
                "      \"time\": \"2015-12-13 12:00\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"cName\": \"CCTV-5 体育\",\n" +
                "      \"pName\": \"2015年世界职业拳王争霸赛-47\",\n" +
                "      \"pUrl\": \"http://tv.cntv.cn/live/cctv5?date=2015-12-13&index=8\",\n" +
                "      \"time\": \"2015-12-13 12:30\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"cName\": \"CCTV-5 体育\",\n" +
                "      \"pName\": \"2015年国际足联俱乐部世界杯1/4决赛1-（墨西哥美洲队-中国广州恒大队）\",\n" +
                "      \"pUrl\": \"http://tv.cntv.cn/live/cctv5?date=2015-12-13&index=9\",\n" +
                "      \"time\": \"2015-12-13 14:00\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"cName\": \"CCTV-5 体育\",\n" +
                "      \"pName\": \"2015年国际泳联花样游泳大奖赛-集体自由自选决赛等\",\n" +
                "      \"pUrl\": \"http://tv.cntv.cn/live/cctv5\",\n" +
                "      \"time\": \"2015-12-13 17:00\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"cName\": \"CCTV-5 体育\",\n" +
                "      \"pName\": \"体育新闻\",\n" +
                "      \"pUrl\": \"\",\n" +
                "      \"time\": \"2015-12-13 18:00\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"cName\": \"CCTV-5 体育\",\n" +
                "      \"pName\": \"实况录像-2015/2016赛季短道速滑世界杯上海站（录播）\",\n" +
                "      \"pUrl\": \"\",\n" +
                "      \"time\": \"2015-12-13 18:30\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"cName\": \"CCTV-5 体育\",\n" +
                "      \"pName\": \"2015/2016赛季中国男子篮球职业联赛-江苏肯帝亚-新疆天山农商银行\",\n" +
                "      \"pUrl\": \"\",\n" +
                "      \"time\": \"2015-12-13 19:30\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"cName\": \"CCTV-5 体育\",\n" +
                "      \"pName\": \"2015年国际乒联职业巡回赛-总决赛-男女单打半决赛\",\n" +
                "      \"pUrl\": \"\",\n" +
                "      \"time\": \"2015-12-13 21:30\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"error_code\": 0,\n" +
                "  \"reason\": \"Success\"\n" +
                "}", TVShows.class);
        final List<TVShows.TVShow> shows = new ArrayList<>();
        TVShows.TVShow favShow = tvShows.getResult().get(0);

        String code = "1_cctv5";
        favShow.setChannelCode(code);
        favShow.setCName(code + "_" + favShow.getCName()); // no use
        favShow.setPName(code + "_" + favShow.getPName());
        // url no use

        Calendar calendar = Calendar.getInstance();
        Date newDate = new Date();
        calendar.setTime(newDate);
        calendar.add(Calendar.DATE, 1);
        int newYear = calendar.get(Calendar.YEAR);
        int newMonth = calendar.get(Calendar.MONTH);
        int newDay = calendar.get(Calendar.DAY_OF_MONTH);
        
        String oldTime = favShow.getTime();
        Date oldDate = TimeUtil.string2Date(oldTime, TimeUtil.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE);
        calendar.setTime(oldDate);
        calendar.set(Calendar.YEAR, newYear);
        calendar.set(Calendar.MONTH, newMonth);
        calendar.set(Calendar.DAY_OF_MONTH, newDay);
        favShow.setTime(TimeUtil.date2String(calendar.getTime(), TimeUtil.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE));

        shows.add(favShow);

        return Observable
                .timer(3, TimeUnit.SECONDS)
                .map(new Function<Long, List<TVShows.TVShow>>() {
                    @Override
                    public List<TVShows.TVShow> apply(Long aLong) throws Exception {
                        return shows;
                    }
                });
    }

    @Override
    public void cleanup() {
    }

    @Override
    public void update(String code, String date, List<TVShows.TVShow> shows) {
    }
}
