package com.basic.service.api.impl;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.basic.dao.api.BaseDao;
import com.basic.entity.api.Document;
import com.basic.service.api.SearchInfoService;
import com.basic.util.EncodeUtil;
import com.framework.common.HttpUtil;

@Service
public class SearchInfoServiceImpl implements SearchInfoService{

	@SuppressWarnings("rawtypes")
	@Resource
	 private BaseDao baseDao;
	
	/**
	 * 根据 gitUrl获取发布文档相关信息
	 * 调用接口查询
	 * @throws ParseException 
	 */
	@Override
	public Map<String, Document> getInfo(String gitUrl) throws ParseException {
		Document docInfo = new Document();
		//获取当前时间戳
 		long time = new Date().getTime();
 		String timeStamp = time + "";
 		timeStamp = timeStamp.substring(0, 10);
 		
 		String host = "http://accountbatch.tencentyun.com:50021/";  //现网域名
 		String interfaceBasic = "accountbatch.document.getDocumentPublishInfo"; //国内接口
 		String interfaceInter = "accountbatch.document.getInterDocumentPublishInfo"; //国际接口
 		String interfaceName = "";
 		
 		String intl = "intlcloud-documents";
 		if(gitUrl.contains(intl)) {
 			gitUrl = "https://github.com/tencentyun/qcloud-documents/blob/intlcloud-documents/"+gitUrl.substring(gitUrl.indexOf("blob")+5);
 			// 调用国际接口，返回国际站信息
 			interfaceName = interfaceInter;
 			docInfo.setLang("en");
 		}else {
 			//调用国内接口，返回中国站信息
 			interfaceName = interfaceBasic;
 			docInfo.setLang("ch");
 		}
 		
 		String url = host + interfaceName;
 		String body = " {\r\n" + 
 				"  \"version\": 1,\r\n" + 
 				"  \"componentName\": \"MC\",\r\n" + 
 				"  \"eventId\": 0,\r\n" + 
 				"  \"timestamp\": "+timeStamp+",\r\n" + 
 				"  \"interface\": {\r\n" + 
 				"    \"interfaceName\": \""+interfaceName+"\",\r\n" + 
 				"    \"para\": {\r\n" + 
 				"      \"sourceUrl\": \""+gitUrl+"\"\r\n" + 
 				"    }\r\n" + 
 				"  }\r\n" + 
 				"} ";
 		
 		
 		JSONObject jsonObj = new JSONObject(body);
 		String result = HttpUtil.sendPost(url, jsonObj);
 		if(result != null && !"".equals(result)) {
 			JSONObject jsonResult = new JSONObject(result);
 	 		// 处理jsonResult取出文档信息
 	 		Object data = jsonResult.get("data");
 	 		String strData = data.toString();
 	 		JSONObject jsonData = new JSONObject(strData);
 	 		JSONArray rows = jsonData.getJSONArray("rows");
 	 		
 	 		if(rows.length() > 0 ) {  //rows 有可能是 [] 空值  != null && !"".equals(rows.toString())
 	 			for(int i=0; i<rows.length(); i++) {
 		 			docInfo.setProduct(rows.getJSONObject(i).getString("categoryTitle"));
 		 			docInfo.setTitle(rows.getJSONObject(i).getString("title"));
 		 			
 		 			String recentReleaseTime = rows.getJSONObject(i).getString("recentReleaseTime");
 		 			Timestamp ts = Timestamp.valueOf(recentReleaseTime);
 		 			Date releaseTime = ts;
 		 			
 		 			docInfo.setReleaseTime(releaseTime);
 		 			Integer pageId = rows.getJSONObject(i).getInt("id");
 		 			Integer categoryId = rows.getJSONObject(i).getInt("categoryId");
 		 			// 处理菜单名称
 		 			String[] menu = rows.getJSONObject(i).getString("sourceUrl").split("/");
 		 			if(docInfo.getLang() == "en") {
 		 				docInfo.setWebUrl("https://intl.cloud.tencent.com/document/product/"+categoryId+"/"+pageId);
 		 				docInfo.setFirstMenu(menu[3]);
 		 			}else {
 		 				docInfo.setWebUrl("https://cloud.tencent.com/document/product/"+categoryId+"/"+pageId);
 		 				docInfo.setFirstMenu(menu[2]);
 		 			}
 		 			String githubUrl = EncodeUtil.getURLDecoderString(gitUrl);
 		 			docInfo.setGithubUrl(githubUrl);
 		 			docInfo.setIfRelease("已发布");
 		 			docInfo.setSource("git");
 		 			// 处理菜单名称
 		 			docInfo.setSecondMenu(rows.getJSONObject(i).getString("categoryTitle"));
 		 			// 对于已发布的文档从data中取出目录信息path
 		 	 		JSONArray path = jsonData.getJSONArray("path");
 		 	 		int len = path.length();
 		 	 		if(len == 4) {
 		 	 			docInfo.setThirdMenu(path.get(3).toString());
 		 	 			docInfo.setFourthMenu(path.get(2).toString());
 		 	 			docInfo.setFiveMenu(path.get(1).toString());
 		 	 		}else if(len == 3) {
 		 	 			docInfo.setThirdMenu(path.get(2).toString());
 		 	 			docInfo.setFourthMenu(path.get(1).toString());
 		 	 			docInfo.setFiveMenu(path.get(0).toString());
 		 	 		}else if(len == 2) {
 		 	 			docInfo.setThirdMenu(path.get(1).toString());
 		 	 			docInfo.setFourthMenu(path.get(0).toString());
 		 	 			docInfo.setFiveMenu("");
 		 	 		}if(len == 1) {
 		 	 			docInfo.setThirdMenu(path.get(0).toString());
 		 	 			docInfo.setFourthMenu("");
 		 	 			docInfo.setFiveMenu("");
 		 	 		}
 	 			}
 	 		}else{
 	 			docInfo.setProduct("");
 	 			docInfo.setTitle("");
 	 			docInfo.setLang("");
 	 			docInfo.setWebUrl("");
 	 			docInfo.setSource("");
 	 			docInfo.setReleaseTime(null);
 	 			docInfo.setGithubUrl(gitUrl);
 	 			docInfo.setIfRelease("未发布");
 	 			docInfo.setFirstMenu("");
 	 	 		docInfo.setSecondMenu("");
 	 	 		docInfo.setThirdMenu("");
 	 	 		docInfo.setFourthMenu("");
 	 	 		docInfo.setFiveMenu("");
 	 		}
 		}else {
 			docInfo.setProduct("");
 			docInfo.setTitle("");
 			docInfo.setLang("");
 			docInfo.setWebUrl("");
 			docInfo.setSource("");
 			docInfo.setReleaseTime(null);
 			docInfo.setGithubUrl(gitUrl);
 			docInfo.setIfRelease("未发布");
 			docInfo.setFirstMenu("");
 	 		docInfo.setSecondMenu("");
 	 		docInfo.setThirdMenu("");
 	 		docInfo.setFourthMenu("");
 	 		docInfo.setFiveMenu("");
 		}
		Map<String,Document>map = new HashMap<>();
		 map.put("docInfo",docInfo);
		return map;
	}
	
	
	/**
	 * 查询文档发布信息
	 * 从数据库查询
	 * 单条数据查询
	 */
	@Override
	public Map<String, Document> searchRelease(String gitUrl) {
		// github链接转码
		gitUrl = EncodeUtil.getURLDecoderString(gitUrl);
		//处理gitUrl与数据库字段格式一致
		if(gitUrl.contains("intlcloud-documents")) {
			gitUrl = gitUrl.substring(gitUrl.indexOf("blob")+4);
			gitUrl = "intlcloud-documents"+gitUrl;
		}else if(gitUrl.contains("qcloud-documents")) {
			gitUrl = gitUrl.substring(gitUrl.indexOf("blob")+5);
		}
		// 记录空格未转码的giturl
		String enGitUrl = gitUrl.replace(" ", "%20");
		Document docInfo = new Document();
		if(gitUrl != null && !"".equals(gitUrl)) {
			String query = "select id,lang,title,recentReleaseTime from cDocPageDetailIntl where sourceUrl = \""+gitUrl+"\";";
			List<Map<String,Object>> result = baseDao.findListMapBySql(query);
			if(result.size() == 0) {
				String encodeQuery = "select id,lang,title,recentReleaseTime from cDocPageDetailIntl where sourceUrl = \""+enGitUrl+"\";";
				List<Map<String,Object>> encodeResult = baseDao.findListMapBySql(encodeQuery);
				result = encodeResult;
			}else if(result.size() != 0) {
				for (Map<String, Object> map : result) {
					int pageId = Integer.parseInt(map.get("id").toString());
					
					docInfo.setTitle(map.get("title").toString());
					docInfo.setLang(map.get("lang").toString());
					docInfo.setReleaseTime((Date) map.get("recentReleaseTime"));
					String getDocInfo = "select pid,categoryId,redirectUrl,source from cDocPageBasicIntl where id = "+pageId+";";
					List<Map<String,Object>> result2 = baseDao.findListMapBySql(getDocInfo);
					for (Map<String, Object> map2 : result2) {
						if(map2.get("redirectUrl")!=null && !"".equals(map2.get("redirectUrl"))) {
							String redirectUrl = map2.get("redirectUrl").toString();
							docInfo.setIfRelease("重定向至："+redirectUrl);
						}else {
							docInfo.setIfRelease("已发布");
						}
						int categoryId = Integer.parseInt(map2.get("categoryId").toString());
			 	 		docInfo.setWebUrl("https://intl.cloud.tencent.com/document/product/"+categoryId+"/"+pageId);
			 	 		docInfo.setGithubUrl(gitUrl);
			 			docInfo.setSource(map2.get("source").toString());
			 			int pid = Integer.parseInt(map2.get("pid").toString());
			 			List<String> pathLs = new ArrayList<>();
			 			while(pid!=0) {
			 				String getMenu = "select id,pid,title from cDocPageBasicIntl where id = "+pid+";";
			 				List<Map<String,Object>> rstMenu = baseDao.findListMapBySql(getMenu);
			 				for (Map<String, Object> menu : rstMenu) {
			 					String path = menu.get("title").toString();
			 					pathLs.add(path);
					 	 		pid = Integer.parseInt(menu.get("pid").toString());
			 				}
			 			}
			 			int len = pathLs.size();
			 	 		if(len == 3) {
			 	 			docInfo.setThirdMenu(pathLs.get(2).toString());
			 	 			docInfo.setFourthMenu(pathLs.get(1).toString());
			 	 			docInfo.setFiveMenu(pathLs.get(0).toString());
			 	 		}else if(len == 2) {
			 	 			docInfo.setThirdMenu(pathLs.get(1).toString());
			 	 			docInfo.setFourthMenu(pathLs.get(0).toString());
			 	 			docInfo.setFiveMenu("");
			 	 		}else if(len == 1) {
			 	 			docInfo.setThirdMenu(pathLs.get(0).toString());
			 	 			docInfo.setFourthMenu("");
			 	 			docInfo.setFiveMenu("");
			 	 		}else {
			 	 			docInfo.setThirdMenu("");
			 	 			docInfo.setFourthMenu("");
			 	 			docInfo.setFiveMenu("");
			 	 		}
			 			String getProduct = "select pid,title from cDocCategoryBasicIntl where id = "+categoryId+";";
		 				List<Map<String,Object>> result3 = baseDao.findListMapBySql(getProduct);
		 				for (Map<String, Object> map3 : result3) {
		 					docInfo.setProduct(map3.get("title").toString());
		 					docInfo.setSecondMenu(map3.get("title").toString());
		 					int pId = Integer.parseInt(map3.get("pid").toString());
		 					while(pId!=0) {
		 						String getCate = "select pid,title from cDocCategoryBasicIntl where id ="+pId+";";
		 						List<Map<String,Object>> result4 = baseDao.findListMapBySql(getCate);
				 				for (Map<String, Object> map4 : result4) {
				 					docInfo.setFirstMenu(map4.get("title").toString());
				 					pId = Integer.parseInt(map4.get("pid").toString());
				 				}
		 					}
		 				}
					}
				}
			}else{
				String queryCh = "select id,lang,title,recentReleaseTime from cDocPageDetail where sourceUrl = \""+gitUrl+"\";";
				List<Map<String,Object>> resIntl = baseDao.findListMapBySql(queryCh);
				if(resIntl.size() == 0) {
					String encodeQuery = "select id,lang,title,recentReleaseTime from cDocPageDetailIntl where sourceUrl = \""+enGitUrl+"\";";
					List<Map<String,Object>> encodeResult = baseDao.findListMapBySql(encodeQuery);
					resIntl = encodeResult;
				}else if(resIntl.size() != 0) {
					for (Map<String, Object> map : resIntl) {
						int pageId = Integer.parseInt(map.get("id").toString());
						docInfo.setTitle(map.get("title").toString());
						docInfo.setLang(map.get("lang").toString());
						docInfo.setReleaseTime((Date) map.get("recentReleaseTime"));
						String getDocInfo = "select pid,categoryId,redirectUrl,source from cDocPageBasic where id = "+pageId+";";
						List<Map<String,Object>> result2 = baseDao.findListMapBySql(getDocInfo);
						for (Map<String, Object> map2 : result2) {
							if(map2.get("redirectUrl")!=null && !"".equals(map2.get("redirectUrl"))) {
								String redirectUrl = map2.get("redirectUrl").toString();
								docInfo.setIfRelease("重定向至："+redirectUrl);
							}else {
								docInfo.setIfRelease("已发布");
							}
							int categoryId = Integer.parseInt(map2.get("categoryId").toString());
				 	 		docInfo.setWebUrl("https://cloud.tencent.com/document/product/"+categoryId+"/"+pageId);
				 	 		docInfo.setGithubUrl(gitUrl);
				 			docInfo.setSource(map2.get("source").toString());
				 			int pid = Integer.parseInt(map2.get("pid").toString());
				 			
				 			List<String> pathLs = new ArrayList<>();
				 			while(pid!=0) {
				 				String getMenu = "select id,pid,title from cDocPageBasic where id = "+pid+";";
				 				List<Map<String,Object>> rstMenu = baseDao.findListMapBySql(getMenu);
				 				for (Map<String, Object> menu : rstMenu) {
				 					String path = menu.get("title").toString();
				 					pathLs.add(path);
						 	 		pid = Integer.parseInt(menu.get("pid").toString());
				 				}
				 			}
				 			int len = pathLs.size();
				 	 		if(len == 3) {
				 	 			docInfo.setThirdMenu(pathLs.get(2).toString());
				 	 			docInfo.setFourthMenu(pathLs.get(1).toString());
				 	 			docInfo.setFiveMenu(pathLs.get(0).toString());
				 	 		}else if(len == 2) {
				 	 			docInfo.setThirdMenu(pathLs.get(1).toString());
				 	 			docInfo.setFourthMenu(pathLs.get(0).toString());
				 	 			docInfo.setFiveMenu("");
				 	 		}else if(len == 1) {
				 	 			docInfo.setThirdMenu(pathLs.get(0).toString());
				 	 			docInfo.setFourthMenu("");
				 	 			docInfo.setFiveMenu("");
				 	 		}else {
				 	 			docInfo.setThirdMenu("");
				 	 			docInfo.setFourthMenu("");
				 	 			docInfo.setFiveMenu("");
				 	 		}
				 			String getProduct = "select pid,title from cDocCategoryBasic where id = "+categoryId+";";
			 				List<Map<String,Object>> result3 = baseDao.findListMapBySql(getProduct);
			 				for (Map<String, Object> map3 : result3) {
			 					docInfo.setProduct(map3.get("title").toString());
			 					docInfo.setSecondMenu(map3.get("title").toString());
			 					Integer pId = Integer.parseInt(map3.get("pid").toString());
			 					while(pId!=0) {
			 						String getCate = "select pid,title from cDocCategoryBasic where id ="+pId+";";
			 						List<Map<String,Object>> result4 = baseDao.findListMapBySql(getCate);
					 				for (Map<String, Object> map4 : result4) {
					 					docInfo.setFirstMenu(map4.get("title").toString());
					 					pId = Integer.parseInt(map4.get("pid").toString());
					 				}
			 					}
			 				}
						}
					}
				}else {
					docInfo.setProduct("");
					docInfo.setLang("");
					docInfo.setTitle("");
					docInfo.setIfRelease("未发布");
					docInfo.setReleaseTime(null);
					docInfo.setFirstMenu("");
					docInfo.setSecondMenu("");
		 	 		docInfo.setThirdMenu("");
		 	 		docInfo.setFourthMenu("");
		 	 		docInfo.setFiveMenu("");
		 	 		docInfo.setWebUrl("");
		 	 		docInfo.setGithubUrl(gitUrl);
		 			docInfo.setSource("");
				}
			}
		}	
		Map<String,Document>map = new HashMap<>();
		map.put("docInfo",docInfo);
		return map;
	}
		
	/**
	 * 查询md文档发布信息
	 * 从数据库查询
	 * 遍历md文档目录时使用
	 */
	@Override
	public Document searchReturnRepo(String gitUrl) {
		Document docInfo = new Document();
		if(gitUrl != null && !"".equals(gitUrl)) {
			String query = "select id,lang,title,recentReleaseTime from cDocPageDetailIntl where sourceUrl = \""+gitUrl+"\";";
			List<Map<String,Object>> result = baseDao.findListMapBySql(query);
			if(result.size() != 0) {
				for (Map<String, Object> map : result) {
					int pageId = Integer.parseInt(map.get("id").toString());
					docInfo.setTitle(map.get("title").toString());
					docInfo.setLang(map.get("lang").toString());
					docInfo.setReleaseTime((Date) map.get("recentReleaseTime"));
					String getDocInfo = "select pid,categoryId,redirectUrl,source from cDocPageBasicIntl where id = "+pageId+";";
					List<Map<String,Object>> result2 = baseDao.findListMapBySql(getDocInfo);
					for (Map<String, Object> map2 : result2) {
						if(map2.get("redirectUrl")!=null && !"".equals(map2.get("redirectUrl"))) {
							String redirectUrl = map2.get("redirectUrl").toString();
							docInfo.setIfRelease("重定向至："+redirectUrl);
						}else {
							docInfo.setIfRelease("已发布");
						}
						int categoryId = Integer.parseInt(map2.get("categoryId").toString());
			 	 		docInfo.setWebUrl("https://intl.cloud.tencent.com/document/product/"+categoryId+"/"+pageId);
			 	 		docInfo.setGithubUrl(gitUrl);
			 			docInfo.setSource(map2.get("source").toString());
			 			int pid = Integer.parseInt(map2.get("pid").toString());
			 			List<String> pathLs = new ArrayList<>();
			 			while(pid!=0) {
			 				String getMenu = "select id,pid,title from cDocPageBasicIntl where id = "+pid+";";
			 				List<Map<String,Object>> rstMenu = baseDao.findListMapBySql(getMenu);
			 				for (Map<String, Object> menu : rstMenu) {
			 					String path = menu.get("title").toString();
			 					pathLs.add(path);
					 	 		pid = Integer.parseInt(menu.get("pid").toString());
			 				}
			 			}
			 			int len = pathLs.size();
			 	 		if(len == 3) {
			 	 			docInfo.setThirdMenu(pathLs.get(2).toString());
			 	 			docInfo.setFourthMenu(pathLs.get(1).toString());
			 	 			docInfo.setFiveMenu(pathLs.get(0).toString());
			 	 		}else if(len == 2) {
			 	 			docInfo.setThirdMenu(pathLs.get(1).toString());
			 	 			docInfo.setFourthMenu(pathLs.get(0).toString());
			 	 			docInfo.setFiveMenu("");
			 	 		}else if(len == 1) {
			 	 			docInfo.setThirdMenu(pathLs.get(0).toString());
			 	 			docInfo.setFourthMenu("");
			 	 			docInfo.setFiveMenu("");
			 	 		}else {
			 	 			docInfo.setThirdMenu("");
			 	 			docInfo.setFourthMenu("");
			 	 			docInfo.setFiveMenu("");
			 	 		}
			 			String getProduct = "select pid,title from cDocCategoryBasicIntl where id = "+categoryId+";";
		 				List<Map<String,Object>> result3 = baseDao.findListMapBySql(getProduct);
		 				for (Map<String, Object> map3 : result3) {
		 					docInfo.setProduct(map3.get("title").toString());
		 					docInfo.setSecondMenu(map3.get("title").toString());
		 					int pId = Integer.parseInt(map3.get("pid").toString());
		 					while(pId!=0) {
		 						String getCate = "select pid,title from cDocCategoryBasicIntl where id ="+pId+";";
		 						List<Map<String,Object>> result4 = baseDao.findListMapBySql(getCate);
				 				for (Map<String, Object> map4 : result4) {
				 					docInfo.setFirstMenu(map4.get("title").toString());
				 					pId = Integer.parseInt(map4.get("pid").toString());
				 				}
		 					}
		 				}
					}
				}
			}else{
				String queryCh = "select id,lang,title,recentReleaseTime from cDocPageDetail where sourceUrl = \""+gitUrl+"\";";
				List<Map<String,Object>> resIntl = baseDao.findListMapBySql(queryCh);
				if(resIntl.size() != 0) {
					for (Map<String, Object> map : resIntl) {
						int pageId = Integer.parseInt(map.get("id").toString());
						docInfo.setTitle(map.get("title").toString());
						docInfo.setLang(map.get("lang").toString());
						docInfo.setReleaseTime((Date) map.get("recentReleaseTime"));
						String getDocInfo = "select pid,categoryId,redirectUrl,source from cDocPageBasic where id = "+pageId+";";
						List<Map<String,Object>> result2 = baseDao.findListMapBySql(getDocInfo);
						for (Map<String, Object> map2 : result2) {
							if(map2.get("redirectUrl")!=null && !"".equals(map2.get("redirectUrl"))) {
								String redirectUrl = map2.get("redirectUrl").toString();
								docInfo.setIfRelease("重定向至："+redirectUrl);
							}else {
								docInfo.setIfRelease("已发布");
							}
							int categoryId = Integer.parseInt(map2.get("categoryId").toString());
				 	 		docInfo.setWebUrl("https://cloud.tencent.com/document/product/"+categoryId+"/"+pageId);
				 	 		docInfo.setGithubUrl(gitUrl);
				 			docInfo.setSource(map2.get("source").toString());
				 			int pid = Integer.parseInt(map2.get("pid").toString());
				 			
				 			List<String> pathLs = new ArrayList<>();
				 			while(pid!=0) {
				 				String getMenu = "select id,pid,title from cDocPageBasic where id = "+pid+";";
				 				List<Map<String,Object>> rstMenu = baseDao.findListMapBySql(getMenu);
				 				for (Map<String, Object> menu : rstMenu) {
				 					String path = menu.get("title").toString();
				 					pathLs.add(path);
						 	 		pid = Integer.parseInt(menu.get("pid").toString());
				 				}
				 			}
				 			int len = pathLs.size();
				 	 		if(len == 3) {
				 	 			docInfo.setThirdMenu(pathLs.get(2).toString());
				 	 			docInfo.setFourthMenu(pathLs.get(1).toString());
				 	 			docInfo.setFiveMenu(pathLs.get(0).toString());
				 	 		}else if(len == 2) {
				 	 			docInfo.setThirdMenu(pathLs.get(1).toString());
				 	 			docInfo.setFourthMenu(pathLs.get(0).toString());
				 	 			docInfo.setFiveMenu("");
				 	 		}else if(len == 1) {
				 	 			docInfo.setThirdMenu(pathLs.get(0).toString());
				 	 			docInfo.setFourthMenu("");
				 	 			docInfo.setFiveMenu("");
				 	 		}else {
				 	 			docInfo.setThirdMenu("");
				 	 			docInfo.setFourthMenu("");
				 	 			docInfo.setFiveMenu("");
				 	 		}
				 			String getProduct = "select pid,title from cDocCategoryBasic where id = "+categoryId+";";
			 				List<Map<String,Object>> result3 = baseDao.findListMapBySql(getProduct);
			 				for (Map<String, Object> map3 : result3) {
			 					docInfo.setProduct(map3.get("title").toString());
			 					docInfo.setSecondMenu(map3.get("title").toString());
			 					Integer pId = Integer.parseInt(map3.get("pid").toString());
			 					while(pId!=0) {
			 						String getCate = "select pid,title from cDocCategoryBasic where id ="+pId+";";
			 						List<Map<String,Object>> result4 = baseDao.findListMapBySql(getCate);
					 				for (Map<String, Object> map4 : result4) {
					 					docInfo.setFirstMenu(map4.get("title").toString());
					 					pId = Integer.parseInt(map4.get("pid").toString());
					 				}
			 					}
			 				}
						}
					}
				}else {
					docInfo.setProduct("");
					docInfo.setLang("");
					docInfo.setTitle("");
					docInfo.setIfRelease("未发布");
					docInfo.setReleaseTime(null);
					docInfo.setFirstMenu("");
					docInfo.setSecondMenu("");
		 	 		docInfo.setThirdMenu("");
		 	 		docInfo.setFourthMenu("");
		 	 		docInfo.setFiveMenu("");
		 	 		docInfo.setWebUrl("");
		 	 		docInfo.setGithubUrl(gitUrl);
		 			docInfo.setSource("");
				}
			}
		}	
		return docInfo;
	}
	
}
