package com.basic.service.api.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import com.basic.dao.api.BaseDao;
import com.basic.entity.api.Document;
import com.basic.service.api.GetInformationService;
import com.basic.util.ExportExcelUtil;

@Service
public class GetInformationServiceImpl implements GetInformationService{
	
	@Resource
	 private BaseDao baseDao;
	
	/**
	 * 根据条件查询中国站文档发布信息
	 */
	@Override
	public JSONObject searchByCond(String pageNum, String pageSize, String[] params, String[] disable, String[] visible,
			String[] inMenu, String[] sourceUrl, String[] redirectUrl) {
		int page; //当前页数
		try {
            page = Integer.valueOf(pageNum);
        } catch (NumberFormatException e) {
            page = 1;
        }
		int perPage = Integer.valueOf(pageSize);
		int start;
		start = (page-1) * perPage;
		
		JSONObject json = new JSONObject();
		List<Document> docLs = new ArrayList<>();
		
		String queryByProduct = "SELECT DISTINCT t4.title as firstMenu,t1.categoryId,t3.title as product, t2.id as page, t1.pid, t2.lang, t2.title, "
				+ " t2.recentReleaseTime,t2.sourceUrl, t1.source, t1.disable, t1.redirectUrl, t5.inMenu, t5.visible "
				+ " from cDocPageBasic t1 , cDocPageDetail t2 , cDocCategoryBasic t3, cDocCategoryBasic t4, cDocPageStatus t5 "
				+ " where t2.id=t1.id and t1.categoryId=t3.id and t4.id=t3.pid and t5.id=t2.id and t1.type='page'" ;
				
		String disableCd = "";
		String visibleCd = "";
		String inmenuCd = "";
		String sourceUrlCd = "";
		String redirectUrlCd = "";
				
		if(disable != null) {
			if(disable.length == 1) {
				disableCd = " and t1.disable = " + disable[0];
			}
		}
		if(visible != null) {
			if(visible.length == 1) {
				visibleCd = " and t5.visible = " + visible[0];
			}
		}
		if(inMenu != null) {
			if(inMenu.length == 1) {
				inmenuCd = " and t5.inMenu = " + inMenu[0];
			}
		}
		if(sourceUrl != null) {
			if("1".equals(sourceUrl[0])) {
				sourceUrlCd = " and t2.sourceUrl = \"\" ";
			}else if("0".equals(redirectUrl[0])) {
				sourceUrlCd = " and t2.sourceUrl != \"\" ";
			}
		}
		if(redirectUrl != null) {
			if("1".equals(redirectUrl[0])) {
				redirectUrlCd = " and t1.redirectUrl = \"\" ";
			}else if("0".equals(redirectUrl[0])) {
				redirectUrlCd = " and t1.redirectUrl != \"\" ";
			}
		}
		String count = "SELECT COUNT(DISTINCT t1.pid, t1.categoryId, t2.title, t3.title, t2.id, t2.lang, t2.recentReleaseTime, t2.sourceUrl, t1.source,"
				+ " t4.title, t1.disable, t1.redirectUrl, t5.inMenu, t5.visible) from cDocPageBasic t1 , cDocPageDetail t2 , cDocCategoryBasic t3, cDocCategoryBasic t4, cDocPageStatus t5 "
				+ " where t2.id=t1.id and t1.categoryId=t3.id and t4.id=t3.pid and t5.id=t2.id and t1.type='page'";
		count = count+ disableCd + visibleCd + inmenuCd + sourceUrlCd + redirectUrlCd;
		int sum  = baseDao.findBySqlForCount(count);
		int totalPage = (sum+perPage-1)/perPage;
		
		String limit = " limit "+start+" , "+perPage+";";
		queryByProduct = queryByProduct + disableCd + visibleCd + inmenuCd + sourceUrlCd + redirectUrlCd + limit;
		List<Map<String,Object>> docInfo = baseDao.findListMapBySql(queryByProduct);  //查询产品下所有文档
		
		String categoryId = "";
		String pageId = "";
		Integer tempId = null;
		String menu = "";
		
		for(Map<String, Object> docMap : docInfo) {
			Document docmt = new Document();  //初始化实体类对象
			docmt.setProduct(docMap.get("product").toString());
			docmt.setTitle(docMap.get("title").toString());
			docmt.setLang(docMap.get("lang").toString());
			docmt.setReleaseTime((Date) docMap.get("recentReleaseTime"));
			docmt.setFirstMenu(docMap.get("firstMenu").toString());
			docmt.setSecondMenu(docMap.get("product").toString());
			categoryId=docMap.get("categoryId").toString();
			pageId=docMap.get("page").toString();
			docmt.setWebUrl("master/product/"+categoryId+"/"+pageId);
			docmt.setGithubUrl(docMap.get("sourceUrl").toString());
			docmt.setSource(docMap.get("source").toString());
			tempId = Integer.parseInt(docMap.get("pid").toString());
			
			docmt.setWebUrl("document/product/"+categoryId+"/"+pageId);
			
			int dis = Integer.parseInt(docMap.get("disable").toString());
			int visi = Integer.parseInt(docMap.get("visible").toString());
			int inmenu = Integer.parseInt(docMap.get("inMenu").toString());
			String seUrl = docMap.get("inMenu").toString();
			String reUrl = docMap.get("inMenu").toString();
			
			Date rrt = (Date) docMap.get("recentReleaseTime");  //文档发布时间
			if(rrt == null) {
				docmt.setIfRelease("未发布");
				docmt.setReleaseTime(null);
				docmt.setThirdMenu("");
				docmt.setFourthMenu("");
				docmt.setFiveMenu("");
			}else {
				docmt.setReleaseTime(rrt);
			}
			
			if(dis == 0) {
				if(inmenu==1 && visi==1) {
					docmt.setIfRelease("已发布");
					tempId = Integer.parseInt(docMap.get("pid").toString());
					List<String> mList = new ArrayList<>();
					while(tempId !=0 ) {
						//获取菜单名
						//获取父级菜单
						System.out.println("获取父级菜单..");
						String sql2 = "select id,pid,title from cDocPageBasic where id="+tempId;
						List<Map<String,Object>> list = baseDao.findListMapBySql(sql2);
						for(Map<String,Object> map : list) {
							menu = map.get("title").toString();
							tempId = Integer.parseInt(map.get("pid").toString());
							System.out.println("pid:"+tempId);
						}
						mList.add(menu);
						//处理菜单名
						if(mList.size()==1) {
							docmt.setThirdMenu(mList.get(0));
							docmt.setFourthMenu("");
							docmt.setFiveMenu("");
						}else if(mList.size() == 2) {
					        docmt.setFiveMenu("");
					        docmt.setFourthMenu(mList.get(0));
					        docmt.setThirdMenu(mList.get(1));
					    }else if(mList.size() == 3) {
					        docmt.setFiveMenu(mList.get(0));
					        docmt.setFourthMenu(mList.get(1));
					        docmt.setThirdMenu(mList.get(2));
					    }
					}
				}else if(visi == 0 && inmenu == 1 && !"".equals(seUrl)){
			         docmt.setIfRelease("弃用（未发布）");
			         docmt.setThirdMenu("");
			         docmt.setFourthMenu("");
			         docmt.setFiveMenu("");
			    }else if(visi == 0 && inmenu == 1 && "".equals(seUrl)){
			         docmt.setIfRelease("弃用（404）");
			         docmt.setThirdMenu("");
			         docmt.setFourthMenu("");
			         docmt.setFiveMenu("");
			    }else if(visi == 1 && inmenu == 0){
			         docmt.setIfRelease("已发布（隐藏）");
			         docmt.setThirdMenu("");
			         docmt.setFourthMenu("");
			         docmt.setFiveMenu("");
			    }else if(visi == 0 && inmenu == 0){
			         docmt.setIfRelease("不存在");
			         docmt.setThirdMenu("");
			         docmt.setFourthMenu("");
			         docmt.setFiveMenu("");
			    }
				}else if(dis == 1) {
					if(visi == 1 && inmenu == 1 && !"".equals(reUrl)) {
						docmt.setIfRelease("弃用（重定向）");
						docmt.setThirdMenu("");
						docmt.setFourthMenu("");
						docmt.setFiveMenu("");
					}else if(visi == 1 && inmenu == 1 && "".equals(reUrl)) {
						docmt.setIfRelease("弃用（文案提示）");
						docmt.setThirdMenu("");
						docmt.setFourthMenu("");
						docmt.setFiveMenu("");
					}else if(visi  == 1 && inmenu == 1 && "1".equals(reUrl)){
						docmt.setIfRelease("弃用（404）");
						docmt.setThirdMenu("");
						docmt.setFourthMenu("");
						docmt.setFiveMenu("");
					}else if(visi == 0 && inmenu == 1){
						docmt.setIfRelease("弃用（未发布）");
						docmt.setThirdMenu("");
						docmt.setFourthMenu("");
						docmt.setFiveMenu("");
					}else if(visi == 1 && inmenu == 0 && !"".equals(reUrl)){
						docmt.setIfRelease("弃用（重定向）");
						docmt.setThirdMenu("");
						docmt.setFourthMenu("");
						docmt.setFiveMenu("");
					}else if(visi == 1 && inmenu == 0 && "1".equals(reUrl)){
						docmt.setIfRelease("弃用（404）");
						docmt.setThirdMenu("");
						docmt.setFourthMenu("");
						docmt.setFiveMenu("");
					}else if(visi == 1 && inmenu == 0 && "".equals(reUrl)){
						docmt.setIfRelease("弃用（文案提示）");
						docmt.setThirdMenu("");
						docmt.setFourthMenu("");
						docmt.setFiveMenu("");
					}else if(visi == 0 && inmenu == 0){
						docmt.setIfRelease("不存在");
						docmt.setThirdMenu("");
						docmt.setFourthMenu("");
						docmt.setFiveMenu("");
					}
				}
				docLs.add(docmt);
		}
		json.put("docLs", docLs);
		json.put("count", sum);
		json.put("totalPage", totalPage);
		return json;
	}

	/**
	 * 根据条件查询中国站文档并导出为excel
	 */
	@Override
	public Boolean exportByCond(String para, String website, String dsab, String vsb,
			String imu, String sourl, String reurl, String path) { 
		//处理条件参数
		String[] params = null;
		String[] disable = null;
		String[] visible = null; 
		String[] inMenu = null;
		String[] sourceUrl = null;
		String[] redirectUrl = null;
		if(para.length()>0) {
			params = para.split(",");
		}
		if(dsab.length()>0) {
			disable = dsab.split(",");
		}
		if(vsb.length()>0) {
			visible = vsb.split(",");
		}
		if(imu.length()>0) {
			inMenu = imu.split(",");
		}
		if(sourl.length()>0) {
			sourceUrl = sourl.split(",");
		}
		if(reurl.length()>0) {
			redirectUrl = reurl.split(",");
		}
		// 根据站点选择查询的数据库表
		String pageBasicTb = "";
		String pageDetailTb = "";
		String categoryBasicTb = "";
		String pageStatusTb = "";
		if("china".equals(website)) {
			pageBasicTb = "cDocPageBasic";
		}else if("intl".equals(website)){
			pageBasicTb = "cDocPageBasicIntl";
		}
		
		if("china".equals(website)) {
			pageDetailTb = "cDocPageDetail";
		}else if("intl".equals(website)){
			pageDetailTb = "cDocPageDetailIntl";
		}
		
		if("china".equals(website)) {
			categoryBasicTb = "cDocCategoryBasic";
		}else if("intl".equals(website)){
			categoryBasicTb = "cDocCategoryBasicIntl";
		}
		
		if("china".equals(website)) {
			pageStatusTb = "cDocPageStatus";
		}else if("intl".equals(website)){
			pageStatusTb = "cDocPageStatusIntl";
		}
		
		Boolean flag = false;
		List<Document> docLs = new ArrayList<>();
		
		String queryCategory = "select id,title from "+categoryBasicTb+" where pid = 0";
		List<Map<String,Object>> categoryLs = baseDao.findListMapBySql(queryCategory);  //查询所有分类
		System.out.println(categoryLs);
		
		for (Map<String, Object> categoryMap : categoryLs) {
			String basicId = categoryMap.get("id").toString();  //文档所属分类Id
			String queryProduct = "select id,title from "+categoryBasicTb+" where pid = " + basicId;
			List<Map<String,Object>> productLs = baseDao.findListMapBySql(queryProduct);  //查询分类下的产品
			System.out.println(productLs);
			
			for(Map<String, Object> productMap : productLs) {
				int categoryId = Integer.parseInt(productMap.get("id").toString());
				String queryByProduct = "SELECT DISTINCT t4.title as firstMenu,t1.categoryId,t3.title as product, t2.id as page, t1.pid, t2.lang, t2.title, "
						+ " t2.recentReleaseTime,t2.sourceUrl, t1.source, t1.disable, t1.redirectUrl, t5.inMenu, t5.visible "
						+ " from "+pageBasicTb+" t1 , "+pageDetailTb+" t2 , "+categoryBasicTb+" t3, "+categoryBasicTb+" t4, "+pageStatusTb+" t5 "
						+ " where t2.id=t1.id and t1.categoryId=t3.id and t4.id=t3.pid and t5.id=t2.id and t1.type='page' and t1.categoryId=" + categoryId;
				String disableCd = "";
				String visibleCd = "";
				String inmenuCd = "";
				String sourceUrlCd = "";
				String redirectUrlCd = "";
				
				if(disable != null) {
					if(disable.length == 1) {
						disableCd = " and t1.disable = " + disable[0];
					}
				}
				if(visible != null) {
					if(visible.length == 1) {
						visibleCd = " and t5.visible = " + visible[0];
					}
				}
				if(inMenu != null) {
					if(inMenu.length == 1) {
						inmenuCd = " and t5.inMenu = " + inMenu[0];
					}
				}
				if(sourceUrl != null) {
					if("1".equals(sourceUrl[0])) {
						sourceUrlCd = " and t2.sourceUrl = \"\" ";
					}else if("0".equals(redirectUrl[0])) {
						sourceUrlCd = " and t2.sourceUrl != \"\" ";
					}
				}
				if(redirectUrl != null) {
					if("1".equals(redirectUrl[0])) {
						redirectUrlCd = " and t1.redirectUrl = \"\" ";
					}else if("0".equals(redirectUrl[0])) {
						redirectUrlCd = " and t1.redirectUrl != \"\" ";
					}
				}
				queryByProduct = queryByProduct + disableCd + visibleCd + inmenuCd + sourceUrlCd + redirectUrlCd + " order by t1.categoryId";
				List<Map<String,Object>> docInfo = baseDao.findListMapBySql(queryByProduct);  //查询产品下所有文档
				
				String pageId = "";
				Integer tempId = null;
				String menu = "";
				
				for(Map<String, Object> docMap : docInfo) {
					Document docmt = new Document();  //初始化实体类对象
					docmt.setProduct(docMap.get("product").toString());
					docmt.setTitle(docMap.get("title").toString());
					docmt.setLang(docMap.get("lang").toString());
					docmt.setReleaseTime((Date) docMap.get("recentReleaseTime"));
					docmt.setFirstMenu(docMap.get("firstMenu").toString());
					docmt.setSecondMenu(docMap.get("product").toString());
					pageId=docMap.get("page").toString();
					docmt.setWebUrl("master/product/"+categoryId+"/"+pageId);
					docmt.setGithubUrl(docMap.get("sourceUrl").toString());
					docmt.setSource(docMap.get("source").toString());
					tempId = Integer.parseInt(docMap.get("pid").toString());
					
					docmt.setWebUrl("document/product/"+categoryId+"/"+pageId);
					
					int dis = Integer.parseInt(docMap.get("disable").toString());
					int visi = Integer.parseInt(docMap.get("visible").toString());
					int inmenu = Integer.parseInt(docMap.get("inMenu").toString());
					String seUrl = docMap.get("inMenu").toString();
					String reUrl = docMap.get("inMenu").toString();
					
					Date rrt = (Date) docMap.get("recentReleaseTime");  //文档发布时间
					if(rrt == null) {
						docmt.setIfRelease("未发布");
						docmt.setReleaseTime(null);
						docmt.setThirdMenu("");
						docmt.setFourthMenu("");
						docmt.setFiveMenu("");
					}else {
						docmt.setReleaseTime(rrt);
					}
					
					if(dis == 0) {
						if(inmenu==1 && visi==1) {
							docmt.setIfRelease("已发布");
							tempId = Integer.parseInt(docMap.get("pid").toString());
							List<String> mList = new ArrayList<>();
							while(tempId !=0 ) {
								//获取菜单名
								//获取父级菜单
								System.out.println("获取父级菜单..");
								String sql2 = "select id,pid,title from "+pageBasicTb+" where id="+tempId;
								List<Map<String,Object>> list = baseDao.findListMapBySql(sql2);
								for(Map<String,Object> map : list) {
									menu = map.get("title").toString();
									tempId = Integer.parseInt(map.get("pid").toString());
									System.out.println("pid:"+tempId);
								}
								mList.add(menu);
								//处理菜单名
								if(mList.size()==1) {
									docmt.setThirdMenu(mList.get(0));
									docmt.setFourthMenu("");
									docmt.setFiveMenu("");
								}else if(mList.size() == 2) {
							        docmt.setFiveMenu("");
							        docmt.setFourthMenu(mList.get(0));
							        docmt.setThirdMenu(mList.get(1));
							    }else if(mList.size() == 3) {
							        docmt.setFiveMenu(mList.get(0));
							        docmt.setFourthMenu(mList.get(1));
							        docmt.setThirdMenu(mList.get(2));
							    }
							}
						}else if(visi == 0 && inmenu == 1 && !"".equals(seUrl)){
					         docmt.setIfRelease("弃用（未发布）");
					         docmt.setThirdMenu("");
					         docmt.setFourthMenu("");
					         docmt.setFiveMenu("");
					    }else if(visi == 0 && inmenu == 1 && "".equals(seUrl)){
					         docmt.setIfRelease("弃用（404）");
					         docmt.setThirdMenu("");
					         docmt.setFourthMenu("");
					         docmt.setFiveMenu("");
					    }else if(visi == 1 && inmenu == 0){
					         docmt.setIfRelease("已发布（隐藏）");
					         docmt.setThirdMenu("");
					         docmt.setFourthMenu("");
					         docmt.setFiveMenu("");
					    }else if(visi == 0 && inmenu == 0){
					         docmt.setIfRelease("不存在");
					         docmt.setThirdMenu("");
					         docmt.setFourthMenu("");
					         docmt.setFiveMenu("");
					    }
					}else if(dis == 1) {
						if(visi == 1 && inmenu == 1 && !"".equals(reUrl)) {
							docmt.setIfRelease("弃用（重定向）");
							docmt.setThirdMenu("");
							docmt.setFourthMenu("");
							docmt.setFiveMenu("");
						}else if(visi == 1 && inmenu == 1 && "".equals(reUrl)) {
							docmt.setIfRelease("弃用（文案提示）");
							docmt.setThirdMenu("");
							docmt.setFourthMenu("");
							docmt.setFiveMenu("");
						}else if(visi  == 1 && inmenu == 1 && "1".equals(reUrl)){
							docmt.setIfRelease("弃用（404）");
							docmt.setThirdMenu("");
							docmt.setFourthMenu("");
							docmt.setFiveMenu("");
						}else if(visi == 0 && inmenu == 1){
							docmt.setIfRelease("弃用（未发布）");
							docmt.setThirdMenu("");
							docmt.setFourthMenu("");
							docmt.setFiveMenu("");
						}else if(visi == 1 && inmenu == 0 && !"".equals(reUrl)){
							docmt.setIfRelease("弃用（重定向）");
							docmt.setThirdMenu("");
							docmt.setFourthMenu("");
							docmt.setFiveMenu("");
						}else if(visi == 1 && inmenu == 0 && "1".equals(reUrl)){
							docmt.setIfRelease("弃用（404）");
							docmt.setThirdMenu("");
							docmt.setFourthMenu("");
							docmt.setFiveMenu("");
						}else if(visi == 1 && inmenu == 0 && "".equals(reUrl)){
							docmt.setIfRelease("弃用（文案提示）");
							docmt.setThirdMenu("");
							docmt.setFourthMenu("");
							docmt.setFiveMenu("");
						}else if(visi == 0 && inmenu == 0){
							docmt.setIfRelease("不存在");
							docmt.setThirdMenu("");
							docmt.setFourthMenu("");
							docmt.setFiveMenu("");
						}
					}
					docLs.add(docmt);
				}
				int size = docLs.size();
				if(size != 0) {
					try {
						ExportExcelUtil.exportByCond(docLs, params, flag, path);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				docLs.clear();
				flag = true;
			}
		}
		System.out.println("End of export excel ...");
		return true;
	}

	
}
