package com.zbsg.common.tools;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ConfigUtils {

	private static String dbName="mysql";//默认mysql,配置文件里可以配置
	private static Properties config=null;
	private static String[] configFiles={"system.properties"};//需要加载到内存听配置信息
	private static Logger logger=Logger.getLogger(ConfigUtils.class);
	
	static{
		logger.debug("初始化配置...");
		loadFiles();
		initConfig();
	}
	
	public static void loadFiles(){
		File classDir=new File(ConfigUtils.class.getResource("/").getPath());
		
		configFiles=classDir.list(new FilenameFilter(){
			@Override
			public boolean accept(File file, String fname) {
				if(fname.endsWith("properties")){
					return true;
				}
				return false;
			}
		});
		
	}
	
	/**
	 * 初始化配置
	 */
	public static void initConfig(){
		try{
			config=new Properties();
		    for(String file:configFiles){
		    	logger.debug("loading config file : "+file);
			    config.load(ConfigUtils.class.getResourceAsStream("/"+file));
		    }
		}catch(Exception e){
			e.printStackTrace();
			logger.error("初始化配置失败！");
			logger.error(e.getMessage());
		}
	}
	
	/**
	 * 得到classes下面的属性文件
	 * @param fileName
	 * @return
	 */
	public static Properties getConfigFile(String fileName){
		Properties pro=new Properties();
		try {
			pro.load(ConfigUtils.class.getResourceAsStream("/"+fileName));
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("获得配置文件失败！");
			logger.error(e.getMessage());
		}
		return pro;
	}
	
	/**
	 * 得到配置中的值
	 * @param key
	 * @return
	 */
	public static void setConfig(String key,String value){
		config.setProperty(key, value);
	}
	
	/**
	 * 得到配置中的值
	 * @param key
	 * @return
	 */
	public static String getConfig(String key){
		return config.getProperty(key);
	}

	/**
	 * 得到配置中的值
	 * @param key
	 * @return
	 */
	public static int getIntConfig(String key){
		if(null!=config.getProperty(key)&&StrUtils.isNumber(config.getProperty(key))){
			return Integer.parseInt(config.getProperty(key));
		}
		return 0;
	}
	
	public static String getDBName(){
		return ConfigUtils.dbName;
	}
	
}
