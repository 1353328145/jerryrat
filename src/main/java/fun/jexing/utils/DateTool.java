package fun.jexing.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateTool {
    /**
     * HTTP date format.
     */
    protected static SimpleDateFormat format =
            new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);
    static {
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
    }
    public static String currentData(){
        Date date  =new Date();
        String format = DateTool.format.format(date);
        return format;
    }
}
