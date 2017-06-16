package com.zbsg.common.tools;

import java.util.Date;
import java.util.UUID;

public class StrUtils {

	public static boolean isNumber(String value){
		return value.matches("\\d+");
	}
	
	public static String getUUID(){
		return UUID.randomUUID().toString();
	}
	
	/**
	 * 以当前日期时间为依据生成唯一性ID
	 * @return
	 */
	public static String getID(){
		return DateUtils.dateToStr(new Date(), DateUtils.YYYYMMDDHHmmssSS);
	}
	
	/**
	 * 判断是否为空
	 * @param value
	 * @return
	 */
	public static boolean isNotBlank(String value){
		if(null!=value&&value.trim().length()>0){
			return true;
		}
		return false;
	}
	
	public static String join(String[] strs,String space){
		StringBuilder sber=new StringBuilder();
		int i=0;
		for(String s:strs){
			if(0<i){
				sber.append(space);
			}else{
				i++;
			}
			sber.append(s);
		}
		return sber.toString();
	}
	
	public static String trim(String str,String mark){
		if(str.startsWith(mark)){
			str=str.replaceFirst(mark, "");
		}
		if(str.endsWith(mark)){
			str=str.substring(0, str.lastIndexOf(mark));
		}
		
		return str;
	}
	
	/**
	 * 去掉下划线,并转为小写
	 * @param tname
	 * @return
	 */
	public static String dropUnderline(String tname){
		StringBuffer columnName=new StringBuffer();
		tname=tname.toLowerCase();
		
		char[] tnames=tname.toCharArray();
		boolean _show=false;
		for(int i=0;i<tnames.length;i++){
			if("_".equals(String.valueOf(tnames[i]))){
				_show=true;
				continue;
			}
			if(_show){
				columnName.append(String.valueOf(Character.toUpperCase(tnames[i])));
				_show=false;
			}else{
				columnName.append(String.valueOf(tnames[i]));
			}
		}
		return columnName.toString();
	}
	
	/**
	 * 增加下划线,并转为大写
	 * @param tname
	 * @return
	 */
	public static String addUnderline(String tname){
		String columnName="";
		
		char[] tnames=tname.toCharArray();
		boolean _show=false;
		for(int i=0;i<tnames.length;i++){
			if(Character.isUpperCase(tnames[i])&&i>0){
				_show=true;
			}
			if(_show){
				columnName+="_"+tnames[i];
				_show=false;
			}else{
				columnName+=tnames[i];
			}
		}
		return columnName.toLowerCase();
	}
	
	/**
	 * 首字符大写
	 * @param s
	 * @return
	 */
	public static String upperFirst(CharSequence s) {
        if (null == s){
            return null;
        }
        int len = s.length();
        if (len == 0){
            return "";
        }
        char c = s.charAt(0);
        if (!Character.isLowerCase(c)){
        	return s.toString();
        }
        return new StringBuilder(len).append(Character.toUpperCase(c)).append(s.subSequence(1, len)).toString();
    }
}
