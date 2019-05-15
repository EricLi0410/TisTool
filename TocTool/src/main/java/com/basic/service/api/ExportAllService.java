package com.basic.service.api;

import java.io.IOException;
import java.text.ParseException;

public interface ExportAllService {
	
	/**
	  *  根据官网URl导出全量映射信息为Excel
	 *  中国站文档信息
	 * @return
	 * @throws IOException 
	 */
	public Boolean exportCh(String path) throws IOException;
	
	/**
	 * 根据官网URL导出全量映射信息为Excel
	 * 国际站文档信息
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public Boolean exportIntl(String path) throws IOException;
	
	/**
	 * 导出git仓库中全量的md文件名称到excel
	 * @param url  gigthub链接地址
	 * @param fileName 文件名
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public Boolean exportMd(String url, String filePath, String path) throws IOException;
	
	/**
	 * 根据giturl 导出映射信息到excel
	 * @return
	 * @throws IOException
	 */
	public Boolean exportGitInfo(String url, String filePath, String path) throws IOException, ParseException ;
	

}
