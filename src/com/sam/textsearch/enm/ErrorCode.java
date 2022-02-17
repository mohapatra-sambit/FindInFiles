package com.sam.textsearch.enm;

public enum ErrorCode {

	FILE_NOT_FOUND("E1", ErrorLevel.ERROR),
	NOT_A_FILE("E2", ErrorLevel.ERROR),
	NOT_A_FOLDER("E3", ErrorLevel.ERROR),
	UNABLE_TO_READ("E4", ErrorLevel.ERROR),
	UNABLE_TO_WRITE("E5", ErrorLevel.ERROR),
	IO_ERROR("E6", ErrorLevel.ERROR),
	INCOMPLETE_INPUT("E7", ErrorLevel.ERROR),
	LOCATION_EMPTY("E8", ErrorLevel.ERROR),
	SAVE_SEARCH_NAME_ALREDY_EXISTS("E9", ErrorLevel.ERROR),
	SAVE_CRITERIA_ALREADY_EXISTS("E10", ErrorLevel.ERROR),
	SAVED_SEARCH_DETAILS_NOT_FOUND("E11", ErrorLevel.ERROR),
	INIT_LOAD_ERROR("E12", ErrorLevel.FATAL);

	private String errorCode;

	private ErrorLevel level;

	private ErrorCode(String code, ErrorLevel l) {
		this.errorCode = code;
		this.level = l;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public ErrorLevel getErrorLevel() {
		return level;
	}

}
