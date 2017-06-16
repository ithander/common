package com.zbsg.common;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.zbsg.common.tools.JsonUtils;

/**
 * Unit test for simple App.
 */
public class AppTest {

    @Test
    public void testApp(){
    	
    	Map<String,Object> a=new HashMap<String,Object>();
    	a.put("a", 1);
    	a.put("aa", 2);
    	
    	Map<String,Object> b=new HashMap<String,Object>();
    	b.putAll(a);
    	
    	a.put("aaa", 3);
    	
    	
    	System.out.println(JsonUtils.toJsonStr(b));
    }
}
