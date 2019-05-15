package com.framework.common;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
* @ClassName: ExcelUtils
* @Description: (excel工具类)
* @author Meiqq
* @date 2019年4月10日 下午5:31:21
* 
* @param <T>
*/

public class ExcelUtils<T> {
	
	private static Log log = LogFactory.getLog(ExcelUtils.class);
	
	public static Workbook create(InputStream in) throws IOException,InvalidFormatException {  
	    if (!in.markSupported()) {  
	        in = new PushbackInputStream(in, 8);  
	    }  
	    if (POIFSFileSystem.hasPOIFSHeader(in)) {  
	        return new HSSFWorkbook(in);  
	    }  
	    if (POIXMLDocument.hasOOXMLHeader(in)) {  
	        return new XSSFWorkbook(OPCPackage.open(in));  
	    }  
	    throw new IllegalArgumentException("你的excel版本目前poi解析不了"); 
	}
	/**
	 * 读取第一个sheet页
	 * @param inputStream
	 * @return
	 * @throws Exception
	 */
	public static List<String[]> read(InputStream inputStream) throws Exception{
		List<String[]> result=new ArrayList<String[]>();
		try {
			Workbook wb =create(inputStream);
				Sheet sheet = wb.getSheetAt(0);
				if(sheet.getSheetName()!=null&&!"".equals(sheet.getSheetName().trim())){
				int rows = sheet.getPhysicalNumberOfRows();
				if(rows<=1)
					throw new Exception("Excel表单中没数据!");
				Row tabHeaderInExcel=sheet.getRow(0);
				if(tabHeaderInExcel==null)
					throw new Exception("Excel表单中没有找到表头!");
				int cells = tabHeaderInExcel.getPhysicalNumberOfCells();
				for (int r = 1; r < rows; r++) {
					Row row = sheet.getRow(r);
					if (row == null) {
						 continue;
					}
					String[] excelCols =new String[cells];
					for (int c = 0; c < cells; c++) {
						Cell cell = row.getCell(c);
						if(cell==null)
							continue;
						Object value=null;
						int type= cell.getCellType();
						switch (type) {
							case Cell.CELL_TYPE_NUMERIC:
								DecimalFormat df = new DecimalFormat("0");  
								value = df.format(cell.getNumericCellValue());
								break;
							case Cell.CELL_TYPE_STRING:
								value=cell.getStringCellValue();
								break;
							case Cell.CELL_TYPE_BOOLEAN:
								value=cell.getBooleanCellValue();
								break;
							default:
								continue;
							}
						if(value==null)
							continue;
						excelCols[c]=value.toString();
					}
					result.add(excelCols);
				}
			}
			return result;
		} catch (IOException e) {
			throw new Exception(e.getMessage());
		}
		catch (InvalidFormatException e){
			throw new Exception(e.getMessage());
		}
	
	}
	/**
	 * 读取多个sheet页  表头 或  表体
	 * @param inputStream
	 * @param i
	 * @return
	 * @throws Exception
	 */
	public static List<String[]> readSheetMore(InputStream inputStream,Integer readSheetIndex) throws Exception{
		List<String[]> result=new ArrayList<String[]>();
		try {
			Workbook wb =create(inputStream);
			Sheet sheet = wb.getSheetAt(readSheetIndex);
			if(sheet.getSheetName()!=null&&!"".equals(sheet.getSheetName().trim())){
				int rows = sheet.getPhysicalNumberOfRows();
				if(rows<=1)
					throw new Exception("Excel表单中没数据!");
				Row tabHeaderInExcel=sheet.getRow(0);
				if(tabHeaderInExcel==null&&readSheetIndex==0)
					throw new Exception("Excel表单中没有找到表头!");
				if(tabHeaderInExcel==null&&readSheetIndex==1)
					throw new Exception("Excel表单中没有找到表体!");
				int cells = tabHeaderInExcel.getPhysicalNumberOfCells();
				for (int r = 1; r < rows; r++) {
					Row row = sheet.getRow(r);
					if (row == null) {
						continue;
					}
					String[] excelCols =new String[cells];
					for (int c = 0; c < cells; c++) {
						Cell cell = row.getCell(c);
						if(cell==null)
							continue;
						Object value=null;
						int type= cell.getCellType();
						switch (type) {
						case Cell.CELL_TYPE_NUMERIC:
							DecimalFormat df = new DecimalFormat("0");  
							value = df.format(cell.getNumericCellValue());  
							break;
						case Cell.CELL_TYPE_STRING:
							value=cell.getStringCellValue();
							break;
						case Cell.CELL_TYPE_BOOLEAN:
							value=cell.getBooleanCellValue();
							break;
						default:
							continue;
						}
						if(value==null)
							continue;
						excelCols[c]=value.toString();
					}
					result.add(excelCols);
				}
			}
			return result;
		} catch (IOException e) {
			throw new Exception(e.getMessage());
		}
		catch (InvalidFormatException e){
			throw new Exception(e.getMessage());
		}
		
	}
	public static String WriteExcel(List<?> list,OutputStream os,String title,Class<?> tClass,String head){
		String [] titles = null;
		if(title.contains(",")){
			titles = title.split(",");
		}else{
			titles = new String[]{title};
		}
		String fileName = null;
		 Workbook wb = new HSSFWorkbook();
		 Sheet sheet = wb.createSheet("sheet1");
		 String [] heads = head.split(",");
		 Row headRow = sheet.createRow(0);
		 for(int i=0;i<heads.length;i++){
			 Cell cell = headRow.createCell(i);
			 cell.setCellValue(heads[i]);
		 }
		 for(int i=0;i<list.size();i++){
			 Row row = sheet.createRow((short)i+1);
			 for(int j=0;j<titles.length;j++){
				 Method method;
				try {
					method = tClass.getMethod(titles[j]);
					Object ov = method.invoke(list.get(i));
					String value = "";
					if(ov!=null){
						value = ov.toString();
					}
					Cell cell = row.createCell(j);
					cell.setCellValue(value);
				} catch (SecurityException e) {
					log.error(e);
				} catch (NoSuchMethodException e) {
					log.error(e);
				} catch (IllegalArgumentException e) {
					log.error(e);
				} catch (IllegalAccessException e) {
					log.error(e);
				} catch (InvocationTargetException e) {
					log.error(e);
				}
			 }
		 }
		 try {
			wb.write(os);
		} catch (IOException e) {
			log.error(e);
		}
		 return fileName;
	}
}
