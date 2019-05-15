package com.basic.controller.api;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.basic.controller.BaseController;
import com.basic.service.api.GetInformationService;

@Controller
public class GetInformationController extends BaseController {
	@Resource
	private GetInformationService getInfoService;
	
	/**
	 *  按条件查询文档信息
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/getInfo/searchByCond", method = {RequestMethod.POST})
	public void searchByCond(HttpServletRequest request, HttpServletResponse response) {
		String num =request().getParameter("num");
		String pageSize =request().getParameter("pageSize");
		String[] params = request.getParameterValues("params[]");
		String[] disable = request.getParameterValues("disable[]");
		String[] visible = request.getParameterValues("visible[]");
		String[] inMenu = request.getParameterValues("inMenu[]");
		String[] sourceUrl = request.getParameterValues("sourceUrl[]");
		String[] redirectUrl = request.getParameterValues("redirectUrl[]");
		JSONObject docLs = getInfoService.searchByCond(num, pageSize, params, disable, visible, inMenu, sourceUrl, redirectUrl);
        toClient(docLs.toString());
	}
	
	/**
	 * 导出按条件查询的文档信息
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/getInfo/exportByCond")
	public void exportByCond(HttpServletRequest request,HttpServletResponse response) throws IOException {
		String para = request.getParameter("param");
		String website = request.getParameter("website");
		String dsab = request.getParameter("disable");
		String vsb = request.getParameter("visible");
		String imu = request.getParameter("inMenu");
		String sourl = request.getParameter("sourceUrl");
		String reurl = request.getParameter("redirectUrl"); 
		String sevrPath = "";
		if("china".equals(website)) {
			sevrPath = "docCh.xls";
		}
		if("intl".equals(website)) {
			sevrPath = "docIntl.xls";
		}
		getInfoService.exportByCond(para, website, dsab, vsb, imu, sourl, reurl, sevrPath);
		downFile(sevrPath,request, response);  //下载

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
