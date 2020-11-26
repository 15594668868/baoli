package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 */
public class DateUtil{
    /**
     * 传入格式获取日期
     * yyyy-MM-dd HH:mm:ss
     * yyyyMMddHHmmssSSS
     * @return 日期
     */
    public static String getDate(Date date, String pattern){
        SimpleDateFormat formatter = new SimpleDateFormat (pattern);
        return formatter.format(date);
    }

    /**
     * 获取当前时间戳
     * @return
     */
    public static String getCurrentTimeMillis(){
       return String.valueOf(System.currentTimeMillis());
    }
    
    /**
     * 日期格式转换从yyyy-MM-dd HH:mm:ss到 yyyyMMddHHmmss
     * @param strDateTime
     * @return
     */
     public static String lengthConvesion(String strDateTime){
	      String strYMDHMS = "";
	      if(strDateTime == null){
	      	return "" ;
	      }  
	      if(strDateTime.length() == 10) {
	      	strYMDHMS = " 23:59:59";
	      }
	      return  strDateTime+strYMDHMS; 
	   }

    /**
     * 得到系统当前日期是星期几，格式 "星期一"
     * @return
     */
    public static String getWeek(){
    	String strCurrentWeek = "";
    	Date currentWeek = new Date();
    	SimpleDateFormat formatter = new SimpleDateFormat ("E");
        strCurrentWeek= formatter.format(currentWeek);
        return strCurrentWeek; 
    }
    
    /**
     * 得到任意输入的一个日期的星期数，传入格式"2020-06-09" 返回格式 "星期一"
     * @param strDate
     * @return
     */
    public static String getDateToWeek(String strDate){
        String strWeek="";
        int iYear = Integer.parseInt(strDate.substring(0, 4));
        int iMonth = Integer.parseInt(strDate.substring(5, 7));
        int iDay = Integer.parseInt(strDate.substring(8, 10));
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, iYear);
        cal.set(Calendar.MONTH, iMonth-1);
        cal.set(Calendar.DAY_OF_MONTH, iDay);
        Date currentDate = cal.getTime();        
        SimpleDateFormat formatter = new SimpleDateFormat("E");
        strWeek= formatter.format(currentDate);                
        return strWeek;
    }


    /**
     * 传入正数或者负数天数，及时间格式，返回对应的时间，例如传入1返回当前日期后一天，传入-1返回昨天。disposeDate(0, "yyyy-MM-dd HH:mm:ss")
     * @param day
     * @param pattern
     * @return
     */
    public static String disposeDate(int day, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, day);
        return format.format(c.getTime());
    }

    /**
     * 传入类似："2020-06-02 08:58:19"，返回传入时间时间戳
     * @param date
     * @return
     */
    public static long getTime(String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date newDate = simpleDateFormat.parse(date);
            return newDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 因有较多查询条件获取时间戳格式，所以写此方法，用于获取时间戳的数组
     * @param startDay 开始时间传入的天，根据正数或负数在当前时间进行增加或减少
     * @param endDay 结束时间传入的天，根据正数或负数在当前时间进行增加或减少
     * @return
     */
    public static Long[] getTimeList(int startDay, int endDay) {
        //获取到当前时间往前推1天的日期和时间戳
        String startDate = disposeDate(startDay, "yyyy-MM-dd HH:mm:ss");
        Long startTime = getTime(startDate);
        //获取到当前时间的日期和时间戳
        String endDate = disposeDate(endDay, "yyyy-MM-dd HH:mm:ss");
        Long endTime = getTime(endDate);
        return new Long[] {startTime, endTime};
    }
}

