package com.sam.textsearch.utils;

public class Constants {

	private Constants() {}
	
	public static final String FILE_NOT_FOUND_MSG = "File not found";
	public static final String IO_ERROR_MSG = "Error while reading";
	public static final String DEFAULT_ERROR_MSG = "Unknown error has occurred.";

	public static final String ERROR_PREFIX = "[ERROR] ";
	
	public static final String[] DEFAULT_EXTENSIONS_FOR_NO_CHECK = {
		"JPG", "JPEG", "BMP", "GIF", "PNG", "MP3", "MP4", "MPEG", "WAV", "AVI", "DAT", "FLV", "JAR", "WAR", "EAR",
		"SAR", "CLASS", "3GP", "AAC", "AIR", "APPLICATION", "ARJ", "BIN", "CHM", "COM", "DIVX", "DOC", "DOCX", "XLS",
		"XLSX", "PPT", "PPTX", "PDF", "EML", "GZ", "TAR", "ISO", "JNLP", "LNK", "MDB", "MID", "MIDI", "MKV", "MOV",
		"MOVIE", "MP4V", "MSG", "MSI", "EXE", "OBJ", "ODP", "ODS", "ODT", "ICO", "PPS", "PPSX", "PST", "REG", "RM", "RMVB",
		"RTF", "SWF", "SYS", "THEME", "THM", "TIF", "TIFF", "TTF", "VCF", "VDW", "VDX", "VLC", "VOB", "VSD", "VSDX",
		"WEBM", "WMA", "WMF", "WMV", "WRF", "XPS", "ZIP", "DLL", "SER"
	};

	private static final String user = System.getProperty("user.name");
	private static final String home = System.getProperty("user.home");
	private static final String file_sep = System.getProperty("file.separator");
	
	public static final String SAVE_SEARCH_FILE_FULL_PATH = new StringBuffer(home)
		.append(file_sep).append("FIF").append(file_sep).append(user)
			.append(".ss").toString();

	public static final String KEYWORD_LABEL = "Search For";
	public static final String LOCATION_LABEL = "File/Folder";
	public static final String FILTERS_LABEL = "Filters";
	public static final String BROWSE_BUTTON_LABEL = "Browse";
	public static final String SEARCH_BUTTON_LABEL = "Search";
	public static final String RESET_BUTTON_LABEL = "Reset";
	public static final String SAVE_BUTTON_LABEL = "Save current search";
	public static final String LOAD_SEARCH_LABEL = "Select previously saved search:";
	public static final String EXIT_BUTTON_LABEL = "Exit";
	public static final String INPUT_SAVE_SEARCH_LABEL = "Enter the name for saving this search...";
	public static final String STOP_BUTTON_LABEL = "Stop";
	
	public static final String ERROR_MSGS_FILE = "/msgs";
	public static final String FIF_ICON_SMALL_PATH = "/img/fif_small.png";
	public static final String FIF_ICON_BIG_PATH = "/img/fif_big.png";
	public static final String LOADER_IMAGE_PATH = "img/load.gif";
	
	public static final String RESULT_HEADER_SERIAL_NO = "#";
	public static final String RESULT_HEADER_FILENAME = "File Name";
	public static final String RESULT_HEADER_LINENO = "Line #";
	
	public static final String KEYWORD_TOOL_TIP = "Keyword to search";
	public static final String LOCATION_TOOL_TIP = "Search location";
	public static final String FILTERS_TOOL_TIP = "Extensions filters";
	public static final String BROWSE_BUTTON_TOOL_TIP = "Browse location";
	public static final String SEARCH_BUTTON_TOOL_TIP = "Start searching";
	public static final String RESET_BUTTON_TOOL_TIP = "Reset all fields";
	public static final String SAVE_BUTTON_TOOL_TIP = "Save this search";
	public static final String LOAD_SEARCH_COMBO_BOX_TOOL_TIP = "Load a saved search";
	public static final String EXIT_BUTTON_TOOL_TIP = "Exit Find in Files";
	public static final String STOP_BUTTON_TOOL_TIP = "Stop the current search";
	
	public static final String SEARCH_INPUTS_TITLE = "Search Inputs";
	public static final String INPUT_SAVE_SEARCH_TITLE = "Save Search";
	
	public static final String SAVE_SEARCH_CONFIRMATION_MSG = "Search is saved!!!";

	public static final String NEW_LINE = System.getProperty("line.separator");
	public static final String BIN_FOLDER = "bin";
	public static final String COMMA = "<C>";

}
