package com.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 日期公用类
 *
 * @author XJin zuzjx@163.com
 */
public class DateTimeUtil {

    /**
     * 英文简写（默认）如：2010-12-01
     */
    public static String FORMAT_SHORT = "yyyy-MM-dd";

    /**
     * 英文全称 如：2010-12-01 23:15:06
     */
    public static String FORMAT_LONG = "yyyy-MM-dd HH:mm:ss";

    /**
     * 精确到毫秒的完整时间 如：yyyy-MM-dd HH:mm:ss.S
     */
    public static String FORMAT_FULL = "yyyy-MM-dd HH:mm:ss.S";

    /**
     * 获得默认的 date pattern
     */
    public static String getDatePattern() {
        return FORMAT_LONG;
    }

    /**
     * 根据预设格式返回当前日期
     *
     * @return
     */
    public static String getNow() {
        return format(new Date());
    }

    /**
     * 根据用户格式返回当前日期
     *
     * @param format
     * @return
     */
    public static String getNow(String format) {
        return format(new Date(), format);
    }

    /**
     * 使用预设格式格式化日期
     *
     * @param date
     * @return
     */
    public static String format(Date date) {
        if (date == null) {
            return null;
        }
        return format(date, getDatePattern());
    }

    /**
     * 使用用户格式格式化日期
     *
     * @param date    日期
     * @param pattern 日期格式
     * @return
     */
    public static String format(Date date, String pattern) {
        String returnValue = "";
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            returnValue = df.format(date);
        }
        return (returnValue);
    }

    /**
     * 使用预设格式提取字符串日期
     *
     * @param strDate 日期字符串
     * @return
     */
    public static Date parse(String strDate) {
        return parse(strDate, getDatePattern());
    }

    /**
     * 使用用户格式提取字符串日期
     *
     * @param strDate 日期字符串
     * @param pattern 日期格式
     * @return
     */
    public static Date parse(String strDate, String pattern) {
        if (strDate == null || ("").equals(strDate)) {
            return null;
        }
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        try {
            return df.parse(strDate);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 在日期上增加数个整月
     *
     * @param date 日期
     * @param n    要增加的月数
     * @return
     */
    public static Date addMonth(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, n);
        return cal.getTime();
    }

    /**
     * 在日期上增加天数
     *
     * @param date 日期
     * @param n    要增加的天数
     * @return
     */
    public static Date addDay(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, n);
        return cal.getTime();
    }

    /**
     * 获取时间戳
     */
    public static String getTimeString() {
        SimpleDateFormat df = new SimpleDateFormat(FORMAT_FULL);
        Calendar calendar = Calendar.getInstance();
        return df.format(calendar.getTime());
    }

    /**
     * 获取日期年份
     *
     * @param date 日期
     * @return
     */
    public static String getYear(Date date) {
        return format(date).substring(0, 4);
    }

    /**
     * 按默认格式的字符串距离今天的天数
     *
     * @param date 日期字符串
     * @return
     */
    public static int countDays(String date) {
        long t = Calendar.getInstance().getTime().getTime();
        Calendar c = Calendar.getInstance();
        c.setTime(parse(date));
        long t1 = c.getTime().getTime();
        return (int) (t / 1000 - t1 / 1000) / 3600 / 24;
    }

    /**
     * 按用户格式字符串距离今天的天数
     *
     * @param date   日期字符串
     * @param format 日期格式
     * @return
     */
    public static int countDays(String date, String format) {
        long t = Calendar.getInstance().getTime().getTime();
        Calendar c = Calendar.getInstance();
        c.setTime(parse(date, format));
        long t1 = c.getTime().getTime();
        return (int) (t / 1000 - t1 / 1000) / 3600 / 24;
    }


    /**
     * 将某时间段根据interval(分钟)进行分割, 得到间隔区间
     *
     * @param begin    开始时间
     * @param end      截止时间
     * @param interval 间隔(分钟)
     * @return 起止时间, 每个map对象中有begin 和end 两个值
     */
    public static List<Map<String, String>> dateSection(String begin, String end, int intervalMin) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar beginC = Calendar.getInstance();
        beginC.setTime(format.parse(begin)); // 开始时间
        Calendar endC = Calendar.getInstance();
        endC.setTime(format.parse(end)); // 结束时间
        if (beginC.after(endC)) {
            throw new Exception("开始时间必须早于结束时间!");
        }


        List<String> times = new ArrayList<String>();
        Calendar targetC = Calendar.getInstance();
        targetC.setTime(beginC.getTime());
        while (true) {
            times.add(format.format(targetC.getTime()));
            targetC.add(Calendar.MINUTE, intervalMin);
            if (!targetC.after(endC) && !targetC.before(endC)) {
                times.add(format.format(endC.getTime()));
                break;
            }
            if (targetC.after(endC)) {
                times.add(format.format(endC.getTime()));
                break;
            }
        }

        List<Map<String, String>> sectionList = new ArrayList<Map<String, String>>();
        String lastTime = "";
        for (String time : times) {
            if (!"".equals(lastTime)) {
                Map<String, String> section = new HashMap<String, String>();
                //section.put("begin", addSecond(lastTime, 1));
                section.put("begin", lastTime);
                section.put("end", time);
                sectionList.add(section);
            }
            lastTime = time;
        }
        return sectionList;
    }


    /**
     * 指定日期增加秒
     *
     * @param time
     * @param second
     * @return
     */
    public static String addSecond(String time, int second) {
        // 定义一个具体的日期
        Calendar c = Calendar.getInstance();
        c.setTime(stringToDate(time, "yyyy-MM-dd HH:mm:ss"));
        c.add(Calendar.SECOND, second); // 加秒
        // System.out.println(dateToString(c.getTime(), "yyyy-MM-dd HH:mm:ss"));
        return format(c.getTime(), "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 字符串转换为date
     *
     * @param dateStr
     * @param formatStr
     * @return
     */
    public static Date stringToDate(String dateStr, String formatStr) {
        DateFormat dd = new SimpleDateFormat(formatStr);
        Date date = null;
        try {
            date = dd.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }


    /**
     * 日期比较
     * @param date1
     * @param date2
     * @return
     */
    public static int compare_date(String date1, String date2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date dt1 = df.parse(date1);
            Date dt2 = df.parse(date2);
            if (dt1.getTime() > dt2.getTime()) {
                //System.out.println("date1 > date2");
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                //System.out.println("date1 < date2");
                return -1;
            } else {
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println("date1 = date2");
        return 0;
    }



}
