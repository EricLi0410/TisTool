package com.basic.controller.api;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.basic.controller.BaseController;
import com.basic.entity.api.Document;
import com.basic.service.api.SearchInfoService;

@Controller
public class SearchInfoController extends BaseController<Object>{
	
	@Resource
	private SearchInfoService searchInfoService;
	
	/**
	 * 根据git url地址获取发布文档相关信息
	 * @param gitUrl
	 * @throws ParseException 
	 */
	@RequestMapping("/searchInfo")
	public void index(HttpServletRequest request, HttpServletResponse response) throws ParseException{
		String gitUrl = request.getParameter("gitUrl");
		JSONObject json = new JSONObject();
        Map<String, Document> doc = searchInfoService.getInfo(gitUrl);
//		Map<String, Document> doc = searchInfoService.searchRelease(gitUrl);
        json.put("json", doc);
        toClient(json.toString());
	}
	
}
