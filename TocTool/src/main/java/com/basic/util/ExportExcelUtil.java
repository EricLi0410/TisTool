package com.basic.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CellStyle;

import com.basic.entity.api.Document;


/**
 * 导出文档映射信息为excel
 * @author v_qqingmei
 *
 */
public class ExportExcelUtil {
	/**
	 * 根据条件查询中国站文档，导出文档信息为excel
	 * @param list
	 * @param params
	 * @param flag
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public static Boolean exportByCond(List<Document> list, String[] params, Boolean flag, String path) throws Exception {
		HSSFWorkbook workbook = null;
		HSSFSheet sheet = null;
		int rowIndex = 0;
		if(flag == false) {
			workbook = new HSSFWorkbook();
			sheet = workbook.createSheet("全量映射信息");
			sheet.setDefaultColumnWidth(17);
			// 创建列表名称样式
	        HSSFCellStyle headerStyle = workbook.createCellStyle();
	        headerStyle.setWrapText(true);// 设置长文本自动换行
	        headerStyle.setAlignment(CellStyle.ALIGN_CENTER);//居中
	        
	        // 创建表头，列
	        HSSFRow headerRow = sheet.createRow(0);
	        headerRow.setHeightInPoints(20f);
	        
	        //循环创建表头
	        for (int i = 0; i < params.length; i++) {
	        	HSSFCell header = headerRow.createCell(i);//0列
	 	        header.setCellValue(params[i]);
	 	        header.setCellStyle(headerStyle);
			}
	        rowIndex = 1;
		}else {
			FileInputStream fs=new FileInputStream(path); //文件保存路径：项目根目录
			POIFSFileSystem ps=new POIFSFileSystem(fs);  //使用POI提供的方法得到excel的信息
			workbook=new HSSFWorkbook(ps); 
			sheet = workbook.getSheet("全量映射信息");
			int startRow = sheet.getLastRowNum() + 1;  //追加开始行数
			rowIndex = startRow;
		}
        //一般数据样式
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        List<String> ls = new ArrayList<String>(); 
        for (Document doc : list) {  //放入数据
        	if(Arrays.asList(params).contains("product") ) {
        		ls.add(doc.getProduct());
        	}
        	if(Arrays.asList(params).contains("title") ) {
        		ls.add(doc.getTitle());
        	}
        	if(Arrays.asList(params).contains("lang") ) {
        		ls.add(doc.getLang());
        	}
        	if(Arrays.asList(params).contains("ifRelease") ) {
        		ls.add(doc.getIfRelease());
    			if(doc.getReleaseTime() == null) {
    				ls.add("0000-00-00 00:00:00");
    			}else {
    				ls.add(doc.getReleaseTime().toString());
    			}
        	}
        	if(Arrays.asList(params).contains("firstMenu") ) {
        		ls.add(doc.getFirstMenu());
        	}
        	if(Arrays.asList(params).contains("secondMenu") ) {
        		ls.add(doc.getSecondMenu());
        	}
        	if(Arrays.asList(params).contains("thirdMenu") ) {
        		ls.add(doc.getThirdMenu());
        	}
        	if(Arrays.asList(params).contains("fourthMenu") ) {
        		ls.add(doc.getFourthMenu());
        	}
        	if(Arrays.asList(params).contains("fiveMenu") ) {
        		ls.add(doc.getFiveMenu());
        	}
        	if(Arrays.asList(params).contains("webUrl") ) {
        		ls.add(doc.getWebUrl());
        	}
        	if(Arrays.asList(params).contains("githubUrl") ) {
        		ls.add(doc.getGithubUrl());
        	}
        	if(Arrays.asList(params).contains("source") ) {
        		ls.add(doc.getSource());
        	}
			HSSFRow rows = sheet.createRow(rowIndex);
	        for (int i = 0; i < ls.size(); i++) {
	            rows.setHeightInPoints(20f);
            	HSSFCell cell = rows.createCell(i);//参数为：列数index
                cell.setCellValue(ls.get(i));
                cell.setCellStyle(cellStyle);
	        }
	        ls.clear();
	        rowIndex++;
        }
        OutputStream outputStream = null;
        try {
            File file = new File(path);  //文件保存地址
            outputStream = new FileOutputStream(file);
            workbook.write(outputStream);
            flag = true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            flag = false;
        } finally {
            try {
                if (outputStream != null) {
                	outputStream.flush();
                    outputStream.close();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
		return flag;
	}
	
	/**
	 * 导出文档映射信息为excel
	 * @param list
	 * @param flag
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static Boolean excelPoi (List<Document> list , Boolean flag, String path) throws IOException {
		HSSFWorkbook workbook = null;
		HSSFSheet sheet = null;
		int rowIndex = 0;
		if(flag == false) {
			System.out.println("start export ...");
			System.out.println("start create excel...");
			workbook = new HSSFWorkbook();
			sheet = workbook.createSheet("全量映射信息");
			sheet.setDefaultColumnWidth(17);
			// 创建列表名称样式
	        HSSFCellStyle headerStyle = workbook.createCellStyle();
	        headerStyle.setWrapText(true);// 设置长文本自动换行
	        headerStyle.setAlignment(CellStyle.ALIGN_CENTER);//居中
	 
	        // 创建表头，列
	        HSSFRow headerRow = sheet.createRow(0);
	        headerRow.setHeightInPoints(20f);
	 
	        HSSFCell header1 = headerRow.createCell(0);//0列
	        header1.setCellValue("产品");
	        header1.setCellStyle(headerStyle);
	        HSSFCell header2 = headerRow.createCell(1);//1列
	        header2.setCellValue("产品标题");
	        header2.setCellStyle(headerStyle);
	        HSSFCell header3 = headerRow.createCell(2);
	        header3.setCellValue("语言");
	        header3.setCellStyle(headerStyle);
	        HSSFCell header4 = headerRow.createCell(3);
	        header4.setCellValue("是否发布");
	        header4.setCellStyle(headerStyle);
	        HSSFCell header5 = headerRow.createCell(4);
	        header5.setCellValue("发布时间");
	        header5.setCellStyle(headerStyle);
	        HSSFCell header6 = headerRow.createCell(5);
	        header6.setCellValue("一级菜单名称");
	        header6.setCellStyle(headerStyle);
	        HSSFCell header7 = headerRow.createCell(6);
	        header7.setCellValue("二级菜单名称");
	        header7.setCellStyle(headerStyle);
	        HSSFCell header8 = headerRow.createCell(7);
	        header8.setCellValue("三级菜单名称");
	        header8.setCellStyle(headerStyle);
	        HSSFCell header9 = headerRow.createCell(8);
	        header9.setCellValue("四级菜单名称");
	        header9.setCellStyle(headerStyle);
	        HSSFCell header10 = headerRow.createCell(9);
	        header10.setCellValue("五级菜单名称");
	        header10.setCellStyle(headerStyle); 
	        HSSFCell header11 = headerRow.createCell(10);
	        header11.setCellValue("官网链接");
	        header11.setCellStyle(headerStyle);
	        HSSFCell header12 = headerRow.createCell(11);
	        header12.setCellValue("github链接");
	        header12.setCellStyle(headerStyle);
	        HSSFCell header13 = headerRow.createCell(12);
	        header13.setCellValue("来源");
	        header13.setCellStyle(headerStyle);
	        
	        rowIndex = 1;
		}else {
			FileInputStream fs=new FileInputStream(path); //文件保存路径：项目根目录
			POIFSFileSystem ps=new POIFSFileSystem(fs);  //使用POI提供的方法得到excel的信息
			workbook=new HSSFWorkbook(ps); 
			sheet = workbook.getSheet("全量映射信息");
			int startRow = sheet.getLastRowNum() + 1;  //追加开始行数
			rowIndex = startRow;
		}
        //一般数据样式
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        
        List<String> ls = new ArrayList<String>(); 
        for (Document doc : list) {  //放入数据
			ls.add(doc.getProduct());
			ls.add(doc.getTitle());
			ls.add(doc.getLang());
			ls.add(doc.getIfRelease());
			if(doc.getReleaseTime() == null) {
				ls.add("0000-00-00 00:00:00");
			}else {
				ls.add(doc.getReleaseTime().toString());
			}
			ls.add(doc.getFirstMenu());
			ls.add(doc.getSecondMenu());
			ls.add(doc.getThirdMenu());
			ls.add(doc.getFourthMenu());
			ls.add(doc.getFiveMenu());  
			ls.add(doc.getWebUrl());
			ls.add(doc.getGithubUrl());
			ls.add(doc.getSource());
			
			HSSFRow rows = sheet.createRow(rowIndex);
	        for (int i = 0; i < ls.size(); i++) {
	            rows.setHeightInPoints(20f);
            	HSSFCell cell = rows.createCell(i);//参数为：列数index
                cell.setCellValue(ls.get(i));
                cell.setCellStyle(cellStyle);
	        }
	        ls.clear();
	        rowIndex++;
        }
        OutputStream outputStream = null;
        try {
            File file = new File(path);  //文件保存地址
            outputStream = new FileOutputStream(file);
            workbook.write(outputStream);
            flag = true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            flag = false;
        } finally {
            try {
                if (outputStream != null) {
                	outputStream.flush();
                    outputStream.close();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
		return flag;
	}
	
	
	/**
	 *  按分类导出全量映信息时调用（弃用）
	 * @param list
	 * @param category
	 * @param size
	 * @param num
	 * @return
	 * @throws IOException
	 */
	public static Boolean excelPoi2 (List<Document> list,String category,int size,int num) throws IOException {
		Boolean flag = true;
		HSSFWorkbook workbook = null;
		HSSFSheet sheet = null;
		
		if(num == 0) { 
			workbook = new HSSFWorkbook();
			for(int n=0; n<size; n++) {
				sheet = workbook.createSheet();
				String tempName = Integer.toString(n);
				workbook.setSheetName(n, tempName);
				sheet.setDefaultColumnWidth(17);
				// 创建列表名称样式
		        HSSFCellStyle headerStyle = workbook.createCellStyle();
		        headerStyle.setWrapText(true);// 设置长文本自动换行
		        headerStyle.setAlignment(CellStyle.ALIGN_CENTER);//居中
		 
		        // 创建表头，列
		        HSSFRow headerRow = sheet.createRow(0);
		        headerRow.setHeightInPoints(20f);
		 
		        HSSFCell header1 = headerRow.createCell(0);//0列
		        header1.setCellValue("产品");
		        header1.setCellStyle(headerStyle);
		        HSSFCell header2 = headerRow.createCell(1);//1列
		        header2.setCellValue("产品标题");
		        header2.setCellStyle(headerStyle);
		        HSSFCell header3 = headerRow.createCell(2);
		        header3.setCellValue("语言");
		        header3.setCellStyle(headerStyle);
		        HSSFCell header4 = headerRow.createCell(3);
		        header4.setCellValue("是否发布");
		        header4.setCellStyle(headerStyle);
		        HSSFCell header5 = headerRow.createCell(4);
		        header5.setCellValue("发布时间");
		        header5.setCellStyle(headerStyle);
		        HSSFCell header6 = headerRow.createCell(5);
		        header6.setCellValue("一级菜单名称");
		        header6.setCellStyle(headerStyle);
		        HSSFCell header7 = headerRow.createCell(6);
		        header7.setCellValue("二级菜单名称");
		        header7.setCellStyle(headerStyle);
		        HSSFCell header8 = headerRow.createCell(7);
		        header8.setCellValue("三级菜单名称");
		        header8.setCellStyle(headerStyle);
		        HSSFCell header9 = headerRow.createCell(8);
		        header9.setCellValue("四级菜单名称");
		        header9.setCellStyle(headerStyle);
		        HSSFCell header10 = headerRow.createCell(9);
		        header10.setCellValue("五级菜单名称");
		        header10.setCellStyle(headerStyle);
		        HSSFCell header11 = headerRow.createCell(10);
		        header11.setCellValue("官网链接");
		        header11.setCellStyle(headerStyle);
		        HSSFCell header12 = headerRow.createCell(11);
		        header12.setCellValue("github链接");
		        header12.setCellStyle(headerStyle);
		        HSSFCell header13 = headerRow.createCell(12);
		        header13.setCellValue("来源");
		        header13.setCellStyle(headerStyle);
			}
		}else {
			FileInputStream fs=new FileInputStream("F://githubUrl.xls");  //F://githubUrl.xls
			POIFSFileSystem ps=new POIFSFileSystem(fs);  //使用POI提供的方法得到excel的信息
			workbook=new HSSFWorkbook(ps); 
		}
		if(workbook.getSheet(category) == null ) {   // 判断是否有名为当前category值的sheet
			System.out.println(num);
			sheet=workbook.getSheetAt(num);
			workbook.setSheetName(num, category);  //重命名
		}else {
			sheet = workbook.getSheet(category);
		}
        //一般数据样式
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        List<String> ls = new ArrayList<String>();
        int rowIndex = 1;
        for (Document doc : list) {  //放入数据
			ls.add(doc.getProduct().toString());
			ls.add(doc.getTitle().toString());
			ls.add(doc.getLang().toString());
			ls.add(doc.getIfRelease().toString());
			ls.add(doc.getReleaseTime().toString());
			ls.add(doc.getFirstMenu().toString());
			ls.add(doc.getSecondMenu().toString());
			ls.add(doc.getThirdMenu().toString());
			ls.add(doc.getFourthMenu().toString());
			ls.add(doc.getFiveMenu().toString());
			ls.add(doc.getWebUrl().toString());
			ls.add(doc.getGithubUrl().toString());
			ls.add(doc.getSource().toString());
			
			HSSFRow rows = sheet.createRow(rowIndex);
	        for (int i = 0; i < ls.size(); i++) {
	            rows.setHeightInPoints(20f);
            	HSSFCell cell = rows.createCell(i);//参数为：列数index
                cell.setCellValue(ls.get(i));
                cell.setCellStyle(cellStyle);
	        }
	        ls.clear();
	        rowIndex++;
        }
        OutputStream outputStream = null;
        try {
            File file = new File("F://githubUrl.xls");
            outputStream = new FileOutputStream(file);
            workbook.write(outputStream);
            flag = true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            flag = false;
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                flag = true;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                flag = false;
            }
        }
		return flag;
	}
	
}
