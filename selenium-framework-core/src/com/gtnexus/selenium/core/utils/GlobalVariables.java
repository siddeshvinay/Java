/**
 * @author chethan kumar
 * This class is used for listing the global variables and constants used throughout 
 * the framework irrespective of product/module
 */


package com.gtnexus.selenium.core.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.poi.xssf.usermodel.XSSFCell;

public class GlobalVariables {
	/*------------------------GLOBAL VARIABLES-----------------------------*/
	public static String appServerName;
	public static String appServerStatus;
	public static String buildNumber;
	public static String release;
	public static boolean UpdateDB;
	public static boolean SendEmail;
	public static boolean CopyReports;	
	public static boolean ScreenCapture;
	public static String environment;
	public static String exeMachineName;
	public static String browserName;
	public static String browserVersion;
	public static String loggedInUser;
	public static String invoiceNum="";
	public static String PRODUCT="";
	public static String KEYWORKFILEPATH;
	public static XSSFCell TestType;
	public static XSSFCell GTNID;		
	public static String FileVal;
	public static String RESULTS_File_PATH;
	public static String TCTYPE_Val;
	
	/*------------------------GLOBAL CONSTANTS--------------------------------*/
	public static DateFormat dateFormat =new SimpleDateFormat("MM/dd/yyyy");
	public static String LogFileName="GTNexusXXXXX_Selenium_Log_Default.txt";	
	public final static String RESULTS_PATH = "\\\\GTBLRFILE1\\BLR_Share_Data\\QA\\Internal\\Automation\\Selenium_Automation_Results\\";
	public final static String TESTNG_RESULT_PATH = "\\test-output\\SuiteResults\\";
	public final static String TESTNG_RESULT = "TestNG_html_Report.html";
	public final static String LOGS = "\\test-output\\Logs\\";
	public final static String FIREFOX_BROWSER = "*firefox";
	public final static String IE_BROWSER = "*iexplore";
	public final static String CHROME_BROWSER = "*chrome";
	public final static String MISSING_PAGE_MSG = "Missing Page";
	public final static String OOPS_MSG = "Oops Page";
	public final static String JBOSSErrorPage = "JBOSS Error";
	public final static String SERVERErrorPage = "Server Error";
	public final static String ScreenShotPath= "\\src\\org\\gtn\\tms\\results\\screenshots";
	public final static String ScreenShotPathMSite="\\src\\org\\gtn\\tms\\results\\screenshots";

	
	
}
