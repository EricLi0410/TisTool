package com.basic.service.api.impl;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.basic.dao.api.BaseDao;
import com.basic.entity.api.Document;
import com.basic.service.api.ExportAllService;
import com.basic.util.ExportExcelUtil;
import com.basic.util.FileUtil;
import com.basic.util.JGitUtil;

@Service
public class ExportAllServiceImpl implements ExportAllService{
	
	@Resource
	 private BaseDao baseDao;
	
	@Override
	public Boolean exportCh(String path) throws IOException {
		Boolean flag = false;
		List<Document> docLs = new ArrayList<>();
		String queryCategory = "select id,title from cDocCategoryBasic where pid = 0";
		List<Map<String,Object>> categoryLs = baseDao.findListMapBySql(queryCategory);  //查询所有分类
		System.out.println(categoryLs);
		
		for (Map<String, Object> categoryMap : categoryLs) {
			String basicId = categoryMap.get("id").toString();  //文档所属分类Id
			String firstMenu = categoryMap.get("title").toString();  //获取文档所属分类作为一级菜单
			String queryProduct = "select id,title from cDocCategoryBasic where pid = " + basicId;
			List<Map<String,Object>> productLs = baseDao.findListMapBySql(queryProduct);  //查询分类下的产品
			
			for(Map<String, Object> productMap : productLs) {
				int categoryId = Integer.parseInt(productMap.get("id").toString());
				String product = productMap.get("title").toString();  //获取文档产品名称
				String secondMenu = productMap.get("title").toString();  //获取文档所属产品作为二级菜单
				String queryByProduct = "SELECT DISTINCT t2.id as page, t1.pid, t2.lang, t2.title,t2.recentReleaseTime,"
						+ "t2.sourceUrl,t1.source, t1.disable, t1.redirectUrl  "
						+ "from cDocPageBasic t1 , cDocPageDetail t2 , cDocCategoryBasic t3 "
						+ "where t2.id=t1.id and t1.categoryId=t3.id  and t1.type='page' and t1.disable=0 "
						+ "and t1.redirectUrl='' and t1.categoryId="+categoryId ;
				
				List<Map<String,Object>> docInfo = baseDao.findListMapBySql(queryByProduct);  //查询产品下所有文档
				
				for(Map<String, Object> docMap : docInfo) {
					Document docmt = new Document();  //初始化实体类对象
					docmt.setProduct(product);
					docmt.setFirstMenu(firstMenu);
					docmt.setSecondMenu(secondMenu);
					docmt.setTitle(docMap.get("title").toString()); //文档标题
					docmt.setLang(docMap.get("lang").toString());  //文档语言
					docmt.setGithubUrl(docMap.get("sourceUrl").toString()); //github链接
					docmt.setSource(docMap.get("source").toString()); //来源
					int page = Integer.parseInt(docMap.get("page").toString());
					
					docmt.setWebUrl("cloud.tencent.com/document/product/"+categoryId+"/"+page);
					
					Date rrt = (Date) docMap.get("recentReleaseTime");
					if(rrt == null) {
						docmt.setIfRelease("未发布");
					}else{
						docmt.setReleaseTime(rrt);
						String queryStatus = "select inMenu,visible from cDocPageStatus where id ="+page+" and lang = \""+docmt.getLang()+"\";";
						List<Map<String,Object>> statusLs = baseDao.findListMapBySql(queryStatus);
						for(Map<String, Object> statusMap : statusLs) {
							Integer inMenu = (Integer) statusMap.get("inMenu");
							Integer visible = (Integer) statusMap.get("visible");
							if(inMenu == 1 && visible == 1) {
								docmt.setIfRelease("已发布");
								Integer pid = (Integer) docMap.get("pid");   //获取文档父级菜单
								List<String> mList = new ArrayList<>();
								while(pid != 0) {
									String sql2 = "select id,pid,title from cDocPageBasic where id = " + pid;
									List<Map<String,Object>> list = baseDao.findListMapBySql(sql2);
									String menu = "";
									for (Map<String, Object> map : list) {
										menu = map.get("title").toString();
										pid = (Integer) map.get("pid");
									}
									mList.add(menu);
								}
								// 处理菜单名称
								if(mList.size() == 1) {
									docmt.setThirdMenu(mList.get(0));
									docmt.setFourthMenu("");
									docmt.setFiveMenu("");
								}else if(mList.size() == 2) {
									docmt.setFiveMenu("");
									docmt.setFourthMenu(mList.get(0));
									docmt.setThirdMenu(mList.get(1));
								}
								else if(mList.size() == 3) {
									docmt.setFiveMenu(mList.get(0));
									docmt.setFourthMenu(mList.get(1));
									docmt.setThirdMenu(mList.get(2));
								}
							}else if(inMenu == 0 && visible == 1){
								docmt.setIfRelease("不可见");
								docmt.setThirdMenu("");
								docmt.setFourthMenu("");
								docmt.setFiveMenu("");
							}else if(inMenu == 1 && visible == 0){
								docmt.setIfRelease("不可见");
								docmt.setThirdMenu("");
								docmt.setFourthMenu("");
								docmt.setFiveMenu("");
							}else if(inMenu == 0 && visible == 0){
								docmt.setIfRelease("不存在");
								docmt.setThirdMenu("");
								docmt.setFourthMenu("");
								docmt.setFiveMenu("");
							}
						}
					}
					docLs.add(docmt);
				}
				int size = docLs.size();
				if(size != 0) {
					ExportExcelUtil.excelPoi(docLs, flag, path);
				}
				docLs.clear();
				flag = true;
			}
		}
		System.out.println("End of export excel ...");
		return true;
	}
	
	@Override
	public Boolean exportIntl(String path) throws IOException {
		Boolean flag = false;
		List<Document> docLs = new ArrayList<>();
		String queryCategory = "select id,title from cDocCategoryBasicIntl where pid = 0";
		List<Map<String,Object>> categoryLs = baseDao.findListMapBySql(queryCategory);  //查询所有分类
		
		for (Map<String, Object> categoryMap : categoryLs) {
			String basicId = categoryMap.get("id").toString();  //文档所属分类Id
			String firstMenu = categoryMap.get("title").toString();  //获取文档所属分类作为一级菜单
			String queryProduct = "select id,title from cDocCategoryBasicIntl where pid = " + basicId;
			List<Map<String,Object>> productLs = baseDao.findListMapBySql(queryProduct);  //查询分类下的产品
			
			for(Map<String, Object> productMap : productLs) {
				int categoryId = Integer.parseInt(productMap.get("id").toString());
				
				String product = productMap.get("title").toString();  //获取文档产品名称
				String secondMenu = productMap.get("title").toString();  //获取文档所属产品作为二级菜单
				String queryByProduct = "SELECT DISTINCT t2.id as page, t1.pid, t2.lang, t2.title,t2.recentReleaseTime,"
						+ "t2.sourceUrl,t1.source, t1.disable, t1.redirectUrl  "
						+ "from cDocPageBasicIntl t1 , cDocPageDetailIntl t2 , cDocCategoryBasicIntl t3 "
						+ "where t2.id=t1.id and t1.categoryId=t3.id  and t1.type='page' and t1.disable=0 "
						+ "and t1.redirectUrl='' and t1.categoryId="+categoryId ;
				
				List<Map<String,Object>> docInfo = baseDao.findListMapBySql(queryByProduct);  //查询产品下所有文档
				
				for(Map<String, Object> docMap : docInfo) {
					Document docmt = new Document();  //初始化实体类对象
					docmt.setProduct(product);
					docmt.setFirstMenu(firstMenu);
					docmt.setSecondMenu(secondMenu);
					docmt.setTitle(docMap.get("title").toString()); //文档标题
					docmt.setLang(docMap.get("lang").toString());  //文档语言
					docmt.setGithubUrl(docMap.get("sourceUrl").toString()); //github链接
					docmt.setSource(docMap.get("source").toString()); //来源
					int page = Integer.parseInt(docMap.get("page").toString());
					docmt.setWebUrl("intl.cloud.tencent.com/document/product/"+categoryId+"/"+page);
					
					Date rrt = (Date) docMap.get("recentReleaseTime");
					if(rrt == null) {
						docmt.setIfRelease("未发布");
					}else{
						docmt.setReleaseTime(rrt);
						String queryStatus = "select inMenu,visible from cDocPageStatusIntl where id ="+page+" and lang = \""+docmt.getLang()+"\";";
						List<Map<String,Object>> statusLs = baseDao.findListMapBySql(queryStatus);
						for(Map<String, Object> statusMap : statusLs) {
							Integer inMenu = (Integer) statusMap.get("inMenu");
							Integer visible = (Integer) statusMap.get("visible");
							if(inMenu == 1 && visible == 1) {
								docmt.setIfRelease("已发布");
								int pid = Integer.parseInt(docMap.get("pid").toString());   //获取文档父级菜单
								List<String> mList = new ArrayList<>();
								while(pid != 0) {
									String sql2 = "select id,pid,title from cDocPageBasicIntl where id = " + pid;
									List<Map<String,Object>> list = baseDao.findListMapBySql(sql2);
									String menu = "";
									for (Map<String, Object> map : list) {
										menu = map.get("title").toString();
										pid = (Integer) map.get("pid");
									}
									mList.add(menu);
								}
								// 处理菜单名称
								if(mList.size() == 1) {
									docmt.setThirdMenu(mList.get(0));
									docmt.setFourthMenu("");
									docmt.setFiveMenu("");
								}else if(mList.size() == 2) {
									docmt.setFiveMenu("");
									docmt.setFourthMenu(mList.get(0));
									docmt.setThirdMenu(mList.get(1));
								}
								else if(mList.size() == 3) {
									docmt.setFiveMenu(mList.get(0));
									docmt.setFourthMenu(mList.get(1));
									docmt.setThirdMenu(mList.get(2));
								}
							}else if(inMenu == 0 && visible == 1){
								docmt.setIfRelease("不可见");
								docmt.setThirdMenu("");
								docmt.setFourthMenu("");
								docmt.setFiveMenu("");
							}else if(inMenu == 1 && visible == 0){
								docmt.setIfRelease("不可见");
								docmt.setThirdMenu("");
								docmt.setFourthMenu("");
								docmt.setFiveMenu("");
							}else if(inMenu == 0 && visible == 0){
								docmt.setIfRelease("不存在");
								docmt.setThirdMenu("");
								docmt.setFourthMenu("");
								docmt.setFiveMenu("");
							}
						}
					}
					docLs.add(docmt);
				}
				int size = docLs.size();
				if(size != 0) {
					ExportExcelUtil.excelPoi(docLs, flag, path);
				}
				docLs.clear();
				flag = true;
			}
		}
		return true;
	}
	
	
	@Override
	public Boolean exportMd(String url,String filePath, String path) throws IOException {
		// 克隆git仓库
		Boolean flag = JGitUtil.cloneRepository(url,filePath);
		if(flag) {
			//遍历目录，导出excel
			Boolean status = FileUtil.fileVisitor(filePath,path);
			return status;
		}else {
			return false;
		}
	}
	
	@Override
	public Boolean exportGitInfo(String url, String filePath, String path) throws IOException, ParseException {
		// 克隆git仓库
		System.out.println("start clocloneRepository....");
		Boolean flag = JGitUtil.cloneRepository(url,filePath);
		if(flag) {
			//遍历目录，导出excel
			System.out.println("start visitorMd...");
			Boolean status = FileUtil.visitor(filePath, path);   //调用接口查询发布信息
//			Boolean status = FileVisitor.visitorMd(filePath,path);  //从数据库查询发布信息
			return status;
		}else {
			return false;
		}
	}	

}
