package com.framework.page;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.util.StringUtils;
import org.springframework.web.util.HtmlUtils;

public class BasePagination<T> implements Serializable {

	private static final long serialVersionUID = -4319028035771574084L;

	/**
	 * 默认一页数量
	 */
	protected static int default_limit = 10;
	/**
	 * 当前页
	 */
	protected Integer pageNum = 1;
	/**
	 * 一页数量
	 */
	protected Integer numPerPage = default_limit;
	/**
	 * 总数，如果页面传入了total，说明是分页跳转，那么则不需要再查询总数。
	 */
	protected Integer totalCount;
	/**
	 * search result
	 */
	protected Collection<T> result;
	/**
	 * 传入的其他搜索参数
	 */
	protected Map<String, String> params;
	/**
	 * execute elapsed time,unit is seconds.
	 */
	protected Long elapsedTime;
	
	protected String filterParamReg = "[\\<\\>\\\"\\']";//需要过滤掉param值中的字符

	protected String backparam;
	/**
	 * 获取总页数
	 * 
	 * @return
	 */
	public Integer getTotalPage() {
		return totalCount == null ? 0 : (totalCount + numPerPage - 1) / numPerPage;
	}

	/**
	 * 获取前一页数。如果没有则返回null
	 * 
	 * @return
	 */
	public Integer getBeforePage() {
		return this.getPageNum() == 0 ? null : this.getPageNum() - 1;
	}

	/**
	 * 获取下一页数。如果没有则返回null
	 * 
	 * @return
	 */
	public Integer getNextPage() {
		return this.getPageNum() + 1 < getTotalPage() ? this.getPageNum() + 1 : null;
	}

	/**
	 * 获取带查询参数的map对象。start：查询起始位。limit：查询多少条
	 * 
	 * @return
	 */
	public Map<String, Object> getSearchParamsMap() {
		decodeHtmlFormatParams();
		
		Map<String, Object> map = new HashMap<String, Object>();
		if (this.params != null) {
			map.putAll(params);
		}
		map.put("start", (this.getPageNum()-1) * this.numPerPage);
		map.put("limit", this.numPerPage*this.getPageNum());
		return map;
	}
	
	public RowBounds getRowBounds(){
		RowBounds bounds = new  RowBounds((this.getPageNum()-1) * this.numPerPage,this.numPerPage);
		return bounds;
	}
	
	/**
	 * 如果传入的参数有的是html格式的（如：&#20013;&#22269; 格式），则decode。
	 */
	public void decodeHtmlFormatParams(){
		if(null != params && !params.isEmpty()){
			for(String key:params.keySet()){
				String value = params.get(key);
				if(!StringUtils.isEmpty(value)){
					value = HtmlUtils.htmlUnescape(value);
					 value = value.trim().replaceAll(filterParamReg, "");				       
					params.put(key, value);
				}				
			}
		}
	}
	
	
	/**
	 * 是否需要设置总数,当页面没有传入total，则为null，那么需要设置总数。
	 * 
	 * @return
	 */
	public boolean isNeedSetTotal() {
		return totalCount == null ? true : false;
	}

	public Integer getNumPerPage() {
		return numPerPage;
	}

	public void setNumPerPage(Integer numPerPage) {
		this.numPerPage = numPerPage;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public Collection<T> getResult() {
		return result;
	}

	public void setResult(Collection<T> result) {
		this.result = result;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public Long getElapsedTime() {
		return elapsedTime;
	}

	public void setElapsedTime(Long elapsedTime) {
		this.elapsedTime = elapsedTime;
	}

	public void setFilterParamReg(String filterParamReg) {
		this.filterParamReg = filterParamReg;
	}

	public Integer getPageNum() {
		return pageNum == null ? 1 : pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public String getBackparam() {
		return backparam;
	}

	public void setBackparam(String backparam) {
		this.backparam = backparam;
	}
	
}
