package com.framework.config;



import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.apache.commons.lang3.StringUtils;

/**
* @ClassName: DateAdapter
* @Description: (日期适配器)
* @author Meiqq
* @date 2017年12月23日 下午5:35:49
* 
*/

public class DateAdapter extends XmlAdapter<String, Date> {

	private String pattern = "yyyy-MM-dd HH:mm:ss";
	private SimpleDateFormat sdf = new SimpleDateFormat(pattern);

	@Override
	public Date unmarshal(String v) throws Exception {
		Date date = null;
		if (!"null".equals(v) && StringUtils.isNotEmpty(v)) {
			date = sdf.parse(v);
		}
		return date;
	}

	@Override
	public String marshal(Date v) throws Exception {
		return sdf.format(v);
	}

}
