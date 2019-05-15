package com.basic.service.api;

import java.io.IOException;

import org.json.JSONObject;

public interface GetInformationService {
	/**
	 * 按条件查询文档信息
	 * @param params
	 * @param disable
	 * @param visible
	 * @param inMenu
	 * @return
	 */
	public JSONObject searchByCond(String pageNum, String pageSize, String[] params, String[] disable, String[] visible,
			String[] inMenu, String[] sourceUrl, String[] redirectUrl);
	
	/**
	 * 导出按条件查询的文档信息
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public Boolean exportByCond(String params, String website, String disable, String visible,
			String inMenu, String sourceUrl, String redirectUrl, String path) throws IOException;

 
}
