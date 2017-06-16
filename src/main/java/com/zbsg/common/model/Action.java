package com.zbsg.common.model;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.zbsg.common.tools.JsonUtils;


public class Action {

	private final static Logger logger = Logger.getLogger(Action.class);
	/**
	 * 初始化Action
	 * @param request
	 * @param response
	 * @param model
	 * @param fils
	 */
	@ModelAttribute
	public void init(HttpServletRequest request,HttpServletResponse response,Model model){
		response.setContentType("text/html;charset=UTF-8");
	}
	
	/**
	 * 输出字符串
	 * @param msg
	 */
	protected void print(Object msg,HttpServletResponse response){
		PrintWriter out=null;
		try{
			out=response.getWriter();
			out.print(msg);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			out.close();
		}
	}
	
	/**
	 * 以Json格式输出参数
	 * @param obj
	 */
	protected void printJson(Object obj,HttpServletResponse response){
		PrintWriter out=null;
		try{
			out=response.getWriter();
			out.print(JsonUtils.toJsonStr(obj));
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			out.close();
		}
	}
	
	/**
	 * 以json格式解析values中相关数据 为Object
	 * @param model
	 * @param values
	 * @return
	 */
	public <T>T parseJson(Class<T> model,ActionValues values){
		if(values.isNotEmpty(ActionValues.JSON_DATA_KEY)){
			return JsonUtils.parseJson(values.getStr(ActionValues.JSON_DATA_KEY), model);	
		}
		return null;
	}
	
	/**
	 * 以json格式解析values中相关数据 为List
	 * @param model
	 * @param values
	 * @return
	 */
	public <T>List<T> parseJsons(Class<T> model,ActionValues values){
		if(values.isNotEmpty(ActionValues.JSON_DATA_KEY)){
			return JsonUtils.parseJsons(values.getStr(ActionValues.JSON_DATA_KEY), model);	
		}
		return null;
	}
	
	public void doneOK(HttpServletResponse response){
		printJson(new DataModel(Msg.Operation_Success,null),response);
	}
	
	/**
	 * 成功返回
	 * @param rv
	 */
	public void doneOK(ResultValues rv){
		done(Msg.Operation_Success,null);
	}
	
	/**
	 * 无数据返回
	 * @param msg
	 */
	public void done(Msg msg,HttpServletResponse response){
		done(msg.getValue(),msg.toString(),null,response);
	}
	
	public void doneError(HttpServletResponse response){
		done(Msg.Operation_Error,null,response);
	}
	
	/**
	 * 无数据返回，返回操作错误信息
	 * @param msg
	 */
	public void doneError(String msg,HttpServletResponse response){
		done(-1,msg,null,response);
	}
	
	private void done(Msg msg,ResultValues rv,HttpServletResponse response){
		printJson(new DataModel(msg,rv),response);
	}
	
	/**
	 * 自定返回操作
	 * @param code
	 * @param msg
	 */
	private void done(int code,String msg,ResultValues rv,HttpServletResponse response){
		printJson(new DataModel(code,msg,rv),response);
	}
	
    
    protected ActionValues getValues(HttpServletRequest request) {
		return new ActionValues(request);
	}
    
    @ExceptionHandler(Exception.class)
    public void exceptionHandler(Exception ex,HttpServletRequest request,HttpServletResponse response)throws Exception{
    	logger.error(ex.getMessage());
    	StackTraceElement[] sts=ex.getStackTrace();
    	for(StackTraceElement st:sts){
    		if(null!=st){
        		logger.error("在类 "+st.getClassName()+" 的第 "+st.getLineNumber()+" 行的 "+st.getMethodName()+" 方法");	
        	}
    	}
    	ex.printStackTrace(response.getWriter());
    	done(Msg.Operation_Error,response);
    }
    
}
