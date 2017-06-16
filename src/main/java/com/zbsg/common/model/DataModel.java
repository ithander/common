package com.zbsg.common.model;

import java.util.Map;

/**
 * 数据模型，用于封装返回的数据
 * @author zyt
 *
 */
public class DataModel {

	private int code;
	private String msg;
	private Map<String,Object> data;
	
	/**
	 * 用于数据集
	 * @param code
	 * @param msg
	 * @param data
	 */
	public DataModel(int code,String msg,Map<String,Object> data){
		this.code=code;
		this.msg=msg;
		this.data=data;
	}
	
	/**
	 * 
	 * @param msg 枚举类型
	 */
	public DataModel(Msg msg){
		this.code=msg.getValue();
		this.msg=msg.getMsg();
	}
	
	/**
	 * 
	 * @param msg 枚举类型
	 * @param data map
	 */
	public DataModel(Msg msg,Map<String,Object> data){
		this.code=msg.getValue();
		this.msg=msg.getMsg();
		this.data=data;
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

}
