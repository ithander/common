package com.zbsg.common.filter; 

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.zbsg.common.tools.JsonUtils;


/**
 * 
 * 记录系统日志
 * @author zyt
 *
 */
public class LogInterceptor implements HandlerInterceptor{

	private Logger logger=Logger.getLogger(LogInterceptor.class);
	
	@Override
	public boolean preHandle(HttpServletRequest request,HttpServletResponse response,Object obj) throws Exception {
		request.setAttribute("startTime", System.currentTimeMillis());
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,HttpServletResponse response,Object obj, ModelAndView mv)throws Exception{
		logger.info("┳┳┳┳┳┳┳┳┳┳┳┳┳┳┳┳┳┳┳┳┳┳┳┳┳┳┳┳┳┳┳┳┳┳┳┳┳┳┳┳┳┳┳┳┳┳┳┳┳┳┳┳┳┳┳┳┳┳┳┳┳┳┳┳┳┳┳┳┳┳┳┳┳");
		logger.info("请求："+request.getRequestURI());
		logger.info("参数："+JsonUtils.toJsonStr(request.getParameterMap()));
		logger.info("耗时："+(System.currentTimeMillis()-Long.parseLong(String.valueOf(request.getAttribute("startTime")))));
		logger.info("┻┻┻┻┻┻┻┻┻┻┻┻┻┻┻┻┻┻┻┻┻┻┻┻┻┻┻┻┻┻┻┻┻┻┻┻┻┻┻┻┻┻┻┻┻┻┻┻┻┻┻┻┻┻┻┻┻┻┻┻┻┻┻┻┻┻┻┻┻┻┻┻┻");
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request,HttpServletResponse response,Object obj, Exception ex)throws Exception{
		
	}
	
}