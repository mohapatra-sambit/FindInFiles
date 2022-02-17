package com.sam.textsearch.bean;

import java.io.File;

import org.apache.log4j.Logger;

import com.sam.textsearch.enm.ErrorCode;
import com.sam.textsearch.excp.KBException;
import com.sam.textsearch.utils.Constants;
import com.sam.textsearch.utils.FileUtils;

public class InputBean {
	
	private static final Logger log = Logger.getLogger(InputBean.class);

	private static String keyword;
	
	private static File location;
	
	private static String[] filters;
	
	private static String filtersAsCSV;

	public static String getKeyword() {
		return keyword;
	}

	public static void setKeyword(String keyword) {
		InputBean.keyword = keyword;
	}

	public static File getLocation() {
		return location;
	}

	public static void setLocation(File location) {
		InputBean.location = location;
	}

	public static String[] getFilters() {
		return filters;
	}

	public static void setFilters(String filters) {
		InputBean.filters = filters.toUpperCase().split(",");
		filtersAsCSV = filters;
	}

	public static void validate() throws KBException {
		log.debug("[InputBean {validate}] :: Validating inputs.");
		if (keyword == null || keyword.trim().length() == 0) {
			log.error("[InputBean {validate}] :: Search keyword is null or empty.");
			throw new KBException(ErrorCode.INCOMPLETE_INPUT,
					Constants.KEYWORD_LABEL);
		}
		if (location.isFile()) {
			FileUtils.validateFile(location, false);
		} else {
			FileUtils.validateFolder(location, false);
		}
		log.debug("[InputBean {validate}] :: Validation OK.");
	}

	public static boolean isFiltersEnabled() {
		return !(filters == null || filters.length == 0);
	}

	public static String getFiltersAsCSV() {
		return filtersAsCSV;
	}

	public static void reset() {
		keyword = null;
		location = null;
		filters = null;
	}

}
