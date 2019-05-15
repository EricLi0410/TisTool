package com.framework.common;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class StringUtils {
	private static Logger logger = Logger
			.getLogger(StringUtils.class.getName());

	/**
	 * 字符串空值处理
	 * 
	 * @param param
	 * @return
	 */
	public static String encodeString(String param) {
		String encodeParam = "";
		if (param != null) {
			encodeParam = param;
		}
		return encodeParam;
	}

	public static Integer encodeStringToInteger(String param) {
		int encodeParam = 0;
		try {
			if (param != null) {
				encodeParam = Integer.parseInt(param);
			}
		} catch (Exception e) {
			encodeParam = 0;
		}
		return encodeParam;
	}

	/**
	 * Double空值处理
	 * 
	 * @param param
	 * @return
	 */
	public static Double encodeDouble(Double param) {
		Double encodeParam = 0.0;
		if (param != null) {
			encodeParam = param;
		}
		return encodeParam;
	}

	/**
	 * 字符串content按指定charsetName格式转换为InputStream
	 * 
	 * @param content
	 * @param charsetName
	 * @return
	 */
	public static InputStream byte2InputStream(String content,
			String charsetName) {
		byte[] bytes = null;
		try {
			bytes = content.getBytes(charsetName);
		} catch (UnsupportedEncodingException e) {
			// e.printStackTrace();
			logger.error("content按照" + charsetName + "格式进行转换为字节流异常");
		}
		return new ByteArrayInputStream(bytes);
	}

	/**
	 * xml字符串转换为dom树
	 * 
	 * @param content
	 * @return
	 */
	public static Document parserToXML(final String content) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		Document document = null;
		try {
			db = dbf.newDocumentBuilder();
			document = db.parse(StringUtils.byte2InputStream(content, "utf-8"));
		} catch (ParserConfigurationException e) {
			// e.printStackTrace();
			logger.error("通过解析器工厂创建解析器失败");
			return null;
		} catch (SAXException e) {
			// e.printStackTrace();
			logger.error("创建树结构异常");
			return null;
		} catch (IOException e) {
			// e.printStackTrace();
			logger.error("读取异常");
			return null;
		}
		return document;
	}

	private static String[] yujings = { "", "台风", "暴雨", "暴雪", "寒潮", "大风",
			"沙尘暴", "高温", "干旱", "雷电", "冰雹", "霜冻", "大雾", "霾", "道路结冰" };
	private static String[] yujing2s = { "", "寒冷", "灰霾", "雷雨大风", "森林火险", "降温",
			"道路冰雪" };
	private static String[] levels = { "", "蓝色", "黄色", "橙色", "红色" };

	public static String paserToYujingLevel(String alarm) {
		String yujing = alarm.substring(0, 2);
		String level = alarm.substring(2);
		int num = Integer.parseInt(level);
		if (yujing.startsWith("9")) {
			yujing = yujing2s[Integer.parseInt(alarm.substring(1, 2))];
			level = levels[num];
		} else {
			int flag = Integer.parseInt(yujing);
			yujing = yujings[flag];
			level = levels[num];
		}
		return yujing + "," + level;
	}

	public static String replaceBlank(String str) {
		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}

	public static String replaceBlankByStr(String str, String rep) {
		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll(rep);
		}
		return dest;
	}

	/**
	 * 判断字符窜是否不为空 不为空返回true
	 * 
	 * @param
	 * @return
	 */
	public static boolean noEmpty(String result) {
		if (result == null || result.trim().equals("") || result.equals("")) {
			return false;
		}
		return true;
	}

	public static boolean isInteger(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * 截取数字字符串 保留自己规定的普通计数法的长度 l为要保留的长度
	 * 
	 * @param result
	 * @param l
	 * @return
	 */
	public static String strSub(String result, int l) {
		if (result.length() > l) {
			BigDecimal db = new BigDecimal(result);
			String ii = db.toPlainString();
			return ii.substring(0, l);
		}
		return result;
	}

	public static String phoneHide(String str) {
		if (str.length() > 7) {
			String ss = str.substring(0, 3) + "****" + str.substring(8);
			return ss;
		} else {
			return str;
		}
	}
	/**
	 * 隐藏email重要信息
	 * @param str
	 * @return
	 */
	public static String emailHide(String str){
		if(str.length()>7){
			String ss =str.split("@")[1];
			String s =str.split("@")[0];
			String email =s.substring(0, 2) +"*****" /*+s.substring(7,s.length())*/ +"@" +ss;
			return email;
		}
		return str;
	}

	public static void main(String[] args) {
		String str = "30999f";
		if (str.length() > 7) {
			String ss = str.substring(0, 3) + "****" + str.substring(7);
			System.out.println(ss);
		}
	}
	
	public static String getUUID(){
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
}