package com.sam.textsearch.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import com.sam.textsearch.bean.StatusBean;
import com.sam.textsearch.enm.ErrorCode;
import com.sam.textsearch.excp.KBException;
import com.sam.textsearch.init.Loader;

public class FileUtils {

	public static void validateFile(File f, boolean needWriteAccess) throws KBException {
		if (!f.exists()) {
			throw new KBException(ErrorCode.FILE_NOT_FOUND, f.getName());
		} else {
			if (!f.isFile()) {
				throw new KBException(ErrorCode.NOT_A_FILE, f.getName());
			} else {
				if (!f.canRead()) {
					throw new KBException(ErrorCode.UNABLE_TO_READ, f.getName());
				} else {
					if (needWriteAccess) {
						if (!f.canWrite()) {
							throw new KBException(ErrorCode.UNABLE_TO_WRITE, f.getName());
						}
					}
				}
			}
		}
	}
	
	public static void validateFolder(File f, boolean needWriteAccess) throws KBException {
		if (!f.exists()) {
			throw new KBException(ErrorCode.FILE_NOT_FOUND, f.getName());
		} else {
			if (!f.isDirectory()) {
				throw new KBException(ErrorCode.NOT_A_FOLDER, f.getName());
			} else {
				if (!f.canRead()) {
					throw new KBException(ErrorCode.UNABLE_TO_READ, f.getName());
				} else {
					if (needWriteAccess) {
						if (!f.canWrite()) {
							throw new KBException(ErrorCode.UNABLE_TO_WRITE, f.getName());
						}
					}
				}
			}
		}
	}

	public static void saveSearchInFile() {
		OutputStream os = null;
		try {
			Properties  p = Loader.getAllSavedSearches();
			File f = new File(Constants.SAVE_SEARCH_FILE_FULL_PATH);
			if(!f.exists()) {
				f.getParentFile().mkdirs();
				f.createNewFile();
			}
			os = new FileOutputStream(f);
			p.store(os, "");
		} catch (Exception e) {
			e.printStackTrace();
			StatusBean.addErrorMsg("Error occurred while saving the search in file.",
					e.getMessage(), KBException.getException(e));
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
					StatusBean.addErrorMsg("Error occurred while closing the saved search file.",
							e.getMessage(), KBException.getException(e));
				}
			}
		}
	}

	public static void writeErrorToFile() {
		String msgs = StatusBean.getErrorMsgs();
		BufferedWriter out = null;
		File file = new File("ts.log");
		try {
			if (file.exists()) {
				file.delete();
				file.createNewFile();
			}
			out = new BufferedWriter(new FileWriter(file));
			out.write(msgs);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
