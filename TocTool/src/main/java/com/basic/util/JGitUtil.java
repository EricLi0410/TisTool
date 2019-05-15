package com.basic.util;

import java.io.File;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.RebaseResult;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;

public class JGitUtil {

	// 校验文件夹
	 public static void checkDirectory(File file){ 
		 //若文件夹不存在则创建文件夹
		if(!file.exists()) {
			 file.mkdirs();
		 }else if(file.isDirectory()){
			 file.mkdirs();
			 String[] childFilePath = file.list(); 
			 for (String path:childFilePath){      //遍历删除文件夹内所有内容
				 File childFile= new File(file.getAbsoluteFile()+"/"+path); 
				 checkDirectory(childFile);
				 childFile.delete();
			 } 
		}
	} 
		
	/**
	 * cloneRepository form github
	 * @param url
	 * @param localPath
	 * @return
	 */
	public static Boolean cloneRepository(String url,String filePath){
		try{
			System.out.println("start download......");
			File file=new File(filePath); 
//			checkDirectory(file);  //校验文件夹
			CloneCommand cc = Git.cloneRepository().setURI(url);  //clone github仓库到 本地
			cc.setDirectory(new File(filePath)).call();
			System.out.println("clone success......");
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	public static RebaseResult pullWithRebase(String url,String filePath) {
		File repoDir = new File(filePath);
		File RepoGitDir = new File(repoDir.getAbsolutePath() + "/.git");
	    if (!RepoGitDir.exists()) {
	    	//本地仓库不存在，克隆git到本地
	        System.out.println("Error! Not Exists : " + RepoGitDir.getAbsolutePath());
	        cloneRepository(url,filePath);
	    } else {
	        Repository localRepo = null;
	        try {
	        	//本地仓库存在，执行git pull --rebase
	        	localRepo = new FileRepository(RepoGitDir.getAbsolutePath());
	            Git git = new Git(localRepo);
	            PullCommand pullCmd = git.pull().setRebase(true);
	            PullResult pullRes = pullCmd.call();
	            RebaseResult rr = pullRes.getRebaseResult();
	            System.out.println("Pulled from remote repository to local repository at " + localRepo.getDirectory());
	            return rr;
	        } catch (Exception e) {
	        	System.out.println(e.getMessage() + " : " + RepoGitDir.getAbsolutePath());
	        } finally {
	            if (localRepo != null) {
	            	localRepo.close();
	            }
	        }
	    }
	    System.out.println("Git pull --rebase have done!");
		return null;
	}
	
}

