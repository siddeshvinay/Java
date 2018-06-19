package com.gtnexus.selenium.core.utils;

import java.io.File;

/*HSSF (Horrible SpreadSheet Format) – reads and writes Microsoft Excel (XLS) format files.
It can read files written by Excel 97 onwards; this file format is known as the BIFF 8 format.
*/

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import sun.text.resources.FormatData;


public class ExcelDataReaderWriter {
	
	
//	public  static String filename=GlobalVariables.KEYWORKFILEPATH;			//System.getProperty("user.dir").concat(PropertiesHolder.properties.get("tmsprops").getProperty("RunTimeDataFilePath"));			//src\\config\\testcases\\TestData.xlsx";
	
		public  static FileInputStream fis=null;
		public  static FileOutputStream fileOut=null;	
		
		private static HSSFWorkbook workbook = null;
		private static HSSFSheet sheet = null;
		private static HSSFRow row =null;
		private static HSSFCell cell = null;	
		
		private static HSSFWorkbook workbook1 = null;
		private static HSSFSheet sheet1 = null;
		private static HSSFCell cell1=null;
		
		/*private static Workbook workbook = null;
		private static Sheet sheet = null;
		private static Row row   =null;
		private static Cell cell = null;		//hss for .xls				
	    */
	
		/**
		 * @author Sivashankar.G
		 * @param 		 
		 * @throws file not found exception
		 */
						
	public  ExcelDataReaderWriter() {
			
			String fname=GlobalVariables.KEYWORKFILEPATH;
			
			try {
			      InputStream inp = new FileInputStream(fname);
			      			     
		    	  POIFSFileSystem fs = new POIFSFileSystem(inp);
		    	  workbook = new HSSFWorkbook(fs);
		    	  log("xls="+workbook.getSheetName(0));
		    	  sheet = workbook.getSheetAt(0);			  						
		    	  fis.close();	
		    	  
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
		}
	
	
	
	/**
	 * This method is responsible to get the row count of the given sheet.
	 * @param sheetName
	 * @return
	 */
	
		// returns the row count in a sheet
		public static  int gtnGetRowCount(String sheetName){			
			int index = workbook.getSheetIndex(sheetName);
			if(index==-1)
				return 0;
			else{
			sheet = workbook.getSheetAt(index);
			int number=sheet.getLastRowNum()+1;
			return number;
			}
			
		}
		
		
	/**
	 * This method is responsible to get the column count of the row.	
	 * @param sheetName
	 * @return
	 */
			
			
	// returns the row count in a sheet
	public static  int gtnGetColumnCount(String sheetName, int rownum){			
		int index = workbook.getSheetIndex(sheetName);
		if(index==-1)
			return 0;
		else{
		sheet = workbook.getSheetAt(index);
		int noOfColumns = sheet.getRow(rownum).getLastCellNum();
		return noOfColumns;
		}
		
	}	
		
		
			
		/**
		 * @author Sivashankar.G
		 * @param param
		 * @return returns the data from a cell
		 * @throws Throwable
		 */
		
		public  static String gtnGetCellData(String sheetName,int colNum,int rowNum){
			try{
				if(rowNum <=0)
					return "";
			
			int index = workbook.getSheetIndex(sheetName);

			if(index==-1)
				return "";
			sheet = workbook.getSheetAt(index);
			row = sheet.getRow(rowNum-1);
			if(row==null)
				return "";
			cell = row.getCell(colNum);
			if(cell==null)
				return "";
			
		  if(cell.getCellType()==Cell.CELL_TYPE_STRING)
			  return cell.getStringCellValue();
		  else if(cell.getCellType()==Cell.CELL_TYPE_NUMERIC || cell.getCellType()==Cell.CELL_TYPE_FORMULA ){
			  
			  String cellText  = String.valueOf(cell.getNumericCellValue());
			  if (HSSFDateUtil.isCellDateFormatted(cell)) {
		           // format in form of M/D/YY
				  double d = cell.getNumericCellValue();

				  Calendar cal =Calendar.getInstance();
				  cal.setTime(HSSFDateUtil.getJavaDate(d));
		            cellText =
		             (String.valueOf(cal.get(Calendar.YEAR))).substring(2);
		           cellText = cal.get(Calendar.MONTH)+1 + "/" +
		                      cal.get(Calendar.DAY_OF_MONTH) + "/" +
		                      cellText;		           
		         }
		    return cellText;
		  }else if(cell.getCellType()==Cell.CELL_TYPE_BLANK)
		      return "";
		  else 
			  return String.valueOf(cell.getBooleanCellValue());
			}
			catch(Exception e){
				
				e.printStackTrace();
				return "row "+rowNum+" or column "+colNum +" does not exist  in xls";
			}
		}
		
		
	/**
	 * @author Sivashankar.G
	 * @param param
	 * @return
	 * @throws Throwable
	 */			
	  // find whether sheets exists	
		public boolean gtnIsSheetExist(String sheetName){
			int index = workbook.getSheetIndex(sheetName);
			if(index==-1){
				index=workbook.getSheetIndex(sheetName.toUpperCase());
					if(index==-1)
						return false;
					else
						return true;
			}
			else
				return true;
		}
		
		/**
		 * @author Sivashankar.G
		 * @param param
		 * @return
		 * @throws Throwable
		 */
		
		// returns number of columns in a sheet	
		public int getColumnCount(String sheetName){
			// check if sheet exists
			if(!gtnIsSheetExist(sheetName))
			 return -1;
			
			sheet = workbook.getSheet(sheetName);
			row = sheet.getRow(0);
			
			if(row==null)
				return -1;
			
			return row.getLastCellNum();		
	   }
				
		/**
		 * @author ckumar2
		 * @param param
		 * @return
		 * @throws Throwable
		 */
		
  public static boolean gtnSetCellDatavalue(String sheetName,String colName, String data,String TCN){
		
	  try{		  
		    String path=GlobalVariables.KEYWORKFILEPATH;
		    
		    fis = new FileInputStream(path); 
			workbook = new HSSFWorkbook(fis);
		
			int index = workbook.getSheetIndex(sheetName);
			int colNum=-1;
			int rowNumber=-1;
			if(index==-1)
				return false;
			
			sheet = workbook.getSheetAt(index);		
			row=sheet.getRow(0);
			for(int i=0;i<row.getLastCellNum();i++){
				//System.out.println(row.getCell(i).getStringCellValue().trim());
				if(row.getCell(i).getStringCellValue().trim().equals(colName))
					colNum=i;
			}
			
			for(int j=1;j<=gtnGetRowCount(sheetName);j++){
				
				  for(int k=0;k<=row.getLastCellNum();k++){
					String TestCase=gtnGetCellData(sheetName, k, j) ;
					if(TestCase.equalsIgnoreCase(TCN)){
						rowNumber=j;
					}
					
				  }					
			  }
			
			if(colNum==-1)
			  return false;
			
			sheet.autoSizeColumn(colNum); 
			row = sheet.getRow(rowNumber-1);
			if (row == null)
				row = sheet.createRow(rowNumber-1);			
				cell=row.getCell(colNum);	
			
			if (cell == null)
		        cell = row.createCell(colNum);
				cell.setCellValue(data);
				fileOut = new FileOutputStream(path);
				workbook.write(fileOut);
				fileOut.close();	
		  }
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
			return true;
	}
  
  
  
	/**
	 * @author ckumar2
	 * @param param
	 * @return
	 * @throws Throwable
	 */
	
public static boolean gtnSetCellDatavalue(String sheetName,String path,String colName, String data,String TCN){
	
try{		  
	    	    
	    fis = new FileInputStream(path); 
		workbook = new HSSFWorkbook(fis);
	
		int index = workbook.getSheetIndex(sheetName);
		int colNum=-1;
		int rowNumber=-1;
		if(index==-1)
			return false;
		
		sheet = workbook.getSheetAt(index);		
		row=sheet.getRow(0);
		for(int i=0;i<row.getLastCellNum();i++){
			//System.out.println(row.getCell(i).getStringCellValue().trim());
			if(row.getCell(i).getStringCellValue().trim().equals(colName))
				colNum=i;
		}
		
		for(int j=1;j<=gtnGetRowCount(sheetName);j++){
			
			  for(int k=0;k<=row.getLastCellNum();k++){
				String TestCase=gtnGetCellData(sheetName, k, j) ;
				if(TestCase.equalsIgnoreCase(TCN)){
					rowNumber=j;
				}
				
			  }					
		  }
		
		if(colNum==-1)
		  return false;
		
		sheet.autoSizeColumn(colNum); 
		row = sheet.getRow(rowNumber-1);
		if (row == null)
			row = sheet.createRow(rowNumber-1);			
			cell=row.getCell(colNum);	
		
		if (cell == null)
	        cell = row.createCell(colNum);
			cell.setCellValue(data);
			fileOut = new FileOutputStream(path);
			workbook.write(fileOut);
			fileOut.close();	
	  }
	catch(Exception e){
		e.printStackTrace();
		return false;
	}
		return true;
}
  
  
  
  /**
	 * @author ckumar2
	 * @param param
	 * @return
	 * @throws Throwable
	 */
	
public static boolean gtnSetCellDatavalue(String colName, String data,String TCN){
	
try{		  
	    String path=GlobalVariables.KEYWORKFILEPATH;
	    
		fis = new FileInputStream(path); 
		workbook = new HSSFWorkbook(fis);
		
		String sheetName="RunTimeData";
	
		int index = workbook.getSheetIndex(sheetName);
		int colNum=-1;
		int rowNumber=-1;
		if(index==-1)
			return false;
		
		sheet = workbook.getSheetAt(index);		
		row=sheet.getRow(0);
		for(int i=0;i<row.getLastCellNum();i++){
			//System.out.println(row.getCell(i).getStringCellValue().trim());
			if(row.getCell(i).getStringCellValue().trim().equals(colName))
				colNum=i;
		}
		
		for(int j=1;j<=gtnGetRowCount(sheetName);j++){
			
			  for(int k=0;k<=row.getLastCellNum();k++){
				String TestCase=gtnGetCellData(sheetName, k, j) ;
				if(TestCase.equalsIgnoreCase(TCN)){
					rowNumber=j;
				}
				
			  }					
		  }
		
		if(colNum==-1)
		  return false;
		
		sheet.autoSizeColumn(colNum); 
		row = sheet.getRow(rowNumber-1);
		if (row == null)
			row = sheet.createRow(rowNumber-1);			
			cell=row.getCell(colNum);	
		
		if (cell == null)
	        cell = row.createCell(colNum);
			cell.setCellValue(data);
			fileOut = new FileOutputStream(path);
			workbook.write(fileOut);
			fileOut.close();	
	  }
	catch(Exception e){
		e.printStackTrace();
		return false;
	}
		return true;
}
   
  
		
		/**
		 * @author ckumar2
		 * @param param
		 * @return
		 * @throws Throwable
		 */
		
	public static String gtnGetCellValue(String sheetName,String colName,String TCN){
	
			try{	
				
			    String path=GlobalVariables.KEYWORKFILEPATH;
			    
				fis = new FileInputStream(path); 				
				workbook = new HSSFWorkbook(fis);
						
				int colNum=0;
				int rowNum=0;				
				System.out.println("sheetName "+sheetName);
				int index=workbook.getSheetIndex(sheetName);
				
				int col_Num=-1;
				if(index==-1)
					return "";
				
				sheet=workbook.getSheetAt(index);
				row=sheet.getRow(0);
				for(int i=0;i<row.getLastCellNum();i++){
					
					if(row.getCell(i).getStringCellValue().trim().equals(colName))
						colNum=i;
				}				
				
				for(int j=1;j<=gtnGetRowCount(sheetName);j++){
					
					  for(int k=0;k<=row.getLastCellNum();k++){
						String TestCase=gtnGetCellData(sheetName, k, j) ;
						if(TestCase.equalsIgnoreCase(TCN)){
							 rowNum=j;
						}
						
					  }						
				}
				
				
			if(rowNum <=0)
				return "";			
			 index = workbook.getSheetIndex(sheetName);

			if(index==-1)
				return "";			
			sheet = workbook.getSheetAt(index);
			row = sheet.getRow(rowNum-1);
			if(row==null)
				return "";
			cell = row.getCell(colNum);
			if(cell==null)
				return "";
			
		  if(cell.getCellType()==Cell.CELL_TYPE_STRING)
			  return cell.getStringCellValue();
		  else if(cell.getCellType()==Cell.CELL_TYPE_NUMERIC || cell.getCellType()==Cell.CELL_TYPE_FORMULA ){
			  
			  String cellText  = String.valueOf(cell.getNumericCellValue());
			  if (HSSFDateUtil.isCellDateFormatted(cell)) {
		           // format in form of M/D/YY
				  double d = cell.getNumericCellValue();

				  Calendar cal =Calendar.getInstance();
				  cal.setTime(HSSFDateUtil.getJavaDate(d));
		            cellText =
		             (String.valueOf(cal.get(Calendar.YEAR))).substring(2);
		           cellText = cal.get(Calendar.MONTH)+1 + "/" +
		                      cal.get(Calendar.DAY_OF_MONTH) + "/" +
		                      cellText;		       
		         }
  
			  return cellText;
		  }else if(cell.getCellType()==Cell.CELL_TYPE_BLANK)
		      return "";
		  else 
			  return String.valueOf(cell.getBooleanCellValue());
			}
		catch(Exception e){			
			e.printStackTrace();
			return "row "+colName+" or column "+TCN +" does not exist  in xls";
		}
	}
	
	
	
	/**
	 * @author ckumar2
	 * @param param
	 * @return
	 * @throws Throwable
	 */
	
public static String gtnGetCellDataValue(String sheetName,String path,String colName,String TCN){

		try{				
		   		    
			fis = new FileInputStream(path); 				
			workbook = new HSSFWorkbook(fis);
					
			int colNum=0;
			int rowNum=0;				
			System.out.println("sheetName "+sheetName);
			int index=workbook.getSheetIndex(sheetName);
			
			int col_Num=-1;
			if(index==-1)
				return "";
			
			sheet=workbook.getSheetAt(index);
			row=sheet.getRow(0);
			for(int i=0;i<row.getLastCellNum();i++){
				
				if(row.getCell(i).getStringCellValue().trim().equals(colName))
					colNum=i;
			}				
			
			for(int j=1;j<=gtnGetRowCount(sheetName);j++){
				
				  for(int k=0;k<=row.getLastCellNum();k++){
					String TestCase=gtnGetCellData(sheetName, k, j) ;
					if(TestCase.equalsIgnoreCase(TCN)){
						 rowNum=j;
					}
					
				  }						
			}
			
			
		if(rowNum <=0)
			return "";			
		 index = workbook.getSheetIndex(sheetName);

		if(index==-1)
			return "";			
		sheet = workbook.getSheetAt(index);
		row = sheet.getRow(rowNum-1);
		if(row==null)
			return "";
		cell = row.getCell(colNum);
		if(cell==null)
			return "";
		
	  if(cell.getCellType()==Cell.CELL_TYPE_STRING)
		  return cell.getStringCellValue();
	  else if(cell.getCellType()==Cell.CELL_TYPE_NUMERIC || cell.getCellType()==Cell.CELL_TYPE_FORMULA ){
		  
		  String cellText  = String.valueOf(cell.getNumericCellValue());
		  if (HSSFDateUtil.isCellDateFormatted(cell)) {
	           // format in form of M/D/YY
			  double d = cell.getNumericCellValue();

			  Calendar cal =Calendar.getInstance();
			  cal.setTime(HSSFDateUtil.getJavaDate(d));
	            cellText =
	             (String.valueOf(cal.get(Calendar.YEAR))).substring(2);
	           cellText = cal.get(Calendar.MONTH)+1 + "/" +
	                      cal.get(Calendar.DAY_OF_MONTH) + "/" +
	                      cellText;		       
	         }

		  return cellText;
	  }else if(cell.getCellType()==Cell.CELL_TYPE_BLANK)
	      return "";
	  else 
		  return String.valueOf(cell.getBooleanCellValue());
		}
	catch(Exception e){			
		e.printStackTrace();
		return "row "+colName+" or column "+TCN +" does not exist  in xls";
	}
}
	

	/**
	 * @author ckumar2
	 *  @param param
	 * @return
	 * @throws Throwable
	 */
	
public static String gtnGetCellValue(String colName,String TCN){

		try{	
			
		    String path=GlobalVariables.KEYWORKFILEPATH;
		    
			fis = new FileInputStream(path); 
			workbook = new HSSFWorkbook(fis);
					
			int colNum=0;
			int rowNum=0;				
			
			String sheetName="RunTimeData";
			
			int index=workbook.getSheetIndex(sheetName);
			
			int col_Num=-1;
			if(index==-1)
				return "";
			
			sheet=workbook.getSheetAt(index);
			row=sheet.getRow(0);
			for(int i=0;i<row.getLastCellNum();i++){
				
				if(row.getCell(i).getStringCellValue().trim().equals(colName))
					colNum=i;
			}				
			
			for(int j=1;j<=gtnGetRowCount(sheetName);j++){
				
				  for(int k=0;k<=row.getLastCellNum();k++){
					String TestCase=gtnGetCellData(sheetName, k, j) ;
					if(TestCase.equalsIgnoreCase(TCN)){
						 rowNum=j;
					}
					
				  }						
			}
			
			
		if(rowNum <=0)
			return "";			
		 index = workbook.getSheetIndex(sheetName);

		if(index==-1)
			return "";			
		sheet = workbook.getSheetAt(index);
		row = sheet.getRow(rowNum-1);
		if(row==null)
			return "";
		cell = row.getCell(colNum);
		if(cell==null)
			return "";
		
	  if(cell.getCellType()==Cell.CELL_TYPE_STRING)
		  return cell.getStringCellValue();
	  else if(cell.getCellType()==Cell.CELL_TYPE_NUMERIC || cell.getCellType()==Cell.CELL_TYPE_FORMULA ){
		  
		  String cellText  = String.valueOf(cell.getNumericCellValue());
		  if (HSSFDateUtil.isCellDateFormatted(cell)) {
	           // format in form of M/D/YY
			  double d = cell.getNumericCellValue();

			  Calendar cal =Calendar.getInstance();
			  cal.setTime(HSSFDateUtil.getJavaDate(d));
	            cellText =
	             (String.valueOf(cal.get(Calendar.YEAR))).substring(2);
	           cellText = cal.get(Calendar.MONTH)+1 + "/" +
	                      cal.get(Calendar.DAY_OF_MONTH) + "/" +
	                      cellText;		       
	         }

		  return cellText;
	  }else if(cell.getCellType()==Cell.CELL_TYPE_BLANK)
	      return "";
	  else 
		  return String.valueOf(cell.getBooleanCellValue());
		}
	catch(Exception e){			
		e.printStackTrace();
		return "row "+colName+" or column "+TCN +" does not exist  in xls";
	}
}




/**
 * @author ckumar2
 * @param param
 * @return returns the data from a cell
 * @throws Throwable
 */

public  static String gtnGetCellData(String path,String sheetName,int colNum,int rowNum){

try{
		
	fis = new FileInputStream(path); 
	workbook = new HSSFWorkbook(fis);		
															
	if(rowNum <=0)
	 return "";
	
	int index = workbook.getSheetIndex(sheetName);
	//System.out.println("index value is "+index);

	if(index==-1)
		return "";
	sheet = workbook.getSheetAt(index);
	row = sheet.getRow(rowNum-1);
	if(row==null)
		return "";
	cell = row.getCell(colNum);
	if(cell==null)
		return "";
	
  if(cell.getCellType()==Cell.CELL_TYPE_STRING)
	  return cell.getStringCellValue();
  else if(cell.getCellType()==Cell.CELL_TYPE_NUMERIC || cell.getCellType()==Cell.CELL_TYPE_FORMULA ){
	  
	  String cellText  = String.valueOf(cell.getNumericCellValue());
	  if (HSSFDateUtil.isCellDateFormatted(cell)) {
           // format in form of M/D/YY
		  double d = cell.getNumericCellValue();

		  Calendar cal =Calendar.getInstance();
		  cal.setTime(HSSFDateUtil.getJavaDate(d));
            cellText =
             (String.valueOf(cal.get(Calendar.YEAR))).substring(2);
           cellText = cal.get(Calendar.MONTH)+1 + "/" +
                      cal.get(Calendar.DAY_OF_MONTH) + "/" +
                      cellText;		           
         }
    return cellText;
  }else if(cell.getCellType()==Cell.CELL_TYPE_BLANK)
      return "";
  else 
	  return String.valueOf(cell.getBooleanCellValue());
	}
	catch(Exception e){
		
		e.printStackTrace();
		return "row "+rowNum+" or column "+colNum +" does not exist  in xls";
	}
}		


/**
 * 
 * @param sheetName
 * @param path
 * @param colName
 * @param data
 * @param RowNum
 * @param rowNumber
 * @return
 */
		
public static boolean gtnSetStrngCellDatavalue(String sheetName,String path,String colName, String data,int RowNum,int rowNumber){
		
	try{
		
		FileInputStream fis;
		FileOutputStream fileOut;
		
		HSSFSheet sheet1;
		HSSFCell cell1;
		HSSFRow	row1;			
		
	    fis=new FileInputStream(path); 	    
		HSSFWorkbook wb= new HSSFWorkbook(fis);
	
		int index=wb.getSheetIndex(sheetName);
		int colNum=-1;
		
		if(index==-1)
			return false;
							
		sheet1 = wb.getSheetAt(index);		
		//row1=sheet1.getRow(RowNum);  // static row. 4
		row1=sheet1.getRow(RowNum);  // static row. 4
		String val = "";
		
		for(int i=0;i<row1.getLastCellNum();i++){
			
			/*if(row.getCell(i).getStringCellValue().trim().equals(colName))
				colNum=i;
		    }*/
			
			cell1 = row1.getCell(i);			
						
			if (cell1 != null) { // the cell does not have a blank value
            
                switch (cell1.getCellType()) {
                case HSSFCell.CELL_TYPE_STRING:
                    val = cell1.getStringCellValue();
                    break;
                case HSSFCell.CELL_TYPE_NUMERIC:
                    val = Double.toString(cell1.getNumericCellValue());
                    break;
                case HSSFCell.CELL_TYPE_BOOLEAN:
                    val = Boolean.toString(cell1.getBooleanCellValue());
                    break;
                case HSSFCell.CELL_TYPE_FORMULA:
                    cell1.getCellFormula();
                    break;
                }
            }
			
			//System.out.println("val-->"+val);

			if(val.equalsIgnoreCase(colName))
				colNum=i;				
				continue; 
			}			
																	
		if(colNum==-1)
		  return false;
		
		sheet1.autoSizeColumn(colNum); 
		row1 = sheet1.getRow(rowNumber-1);
		if (row1 == null)
			row1 = sheet1.createRow(rowNumber-1);			
			cell1=row1.getCell(colNum);	
		
		if (cell1 == null)
			cell1 = row1.createCell(colNum);
			cell1.setCellValue(data);				
			fileOut = new FileOutputStream(path);
			wb.write(fileOut);
			fileOut.close();	
	  }

	catch(Exception e){
		e.printStackTrace();
		return false;
	}
		return true;
	
}


/**
 * 
 * @param sheetName
 * @param path
 * @param colName
 * @param data
 * @param RowNum
 * @param rowNumber
 * @return
 */

 
		
public static boolean gtnSetCellDatavalue(String sheetName,String path,String colName, String data,int RowNum,int rowNumber){
		
	try{
		
		FileInputStream fis;
		FileOutputStream fileOut;
		
		HSSFSheet sheet1;
		HSSFCell cell1;
		HSSFRow	row1;			
		
	    fis=new FileInputStream(path); 	    
		HSSFWorkbook wb= new HSSFWorkbook(fis);
	
		int index=wb.getSheetIndex(sheetName);
		int colNum=-1;
		
		if(index==-1)
			return false;
							
		sheet1 = wb.getSheetAt(index);		
		//row1=sheet1.getRow(RowNum);  // static row. 4
		row1=sheet1.getRow(RowNum);  // static row. 4
		//String val = "";
		
		for(int i=0;i<row1.getLastCellNum();i++){
			
			if(row1.getCell(i).getStringCellValue().trim().equals(colName))
				colNum=i;
		    }
			
		/*	cell1 = row1.getCell(i);			
						
			if (cell1 != null) { // the cell does not have a blank value
            
                switch (cell1.getCellType()) {
                case HSSFCell.CELL_TYPE_STRING:
                    val = cell1.getStringCellValue();
                    break;
                case HSSFCell.CELL_TYPE_NUMERIC:
                    val = Double.toString(cell1.getNumericCellValue());
                    break;
                case HSSFCell.CELL_TYPE_BOOLEAN:
                    val = Boolean.toString(cell1.getBooleanCellValue());
                    break;
                case HSSFCell.CELL_TYPE_FORMULA:
                    cell1.getCellFormula();
                    break;
                }
            }
			
			//System.out.println("val-->"+val);

			if(val.equalsIgnoreCase(colName))
				colNum=i;				
				continue; 
			}		*/	
																	
		if(colNum==-1)
		  return false;
		
		sheet1.autoSizeColumn(colNum); 
		row1 = sheet1.getRow(rowNumber-1);
		if (row1 == null)
			row1 = sheet1.createRow(rowNumber-1);			
			cell1=row1.getCell(colNum);	
		
		if (cell1 == null)
			cell1 = row1.createCell(colNum);
			cell1.setCellValue(data);				
			fileOut = new FileOutputStream(path);
			wb.write(fileOut);
			fileOut.close();	
	  }

	catch(Exception e){
		e.printStackTrace();
		return false;
	}
		return true;
	
}

	
			
/**
 * 
 * @param sheetName
 * @param path
 * @param colName
 * @param data
 * @param RowNum
 * @param rowNumber
 * @return
 */
		
public static boolean gtnSetCellIntDatavalue(String sheetName,String path,String colName, int data,int RowNum,int rowNumber){
		
	try{
		
		FileInputStream fis;
		FileOutputStream fileOut;
		
		HSSFSheet sheet1;
		HSSFCell cell1;
		HSSFRow	row1;			
		
	    fis=new FileInputStream(path); 	    
		HSSFWorkbook wb= new HSSFWorkbook(fis);
	
		int index=wb.getSheetIndex(sheetName);
		int colNum=-1;
		
		if(index==-1)
			return false;
							
		sheet1 = wb.getSheetAt(index);		
		row1=sheet1.getRow(RowNum);  // static row. 4
		String val = "";
		
		for(int i=0;i<row1.getLastCellNum();i++){
			
			cell1 = row1.getCell(i);
			
			if (cell1 != null) { // the cell does not have a blank value
            
                switch (cell1.getCellType()) {
                case HSSFCell.CELL_TYPE_STRING:
                    val = cell1.getStringCellValue();
                    break;
                case HSSFCell.CELL_TYPE_NUMERIC:
                    val = Double.toString(cell1.getNumericCellValue());
                    break;
                case HSSFCell.CELL_TYPE_BOOLEAN:
                    val = Boolean.toString(cell1.getBooleanCellValue());
                    break;
                case HSSFCell.CELL_TYPE_FORMULA:
                    cell1.getCellFormula();
                    break;
                }
            }
			//System.out.println("val-->"+val);

			if(val.equalsIgnoreCase(colName))
				colNum=i;				
				continue;
			}			
														
		if(colNum==-1)
		  return false;
		
		sheet1.autoSizeColumn(colNum); 
		row1 = sheet1.getRow(rowNumber-1);
		if (row1 == null)
			row1 = sheet1.createRow(rowNumber-1);			
			cell1=row1.getCell(colNum);	
		
		if (cell1 == null)
			cell1 = row1.createCell(colNum);
			cell1.setCellValue(data);				
			fileOut = new FileOutputStream(path);
			wb.write(fileOut);
			fileOut.close();	
	  }

	catch(Exception e){
		e.printStackTrace();
		return false;
	}
		return true;
	
}


		
	
private static void log(String message)
{
        System.out.println(message);
}





/**
 * 	@author ckumar2
 * @param sheetName
 * @param path
 * @param colName
 * @return
 */
	
	public static int gtnGetColNumByColName(String sheetName,String path,String colName){
	
			int colNum=0;
			int rowNum=0;		 
		
			try{							
					  		    
				fis = new FileInputStream(path); 				
				workbook = new HSSFWorkbook(fis);						
				
			//	System.out.println("sheetName "+sheetName);
				int index=workbook.getSheetIndex(sheetName);
																				
				sheet=workbook.getSheetAt(index);
				row=sheet.getRow(0);
				for(int i=0;i<row.getLastCellNum();i++){	
					
					if(row.getCell(i).getStringCellValue().trim().equalsIgnoreCase(colName))
						colNum=i;
				}	fis.close();		
	
			} catch(Exception e){
					Framework_Utils.gtnInfoLog("Exception is encountered.");
					colNum=-1;
				}			
			return colNum;	
	    }
	
	
	/**
	 * 	@author ckumar2
	 * @param sheetName
	 * @param path
	 * @param colName
	 * @return
	 */
		
	public static int gtnGetColNumByColName(String sheetName,String path,int rowNum,String colName){
		
				int colNum=0;				
			
				try{							
						  		    
					fis = new FileInputStream(path); 				
					workbook = new HSSFWorkbook(fis);		
					
				//	System.out.println("sheetName "+sheetName);
					int index=workbook.getSheetIndex(sheetName);																					
					sheet=workbook.getSheetAt(index);
					row=sheet.getRow(rowNum);					
					for(int i=0;i<row.getLastCellNum();i++){
						
						//
						cell=row.getCell(i);	
						 if (cell!=null) {
							// System.out.println("Column Value-->"+row.getCell(i).getStringCellValue().trim());
							if(row.getCell(i).getStringCellValue().trim().equalsIgnoreCase(colName))
								colNum=i;
						   }
						 }
						 fis.close();		
		
				} catch(Exception e){
						Framework_Utils.gtnInfoLog("Exception is encountered.");
						colNum=-1;
					}			
				return colNum;	
		    }	
		
	
	

	/**
	 * @author ckumar2	
	 * @param sheetName
	 * @param path
	 * @param colName
	 * @return
	 */
		
public static int gtnGetRowNumByName(String sheetName,String path,String colName){
					
				 int rowval=0;	 
			
				try{							
					
					fis = new FileInputStream(path); 
					workbook = new HSSFWorkbook(fis);
				
					int index=workbook.getSheetIndex(sheetName);	
					//System.out.println("index value is "+index);
					int rowNumber=-1;
					
					if(index==-1){
						return -1;
				     }
					else {sheet = workbook.getSheetAt(index);			
						rowNumber=sheet.getLastRowNum();				// get the empty RowNum;		
						//rowval=rowNumber;
					   
						for (int i = 0; i <= rowNumber; i++) {
							row=sheet.getRow(i);
							cell1 = row.getCell(i);
							
						if (cell1 != null) { // the cell does not have a blank value
															
							 String rowva=row.getCell(0).getStringCellValue().trim();
							 // System.out.println(rowva);															
								if (rowva.equalsIgnoreCase(colName)) {
									rowval=i;
									break;
								}
							}						 
							
						}						
						
					}		
				
				  fis.close();
				} catch(Exception e){
						Framework_Utils.gtnInfoLog("Exception is encountered.");
						rowval=-1;
					}			
				return rowval;	
		    }	
	

/**
 * @author ckumar2
 * @param param
 * @return
 * @throws Throwable
 */

 public static int gtnGetExclRowCount(String sheetName, String pathVal){
			
			int rowval=0;

		try{			
			   	System.out.println("pathVal "+pathVal);
			   	System.out.println("sheetName "+sheetName);
			   	
				fis = new FileInputStream(pathVal); 
				workbook = new HSSFWorkbook(fis);
			
				int index=workbook.getSheetIndex(sheetName);	
				//System.out.println("index value is "+index);
				int rowNumber=-1;
				
				if(index==-1){
					return -1;
			     }
				else {sheet = workbook.getSheetAt(index);			
					rowNumber=sheet.getLastRowNum();				// get the empty RowNum;		
					rowval=rowNumber;
				}		
				
		} catch(Exception e){
			return -1;
		}
			return rowval; 
			
		}	
					

/** This method is responsible to set the value to the xlsx file.
 * @author Shubhah		
 * @param sheetName
 * @param path
 * @param colName
 * @param data
 * @param RowNum
 * @param rowNumber
 * @return
 */
				

  public static boolean gtnSetXLCellDatavalue(String sheetName,String path,String colName, String data,int RowNum,int rowNumber){
			
		  try{		  
				    FileInputStream fis1;
				    fis1=fis;
				    fis=new FileInputStream(path); 
				    fis1=new FileInputStream(path);
				    
					workbook = new HSSFWorkbook(fis);					
					workbook1 = new HSSFWorkbook(fis1);				
					int index=workbook.getSheetIndex(sheetName);
				
					int colNum=-1;
					
					if(index==-1)
						return false;
										
					sheet = workbook.getSheetAt(index);
					sheet1 = workbook1.getSheetAt(index);
					row=sheet.getRow(RowNum);  // static row. 4
					for(int i=0;i<row.getLastCellNum();i++){
						if(row.getCell(i).getStringCellValue().trim().equalsIgnoreCase(colName))
							colNum=i;
					}
												
					if(colNum==-1)
					  return false;
					
					sheet.autoSizeColumn(colNum); 
					row = sheet.getRow(rowNumber-1);
					HSSFRow row1 = sheet1.getRow(rowNumber-1);

					if (row == null)
						row = sheet.createRow(rowNumber-1);			
						cell=row.getCell(colNum);	
					
					if (cell1 == null){
						HSSFCell cell1 =  row1.createCell(colNum);
						cell1.setCellValue(data);
						fileOut = new FileOutputStream(path);
						workbook1.write(fileOut);
						fileOut.close();
					}
				  }
				catch(Exception e){
					e.printStackTrace();
					return false;
				}
					return true;
		}
 
  
  

/** This method is responsible to set the value to the xls file.
 * @author Chethan Kumar
 * @param sheetName
 * @param path
 * @param colName
 * @param data
 * @param RowNum
 * @param rowNumber
 * @return
 */
			

  public static boolean gtnSetXLCellDatavalue(String sheetName,String path,String colName, int data,int RowNum,int rowNumber){
			
  try{		  
		    FileInputStream fis1;
		    fis1=fis;
		    
		    fis=new FileInputStream(path); 
		    fis1=new FileInputStream(path);
		    
			workbook=new HSSFWorkbook(fis);					
			workbook1=new HSSFWorkbook(fis1);
								
			int index=workbook.getSheetIndex(sheetName);
		
			int colNum=-1;
			if(index==-1)
				return false;
								
			sheet = workbook.getSheetAt(index);
			sheet1 = workbook1.getSheetAt(index);
			System.out.println("RowNum-->"+RowNum);
			row=sheet.getRow(RowNum-1);  // static row. 4	
			//row=sheet.getRow(RowNum);  // static row. 4
				
			for(int i=1;i<row.getLastCellNum();i++){								
											
			if(row.getCell(i).getStringCellValue().trim().equals(colName))
				colNum=i;					
			}						
			
			System.out.println("Column Number-->"+colNum+"colName-->"+colName);
									
			if(colNum==-1)
			  return false;
			
			sheet.autoSizeColumn(colNum); 
			row = sheet.getRow(rowNumber-1);
			HSSFRow row1 = sheet1.getRow(rowNumber-1);			
			
			if (row == null)
				row = sheet.createRow(rowNumber-1);			
				cell=row.getCell(colNum);	
			
			if (cell1 == null){
				HSSFCell cell1 =  row1.createCell(colNum);
				cell1.setCellValue(data);
				fileOut = new FileOutputStream(path);
				workbook1.write(fileOut);
				fileOut.close();
			}
	   }
  
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
			return true;
}

  
  /** This method is responsible to set the value to the xls file.
   * @author Chethan Kumar
   * @param sheetName
   * @param path
   * @param colName
   * @param data
   * @param RowNum
   * @param rowNumber
   * @return
   */
  			

   public static boolean gtnSetXLCellDatavalue(String sheetName,String path,String colName, double data,int RowNum,int rowNumber){
  			
    try{		  
  		      		
    	  FileInputStream fis1;
  		  fis1=fis;
  		    
  		    fis=new FileInputStream(path); 
  		    fis1=new FileInputStream(path);
  		    
  			workbook=new HSSFWorkbook(fis);					
  			workbook1=new HSSFWorkbook(fis1);
  								
  			int index=workbook.getSheetIndex(sheetName);
  		
  			int colNum=-1;
  			if(index==-1)
  				return false;
  								
  			sheet = workbook.getSheetAt(index);
  			sheet1 = workbook1.getSheetAt(index);
  			row=sheet.getRow(RowNum-1);  // static row. 4	
  			//row=sheet.getRow(RowNum);  // static row. 4
  				
  			for(int i=0;i<row.getLastCellNum();i++){	
  			 			  											
	  			if(row.getCell(i).getStringCellValue().trim().equals(colName))
	  				colNum=i;					
	  			}						
  			
  			System.out.println("Column Number-->"+colNum+"colName-->"+colName);
  									
  			if(colNum==-1)
  			  return false;
  			
  			sheet.autoSizeColumn(colNum); 
  			row = sheet.getRow(rowNumber-1);
  			HSSFRow row1 = sheet1.getRow(rowNumber-1);			
  			
  			if (row1 == null)
  				row1 = sheet.createRow(rowNumber-1);			
  				cell=row.getCell(colNum);	
  				
  			//	cell1=null;
  			
  			if (cell1 == null){
  				HSSFCell cell1 =  row1.createCell(colNum);
  				cell1.setCellValue(data);
  				fileOut = new FileOutputStream(path);
  				workbook1.write(fileOut);
  				fileOut.close();
  			}
  	   }
    
  		catch(Exception e){
  			e.printStackTrace();
  			return false;
  		}
  			return true;
  }
  
   
   /** This method is responsible to set the value to the xls file.
    * @author Chethan Kumar
    * @param sheetName
    * @param path
    * @param colName
    * @param data
    * @param RowNum
    * @param rowNumber
    * @return
    */
   			

   public static boolean gtnSetXLCellDatavalue(String sheetName,String path,int colNum, double data,int RowNum,int rowNumber){
   			
     try{		  
   		    FileInputStream fis1;
   		    fis1=fis;
   		    
   		    fis=new FileInputStream(path); 
   		    fis1=new FileInputStream(path);
   		    
   			workbook=new HSSFWorkbook(fis);					
   			workbook1=new HSSFWorkbook(fis1);
   								
   			int index=workbook.getSheetIndex(sheetName);
   		   			   								
   			sheet = workbook.getSheetAt(index);
   			sheet1 = workbook1.getSheetAt(index);
   			row=sheet.getRow(rowNumber);  // static row. 4	   			  				   			
   			
   			sheet.autoSizeColumn(colNum); 
   			row = sheet.getRow(rowNumber);
   			HSSFRow row1 = sheet1.getRow(rowNumber);			
   			
   			if (row == null)
   				row = sheet.createRow(rowNumber);			
   				cell=row.getCell(colNum);	
   			
	   			if (cell1 == null){
	   				HSSFCell cell1=row1.createCell(colNum);
	   				cell1.setCellValue(data);
	   				fileOut = new FileOutputStream(path);
	   				workbook1.write(fileOut);
	   				fileOut.close();
	   			}
   	   		}
     
   		catch(Exception e){
   			e.printStackTrace();
   			return false;
   		}
   		  return true;
   }  
  
   
  
  
  
  
  /**
	 *@author ckumar2
	 * @param param
	 * @return
	 * @throws Throwable
	 *//*
	
public static boolean gtnSetCellDatavalue(String sheetName,String path,String colName, int data,int RowNum,int rowNumber){
	
	try{		  
	    		    
	    fis=new FileInputStream(path); 	    
		HSSFWorkbook wb= new HSSFWorkbook(fis);
	
		int index=wb.getSheetIndex(sheetName);
		int colNum=-1;
		
		if(index==-1)
			return false;
							
		HSSFSheet sheet1 = wb.getSheetAt(index);		
		HSSFRow	row1=sheet1.getRow(RowNum);  // static row. 4
		
		for(int i=0;i<row1.getLastCellNum();i++){
			//System.out.println(row.getCell(i).getStringCellValue().trim());
			if(row1.getCell(i).getStringCellValue().trim().equals(colName))
				colNum=i;
		}
									
		if(colNum==-1)
		  return false;
		
		sheet.autoSizeColumn(colNum); 
		row = sheet.getRow(rowNumber-1);
		if (row == null)
			row = sheet.createRow(rowNumber-1);			
			HSSFCell cell1=row.getCell(colNum);	
		
		if (cell1 == null)
			cell1 = row1.createCell(colNum);
			cell1.setCellValue(data);
			fileOut = new FileOutputStream(path);
			workbook.write(fileOut);
			fileOut.close();	
	  }
	catch(Exception e){
		e.printStackTrace();
		return false;
	}
		return true;
	}
	*/

/**
 * This method is responsible to delete the rows in the excel.
 * @param sheetName
 * @param path
 * @param rowNo
 * @param lastRowNum
 * @return
 */

 public static boolean gtnDeleteRowsInWorksheet(String sheetName,String path,int rowNo,int lastIndex){
	
	try{				
				
		FileInputStream  fis=new FileInputStream(path); 	    
		HSSFWorkbook wb = new HSSFWorkbook(fis);	
		int index=wb.getSheetIndex(sheetName);
		HSSFSheet  sheet=wb.getSheetAt(index);		
							
		int lastRowNum=sheet.getLastRowNum();											
		for (int i=0; i <lastIndex; i++) {
			HSSFRow row=sheet.getRow(i);		
			
			if (row != null) {
				sheet.removeRow(sheet.getRow(i));  		        	
			}				       
		}			

		for (int i = 0; i<=lastIndex; i++) {
			
			if(rowNo>=0&&rowNo<lastRowNum){
				sheet.shiftRows(rowNo+1,lastRowNum, -1);
			}
		}
																			
		FileOutputStream fileOut = new FileOutputStream(path);
		wb.write(fileOut);
		fileOut.close();	
		
	  }
	catch(Exception e){
		e.printStackTrace();
		return false;
	}
		return true;
	}

/**
 * This method is responsible to delete the given work sheet.
 * @param sheetName
 * @param path
 * @return
 */

 public static boolean gtnDeleteworkSheet(String sheetName,String path){
	
	try{		
		   		    
		fis=new FileInputStream(path); 	    
		workbook = new HSSFWorkbook(fis);	
		int index=workbook.getSheetIndex(sheetName);
		sheet=workbook.getSheetAt(index);
		if(sheet != null)   {
		    workbook.removeSheetAt(index);
		}
		
		fileOut = new FileOutputStream(path);
		workbook.write(fileOut);
		fileOut.close();	
		
	 }catch(Exception E){		
		 System.out.println("Exception-->"+E.getMessage());
		 System.out.println("Exception is encountered.");
		 return false;
	 }
	  return true;
	}
	
	

public static boolean gtnremoveMergedRegionsInSheet(String sheetName,String path){
		
	try {
		
		fis=new FileInputStream(path);
		workbook = new HSSFWorkbook(fis);	
		int index=workbook.getSheetIndex(sheetName);
		sheet=workbook.getSheetAt(index);
	     
	    int nbrMergedRegions = workbook.getSheetAt(index).getNumMergedRegions();	     
	    System.out.println("found a total of merged regions of: "+ String.valueOf( nbrMergedRegions ));
	    
	    for(int i = 0; i < workbook.getSheetAt(index).getNumMergedRegions(); i++){
	        workbook.getSheetAt(index).removeMergedRegion(i);             
	        System.out.println("trying to remove MR: "+ String.valueOf(i));
	    }     
	    
		fileOut = new FileOutputStream(path);
		workbook.write(fileOut);
		fileOut.close();
	    
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		 return false;
	} 	    
	    return true;
    
}
	


/**
 * @author Shubha. H
 * @param param
 * @return
 * @throws Throwable
 */

public static boolean gtnSetCellDatavalue(String sheetName,String path,int rowNumber,int colNumber, String data){
	
	  try{		  
		    		    
		    fis = new FileInputStream(path); 		    
			workbook = new HSSFWorkbook(fis);
		
			int index=workbook.getSheetIndex(sheetName);
			
			if(index==-1)
				return false;				
			
			sheet = workbook.getSheetAt(index);		
										
			if(colNumber==-1)
			  return false;
			
			sheet.autoSizeColumn(colNumber); 
			row = sheet.getRow(rowNumber-1);
			if (row == null)
				row = sheet.createRow(rowNumber-1);			
				cell=row.getCell(colNumber);	
			
			if (cell == null)
		        cell = row.createCell(colNumber);
				cell.setCellValue(data);
				fileOut = new FileOutputStream(path);
				workbook.write(fileOut);
				fileOut.close();	
		  }
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
			return true;
	}
			

/**This method is responsible to fetch number of repeated column headers
 * @author Shubha. H
 * @param param
 * @return 
 * @return
 * @throws Throwable
 */

public static int gtnGetNumofColHeader(String sheetName,String path,int rowNumber,String colName){
	int cnt = 0;
	  try{		  
		    		    
		  fis = new FileInputStream(path); 		    
			workbook = new HSSFWorkbook(fis);
		
			int index=workbook.getSheetIndex(sheetName);
			
			if(index==-1)
				return cnt;
			
			rowNumber=rowNumber-1;  
			
			sheet = workbook.getSheetAt(index);		
			row=sheet.getRow(rowNumber); 
			for(int i=0;i<row.getLastCellNum();i++){												
				if(row.getCell(i).getStringCellValue().trim().equals(colName))
					cnt = cnt + 1;
			}
	 }
		catch(Exception e){
			e.printStackTrace();
			return cnt;
		}
			return cnt;
	}
	
	
	
/**
 * @author Shubha. H
 * @param param
 * @return 
 * @return
 * @throws Throwable
 */
public static boolean gtnSetCellIntvalue(String sheetName,String path,String colName, int data,int RowNum,int rowNumber){
	
	  try{		  
		    		    
		    fis = new FileInputStream(path); 		    
			workbook = new HSSFWorkbook(fis);
		
			int index=workbook.getSheetIndex(sheetName);
			int colNum=-1;
						
			//int rowNumber=-1;
			
			if(index==-1)
				return false;
			
			RowNum=RowNum-1;  // index starts with 0.
			
			sheet = workbook.getSheetAt(index);		
			row=sheet.getRow(RowNum);  // static row. 4
			for(int i=0;i<row.getLastCellNum();i++){
				
				//System.out.println("cellval"+row.getCell(i).getStringCellValue().trim());
				//System.out.println("colName-->"+colName);
				
				if(row.getCell(i).getStringCellValue().trim().equals(colName))
					colNum=i;
			}
			
										
			if(colNum==-1)
			  return false;
			
			sheet.autoSizeColumn(colNum); 
			row = sheet.getRow(rowNumber-1);
			if (row == null)
				row = sheet.createRow(rowNumber-1);			
				cell=row.getCell(colNum);	
			
			if (cell == null)
		        cell = row.createCell(colNum);
				cell.setCellValue(data);
				fileOut = new FileOutputStream(path);
				workbook.write(fileOut);
				fileOut.close();	
		  }
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
			return true;
	}



/**
 * @author shubhah
 * @param param
 * @return
 * @throws Throwable
 */

public static boolean gtnSetCellDatevalue(String sheetName,String path,String colName, String data,int RowNum,int rowNumber,String dateformat){

try{		  
    		    
    fis = new FileInputStream(path); 		    
	workbook = new HSSFWorkbook(fis);

	int index=workbook.getSheetIndex(sheetName);
	int colNum=-1;
				
	//int rowNumber=-1;
	
	if(index==-1)
		return false;
	
	RowNum=RowNum-1;  // index starts with 0.
	
	sheet = workbook.getSheetAt(index);		
	row=sheet.getRow(RowNum);  // static row. 4
	
	for(int i=0;i<row.getLastCellNum();i++){	
		
		if(row.getCell(i).getStringCellValue().trim().equals(colName)){
			colNum=i;
			break;
		}
	}
	
								
	if(colNum==-1)
	  return false;
	
	sheet.autoSizeColumn(colNum); 
	row = sheet.getRow(rowNumber-1);
	if (row == null)
		row = sheet.createRow(rowNumber-1);			
		cell=row.getCell(colNum);	
	
	if (cell == null)
        cell = row.createCell(colNum);
	
		String convFormat = null;
		switch(dateformat.toLowerCase()){
			case "mmddyyyy" :
				convFormat = "m/d/yy" ;
				break;
			case "ddmmyyyy" :	
				convFormat = "d/m/yy" ;
				break;
			case "yyyymmdd" :
				convFormat = "yy/m/d" ;
				break;
		}
	
		CellStyle cellStyle = workbook.createCellStyle();
		CreationHelper createHelper = workbook.getCreationHelper();
		cellStyle.setDataFormat(
		createHelper.createDataFormat().getFormat(convFormat));		
	
		switch(dateformat.toLowerCase()){
			case "mmddyyyy" :
				SimpleDateFormat datevalue1 = new SimpleDateFormat("MM/dd/yyyy");
				Date date1 = datevalue1.parse(data);
				cell.setCellValue(new Date());
				cell.setCellValue(date1);
				cell.setCellStyle(cellStyle); 
				break;
			
			case "ddmmyyyy" :
				SimpleDateFormat datevalue2 = new SimpleDateFormat("dd/MM/yyyy");
				Date date2 = datevalue2.parse(data);
				cell.setCellValue(new Date());
				cell.setCellValue(date2);
				cell.setCellStyle(cellStyle); 
				break;
				
			case "yyyymmdd" :
				SimpleDateFormat datevalue3 = new SimpleDateFormat("yyyy/MM/dd");
				Date date3 = datevalue3.parse(data);
				cell.setCellValue(new Date());
				cell.setCellValue(date3);
				cell.setCellStyle(cellStyle); 
				break;		
		}
		
		fileOut = new FileOutputStream(path);
		workbook.write(fileOut);
		fileOut.close();	
  }
catch(Exception e){
	e.printStackTrace();
	return false;
}
	return true;
}
	
/**
 * @author shubhah
 * @param param
 * @return
 * @throws Throwable
 */	

public static boolean gtnSetCellDatevalue(String sheetName,String path,int colNum, String data,int RowNum,int rowNumber,String dateformat){

try{		  
    		    
    fis = new FileInputStream(path); 		    
	workbook = new HSSFWorkbook(fis);

	int index=workbook.getSheetIndex(sheetName);					
	
	if(index==-1)
		return false;
	
	RowNum=RowNum-1; 
	
	sheet = workbook.getSheetAt(index);		
	row=sheet.getRow(RowNum);  
								
	if(colNum==-1)
	  return false;
	
	sheet.autoSizeColumn(colNum); 
	row = sheet.getRow(rowNumber-1);
	if (row == null)
		row = sheet.createRow(rowNumber-1);			
		cell=row.getCell(colNum);	
	
	if (cell == null)
        cell = row.createCell(colNum);
	
		String convFormat = null;
		switch(dateformat.toLowerCase()){
			case "mmddyyyy" :
				convFormat = "m/d/yy" ;
				break;
			case "ddmmyyyy" :	
				convFormat = "d/m/yy" ;
				break;
			case "yyyymmdd" :
				convFormat = "yy/m/d" ;
				break;
		}
	
		CellStyle cellStyle = workbook.createCellStyle();
		CreationHelper createHelper = workbook.getCreationHelper();
		cellStyle.setDataFormat(
		createHelper.createDataFormat().getFormat(convFormat));		
	
		switch(dateformat.toLowerCase()){
			case "mmddyyyy" :
				SimpleDateFormat datevalue1 = new SimpleDateFormat("MM/dd/yyyy");
				Date date1 = datevalue1.parse(data);
				cell.setCellValue(new Date());
				cell.setCellValue(date1);
				cell.setCellStyle(cellStyle); 
				break;
			
			case "ddmmyyyy" :
				SimpleDateFormat datevalue2 = new SimpleDateFormat("dd/MM/yyyy");
				Date date2 = datevalue2.parse(data);
				cell.setCellValue(new Date());
				cell.setCellValue(date2);
				cell.setCellStyle(cellStyle); 
				break;
				
			case "yyyymmdd" :
				SimpleDateFormat datevalue3 = new SimpleDateFormat("yyyy/MM/dd");
				Date date3 = datevalue3.parse(data);
				cell.setCellValue(new Date());
				cell.setCellValue(date3);
				cell.setCellStyle(cellStyle); 
				break;		
		}
		
		fileOut = new FileOutputStream(path);
		workbook.write(fileOut);
		fileOut.close();	
  }
catch(Exception e){
	e.printStackTrace();
	return false;
}
	return true;
}

	
/**
 *@author shubhah
 * @param param
 * @return
 * @throws Throwable
 */

public static boolean gtnSetCellDatavalue(String sheetName,String path,String colName, int data,int RowNum,int rowNumber){

try{		  
    		    
    fis=new FileInputStream(path); 	    
	workbook = new HSSFWorkbook(fis);

	int index=workbook.getSheetIndex(sheetName);
	int colNum=-1;
	
	if(index==-1)
		return false;
						
	sheet = workbook.getSheetAt(index);		
	row=sheet.getRow(RowNum);  // static row. 4
	
	for(int i=0;i<row.getLastCellNum();i++){
		//System.out.println(row.getCell(i).getStringCellValue().trim());
		if(row.getCell(i).getStringCellValue().trim().equals(colName))
			colNum=i;
	}
								
	if(colNum==-1)
	  return false;
	
	sheet.autoSizeColumn(colNum); 
	row = sheet.getRow(rowNumber-1);
	if (row == null)
		row = sheet.createRow(rowNumber-1);			
		cell=row.getCell(colNum);	
	
	if (cell == null)
        cell = row.createCell(colNum);
		cell.setCellValue(data);
		fileOut = new FileOutputStream(path);
		workbook.write(fileOut);
		fileOut.close();	
  }
catch(Exception e){
	e.printStackTrace();
	return false;
}
	return true;
}
	
	
	
/** This method is responsible to set the value to the xlsx file.
 * @author Shubhah		
 * @param sheetName
 * @param path
 * @param colName
 * @param data
 * @param RowNum
 * @param rowNumber
 * @return
 */
				

  public static boolean gtnSetXLCellDatavalue1(String sheetName,String path,String colName, String data,int RowNum,int rowNumber){
			
		  try{		  
			    FileInputStream fis1;
			    fis1=fis;
			    fis=new FileInputStream(path); 
			    fis1=new FileInputStream(path);
			    
				workbook = new HSSFWorkbook(fis);					
				workbook1 = new HSSFWorkbook(fis1);				
				int index=workbook.getSheetIndex(sheetName);
			
				int colNum=-1;
				if(index==-1)
					return false;
									
				sheet = workbook.getSheetAt(index);
				sheet1 = workbook1.getSheetAt(index);
				row=sheet.getRow(RowNum);  // static row. 4
				for(int i=0;i<row.getLastCellNum();i++){
					if(row.getCell(i).getStringCellValue().trim().equals(colName))
						colNum=i;
				}
											
				if(colNum==-1)
				  return false;
				
				sheet.autoSizeColumn(colNum); 
				row = sheet.getRow(rowNumber-1);
				HSSFRow row1 = sheet1.getRow(rowNumber-1);

				if (row == null)
					row = sheet.createRow(rowNumber-1);			
					cell=row.getCell(colNum);	
				
				if (cell1 == null){
					HSSFCell cell1 =  row1.createCell(colNum);
					cell1.setCellValue(data);
					fileOut = new FileOutputStream(path);
					workbook1.write(fileOut);
					fileOut.close();
				}
			  }
			catch(Exception e){
				e.printStackTrace();
				return false;
			}
				return true;
		}

  
  
  
  public static boolean gtnSetXLCellDatavalue1(String sheetName,String path,String colName, int data,int RowNum,int rowNumber){
		
	  try{		  
			    FileInputStream fis1;
			    fis1=fis;
			    
			    fis=new FileInputStream(path); 
			    fis1=new FileInputStream(path);
			    
				workbook=new HSSFWorkbook(fis);					
				workbook1=new HSSFWorkbook(fis1);
									
				int index=workbook.getSheetIndex(sheetName);
			
				int colNum=-1;
				if(index==-1)
					return false;
									
				sheet = workbook.getSheetAt(index);
				sheet1 = workbook1.getSheetAt(index);
				row=sheet.getRow(RowNum-1);  // static row. 4	
				//row=sheet.getRow(RowNum);  // static row. 4
					
				for(int i=0;i<row.getLastCellNum();i++){								
												
				if(row.getCell(i).getStringCellValue().trim().equals(colName))
					colNum=i;	
				}										
										
				if(colNum==-1)
				  return false;
				
				sheet.autoSizeColumn(colNum); 
				row = sheet.getRow(rowNumber-1);
				HSSFRow row1 = sheet1.getRow(rowNumber-1);			
				
				if (row == null)
					row = sheet.createRow(rowNumber-1);			
					cell=row.getCell(colNum);	
				
				if (cell1 == null){
					HSSFCell cell1 =  row1.createCell(colNum);
					cell1.setCellValue(data);
					fileOut = new FileOutputStream(path);
					workbook1.write(fileOut);
					fileOut.close();
				}
		   }
	  
			catch(Exception e){
				e.printStackTrace();
				return false;
			}
				return true;
	  }


 
  
  
  
  
  
  
  
  
  



}// class ends here.
