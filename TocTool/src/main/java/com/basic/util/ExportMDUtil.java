package com.basic.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CellStyle;

public class ExportMDUtil {
	/**
	 *  导出md文件名到excel
	 * @param list
	 * @param flag
	 * @return
	 * @throws IOException
	 */
	public static Boolean excelPoi (List<String> list, Boolean flag, String localPath) throws IOException {
		HSSFWorkbook workbook = null;
		HSSFSheet sheet = null;
		int rowIndex = 0;
		if(flag == false) {
			System.out.println("start export ...");
			System.out.println("start create excel...");
			workbook = new HSSFWorkbook();
			sheet = workbook.createSheet("全量md");
			sheet.setDefaultColumnWidth(17);
			// 创建列表名称样式
	        HSSFCellStyle headerStyle = workbook.createCellStyle();
	        headerStyle.setWrapText(true);// 设置长文本自动换行
	        headerStyle.setAlignment(CellStyle.ALIGN_CENTER);//居中
	 
	        // 创建表头，列
	        HSSFRow headerRow = sheet.createRow(0);
	        headerRow.setHeightInPoints(20f);
	 
	        HSSFCell header1 = headerRow.createCell(0);//0列
	        header1.setCellValue("github链接");
	        
	        rowIndex = 1;
		}else {
			FileInputStream fs=new FileInputStream(localPath);  
			POIFSFileSystem ps=new POIFSFileSystem(fs);  //使用POI提供的方法得到excel的信息
			workbook=new HSSFWorkbook(ps); 
			sheet = workbook.getSheet("全量md");
			int startRow = sheet.getLastRowNum() + 1;  //追加开始行数
			rowIndex = startRow;
		}
        //一般数据样式
        HSSFCellStyle cellStyle = workbook.createCellStyle();
       for (String ls : list) {
    	    HSSFRow rows = sheet.createRow(rowIndex);
   			rows.setHeightInPoints(20f);
   			HSSFCell cell = rows.createCell(0);
   			cell.setCellValue(ls);
            cell.setCellStyle(cellStyle);
            rowIndex++;
	   }
        
        OutputStream outputStream = null;
        try {
            File file = new File(localPath);
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
