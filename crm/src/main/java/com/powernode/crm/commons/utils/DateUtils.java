package com.powernode.crm.commons.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author LXM
 * @create 2022-04-27 21:32
 */
public class DateUtils {
    public static String formatDateTime(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowStr = sdf.format(new Date());
        return nowStr;
    }

    public static String formatDate(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdf.format(date);
        return dateStr;
    }


    public static String formatTime(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String s = sdf.format(date);
        return s;
    }
}
