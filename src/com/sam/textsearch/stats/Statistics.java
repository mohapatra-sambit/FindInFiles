package com.sam.textsearch.stats;

public class Statistics {

	private static int filesReadCounter;
	
	private static int noOfOccurrencesCounter;
	
	private static int foundInFilesCounter;

	public static int getFilesReadCounter() {
		return filesReadCounter;
	}

	public static int getNoOfOccurrencesCounter() {
		return noOfOccurrencesCounter;
	}

	public static int getFoundInFilesCounter() {
		return foundInFilesCounter;
	}
	
	public static void incrementFilesReadCounter() {
		filesReadCounter++;
	}
	
	public static void incrementFoundInFilesCounter() {
		foundInFilesCounter++;
	}
	
	public static void incrementNoOfOccurrencesCounter() {
		noOfOccurrencesCounter++;
	}

	public static void reset() {
		filesReadCounter = 0;
		noOfOccurrencesCounter = 0;
		foundInFilesCounter = 0;
	}
}
