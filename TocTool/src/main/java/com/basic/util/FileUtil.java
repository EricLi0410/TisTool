package com.basic.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;  
import java.nio.file.FileVisitResult;  
import java.nio.file.Files;  
import java.nio.file.Path;  
import java.nio.file.Paths;  
import java.nio.file.SimpleFileVisitor;  
import java.nio.file.attribute.BasicFileAttributes;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.basic.entity.api.Document;
import com.basic.service.api.impl.SearchInfoServiceImpl;
  
/**
 * Traverse the directory to get the specified suffix file
 * @author v_qqingmei
 *
 */
public class FileUtil{  
	/**
	 * 导出md文件名称到excel
	 * @param filePath 服务器上文件路径
	 * @param localPath 下载到本地路径
	 * @return
	 * @throws IOException
	 */
	 public static Boolean fileVisitor(String filePath,String localPath) throws IOException {
    	Boolean flag = false;
        Path startingDir = Paths.get(filePath);  
        FindFileVisitor findJavaVisitor = new FindFileVisitor(".md");  
        
        Files.walkFileTree(startingDir, findJavaVisitor);  
        List<String> gitUrl = new ArrayList<String>();
        for (String name : findJavaVisitor.getFilenameList()) { 
        	String fileName = filePath.substring(filePath.lastIndexOf("\\") + 1);
        	String prefix = "";
        	if("intlcloud-documents".equals(fileName)) {
        		prefix = "intlcloud-documents/master";
        	}else {
        		prefix = "master";
        	}
        	String newName = name.replace(filePath,prefix).replace("\\", "/");
        	if(gitUrl.size() < 29) {
        		gitUrl.add(newName);
        	}else if(gitUrl.size() == 29){
        		gitUrl.add(newName);
        		// 调用导出excel
        		ExportMDUtil.excelPoi(gitUrl,flag,localPath);
        		gitUrl.clear();
        		flag = true;
        	}
        }  
        return flag;
    }  
	 
	 
	 /**
	  * 遍历md文件,查询发布信息并导出为excel
	  * 调用查询接口查询
	  * @param filePath
	  * @param localPath
	  * @return
	  * @throws IOException
	 * @throws ParseException 
	  */
	 public static Boolean visitor(String filePath, String localPath) throws IOException, ParseException {
	    	Boolean flag = false;
	        Path startingDir = Paths.get(filePath);  
	        FindFileVisitor findJavaVisitor = new FindFileVisitor(".md");  
	        Files.walkFileTree(startingDir, findJavaVisitor);  
	        List<Document> docLs = new ArrayList<>();
	        Document docInfo = new Document();
	        for (String name : findJavaVisitor.getFilenameList()) {  
	        	String fileName = filePath.substring(filePath.lastIndexOf("\\")+1);
	        	System.out.println("fileName:"+fileName);
	        	String prefix = "";
	        	if("intlcloud-documents".equals(fileName)) {
	        		prefix = "intlcloud-documents/master";
	        	}else {
	        		prefix = "master";
	        	}
	        	String newName = name.replace(filePath, prefix).replace("\\", "/");
	        	docInfo = Request.sendRequest(newName);  //调用接口，查询gitUrl发布信息
	        	if(docLs.size() < 29) {
	        		docLs.add(docInfo);
	        	}else if(docLs.size() == 29){
	        		docLs.add(docInfo);
	        		try {
						// 调用导出excel
	        			System.out.println("导出excel...");
						ExportExcelUtil.excelPoi(docLs,flag,localPath);
					} catch (Exception e) {
						e.printStackTrace();
					}
	        		docLs.clear();
	        		flag = true;
	        	}
	        }  
	        return flag;
	  }  
	 
	 /**
	   * 遍历md文件,查询发布信息并导出为excel
	   * 从数据库查询
	  * @param filePath
	  * @param localPath
	  * @return
	  * @throws IOException
	 * @throws ParseException 
	  */
	 public static Boolean visitorMd(String filePath,String localPath) throws IOException, ParseException {
	    	Boolean flag = false;
	        Path startingDir = Paths.get(filePath);  
	        FindFileVisitor findJavaVisitor = new FindFileVisitor(".md");  
	        Files.walkFileTree(startingDir, findJavaVisitor);  
	        List<Document> docLs = new ArrayList<>();
	       Document docInfo = new Document();
	        SearchInfoServiceImpl search = new SearchInfoServiceImpl();
	        for (String name : findJavaVisitor.getFilenameList()) {  
	        	docInfo = search.searchReturnRepo(name);
	        	if(docLs.size() < 29) {
	        		docLs.add(docInfo);
	        	}else if(docLs.size() == 29){
	        		docLs.add(docInfo);
	        		// 调用导出excel
	        		ExportExcelUtil.excelPoi(docLs,flag,localPath);
	        		docLs.clear();
	        		flag = true;
	        	}
	        }  
	        return flag;
	  }  
	 
	 /**
	  * 复制文件
	  * @param source
	  * @param copy
	  * @throws IOException
	  */
	 public static void copyFile(String source, String copy) throws IOException {
			File source_file = new File(source);
		    File copy_file = new File(copy);
			
		    if (!source_file.exists()) {
		        throw new IOException("文件复制失败：源文件（" + source_file + "） 不存在");
		    }
		    if (copy_file.isDirectory()) {
		        throw new IOException("文件复制失败：复制路径（" + copy_file + "） 错误");
		    }
		    File parent = copy_file.getParentFile();
		    // 创建复制路径
		    if (!parent.exists()) {
		        parent.mkdirs();
		    }
		    // 创建复制文件
		    if (!copy_file.exists()) {
		        copy_file.createNewFile();
		    }

		    FileInputStream fis = new FileInputStream(source_file);
		    FileOutputStream fos = new FileOutputStream(copy_file);

		    BufferedInputStream bis = new BufferedInputStream(fis);
		    BufferedOutputStream bos = new BufferedOutputStream(fos);

		    byte[] KB = new byte[1024];
		    int index;
		    try {
				while ((index = bis.read(KB)) != -1) {
				    bos.write(KB, 0, index);
				}
				bos.close();
				bis.close();
				fos.close();
				fis.close();
				System.out.println("复制完成！");
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
			
		}
	 
} 

  
class FindFileVisitor extends SimpleFileVisitor<Path> {  
    private List<String> filenameList = new ArrayList<String>();  
    private String fileSuffix = null;  
    public FindFileVisitor(String fileSuffix) {  
        this.fileSuffix = fileSuffix;  
    }  
  
    @Override  
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {  
        if (file.toString().endsWith(fileSuffix)) {  
            filenameList.add(file.toString());  
        }  
        return FileVisitResult.CONTINUE;  
    }  
  
    public List<String> getFilenameList() {  
        return filenameList;  
    }  
  
    public void setFilenameList(List<String> filenameList) {  
        this.filenameList = filenameList;  
    }  
  
}  
