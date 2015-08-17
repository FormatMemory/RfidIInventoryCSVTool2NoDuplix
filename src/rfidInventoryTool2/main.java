package rfidInventoryTool2;

import java.awt.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
//import java.io.Console;
import java.util.Scanner;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;


public class main {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String workspace =  ".\\"; //".\\.\\file\\";
		
		
		System.out.println("     ------Program Start------\n");
		
		System.out.println("     -----------------*----------------*--------------------");
		System.out.println("Please make sure the input file format are '*.csv'.");
		System.out.println("Please make sure extra information in input file has been deleted.");
		System.out.println("And headers should be in the first line in input file.");
		//System.out.println("Please make sure the input master inventory file master key and UPC column formats are [fraction]");
		System.out.println("     -----------------*-----------------*-------------------\n");
		System.out.println("Workspace:  "+workspace);
		System.out.println();
		
	
		System.out.println("Enter date in your File name, For example: 2015_08_11");
	    Scanner sc = new Scanner(System.in);
	    String targetFileName = sc.nextLine();
	    
	    System.out.println("How many files you want to deal with? ");
	    Scanner scNum = new Scanner(System.in);
	    Integer filenum = Integer.parseInt(scNum.nextLine());
	    try
	    {
	    	int i=0;
			while(i<filenum)
			{
				String thisFileName = String.valueOf(i+1)+"_" + targetFileName+"-CDT_reads.csv";
				String outFileName = "Output_";
				System.out.println(thisFileName);
				outFileName = outFileName+String.valueOf(i+1)+"_" + targetFileName+"-CDT_reads.csv";
				String outFilePath = workspace+"Output\\";
				ArrayList<ArrayList<String>> finalList = readFromScan(thisFileName,workspace+thisFileName);
				Integer numMiss = writeToFile(finalList, outFileName,outFilePath);
				 
				i++;
				
				System.out.println();
			}
	    }
		
		catch (Exception e)
	    {
				e.printStackTrace();
		}
	    finally
		{
	    	System.out.println("\n      ------Program Finish------");
	    	System.in.read();
		}
	
		
		
		
		
	}
	public static ArrayList<ArrayList<String>> readFromScan(String fileName, String filePath) throws IOException
	{
		HashSet<String> EPCSet = new HashSet<String>();
		ArrayList<ArrayList<String>> finalList = new ArrayList<ArrayList<String>>();
		Reader in = new FileReader(filePath);
		Iterable<CSVRecord> records = CSVFormat.EXCEL.withHeader().parse(in);
		for (CSVRecord record : records) {
			String s = record.get("epc");
			if(!EPCSet.contains(s))
			{
				EPCSet.add(s);
				ArrayList<String> temp = new ArrayList<String>();
				for(int i = 0; i<12;i++)
				{
					temp.add(record.get(i));
				}
				
				finalList.add(temp);
			}
				
		}
		
		System.out.println("-File--"+fileName+" -Readed---"+finalList.size()+"--Records Has Been Readed.");
		return finalList;
	}
	
	public static int writeToFile(ArrayList<ArrayList<String>>finalList, String fileName, String filePath)
	{
		//create folder
		File file = new File(filePath);
		if (!file.exists()) {
			if (file.mkdir()) {
				System.out.println("Directory is created!");
			} else {
				System.out.println("Failed to create directory!");
			}
		}
		
		filePath = filePath+fileName;
		
		//....
				Integer retNum = finalList.size();
				
				FileWriter fileWriter = null;
				CSVPrinter csvFilePrinter = null;
			    //Create the CSVFormat object with "\n" as a record delimiter
				CSVFormat csvFileFormat = CSVFormat.EXCEL.withRecordSeparator("\n");
				 
			    try {
			       // String fileName;
					//initialize FileWriter object
			        fileWriter = new FileWriter(filePath);
			        //initialize CSVPrinter object
			        csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);
			        //Create CSV file header
			        csvFilePrinter.printRecord("time", "reader",	"port", "beam", "epc",	"crc",	"rssi", "session", "target", "power", "field", "zone");

			        //Write a new student object list to the CSV file
			        for (ArrayList<String> temp : finalList) {
			            //System.out.println(temp);
			            csvFilePrinter.printRecord(temp);
			        }
			      // System.out.println("CSV file was created successfully !!!");
			         
			    } catch (Exception e) {
			        System.out.println("Error in CsvFileWriter !!!");
			        e.printStackTrace();
			    } finally {
			        try {
			            fileWriter.flush();
			            fileWriter.close();
			            csvFilePrinter.close();
			        } catch (IOException e) {
			            System.out.println("Error while flushing/closing fileWriter/csvPrinter !!!");
			                e.printStackTrace();
			            }
				//System.out.println("End");
			    	}
				
				System.out.println("----Items' Information of---"+retNum+"--Records Have Been Write to File: "+fileName);
				return retNum;
	}

}
