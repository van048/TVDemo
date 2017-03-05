package cn.ben.tvdemo.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeUtil {
    public static final String FORMAT_YEAR_MONTH_DAY = "yyyy-MM-dd";
    public static final String FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE = "yyyy-MM-dd HH:mm";

    private TimeUtil() {
    }

    public static String plusOnCurrentDate(int inc) {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, inc);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT_YEAR_MONTH_DAY, Locale.getDefault());
        return simpleDateFormat.format(calendar.getTime());
    }

    public static String date2String(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        return sdf.format(date);
    }

    public static Date string2Date(String dateStr, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
