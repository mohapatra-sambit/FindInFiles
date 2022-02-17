package com.sam.textsearch.bean;

import com.sam.textsearch.utils.Constants;

public class StatusBean {

	private static boolean isAnySavedSearchAvailable;
	
	private static boolean newSavedSearchedAdded;
	
	private static StringBuffer errBuffer = new StringBuffer();

	public static boolean isAnySavedSearchAvailable() {
		return isAnySavedSearchAvailable;
	}

	public static void setAnySavedSearchAvailable(boolean isAnySavedSearchAvailable) {
		StatusBean.isAnySavedSearchAvailable = isAnySavedSearchAvailable;
	}
	
	public static boolean isNewSavedSearchedAdded() {
		return newSavedSearchedAdded;
	}

	public static void setNewSavedSearchedAdded(boolean newSavedSearchedAdded) {
		StatusBean.newSavedSearchedAdded = newSavedSearchedAdded;
	}
	
	public static void addErrorMsg(String ... msgs) {
		for (int i=0; i<msgs.length; i++) {
			errBuffer.append(msgs[i]).append(Constants.NEW_LINE);
		}
	}
	
	public static String getErrorMsgs() {
		return errBuffer.toString();
	}
	
	public static boolean hasError() {
		return errBuffer.length() > 0;
	}

}
