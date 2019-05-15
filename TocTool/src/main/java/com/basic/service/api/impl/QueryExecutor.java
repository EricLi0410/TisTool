package com.basic.service.api.impl;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.eclipse.jgit.api.RebaseResult;
import com.basic.util.FileUtil;
import com.basic.util.JGitUtil;

/**
 * 定时线程，每1小时执行一次
 * 校验服务器上git仓库
 * 导出md文件映射信息为excel表存到服务器
 * @author v_qqingmei
 *
 */
public class QueryExecutor{
	
/*	public static void main(String[] args) {
        ScheduledThreadPoolExecutor executorService = new ScheduledThreadPoolExecutor(2);
        IntlTask task1 = new IntlTask();
        ChTask task2 = new ChTask();
        //启动后开始执行任务，以后每隔1小时执行一次
        executorService.scheduleWithFixedDelay(task1, 0, 60,TimeUnit.MINUTES);
        executorService.scheduleWithFixedDelay(task2, 0, 60,TimeUnit.MINUTES);
    }
 */
	/**
	 * 创建线程
	 * 校验国际站git仓库，遍历md文件查询发布信息，并导出为excel
	 * @author v_qqingmei
	 *
	 */
    private static class IntlTask implements Runnable{
        @Override
        public void run() { 
            //捕获所有的异常，保证定时任务能够继续执行
            try{
                System.out.println("国际站线程：任务开始...");
                System.out.println("校验本地git仓库");
                String gitRepo = "https://github.com/tencentyun/intlcloud-documents.git";
                String filePath = "E:\\apache-tomcat_7.0.90\\webapps\\intlcloud-documents";
                String sevrPathTmp = "E:\\apache-tomcat_7.0.90\\webapps\\gitMappingIntl_tmp.xls";
                String sevrPath = "E:\\apache-tomcat_7.0.90\\webapps\\gitMappingIntl.xls";
                RebaseResult rr = JGitUtil.pullWithRebase(gitRepo,filePath);
                System.out.println(rr.getStatus());
                if(rr.getStatus().equals(RebaseResult.Status.UP_TO_DATE)) {
                	System.out.println("当前git已是最新版本,不需要遍历查询！");
	            }else {
	            	System.out.println("遍历文件，查询发布信息...");
	            	FileUtil.visitor(filePath, sevrPathTmp);
	            	System.out.println("复制临时文件为正式文件...");
	            	FileUtil.copyFile(sevrPathTmp, sevrPath);
	            }
                System.out.println("国际站线程：任务结束...");
            }catch (Throwable e) {
                System.out.println(e.getMessage());
            }
        }
    }
    
    /**
     * 创建线程
	 * 校验中国站git仓库，遍历md文件查询发布信息，并导出为excel
     * @author v_qqingmei
     *
     */
    private static class ChTask implements Runnable{
        @Override
        public void run() { 
            //捕获所有的异常，保证定时任务能够继续执行
            try{
                System.out.println("中国站线程：任务开始...");
                System.out.println("校验本地git仓库");
                String gitRepo = "https://github.com/tencentyun/qcloud-documents.git";
                String filePath = "E:\\apache-tomcat_7.0.90\\webapps\\qcloud-documents";
                String sevrPathTmp = "E:\\apache-tomcat_7.0.90\\webapps\\gitMappingCh_tmp.xls";
                String sevrPath = "E:\\apache-tomcat_7.0.90\\webapps\\gitMappingCh.xls";
                RebaseResult rr = JGitUtil.pullWithRebase(gitRepo,filePath);
                System.out.println(rr.getStatus());
                if(rr.getStatus().equals(RebaseResult.Status.UP_TO_DATE)) {
                	System.out.println("当前git已是最新版本,不需要遍历查询！");
	            }else {
	            	System.out.println("遍历文件，查询发布信息...");
	            	FileUtil.visitor(filePath, sevrPathTmp);
	            	System.out.println("复制临时文件为正式文件...");
	            	FileUtil.copyFile(sevrPathTmp, sevrPath);
	            }
                System.out.println("中国站线程：任务结束...");
            }catch (Throwable e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
