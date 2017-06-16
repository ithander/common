package com.zbsg.common.dao;

import java.io.BufferedReader;
import java.io.Reader;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.zbsg.common.model.ActionValues;
import com.zbsg.common.tools.ConfigUtils;

public class BaseDao extends Dao {

	private static String pageHeader="select * from ( select a.*,rownum rn from (";
	private static String pageFooter=" ) a ) where rn between :fromRow and :toRow";
	
	/**
	 * 记算总记录数
	 * @param sql
	 * @param params map类型
	 * @return
	 */
	public Long count(String sql,Map<String,Object> params){
		return getNamedJdbcTemplate().queryForObject("select nvl(count(0),0) total from ("+sql+") a", params, Long.class);
	}
	
	/**
	 * 计算总记录数
	 * @param sql
	 * @param params 对象数组
	 * @return
	 */
	public Long counts(String sql,Object ...params){
		return getJdbcTemplate().queryForObject(sql, params, Long.class);
	}
	
	/**
	 * 查询一条数字列
	 * @param sql
	 * @param params map类型
	 * @return
	 */
	public Integer getInt(String sql,Map<String,Object> params){
		return getNamedJdbcTemplate().queryForObject(sql, params, Integer.class);
	}
	
	/**
	 * 查询一条数字列
	 * @param sql
	 * @param params 对象数组
	 * @return
	 */
    public Integer getsInt(String sql,Object ...params){
		return getJdbcTemplate().queryForObject(sql, params, Integer.class);
	}
    
    /**
     * 查询一条任意类型列
     * @param sql
     * @param params map类型
     * @param cls
     * @return
     */
    public Object getObj(String sql,Map<String,Object> params,Class<?> cls){
		return getNamedJdbcTemplate().queryForObject(sql, params, cls);
	}
	
    /**
     * 查询一条任意类型列
     * @param sql
     * @param params 对象数组
     * @return
     */
    public Object getsObj(String sql,Object ...params){
		return getJdbcTemplate().queryForObject(sql,Arrays.asList(params).subList(0, params.length-1).toArray(), (Class<?>)params[params.length-1]);
	}
    
    /**
     * 查询一条字符串列
     * @param sql
     * @param params map类型
     * @return
     */
    public String getStr(String sql,Map<String,Object> params){
		return getNamedJdbcTemplate().queryForObject(sql, params, String.class);
	}
	
    /**
     * 查询一条字符串列
     * @param sql
     * @param params 对象数组
     * @return
     */
    public String getsStr(String sql,Object ...params){
		return getJdbcTemplate().queryForObject(sql, params, String.class);
	}
    
    /**
     * 查询多条字符串列
     * @param sql
     * @param params 对象数组
     * @return
     */
    public List<String> listsStrs(String sql,Object ...params){
		return getJdbcTemplate().queryForList(sql, String.class, params);
	}
    
    /**
     * 查询多条字符串列
     * @param sql
     * @param params 对象数组
     * @return
     */
    public List<Integer> listsInts(String sql,Object ...params){
		return getJdbcTemplate().queryForList(sql, Integer.class, params);
	}
	
	/**
	 * 用传入的Map值 修改数据库
	 * @param sql
	 * @param params map类型
	 * @return
	 */
	public int updateSQL(String sql,Map<String,Object> params){
		return getNamedJdbcTemplate().update(sql, params);	
	}
	
	/**
	 * 简单修改，多参数值
	 * @param sql
	 * @param params 对象数组
	 * @return
	 */
	public int updatesSQL(String sql,Object ... params){
		return getJdbcTemplate().update(sql, params);
	}
	
	/**
	 * 查询一条记录
	 * @param sql
	 * @param params map类型
	 * @return
	 */
	public Map<String,Object> getSQL(String sql,Map<String,Object> params){
		List<Map<String,Object>> rs=listSQL(sql,params);
		if(null!=rs&&rs.size()>0){
			return rs.get(0);
		}
		return null;
	}
	
	/**
	 * 查询一条记录
	 * @param sql
	 * @param params 对象类型
	 * @return
	 */
	public Map<String,Object> getsSQL(String sql,Object ... params){
		List<Map<String,Object>> rs=listsSQL(sql,params);
		if(null!=rs&&rs.size()>0){
			return rs.get(0);
		}
		return null;
	}

	/**
	 * 查询，传map
	 * @param sql
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>> listSQL(String sql,Map<String,Object> params){
		ActionValues values=null;
		if(params instanceof ActionValues){
			values=(ActionValues)params;
		}else{
			values=new ActionValues(params);
		}
		
		if(values.isPage()){//如果需要分页
			
			//统计总记录数
			long total=count(sql,params);
			values.setTotal(total);
			
			if(!(params==values)){//如果不是同一实例,把数据同步到params中
				params.put(ActionValues.TOTAL, total);
				params.put(ActionValues.PAGE_NUM,values.getInt(ActionValues.PAGE_NUM));
			}
			
			
			int pageNow=1;
	        int pageSize=30;
	        if(0>=values.getInt(ActionValues.PAGE_NOW)){
	        	values.add(ActionValues.PAGE_NOW, pageNow);
	        }else{
	        	pageNow=values.getInt(ActionValues.PAGE_NOW);
	        }
	        
	        if(0>=values.getInt(ActionValues.PAGE_SIZE)){
	        	values.add(ActionValues.PAGE_SIZE,pageSize);
	        }else{
	        	pageSize=values.getInt(ActionValues.PAGE_SIZE);
	        }
			
	        StringBuilder sber=new StringBuilder();
			if("oracle".equals(ConfigUtils.getDBName())){
				
				sber.append(pageHeader).append(" ").append(sql);
				if(values.isSort()){
					sber.append(" ").append(values.orderBy()).append(" ").append(values.sort()).append(" ");
				}
				sber.append(pageFooter);
				values.add("fromRow", (pageNow<=1?0:(pageNow-1))*pageSize);
				values.add("toRow", (pageNow<=1?0:(pageNow-1))*pageSize+pageSize);
			}
			
			if("mysql".equals(ConfigUtils.getDBName())){
				sber.append(sql);
				if(values.isSort()){
					sber.append(" ").append(values.orderBy()).append(" ").append(values.sort()).append(" ");
				}
		        sber.append(" limit ").append((pageNow<=1?0:(pageNow-1))*pageSize).append(",").append(pageSize);
			}
			sql=sber.toString();
		}
		
	    return getNamedJdbcTemplate().query(sql, params, new RowMapper<Map<String,Object>>(){
			@Override
			public Map<String,Object> mapRow(ResultSet rst, int index) throws SQLException {
				ResultSetMetaData meta=rst.getMetaData();
				int columnNum=meta.getColumnCount();
				Map<String,Object> result=new HashMap<String,Object>(columnNum);
				for(int i=1;i<=columnNum;i++){
					if(2005==meta.getColumnType(i)){//只针对oracle的clob类型
						Clob clob=rst.getClob(i);
						if(null!=clob){
							StringBuilder sber=new StringBuilder();
							Reader reader=clob.getCharacterStream();
							BufferedReader br=new BufferedReader(reader);
							try{
								if(!br.ready()){
									String temp=br.readLine();
									while(null!=temp){
										sber.append(temp);
										temp=br.readLine();
									}
								}
								br.close();
								reader.close();
							}catch(Exception e){
								e.printStackTrace();
							}
							result.put(meta.getColumnLabel(i).toLowerCase(),sber.toString());
						}
					}else{
					    result.put(meta.getColumnLabel(i).toLowerCase(),rst.getObject(i));
					}
				}
				return result;
			}
    	});
	}
	
	/**
	 * 查询,多参数值,无法判断是否分页,只能把分页limit写在sql中
	 * @param sql
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>> listsSQL(String sql,Object ... params){
		return getJdbcTemplate().query(sql, params, new RowMapper<Map<String,Object>>(){
			@Override
			public Map<String,Object> mapRow(ResultSet rst, int index)throws SQLException {
				ResultSetMetaData meta=rst.getMetaData();
				int columnNum=meta.getColumnCount();
				Map<String,Object> result=new HashMap<String,Object>(columnNum);
				for(int i=1;i<=columnNum;i++){
					if(Types.CLOB==meta.getColumnType(i)){//只针对oracle的clob类型
						Clob clob=rst.getClob(i);
						if(null!=clob){
							StringBuilder sber=new StringBuilder();
							Reader reader=clob.getCharacterStream();
							BufferedReader br=new BufferedReader(reader);
							try{
								if(!br.ready()){
									String temp=br.readLine();
									while(null!=temp){
										sber.append(temp);
										temp=br.readLine();
									}
								}
								br.close();
								reader.close();
							}catch(Exception e){
								e.printStackTrace();
							}
							result.put(meta.getColumnLabel(i).toLowerCase(),sber.toString());
						}
					}else if(Types.DATE==meta.getColumnType(i)){
						result.put(meta.getColumnLabel(i).toLowerCase(),rst.getObject(i));
					}else if(Types.TIMESTAMP==meta.getColumnType(i)){
						result.put(meta.getColumnLabel(i).toLowerCase(),rst.getObject(i));
					}else{
					    result.put(meta.getColumnLabel(i).toLowerCase(),rst.getObject(i));
					}
				}
				return result;
			}
    	});
	}
	
}
