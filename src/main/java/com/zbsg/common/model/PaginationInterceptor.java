package com.zbsg.common.model;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.log4j.Logger;

import com.zbsg.common.model.ActionValues;
import com.zbsg.common.tools.ConfigUtils;
import com.zbsg.common.tools.StrUtils;

@Intercepts({@Signature(type=StatementHandler.class,method="prepare",args={Connection.class,Integer.class})}) 
public class PaginationInterceptor implements Interceptor{
	private static String dialect=ConfigUtils.getDBName();//数据库方言
	private static Logger logger=Logger.getLogger(PaginationInterceptor.class);
	
	@SuppressWarnings("unchecked")
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		 StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
		 Object obj=statementHandler.getParameterHandler().getParameterObject();
		 MapperMethod.ParamMap<Object> paramObj=null;
		 ActionValues values=null;
		 boolean updateData=false;//如果obj是map则需要在分页执行后同步一下分页数据
		 if(obj instanceof MapperMethod.ParamMap){
			 paramObj=(MapperMethod.ParamMap<Object>)obj;
		 }
		 if(obj instanceof ActionValues){
			 values=(ActionValues)obj;
		 }
		 if(obj instanceof Map&&!(obj instanceof ActionValues)){
			 updateData=true;
			 values=new ActionValues(((Map<String, Object>)obj));
		 }
		 
		 MetaObject metaStatementHandler = SystemMetaObject.forObject(statementHandler); 
	     
	     MappedStatement mappedStatement = (MappedStatement)   
	     metaStatementHandler.getValue("delegate.mappedStatement");
	     
	     // 只要参数对象里有名称为pageNow的参数则进行分页。 
	    	 
	    	 if(null!=paramObj&&paramObj.keySet().contains("pageNow")){//判断参数里是否有名称为pageNow的参数
	    		 ActionValues pageValues=new ActionValues();
	    		 int pageNow=-1;
	    		 int pageSize=-1;
	    		 String orderBy=null;
	    		 String sort=null;
	    		 
	    		 Iterator<String> keys=paramObj.keySet().iterator();
	    		 while(keys.hasNext()){
	    			 String key=keys.next();
	    			 if("pageNow".equals(key)&&paramObj.get(key) instanceof Integer){
	    				 pageNow=Integer.parseInt(String.valueOf(paramObj.get(key)));
	    			 }
	    			 if("pageSize".equals(key)&&paramObj.get(key) instanceof Integer){
	    				 pageSize=Integer.parseInt(String.valueOf(paramObj.get(key)));
	    			 }
	    			 if("orderBy".equals(key)&&paramObj.get(key) instanceof String){
	    				 orderBy=String.valueOf(paramObj.get(key));
	    			 }
	    			 if("sort".equals(key)&&paramObj.get(key) instanceof String){
	    				 sort=String.valueOf(paramObj.get(key));
	    			 }
	    		 }
	    		 
	    		 
	    		pageValues.add("pageNow", pageNow);
	    		pageValues.add("pageSize", pageSize);
	    		pageValues.add("orderBy", orderBy);
	    		pageValues.add("sort", sort);
	    		BoundSql boundSql = (BoundSql) metaStatementHandler.getValue("delegate.boundSql");  
			          
		        String sql = boundSql.getSql();
		        String pageSQL=null;
		        if("mysql".equals(ConfigUtils.getDBName())){
		        	pageSQL=buildPageSqlForMysql(sql,pageNow,pageSize,orderBy,sort);
		        }else if("oracle".equals(ConfigUtils.getDBName())){
		        	pageSQL=buildPageSqlForOracle(sql,pageNow,pageSize,orderBy,sort);
		        }
		        
		    	metaStatementHandler.setValue("delegate.boundSql.sql", pageSQL==null?sql:pageSQL);  
		        // 采用物理分页后，就不需要mybatis的内存分页了，所以重置下面的两个参数  
		        metaStatementHandler.setValue("delegate.rowBounds.offset",RowBounds.NO_ROW_OFFSET);  
		        metaStatementHandler.setValue("delegate.rowBounds.limit", RowBounds.NO_ROW_LIMIT);  
		        Connection connection = (Connection) invocation.getArgs()[0];  
		        // 重设分页参数里的总页数等  
		             
		        setPageParameter(sql, connection, mappedStatement, boundSql,pageValues); 
	    	 }
	    	 
	    	 if(null!=values&&values.isNotEmpty("pageNow")){
	    		 BoundSql boundSql = (BoundSql) metaStatementHandler.getValue("delegate.boundSql");  
		          
	             String sql = boundSql.getSql();
	             String pageSql=null;
	             if ("mysql".equals(ConfigUtils.getDBName())) {  
	                 pageSql = buildPageSqlForMysql(sql, values);  
	             } else if ("oracle".equals(ConfigUtils.getDBName())) {  
	                 pageSql = buildPageSqlForOracle(sql, values);  
	             }
	             metaStatementHandler.setValue("delegate.boundSql.sql", pageSql==null?sql:pageSql);  
	             // 采用物理分页后，就不需要mybatis的内存分页了，所以重置下面的两个参数  
	             metaStatementHandler.setValue("delegate.rowBounds.offset",RowBounds.NO_ROW_OFFSET);  
	             metaStatementHandler.setValue("delegate.rowBounds.limit", RowBounds.NO_ROW_LIMIT);  
	             Connection connection = (Connection) invocation.getArgs()[0];  
	             // 重设分页参数里的总页数等  
	             setPageParameter(sql, connection, mappedStatement, boundSql,values); 
	             if(updateData){
	            	 ((Map<String,Object>)obj).putAll(values);
	             }
	    	 }
	    	 
	    
	     logger.info(statementHandler.getBoundSql().getSql());
	     // 将执行权交给下一个拦截器  
	     return invocation.proceed();  
	}
	
    private String buildPageSqlForMysql(String sql, int pageNow,int pageSize,String orderBy,String sort) {  
        StringBuilder pageSql = new StringBuilder(100);  
        pageNow=pageNow<1?1:pageNow;
        pageSize=pageSize<0?30:pageSize;
        int fromRow=(pageNow==1?0:pageNow-1)*pageSize;
        pageSql.append(sql);
        if(StrUtils.isNotBlank(orderBy)){
        	pageSql.append(" order by ");
        	pageSql.append(orderBy);
            pageSql.append("  ");
            
            pageSql.append(StrUtils.isNotBlank(sort)?sort:"desc");
        }
        
        pageSql.append(" limit " + fromRow + "," +pageSize);  
        return pageSql.toString();  
    }  
    
    private String buildPageSqlForOracle(String sql, int pageNow,int pageSize,String orderBy,String sort) {  
    	StringBuilder pageSql = new StringBuilder(100);  
    	pageNow=pageNow<1?1:pageNow;
        pageSize=pageSize<0?30:pageSize;
        int fromRow=(pageNow==1?0:pageNow-1)*pageSize;
        int endrow=pageNow*pageSize;  
        pageSql.append("select * from ( select temp.*, rownum row_id from ( ");  
        pageSql.append(sql);
        
        if(StrUtils.isNotBlank(orderBy)){
        	pageSql.append(" order by "+orderBy+" "+sort==null?"desc":sort);
        }
        
        pageSql.append(" ) temp where rownum <= ").append(endrow);  
        pageSql.append(") where row_id > ").append(fromRow);  
        return pageSql.toString();  
    }  
    
    private String buildPageSqlForMysql(String sql, ActionValues values) {  
        StringBuilder pageSql = new StringBuilder(100);  
        int pageNow=values.getInt(ActionValues.PAGE_NOW)<1?1:values.getInt(ActionValues.PAGE_NOW);
        int pageSize=values.getInt(ActionValues.PAGE_SIZE)<1?30:values.getInt(ActionValues.PAGE_SIZE);
        int fromRow=(pageNow==1?0:pageNow-1)*pageSize;
        pageSql.append(sql);
        if(values.isNotEmpty(ActionValues.ORDER_BY)){
        	pageSql.append(" order by ");
            pageSql.append(values.getStr(ActionValues.ORDER_BY));
            pageSql.append("  ");
            pageSql.append(values.getStr(ActionValues.ORDER_SORT)==null?"desc":values.getStr(ActionValues.ORDER_SORT));
        }
        
        pageSql.append(" limit " + fromRow + "," +(values.getInt("pageSize")==-1?30:values.getInt("pageSize") ));  
        return pageSql.toString();  
    }  
    
    private String buildPageSqlForOracle(String sql, ActionValues values) {  
        StringBuilder pageSql = new StringBuilder(100);  
        int pageNow=values.getInt(ActionValues.PAGE_NOW)<1?1:values.getInt(ActionValues.PAGE_NOW);
        int pageSize=values.getInt(ActionValues.PAGE_SIZE)<1?30:values.getInt(ActionValues.PAGE_SIZE);
        int fromRow=(pageNow==1?0:pageNow-1)*pageSize;
        int endrow=pageNow*pageSize;  
        pageSql.append("select * from ( select temp.*, rownum row_id from ( ");  
        pageSql.append(sql);
        if(values.isNotEmpty(ActionValues.ORDER_BY)){
            pageSql.append(" order by "+values.getStr(ActionValues.ORDER_BY)+" "+values.getStr(ActionValues.ORDER_SORT)==null?"desc":values.getStr(ActionValues.ORDER_SORT));
        }
        pageSql.append(" ) temp where rownum <= ").append(endrow);  
        pageSql.append(") where row_id > ").append(fromRow);  
        return pageSql.toString();  
    }  
	
    /** 
     * 从数据库里查询总的记录数并计算总页数，回写进分页参数<code>PageParameter</code>,这样调用  
     * 者就可用通过 分页参数<code>PageParameter</code>获得相关信息。 
     *  
     * @param sql 
     * @param connection 
     * @param mappedStatement 
     * @param boundSql 
     * @param page 
     */  
    private void setPageParameter(String sql, Connection connection, MappedStatement mappedStatement,BoundSql boundSql, ActionValues values) {  
        // 记录总记录数  
        String countSql = "select count(0) from (" + sql + ") as total";  
        PreparedStatement countStmt = null;  
        ResultSet rs = null;  
        try {  
            countStmt = connection.prepareStatement(countSql);  
            BoundSql countBS = new BoundSql(mappedStatement.getConfiguration(),countSql,boundSql.getParameterMappings(),boundSql.getParameterObject());  
            setParameters(countStmt, mappedStatement, countBS, boundSql.getParameterObject());  
            rs = countStmt.executeQuery();  
            int totalCount = 0;  
            if (rs.next()) {  
                totalCount = rs.getInt(1);  
            }  
            values.put(ActionValues.TOTAL, totalCount);
            int pageSize=values.getInt(ActionValues.PAGE_SIZE)<0?30:values.getInt(ActionValues.PAGE_SIZE);
            int totalPage = totalCount/pageSize+((totalCount%pageSize== 0)?0:1);
            values.put(ActionValues.PAGE_SIZE, pageSize);
            values.put(ActionValues.PAGE_NUM, totalPage);
        } catch (SQLException e) {  
            logger.error("Ignore this exception", e);  
        } finally {  
            try {  
                rs.close();  
            } catch (SQLException e) {  
                logger.error("Ignore this exception", e);  
            }  
            try {  
                countStmt.close();  
            } catch (SQLException e) {  
                logger.error("Ignore this exception", e);  
            }  
        }  
    }  
	
	/** 
     * 对SQL参数(?)设值,参考org.apache.ibatis.executor.parameter.DefaultParameterHandler 
     * @param ps 
     */  
    private void setParameters(PreparedStatement ps,MappedStatement mappedStatement,BoundSql boundSql,Object parameterObject) throws SQLException {  
		ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, parameterObject, boundSql);  
	    parameterHandler.setParameters(ps); 
    }  
      

	@Override
	public Object plugin(Object target) {
		if(target instanceof StatementHandler){  
	        return Plugin.wrap(target, this);  
	    }else{  
	        return target;  
	    }  
	}

	@Override
	public void setProperties(Properties p) {
		dialect = p.getProperty("dialect");  
        if (StrUtils.isNotBlank(dialect)) {
        	dialect="mysql";
        }  
	}
}
