package com.sam.textsearch.utils;


public class GenUtils {
	
	public static String replaceComma(String s) {
		s = s.replaceAll("[,]", Constants.COMMA);
		return s;
	}
	
	public static String revertComma(String s) {
		s = s.replaceAll(Constants.COMMA, ",");
		return s;
	}
	
}
