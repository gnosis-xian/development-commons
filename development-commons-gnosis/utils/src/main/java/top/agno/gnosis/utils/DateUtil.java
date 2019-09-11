/*
 * Copyright © 2015-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package top.agno.gnosis.utils;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <pre>
 * 名称: DateUtil
 * 描述: 日期操作工具类
 * </pre>
 *
 * @author yto.net.cn
 * @since 1.0.0
 */
@SuppressWarnings("all")
public final class DateUtil {

    /**
     * 日期类型枚举
     */
    public enum Type {
        /**
         * 年
         */
        Year,
        /**
         * 月
         */
        Month,
        /**
         * 周
         */
        Week,
        /**
         * 天
         */
        Day,
        /**
         * 时
         */
        Hour,
        /**
         * 分
         */
        Minutes,
        /**
         * 秒
         */
        Seconds;
    }

    /**
     * 默认日期格式：yyyy-MM-dd
     */
    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";

    /**
     * 默认时间格式：yyyyMM
     */
    public static final String YYYYMM_DATE_PATTERN = "yyyyMM";

    /**
     * 默认时间格式：yyyyMMdd
     */
    public static final String YYYYMMDD_DATE_PATTERN = "yyyyMMdd";

    /**
     * 默认时间格式：yyyy-MM-dd HH:mm:ss
     */
    public static final String DEFAULT_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 默认时间戳格式，到毫秒 yyyy-MM-dd HH:mm:ss SSS
     */
    public static final String DEFAULT_DATEDETAIL_PATTERN = "yyyy-MM-dd HH:mm:ss SSS";

    /**
     * hujiang时间戳格式
     */
    public static final String HUJIANG_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";

    /**
     * hujiang-pass时间戳格式-旧版（这种格式在SimpleDateFormat下解析2017-02-09T17:57:49.0000000是OK的，在joad下就会比较严格，会出现异常）
     */
    public static final String HUJIANG_DATE_FORMAT_PASS = "yyyy-MM-dd'T'HH:mm:ss";

    /**
     * hujiang-pass时间戳格式-新版（严格的格式）
     */
    public static final String HUJIANG_DATE_FORMAT_PASS_SSSSSSS = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSS";

    public final static String timeFormat2 = "HH:mm:ss";
    public final static String dayFormat = "yyyy/MM/dd";
    public final static String yearFormat = "yyyy";
    public final static String month = "MM";
    public final static String second = "ss";
    public final static String noSecondFormat = "yyyy-MM-dd HH:mm";

    public final static String chineseDtFormat = "yyyy年MM月dd日";
    public final static String chinaTime = "MM时:mm分:ss秒";
    public final static String chinaDate = "yyyy年MM月dd日 HH时:mm分:ss秒";
    public final static String currentTimeZone = "GMT+8";

    public final static String[] weekArr = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};

    /**
     * 1天折算成毫秒数
     */
    public static final long MILLIS_A_DAY = 24 * 3600 * 1000;
    /**
     * 1分钟算成毫秒数
     */
    public static final long MILLIS_A_MINUTES = 60 * 1000;
    /**
     * 1小时算成毫秒数
     */
    public static final long MILLIS_A_HOUR = 60 * 60 * 1000;
    /**
     * 毫秒数
     */
    public static final long MILLIS = 1000;
    /**
     * 一天内最后的一小时
     */
    public static final int LAST_HOUR_IN_DAY = 23;
    /**
     * 一小时内最后的一分钟
     */
    public static final int LAST_MINUTE_IN_HOURE = 59;
    /**
     * 一分钟内最后的一秒
     */
    public static final int LAST_SECOND_IN_MINUTE = 59;
    /**
     * 一周的天数
     */
    public static final int DAYS_A_WEEK = 7;
    /**
     * 一年的月数
     */
    public static final int MONTHS_A_YEAR = 12;
    /**
     * 周一
     */
    public static final int FIRST_OF_WEEK = 1;
    /**
     * 周二
     */
    public static final int SECOND_OF_WEEK = 2;
    /**
     * 周三
     */
    public static final int THIRD_OF_WEEK = 3;
    /**
     * 周四
     */
    public static final int FOURTH_OF_WEEK = 4;
    /**
     * 周五
     */
    public static final int FIFTH_OF_WEEK = 5;
    /**
     * 周六
     */
    public static final int SIXTH_OF_WEEK = 6;
    /**
     * 周日
     */
    public static final int SEVENTH_OF_WEEK = 7;
    /**
     * 一月
     */
    public static final int FITST_MONTH = 1;
    /**
     * 二月
     */
    public static final int SECOND_MONTH = 2;
    /**
     * 三月
     */
    public static final int THIRD_MONTH = 3;
    /**
     * 四月
     */
    public static final int FOURTH_MONTH = 4;
    /**
     * 五月
     */
    public static final int FIFTH_MONTH = 5;
    /**
     * 六月
     */
    public static final int SIXTH_MONTH = 6;
    /**
     * 七月
     */
    public static final int SEVENTH_MONTH = 7;
    /**
     * 八月
     */
    public static final int EIGHTH_MONTH = 8;
    /**
     * 九月
     */
    public static final int NINTH_MONTH = 9;
    /**
     * 十月
     */
    public static final int TENTH_MONTH = 10;
    /**
     * 十一月
     */
    public static final int ELEVENTH_MONTH = 11;
    /**
     * 十二月
     */
    public static final int TWELFTH_MONTH = 12;

    private DateUtil() {
    }

    /**
     * 取得系统当前年份
     *
     * @return 当前系统年份
     */
    public static int currentYear() {
        return DateTime.now().getYear();
    }

    /**
     * 取得当前系统日期
     *
     * @return 当前系统日期
     */
    public static Date currentDate() {
        return DateTime.now().toDate();
    }

    /**
     * 取得系统当前日期，返回默认日期格式的字符串。
     *
     * @param strFormat 格式
     * @return 指定格式的当前系统日志
     */
    public static String nowDate(final String strFormat) {
        return new DateTime().toString(strFormat, Locale.CHINESE);
    }

    /**
     * 取得当前系统时间戳
     *
     * @return 当前系统时间戳
     */
    public static Timestamp currentTimestamp() {
        return new Timestamp(new DateTime().getMillis());
    }

    /**
     * 将日期字符串转换为java.util.Date对象
     *
     * @param dateString 日期字符串
     * @param pattern    日期格式
     * @return date
     */
    public static Date toDate(final String dateString, final String pattern) {
        return DateTime.parse(dateString, DateTimeFormat.forPattern(pattern)).toDate();
    }

    /**
     * 将日期字符串转换为java.util.Date对象，使用默认日期格式
     *
     * @param dateString 日期字符串
     * @return 日期
     */
    public static Date toDate(final String dateString) {
        return LocalDateTime.parse(dateString).toDate();
    }

    /**
     * 将时间字符串转换为java.util.Date对象
     *
     * @param dateString 日期字符串
     * @return 日期
     */
    public static Date toDateTime(final String dateString) {
        return DateTime.parse(dateString, DateTimeFormat.forPattern(DEFAULT_DATETIME_PATTERN)).toDate();
    }

    /**
     * 将java.util.Date对象转换为字符串
     *
     * @param date    日期
     * @param pattern 格式
     * @return 日期字符串
     */
    public static String toDateString(final Date date, final String pattern) {
        return new DateTime(date).toString(pattern, Locale.CHINESE);
    }

    /**
     * 将java.util.Date对象转换为字符串，使用默认日期格式
     *
     * @param date 日期
     * @return 日期字符串
     */
    public static String toDateString(final Date date) {
        return new DateTime(date).toString(DEFAULT_DATE_PATTERN, Locale.CHINESE);
    }

    /**
     * 将java.util.Date对象转换为时间字符串，使用默认日期格式
     *
     * @param date 日期
     * @return 日期字符串
     */
    public static String toDateTimeString(final Date date) {
        return new DateTime(date).toString(DEFAULT_DATETIME_PATTERN, Locale.CHINESE);
    }

    /**
     * 日期相减
     *
     * @param date 日期
     * @param days 天数
     * @return 日期
     */
    public static Date diffDate(final Date date, final Integer days) {
        return new DateTime(date).minusDays(days).toDate();
    }

    /**
     * 返回毫秒
     *
     * @param date 日期
     * @return 返回毫秒
     */
    public static long getMillis(final Date date) {
        return new DateTime(date).getMillis();
    }

    /**
     * 日期相加
     *
     * @param date 日期
     * @param days 天数
     * @return 返回相加后的日期
     */
    public static Date addDate(final Date date, final Integer days) {
        return new DateTime(date).plusDays(days).toDate();
    }

    /**
     * 日期增加年数
     *
     * @param date  日期
     * @param years 年
     * @return 日期
     */
    public static Date addYear(final Date date, final Integer years) {
        return new DateTime(date).plusYears(years).toDate();
    }

    /**
     * 日期增加月数
     *
     * @param date   日期
     * @param months 月数
     * @return 日期
     */
    public static Date addMonth(final Date date, final Integer months) {
        return new DateTime(date).plusMonths(months).toDate();
    }

    /**
     * 日期增加小时
     *
     * @param date  日期
     * @param hours 小时数
     * @return 日期
     */
    public static Date addHours(final Date date, final Integer hours) {
        return new DateTime(date).plusHours(hours).toDate();
    }

    /**
     * 日期增加分钟
     *
     * @param date    日期
     * @param minutes 分钟数
     * @return 日期
     */
    public static Date addMinutes(final Date date, final Integer minutes) {
        return new DateTime(date).plusMinutes(minutes).toDate();
    }

    /**
     * 日期增加秒
     *
     * @param date    日期
     * @param seconds 秒数
     * @return 日期
     */
    public static Date addSeconds(final Date date, final Integer seconds) {
        return new DateTime(date).plusSeconds(seconds).toDate();
    }

    /**
     * 根据季度获得相应的月份
     *
     * @param quarters 季度
     * @return 返回相应的月份
     */
    public static String getMonth(final String quarters) {
        String month;
        int m = Integer.parseInt(quarters);
        m = m * THIRD_MONTH - SECOND_MONTH;
        if (m > 0 && m < TENTH_MONTH) {
            month = "0" + String.valueOf(m);
        } else {
            month = String.valueOf(m);
        }
        return month;
    }

    /**
     * 根据月份获得相应的季度
     *
     * @param month 月份
     * @return 返回相应的季度
     */
    public static String getQuarters(final String month) {
        String quarters = null;
        int m = Integer.parseInt(month);
        if (m == FITST_MONTH || m == SECOND_MONTH || m == THIRD_MONTH) {
            quarters = "1";
        }
        if (m == FOURTH_MONTH || m == FIFTH_MONTH || m == SIXTH_MONTH) {
            quarters = "2";
        }
        if (m == SEVENTH_MONTH || m == EIGHTH_MONTH || m == NINTH_MONTH) {
            quarters = "3";
        }
        if (m == TENTH_MONTH || m == ELEVENTH_MONTH || m == TWELFTH_MONTH) {
            quarters = "4";
        }
        return quarters;
    }

    /**
     * 获取日期所在星期的第一天，这里设置第一天为星期日
     *
     * @param datestr 日期字符串
     * @return 获取日期所在星期的第一天
     */
    public static String getFirstDateOfWeek(final String datestr) {
        DateTime dt = DateTime.parse(datestr);
        return dt.plusDays(-(dt.getDayOfWeek()) + 1).toString(DEFAULT_DATE_PATTERN);
    }

    /**
     * 获取日期所在当年的第几周
     *
     * @param datestr 日期字符串
     * @return 日期所在当年的第几周
     */
    public static int getWeekOfYear(final String datestr) {
        return DateTime.parse(datestr).weekOfWeekyear().get();
    }

    /**
     * 通过日期字符串yyyy-MM-dd HH:mm:ss 获取星期
     *
     * @param datestr 日期字符串
     * @return 星期
     */
    public static String getWeekday(final String datestr) {
        try {
            switch (DateTime.parse(datestr).dayOfWeek().get()) {
                case FIRST_OF_WEEK:
                    return "星期一";
                case SECOND_OF_WEEK:
                    return "星期二";
                case THIRD_OF_WEEK:
                    return "星期三";
                case FOURTH_OF_WEEK:
                    return "星期四";
                case FIFTH_OF_WEEK:
                    return "星期五";
                case SIXTH_OF_WEEK:
                    return "星期六";
                default:
                    return "星期天";
            }
        } catch (final Exception ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    /**
     * 对象转换成日期
     *
     * @param object 对象
     * @return 日期
     */
    public static Date getDate(final Object object) {
        DateTime dt = null;
        if (object instanceof String) {
            dt = DateTime.parse((String) object);
        } else if (object instanceof Date || object instanceof Timestamp) {
            return (Date) object;
        } else if (object instanceof Long) {
            dt = new DateTime((Long) object);
        } else {
            throw new RuntimeException("this object can't to date!");
        }
        return dt.toDate();
    }

    /**
     * 转换成日期
     *
     * @param ticks 时间戳
     * @return 日期
     */
    public static Date fromTimeticks(final Long ticks) {
        return new DateTime(ticks).toDate();
    }

    /**
     * 转换成时间戳
     *
     * @param time 日期
     * @return 时间戳
     */
    public static Long toTimeticks(final Date time) {
        return time.getTime();
    }

    /**
     * 获取UTC时间戳（以秒为单位）
     *
     * @return utc时间戳， 以秒为单位
     */
    public static int getTimestampInSeconds() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        return (int) (cal.getTimeInMillis() / MILLIS);
    }

    /**
     * 获取UTC时间戳（以毫秒为单位）
     *
     * @return utc时间戳， 以毫秒为单位
     */
    public static long getTimestampInMillis() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        return cal.getTimeInMillis();
    }

    /**
     * <b>获取当前时间</b><br>
     * y 年 M 月 d 日 H 24小时制 h 12小时制 m 分 s 秒
     *
     * @param format 日期格式
     * @return String
     */
    public static String getCurrentDate(final String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date());
    }

    /**
     * 获取制定日期的格式化字符串
     *
     * @param date   Date 日期
     * @param format String 格式
     * @return String
     */
    public static String getFormatedDate(final Date date, final String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 判断哪个日期在前 如果日期一在日期二之前，返回true,否则返回false
     *
     * @param date1 日期一
     * @param date2 日期二
     * @return boolean
     */
    public static boolean isBefore(final Date date1, final Date date2) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(date1);

        Calendar c2 = Calendar.getInstance();
        c2.setTime(date2);

        if (c1.before(c2)) {
            return true;
        }
        return false;
    }

    /**
     * 将字符串转换成日期
     *
     * @param date String 日期字符串
     * @return Date
     * @throws ParseException 异常
     */
    public static Date parseDateFromString(final String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.parse(date);
    }

    /**
     * 获取指定日期当月的最后一天
     *
     * @param date 日期
     * @return 当月的最后一天
     */
    public static Date lastDayOfMonth(final Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, 1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return cal.getTime();
    }

    /**
     * 获取指定日期当月的第一天
     *
     * @param date 日期
     * @return 指定日期当月的第一天
     */
    public static Date firstDayOfMonth(final Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return cal.getTime();
    }

    /**
     * 是否是闰年
     *
     * @param year 年份
     * @return boolean
     */
    public static boolean isLeapYear(final int year) {
        GregorianCalendar calendar = new GregorianCalendar();
        return calendar.isLeapYear(year);
    }

    /**
     * 获取指定日期之前或者之后多少天的日期
     *
     * @param day    指定的时间
     * @param offset 日期偏移量，正数表示延后，负数表示天前
     * @return Date
     */
    public static Date getDateByOffset(final Date day, final int offset) {
        Calendar c = Calendar.getInstance();
        c.setTime(day);
        c.add(Calendar.DAY_OF_MONTH, offset);
        return c.getTime();
    }

    /**
     * 获取一天开始时间 如 2014-12-12 00:00:00
     *
     * @return 一天开始时间
     */
    public static Date getDayStart() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取一天结束时间 如 2014-12-12 23:59:59
     *
     * @return 一天结束时间
     */
    public static Date getDayEnd() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, LAST_HOUR_IN_DAY);
        calendar.set(Calendar.MINUTE, LAST_MINUTE_IN_HOURE);
        calendar.set(Calendar.SECOND, LAST_SECOND_IN_MINUTE);
        return calendar.getTime();
    }

    /**
     * 时间分段 比如：2014-12-12 10:00:00 ～ 2014-12-12 14:00:00 分成两段就是 2014-12-12
     * 10：00：00 ～ 2014-12-12 12：00：00 和2014-12-12 12：00：00 ～ 2014-12-12 14：00：00
     *
     * @param start  起始日期
     * @param end    结束日期
     * @param pieces 分成几段
     * @return 分段数组
     */
    public static Date[] getDatePieces(final Date start, final Date end, final int pieces) {
        Long sl = start.getTime();
        Long el = end.getTime();

        Long diff = el - sl;

        Long segment = diff / pieces;

        Date[] dateArray = new Date[pieces + 1];

        for (int i = 1; i <= pieces + 1; i++) {
            dateArray[i - 1] = new Date(sl + (i - 1) * segment);
        }

        // 校正最后结束日期的误差，可能会出现偏差，比如14:00:00 ,会变成13:59:59之类的
        dateArray[pieces] = end;

        return dateArray;
    }

    /**
     * 获取某个日期的当月第一天
     *
     * @param date 日期
     * @return 指定日期的当月第一天
     */
    public static Date getFirstDayOfMonth(final Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return cal.getTime();
    }

    /**
     * 获取某个日期的当月最后一天
     *
     * @param date 日期
     * @return 指定日期的当月最后一天
     */
    public static Date getLastDayOfMonth(final Date date) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 1);
        cal.set(Calendar.DAY_OF_MONTH, 0);
        return cal.getTime();
    }

    /**
     * 获取某个日期的当月第一天
     *
     * @param year  年份
     * @param month 月份
     * @return 指定日期的当月第一天
     */
    public static Date getFirstDayOfMonth(final int year, final int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return cal.getTime();
    }

    /**
     * 获取某个日期的当月最后一天
     *
     * @param year  年份
     * @param month 月份
     * @return 指定日期的当月最后一天
     */
    public static Date getLastDayOfMonth(final int year, final int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, 0);
        return cal.getTime();
    }

    /**
     * 获取两个日期的时间差，可以指定年，月，周，日，时，分，秒
     *
     * @param date1 第一个日期
     * @param date2 第二个日期<font color="red">此日期必须在date1之后</font>
     * @param type  DateUtils.Type.X的枚举类型
     * @return long值
     * @throws Exception 异常
     */
    public static long getDiff(final Date date1, final Date date2, final Type type) throws Exception {
        if (!isBefore(date1, date2)) {
            throw new Exception("第二个日期必须在第一个日期之后");
        }
        long d = Math.abs(date1.getTime() - date2.getTime());
        switch (type) {
            case Year:
                return getDiffYear(date1, date2);
            case Month:
                return getDiffMonth(date1, date2, getDiff(date1, date2, Type.Year));
            case Week:
                return getDiff(date1, date2, Type.Day) / DAYS_A_WEEK;
            case Day:
                return (int) ((date2.getTime() - date1.getTime()) / MILLIS_A_DAY);
            case Hour:
                return (int) ((date2.getTime() - date1.getTime()) / MILLIS_A_HOUR);
            case Minutes:
                return (int) ((date2.getTime() - date1.getTime()) / MILLIS_A_MINUTES);
            case Seconds:
                return (int) ((date2.getTime() - date1.getTime()) / MILLIS);
            default:
                throw new Exception("请指定要获取的时间差的类型：年，月，天，周，时，分，秒");
        }
    }

    /**
     * 获取年分差
     *
     * @param date1 日期1
     * @param date2 日期2
     * @return 年分差
     */
    public static long getDiffYear(final Date date1, final Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();

        cal1.setTime(date1);
        int year1 = cal1.get(Calendar.YEAR);
        int month1 = cal1.get(Calendar.MONTH);
        int day1 = cal1.get(Calendar.DAY_OF_MONTH);
        int hour1 = cal1.get(Calendar.HOUR_OF_DAY);
        int minute1 = cal1.get(Calendar.MINUTE);
        int second1 = cal1.get(Calendar.SECOND);

        cal2.setTime(date2);
        int year2 = cal2.get(Calendar.YEAR);
        int month2 = cal2.get(Calendar.MONTH);
        int day2 = cal2.get(Calendar.DAY_OF_MONTH);
        int hour2 = cal2.get(Calendar.HOUR_OF_DAY);
        int minute2 = cal2.get(Calendar.MINUTE);
        int second2 = cal2.get(Calendar.SECOND);

        int yd = year2 - year1;

        if (month1 > month2) {
            yd -= 1;
        } else {
            if (day1 > day2) {
                yd -= 1;
            } else {
                if (hour1 > hour2) {
                    yd -= 1;
                } else {
                    if (minute1 > minute2) {
                        yd -= 1;
                    } else {
                        if (second1 > second2) {
                            yd -= 1;
                        }
                    }
                }
            }
        }
        return (long) yd;
    }

    /**
     * 获取月分差
     *
     * @param date1 日期1
     * @param date2 日期2
     * @param year  年份差
     * @return 月分差
     */
    public static long getDiffMonth(final Date date1, final Date date2, final long year) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();

        cal1.setTime(date1);
        int month1 = cal1.get(Calendar.MONTH);
        int day1 = cal1.get(Calendar.DAY_OF_MONTH);
        int hour1 = cal1.get(Calendar.HOUR_OF_DAY);
        int minute1 = cal1.get(Calendar.MINUTE);
        int second1 = cal1.get(Calendar.SECOND);

        cal2.setTime(date2);
        int month2 = cal2.get(Calendar.MONTH);
        int day2 = cal2.get(Calendar.DAY_OF_MONTH);
        int hour2 = cal2.get(Calendar.HOUR_OF_DAY);
        int minute2 = cal2.get(Calendar.MINUTE);
        int second2 = cal2.get(Calendar.SECOND);

        int md = (month2 + MONTHS_A_YEAR) - month1;

        if (day1 > day2) {
            md -= 1;
        } else {
            if (hour1 > hour2) {
                md -= 1;
            } else {
                if (minute1 > minute2) {
                    md -= 1;
                } else {
                    if (second1 > second2) {
                        md -= 1;
                    }
                }
            }
        }
        return (long) md + year * MONTHS_A_YEAR;
    }
}
