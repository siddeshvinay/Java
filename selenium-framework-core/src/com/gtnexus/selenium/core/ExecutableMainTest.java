/**
 * @author chethan kumar
 * This class is used as the main executable test to fetch details from excel data sheet and
 * update test execution results to automation DB
 */

package com.gtnexus.selenium.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.gtnexus.selenium.core.driver.StartDriver;
import com.gtnexus.selenium.core.model.MapObject;
import com.gtnexus.selenium.core.model.ParamDataObject;
import com.gtnexus.selenium.core.model.PropertiesHolder;
import com.gtnexus.selenium.core.utils.Common_Utils;
import com.gtnexus.selenium.core.utils.Framework_Utils;
import com.gtnexus.selenium.core.utils.GlobalVariables;

public class ExecutableMainTest extends StartDriver{
	private LinkedHashMap<String, String> testCaseMap = new LinkedHashMap<>();
	private Map<String, List<ParamDataObject>> keywordMap = new HashMap<>();
	public List<String> errorList = new ArrayList<>();
	
	private LinkedHashMap<String, String> tctypMap = new LinkedHashMap<>();
	private LinkedHashMap<String, String> tcidMap = new LinkedHashMap<>();
	
	

	public List<String> getErrorList() {
		return errorList;
	}

	public void setErrorList(List<String> errorList) {
		this.errorList = errorList;
	}

	public ExecutableMainTest(){
		super();
	}
	
//############################################################################################################
	/**
	 * @author Chethan Kumar
	 * Main method to extract data from map object and run using controller
	 * @param mapObj
	 * @throws Exception
	 */
//############################################################################################################	
	@SuppressWarnings("unused")
	private void gtnFetchKeywordAndData(MapObject mapObj) throws Exception {
		List<ParamDataObject> objList = new ArrayList<>();
		List<String> paramList = new ArrayList<>();
		int r, cTT=5, cGTNID=6, rowNum;
		String filepath = GlobalVariables.KEYWORKFILEPATH;

		DateFormat df= new SimpleDateFormat("YYYY-MM-dd HH:mm:ss.SSS");
		//System.out.println();
		System.out.println("Driver in fetchKeyword method=========>" + webDriver);

		testCaseMap = mapObj.getTcMap();
		keywordMap = mapObj.getKeyMap();
		
		
		Set<String> keySet = testCaseMap.keySet();
		Iterator<String> iterator = keySet.iterator();			
		while (iterator.hasNext()) {
			Date sd= new Date();
			String tcExecutionStatus = null;
			String tcid = (String) iterator.next();
			
			//####################Code to fetch test case type and GTN ID##########################
			InputStream in = new FileInputStream(filepath.replace(" ", ""));						
			XSSFWorkbook wb = new XSSFWorkbook(in);
			XSSFSheet sheet = wb.getSheetAt(0);

			for(Row row : sheet){
				for(Cell cell : row){
					if(cell.getCellType() == Cell.CELL_TYPE_STRING){
					//	if(cell.getRichStringCellValue().getString().trim().equals("TestCase")){
							//System.out.println("cell.getRichStringCellValue()-->"+cell.getRichStringCellValue());
							if(cell.getRichStringCellValue().getString().trim().equals(tcid)){
								rowNum = row.getRowNum();
								r = rowNum;
								
								XSSFRow rows = sheet.getRow(r);
								//System.out.println("rows.getCell(cTT);-->"+rows.getCell(cTT-5));	
								String cellText  = String.valueOf(rows.getCell(cTT-5));								
								//System.out.println("cellText---->"+cellText);
								
								if (cellText.equalsIgnoreCase("TestCase")) {
									GlobalVariables.TestType = rows.getCell(cTT);
									GlobalVariables.GTNID = rows.getCell(cGTNID);
									String tcType=String.valueOf(rows.getCell(cTT));
									
									if (tcType.equalsIgnoreCase("REGRESSION")) {
										GlobalVariables.TCTYPE_Val=String.valueOf(rows.getCell(cTT-1));
									}else if(tcType.equalsIgnoreCase("DATA_SETUP")){
										GlobalVariables.TCTYPE_Val=String.valueOf(rows.getCell(cTT-1));
									}
								}	
																																														
								//System.out.println("GlobalVariables.TestType---->"+GlobalVariables.TestType);
								//System.out.println("GlobalVariables.GTNID---->"+GlobalVariables.GTNID);
																
								
								/*XSSFRow rows = sheet.getRow(r);
								GlobalVariables.TestType = rows.getCell(cTT);
								GlobalVariables.GTNID = rows.getCell(cGTNID);*/
	
							}
						//}
					}
				}
			}
			
		//#########################Fetch test case type and GTN ID code end###############################
									
			String strconcat = "-";
			strconcat = strconcat + StringUtils.repeat("-", 100);
			Framework_Utils.gtnWriteLog("",strconcat);
			Framework_Utils.gtnWriteLog("",Framework_Utils.gtnGetTimeStamp() + " :: BEGIN TEST CASE : " + tcid + "-" + testCaseMap.get(tcid));
			Framework_Utils.gtnWriteLog("",strconcat);
							
								
			objList = keywordMap.get(tcid);
			for (ParamDataObject paraObj : objList) {
				Framework_Utils.gtnUpdateLog("Executing the Keyword: "+ paraObj.getKeywordName());
				System.out.println(" -- > Executing the Keyword : " + paraObj.getKeywordName());
				paramList = paraObj.getParamList();			
				try{
					if (new Controller().mainController(paraObj.getKeywordName(),paraObj.getClassname(), paramList, tcid, this)){
						tcExecutionStatus="PASS";
						continue;
					}
					else{
						tcExecutionStatus="FAIL";
						Framework_Utils.gtnWriteLog("FAIL", "Test case exited with fail status");
						break;
					}
				}catch(Throwable t){
					Framework_Utils.gtnWriteLog("INFO", "Test case stopped due to following error : " + t.getMessage());
				}
			}
			Date ed= new Date();
			String ExeTime=Framework_Utils.gtnCalDateTimeDiff(sd, ed);
			//System.out.println(tcid +" - " + testCaseMap.get(tcid)+" - " + GlobalVariables.TestType+ " - "+ tcExecutionStatus+ " - "+GlobalVariables.GTNID);
			
			
			//if(PropertiesHolder.properties.get("config").getProperty("UPDATE_DB").equalsIgnoreCase("true")){
			//System.out.println("UpdateDB-->"+GlobalVariables.UpdateDB);
			if(GlobalVariables.UpdateDB){	
			 Framework_Utils.gtnUpdateTCExeStatusToDB(tcid, testCaseMap.get(tcid), df.format(sd), df.format(ed), ExeTime, GlobalVariables.TestType, tcExecutionStatus, GlobalVariables.GTNID,GlobalVariables.browserName);
			}else{
				Framework_Utils.gtnWriteLog("INFO", "Update DB is set as false.");
			}
			Framework_Utils.gtnUpdateLog("----------------------------------------------------------------------------------------------------------------");
			Framework_Utils.gtnUpdateLog("Total time taken to execute the test case: "+ tcid+" in (ss.SSS) format is: "+ExeTime);
			Framework_Utils.gtnUpdateLog("================================================================================================================");
		}
				
		//System.out.println();
	}
	
	
	
//############################################################################################################
	/**
	 * @author Chethan Kumar
	 * @category TestNG Main Test
	 * @throws Exception
	 */
//############################################################################################################	
	@Test
	public void mainExecutableTest() throws Exception {	
			    
		String filepath = GlobalVariables.KEYWORKFILEPATH;
		System.out.println("filepath"+filepath);
		MapObject mapObj = new MapObject();
		int sheetnum = 0;
		try {
			mapObj = new Common_Utils(webDriver, baseURL).gtnReadFromExcel(filepath,sheetnum);
			gtnFetchKeywordAndData(mapObj);
			if (errorList.size() > 0) {
				for (String str : errorList) {
					//Reporter.log(str);
					Framework_Utils.gtnFailLog("The script has failed to execute due to exception: \n " + str);
				}
				Assert.assertTrue(Boolean.FALSE,"The Script has failed due to above reasons");
			}
			
			if (GlobalVariables.CopyReports) {
				Framework_Utils.gtnCopyResultsToCommonFolder();	
			}else{
				Framework_Utils.gtnInfoLog("Reports are not copied to remote location as flag set to false.");
			}
						
			
		} catch (Exception e) {
			Framework_Utils.gtnFailLog("The script has failed to execute further with exception: \n " + e);
			//throw e;
		}
	}
	
//############################################################################################################
	

}
