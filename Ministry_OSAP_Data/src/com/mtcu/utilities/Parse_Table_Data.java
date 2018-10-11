
package com.mtcu.utilities;

import java.io.*;
import java.util.List; 
import java.util.ArrayList;

//import org.apache.commons.csv.*;

public class Parse_Table_Data {

	
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
		BufferedReader br = new BufferedReader (new InputStreamReader ( new FileInputStream(input_filename),"UTF-8")); //configure the file access protocol
		String line = null; // initialize the variable
		List<String> dataFileRows = new ArrayList<String>(); //declare the List variable as an ArrayList composed of Strings
		line = br.readLine(); // read the initial line from the input file
		while (line != null) { // loop through reading a single line from the input file until a null string is reached. End of File
			line = line.trim(); // trim the white spaces from the beginning and end of each line
		 	dataFileRows.add(line); // add the line (String) to the List
		 	line = br.readLine(); //read a single line from the file input stream
		}
		br.close(); //close buffered read instance for the input file
		
		return dataFileRows;
	}
	
	static void writeArrayToCSV(String[][] elementsToWrite, String output_filename) throws IOException {
	
	// This method will write a two dimensional array to a file with the passed filename
	// This method will overwrite the existing file each time.
	BufferedWriter bw = new BufferedWriter(new OutputStreamWriter (new FileOutputStream(output_filename),"UTF-8"));	
	FileWriter fw = null;
	
	fw = new FileWriter(output_filename);
	bw = new BufferedWriter(fw);
	
	for (int rows=0; rows<elementsToWrite.length; ++rows) {
		
		for (int cols=0;cols <elementsToWrite[0].length; ++cols) {
			if (cols == (elementsToWrite[0].length-1)) {
				bw.write(elementsToWrite[rows][cols]+"\n"); // no comma appended after the last element in row	
			} else 
				
			{
				bw.write(elementsToWrite[rows][cols]+","); // add a comma after each element
			}
		}
		
	}
	
	
	if (bw !=null) {
		bw.close();
		}
	if (fw !=null) {
		fw.close();
		}
	}
	
	
	
	
    static void flattenColumnsFromCSV(List<String> referenceList, int repeatingColumns, String output_filename) throws IOException, FileNotFoundException{
    	//Read the repeating columns and rows into an array.
    	
    	int totalRowCount =getNumberofListElements(referenceList); //get total row count from original file
    	String[] dataColumns = getColumnFieldNames(referenceList); //get first row headers
    	int dataColumnsCount = getNumberofColumnsfromString(dataColumns); //get count for first header rows
    	int counter=1;
    	
    	// Rows = Take total rows minus 1 for header that will not repeat from existing List and multiply it by the number of data columns to give the new list size
    	// Rows continued Add 1 additional row for the header
    	// Columns = Take the number of repeating columns defined by the user and two (2) 1 for the repeating field name and 1 for the data value
    	// All data will be stored as strings
    	String[][] flattenedData = new String[(totalRowCount-1)*(dataColumnsCount-repeatingColumns)+1][repeatingColumns+2];
    	
    	//need to store a row of headers just the initial columns that will repeat.
    	
    	// this code below will manage the actual data being flattened into two vertical columns after the repeating columns.
    	
    	for (int dataCols= repeatingColumns; dataCols < dataColumnsCount; ++dataCols) {
    		for (int rows=1; rows < totalRowCount ; ++rows ) {
    			for (int cols =0 ; cols <  repeatingColumns +2 ; ++ cols) {
    				if (cols <repeatingColumns) {	
    				flattenedData[counter][cols] = getListEntry(referenceList,rows)[cols];
    				}
    				if (cols == repeatingColumns ) {
    					flattenedData[counter][cols]=dataColumns[dataCols];
    					                                                               
    					}
    				
    				if (cols == repeatingColumns+1 ) {
    					flattenedData[counter][cols] =getListEntry(referenceList,rows)[dataCols];
    					                                                               
    					}
    				}
    			++counter;
    			}
    			
    	}	
    	
    	// Populate the header row of the new normalized Data
    	for (int i=0; i<repeatingColumns; ++ i) {
    		flattenedData[0][i]=getListEntry(referenceList,0)[i];
    	}
    	
    	flattenedData[0][repeatingColumns]="Metric Description";
    	flattenedData[0][repeatingColumns+1]="Metric Value";
    	//print out normalized data
    	for (int x=0; x < 60; ++x) {
    		for (int y=0 ; y< repeatingColumns+2; ++y) {
    			
    			System.out.print(flattenedData[x][y]+" ");
    		}
    		System.out.println(""); // 
    	}
       System.out.println("Total Count: "+counter);		
       System.out.println("Total Rows: "+totalRowCount);
       
       writeArrayToCSV(flattenedData,output_filename);
    }
	
	static String[] getRowElementsFromCSV (BufferedReader br) throws IOException {
    	
    	//get all of the header names from the first row the file
    	// last read from this file should be null
       	String line = br.readLine(); // read a single life from the Buffer Reader
    	String [] fieldTitles = {};
    	fieldTitles = line.split(","); //split the elements in a string at each occurrence of a comma (',')
    	   	
    	    	       	
    	return fieldTitles;
    }
	
	static String[] parseStringToArray(String line) {
		
		String elements[] = {};
		elements = line.split(","); //split the elements in a string at each occurrence of a comma (',')
		return elements;
	}
	
	
	public static void main(String[] args) throws IOException {
		
		List<String> DataFromSource = null;
		
		DataFromSource = 	readFromCSVIntoArray("2017-18 OSAP Data.csv");
		flattenColumnsFromCSV(DataFromSource,4,"2017-18 OSAP Data Flattened.csv");
		
		DataFromSource = null;
		DataFromSource = 	readFromCSVIntoArray("2016-17 OSAP Data.csv");
		flattenColumnsFromCSV(DataFromSource,4,"2016-17 OSAP Data Flattened.csv");
		
		DataFromSource = null;
		DataFromSource = 	readFromCSVIntoArray("2015-16 OSAP Data.csv");
		flattenColumnsFromCSV(DataFromSource,4,"2015-16 OSAP Data Flattened.csv");
		
		DataFromSource = null;
		DataFromSource = 	readFromCSVIntoArray("2014-15 OSAP Data.csv");
		flattenColumnsFromCSV(DataFromSource,4,"2014-15 OSAP Data Flattened.csv");

		DataFromSource = null;
		DataFromSource = 	readFromCSVIntoArray("2013-14 OSAP Data.csv");
		flattenColumnsFromCSV(DataFromSource,4,"2013-12 OSAP Data Flattened.csv");
		
		DataFromSource = null;
		DataFromSource = 	readFromCSVIntoArray("2012-13 OSAP Data.csv");
		flattenColumnsFromCSV(DataFromSource,4,"2012-13 OSAP Data Flattened.csv");

		//String RepeatedColumns[][] = getRepeatingColumnsFromCSV("2016-17 OSAP Data.csv",4);
		
		//System.out.println("Number for Repeated Rows: " +getNumberofListElements(DataFromSource));
		//System.out.println("Number of Header Fields: "+getNumberofColumnsfromString(getColumnFieldNames(DataFromSource)));   
		

		
	}
}
	
