package com.sam.textsearch.init;

import java.io.File;
import java.io.FileInputStream;
import java.util.Collection;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.sam.textsearch.bean.InputBean;
import com.sam.textsearch.bean.StatusBean;
import com.sam.textsearch.enm.ErrorCode;
import com.sam.textsearch.excp.KBException;
import com.sam.textsearch.utils.Constants;
import com.sam.textsearch.utils.GenUtils;

public class Loader {
	
	private static final Logger log = Logger.getLogger(Loader.class);
	
	private static Properties e = new Properties();
	
	private static Properties ss = new Properties(); 
	
	private static void loadErrorMsgs() throws KBException {
		log.debug("[Loader {loadErrorMsgs}] :: Loading error messages from file.");
		try {
			e.load(Loader.class.getResourceAsStream(Constants.ERROR_MSGS_FILE));
			log.debug("[Loader {loadErrorMsgs}] :: Error messages loaded successfully.");
		} catch (Exception e) {
			log.error("[Loader {loadErrorMsgs}] :: Error occurred while loading the error messages.", e);
			throw new KBException(ErrorCode.INIT_LOAD_ERROR);
		}
	}

	public static void load() throws KBException {
		log.debug("[Loader {load}] :: Initiating loading process.");
		loadErrorMsgs();
		loadSavedSearches();
	}
	
	private static void loadSavedSearches() {
		log.debug("[Loader {loadSavedSearches}] :: Loading the saved searches.");
		File savedSearchFile = new File(Constants.SAVE_SEARCH_FILE_FULL_PATH);
		if (savedSearchFile.exists()) {
			try {
				log.debug("[Loader {loadSavedSearches}] :: Saved search file found. Reading it.");
				ss.load(new FileInputStream(savedSearchFile));
				if (ss.isEmpty()) {
					log.debug("[Loader {loadSavedSearches}] :: Saved search file is empty.");
					StatusBean.setAnySavedSearchAvailable(false);
				} else {
					log.debug("[Loader {loadSavedSearches}] :: Saved search(s) are loaded.");
					StatusBean.setAnySavedSearchAvailable(true);
				}
			} catch (Exception e) {
				log.error("Error occurred while loading the saved searches.", e);
				StatusBean.setAnySavedSearchAvailable(false);
			}
		} else {
			log.error("[Loader {loadSavedSearches}] :: Saved search file is not found.");
			StatusBean.setAnySavedSearchAvailable(false);
		}
	}

	public static Properties getErrorMsgs() {
		return e;
	}

	public static String getSavedSearchDetails(String savedSearchName) throws KBException {
		if (ss.containsKey(savedSearchName)) {
			return ss.getProperty(savedSearchName);
		}
		log.error("[Loader {getSavedSearchDetails}] :: " + savedSearchName + " not found.");
		throw new KBException(ErrorCode.SAVED_SEARCH_DETAILS_NOT_FOUND, savedSearchName);
	}
	
	public static void addSavedSearch(String name, String keyword, String location,
			String filter) throws KBException {
		if (ss.containsKey(name)) {
			log.error("[Loader {addSavedSearch}] :: " + name + " already exists.");
			throw new KBException(ErrorCode.SAVE_SEARCH_NAME_ALREDY_EXISTS, name);
		}
		keyword = GenUtils.replaceComma(keyword);
		location = GenUtils.replaceComma(location);
		filter = GenUtils.replaceComma(filter);
		String value = keyword + "," + location + "," + filter;
		log.debug("[Loader {addSavedSearch}] :: Adding [" + name + " : " + value + "]");
		ss.setProperty(name, value);
		StatusBean.setNewSavedSearchedAdded(true);
	}

	public static Properties getAllSavedSearches() {
		return ss;
	}

	public static Collection<String> getAllSavedSearchNames() {
		return ss.stringPropertyNames();
	}
	
	public static void checkSaveCriteria() throws KBException {
		log.debug("[Loader {checkSaveCriteria}] :: Checking save criteria.");
		String k = InputBean.getKeyword();
		String l = InputBean.getLocation().getAbsolutePath();
		String f = InputBean.getFiltersAsCSV();
		String value = k + "," + l + "," + f;
		Collection<String> keys = getAllSavedSearchNames();
		for (String key : keys) {
			String v = ss.getProperty(key);
			if (v.equals(value)) {
				log.error("[Loader {checkSaveCriteria}] :: Criteria [" + key + " : " + value + "] already exists.");
				throw new KBException(ErrorCode.SAVE_CRITERIA_ALREADY_EXISTS, key);
			}
		}
	}

	public static void addSavedSearch(String name) throws KBException {
		addSavedSearch(name, InputBean.getKeyword(), InputBean.getLocation().getAbsolutePath(),
				InputBean.getFiltersAsCSV() == null ? "" : InputBean.getFiltersAsCSV());
	}

}
