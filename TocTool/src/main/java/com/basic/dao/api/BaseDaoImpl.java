package com.basic.dao.api;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.basic.dao.api.BaseDao;

@SuppressWarnings({"unchecked","deprecation","rawtypes"})
public class BaseDaoImpl<T> extends SimpleJdbcDaoSupport implements BaseDao<T>{
	private final Logger logger = Logger.getLogger(BaseDaoImpl.class);
	//增加操作
	
	/**
	 * 创建新对象，然后返回新对象的主键值操作，该方法暂时保留不用
	 * @param sql 要执行的SQL语句
	 * @param javaBean 含有SQL语句中参数值的实体对象
	 * @return 新对象的主键
	 */
	public int saveEntityforSequence(String sql, Object javaBean){
		SqlParameterSource paramSource = new BeanPropertySqlParameterSource(javaBean);
		KeyHolder generatedKeyHolder = new GeneratedKeyHolder();
		this.getSimpleJdbcTemplate().getNamedParameterJdbcOperations().update(sql, paramSource, generatedKeyHolder,new String[]{"gid"});
		return generatedKeyHolder.getKey().intValue();
	}
	
	/**
	 * 创建新对象操作
	 * @param sql 要执行的SQL语句
	 * @param sqlParams 含有SQL语句中参数值的序列
	 * @return true:操作成功，false:操作失败
	 */
	public boolean saveEntitybyParams(String sql, Object... sqlParams){
		int i = 0;
		i = this.getSimpleJdbcTemplate().update(sql, sqlParams);
		if (i > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 创建新对象操作
	 * @param sql 要执行的SQL语句
	 * @param javaBean 含有SQL语句中参数值的实体对象
	 * @return true:操作成功，false:操作失败
	 */
	public boolean saveEntityforJavabean(String sql, Object javaBean){
		int i = 0;
		SqlParameterSource paramSource = new BeanPropertySqlParameterSource(javaBean);
		i = this.getSimpleJdbcTemplate().getNamedParameterJdbcOperations().update(sql, paramSource);
		if (i > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 更新对象操作
	 * @param sql 要执行的SQL语句
	 * @param sqlParams 含有SQL语句中参数值的序列
	 * @return true:操作成功，false:操作失败
	 */
	public boolean updateEntitybyParams(String sql, Object... sqlParams){
		int i = 0;
		i = this.getSimpleJdbcTemplate().update(sql, sqlParams);
		if (i > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 更新对象操作
	 * @param sql 要执行的SQL语句
	 * @param javaBean 含有SQL语句中参数值的实体对象
	 * @return true:操作成功，false:操作失败
	 */
	public boolean updateEntityforJavabean(String sql, Object javaBean){
		int i = 0;
		SqlParameterSource paramSource = new BeanPropertySqlParameterSource(javaBean);
		i = this.getSimpleJdbcTemplate().getNamedParameterJdbcOperations().update(sql, paramSource);
		if (i > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	//查询操作
	
	/**
	 * 查询指定条件对象操作
	 * @param sql 要执行的SQL语句
	 * @param clazz 指定实体对象的类型
	 * @param sqlParams 含有SQL语句中参数值的序列
	 * @return 指定类型的对象
	 */
	public T findBySql(String sql, Class clazz, Object... sqlParams){
		try {
			return (T)this.getSimpleJdbcTemplate().queryForObject(sql, ParameterizedBeanPropertyRowMapper.newInstance(clazz), sqlParams);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("没有查询到数据");
			return null;
		}
	}
	
	/**
	 * 查询指定对象类型，条件的对象集合操作
	 * @param sql 要执行的SQL语句
	 * @param clazz 指定实体对象的类型
	 * @param sqlParams 含有SQL语句中参数值的序列
	 * @return 对象集合
	 */
	public List<T> findBySqlList(String sql, Class clazz, Object... sqlParams){
		return this.getSimpleJdbcTemplate().query(sql, ParameterizedBeanPropertyRowMapper.newInstance(clazz), sqlParams);
	}
	
	/**
	 * 查询指定对象类型对象集合操作
	 * @param sql 要执行的SQL语句
	 * @param clazz 指定实体对象的类型
	 * @return 对象集合
	 */
	public List<T> findBySqlList(String sql, Class clazz){
		return this.getSimpleJdbcTemplate().query(sql, ParameterizedBeanPropertyRowMapper.newInstance(clazz));
	}
	
	/**
	 * 执行存储过程并返回值操作
	 * @param sql 要执行的SQL语句
	 * @param objects 参数列表，从前到后依次为输入、输出参数
	 * @return 返回值
	 */
	public List<Object> findByProcedure(String sql, Object[] ins, int[] outTypes){
		Connection con = null;
		List<Object> results = null;
		try {
			con = this.getConnection();
			CallableStatement call = con.prepareCall(sql);
			int i = 1;
			int index = 0;
			//遍历输入参数，并设置进过程中
			for(i=1;i<=ins.length;i++){
				index = i-1;
				call.setObject(i, ins[index]);
			}
			//遍历输出参数，并注册进过程中
			for(i=(ins.length+1);i<=(ins.length+outTypes.length);i++){
				index = i-(ins.length+1);
				call.registerOutParameter(i, outTypes[index]);
			}
			call.execute();
			//执行完毕，提取运行结果
			results = new ArrayList<Object>();
			for(i=(ins.length+1);i<=(ins.length+outTypes.length);i++){
				results.add(call.getObject(i));
			}		
		} catch (Exception e) {
			logger.debug("findByProcedure 方法执行错误!");
			return null;
		} 
		return results;
	}
	
	/**
	 * 查询信息是否存在，其实就是调用的findBySqlForCount方法，只是该方法直接返回boolean类型结果
	 * @param sql 要执行的SQL语句
	 * @param sqlParams 含有SQL语句中参数值的序列
	 * @return true 存在 false 不存在
	 */
	public boolean checkExists(String sql, Object... sqlParams){
		int count = findBySqlForCount(sql, sqlParams);
		if(count!=0){
			return true;
		}
		return false;
	}
	
	/**
	 * 查询符合指定条件的记录总数操作
	 * @param sql 要执行的SQL语句
	 * @param sqlParams 含有SQL语句中参数值的序列
	 * @return 记录数量
	 */
	public int findBySqlForCount(String sql, Object... sqlParams){
		return this.getSimpleJdbcTemplate().queryForInt(sql, sqlParams);
	}
	
	/**
	 * 查询符合指定条件的记录Map<paramName, paramValue>
	 * @param sql 要执行的SQL语句
	 * @param sqlParams 含有SQL语句中参数值的序列
	 * @return Map<paramName, paramValue>
	 */
	public Map<String,Object> findMapBySql(String sql, Object... sqlParams){
		try {
			return this.getSimpleJdbcTemplate().queryForMap(sql, sqlParams);
		} catch (Exception e) {
			System.out.println("没有查询到数据");
		}
		return null;
	}

	@Override
	public Map<String, Object> findMapBySql(String sql) {
		try {
			return this.getSimpleJdbcTemplate().queryForMap(sql);
		} catch (Exception e) {
			System.out.println("没有查询到数据");
		}
		return null;
	}
	
	/**
	 * 查询符合指定条件的记录集合List<Map<paramName, paramValue>>
	 * @param sql 要执行的SQL语句
	 * @param sqlParams 含有SQL语句中参数值的序列
	 * @return List<Map<paramName, paramValue>>
	 */
	public List<Map<String,Object>> findListMapBySql(String sql, Object... sqlParams){
		return this.getSimpleJdbcTemplate().queryForList(sql, sqlParams);
	}

	@Override
	public List<Map<String, Object>> findListMapBySql(String sql) {
		return this.getSimpleJdbcTemplate().queryForList(sql);
	}

	
	/** 
	 * 删除对象操作
	 * @param sql 要执行的SQL语句
	 * @param sqlParams 含有SQL语句中参数值的序列
	 * @return true:操作成功，false:操作失败
	 */
	public boolean deleteEntity(String sql, Object... sqlParams){
		int i = 0;
		i = this.getSimpleJdbcTemplate().update(sql, sqlParams);
		if (i > 0) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * 清空表
	 * @param tableName 表名
	 * @return
	 */
	public boolean deleteTableAll(String tableName){
		this.getSimpleJdbcTemplate().update("truncate table "+tableName);
		if (findBySqlForCount("select count(0) from "+tableName) == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 执行存储过程
	 * @param sql
	 * @param ins
	 * @param outTypes
	 * @return
	 */
	public ResultSet findByProcedureforResultset(String sql,Object[] ins,int[] outTypes) {
		Connection con = null;
		ResultSet rs=null;
		try {
			con = this.getConnection();
			CallableStatement call = con.prepareCall(sql);
			int i = 1;
			int index = 0;
			// 遍历输入参数，并设置进过程中
			for (i = 1; i <= ins.length; i++) {
				index = i - 1;
				call.setObject(i, ins[index]);
			}
			// 遍历输出参数，并注册进过程中
			for (i = (ins.length + 1); i <= (ins.length + outTypes.length); i++) {
				index = i - (ins.length + 1);
				call.registerOutParameter(i, outTypes[index]);
			}
			call.execute();
			// 执行完毕，提取运行结果
			rs = (ResultSet) call.getObject(2);// 获取游标的值
		} catch (Exception e) {
			
			e.printStackTrace();
			logger.debug("findByProcedure 方法执行错误!");
			return null;
		} finally{
			
		}
		return rs;
	}
	
	@Override
	public Map<String, Object> findByProcedures(String sql, Object... sqlParams) {
		Map<String,Object> maps=null;
		
		try {
			Connection con =this.getJdbcTemplate().getDataSource().getConnection();
			CallableStatement callsm = null;
			callsm = con.prepareCall(sql);
			if (sqlParams!=null) {
				for (int i=0;i<sqlParams.length;i++) {
					callsm.setObject(i+1, sqlParams[i]);
				}
			}
			
			callsm.execute();
			ResultSet rowSet = callsm.getResultSet();
			ResultSetMetaData  rsm= rowSet.getMetaData(); //获得列集
			int col=0;
			maps=new HashMap<String,Object>();
			col = rsm.getColumnCount();
			List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
			while(rowSet.next()){
				 Map<String,Object> map=new HashMap<String, Object>();
				 for (int i = 0; i < col; i++) {
					String key=rsm.getColumnName(i+ 1 );
					map.put(key, rowSet.getString(key));
					
				 }
				 list.add(map);
			}
			maps.put("data1", list);
			int c=1;
			list=new ArrayList<Map<String,Object>>();
			while(callsm.getMoreResults()) {// 这个判断会自动指向下一个结果集
				ResultSet rs2 = callsm.getResultSet();
				ResultSetMetaData  rsm2= rs2.getMetaData(); //获得列集
				while(rs2.next()){
					 Map<String,Object> map=new HashMap<String, Object>();
					 for (int i = 0; i < col; i++) {
						String key=rsm2.getColumnName(i+ 1 );
						map.put(key, rs2.getString(key));
					 }
					 list.add(map);
				}
				c++;
				maps.put("data"+c, list);
			}
			callsm.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return maps;
	}
	
	

}