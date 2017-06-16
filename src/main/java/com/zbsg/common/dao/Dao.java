package com.zbsg.common.dao;

import java.sql.Connection;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class Dao {

	private DataSource dataSource;
	
	private static JdbcTemplate jdbcTemplate;
	
	private static NamedParameterJdbcTemplate namedJdbcTemplate;
	
	private static QueryRunner queryRunner;
	
	public Connection getConnection(){
		Connection conn=null;
		try{
            conn=dataSource.getConnection();	
		}catch(Exception e){
			e.printStackTrace();
		}
		return conn;
	}
	
	@Autowired
	private void setDataSource(DataSource dataSource){
		if(null!=dataSource&&(null==jdbcTemplate||null==namedJdbcTemplate||null==queryRunner)){
			jdbcTemplate=new JdbcTemplate(dataSource);
			namedJdbcTemplate=new NamedParameterJdbcTemplate(jdbcTemplate);
			queryRunner=new QueryRunner(dataSource);
		}
	}
	
	public DataSource getDataSource() {
		return dataSource;
	}
	
	/*public static QueryRunner getQueryRunner() {
		return queryRunner;
	}*/

	public static JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public static NamedParameterJdbcTemplate getNamedJdbcTemplate() {
		return namedJdbcTemplate;
	}
	
	
}
