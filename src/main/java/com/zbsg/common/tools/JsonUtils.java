package com.zbsg.common.tools;

import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class JsonUtils {

	private static Gson gson=new Gson();
	
	/**
	 * 把obj转为json字符串
	 * @param obj
	 * @return
	 */
	public static String toJsonStr(Object obj){
		return gson.toJson(obj);
	}
	
	/**
	 * 解析json字符串到实体对象中
	 * @param json
	 * @param cls
	 * @return
	 */
	public static <T>T parseJson(String json,Class<T> cls){
		return gson.fromJson(json, cls);
	}
	
	/**
	 * 批量转换json字符串为list实体类
	 * @param json
	 * @param cls
	 * @return
	 */
	public static <T>List<T> parseJsons(String json,Class<T> cls){
		return gson.fromJson(json, new TypeToken<List<T>>() {}.getType());
	}
	
	public static Map<String,Object> parseJsonToMap(String json){
		return gson.fromJson(json, new TypeToken<Map<String,Object>>() {}.getType());
	}
	
	public static List<Map<String,Object>> parseJsonToMaps(String json){
		return gson.fromJson(json, new TypeToken<List<Map<String,Object>>>() {}.getType());
	}
	
}
