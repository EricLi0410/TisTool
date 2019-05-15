package com.basic.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import com.basic.entity.api.Document;
import com.framework.common.HttpUtil;

public class Request {
	 /**
	  * 调用接口，获取gitUrl的发布信息
	  * @param gitUrl
	  * @return
	 * @throws ParseException 
	  */
	 public static Document sendRequest(String gitUrl) throws ParseException{
		 Document docInfo = new Document();
		 
		//获取当前时间戳
 		long time = new Date().getTime();
 		String timeStamp = time + "";
 		timeStamp = timeStamp.substring(0, 10);
 				
 		String host = "http://zyaccountbatch.tencentyun.com/";  //请求域名
//		host = "http://accountbatch.tencentyun.com:50021/";  //请求域名
 		String interfaceBasic = "accountbatch.document.getDocumentPublishInfo"; //国内接口
 		String interfaceInter = "accountbatch.document.getInterDocumentPublishInfo"; //国际接口
 		String interfaceName = "";
 		String sourceUrl = "https://github.com/tencentyun/qcloud-documents/blob/"+gitUrl;
 		System.out.println("sourceUrl:"+sourceUrl);
 		String intl = "intlcloud-documents";
 		if(gitUrl.contains(intl)) {
 			// 调用国际接口，返回国际站信息
 			interfaceName = interfaceInter;
 			docInfo.setLang("en");
 		}else {
 			//调用国内接口，返回中国站信息
 			interfaceName = interfaceBasic;
 			docInfo.setLang("ch");
 		}
 		String url = host + interfaceName;
 		System.out.println("url:"+url);
 		String body="{\n" + 
 				"  \"version\": 1,\n" + 
 				"  \"componentName\": \"MC\",\n" + 
 				"  \"eventId\": 0,\n" + 
 				"  \"timestamp\": "+timeStamp+",\n" + 
 				"  \"interface\": {\n" + 
 				"    \"interfaceName\": \""+interfaceName+"\",\n" + 
 				"    \"para\": {\n" + 
 				"      \"sourceUrl\": \""+sourceUrl+"\"\n" + 
 				"    }\n" + 
 				"  }\n" + 
 				"}";
 		JSONObject jsonObj = new JSONObject(body);
 		String result = HttpUtil.sendPost(url, jsonObj);
 		System.out.println("result:"+result);
 		if(result.length() > 0) {
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
 		 			if("0000-00-00 00:00:00".equals(recentReleaseTime)) {
 		 				docInfo.setReleaseTime(null);
 		 			}else {
	 		 			Timestamp ts = Timestamp.valueOf(recentReleaseTime);
	 		 			System.out.println("时间戳："+ts);
	 		 			Date releaseTime = ts;
	 		 			docInfo.setReleaseTime(releaseTime);
 		 			}
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
 		 			docInfo.setGithubUrl(gitUrl);
 		 			docInfo.setIfRelease("已发布");
 		 			docInfo.setSource("git");
 		 			
 		 			docInfo.setSecondMenu(rows.getJSONObject(i).getString("categoryTitle"));
 		 			// 对于已发布的文档从data中取出目录信息path
 		 	 		JSONArray path = jsonData.getJSONArray("path");
 		 	 		int len = path.length();
 		 	 		if(len == 4) {
 		 	 			docInfo.setThirdMenu(path.get(2).toString());
 		 	 			docInfo.setFourthMenu(path.get(1).toString());
 		 	 			docInfo.setFiveMenu(path.get(0).toString());
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
		return docInfo;
	 }
	
}
