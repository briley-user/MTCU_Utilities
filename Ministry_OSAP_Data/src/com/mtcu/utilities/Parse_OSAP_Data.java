
package com.mtcu.utilities;

import java.io.*;
import java.util.List; 
import java.util.ArrayList;

//import org.apache.commons.csv.*;

public class Parse_OSAP_Data {

	
	int count_columns(String column) 
		{
		
		return 0;
		}
	

	static int getNumberofListElements(List<String> referenceList) {
	
		return referenceList.size();
	}
	
	static int getNumberofColumnsfromString(String[] singleListElement) {
		
				
		return singleListElement.length;
	}
	
	static String[] getColumnFieldNames(List<String> referenceList) {
		
		
		// take first row from list to capture the field name from the CSV table
		return  getListEntry(referenceList,0);
		
		
		
	}
	
	static String[] getListEntry(List<String> referenceList, int index) {
		String[] listElement = {};
		listElement = parseStringToArray(referenceList.get(index));
		
		return listElement;
	}
	
	static List<String> readFromCSVIntoArray(String input_filename) throws IOException {
		BufferedReader br = new BufferedReader (new InputStreamReader ( new FileInputStream(input_filename),"UTF-8"));
		String line = null; 
		List<String> dataFileRows = new ArrayList<String>();
		line = br.readLine();
		while (line != null) {
		 	dataFileRows.add(line);
		 	line = br.readLine();
		}
		br.close();
		
		return dataFileRows;
	}
	
    static void normalizeColumnsFromCSV(List<String> referenceList, int repeatingColumns ) throws IOException {
    	//Read the repeating columns and rows into an array.
    	
    	int totalRowCount =getNumberofListElements(referenceList); //get total row count from original file
    	String[] dataColumns = getColumnFieldNames(referenceList); //get first row headers
    	int dataColumnsCount = getNumberofColumnsfromString(dataColumns); //get count for first header rows
    	int counter=1;
    	int repeatingFieldColumnsCount = repeatingColumns;
    	String[] repeatingColumnHeaderList = new String[repeatingColumns]; // String Array containing the columns that will  be repeating
    	repeatingColumnHeaderList=getColumnFieldNames(referenceList); // get header row from the in memory data file table
    	
    	// Rows = Take total rows from existing List and multiply it by the number of data columns to give the new list size
    	// Columns = Take the number of repeating columns defined by the user and two (2) 1 for the repeating field name and 1 for the data value
    	// All data will be stored as strings
    	String[][] normalizedData = new String[totalRowCount*(dataColumnsCount-repeatingColumns)][repeatingColumns+2];
    	
    	//need to store a row of headers just the initial columns that will repeat.
    	
    	// this code below will manage the actual data being normalized into two additional columns vertical instead of horizontal.
    	
    	for (int dataCols= repeatingFieldColumnsCount; dataCols < dataColumnsCount; ++dataCols) {
    		for (int rows=1; rows < totalRowCount ; ++rows ) {
    			for (int cols =0 ; cols <  repeatingFieldColumnsCount +2 ; ++ cols) {
    				if (cols <repeatingFieldColumnsCount) {	
    				normalizedData[counter][cols] = getListEntry(referenceList,rows)[cols];
    				}
    				if (cols == repeatingFieldColumnsCount ) {
    					normalizedData[counter][cols]=dataColumns[dataCols];
    					                                                               
    					}
    				
    				if (cols == repeatingFieldColumnsCount+1 ) {
    					normalizedData[counter][cols] =getListEntry(referenceList,rows)[dataCols];
    					                                                               
    					}
    				}
    			++counter;
    			}
    			
    	}	
    	
    	// Populate the header row of the new normalized Data
    	for (int i=0; i<repeatingColumns; ++ i) {
    		normalizedData[0][i]=getListEntry(referenceList,0)[i];
    	}
    	
    	normalizedData[0][repeatingFieldColumnsCount]="Metric Description";
    	normalizedData[0][repeatingFieldColumnsCount+1]="Metric Value";
    	//print out normalized data
    	for (int x=0; x < 60; ++x) {
    		for (int y=0 ; y< repeatingColumns+2; ++y) {
    			
    			System.out.print(normalizedData[x][y]+" ");
    		}
    		System.out.println("");
    	}
       System.out.println("Total Count: "+counter);		
    }
	
	static String[] getRowElementsFromCSV (BufferedReader br) throws IOException {
    	
    	//get all of the header names from the first row the file
    	// last read from this file should be null
       	String line = br.readLine(); // read a single life from the Buffer Reader
    	String [] fieldTitles = {};
    	fieldTitles = line.split(",");
    	   	
    	    	       	
    	return fieldTitles;
    }
	
	static String[] parseStringToArray(String line) {
		
		String elements[] = {};
		elements = line.split(",");
		return elements;
	}
	
	
	public static void main(String[] args) throws IOException {
		
		
		List<String> HeaderNames = 	readFromCSVIntoArray("2017-18_OSAP_Data.csv");
	//	 HeaderNames = 	readFromCSVIntoArray("2016-17 OSAP Data.csv");
	//	 HeaderNames = 	readFromCSVIntoArray("2015-16 OSAP Data.csv");
	//	HeaderNames = 	readFromCSVIntoArray("2014-15 OSAP Data.csv");
	//	HeaderNames = 	readFromCSVIntoArray("2013-14 OSAP Data.csv");
	//	HeaderNames = 	readFromCSVIntoArray("2012-13 OSAP Data.csv");
		//String RepeatedColumns[][] = getRepeatingColumnsFromCSV("2016-17 OSAP Data.csv",4);
		normalizeColumnsFromCSV(HeaderNames,4);		
		System.out.println("Number for Repeated Rows: " +getNumberofListElements(HeaderNames));
		System.out.println("Number of Header Fields: "+getNumberofColumnsfromString(getColumnFieldNames(HeaderNames)));   
		

		
	}
}
	
