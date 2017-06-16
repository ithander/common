package com.zbsg.common.model;


import java.util.HashMap;
import java.util.Iterator;

public class ResultValues extends HashMap<String,Object>{
	
	 private static final long serialVersionUID=1L;
	    
	    /**
	     * 获得字符串形式的值
	     * @param key
	     * @return
	     */
	    public String getStr(String key){
	        if(null!=get(key)){
	            return String.valueOf(get(key));
	        }
	        return null;
	    }
	    
	    public int getInt(String key){
	    	if(isNotEmpty(key)){
	    		return Integer.parseInt(String.valueOf(get(key)));
	    	}
	    	return -1;
	    }
	    
	    public long getLong(String key){
	    	if(isNotEmpty(key)){
	    		return Long.parseLong(String.valueOf(get(key)));
	    	}
	    	return -1;
	    }
	    
	    public <T>T getBean(Class<T> cls){
	    	T t=null;
	    	try {
				//t=cls.newInstance();
				//BeanUtils.copyProperties(t, this);
			} catch (Exception e) {
				e.printStackTrace();
			}
	        
	    	return t;
	    }
	    
	    public boolean isNotEmpty(String key){
	        Object obj=get(key);
	        if(null!=obj&&!"".equals(String.valueOf(obj).trim())){
	             return true;
	        }
	        return false;
	    }
	    
	    /**
	     * 把所有Key去掉下划线
	     */
	    public void formatKey(){
	        ResultValues result=null;
	        if(size()>0){
	            result=new ResultValues();
	            Iterator<String> it=keySet().iterator();
	            String key=null;
	            while(it.hasNext()){
	                key=it.next();
	                result.put(convertToPropertyName(key),get(key));
	            }
	            clear();
	            putAll(result);
	        }
	    }
	    
	    /**
	     * 去掉下划线
	     * @param name
	     * @return
	     */
	    public String convertToPropertyName(String name){
	        name=name.toLowerCase();
	        String[] str=name.split("\\_");
	        int size=str.length;
	        for(int i=0;i<size;i++){
	            if(0==i){
	                name=str[i];
	            }else{
	                name=name+upperFirstCase(str[i]);
	            }
	        }
	        return name;
	    }
	    
	    /**
	     * 首字母转大写
	     * @param str
	     * @return
	     */
	    private String upperFirstCase(String str){
	        if(null==str||"".equals(str)){
	            return "";
	        }
	        return str.substring(0,1).toUpperCase()+str.substring(1);
	    }
	    
}
