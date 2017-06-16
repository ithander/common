/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zbsg.common.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.zbsg.common.tools.DateUtils;
import com.zbsg.common.tools.JsonUtils;
import com.zbsg.common.tools.StrUtils;


/**
 * 用于参数传递
 * @author zyt
 */
public class ActionValues extends HashMap<String,Object>{
    
    private static final long serialVersionUID=1L;
    
    private boolean page=false;//默认不分页
    
    public final static String PAGE_NOW="pageNow";//当前页号 键名
    public final static String PAGE_NUM="pageNum";//总页数 键名
    public final static String PAGE_SIZE="pageSize";//每页记录数 键名
    
    public final static String FROM_ROW="fromRow";//从该行查询记录
    public final static String TO_ROW="toRow";//查询到该行
    
    public final static String TOTAL="total";//总记录数
    public final static int DEFAULT_PAGE_SIZE=30;//默认每页记录数
    public final static String JSON_DATA_KEY="jsonData";//默认json数据键名
    
    public final static String ORDER_BY="orderby";//排序字段key,多个字段值用逗号分隔
    public final static String ORDER_SORT="sort";//升序 desc,降序 asc
    public ActionValues(){
    	super();
    }
    
    /**
     * 高定容量
     * @param size
     */
    public ActionValues(int size){
    	super(size);
    }
    
    public ActionValues(Map<String,Object> values){
    	super(null!=values?values.size():16);
        if(null!=values){
        	this.putAll(values);
        }
    }
    
   	public ActionValues(HttpServletRequest request){
        super(sizeParams(request)+7);  		
   		String name=null;
   		Enumeration<String> paramNames=request.getParameterNames();
   		Enumeration<String> attrNames=request.getAttributeNames();
   		
   		while(paramNames.hasMoreElements()){
   			name=paramNames.nextElement();
   			if(name.endsWith("FILTERED")||name.startsWith("org.springframework")||name.equals("SpringEncodingFilter.FILTERED")||name.startsWith("org.apache.shiro")||name.equals("characterEncodingFilter.FILTERED")||name.equals("shiroFilter.FILTERED")||name.equals("authc.FILTERED")){//过滤无效参数
   				continue;
   			}
   			if(request.getParameterValues(name).length>1){
   				put(name, request.getParameterValues(name));
   			}else{
   				//过滤空值
   				if(null!=request.getParameter(name)&&!"".equals(request.getParameter(name).trim())){
   					put(name, request.getParameter(name));	
   				}
   			}
   		}
   		
   		while(attrNames.hasMoreElements()){
   			name=attrNames.nextElement();
   			if(name.endsWith("FILTERED")||name.startsWith("org.springframework")||name.equals("SpringEncodingFilter.FILTERED")||name.startsWith("org.apache.shiro")||name.equals("characterEncodingFilter.FILTERED")||name.equals("shiroFilter.FILTERED")||name.equals("authc.FILTERED")){//过滤无效参数
   				continue;
   			}
   			if(null!=request.getAttribute(name)&&!"".equals(String.valueOf(request.getAttribute(name)).trim())){
   				put(name,request.getAttribute(name));	
   			}
   		}
   		
   	}
    
   	/**
   	 * 加入数据
   	 * @param key
   	 * @param obj
   	 */
    public void add(String key,Object obj){
    	this.put(key, obj);
    }
    
    /**
     * 获得字符串数据
     * @param key
     * @return
     */
    public String getStr(String key){
        if(null!=get(key)){
            return String.valueOf(get(key));
        }
        return null;
    }
    
    /**
     * 获得整型数字
     * @param key
     * @return
     */
    public int getInt(String key){
    	int r=0;
    	try{
	    	if(isNotEmpty(key)){
	    		String value=String.valueOf(get(key));
	    		if(value.contains(".")){
	    			value=StrUtils.trim(value, ".0");
	    		}
	    		r=Integer.parseInt(value);
	    	}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return r;
    }
    
    /**
     * 获取长整型数字值
     * @param key
     * @return
     */
    public long getLong(String key){
    	if(isNotEmpty(key)){
    		return Long.parseLong(String.valueOf(get(key)));
    	}
    	return 0;
    }
    
    /**
     * 获取单精度数字值
     * @param key
     * @return
     */
    public float getFloat(String key){
    	if(isNotEmpty(key)){
    		return Float.parseFloat(String.valueOf(get(key)));
    	}
    	return 0.0F;
    }
    
    /**
     * 获取双精度数字值 常用于金钱计算
     * @param key
     * @return
     */
    public double getDouble(String key){
    	if(isNotEmpty(key)){
    		return Double.parseDouble(String.valueOf(get(key)));
    	}
    	return 0.0;
    }
    
    /**
     * 获取大数字
     * @param key
     * @return
     */
    public BigDecimal getBigDecimal(String key){
    	if(isNotEmpty(key)){
    		return new BigDecimal(getStr(key));
    	}
    	return BigDecimal.ZERO;
    }
    
    /**
     * 得以日期类型
     * @param key
     * @return
     */
    public java.util.Date getDate(String key){
    	if(isNotEmpty(key)){
    		return DateUtils.parseDate(getStr(key), DateUtils.YYYY_MM_DD_HH_mm_ss);
    	}
    	return null;
    }
    
    /**
     * 是否为空
     * @param key
     * @return
     */
    public boolean isNotEmpty(String key){
        Object obj=get(key);
        if(null!=obj&&!"".equals(String.valueOf(obj).trim())){
             return true;
        }
        return false;
    }
    
    /**
     * 判断是否存在
     * @param key
     * @return
     */
    public boolean isNotNull(String key){
        if(null!=get(key)){
            return true;
        }
        return false;
    }

    /**
     * 过滤空字符串,把所有key对应的值为空的或""的，移除掉。
     */
    public void filterEmpty(){
    	Iterator<String> keys=keySet().iterator();
    	String key=null;
    	List<String> keys_str=new ArrayList<String>();
    	while(keys.hasNext()){
    		key=keys.next();
    		if(!isNotEmpty(key)){
    			keys_str.add(key);
    		}
    	}
    	
    	for(String k:keys_str){
    		remove(k);
    	}
    	
    }
    
    public void setTotal(long total){
         add(ActionValues.TOTAL,total);
         int pageSize=getInt(ActionValues.PAGE_SIZE)==-1?30:getInt(ActionValues.PAGE_SIZE);
         add(ActionValues.PAGE_NUM,(total/pageSize)+(total%pageSize==0?0:1));
    }
    
    /**
     * 判断是否分页 true分页   false不分页
     * @return
     */
    public boolean isPage(){
    	return page;
    }
    
    /**
     * 打开分页功能，如果开启分页
     */
	public ActionValues openPage() {
		page=true;
		openPage(getInt(PAGE_NOW),getInt(PAGE_SIZE)==0?DEFAULT_PAGE_SIZE:getInt(PAGE_SIZE));
		return this;
	}
	
	public void closePage(){
		page=false;
	}
	
	/**
     * 打开分页功能，并设置当前页和每页数据条数
     */
	public ActionValues openPage(int pageNow,int pageSize) {
		page=true;
		add(PAGE_NOW, pageNow<=0?1:pageNow);
		add(PAGE_SIZE,pageSize<=0?30:pageSize);
		add(FROM_ROW,pageNow<=1?0:(pageNow-1)*pageSize);
		add(TO_ROW,pageNow<=1?pageSize:pageNow*pageSize);
		return this;
	}
	
	/**
     * 关闭分页功能
     */
	public void offPage(){
		page=false;
	}
	
	/**
	 * 计算Enumeration长度
	 * @param params
	 * @return
	 */
	private static int sizeParams(HttpServletRequest request){
		Enumeration<String> paramNames=request.getParameterNames();
   		Enumeration<String> attrNames=request.getAttributeNames();
		int size=0;
		if(null!=paramNames){
			while(paramNames.hasMoreElements()){
				paramNames.nextElement();
				size++;
			}
		}
		if(null!=attrNames){
			while(attrNames.hasMoreElements()){
				attrNames.nextElement();
				size++;
			}
		}
		return size;
	}
	
	/**
	 * 是否需要排序
	 * @return
	 */
	public boolean isSort(){
        return isNotEmpty(ORDER_BY);
	}
	
	/**
	 * 排序字段
	 * @return
	 */
	public String orderBy(){
		return getStr(ORDER_BY);
	}
	
	/**
	 * 升降序
	 * @return
	 */
	public String sort(){
		return getStr(ORDER_SORT);
	}
	
	@Override
	public String toString() {
		if(0==size()){
			return "{}";
		}
		return JsonUtils.toJsonStr(this);
	}
}
