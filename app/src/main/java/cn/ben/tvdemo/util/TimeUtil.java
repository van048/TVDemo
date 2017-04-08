package cn.ben.tvdemo.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeUtil {
    public static final String FORMAT_YEAR_MONTH_DAY = "yyyy-MM-dd";
    public static final String FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE = "yyyy-MM-dd HH:mm";
    private static final Calendar mCalendar = Calendar.getInstance();

    private TimeUtil() {
    }

    public static String plusOnDate(String date, int inc, String inputFormat, String outputFormat) {
        mCalendar.setTime(string2Date(date, inputFormat));
        mCalendar.add(Calendar.DATE, inc);
        return date2String(mCalendar.getTime(), outputFormat);
    }

    public static String plusOnCurrentDate(int inc, String outputFormat) {
        mCalendar.setTime(new Date());
        mCalendar.add(Calendar.DATE, inc);
        return date2String(mCalendar.getTime(), outputFormat);
    }

    public static Date plusOnCurrentDate(int inc) {
        mCalendar.setTime(new Date());
        mCalendar.add(Calendar.DATE, inc);
        return mCalendar.getTime();
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

    public static boolean areSameDay(Date dateA, Date dateB) {
        Calendar calDateA = Calendar.getInstance();
        calDateA.setTime(dateA);

        Calendar calDateB = Calendar.getInstance();
        calDateB.setTime(dateB);

        return calDateA.get(Calendar.YEAR) == calDateB.get(Calendar.YEAR)
                && calDateA.get(Calendar.MONTH) == calDateB.get(Calendar.MONTH)
                && calDateA.get(Calendar.DAY_OF_MONTH) == calDateB.get(Calendar.DAY_OF_MONTH);
    }
}
