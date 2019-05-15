package com.basic.util;

import java.io.File;

public class Recursion {
	/**
	 * @Date: 2019-4-9 10:30
	 * @Description: 递归遍历文件目录
	 */
	public static class DiGui {
	    public void traverseFolder2(String path) {
	        File file = new File(path);
	        if (file.exists()) {
	            File[] files = file.listFiles();
	            if (null == files || files.length == 0) {
	                System.out.println("文件夹是空的!");
	                return;
	            } else {
	                for (File file2 : files) {
	                    if (file2.isDirectory()) {
	                    	System.out.println("文件夹:" + file2.getAbsolutePath());
	                        traverseFolder2(file2.getAbsolutePath());
	                    } else {
	                        // 判断是否是以.doc结尾的文件
	                        if(file2.getName().endsWith(".doc")){
	                            // 该部分可以直接调用存储到excel的方法，也可以存储到集合里，最后统一存储
	                            System.out.println(file2.getAbsolutePath());
	                        }
	                    }
	                }
	            }
	        } else {
	            System.out.println("文件不存在!");
	        }
	    }

	    public static void main(String[] args){
	        DiGui digui = new DiGui();
	        //要递归遍历的文件目录
	        digui.traverseFolder2("F:\\qcloud-documents\\product");
	    }
	}


}
