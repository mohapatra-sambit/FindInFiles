package com.sam.textsearch.mgr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.sam.textsearch.bean.DataBean;
import com.sam.textsearch.bean.InputBean;
import com.sam.textsearch.model.ResultTableModel;
import com.sam.textsearch.stats.Statistics;
import com.sam.textsearch.ui.GUIMain;
import com.sam.textsearch.utils.Constants;

public class TSManager {
	
	private static final Logger log = Logger.getLogger(TSManager.class);
	
	private static boolean stopFlag = false;
	
	public void searchText(GUIMain gui) {
		log.debug("[TSManager {searchText}] :: Commencing search.");
		stopFlag = false;
		search(InputBean.getLocation(), gui);
		log.debug("[TSManager {searchText}] :: Search is complete.");
		String msg = new StringBuffer("Search Complete.  ")
				.append(Statistics.getNoOfOccurrencesCounter())
				.append(" occurences found in ")
				.append(Statistics.getFoundInFilesCounter())
				.append(" files.  Total files read: ")
				.append(Statistics.getFilesReadCounter())
				.append(".").toString();
		log.debug("[TSManager {searchText}] :: Displaying results.");
		gui.setInformation(msg);
		ResultTableModel.getInstance().refresh();
	}

	private void search(File src, GUIMain gui) {
		if (!stopFlag) {
			log.debug("[TSManager {search}] :: Checking [" + src.getName() + "].");
			if (src.isFile()) {
				log.debug("[TSManager {search}] :: It is a file.");
				if (!isNotToBeCheckedByDefault(src.getName().toUpperCase())) {
					if (!isFilteredFile(src)) {
						log.debug("[TSManager {search}] :: Cleared for search.");
						Statistics.incrementFilesReadCounter();
						gui.setInformation("Searching " + src.getAbsolutePath() + "...");
						searchFile(src);
					}
				}
			} else {
				System.out.println("Stop flag now is :" + stopFlag);
				if (!isFolderNotToBeCheckedByDefault(src)) {
					log.debug("[TSManager {search}] :: It is a folder. Going inside it.");
					File[] files = src.listFiles();
					for (File file : files) {
						if (!stopFlag) {
							search(file, gui);
						} else {
							break;
						}
					}
				}
			}
		}
	}

	private boolean isFolderNotToBeCheckedByDefault(File src) {
		String folderName = src.getName();
		if (folderName.startsWith(".")) {
			return true;
		}
		if (folderName.equalsIgnoreCase(Constants.BIN_FOLDER)) {
			return true;
		}
		return false;
	}

	private void searchFile(File file) {
		log.debug("[TSManager {searchFile}] :: Searching file [" + file.getName() + "]");
		BufferedReader r = null;
		try {
			r = new BufferedReader(new FileReader(file));
			String line = null;
			int lineNo = 1;
			boolean isAccounted = false;
			while ((line = r.readLine()) != null) {
				if (line.contains(InputBean.getKeyword())) {
					if (!isAccounted) {
						Statistics.incrementFoundInFilesCounter();
						isAccounted = true;
					}
					Statistics.incrementNoOfOccurrencesCounter();
					setTableDataBean(file.getAbsolutePath(), lineNo);
				}
				++lineNo;
			}
		} catch (FileNotFoundException e) {
			handleError(e, Constants.FILE_NOT_FOUND_MSG, file);
		} catch (IOException e) {
			handleError(e, Constants.IO_ERROR_MSG, file);
		}
	}

	private void handleError(Exception e, String s, File f) {
		log.error("[TSManager {handleError}] :: Error occurred while searching.", e);
		String content = Constants.ERROR_PREFIX + s + " [" + f.getName() + "].";
		setTableDataBean(content, 0);
	}

	private void setTableDataBean(String content, int line) {
		DataBean b = new DataBean();
		b.setFileName(content);
		b.setLineNumber(line);
		ResultTableModel.getInstance().addData(b);
	}

	private boolean isFilteredFile(File file) {
		log.debug("[TSManager {isFilteredFile}] :: Checking the file filters.");
		String filename = file.getName().toUpperCase();
		if (InputBean.isFiltersEnabled()) {
			String[] allfilters = InputBean.getFilters();
			for (String filter : allfilters) {
				if (filename.endsWith(filter)) {
					log.debug("[TSManager {isFilteredFile}] :: No.");
					return false;
				}
			}
			log.debug("[TSManager {isFilteredFile}] :: Yes.");
			return true;
		}
		log.debug("[TSManager {isFilteredFile}] :: No.");
		return false;
	}

	private boolean isNotToBeCheckedByDefault(String filename) {
		log.debug("[TSManager {isNotToBeCheckedByDefault}] :: Checking default files for not searching.");
		String[] defaltFilters = Constants.DEFAULT_EXTENSIONS_FOR_NO_CHECK;
		for (String extn : defaltFilters) {
			if (filename.endsWith(extn)) {
				log.debug("[TSManager {isNotToBeCheckedByDefault}] :: Yes.");
				return true;
			}
		}
		log.debug("[TSManager {isNotToBeCheckedByDefault}] :: No.");
		return false;
	}

	public static void stop() {
		stopFlag = true;
	}
}
