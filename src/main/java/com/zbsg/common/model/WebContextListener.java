package com.zbsg.common.model;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

public class WebContextListener implements ServletContextListener {

	private Logger logger=Logger.getLogger(WebContextListener.class);
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		try{
            logger.debug("启用系统初始化...");
		}catch(Exception e){
			e.printStackTrace();
			logger.error("未得到指定的APP_ID ！！！");
		}
	}


}
