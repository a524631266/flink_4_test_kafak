package practicing.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @Auther: Liangliang.Zhang4
 * @Date: 2022/6/20
 * @Description:
 * @Version: 1.0
 */
public class TimeUtils {

    public static Date strToDate(String dateStr) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss SSS");
        Date date = null;
        date = sdf.parse(dateStr);
        return date;
    }


    public static String dateToString(Date date)  {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }


    public static String getHHmmss(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss SSS");
        String str = sdf.format(date);
        return str;
    }

    public static String getHHmmss(Long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss SSS");
        String str = sdf.format(new Date(time));
        return str;
    }

    public static String getSS(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("ss SSS");
        String str = sdf.format(date);
        return str;
    }

    public static String getSS(Long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("ss SSS");
        String str = sdf.format(new Date(time));
        return str;
    }


    public static String getMm(Long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("mm");
        String str = sdf.format(new Date(time));
        return str;
    }

    public static String getWithoutMm(Long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
        String str = sdf.format(new Date(time));
        return str;
    }

    public static void main(String[] args) {


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
        String str = sdf.format(new Date());
        System.out.println(str);

        System.out.println(Integer.valueOf("00"));


    }
}
