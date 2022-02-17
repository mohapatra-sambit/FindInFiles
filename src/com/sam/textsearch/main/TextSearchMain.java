package com.sam.textsearch.main;

import java.awt.Toolkit;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.sam.textsearch.bean.StatusBean;
import com.sam.textsearch.excp.KBException;
import com.sam.textsearch.init.Loader;
import com.sam.textsearch.mgr.TSManager;
import com.sam.textsearch.ui.GUIMain;
import com.sam.textsearch.utils.FileUtils;

public class TextSearchMain {
	
	private static final Logger log = Logger.getLogger(TSManager.class);

	public static void main(String[] args) {
		try {
			setUpLogger();
			log.debug("[TextSearchMain {main}] :: Starting Find in Files.");
			setLookAndFeel();
			log.debug("[TextSearchMain {main}] :: Loading data.");
			Loader.load();
			log.debug("[TextSearchMain {main}] :: Starting GUI.");
			GUIMain.start();
		} catch (KBException e) {
			log.error("[TextSearchMain {main}] :: Application error [" + e.getErrorMessage() + "].");
			StatusBean.addErrorMsg("Error occurred while starting the application.",
					e.getMessage(), KBException.getException(e));
			JOptionPane.showMessageDialog(null, e.getErrorMessage(),
					e.getCode().getErrorLevel().getLevel(),
					JOptionPane.ERROR_MESSAGE);
		} finally {
			if (StatusBean.hasError()) {
				log.debug("[TextSearchMain {main}] :: Writing error details to file.");
				FileUtils.writeErrorToFile();
			}
		}
	}

	private static void setUpLogger() {
		Properties p = new Properties();
		try {
			p.load(TSManager.class.getResourceAsStream("/log4j.properties"));
			PropertyConfigurator.configure(p);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error occurred while reading the logging properties. " +
					"Logging will be disabled. You may still find some logs in case of " +
					"any error after the application is closed.");
		}
	}

	private static void setLookAndFeel() {
		log.debug("[TextSearchMain {setLookAndFeel}] :: Setting up look and feel for the UI.");
		Toolkit.getDefaultToolkit().setDynamicLayout(true);
		System.setProperty("sun.awt.noerasebackground", "true");
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);
		try {
			UIManager.setLookAndFeel("de.muntjak.tinylookandfeel.TinyLookAndFeel");
			log.debug("[TextSearchMain {setLookAndFeel}] :: Tiny look and feel is set.");
		} catch (Exception ex) {
			StatusBean.addErrorMsg("Error occurred while setting up UI. Setting default UI.",
					ex.getMessage(), KBException.getException(ex));
			log.error("[TextSearchMain {setLookAndFeel}] :: Error occurred while setting up UI. Setting default UI.", ex);
			try {
				UIManager.setLookAndFeel(
						"com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			} catch (Exception e) {
				StatusBean.addErrorMsg("Error occurred while setting up default UI.",
						e.getMessage(), KBException.getException(e));
				log.error("[TextSearchMain {setLookAndFeel}] :: Error occurred while setting up default UI.", e);
			}
		}
	}

}
