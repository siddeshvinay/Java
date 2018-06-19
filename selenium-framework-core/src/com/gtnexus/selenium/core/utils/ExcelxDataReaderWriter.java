package com.gtnexus.selenium.core.utils;


/*XSSF (XML SpreadSheet Format) – reads and writes Office Open XML (XLSX) format files.
Similar feature set to HSSF, but for Office Open XML files.*/
	
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Calendar;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/*import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;*/

public class ExcelxDataReaderWriter {
 						
		public  static FileInputStream fis=null;
		public  static FileOutputStream fileOut=null;	
		
		private static Workbook workbook = null;
		private static Sheet sheet = null;
		private static Row row   =null;
		private static Cell cell = null;		//hss for .xls
		
		private static XSSFCell cell1=null;		
		private static XSSFWorkbook workbook1 = null;
		private static XSSFSheet sheet1 = null;
		
		
		/*private static XSSFRow row=null;
		private static XSSFWorkbook workbook = null;
		private static XSSFSheet sheet = null;
		private static XSSFCell cell=null;
	*/		
					
							
	/*	private static XSSFWorkbook workbook1 = null;
		private static XSSFSheet sheet1 = null;
		private static XSSFCell cell1=null;		
*/		
	
		/**
		 * @author Sivashankar.G
		 * @param param
		 * @return
		 * @throws file not found exception
		 */
						
	public ExcelxDataReaderWriter() {
			
			String fname=GlobalVariables.KEYWORKFILEPATH;
			
			try {
			      InputStream inp = new FileInputStream(fname);			   			      
			      workbook = new XSSFWorkbook(inp);
			      log("xlsx="+workbook.getSheetName(0));
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
 * @return row count in a sheet
 */

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
		 * @author ckumar2
		 * @param param
		 * @return returns the data from a cell
		 * @throws Throwable
		 */
		
	public  static String gtnGetCellData(String path,String sheetName,int colNum,int rowNum){
		
		try{
				
			fis = new FileInputStream(path); 
			workbook = new XSSFWorkbook(fis);		
																	
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
	 * @author ckumar2
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
			workbook = new XSSFWorkbook(fis);
		
			int index = workbook.getSheetIndex(sheetName);
			int colNum=-1;
			int rowNumber=-1;
			if(index==-1)
				return false;
			System.out.println("index-->"+index);
			
			sheet=workbook.getSheetAt(index);		
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
			
			System.out.println("rowNumber"+rowNumber);
			
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
		workbook = new XSSFWorkbook(fis);
	
		int index = workbook.getSheetIndex(sheetName);
		int colNum=-1;
		int rowNumber=-1;
		if(index==-1)
			return false;
		System.out.println("index-->"+index);
		
		sheet=workbook.getSheetAt(index);		
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
		
		System.out.println("rowNumber"+rowNumber);
		
		if(colNum==-1)
		  return false;
		
		sheet.autoSizeColumn(colNum); 
		row = sheet.getRow(rowNumber-1);
		if (row == null)
			row = sheet.createRow(rowNumber-1);			
			cell=row.getCell(colNum);	
		
		if (cell == null)
			
			//XSSFCell cell1 = row.createCell(38); 
			
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
		workbook = new XSSFWorkbook(fis);
		
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
		    cell.removeCellComment();		    	 
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
				workbook = new XSSFWorkbook(fis);
						
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
			workbook = new XSSFWorkbook(fis);
					
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
			workbook = new XSSFWorkbook(fis);
					
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
 * @return
 * @throws Throwable
 */
		
public static boolean gtnSetCellDatavalue(String sheetName,String path,String colName, String data,int RowNum,int rowNumber){
	
	  try{		  
		    		    
		    fis=new FileInputStream(path); 
		    
			workbook = new XSSFWorkbook(fis);
		
			int index=workbook.getSheetIndex(sheetName);
			int colNum=-1;
			//int rowNumber=-1;
			
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

/*public static boolean gtnSetCellDatavalue(String sheetName,String path,String colName, String data,int RowNum,int rowNumber){
		
	  try{		  
		    		    
		    fis=new FileInputStream(path); 		    
			workbook = new XSSFWorkbook(fis);
		
			int index=workbook.getSheetIndex(sheetName);
			int colNum=-1;
			//int rowNumber=-1;
			
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
			System.out.println("Cell type -->"+cell.getCellType());
			
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
		*/
		
	/**
	 *@author ckumar2
	 * @param param
	 * @return
	 * @throws Throwable
	 */
	
 public static boolean gtnSetCellDatavalue(String sheetName,String path,String colName, int data,int RowNum,int rowNumber){
	
	try{		  
	    		    
	    fis=new FileInputStream(path); 
	    
		workbook = new XSSFWorkbook(fis);
	
		int index=workbook.getSheetIndex(sheetName);
		int colNum=-1;
		//int rowNumber=-1;
		
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
	

/**
 * This method is responsible to return the row count of the given work sheet.
 * @author ckumar2
 * @param param
 * @return row count.
 * @throws Throwable
 */

public static int gtnGetExclRowCount(String sheetName, String pathVal){
	
	int rowval=0;

try{			
	   	System.out.println("pathVal "+pathVal);
	   	System.out.println("sheetName "+sheetName);
	   	
		fis = new FileInputStream(pathVal); 
		workbook = new XSSFWorkbook(fis);
	
		int index=workbook.getSheetIndex(sheetName);	
		System.out.println("index value is "+index);
				
		if(index==-1){
			return -1;
	     }
		else {
			sheet = workbook.getSheetAt(index);										
			rowval=sheet.getLastRowNum();
		}		
		
} catch(Exception e){
	return -1;
}
	return rowval; 
	
}	


/**
 * This method is responsible to return the row count of the given work sheet.
 * @author ckumar2
 * @param param
 * @return row count.
 * @throws Throwable
 */

  public static int gtnGetExclColumnCount(String sheetName, String pathVal, int rowVal){
		
		int noOfColumns=-1;
	
		try{			
			   	//System.out.println("pathVal "+pathVal);
			   	//System.out.println("sheetName "+sheetName);
			   	
				fis = new FileInputStream(pathVal); 
				workbook = new XSSFWorkbook(fis);			
				int index=workbook.getSheetIndex(sheetName);		
				
				if(index==-1){
					return -1;
			     }
				else {
					sheet = workbook.getSheetAt(index);			
					noOfColumns = sheet.getRow(rowVal).getLastCellNum();				// get the empty RowNum;					
				}		
				
		} catch(Exception e){
			return -1;
		}
		return noOfColumns; 		
	}	
  
  
	
	/**
	 * 	
	 * @param message
	 */

	private static void log(String message)
	{
	    System.out.println(message);
	}

/**
 * @author ckumar2	
 * @param fname2
 * @return
 */
	
	private static String GetFileExtension(String fname2)
	{
	    String fileName = fname2;
	    String fname="";
	    String ext="";
	    int mid= fileName.lastIndexOf(".");
	    fname=fileName.substring(0,mid);
	    ext=fileName.substring(mid+1,fileName.length());
	    return ext;
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
				workbook = new XSSFWorkbook(fis);						
				
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
		
public static int gtnGetColNumByColName(String sheetName,String path,String colName, int rowNumber){

		int colNum=0;
		int rowNum=0;		 
	
		try{							
				  		    
			fis = new FileInputStream(path); 				
			workbook = new XSSFWorkbook(fis);						
			
		//	System.out.println("sheetName "+sheetName);
			int index=workbook.getSheetIndex(sheetName);
																			
			sheet=workbook.getSheetAt(index);
			row=sheet.getRow(rowNumber);
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
		
		public static int gtnGetRowNumByName(String sheetName,String path,String colName){
					
				 int rowval=0;	 
			
				try{							
					
					fis = new FileInputStream(path); 
					workbook = new XSSFWorkbook(fis);
				
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
							String rowva=row.getCell(0).getStringCellValue();
							System.out.println(rowva);
							
							if (rowva.equalsIgnoreCase(colName)) {
								rowval=i;
								break;
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
		    
			workbook = new XSSFWorkbook(fis);
			
			workbook1 = new XSSFWorkbook(fis1);
		
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
			XSSFRow row1 = sheet1.getRow(rowNumber-1);

			if (row == null)
				row = sheet.createRow(rowNumber-1);			
				cell=row.getCell(colNum);	
			
			if (cell1 == null){
				XSSFCell cell1 =  row1.createCell(colNum);
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
		    
			workbook=new XSSFWorkbook(fis);					
			workbook1=new XSSFWorkbook(fis1);
								
			int index=workbook.getSheetIndex(sheetName);
		
			int colNum=-1;
			if(index==-1)
				return false;
								
			sheet = workbook.getSheetAt(index);
			sheet1 = workbook1.getSheetAt(index);
			//row=sheet.getRow(RowNum-1);  // static row. 4	
			row=sheet.getRow(RowNum);  // static row. 4		
				
			for(int i=0;i<row.getLastCellNum();i++){								
											
			if(row.getCell(i).getStringCellValue().trim().equals(colName))
				colNum=i;					
			}										
									
			if(colNum==-1)
			  return false;
			
			sheet.autoSizeColumn(colNum); 
			row = sheet.getRow(rowNumber-1);
			XSSFRow row1 = sheet1.getRow(rowNumber-1);			
			
			if (row == null)
				row = sheet.createRow(rowNumber-1);			
				cell=row.getCell(colNum);	
			
			if (cell1 == null){
				XSSFCell cell1 =  row1.createCell(colNum);
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
  		    
  			workbook=new XSSFWorkbook(fis);					
  			workbook1=new XSSFWorkbook(fis1);
  								
  			int index=workbook.getSheetIndex(sheetName);
  		
  			int colNum=-1;
  			if(index==-1)
  				return false;
  								
  			sheet = workbook.getSheetAt(index);
  			sheet1 = workbook1.getSheetAt(index);
  			//row=sheet.getRow(RowNum-1);  // static row. 4	
  			row=sheet.getRow(RowNum);  // static row. 4		
  				
  			for(int i=0;i<row.getLastCellNum();i++){								
  											
  			if(row.getCell(i).getStringCellValue().trim().equals(colName))
  				colNum=i;					
  			}										
  									
  			if(colNum==-1)
  			  return false;
  			
  			sheet.autoSizeColumn(colNum); 
  			row = sheet.getRow(rowNumber-1);
  			XSSFRow row1 = sheet1.getRow(rowNumber-1);			
  			
  			if (row == null)
  				row = sheet.createRow(rowNumber-1);			
  				cell=row.getCell(colNum);	
  			
  			if (cell1 == null){
  				XSSFCell cell1 =  row1.createCell(colNum);
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
  		XSSFWorkbook wb = new XSSFWorkbook(fis);	
  		int index=wb.getSheetIndex(sheetName);
  		XSSFSheet sheet=wb.getSheetAt(index);		
  							
  		int lastRowNum=sheet.getLastRowNum();											
  		for (int i=0; i <lastIndex; i++) {
  			XSSFRow row=sheet.getRow(i);		
  			
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

  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
 

	
	
 }  // Class End.
