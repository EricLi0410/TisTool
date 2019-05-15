package com.basic.dao.api;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
@SuppressWarnings({"rawtypes"})
public interface BaseDao<T> {

	
	//增加操作
	
	/**
	 * 创建新对象，然后返回新对象的主键值操作，该方法暂时保留不用
	 * @param sql 要执行的SQL语句
	 * @param javaBean 含有SQL语句中参数值的实体对象
	 * @return 新对象的主键
	 */
	public int saveEntityforSequence(String sql, Object javaBean);
	
	/**
	 * 创建新对象操作
	 * @param sql 要执行的SQL语句
	 * @param sqlParams 含有SQL语句中参数值的序列
	 * @return true:操作成功，false:操作失败
	 */
	public boolean saveEntitybyParams(String sql, Object... sqlParams);
		
	/**
	 * 创建新对象操作
	 * @param sql 要执行的SQL语句
	 * @param javaBean 含有SQL语句中参数值的实体对象
	 * @return true:操作成功，false:操作失败
	 */
	public boolean saveEntityforJavabean(String sql, Object javaBean);
	
	
	/**
	 * 更新对象操作
	 * @param sql 要执行的SQL语句
	 * @param sqlParams 含有SQL语句中参数值的序列
	 * @return true:操作成功，false:操作失败
	 */
	public boolean updateEntitybyParams(String sql, Object... sqlParams);
		
	/**
	 * 更新对象操作
	 * @param sql 要执行的SQL语句
	 * @param javaBean 含有SQL语句中参数值的实体对象
	 * @return true:操作成功，false:操作失败
	 */
	public boolean updateEntityforJavabean(String sql, Object javaBean);
	
	
	//查询操作
	
	/**
	 * 查询指定条件对象操作
	 * @param sql 要执行的SQL语句
	 * @param clazz 指定实体对象的类型
	 * @param sqlParams 含有SQL语句中参数值的序列
	 * @return 指定类型的对象
	 */

	public T findBySql(String sql, Class clazz, Object... sqlParams);

	/**
	 * 查询指定对象类型，条件的对象集合操作
	 * @param sql 要执行的SQL语句
	 * @param clazz 指定实体对象的类型
	 * @param sqlParams 含有SQL语句中参数值的序列
	 * @return 对象集合
	 */
	public List<T> findBySqlList(String sql, Class clazz, Object... sqlParams);

	/**
	 * 查询指定对象类型对象集合操作
	 * @param sql 要执行的SQL语句
	 * @param clazz 指定实体对象的类型
	 * @return 对象集合
	 */
	public List<T> findBySqlList(String sql, Class clazz);

	/**
	 * 执行存储过程并返回值操作
	 * @param sql 要执行的SQL语句
	 * @param ins 输入参数值列表
	 * @param outs 输出参数值类型
	 * @param objects 参数列表，从前到后依次为输入、输出参数
	 * @return 返回值
	 */
	public List<Object> findByProcedure(String sql, Object[] ins, int[] outTypes);
	
	/**
	 * 查询信息是否存在，其实就是调用的findBySqlForCount方法，只是该方法直接返回boolean类型结果
	 * @param sql 要执行的SQL语句
	 * @param sqlParams 含有SQL语句中参数值的序列
	 * @return true 存在 false 不存在
	 */
	public boolean checkExists(String sql, Object... sqlParams);
	
	/**
	 * 查询符合指定条件的记录总数操作
	 * @param sql 要执行的SQL语句
	 * @param sqlParams 含有SQL语句中参数值的序列
	 * @return 记录数量
	 */
	public int findBySqlForCount(String sql, Object... sqlParams);
	
	/**
	 * 查询符合指定条件的记录Map<paramName, paramValue>
	 * @param sql 要执行的SQL语句
	 * @param sqlParams 含有SQL语句中参数值的序列
	 * @return Map<paramName, paramValue>
	 */
	public Map<String,Object> findMapBySql(String sql, Object... sqlParams);
	public Map<String,Object> findMapBySql(String sql);
	
	/**
	 * 查询符合指定条件的记录Map<paramName, paramValue>
	 * @param sql 要执行的SQL语句
	 * @param sqlParams 含有SQL语句中参数值的序列
	 * @return Map<paramName, paramValue>
	 */
	public List<Map<String,Object>> findListMapBySql(String sql, Object... sqlParams);
	public List<Map<String,Object>> findListMapBySql(String sql);
	
	
	
	/** 
	 * 删除对象操作
	 * @param sql 要执行的SQL语句
	 * @param sqlParams 含有SQL语句中参数值的序列
	 * @return true:操作成功，false:操作失败
	 */
	public boolean deleteEntity(String sql, Object... sqlParams);
	
	/**
	 * 执行存储过程
	 * @param sql
	 * @param ins
	 * @param outTypes
	 * @return
	 */
	public ResultSet findByProcedureforResultset(String sql, Object[] ins,int[] outTypes);
	/**
	 * 执行存储过程  返回俩个查询结果集
	 * @param sql
	 * @return
	 */
	public  Map<String,Object> findByProcedures(String sql,Object... sqlParams);
	
	
	/**
	 * 清空表
	 * @param tableName 表名
	 * @return
	 */
	public boolean deleteTableAll(String tableName);
}
