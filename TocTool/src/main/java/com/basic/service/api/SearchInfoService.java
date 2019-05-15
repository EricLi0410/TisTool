package com.basic.service.api;

import java.text.ParseException;
import java.util.Map;

import com.basic.entity.api.Document;

public interface SearchInfoService {
	
	/**
	 * 根据gitUrl查询文档发布信息
	 * @param gitUrl
	 * @return
	 * @throws ParseException
	 */
	public  Map<String,Document> getInfo(String gitUrl) throws ParseException;
	
	/***
	 * 查询md文档发布信息
	 * 单个查询
	 * @param gitUrl
	 * @return
	 */
	public Map<String, Document> searchRelease(String gitUrl);
	
	/**
	 * 查询md文档发布信息
	 * 遍历md文档目录时使用
	 * @param gitUrl
	 * @return
	 */
	public Document searchReturnRepo(String gitUrl);
	

}
