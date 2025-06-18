package com.gl.MDRProcess.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.google.common.base.CaseFormat;
import com.google.common.base.CharMatcher;

@Component
public class Utility {

	private final static String NUMERIC_STRING = "0123456789";
	public String newDate(int nextdateDay) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();

		cal.add(Calendar.DAY_OF_MONTH, nextdateDay);  
		String newDate = sdf.format(cal.getTime());  

		return newDate;

	}

	public static String randomNumericString(int length) {
		StringBuilder builder = new StringBuilder();
		while (length-- != 0) {
			int character = (int)(Math.random()*NUMERIC_STRING.length());
			builder.append(NUMERIC_STRING.charAt(character));
		}
		return builder.toString();
	}


	public Date formatChanger(LocalDateTime localDateTime ) throws ParseException {
		String dmyFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(localDateTime);
		Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dmyFormat);
		
		return date;
	}
	
	public String toCamelCase(String sentence) {
	    String[] words = sentence.replaceAll("[^a-zA-Z0-9- ]", "").split(" ");
	    StringBuilder sb = new StringBuilder();
	    for (String word : words) {
	        if (!word.isEmpty()) {
	            sb.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1).toLowerCase()).append(" ");
	        }
	    }
	    return sb.toString().trim();
	}

	public static void main(String[] args) {
//		String str = "org.hibernate.dialect.MySQL5InnoDBDialect";
//		
//		System.out.println(str.toLowerCase().contains("mysql"));
		System.out.println(new Utility().toCamelCase("i ravi-kumar"));
	}
	

}
