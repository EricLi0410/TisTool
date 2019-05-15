package com.basic.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.eclipse.jgit.api.RebaseResult;

import com.basic.util.FileUtil;
import com.basic.util.JGitUtil;

public class ScheduledExecutorController implements ServletContextListener{

	private String path = System.getProperty("catalina.home")+"/tencentDoc";
	
	private ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
	
	public void threadChGit(){
		executor.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				System.out.println("线程1：中国站");
			/*	String url = "https://github.com/tencentyun/qcloud-documents";
				String filePath = path + "\\qcloud-documents";
				String tmpPath = path + "\\gitMappingCh_tmp.xls";
				String sevrPath = path + "\\gitMappingCh.xls";
				RebaseResult rr = JGitUtil.pullWithRebase(url, filePath);
				System.out.println("pull rebase result:"+rr);
				if(!"UP_TO_DATE".equals(rr.getStatus().toString())) {
					try {
						FileUtil.visitor(filePath, tmpPath);
						if(true) {
							FileUtil.copyFile(tmpPath, sevrPath);
						}
					} catch (IOException | ParseException e) {
						e.printStackTrace();
					}
				}else {
					System.out.println("当前已是最新！");
				}*/
			}
		}, 5, 60, TimeUnit.MINUTES);//延迟1秒后执行，执行周期为5s执行依次
	}
	
	public void threadIntlGit(){
		executor.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				System.out.println("线程2：国际站");
		/*		String url = "https://github.com/tencentyun/intlqcloud-documents";
				String filePath = path + "\\intlcloud-documents";
				String tmpPath = path + "\\gitMappingIntl_tmp.xls";
				String sevrPath = path + "\\gitMappingIntl.xls";
				RebaseResult rr = JGitUtil.pullWithRebase(url, filePath);
				System.out.println("pull rebase result:"+rr.getStatus().toString());
				if(!"UP_TO_DATE".equals(rr.getStatus().toString())) {
					try {
						FileUtil.visitor(filePath, tmpPath);
						if(true) {
							FileUtil.copyFile(tmpPath, sevrPath);
						}
					} catch (IOException | ParseException e) {
						e.printStackTrace();
					}
				}else {
					System.out.println("当前已是最新！");
				}*/
			}
		},  5, 60, TimeUnit.MINUTES);//延迟1秒后执行，执行周期为5s执行依次
	
	}
	
	
	public void contextInitialized(ServletContextEvent arg0) {
		try {
			ScheduledExecutorController schedule = new ScheduledExecutorController();
			schedule.threadIntlGit();
			schedule.threadChGit();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			executor.shutdown();
		}
	}
	
}

