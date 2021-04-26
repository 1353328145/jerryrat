package fun.jexing.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * HTTP date format.
 */
public class DateTool {
    protected static SimpleDateFormat format =
            new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);
    static {
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
    }
    public static String currentDate(){
        Date date  =new Date();
        return DateTool.format.format(date);
    }

    public static String getDateString(Date date){
        return DateTool.format.format(date);
    }
}
