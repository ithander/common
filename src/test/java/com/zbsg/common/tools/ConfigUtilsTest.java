package com.zbsg.common.tools;

import org.junit.Test;

import junit.framework.TestCase;

public class ConfigUtilsTest extends TestCase {

	@Test
	public void test(){
		System.out.println(ConfigUtils.getConfig("dbName"));
	}
	
}
