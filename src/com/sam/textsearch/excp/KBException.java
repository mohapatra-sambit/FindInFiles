package com.sam.textsearch.excp;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;

import com.sam.textsearch.enm.ErrorCode;
import com.sam.textsearch.init.Loader;
import com.sam.textsearch.utils.Constants;

public class KBException extends Exception {

	private static final long serialVersionUID = 5639204268328168892L;
	
	private ErrorCode code;

	private String[] params;

	public KBException(ErrorCode code) {
		this.code = code;
	}

	public KBException(ErrorCode code, String... params) {
		this(code);
		this.params = params;
	}

	public void printError() {
		System.out.println("[" + code.getErrorLevel().getLevel()
				+ "]: " + getError());
	}
	
	public String getErrorMessage() {
		return getError();
	}

	public ErrorCode getCode() {
		return code;
	}
	
	private String getError() {
		Properties msgs = Loader.getErrorMsgs();
		String msg = "";
		if (msgs == null) {
			msg = Constants.DEFAULT_ERROR_MSG;
		} else {
			msg = msgs.getProperty(code.getErrorCode(),	Constants.DEFAULT_ERROR_MSG);
			if (params != null) {
				for (int i = 0; i < params.length; i++) {
					String placeHolder = "{" + (i + 1) + "}";
					if (msg.contains(placeHolder)) {
						msg = msg.replace(placeHolder, params[i]);
					}
				}
			}
		}
		return msg;
	}

	public static String getException(Exception e) {
		StringWriter w = new StringWriter();
		e.printStackTrace(new PrintWriter(w));
		return w.toString();
	}
}
