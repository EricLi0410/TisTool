package com.basic.controller.api;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.ParseException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.basic.controller.BaseController;
import com.basic.service.api.ExportAllService;
import com.basic.util.FileUtil;

@Controller
public class ExportAllController extends BaseController{
	@Resource
	private ExportAllService exportAllService;
	
	/**
	 *  导出全量映射
	 *  中国站文档
	 * @throws IOException 
	 */
	@RequestMapping("/exportAll/exportChMapping")
	public void exportChMapping(HttpServletRequest request,HttpServletResponse response) throws IOException {
		String proPath = request.getSession().getServletContext().getRealPath(""); //excel保存地址（项目根目录）
		String temp = proPath.substring(0, proPath.lastIndexOf("\\"));
		String path = temp.substring(0, temp.lastIndexOf("\\"));
		String sevrPath = path +"\\gitUrlCH.xls"; 
		exportAllService.exportCh(sevrPath);  //导出excel到项目根目录
		downFile(sevrPath,request, response);  //下载
	}
	
	/**
	 *  导出全量映射
	 *  国际站文档
	 * @throws IOException 
	 */
	@RequestMapping("/exportAll/exportIntlMapping")
	public void exportIntlMapping(HttpServletRequest request,HttpServletResponse response) throws IOException {
		String proPath = request.getSession().getServletContext().getRealPath(""); //excel保存地址（项目根目录）
		String temp = proPath.substring(0, proPath.lastIndexOf("\\"));
		String path = temp.substring(0, temp.lastIndexOf("\\"));
		String sevrPath = path +"\\gitUrlIntl.xls"; 
		exportAllService.exportIntl(sevrPath);  //导出excel到项目根目录
		downFile(sevrPath,request, response);  //下载
	}
	
	
	/**
	  * 查询中国站git仓库中全量的md文件名称 并导出为excel
	 * @param url
	 * @param localPath
	 * @throws IOException 
	 */
	@RequestMapping("/exportAll/exportChMd")
	public void exportChMd(HttpServletRequest request,HttpServletResponse response) throws IOException {
		String filePath = "E:\\apache-tomcat_7.0.90\\webapps\\qcloud-documents";
		String sevrPath = "E:\\apache-tomcat_7.0.90\\webapps\\gitChMd.xls";
		FileUtil.fileVisitor(filePath, sevrPath);
		downFile(sevrPath, request, response); //下载excel到本地
	}

	
	/**
	  * 查询国际站git仓库中全量的md文件名称 并导出为excel
	 * @param url
	 * @param localPath
	 * @throws IOException 
	 */
	@RequestMapping("/exportAll/exportIntlMd")
	public void exportIntlMd(HttpServletRequest request,HttpServletResponse response) throws IOException {
		String filePath = "E:\\apache-tomcat_7.0.90\\webapps\\intlcloud-documents";
		String sevrPath = "E:\\apache-tomcat_7.0.90\\webapps\\gitIntlMd.xls";
		FileUtil.fileVisitor(filePath, sevrPath);
		downFile(sevrPath, request, response); //下载excel到本地
	}

	/**
	 *  导出中国站git仓库md文件映射信息为Excel
	 * @throws ParseException 
	 */
	@RequestMapping("/exportAll/exportChGitMap")
	public void exportChGitMap(HttpServletRequest request,HttpServletResponse response) throws IOException, ParseException {
		downFile("E:\\apache-tomcat_7.0.90\\webapps\\gitMappingCH.xls", request, response); //下载excel到本地
	}
	
	
	/**
	 *  导出国际站git仓库md文件映射信息为Excel
	 * @throws ParseException 
	 */
	@RequestMapping("/exportAll/exportIntlGitMap")
	public void exportIntlGitMap(HttpServletRequest request,HttpServletResponse response) throws IOException, ParseException {
		downFile("E:\\apache-tomcat_7.0.90\\webapps\\gitMappingIntl.xls", request, response); //下载excel到本地
	}

	
	/**
	 * 触发浏览器下载，从服务器下载excel到本地
	 * @param name  文件名
	 * @param request
	 * @param response
	 */
	public void downFile(String sevrPath, HttpServletRequest request,HttpServletResponse response) {
		try {
			// 处理文件名
			String name = sevrPath.substring(sevrPath.lastIndexOf("/") + 1);
			// 设置响应头，控制浏览器下载该文件
			response.setHeader("content-disposition", "attachment;filename="
			        + URLEncoder.encode(name, "UTF-8"));
			// 读取要下载的文件，保存到文件输入流
			FileInputStream in = new FileInputStream(sevrPath);
			// 创建输出流
			OutputStream out = response.getOutputStream();
			// 创建缓冲区
			byte buffer[] = new byte[1024];
			int len = 0;
			// 循环将输入流中的内容读取到缓冲区当中
			while ((len = in.read(buffer)) > 0) {
			    // 输出缓冲区的内容到浏览器，实现文件下载
			    out.write(buffer, 0, len);
			    out.flush();
			}
			// 关闭文件输入流
			in.close();
			// 关闭输出流
			out.close();
			System.out.println("download file success ! ");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
