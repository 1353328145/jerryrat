package fun.jexing.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private static boolean log = true;
    public static void log(String msg,Class<?> c){
        if (log){
            StringBuilder sb = new StringBuilder("时间: ");
            sb.append(Logger.format.format(new Date()));
            sb.append(" | ").append(Thread.currentThread().getName()).append(" --> ").append(c.getName()).append(" : ").append(msg);
            System.out.println(sb);
        }
    }
}
