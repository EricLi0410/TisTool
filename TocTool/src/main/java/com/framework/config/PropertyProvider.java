package com.framework.config;

import java.text.MessageFormat;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

public class PropertyProvider {
	   private MessageSource messageSource;

	   public PropertyProvider(MessageSource msgSource) {
	      messageSource = msgSource;
	   }
	   public String getProperty(String aPKey)  {
	      String value = "";
	      try {
	         value = messageSource.getMessage(aPKey, null, null);
	      } catch (NoSuchMessageException nsme) {
	    	  nsme.printStackTrace();
	      }
	      return value;
	   }
	   public String getProperty(String aPKey, Object[] aPParam)  {
	      if (aPParam != null) {
	         return getProperty(aPKey, aPParam, Locale.getDefault());
	      } else {
	         return getProperty(aPKey);
	      }
	   }
	   public String getProperty(String aPKey, Object[] aPParam, Locale aPLocale) {
	      String value = "";
	      try {
	         value = messageSource.getMessage(aPKey, aPParam, aPLocale);
	         if (aPParam != null) {
	            MessageFormat mf = new MessageFormat(value, aPLocale);
	            value = mf.format(aPParam, new StringBuffer(), null).toString();
	         }
	      } catch (NoSuchMessageException nsme) {
	    	  nsme.printStackTrace();
	      }
	      return value;
	   }
	   public String getResources() {
	      return messageSource.toString();
	   }
}
