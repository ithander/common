package com.zbsg.common.model;

public enum Msg {

	/**
	 * 通用提示信息
	 */
	Operation_Error(-1,"操作失败或发生异常!"),
	
	Operation_Success(0,"操作成功");
	
	/**
	 * 快弟员  快递单 状态
	 */
	
	private int code;
	private String msg;
	
	private Msg(int c,String m){
		this.code=c;
		this.msg=m;
	}
	
	public int getValue(){
		return code;
	}
	
	public String getMsg(){
		return msg;
	}

	
	@Override
	public String toString() {
		return this.msg;
	}
	
}
