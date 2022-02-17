package com.sam.textsearch.enm;

public enum ErrorLevel {

	INFORMATION("INFO"),
	WARNING("WARN"),
	ERROR("ERROR"),
	FATAL("FATAL");
	
	private String level;
	
	private ErrorLevel(String l) {
		this.level = l;
	}
	
	public String getLevel() {
		return level;
	}
	
}
