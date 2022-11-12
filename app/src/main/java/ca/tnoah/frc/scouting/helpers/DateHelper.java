package ca.tnoah.frc.scouting.helpers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateHelper {
    private static final String TODAY_AT = "Today at ";
    private static final String YESTERDAY_AT = "Yesterday at ";

    public static String simpleDate(Date date) {
        if (date == null) return "";

        if (isToday(date))
            return TODAY_AT + getTime(date);
        if (isYesterday(date))
            return YESTERDAY_AT + getTime(date);

        return getDateTime(date);
    }

    public static boolean isSameDay(Date date1, Date date2) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd", Locale.CANADA);
        return fmt.format(date1).equals(fmt.format(date2));
    }

    public static boolean isToday(Date date) {
        return isSameDay(date, new Date());
    }

    public static boolean isYesterday(Date date) {
        Date yesterday = new Date(System.currentTimeMillis()-24*60*60*1000);
        return isSameDay(yesterday, date);
    }

    public static boolean isSameMonth(Date date1, Date date2) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMM", Locale.CANADA);
        return fmt.format(date1).equals(fmt.format(date2));
    }

    public static boolean isSameYear(Date date1, Date date2) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy", Locale.CANADA);
        return fmt.format(date1).equals(fmt.format(date2));
    }

    public static String getTime(Date date) {
        SimpleDateFormat fmt = new SimpleDateFormat("h:mm a", Locale.CANADA);
        return fmt.format(date);
    }

    public static String getDate(Date date) {
        SimpleDateFormat fmt = new SimpleDateFormat("MMMM d", Locale.CANADA);
        return fmt.format(date);
    }

    public static String getDateTime(Date date) {
        return getDate(date) + " at " + getTime(date);
    }

}