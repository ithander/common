package com.zbsg.common.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils {

	public final static String YYYY_MM="yyyy-MM"; 
	public final static String YYYY_MM_DD="yyyy-MM-dd"; 
	public final static String YYYY_MM_DD_HH_mm_ss="yyyy-MM-dd HH:mm:ss"; 
	public final static String YYYY_MM_DD_HH_mm_ss_SS="yyyy-MM-dd HH:mm:ss SS"; 
	public final static String YYYYMMDDHHmmssSS="yyyyMMddHHmmssSS"; 
	public final static SimpleDateFormat sdf=new SimpleDateFormat();
	
	public final static Calendar car=Calendar.getInstance();
	
	static {//设置时区为本地默认时区
		sdf.setTimeZone(TimeZone.getDefault());
		car.setTimeZone(TimeZone.getDefault());
	}
	
	/**
	 * 得到当前时间对象
	 * @return
	 */
	public static Date now(){
		return new Date();
	}
	
	/**
	 * 昨天的日期对象
	 * @return
	 */
	public static Date lastDay(){
		return agoDay(1);
	}
	
	/**
	 * 获取前指定天的时间对象
	 * @param daynum
	 * @return
	 */
	public static Date agoDay(int daynum){
        car.clear();
        car.add(Calendar.DATE, daynum);
        long subTime=car.getTimeInMillis();
        car.setTime(now());
        long nowTime=car.getTimeInMillis();
        car.setTimeInMillis(nowTime-subTime);
		return car.getTime();
	}
	
	/**
	 * 获取前指定天的时间对象
	 * @param daynum
	 * @return
	 */
	public static String agoDayStr(int daynum,String format){
        car.clear();
        car.add(Calendar.DATE, daynum);
        long subTime=car.getTimeInMillis();
        car.setTime(now());
        long nowTime=car.getTimeInMillis();
        car.setTimeInMillis(nowTime-subTime);
		return dateToStr(car.getTime(),format);
	}
	
	/**
	 * 获得指写天数后的日期对象
	 * @param daynum
	 * @return
	 */
	public static Date afterDay(int daynum){
		car.clear();
	    car.add(Calendar.DATE, daynum);
	    long subTime=car.getTimeInMillis();
	    car.setTime(now());
	    long nowTime=car.getTimeInMillis();
	    car.setTimeInMillis(nowTime+subTime);
	    return car.getTime();
	}
	
	/**
	 * 获得指定月数前的日期对象
	 * @param daynum
	 * @return
	 */
	public static Date agoMonth(int monthNum){
		car.clear();
	    car.add(Calendar.MONTH, monthNum);
	    long subTime=car.getTimeInMillis();
	    car.setTime(now());
	    long nowTime=car.getTimeInMillis();
	    car.setTimeInMillis(nowTime-subTime);
	    return car.getTime();
	}
	
	/**
	 * 获得指定月数后的日期对象
	 * @param daynum
	 * @return
	 */
	public static Date afterMonth(int monthNum){
		car.clear();
	    car.add(Calendar.MONTH, monthNum);
	    long subTime=car.getTimeInMillis();
	    car.setTime(now());
	    long nowTime=car.getTimeInMillis();
	    car.setTimeInMillis(nowTime+subTime);
	    return car.getTime();
	}
	
	/**
	 * 获得指定日期，指定月数后的日期对象
	 * @param daynum
	 * @return
	 */
	public static Date afterMonth(Date date,int monthNum){
		car.clear();
	    car.add(Calendar.MONTH, monthNum);
	    long subTime=car.getTimeInMillis();
	    car.setTime(date);
	    long nowTime=car.getTimeInMillis();
	    car.setTimeInMillis(nowTime+subTime);
	    return car.getTime();
	}
	
	/**
	 * 返回YYYY-MM 为格式的时间字符
	 * @return String
	 */
	public static String getSysmonth(){
		sdf.applyPattern(YYYY_MM);
		return sdf.format(now());
	}
	
	public static String getSysmonth(Date date){
		sdf.applyPattern(YYYY_MM);
		return sdf.format(date);
	}
	/**
	 * 返回YYYY-MM-DD 为格式的时间字符
	 * @return String
	 */
	public static String getSysdate(){
		sdf.applyPattern(YYYY_MM_DD);
		return sdf.format(now());
	}
	
	public static String getSysdate(Date date){
		sdf.applyPattern(YYYY_MM_DD);
		return sdf.format(date);
	}
	/**
	 * 返回YYYY-MM-DD HH:mm:ss 为格式的时间字符
	 * @return String
	 */
	public static String getSystime(){
		sdf.applyPattern(YYYY_MM_DD_HH_mm_ss);
		return sdf.format(now());
	}
	
	/**
	 * 定制格式化日期字符串
	 * @param date
	 * @param format
	 * @return
	 */
	public static String dateToStr(Date date,String format){
		sdf.applyPattern(format);
		return sdf.format(date);
	}
	
	/**
	 * 转换字符串为date对象
	 * @param dateStr
	 * @param format
	 * @return
	 */
	public static Date parseDate(String dateStr,String format){
		try {
			sdf.applyPattern(format);
			return sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	 /**  
     * 计算两个日期之间相差的天数  
     * @param smdate 较小的时间 
     * @param bdate  较大的时间 
     * @return 相差天数 
     * @throws ParseException  
     */    
    public static int daysBetween(Date smdate,Date bdate) throws ParseException    
    {    
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
        smdate=sdf.parse(sdf.format(smdate));  
        bdate=sdf.parse(sdf.format(bdate));  
        Calendar cal = Calendar.getInstance();    
        cal.setTime(smdate);    
        long time1 = cal.getTimeInMillis();                 
        cal.setTime(bdate);    
        long time2 = cal.getTimeInMillis();         
        long between_days=(time2-time1)/(1000*3600*24);  
            
       return Integer.parseInt(String.valueOf(between_days));           
    }    
      
	/** 
	*字符串的日期格式的计算 
	*/  
    public static int daysBetween(String smdate,String bdate) throws ParseException{  
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
        Calendar cal = Calendar.getInstance();    
        cal.setTime(sdf.parse(smdate));    
        long time1 = cal.getTimeInMillis();                 
        cal.setTime(sdf.parse(bdate));    
        long time2 = cal.getTimeInMillis();         
        long between_days=(time2-time1)/(1000*3600*24);  
            
       return Integer.parseInt(String.valueOf(between_days));     
    }  
  
}
