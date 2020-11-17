package com.xwkj.ygjces.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 *
 * @author 张永辉
 */
public class DateUtil {


    public final static String YYYY_MM_DD = "yyyy-MM-dd";

    public final static String YYYY_MM = "yyyy-MM";

    public final static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    public final static String HH_MM = "HH:mm";


    /**
     * 返回两个日期之间相差的月数
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static Integer getDifMonth(Date startDate, Date endDate) {
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        start.setTime(startDate);
        end.setTime(endDate);
        int result = end.get(Calendar.MONTH) - start.get(Calendar.MONTH);
        int month = (end.get(Calendar.YEAR) - start.get(Calendar.YEAR)) * 12;
        return Math.abs(month + result);
    }

    /**
     * 返回日期中的月份
     *
     * @param date
     * @return
     */
    public static Integer getMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH);
        return month + 1;
    }

    /**
     * String类型转换为Date类型
     *
     * @param strTime
     * @param formatType
     * @return
     */
    public static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }

    /**
     * Date类型转换为String类型
     *
     * @param data
     * @param formatType
     * @return
     */
    public static String dateToString(Date data, String formatType) {
        return new SimpleDateFormat(formatType).format(data);
    }

    /**
     * 获取当年的第一天
     *
     * @param
     * @return
     */
    public static Date getCurrYearFirst() {
        Calendar currCal = Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);
        return getYearFirst(currentYear);
    }

    /**
     * 获取某年第一天日期
     *
     * @param year 年份
     * @return Date
     */
    public static Date getYearFirst(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        Date currYearFirst = calendar.getTime();
        return currYearFirst;
    }

    /**
     * 获取两个日期之间的间隔小时数
     * @param start
     * @param end
     * @return
     */
    public static Integer getBetweenHours(Date start , Date end){
        long startTime = start.getTime();
        long endTime = end.getTime();

        int between = (int)(Math.abs(endTime-startTime)/(3600*1000));
        return between;
    }

    /**
     * 获取两个日期之间的间隔天数
     * @param start
     * @param end
     * @return
     */
    public static Integer getBetweenDay(Date start , Date end){
        long startTime = start.getTime();
        long endTime = end.getTime();

        int between = (int)(Math.abs(endTime-startTime)/(3600*1000*24));
        return between;
    }
}
