/**
 * @author Chethan Kumar
 * This class is used for common utilities like capturing screenshots, finding the element, etc 
 * to be used during the script generation and execution 
 */
package com.gtnexus.selenium.core.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;

import com.gtnexus.selenium.core.model.MapObject;
import com.gtnexus.selenium.core.model.ParamDataObject;
import com.gtnexus.selenium.core.model.PropertiesHolder;
import com.thoughtworks.selenium.Selenium;

public class Common_Utils {
	protected WebDriver webDriver;
	protected Selenium selenium;
	protected long timeout = 90;
	protected WebElement webElement;	
	public Properties intprops;
	protected static String baseURL = null;
	protected boolean isFirefox = false;
	private boolean isParallel = Boolean.FALSE;
	protected String platform = null;
	public static String testCaseName= null;
	protected LinkedHashMap<String, String> testCaseMap = new LinkedHashMap<>();
	protected Map<String, List<ParamDataObject>> keywordMap = new HashMap<>();
	protected static List<String> usrRoles = new ArrayList<>();
	protected static Map<String,Integer> orgPrefs = new HashMap<String, Integer>();
	protected static List<Integer> adjSttings = new ArrayList<Integer>();
	
	public Common_Utils(WebDriver driver, String url) throws Exception	{
		//initiaLize();
		setWebDriver(driver);
		setBaseURL(url);
	}
    
	
	/**
	 * @author chethan kumar
	 * Method to load the all properties
	 * @param mapObj
	 * @throws Exception
	 */	
	public void initiaLize() throws Exception {
		//common = new Properties();
		//common.load(new FileInputStream(System.getProperty("user.dir")+ "\\resources\\properties\\common.properties"));
		//intprops = new Properties();
		//intprops.load(new FileInputStream(System.getProperty("user.dir")+"\\resources\\properties\\integration.properties"));
	}
	
	public WebDriver getWebDriver() {
		return webDriver;
	}

	public void setWebDriver(WebDriver webDriver) {
		this.webDriver = webDriver;
	}
	
	public Selenium getSelenium() {
		return selenium;
	}	

	public void setSelenium(Selenium selenium) {
		this.selenium = selenium;
	}
	
	public String getBaseURL() {
		return baseURL;
	}

	public void setBaseURL(String baseURL) {
		this.baseURL = baseURL;
	}
	
		
	public static Properties LoadProperty(Properties propobj,String Path) throws Exception{
		Properties propobj1 = new Properties();
		propobj1.load(new FileInputStream(Path));
		return propobj1;
		
	}
	
	
 /*****************************************************************************************
								Generic Utilities
 *****************************************************************************************/
	
	/**
	 * @author chethan kumar
	 * @param 
	 * @return
	 * @throws Throwable
	 */
	public void gtnCaptureScreenshot(String folderName, String screenName){
		String screenShotPath;
		if (platform.equalsIgnoreCase("web"))
			screenShotPath = GlobalVariables.ScreenShotPath;
		else
			screenShotPath = GlobalVariables.ScreenShotPathMSite;
    		screenShotPath = screenShotPath + folderName + "\\"+ screenName + ".jpg";
	   		File srcFile =  null;
	   		if (isParallel)  {
	   	 		WebDriver agumentedDriver = new Augmenter().augment(webDriver);
	   		} else 
	   			srcFile = ((TakesScreenshot)webDriver).getScreenshotAs(OutputType.FILE);
		    try {
		    	FileUtils.copyFile(srcFile, new File(screenShotPath));
			} catch (IOException e) {
				System.out.println("Taking screenshot failed due to " + e);
				e.printStackTrace();
			}
	}

	
	
	/** 
	 @author Amogh Wadeyar
	 * @return
	 * @throws Throwable
	 */
	public String gtnGetPopUpWindow(WebElement element){
		Set<String> beforePopUP = webDriver.getWindowHandles();
		element.click();
		Set<String> afterPopUP = webDriver.getWindowHandles();
		beforePopUP.removeAll(afterPopUP);
		if (afterPopUP.size() == 0) {
			return (String) afterPopUP.toArray()[0];
		}
		return null;
	}
	
	/** 
	 @author Amogh Wadeyar
	 * @return
	 * @throws Throwable
	 */
	public boolean gtnCheckTheElementIsLink(WebElement element,String elementName){
		if (gtnGetElement(element) && element.getAttribute("href")==null){
			Reporter.log(elementName + " is NOT displayed as link.");
			return false;
		}
		else {
			Reporter.log(element + " is displayed as link.");
			return true;
		}
	}
	
	/**
	 * @author chethan kumar
	 * @return
	 * @throws Throwable
	 */
	public XSSFSheet gtnGetValuesFromExcel(String filePath, int sheetnum)throws Exception {
		InputStream inputStream = new FileInputStream(filePath);
		XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
		XSSFSheet sheet = workbook.getSheetAt(sheetnum);
		return sheet;
	}

	/**
	 * @author chethan kumar
	 * @return
	 * @throws Throwable
	 */
	public void gtnMaximiseWindow(){
		webDriver.manage().window().maximize();
	}
	
	/**
	 * @author chethan kumar
	 * @return
	 * @throws Throwable
	 */
	public boolean gtnGetElement(WebElement element){
		try{
			if(element.isDisplayed()&&element.isEnabled()){
				return Boolean.TRUE;
			}else{
				Framework_Utils.gtnFailLog("Web element " + element + "is not displayed/enabled");
				return Boolean.FALSE; 	 
			}		
		}catch(Throwable t){
			Framework_Utils.gtnFailLog("Web element " + element + "  not FOUND");
			return Boolean.FALSE;
		}
	} 

	/**
	 * @author chethan kumar
	 * @return
	 * @throws Throwable
	 */
	public WebElement gtnGetObjectByXpath(String xpathkey){
		try{
			return webDriver.findElement(By.xpath(xpathkey));
		}catch(Throwable t){
		  // Framework_Utils.gtnFailLog("Web element  " + xpathkey + "  not FOUND");
			t.printStackTrace();
			return null;
		}
	} 
	
	
	/**
	 * @return
	 * @author chethan kumar
	 * @param
	 **/
	public void gtnImplicitWait(){
		int myint  = new Integer(PropertiesHolder.properties.get("config").getProperty("MAX_WAIT_TIME"));
		webDriver.manage().timeouts().implicitlyWait(myint, TimeUnit.SECONDS);
	}
	

	/**
	 * This method is responsible for the enter the given text in the given element.
	 * @param element
	 * @param elementName
	 * @param strText
	 * @return
	 * @throws Exception
	 */	
	
	public boolean gtnInsertText (WebElement element,String elementName,String strText) throws Exception{
			 	
		if(gtnGetElement(element)){		      
			//if (strText.isEmpty())
			//	return Boolean.TRUE;
			//else {
			  element.clear();
			  element.sendKeys(strText);
			  Framework_Utils.gtnPassLog(strText + " -Inserted text successfully into Edit element << "+elementName+" >>");
			  return Boolean.TRUE;
			//}
		}else{
			Framework_Utils.gtnFailLog(strText + " -Failed to Insert text into Edit element << "+ elementName +" >>");
			return Boolean.FALSE;		  
	     }
	}
	
	
	/**
	 * This method is responsible for click event on the given element.
	 * @param element
	 * @param elementName
	 * @return
	 * @throws InterruptedException
	 */
		
	public boolean gtnClickObj (WebElement element,String elementName) throws InterruptedException{
		if(gtnGetElement(element)){	
			element.click();
			Framework_Utils.gtnPassLog(elementName + " Button element was clicked successfully");
			return Boolean.TRUE;
		}
		else{
			Framework_Utils.gtnFailLog(elementName + " Failed to click on the Button element ");
			return Boolean.FALSE;
		}	
	}


	/**
	 *  This method is responsible to check the given  element is present or not. 
	 * @param element	 * 
	 * @param elementName
	 * @return
	 * @throws InterruptedException
	 */
	
	public boolean gtnCheckButtonVisibility(WebElement element,String elementName)throws InterruptedException{
		if (gtnIsElementPresent(element,elementName)){
			Framework_Utils.gtnPassLog(elementName + " Element was visible");
			return true;
		}else{ 
			Framework_Utils.gtnFailLog(elementName + " Element is NOT visible");
			return false;
		}	
	   }


	/**
	 *  This method is responsible for selecting a given value from the drop down.
	 * @param element
	 * @param elementName
	 * @param listItem
	 * @return
	 * @throws InterruptedException
	 */
	
	
	public boolean gtnSelectDropdown(WebElement element,String elementName,String listItem) throws InterruptedException{
		if(gtnGetElement(element)){		  
			 Select select = new Select(element);
			 
			 select.selectByVisibleText(listItem);
			 Framework_Utils.gtnPassLog(listItem + " -Item selected successfully in dropdown/list << "+ elementName +" >>");
			 return Boolean.TRUE;
		}
		else
			Framework_Utils.gtnFailLog("Cannot find '" + listItem + "' in the dropdown/list << "+ elementName +" >>");
			return Boolean.FALSE;
	}


	/**
	 * This method is responsible selecting a value from list box.
	 * @param element
	 * @param elementName
	 * @param Value
	 * @return
	 * @throws InterruptedException
	 */
	
	public boolean gtnSelectList(WebElement element,String elementName,String Value) throws InterruptedException{
		
		if(gtnGetElement(element)){	
			List<WebElement> options = element.findElements(By.tagName("option"));
			for (WebElement option : options) {
				if(Value.trim().equalsIgnoreCase(option.getText().trim())){
					option.click();
					Framework_Utils.gtnPassLog(Value + " -Item selected successfully in list << "+elementName+" >>");
					return true;
				}
			}
			Framework_Utils.gtnPassLog(Value + " - Item not found / Failed to select in list <<"+ elementName+">>");
		}
		return false;
	}
	
	
	/**
	 * This method is responsible selecting a value from list box.
	 * @param element
	 * @param elementName
	 * @param Value
	 * @return
	 * @throws InterruptedException
	 */
	
	public boolean gtngwtSelectList(WebElement element,String elementName,String Value) throws InterruptedException{
		
		if(gtnGetElement(element)){	
			List<WebElement> options = element.findElements(By.tagName("option"));
			for (WebElement option : options) {
				if(Value.trim().equalsIgnoreCase(option.getText().trim())){
					option.click();
					Framework_Utils.gtnPassLog(Value + " -Item selected successfully in list << "+elementName+" >>");
					return true;
				}
			}
			Framework_Utils.gtnPassLog(Value + " - Item not found / Failed to select in list <<"+ elementName+">>");
		}
		return false;
	}	
	
	
	
	/**
	 * This method is responsible selecting a value from list box.
	 * @param element
	 * @param elementName
	 * @param Value
	 * @return
	 * @throws InterruptedException
	 */
	
	public boolean gtnSelectListVal(WebElement element,String elementName,String Value) throws InterruptedException{
		
		if(gtnGetElement(element)){	
			List<WebElement> options = element.findElements(By.tagName("option"));
			for (WebElement option : options) {
				if(Value.trim().equalsIgnoreCase(option.getText().trim()) || Value.contains(option.getText().trim())){
					option.click();
					Framework_Utils.gtnPassLog(Value + " -Item selected successfully in list << "+elementName+" >>");
					return true;
				}
			}
			Framework_Utils.gtnPassLog(Value + " - Item not found / Failed to select in list <<"+ elementName+">>");
		}
		return false;
	}
	
	
	
	/**
	 * This method is responsible selecting a value from list box.
	 * @param element
	 * @param elementName
	 * @param Value
	 * @return
	 * @throws InterruptedException
	 */
	
	public String gtnGetDefaultSelectListVal(WebElement element) throws InterruptedException{
		
		String retVal="";
		
		try {
			if(gtnGetElement(element)){	
				List<WebElement> options = element.findElements(By.tagName("option"));
				for (WebElement option : options) {
					retVal=option.getText().trim();					
					break;
				}				
			}
		} catch (Exception e) {
			System.out.println("Exception is encountered.-->"+e.getMessage());
			return retVal;
		}		
		
		return retVal;
	}	
	
	
	
	
	
	
	
	
	/**
	 * This method is responsible selecting a value from list box.
	 * @param element
	 * @param elementName
	 * @param Value
	 * @return
	 * @throws InterruptedException
	 */
	
	public boolean gtngwtSelectListVal(WebElement element,String elementName,String Value) throws InterruptedException{
		
		if(gtnGetElement(element)){	
			List<WebElement> options = element.findElements(By.tagName("div"));
			for (WebElement option : options) {
				if(Value.trim().equalsIgnoreCase(option.getText().trim()) || Value.contains(option.getText().trim())){
					option.click();
					Framework_Utils.gtnPassLog(Value + " -Item selected successfully in list << "+elementName+" >>");
					return true;
				}
			}
			Framework_Utils.gtnPassLog(Value + " - Item not found / Failed to select in list <<"+ elementName+">>");
		}
		return false;
	}
	
	
	 /**
     * @author Chethan Kumar
     * @return
     * @throws Exception
      */
	    public boolean gtngwtCheckItemPresenceInListbox(WebElement ele,String Value) throws InterruptedException {
	    	boolean valflag=false;
	    	try{
		    	List<WebElement> options = ele.findElements(By.tagName("div"));
				for (WebElement option : options) {
					if(Value.trim().equals(option.getText().trim()) || Value.contains(option.getText().trim()) ){
						valflag=true;
						Framework_Utils.gtnPassLog(Value + " - Item is present in the list box");
						break;
					}
				}

	    	}catch (Exception e) {
				e.getMessage();
			}
			Thread.sleep(5000);
			webDriver.manage().timeouts().implicitlyWait(60l, TimeUnit.SECONDS);
			return valflag;
	    }
	
	/**
	 * This method is responsible selecting a value from list box.
	 * @param element
	 * @param elementName
	 * @param Value
	 * @return
	 * @throws InterruptedException
	 */
	
	public String gtnGetListItems(WebElement element) throws InterruptedException{
		
	 	String ListValues="";
	 
	    List<WebElement> options=element.findElements(By.tagName("option"));
		for (WebElement option : options) {
		  ListValues=ListValues+option.getText()+";";	
		  ListValues=ListValues.substring(0, ListValues.length()-1).trim();
		}				
		return ListValues;
	}
		
		
	/**
	 * This method is for selecting the given radio element.
	 * @author Vinay Ananthaswamy
	 * @param element
	 * @param elementName
	 * @param Value
	 * @return
	 * @throws InterruptedException
	 */
	
	public boolean gtnselectRadioButton(WebElement element, String elementName,String Value) throws InterruptedException{
		boolean flag = false;
		if(gtnGetElement(element)){
			if(!element.isSelected() && !Value.isEmpty() && Value !=  "No"){
				element.click();
				if(element.isSelected() == true){
					Framework_Utils.gtnPassLog(elementName + " Radio button is selected");
					flag = true;
				}				
			}else{
				Framework_Utils.gtnPassLog(elementName + " Radio button is either already selected or not required to be selected");
				flag = true;
			}
		}else{
			Framework_Utils.gtnFailLog(elementName + " Radio button is not available");
			flag = false;
		}
		return flag;
	}

	/**
	 *
	 * @param ListBoxItems
	 * @return
	 */
	
	/**
	 * This method is responsible for selecting a mutliple values from the multilist box. 
	 * @author Pavan Athani
	 * @param element
	 * @param elementName
	 * @param ListBoxItems
	 * @return
	 */
	
	public boolean gtnSelectMultipleItemsInList(WebElement element,String elementName,String ListBoxItems){
		int ilist;
		boolean sflag = false;
		List<String> listvalues = null;
		
		List<String> list = Arrays.asList(ListBoxItems.split(";"));
		
	  if(gtnGetElement(element)){	
		List<WebElement> options=element.findElements(By.tagName("option"));
		
		for (ilist=0;ilist<=list.size()-1;ilist++){
			for(int jlist=0;jlist<=options.size()-1;jlist++){
				listvalues = Arrays.asList(options.get(jlist).getText().trim());
				//Framework_Utils.gtnInfoLog("listvalues"+listvalues);
				if(listvalues.get(0).equalsIgnoreCase(list.get(ilist))){
					options.get(jlist).click();
					Framework_Utils.gtnPassLog(listvalues + " - Item selected successfully in listbox << "+elementName+" >>");
					sflag = true;
					break;
				}else{
					sflag = false;	
				}			
			}
			if(sflag==false){
               Framework_Utils.gtnFailLog(listvalues + "Item not present/failed to select in listbox << "+elementName+" >>");
			}
		}
		
	  }	
		return sflag;
	}

	
	/** 
	 * This method is responsible to execute the given query and return the records as a list values.
	 * @author chethan kumar	 	
	 * @since 18th July 2012
	 * @param Query					:	String variable containing Sql Query which will be executed in the DB. 
	 * @param ColName				:	String variable containing Column Name which needs to be fetched by executing sql query in the DB.
	 * @return						:	Returns Boolean value signifying whether query execution is successful or not
	 * @throws SQLException
	 */			
	
	public List<String> gtnExecuteDBQuery(String Query,int maxtime)throws Exception {
		Connection con = null;	
		List<String> resultSet = new ArrayList();
		
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
				
			if (baseURL.contains("supportp")) {
				con = java.sql.DriverManager.getConnection(PropertiesHolder.properties.get("config").getProperty("SupportpDB"));				
			}			
			else if (baseURL.contains("rctp")) {
				con = java.sql.DriverManager.getConnection(PropertiesHolder.properties.get("config").getProperty("RCTpDB"));
			}
			else if (baseURL.contains("rctq")) {
				con = java.sql.DriverManager.getConnection(PropertiesHolder.properties.get("config").getProperty("RCTqDB"));
			}			
			else if (baseURL.contains("fte")) {
				con=java.sql.DriverManager.getConnection(PropertiesHolder.properties.get("config").getProperty("QASharedDB"));
			}
			else if (baseURL.contains("qaauto1b")) {
				con = java.sql.DriverManager.getConnection(PropertiesHolder.properties.get("config").getProperty("QaAutoDB"));
			}
			
			System.out.println(Query);
			
			Statement statement = null;
			ResultSet rs = null;
			boolean status = false;
			int i = 0;
			Thread.sleep(2000);

			while (status == false && i < maxtime) {
				if (con != null) {
					statement = con.createStatement();
					rs = statement.executeQuery(Query);					
				
					 ResultSetMetaData rsmd = rs.getMetaData();
						while (rs.next()) {
							for (i = 1; i <= rsmd.getColumnCount(); i++)
								resultSet.add(rs.getString(i));
								status = true;
						}
												
						rs.close();
						Thread.sleep(2000);
						i+=1;
					}				  						
			 }
			return resultSet;
			
		} catch (Exception e) {
			Framework_Utils.gtnFailLog("Can not establish DB connection for the envt " + baseURL);
			e.printStackTrace();
			return null;
		}
		finally {
			con.close();
		}
	}
	
	
	/** 
	 * This method is responsible to execute the given query and return the records as a list values.
	 * @author ckumar 	
	 * @since 09-17-2014
	 * @param Query					:	String variable containing Sql Query which will be executed in the DB. 
	 * @param ColName				:	String variable containing Column Name which needs to be fetched by executing sql query in the DB.
	 * @return						:	Returns Boolean value signifying whether query execution is successful or not
	 * @throws SQLException
	 */			
	
 public void gtnExecuteUpdateDBQuery(String Query,int maxtime)throws Exception {
	
		Connection con = null;	
				
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			
			if (baseURL.contains("supportp")) {
				con = java.sql.DriverManager.getConnection(PropertiesHolder.properties.get("config").getProperty("SupportpDB"));				
			}			
			else if (baseURL.contains("rctp")) {
				con = java.sql.DriverManager.getConnection(PropertiesHolder.properties.get("config").getProperty("RCTpDB"));
			}
			else if (baseURL.contains("fte")) {
				con=java.sql.DriverManager.getConnection(PropertiesHolder.properties.get("config").getProperty("QASharedDB"));
			}else if (baseURL.contains("qaauto1b")) {
				con = java.sql.DriverManager.getConnection(PropertiesHolder.properties.get("config").getProperty("QaAutoDB"));
			}
			
			
			System.out.println(Query);
			
			Statement statement = null;
			ResultSet rs = null;
			boolean status = false;
			int i = 0;
			Thread.sleep(2000);

			while (status == false && i < maxtime) {
				if (con != null) {
					statement = con.createStatement();
					rs = statement.executeQuery(Query);		
				}	
			}
		} 			
		 catch (Exception e) {
			Framework_Utils.gtnFailLog("Can not establish DB connection for the envt " + baseURL);
			e.printStackTrace();			
		}
		finally {
			con.close();
		}
	}		
	
 
 /** 
	 * This method is responsible to execute the given query and return the records as a list values.
	 * @author ckumar 	
	 * @since 09-17-2014
	 * @param Query					:	String variable containing Sql Query which will be executed in the DB. 
	 * @param ColName				:	String variable containing Column Name which needs to be fetched by executing sql query in the DB.
	 * @return						:	no
	 * @throws SQLException
	 */			
	
public void gtnInsertUpdateDeleteDBQuery(String Query,int maxtime)throws Exception {
	
		Connection con = null;	
				
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			
			if (baseURL.contains("supportp")) {
				con = java.sql.DriverManager.getConnection(PropertiesHolder.properties.get("config").getProperty("SupportpDB"));				
			}			
			else if (baseURL.contains("rctp")) {
				con = java.sql.DriverManager.getConnection(PropertiesHolder.properties.get("config").getProperty("RCTpDB"));
			}
			else if (baseURL.contains("fte")) {
				con=java.sql.DriverManager.getConnection(PropertiesHolder.properties.get("config").getProperty("QASharedDB"));
			}
			else if (baseURL.contains("qaauto1b")) {
				con = java.sql.DriverManager.getConnection(PropertiesHolder.properties.get("config").getProperty("QaAutoDB"));
			}
			
			Statement statement=null;		
					 		
			Thread.sleep(2000);
		
				if (con != null) {
					statement = con.createStatement();
					int rs = statement.executeUpdate(Query);	
					System.out.println("rs "+rs);
					if(rs==1)
			            System.out.print("Successfully executed the query");
						Framework_Utils.gtnInfoLog("Successfully executed the query");		
						con.close();
			        } 
		  }
			catch (Exception e) {
	        	  Framework_Utils.gtnFailLog("Can not establish DB connection for the envt " + baseURL);
	  			  e.printStackTrace();
	        }						 	
	}		
 
 
 
	/**
	 * @author chethan kumar
	 * @return
	 * @Method is to check  Missing page and OOPs page
	 * @throws Throwable
	 */
	public int gtnCheckMissingandOOPsPage() {
		int flg = 0;
		String strTitle = webDriver.getTitle();
		if (strTitle.equalsIgnoreCase(GlobalVariables.MISSING_PAGE_MSG) || strTitle.equalsIgnoreCase(GlobalVariables.SERVERErrorPage))
		{
			flg = 1;
			Reporter.log("<font color=RED><b> Page is going to Missisng page </b></font>");
		}
		else if (strTitle.equalsIgnoreCase(GlobalVariables.OOPS_MSG))
		{
			flg = 2;
			Reporter.log("<font color=RED><b> Page is going to oops page </b></font>");
		}
		else if (strTitle.equalsIgnoreCase(GlobalVariables.JBOSSErrorPage))
		{
			flg = 3;
			Reporter.log("<font color=RED><b> Page is going to JBoss Error </b></font>");
		}
		else
			flg = 0;
		return flg;
	}

	/**
	 * @author chethan kumar
	 * @return Returns count (number of occurrences)
	 * @Method is to get the count of same type of element with the help of XPath
	 * @throws Throwable
	 * @param xPathLocators 			:	Contains xpath expression
	 */
	public int gtnGetXPathCount(String xPathLocators) 	{
		int count = webDriver.findElements(By.xpath(xPathLocators)).size();
		return count;
	}

	/**
	 * @author chethan kumar
	 * @return
	 * @throws Throwable
	 */
	public int gtnGetCSSCount(String cssLocator) {
		int count = webDriver.findElements(By.cssSelector(cssLocator)).size();
		return count;
	}

	/**
	 * @author chethan kumar
	 * @return
	 * @throws Throwable
	 */
	public void gtnWaitForElementPresent(final WebElement element,String elementName) throws Exception {
	  try{
		webDriver.manage().timeouts().implicitlyWait(8l, TimeUnit.SECONDS);
		WebDriverWait wait = new WebDriverWait(webDriver, 90);
		wait.until(gtnVisibilityOfElementLocated(element));
		webDriver.manage().timeouts().implicitlyWait(90l, TimeUnit.SECONDS);
		Thread.sleep(200L);
	  }catch(Exception e){
		 Framework_Utils.gtnInfoLog(elementName + " Element not found and could not wait");
	  }
	}
	
	public ExpectedCondition<WebElement> gtnVisibilityOfElementLocated(final WebElement element){
		return new ExpectedCondition<WebElement>() {
			public WebElement apply(WebDriver driver){
				WebElement toReturn = element;
				if (toReturn.isDisplayed() && toReturn.isEnabled()){
					return toReturn;
				}
				return null;
			}
		};
	}
	
	/**
	 * @author chethan kumar
	 * @return
	 * @throws Throwable
	 */
	public void gtnWaitForObjectByXpath(final By element) throws Exception {
		webDriver.manage().timeouts().implicitlyWait(8l, TimeUnit.SECONDS);
		WebDriverWait wait = new WebDriverWait(webDriver, 90);
		wait.until(gtnVisibilityOfObjectByXpath(element));
		webDriver.manage().timeouts().implicitlyWait(90l, TimeUnit.SECONDS);
		Thread.sleep(200L);
	}
	public ExpectedCondition<WebElement> gtnVisibilityOfObjectByXpath(final By locator){
		return new ExpectedCondition<WebElement>() {
			public WebElement apply(WebDriver driver){
				WebElement toReturn = driver.findElement(locator);
				if (toReturn.isDisplayed() && toReturn.isEnabled()){
					return toReturn;
				}
				return null;
			}
		};
	}
	
	
	/**
	 * @author chethan kumar
	 * @return
	 * @throws Throwable
	 */
	public void gtnWaitForElementNotPresent(final WebElement element,String elementName) throws Exception	{
		WebDriverWait wait = new WebDriverWait(webDriver, 200);
		wait.until(gtnNonvisibilityOfElementLocated(element));
	}

	public ExpectedCondition<WebElement> gtnNonvisibilityOfElementLocated(final WebElement element)
	{
		return new ExpectedCondition<WebElement>()
				{
			public WebElement apply(WebDriver driver)
			{
				WebElement toReturn = element;
				System.out.println("Inside nonvisibilityOfElementLocated    ::::   "+toReturn.isDisplayed()+toReturn.getText());
				if (!toReturn.isDisplayed()) 
				{
					return toReturn;
				}
				return null;
			}
				};
	}

	/**
	 * @author chethan kumar
	 * @return
	 * @throws Throwable
	 */
	public boolean gtnIsElementPresent(WebElement element,String elementName) {
		webDriver.manage().timeouts().implicitlyWait(20l, TimeUnit.SECONDS);
		try{
			webDriver.manage().timeouts().implicitlyWait(20l, TimeUnit.SECONDS);
			boolean status = element.isDisplayed();
			if (status == true) {
			//	String[] ele = element.toString().split("-> ");
				//Framework_Utils.gtnPassLog("[" + ele[1] + " : Element is present");
			//	Framework_Utils.gtnPassLog(elementName +"Element is present");
				return true;
			} else {
				//Framework_Utils.gtnFailLog(elementName + " element not found :  ");
				return false;
			}
			//return true;
		} catch (NoSuchElementException e) {
		 //	Framework_Utils.gtnFailLog("No element found :  ");
			e.getMessage();
			return false;
		}

	}

	/**
	 * @author Srinivas JN
	 * @return
	 * @throws Throwable
	 */
	public void gtnWaitUtilLoadingNotPresent(WebElement element) throws InterruptedException
	{
		try{
			webDriver.manage().timeouts().implicitlyWait(300, TimeUnit.SECONDS);
			while (true) 
			{
				boolean isload = element.isDisplayed();
				if (!isload){
					break;
				}
				else{
					Thread.sleep(500);
				}
			}
		}catch (Exception e) {
			e.getMessage();
		}
		
		// FIXME; Why further sleep, if already loaded
		Thread.sleep(5000L);
		webDriver.manage().timeouts().implicitlyWait(60l, TimeUnit.SECONDS);
		
	}
	
	
	/**
	 * @author chethan kumar
	 * @return
	 * @throws Throwable
	 */
	public MapObject gtnReadFromExcel(String filepath, int sheetnum) {
		int j = 0, colcount = 0, flg = 0;
		MapObject mapObj = new MapObject();
		String tcid = null, tcdesc = null;
		XSSFSheet sheet = null;
		XSSFRow row = null;
		XSSFCell cell = null, keycell = null;
		try {
			sheet = gtnGetValuesFromExcel(filepath, sheetnum);
			int number = sheet.getLastRowNum();
			int i = 8;

			while (i < number) 
			{
				row = sheet.getRow(i);
				cell = row.getCell(0);
				if (cell.getStringCellValue().trim().indexOf("[COMMENT]") == -1  && cell.getStringCellValue().trim().indexOf("END") == -1)
				{
					if (cell.getStringCellValue().trim().indexOf("TestCase") > -1)
					{
						XSSFCell cell1 = row.getCell(1);
						if (Integer.parseInt(cell1.getStringCellValue().trim()) == 1)
						// if (Util.gtnStr2Int(cell1.getStringCellValue().trim()) == 1)
						{
							cell = row.getCell(3);
							tcid = cell.getStringCellValue().trim();

							cell = row.getCell(4);
							tcdesc = cell.getStringCellValue().trim();

							testCaseMap.put(tcid, tcdesc);

							List<ParamDataObject> paramObjList = new ArrayList<>();

							for (j = i + 2 ; j <= number ; j++)
							{
								row = sheet.getRow(j);
								cell = row.getCell(0);
								if (cell.getStringCellValue().trim().indexOf("[COMMENT]") == -1
										&& cell.getStringCellValue().trim().indexOf("TestCase") == -1
										&& cell.getStringCellValue().trim().indexOf("END") == -1)
								{
									colcount = row.getLastCellNum();
									List<String> paramlist = new ArrayList<>();
									ParamDataObject paramObj = new ParamDataObject();

									for (int indx = 0 ; indx < colcount; indx++)
									{
										keycell = row.getCell(indx);
										if (indx == 0)
											paramObj.setKeywordName(keycell.getStringCellValue().trim());
										else if (indx == 2)
											paramObj.setClassname(keycell.getStringCellValue().trim());
										else if (indx >= 3)
										{
											if (keycell.getStringCellValue().trim().indexOf("##") == -1 )
												paramlist.add(keycell.getStringCellValue().trim());
											else
												break;
										}
									}
									paramObj.setParamList(paramlist);
									paramObjList.add(paramObj);
								}
								else if (cell.getStringCellValue().trim().indexOf("TestCase") > -1)
									break;
								else if (cell.getStringCellValue().trim().indexOf("END") > -1)
								{
									flg = 1;
									break;
								}
							}
							keywordMap.put(tcid, paramObjList);
						}
						else
						{
							for (j = i + 2 ; j <= number ; j++)
							{
								row = sheet.getRow(j);
								cell = row.getCell(0);
								if (cell.getStringCellValue().trim().indexOf("TestCase") > -1)
									break;
								else if (cell.getStringCellValue().trim().indexOf("END") > -1)
								{
									flg = 1;
									break;
								}
							}
						}
						i = j ;
					}

				}
				else if (cell.getStringCellValue().trim().indexOf("END") > -1)
					break;
				else
					i++;

				if (flg == 1)
					break;
			}
			mapObj.setTcMap(testCaseMap);
			mapObj.setKeyMap(keywordMap);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(keycell);
		}
		return mapObj;
	}
	
		
	/**
	 * @author chethan kumar
	 * @return
	 * @since 18th July 2012
	 * @throws Throwable
	 * @Method to copy Master PO Xml file specific to a Customer and then Create a new PO Xml file by changing all necessary tags in it
	 * @param Customer	:	String variable containing the name of the Customer for which the XML file would be created
	 **/	
	public void gtnCopyAndCreatePOFile(String FileName){
		try {
			String ID= Framework_Utils.gtnGetUniqueId();
			GlobalVariables.invoiceNum =ID;
			SAXBuilder builder = new SAXBuilder();
			File filepo = new File(System.getProperty("user.dir").concat(intprops.getProperty("MasterPOxmlPath")).concat(FileName).concat(".xml"));
			if (filepo.exists()){ 
				Document document = (Document) builder.build(filepo);
				Element root = document.getRootElement();

				Element FName = root.getChild("TransactionInfo");
				FName.getChild("FileName").setText(ID);

				Element order = root.getChild("Order");
				Element Header = order.getChild("Header");
				Header.getChild("OrderMessageID").setText(ID);
				Header.getChild("OrderNumber").setText(ID);

				String des = new XMLOutputter().outputString(document);
				File NewPOFile = new File(System.getProperty("user.dir") + File.separator + intprops.getProperty("POxmlPath") + File.separator + FileName + ".xml");	
				FileWriter fileWriter = new FileWriter(NewPOFile);
				fileWriter.write(des);
				fileWriter.close();
			}
			else{
				Framework_Utils.gtnFailLog("PO file doesn't exist " + FileName.toUpperCase());
				Assert.assertTrue(false, "PO file doesn't exist");
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}

	}
	
	
	/**
	 @author Amogh Wadeyar
	 * @param param
	 * @return
	 * @throws Exception 
	 * @throws Throwable
	 */
	public  boolean gtnWaitForPageLoad() throws Exception{
		webDriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		Framework_Utils.gtnInfoLog("Waiting for page to load");
		System.out.println("Waiting for page to load");
        //return ((JavascriptExecutor)webDriver).executeScript("return document.readyState").equals("complete");
		((JavascriptExecutor) webDriver).executeScript(
       		"function pageloadingtime()"+
       				"{"+
       				"return 'Page has completely loaded'"+
       				"}"+
       		"return (window.onload=pageloadingtime());");
       return true;
	
	}
	
		
	/**
     * @author chethan kumar
     * @param FilePath
     * @return
     * @throws Exception
     **/
     public boolean gtnFileDownLoad(String FilePath) throws Exception {
            boolean status = false;          
            
            String orgFileName=Framework_Utils.gtnGetFileFromFolder(System.getProperty("user.dir").concat("//resources//datafiles//temp"));
            if(orgFileName==null){
                   Framework_Utils.gtnFailLog("file could not download properly "+ FilePath);
                   return false;
            }
            String srcFileLocation=System.getProperty("user.dir").concat("//resources//datafiles//temp//").concat(orgFileName);
            
            boolean result = Framework_Utils.gtnMoveFile(srcFileLocation,FilePath);
            Thread.sleep(4000);
            
            if(result){
                   // verify if the file has download successfully
                   File fil =  new File(FilePath);   
                   if(fil.exists()== true){
                         System.out.println("file has download successfully - Path:" + FilePath);
                         Framework_Utils.gtnPassLog("file has download successfully - Path:" + FilePath );
                         status = true;
                   }else{
                         System.out.println("failed to download file -Path:"+ FilePath);
                         Framework_Utils.gtnFailLog("failed to download file -Path:"+ FilePath);
                         status = false;
                   }
                   }else{
                         System.out.println("failed to move file -Path:"+ FilePath);
                         Framework_Utils.gtnFailLog("failed to move file -Path:"+ FilePath);
                         status = false;
                   }  
            Framework_Utils.gtnDeleteAllFilesFromFolder(System.getProperty("user.dir") + "//resources//datafiles//temp");
            return status;
     }
		
	/**
	 * This method is responsible for checking the given characters present in the string or not.
	 * @author ckumar
	 * @param StngVal
	 * @param SrchChar
	 */
		
	public void gtnSrchCharacterInString(String StngVal,String SrchChar){
		
		boolean retValString=StngVal.contains(SrchChar);		
			
		if (retValString==true) {
			Framework_Utils.gtnPassLog("The given characters"+SrchChar+ "is found in the given string"+StngVal);
		} else{
			Framework_Utils.gtnFailLog("The given characters"+SrchChar+ "is not found in the given string"+StngVal);
		}				
	}			
	
/**
 * This method is responsible for expand/collapse the given image and validate the same.
 * @author ckumar
 * @param headerName
 * @param event
 * @param srchImage
 */	

	public void gtnSectionHeaderImage(String headerName,String event,String srchImage) {
		
		String img_val=null;
	    String event_val=event.trim().toLowerCase();
	    			
		webDriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);	
		WebElement img_ele=webDriver.findElement(By.xpath("//div[text()='"+headerName+"']/ancestor::table[@class='sectionheader']//img"));
					
		try{
			
			 	gtnscrollTo(webDriver, img_ele);	
			 	Framework_Utils.gtnInfoLog("Scroll to Image element.");
			 	img_val=img_ele.getAttribute("src");
			 
			   if(event_val.equals("yes") && img_val.contains("expandplus.gif")){	 
				 img_ele.click();			
				 Framework_Utils.gtnPassLog("Image is found and its clicked"+"Expected value is-->"+srchImage+"Actual value is-->"+img_val);
			   } else if(event_val.equals("yes") && img_val.contains("collapseminus.gif")){
				   Framework_Utils.gtnInfoLog("Section header is already expanded"); 
			   }else if(event_val.equals("no") && img_val.contains("expandplus.gif")){	 
				 img_ele.click();			
				 Framework_Utils.gtnPassLog("Image is found and its clicked"+"Expected value is-->"+srchImage+"Actual value is-->"+img_val);
			   } else if(event_val.equals("no") && img_val.contains("collapseminus.gif")){
				   Framework_Utils.gtnInfoLog("Section header is already expanded"); 
			   }
				 
			
			 
			
			/* if(event_val.equals("yes")){	 
				  img_val=img_ele.getAttribute("src");	
				  
				  if(img_val.contains(srchImage)){
					  img_ele.click();			
					  Framework_Utils.gtnPassLog("Image is found and its clicked"+"Expected value is-->"+srchImage+"Actual value is-->"+img_val);
				  }else{
					  Framework_Utils.gtnFailLog("Image is not found"+"Expected value is-->"+srchImage+"Actual value is-->"+img_val); 
			 }	  }
			 else{ 
		 		  img_val=img_ele.getAttribute("src");	 	
			
				  if(img_val.contains(srchImage)){
					  Framework_Utils.gtnPassLog("Image is found"+"Expected value is-->"+srchImage+"Actual value is-->"+img_val);
				  }else{
					  Framework_Utils.gtnFailLog("Image is not found"+"Expected value is-->"+srchImage+"Actual value is-->"+img_val); 
				  }	 
				
			 	 }	*/
			 
		} catch(Exception e){
			Framework_Utils.gtnFailLog("Image is not found");
		}		 
		 
   }		
		
	
 /**
  * This method is responsible for tab navigation with in a page.
  * @author ckumar
  * @param eleName
  * @return true 
  * @throws Exception
  */

	public boolean gtnTabNavigation(String eleName) throws Exception {
		
		boolean click_status=false; 
		WebElement tab_ele= null;
		
		webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);	
		tab_ele=webDriver.findElement(By.xpath("(//span[text()='"+eleName.trim()+"']/parent::span/parent::em)[1]"));		
		if(!gtnElementPresent(tab_ele)){
			tab_ele=webDriver.findElement(By.xpath("(//span[text()='"+eleName.trim()+"']/parent::span/parent::em)[2]"));   
		}
		
		try{
			if(tab_ele.isDisplayed()){
				tab_ele.click();
				Thread.sleep(15000);
				click_status=true;
				Framework_Utils.gtnPassLog("element is found and its clicked. its name is-->"+eleName);
			}else{
				Framework_Utils.gtnFailLog("element is not found and its clicked. its name is-->"+eleName);
			 }	
		} catch(Exception e){
			Framework_Utils.gtnFailLog("element is not found"+eleName);
			click_status=false;
		}
		
		return click_status;
	}		
	

     /**
     * This method is responsible for validating the given element is present or not.      
 	 * @author ckumar
 	 * @return true if element is present otherwise returns false.
 	 * @throws Exception 
 	 */
 	public boolean gtnElementPresent(WebElement element) {
 		webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
 		boolean status=false;
 		 try{
 			 status=element.isDisplayed();
 			 
 			}catch(NoSuchElementException e){
 				status=false;
 			}
 		 	return status;		
 	}

 	/**
 	 * This method is responsible for clicking on the given element.  
 	 * @author ckumar
 	 * @return true if element is clicked.
 	 * @throws Exception 
 	 */	
 	
 	public boolean gtnClickElement(WebElement element,String value){
 	
 		webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
 		boolean status=false;
 		 try{
 			 status=element.isDisplayed();		
 		//	 System.out.println("status "+status);
 			 if (status){				 
 				 element.click(); 
 				 Framework_Utils.gtnPassLog("element is found and its clicked.-->"+value);					 
 			  }
 			}catch(NoSuchElementException e){
 				status=false;
 				Framework_Utils.gtnInfoLog("the element is not found.-->"+value);
 			}
 		 	return status;		
 		}	
 	
 	/**
 	 * This method will validate the actaul value with expected value and write it in the logfile.
	 * @author ckumar
	 * @return
	 * @throws Exception 
	 */
	public void gtnElementValuesValidation(String desc,String exp_val,String act_val) {
			
		if(exp_val.trim().equalsIgnoreCase(act_val.trim())){
			Framework_Utils.gtnPassLog(desc+" expected value is matched with actual value. expected value is --> "+exp_val+ " actual value is --> "+act_val);
		}else{
			Framework_Utils.gtnFailLog(desc+" expected value is not matched with actual value. expected value is -->"+exp_val+ "actual value is -->"+act_val);
		}
			
	}
	
 	/**
 	 * This method will do the text comparison and write it in the logfile.
	 * @author ckumar
	 * @return
	 * @throws Exception 
	 */
	public void gtnElementValuesValidation(String exp_val,String act_val) {
			
		if(exp_val.equalsIgnoreCase(act_val)){
			Framework_Utils.gtnPassLog("expected value is matched with actual value. expected value is --> "+exp_val+ " actual value is --> "+act_val);
		}else{
			Framework_Utils.gtnFailLog("expected value is not matched with actual value. expected value is -->"+exp_val+ " actual value is -->"+act_val);
		}
			
	}
	
		
	/**
	 * This method will do the boolean comparison  write it in the logfile
	 * @author ckumar
	 * @return
	 * @throws Exception 
	 */
	public void gtnElementValidation(boolean status,String desc) {

		if(status==true){
			Framework_Utils.gtnPassLog(desc);
		}else{
			Framework_Utils.gtnFailLog(desc);
		}
			
	}
	
	/**
	 * This method is responsible to get the value from the given element.
	 * @author ckumar
	 * @param ele
	 * @return element value otherwise returns null.
	 */
	  
	   public String gtnGetElementValue(WebElement ele){
		   
		   String act_ele_value=null;
		   
		 try{  
			
			act_ele_value=ele.getText().trim();
			 
			/*boolean retval_ele=gtnElementPresent(ele);
		    if(retval_ele){
		      act_ele_value=ele.getText().trim();}
		    else{
		    	 Framework_Utils.gtnInfoLog("element not found");
		     }*/
		    
		 } catch(NoSuchElementException e){
			 Framework_Utils.gtnInfoLog("Exception:Unable to locate the element"); 
			 return act_ele_value;
		 } 
		   
		   return act_ele_value;
		   
	   }
	   
	   
	   	 /**
	   	   * This method is responsible to get the text value from the given element.
		   * @author ckumar
		   * @param ele
		   * @return element value.
		   */
		  
		   public String gtnGetElementTextValue(WebElement ele){
			   
			   String act_ele_value=null;
			   
			 try{  
			    boolean retval_ele=gtnElementPresent(ele);
			    if(retval_ele){
			      act_ele_value=ele.getAttribute("value").trim();}
			    else{
			    	 Framework_Utils.gtnInfoLog("element not found");
			     }
			    
			 } catch(Exception e){
				 Framework_Utils.gtnInfoLog("Exception:Unable to locate the element"); 
				 return act_ele_value;
			 }
			   
			   return act_ele_value;
			   
		   }   
	   
	   
	   
	   
	   /**
   	     * This method is responsible to check the given element is enabled or disabled.
		 * @author ckumar
		 * @return return true, if element is enabled.
		 * @throws Exception 
		 */		
		
		public boolean gtnElementIsEnabled(WebElement element,String ele_name){
			
			boolean enabled_status=false;
			try{
		
				if(element.isDisplayed()){
					enabled_status=element.isEnabled(); // return true when button got enabled.
				//	Framework_Utils.gtnPassLog("<<"+ele_name+">> Element is enabled");
					return enabled_status;	
				}else{
				//	Framework_Utils.gtnFailLog("<<"+ele_name+">> Element is not enabled");
					return enabled_status;			
				  }		
				
			} catch (NoSuchElementException e)	{
				Framework_Utils.gtnInfoLog("<<"+ele_name+">>"+"Element is not found");
				enabled_status=false;
			} return enabled_status;
		}
		
		
		/**
   	     * This method is responsible to check the given element is selected.
		 * @author ckumar
		 * @return return true, if element is selected.
		 * @throws Exception 
		 */		
		
		public boolean gtnElementIsSelected(WebElement element,String ele_name){
			
			boolean selected_status=true;
			try{
		
				if(element.isSelected()){
					selected_status=element.isSelected(); // return true when button got enabled.
				//	Framework_Utils.gtnPassLog("<<"+ele_name+">> Element is enabled");
					return selected_status;	
				}else{
				//	Framework_Utils.gtnFailLog("<<"+ele_name+">> Element is not enabled");
					return selected_status;			
				  }		
				
			} catch (NoSuchElementException e)	{
				Framework_Utils.gtnInfoLog("<<"+ele_name+">>"+"Element is not found");
				selected_status=true;
			} return selected_status;
		}
	
		
		
		
		
		
		
		 /**
   	     * This method is responsible to check the given element is disabled.
		 * @author ckumar
		 * @return return true, if element is enabled.
		 * @throws Exception 
		 */		
		
		public boolean gtnElementIsDisabled(WebElement element,String ele_name){
			
			boolean disabled_status=true;
			try{
		
				if(element.isDisplayed()){
					disabled_status=element.isEnabled(); // return true when button got enabled.
				//	Framework_Utils.gtnPassLog("<<"+ele_name+">> Element is enabled");
					return disabled_status;	
				}else{
				//	Framework_Utils.gtnFailLog("<<"+ele_name+">> Element is not enabled");
					return disabled_status;			
				  }		
				
			} catch (NoSuchElementException e)	{
				Framework_Utils.gtnInfoLog("<<"+ele_name+">>"+"Element is not found");
				disabled_status=true;
			} return disabled_status;
		}
	
		
/**
  * This method is responsible to return the current system date.
  * @author ckumar
  * @return current date.
  */
   public static String getCurrentDate(){
   
		Calendar localCalendar=Calendar.getInstance(TimeZone.getDefault());
	  	
	    int currentDay=localCalendar.get(Calendar.DATE);
	    int currentMonth= localCalendar.get(Calendar.MONTH) + 1;
	    int currentYear= localCalendar.get(Calendar.YEAR);    
	    
	    String cur_date=currentDay+"_"+currentMonth+"_"+currentYear;    
	    return cur_date;
   	}    
		    
		   /**
		    * 
		    * @return current time stamp
		    */
		
/**
 * This method is responsible for return the current system time.
 * @author ckumar
 * @return the current system time.
 */
   
   public static String GetCurrentTime(){
		
		 Calendar cal = Calendar.getInstance();
	     Date myDate=new Date();  
	     cal.setTime(myDate);
	     StringBuffer time = new StringBuffer();
	     time.append(cal.get(java.util.Calendar.HOUR_OF_DAY)).append('_');
	     time.append(cal.get(java.util.Calendar.MINUTE)).append('_');
	     time.append(cal.get(java.util.Calendar.SECOND));
	     String sys_time = time.toString();

		 return sys_time;				
	}

		   
/**
 * This method is responsible get the all item values present in the drop down.
 * @author ckumar	   
 * @param elem
 * @return
 * @throws Exception
 */
	   public String gtnDropDownGetAllItems(WebElement elem) throws Exception{
	 		
	 		String temp_arr_values=null;	
	 		String temp_arr_val="";
	 		Thread.sleep(2000);	
	 		
	 		try{	
		 		 List<WebElement> options = elem.findElements(By.tagName("option"));
		 	    	
		 	     for (WebElement option : options) {   
		 	    	 System.out.println(option.getText());
		 	    	 temp_arr_val=temp_arr_val+";"+option.getText();
		 	    	// System.out.println(temp_arr_val);
		 	    	 temp_arr_values=temp_arr_val.substring(1,temp_arr_val.length()).trim();
		 	      } 
	 	    								
	 			}			
	 			catch(Exception e){	
	 			  System.out.println("Exception");
	 			  return temp_arr_values; 			  
	 			}			 	
	 		return temp_arr_values;
	 	  }  
	   		   
		   
   /**
    * This method is responsible for get the system date based on the given date format.
    * @author ckumar
    * @param dateformat
    * @param inDate
    * @return true/false.
    */
		   
		   public  String GetReqDateFormat(String dateformat) {
			 
			   	String cur_date=null;
			   	int currentDay;
			    int currentMonth;
			    int currentYear;
			    		   	   
			    try {
			    	
			      Calendar localCalendar=Calendar.getInstance(TimeZone.getDefault());	
			    	   
			      switch (dateformat) {
			  	
					  	case "mm/dd/yyyy":		
						    			    currentDay=localCalendar.get(Calendar.DATE);
						    			    currentMonth= localCalendar.get(Calendar.MONTH) + 1;
						    			    currentYear= localCalendar.get(Calendar.YEAR);    
						    			    cur_date=currentMonth+"/"+currentDay+"/"+currentYear;   
						    			    break;
					      			  		
					  	case "dd/yyyy/mm":	currentDay=localCalendar.get(Calendar.DATE);
										    currentMonth= localCalendar.get(Calendar.MONTH) + 1;
										    currentYear= localCalendar.get(Calendar.YEAR);    
										    cur_date=currentDay+"/"+currentMonth+"/"+currentYear;   
										    break;								
					      					
					  	case "yyyy/mm/dd":	currentDay=localCalendar.get(Calendar.DATE);
										    currentMonth= localCalendar.get(Calendar.MONTH) + 1;
										    currentYear= localCalendar.get(Calendar.YEAR);    
										    cur_date=currentYear+"/"+currentMonth+"/"+currentDay;   
										    break;		
										    
						case "mm-dd-yyyy":		
						    			    currentDay=localCalendar.get(Calendar.DATE);
						    			    currentMonth= localCalendar.get(Calendar.MONTH) + 1;
						    			    currentYear= localCalendar.get(Calendar.YEAR);    
						    			    cur_date=currentMonth+"-"+currentDay+"-"+currentYear;   
						    			    break;
						  			  		
						case "dd-yyyy-mm":	currentDay=localCalendar.get(Calendar.DATE);
											currentMonth= localCalendar.get(Calendar.MONTH) + 1;
											currentYear= localCalendar.get(Calendar.YEAR);    
											cur_date=currentDay+"-"+currentMonth+"-"+currentYear;   
											break;								
		  					
						case "yyyy-mm-dd":	currentDay=localCalendar.get(Calendar.DATE);
										    currentMonth= localCalendar.get(Calendar.MONTH) + 1;
										    currentYear= localCalendar.get(Calendar.YEAR);    
										    cur_date=currentYear+"-"+currentMonth+"-"+currentDay;   
										    break;	
														
					  	default:
					  	break;
			  		}  	  
				      
			    } catch (Exception E) {
			    	cur_date=null;
			    }
			    return cur_date;
			  }
		   		   		   		   
   /**
    * This method is responsible for get the system date based on the given date format.
    * @author ckumar
    * @param dateformat
    * @param inDate
    * @return true/false.
    */
		   
  /* public  String GetFutureReqDateFormat(String dateformat,int days) {
		 
		   	String cur_date=null;
		   	int currentDay;
		    int currentMonth;
		    int currentYear;
		    		   	   
		    try {
		    	
		      Calendar localCalendar=Calendar.getInstance(TimeZone.getDefault());	
		          
		    	   
		      switch (dateformat) {
		  	
				case "mm/dd/yyyy":											
				    			    currentDay=localCalendar.get(Calendar.DATE) ;				    			    
				    			    currentMonth= localCalendar.get(Calendar.MONTH);
				    			    currentYear= localCalendar.get(Calendar.YEAR);   				    			    
				    			    cur_date=currentMonth+"/"+currentDay+"/"+currentYear; 
				    			    				    			  
				    		        System.out.println("cur_date"+cur_date);
				    		        
									cur_date=sDateWithFormat("MM/dd/yyyy", days);
				    		        
				    			    
				    			    break;
			      			  		
			  	case "dd/yyyy/mm":	currentDay=localCalendar.get(Calendar.DATE) + days;		
								    currentMonth= localCalendar.get(Calendar.MONTH) + 1;
								    currentYear= localCalendar.get(Calendar.YEAR);    
								    cur_date=currentDay+"/"+currentMonth+"/"+currentYear;   
			  		
			  						cur_date=sDateWithFormat("dd/yyyy/MM", days);
								    break;								
			      					
			  	case "yyyy/mm/dd":	
			  						currentDay=localCalendar.get(Calendar.DATE) + days;		
								    currentMonth= localCalendar.get(Calendar.MONTH) + 1;
								    currentYear= localCalendar.get(Calendar.YEAR);    
								    cur_date=currentYear+"/"+currentMonth+"/"+currentDay;  
			  						cur_date=sDateWithFormat("yyyy/MM/dd", days);
								    break;		
								    
				case "mm-dd-yyyy":		
									currentDay=localCalendar.get(Calendar.DATE) + days;		
				    			    currentMonth= localCalendar.get(Calendar.MONTH) + 1;
				    			    currentYear= localCalendar.get(Calendar.YEAR);    
				    			    cur_date=currentMonth+"-"+currentDay+"-"+currentYear;   
				    			    cur_date=sDateWithFormat("yyyy/MM/dd", days);
				    			    break;
				  			  		
				case "dd-yyyy-mm":	currentDay=localCalendar.get(Calendar.DATE) + days;				
									currentMonth= localCalendar.get(Calendar.MONTH) + 1;
									currentYear= localCalendar.get(Calendar.YEAR);    
									cur_date=currentDay+"-"+currentMonth+"-"+currentYear;   
					 				cur_date=sDateWithFormat("dd-yyyy-MM", days);
									break;								
					
				case "yyyy-mm-dd":	currentDay=localCalendar.get(Calendar.DATE) + days;		
								    currentMonth= localCalendar.get(Calendar.MONTH) + 1;
								    currentYear= localCalendar.get(Calendar.YEAR);    
								    cur_date=currentYear+"-"+currentMonth+"-"+currentDay; 
									cur_date=sDateWithFormat("yyyy-MM-dd", days);
									break;	
															
						  	default:
						  	break;
				  		}  	  
					      
				    } catch (Exception E) {
				    	cur_date=null;
				    }
				    return cur_date;
				  }	   
	   
		   */
		   
		   
		   
		   
		   
		   
		   
		   
   /**
     * This method is responsible  to get the default value from the drop down. 
	 * @author ckumar
	 * @return
	 * @throws Throwable
	 */
	public String gtnGetDefaultSelectListValue(WebElement element) throws InterruptedException{
				
		String Retval=null;
			
			try{	
				if(gtnElementPresent(element)){	
				
					String selectedOption = new Select(element).getFirstSelectedOption().getText();
					Retval=selectedOption;				
					}			
			} catch (Exception e){
				Framework_Utils.gtnInfoLog("Exception encountered during the execution of the method");
				Retval=null;
			}
			return Retval;
	}
			   
	

 /**
  * @author ckumar
  * This method is responsible to handle the alert windows.
  * @param alrtstatus
  * @param alrtmsgval
  */  

 public void gtnHandleAlertWindow(String alrtstatus,String alrtmsgval){
	  		  
	  try{		  
		  String ActAlertMsg=null;
			  
		  if(webDriver.switchTo().alert() != null){  
			  
			    System.out.println("Alert window is present");
			    Alert alert=webDriver.switchTo().alert();
			 
				switch (alrtstatus.toLowerCase().trim()) {
				  
					case "accept":	 ActAlertMsg=alert.getText().trim();
									 gtnElementValuesValidation(alrtmsgval, ActAlertMsg);
									 alert.accept();
									 Framework_Utils.gtnPassLog("Accept the alert window.");
									 System.out.println("Accept the alert window and its message.");
								     break;
						
					case "reject":	ActAlertMsg=alert.getText().trim();							
									alert.dismiss();
									gtnElementValuesValidation(alrtmsgval, ActAlertMsg);
									Framework_Utils.gtnPassLog("Reject the alert window.");
									System.out.println("reject the alert window and its message.");
									break;						
					default:
					break;
				  	}
		     }else{
		    	Framework_Utils.gtnFailLog("Alert window is not present"); 
		    	System.out.println("Alert window is not present");
		     }
		  }catch(Exception e){
			  System.out.println("Exception is encountered");
			  Framework_Utils.gtnInfoLog("Exception is encountered.");
		  }     
	  }  	
	
	
		   
/*******************************************************************************************
// 								GWT Utils
********************************************************************************************/
	
	/** 
	 * This method is responsible to restore the grid based on the given column.
	 * @author venugopal
	 * Method to restore default grid column by passing the xpath
	 * @param param
	 * @return
	 * @throws Exception
	 */
 public boolean gtnGwtGridRestoreDefault(String xpathvalue,String desc) throws Throwable{
	  
		boolean status=false;
	 
		WebElement element=gtnGetObjectByXpath(xpathvalue);
		try{	
			if(gtnElementPresent(element)){
	        	  new Actions(webDriver).moveToElement(element).build().perform();
	        	  webDriver.findElement(By.xpath(xpathvalue + "//a")).click();
	                             
	               if(gtnElementPresent(gtnGetObjectByXpath("//a[contains(text(),'Restore Defaults')]"))){
	                     webDriver.findElement(By.xpath("//a[contains(text(),'Restore Defaults')]")).click();
	                     Framework_Utils.gtnPassLog("Successfully clicked on the Restore Defaults in grid "+desc);
	                     status=true;
	               }
	               else
	            	   Framework_Utils.gtnFailLog("Failed to click on the Restore Defaults in grid "+desc);
	               	   status=false;
	         }
	        else
	        	Framework_Utils.gtnFailLog("element is not present in the grid "+desc);
	     	   
		   }catch(Exception e){
			 //  System.out.println("exception encountered");
			   status = false;
		   }
		return status;
	}
	
	/**
	 * This method is responsible to select the value from the gwt combo box.
	 * @author Chethan Kumar.
	 * @param param
	 * @return
	 * @throws Exception 
	 * @throws Throwable
	 */
	public boolean gtnGwtSelectComboBox(WebElement element1,WebElement element2,String elementName,String ToSelect) throws Exception{
	    boolean sflag = false;
		gtnWaitForElementPresent(element1,elementName);
	    if(gtnGetElement(element1)){	
			 element1.click();
			 Thread.sleep(100);
			 WebElement div = element2;
			 List<WebElement> divs = div.findElements(By.tagName("div"));
				for (WebElement option : divs) {
					if(ToSelect.equals(option.getText())){
						option.click();
						Framework_Utils.gtnPassLog(ToSelect + " - value selected successfully in GWT ComboBox << "+elementName+" >>");
						sflag = true;
						break;
						
					}else{
						sflag = false;
					}
				} 
			   if(sflag==false){
				   Framework_Utils.gtnFailLog(ToSelect + " - value not selected in GWT ComboBox << "+elementName+" >>");
			   }
		}else{
			Framework_Utils.gtnFailLog("GWT ComboBox is not visible to select value");
			sflag=false;
		}
		return sflag;
	}
	
	/**
	 * This method is responsible to get the column number from the gwt grid based on the column name.
	 * @author chethan kumar
	 * @param driver
	 * @param Value
	 * @return
	 */
	public int gtnGetColNumByName(String gridId,String colName){
		int counter = 1;
		boolean flag = false;
	  try{
		List<WebElement> headers = webDriver.findElements(By.xpath("//div[@id='" + gridId + "']//div[@role='columnheader']//span"));
		for (WebElement option : headers) {
			if(colName.equalsIgnoreCase(option.getText().trim())){
				System.out.println("Column Header values are "+option);
				flag=true;
		       break;
			}else{
			   flag=false; 	
			}
			counter++;
		 }
		 if(flag==true){
			 return counter;
		 }else{
			 Framework_Utils.gtnFailLog("Col Name did not match in the grid");
			 return 0;
		 }
		}catch(Exception e){
			//e.printStackTrace();
			return 0;
		}
	}
	
	
	/**
	 * This method is responsible to get the cell value from the gwt grid.
	 * @author chethan kumar
	 * @return
	 * @throws Exception 
	 */
	public String gtnGwtGetTableCellData(String gridId,int row,String colName) throws Exception{
		String cellValue = null;
		int col = 0;
		Thread.sleep(2000);
		gtnWaitForObjectByXpath(By.xpath("//div[@id='" + gridId + "']"));
		webElement = gtnGetObjectByXpath("//div[@id='"+ gridId + "']");
		
		if(webElement!=null){
			if (Framework_Utils.isInteger(colName)==true){
			 Integer colNum = new Integer(colName);
			 col = colNum;
			}else{
				int colNum = gtnGetColNumByName(gridId,colName);
				col = colNum;
			}
			if(col>=1){				
				webElement = gtnGetObjectByXpath("//div[@id='"+ gridId +"_row_"+ row + "']//td["+ col +"]//div");
				if(webElement.isDisplayed()){
					//String value = webDriver.findElement(By.xpath("//div[@id='demoLocalGrid_row_"+ row + "']//td["+ colNum +"]//div")).getText();
					cellValue = webElement.getText();
					Framework_Utils.gtnPassLog(cellValue + " Value entered in the element");
				}else{
					Framework_Utils.gtnFailLog("The Element not found");
				}	
			}
		}else{
			Framework_Utils.gtnFailLog("The div element not found");
		}
		return cellValue;
	}
	
	/**
	 * This method is responsible to set the cell value  for the editable gwt grid.
	 * @author chethan kumar
	 * @return
	 * @throws Exception 
	 */
	public boolean gtnGwtSetTableCellData(String gridId,String objType,int row,String colName,String itemValue) throws Exception{
		boolean status = false;
		int col = 0;
		Thread.sleep(2000);
		gtnWaitForObjectByXpath(By.xpath("//div[@id='" + gridId + "']"));
		webElement = gtnGetObjectByXpath("//div[@id='"+ gridId + "']");
		if(webElement!=null){
			if (Framework_Utils.isInteger(colName)==true){
			 Integer colNum = new Integer(colName);
			 col = colNum;
			}else{
				int colNum = gtnGetColNumByName(gridId,colName);
				col = colNum;
			}
			switch(objType.toLowerCase()){
			    case "checkbox": 
					webElement = gtnGetObjectByXpath("//div[@id='"+ gridId +"_row_"+ row + "']//td["+ col +"]//div");
					  if(webElement.isDisplayed()){
								webElement.click();							
								Framework_Utils.gtnPassLog("checkbox selected successfully");
								status = true;
					  }else{
						  Framework_Utils.gtnFailLog("The checkbox element not found");
						  status = false;
					  }
					  break;
			    case "link":
			    	webElement = gtnGetObjectByXpath("//div[@id='"+ gridId +"_row_"+ row + "']//td["+ col +"]//div/a");
					  if(webElement.isDisplayed()){
						  webElement.click();
						  Framework_Utils.gtnPassLog("Link clicked successfully");
						  status = true;
					  }else{
						  Framework_Utils.gtnFailLog("The Link element not found");
						  status = false;
					  }
					  break;
			    case "edit":
			    	break;		    
		    }
			
		}else{
			Framework_Utils.gtnFailLog("The div table element not found");
		}
		return status;
	}	
	
	/**
	 * This method is responsible to check all the check boxes present in the gwt grid.
	 * @author chethan kumar
	 * @return
	 * @throws Exception 
	 */
	public boolean gtnGwtTableCheckAll(String gridId) throws Exception{
		boolean status = false;
		//int col = 0;
		Thread.sleep(2000);
		gtnWaitForObjectByXpath(By.xpath("(//div[@id='" + gridId + "']//div[@role='columnheader'])[1]"));
		webElement = gtnGetObjectByXpath("(//div[@id='"+gridId+"']//div[@role='columnheader'])[1]");
		if(webElement!=null){
			webElement.click();
			Framework_Utils.gtnPassLog("CheckAll checkbox clicked successfully");
		}else{
			Framework_Utils.gtnFailLog("The div table element not found");
		}
		return status;
	}
	
	/**
	   * This method is responsible for the returning the row number based on the cell value present in the grid.
	   * @author vinod&chethan
	   * @param gridid
	   * @param ColNum
	   * @param search_text
	   * @return row number if value is matched else it will return -1.
	   * @throws Exception
	   */
	          
	public int gtnGetRowWithCellText(String gridId,int ColNum,String searchText) throws Exception {
	  	  int ret_val=-1;
		  int rowcount_final = -1;
		  int counter = 0;
		  boolean status = true;
		  
		  Thread.sleep(3000);
		  
		  	 while(status){
		  		 Thread.sleep(2000);
		  		 WebElement ele_next=webDriver.findElement(By.xpath("//*[@id='"+gridId+"_pagingToolbar_next']//button"));
		  		 status=ele_next.getAttribute("aria-disabled").equals("false");	  		
		  		 ret_val=gtnGetRowNum(gridId, ColNum,searchText);
		  		 
		  		if(ret_val==-1){
		  			webDriver.findElement(By.xpath("//*[@id='"+gridId+"_pagingToolbar_next']//button/img")).click();
		  			webDriver.manage().timeouts().implicitlyWait(350, TimeUnit.SECONDS);
		  			Thread.sleep(3500);
		  			counter++;
		  			if(counter>=20){
		  				rowcount_final = -1;
		  				break;
		  			}	
		  		 }else{
		  			rowcount_final= ret_val;
		  			break;
		  		 }	 
		  	   }  	    	  
		  	    	  
	  		 return rowcount_final;
		}
	  
	  /**
	   * This method is responsible for the returning the rownumber based on the cell value present in the grid.
	   * @author ckumar
	   * @param gridid
	   * @param ColNum
	   * @param search_text
	   * @return a positive number,if values is matched.
	   * @throws Exception
	   */
  public int gtnGetRowNum(String gridId,int ColNum,String searchText)throws Exception{	  
		  	  int Page_rowCount=0;
		  	  boolean flag = false;
			  int i=0;		 	  

			  		Page_rowCount=webDriver.findElements(By.xpath("//*[contains(@id,'"+gridId+"_row')]")).size();
			  		
					for (i=0; i <=Page_rowCount-1;i++) {				  				
						  String xpath_key_val="//div[@id='"+ gridId +"_row_"+ i +"']//td["+ColNum+"]//div";
						  String xpath_key=xpath_key_val.trim();
						//  System.out.println(xpath_key);
						  String ret_val=webDriver.findElement(By.xpath(xpath_key)).getText().trim();
						 // System.out.println(ret_val);			
						  
						  if(ret_val.equalsIgnoreCase(searchText)){			
							  flag = true;
							  break;
						    }else{
						      flag = false;					  					      
						    }			  	  			   								 	
					  }
		
					if(flag==true){				
					return i;
					}else{				
					 return -1;
					}
			}
		         


	  	/**
	  	 * This method is responsible for return the number of rows present in the grid.
		 * @author ckumar
		 * @return return number rows present in the grid.
		 * @throws Exception 
		 */	
			
		public int gtnGridRowsCount(String gridId) throws Exception {		
			int row_count=-1;
			
			String disp_items_val=webDriver.findElement(By.xpath("(//div[contains(@id,'"+gridId+"')]/descendant::*[contains(@class,'my-paging-display x-component')])[1]")).getText();
	      
		  	  try {
		  		  
		  		  	disp_items_val=webDriver.findElement(By.xpath("//div[contains(@id,'"+gridId+"_pagingToolbar')]/descendant::*[contains(@class,'my-paging-display x-component')]")).getText();
		    	  	System.out.println("Display number of line items are --> "+disp_items_val);
		    	 	int start_index=disp_items_val.indexOf("of");	    	   	  
		    	  	String total_count=disp_items_val.substring(start_index+2, disp_items_val.length()).trim();   
		       	  	row_count=Integer.parseInt(total_count);  	
		  	  	} catch (Exception e) {			
		  	  	  return -1;
		  	  	} 
	    	    	
			 return row_count;					
		}	

		
/**
 * This method is responsible for return the number of rows present in the grid.
 * @author ckumar
 * @return return number rows present in the grid.
 * @throws Exception 
 */	
			
	public int gtnGwtGridRowsCount(String gridId) throws Exception {		
		int row_count=-1;
						      
	  	  try {
	  		  
	  		List<WebElement> GrdTbl=webDriver.findElements(By.xpath("//div[starts-with(@id,'"+ gridId +"_row_')]"));
	  		row_count = GrdTbl.size();
	  		
	  	  	} catch (Exception e) {			
	  	  	  return -1;
	  	  	} 
    	    	
		 return row_count;					
	}	

		
		
		
		
		
		
		
		
		
		
		
		

	/**
		 * This method is responsible for getting the cell value from the gwt grid.
		 * @author ckumar
		 * @param gridId
		 * @param row
		 * @param col
		 * @return cell value.
		 * @throws Exception
		 */

	  	public String gtnGwtGetCellData(String gridId,int row,int col) throws Exception{
	  		String cellValue = null;		
	  		Thread.sleep(2000);	
	  		
	  		try{	
	  				WebElement cell_ele=gtnGetObjectByXpath("//div[@id='"+ gridId +"_row_"+ row + "']//td["+ col +"]//div");
	  				cellValue=cell_ele.getText();  						
	  			}			
	  			catch(Exception e){  		
	  				Framework_Utils.gtnInfoLog("Exception:Unable to locate the element");  	
	  			  return cellValue;
	  			}			
	  	
	  		return cellValue;
	  	  }  


		/**This method is responsible to the check whether gwt widget is disabled.
		 * @author ckumar
		 * @return return true, if element is disabled.
		 * @throws Exception 
		 */		
		
		public boolean gtnGwtElementIsDisabled(WebElement element){
				boolean disabled_status=false;
				try{
				     disabled_status=element.getAttribute("aria-disabled").equals("true"); // return true when button got disabled.	  	 
				   } 
				catch(Exception e){
					disabled_status=false;
				}			
				return disabled_status;		
			}
		
		
		/**This method is responsible to the check whether gwt widget is disabled.
		 * @author ckumar
		 * @return return true, if element is disabled.
		 * @throws Exception 
		 */		
		
		public boolean gtnGwtElementIsDisabled(WebElement element, String eleType){
				boolean disabled_status=false;
				try{
					
					switch (eleType) {
					
					case "dropdown":
						disabled_status=element.getAttribute("disabled").equals("true"); // return true when button got disabled.	  	 		
						break;

					default:
						Framework_Utils.gtnInfoLog("Type mismatch");
						break;
					}					
				     
				   } 
				catch(Exception e){
					disabled_status=false;
				}			
				return disabled_status;		
			}

		
	/** This method is responsible to the check whether gwt widget is enabled.
	 * @author ckumar
	 * @return return true, if element is enabled.
	 * @throws Exception 
	 */		
	
	public boolean gtnGwtElementIsEnabled(WebElement element){
		webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		boolean disabled_status=false;
		
		try{
			disabled_status=element.getAttribute("aria-disabled").equalsIgnoreCase("false"); // return true when button got enabled.
			System.out.println("disabled_status"+disabled_status);
		  } 
		catch(Exception e){
			disabled_status=false;
		}			
		return disabled_status;						
	}			
	
		
	/**
	   * This method is responsible for the checking the paging tool bar button status is enabled.
	   * @param page_name
	   * @param field_name
	   * @param enable_status
	   * @param click_status
	   * @return
	   */		
		  
  public void gtnGetPagingToolbarButtonsEnableStatus(String pageName,String fieldName,String enableState,String click_status){
				  	   
				 boolean enabled_status=false;					   
				 webDriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);			   
									   
				try{   
					   
						switch (fieldName.toLowerCase().trim()) {	   
					   	    	  
						case "first":	  WebElement frst_ele=webDriver.findElement(By.xpath("//table[@id='"+pageName.trim()+"_pagingToolbar_first']//button"));
						 				  enabled_status=gtnGwtElementIsEnabled(frst_ele);			  
										  if(enabled_status==true && enableState.toLowerCase().trim()=="yes"){
											  Framework_Utils.gtnPassLog("Paging tool bar first element is enabled in the grid page-->"+pageName);									 
										  }	else if(enabled_status==false && enableState.toLowerCase().trim()=="no"){
											  Framework_Utils.gtnPassLog("Paging tool bar first element is enabled in the grid page-->"+pageName);									 
										  }else{
											//  Framework_Utils.gtnFailLog("Paging tool bar first element is not enabled in the grid page-->"+page_name); 
										  }
										  break;
															
						case "prev":	  WebElement prev_ele= webDriver.findElement(By.xpath("//table[@id='"+pageName.trim()+"_pagingToolbar_prev']//button"));
										  enabled_status=gtnGwtElementIsEnabled(prev_ele);										
										  if(enabled_status==true && enableState.toLowerCase().trim()=="yes"){
										  Framework_Utils.gtnPassLog("Paging tool bar prev element is enabled in the grid page-->"+pageName);
										  }else if(enabled_status==false && enableState.toLowerCase().trim()=="no"){
											  Framework_Utils.gtnPassLog("Paging tool bar prev element is not enabled in the grid page-->"+pageName);
										  }else{
											//  Framework_Utils.gtnFailLog("Paging tool bar prev element is not enabled/disabled in the grid page-->"+page_name); 
										  }
											break;
						
						case "text":
							 break;
							 
						case "next":	  WebElement next_ele= webDriver.findElement(By.xpath("//table[@id='"+pageName.trim()+"_pagingToolbar_next']//button"));
										  enabled_status=gtnGwtElementIsEnabled(next_ele);
										  
										  if(enabled_status==true && enableState.toLowerCase().trim()=="yes"){
										  Framework_Utils.gtnPassLog("Paging tool bar next element is enabled in the grid page-->"+pageName);
										  }else if(enabled_status==false && enableState.toLowerCase().trim()=="no"){
											  Framework_Utils.gtnPassLog("Paging tool bar next element is not enabled in the grid page-->"+pageName);
										  }else{
											//  Framework_Utils.gtnFailLog("Paging tool bar next element is not enabled/disabled in the grid page-->"+page_name); 
										  }							
							 break;

						case "last":	  WebElement last_ele=webDriver.findElement(By.xpath("//table[@id='"+pageName.trim()+"_pagingToolbar_last']//button"));
									  	  enabled_status=gtnGwtElementIsEnabled(last_ele);
																				 
										  if(enabled_status==true && enableState.toLowerCase().trim()=="yes"){
											  Framework_Utils.gtnPassLog("Paging tool bar last element is enabled in the grid page-->"+pageName);
										  }else if(enabled_status==false && enableState.toLowerCase().trim()=="no"){
											  Framework_Utils.gtnPassLog("Paging tool bar last element is not enabled in the grid page-->"+pageName);
										  }else{
											//  Framework_Utils.gtnFailLog("Paging tool bar last element is not enabled/disabled in the grid page-->"+page_name); 
										  }	
										  break;

						case "refresh":
										  WebElement refresh_ele= webDriver.findElement(By.xpath("//table[@id='"+pageName.trim()+"_pagingToolbar_refresh']//button"));
										  enabled_status=gtnGwtElementIsEnabled(refresh_ele);																				 
										  if(enabled_status==true && enableState.toLowerCase().trim()=="yes"){
											  Framework_Utils.gtnPassLog("Paging tool bar refresh element is enabled in the grid page-->"+pageName);								
										  }else if(enabled_status==false && enableState.toLowerCase().trim()=="no"){
											  Framework_Utils.gtnPassLog("Paging tool bar refresh element is enabled in the grid page-->"+pageName);
										  }	else{
											 // Framework_Utils.gtnFailLog("Paging tool bar refresh element is not enabled/disabled in the grid page-->"+page_name); 
										  }	
										  break;					 
									 			 
						case "excel":
										  WebElement excel_ele= webDriver.findElement(By.xpath("(//table[@id='"+pageName.trim()+"_pagingToolbar_excelButton']//button)[1]"));
										  enabled_status=gtnGwtElementIsEnabled(excel_ele);
																				 
										  if(enabled_status==true && enableState.toLowerCase().trim()=="yes"){
											  Framework_Utils.gtnPassLog("Paging tool bar excel element is enabled in the grid page-->"+pageName);
										  }else if(enabled_status==false && enableState.toLowerCase().trim()=="no"){
											  Framework_Utils.gtnPassLog("Paging tool bar excel element is enabled in the grid page-->"+pageName); 
										  }else{
											//  Framework_Utils.gtnFailLog("Paging tool bar excel element is not enabled/disabled in the grid page-->"+page_name); 
										  }	
										  break;
						
						default:		  Framework_Utils.gtnInfoLog("Invalid field name");
						break;
					  }
					   
					} catch(Exception e){
						Framework_Utils.gtnInfoLog("Unable to locate the element-->paging tool bar buttons");
					  }  
						  
		 }
				  
		  
		
	  /**
	   * This method is responsible for the checking the paging tool bar button status is disabled.
	   * @param page_name
	   * @param field_name
	   * @param disable_status
	   * @return
	   */  
		  
		  public void gtnGetPagingToolbarButtonsDisableStatus(String pageName,String fieldName,String disableState){
		  	   
			   boolean disabled_status=false;	   
			   
			   try{
					   switch (fieldName.toLowerCase().trim()) {
					    	  
						case "first":	
										  WebElement frst_ele=webDriver.findElement(By.xpath("(//table[@id='"+pageName.trim()+"_pagingToolbar_first']//button)[1]"));
										  if(!gtnElementPresent(frst_ele)){
											  frst_ele=webDriver.findElement(By.xpath("(//table[@id='"+pageName.trim()+"_pagingToolbar_first']//button)[2]"));   
										  }	
										  
										  disabled_status=gtnGwtElementIsDisabled(frst_ele);
										  if(disabled_status==true && disableState.toLowerCase().trim()=="yes"){
											  Framework_Utils.gtnPassLog("Paging tool bar first element is disabled in the grid page-->"+pageName);
										  }else if(disabled_status==false && disableState.toLowerCase().trim()=="no"){
												  Framework_Utils.gtnPassLog("Paging tool bar first element is disabled in the grid page-->"+pageName);
										  }else{
											  Framework_Utils.gtnFailLog("Paging tool bar first element is not disabled in the grid page-->"+pageName); 
										  }
										  break;
															
						case "prev":	  WebElement prev_ele=webDriver.findElement(By.xpath("(//table[@id='"+pageName.trim()+"_pagingToolbar_prev']//button)[1]"));
										  if(!gtnElementPresent(prev_ele)){
											  prev_ele=webDriver.findElement(By.xpath("(//table[@id='"+pageName.trim()+"_pagingToolbar_prev']//button)[2]"));   
										    }	
										  disabled_status=gtnGwtElementIsDisabled(prev_ele);
										  if(disabled_status==true && disableState.toLowerCase().trim()=="yes"){
											  Framework_Utils.gtnPassLog("Paging tool bar prev element is disabled in the grid page-->"+pageName);
										  }else if(disabled_status==false && disableState.toLowerCase().trim()=="no"){
											  Framework_Utils.gtnPassLog("Paging tool bar prev element is disabled in the grid page-->"+pageName);
										  }
										  else{
											  Framework_Utils.gtnFailLog("Paging tool bar prev element is not enabled/disabled in the grid page-->"+pageName); 
										  }
										  break;
						
						case "text":
							 break;
							 
						case "next":	  WebElement next_ele=webDriver.findElement(By.xpath("(//table[@id='"+pageName.trim()+"_pagingToolbar_next']//button)[1]"));
										  if(!gtnElementPresent(next_ele)){
											  next_ele=webDriver.findElement(By.xpath("(//table[@id='"+pageName.trim()+"_pagingToolbar_next']//button)[2]"));   
										    }							
										  disabled_status=gtnGwtElementIsDisabled(next_ele);
										  if(disabled_status==true && disableState.toLowerCase().trim()=="yes"){
											  Framework_Utils.gtnPassLog("Paging tool bar next element is disabled in the grid page-->"+pageName);
											}else if(disabled_status==false && disableState.toLowerCase().trim()=="no"){
												  Framework_Utils.gtnPassLog("Paging tool bar next element is disabled in the grid page-->"+pageName);
												}
										  else{
											  Framework_Utils.gtnFailLog("Paging tool bar next element is not enabled/disabled in the grid page-->"+pageName); 
										  }							
										  break;
				
						case "last":	  WebElement last_ele= webDriver.findElement(By.xpath("(//table[@id='"+pageName.trim()+"_pagingToolbar_last']//button)[1]"));
										  if(!gtnElementPresent(last_ele)){
											  last_ele=webDriver.findElement(By.xpath("(//table[@id='"+pageName.trim()+"_pagingToolbar_last']//button)[2]"));   
										    }	
										
										  disabled_status=gtnGwtElementIsDisabled(last_ele);
										  if(disabled_status==true && disableState.toLowerCase().trim()=="yes"){
											  Framework_Utils.gtnPassLog("Paging tool bar last element is disabled in the grid page-->"+pageName);
											}else if(disabled_status==false && disableState.toLowerCase().trim()=="no"){
												  Framework_Utils.gtnPassLog("Paging tool bar last element is disabled in the grid page-->"+pageName);
												}
										  else{
											  Framework_Utils.gtnFailLog("Paging tool bar last element is not enabled/disabled in the grid page-->"+pageName); 
										  }	
										  break;
				
						case "refresh":
										  WebElement refresh_ele= webDriver.findElement(By.xpath("(//table[@id='"+pageName.trim()+"_pagingToolbar_refresh']//button)[1]"));
										  if(!gtnElementPresent(refresh_ele)){
											  refresh_ele=webDriver.findElement(By.xpath("(//table[@id='"+pageName.trim()+"_pagingToolbar_refresh']//button)[2]"));   
										    }
										  disabled_status=gtnGwtElementIsDisabled(refresh_ele);
										  if(disabled_status==true && disableState.toLowerCase().trim()=="yes"){
											  Framework_Utils.gtnPassLog("Paging tool bar refresh element is disabled in the grid page-->"+pageName);
										 }else if(disabled_status==false && disableState.toLowerCase().trim()=="no"){
											  Framework_Utils.gtnPassLog("Paging tool bar refresh element is disabled in the grid page-->"+pageName);
										 }
										  else{
											  Framework_Utils.gtnFailLog("Paging tool bar refresh element is not enabled/disabled in the grid page-->"+pageName); 
										  }	
										  break;					 
									 			 
						case "excel":
										  WebElement excel_ele= webDriver.findElement(By.xpath("(//table[@id='"+pageName.trim()+"_pagingToolbar_excelButton']//button)[1]"));
										  if(!gtnElementPresent(excel_ele)){
											  excel_ele=webDriver.findElement(By.xpath("(//table[@id='"+pageName.trim()+"_pagingToolbar_excelButton']//button)[2]"));   
										    }
										  disabled_status=gtnGwtElementIsDisabled(excel_ele);
										  if(disabled_status==true && disableState.toLowerCase().trim()=="yes"){
											  Framework_Utils.gtnPassLog("Paging tool bar excel element is disabled in the grid page-->"+pageName);
										 }else if(disabled_status==false && disableState.toLowerCase().trim()=="no"){
											  Framework_Utils.gtnPassLog("Paging tool bar excel element is disabled in the grid page-->"+pageName);
										 }
										  else{
											  Framework_Utils.gtnFailLog("Paging tool bar excel element is not enabled/disabled in the grid page-->"+pageName); 
										  }	
										  break;
						
						default:		  Framework_Utils.gtnInfoLog("Invalid field name");
						break;
					}			   
			 
			   }catch(Exception e){
				   Framework_Utils.gtnInfoLog("Unable to locate the element paging tool bar buttons");
			   }			  
					   
			}   
		  

	/** This method is responsible for return the column header values from the grid.
	 * @author ckumar
	 * @param gridid
	 * @return column header values
	 * @throws Exception 
	 */
		     public String gtnGwtGridColumnHeaderValues(String gridId) throws Exception {
			 String col_values=null;
			 
			 try {		 
				 webDriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);		
				 List<WebElement> col_headers=webDriver.findElements(By.xpath("//div[@id='"+gridId+"']//tr[@class='x-grid3-hd-row']//td")); 

				 for (WebElement option : col_headers) {
					  String retval=option.getText().trim();
					  col_values=col_values+retval;
					 }		
				   return col_values;				 
				 
			} catch (Exception e) {
				return null;
			}		
			   	
		  } 

		     
    /**
     * This method is responisble for the sorting the given grid based on the column by ascending or descending.
     * @param xpathvalue
     * @param sortytype
     * @param desc
     * @return
     * @throws Throwable
     */
		     		     
 public boolean gtnGwtGridSort(String xpathvalue,String sortytype,String desc) throws Throwable{
		    		 	  
		    	 boolean status=false;		 		
		    	 WebElement element=gtnGetObjectByXpath(xpathvalue);
		    			 
		    	try{	
		    		 			
		    		 	switch (sortytype.toLowerCase().trim()) {
		    				
		    		 		case "asc":
		    								if(gtnElementPresent(element)){
		    									new Actions(webDriver).moveToElement(element).build().perform();
		    									webDriver.findElement(By.xpath(xpathvalue + "//a")).click();
		    			 	                             
		    					 	               if(gtnElementPresent(gtnGetObjectByXpath("//a[contains(text(),'Sort Ascending')]"))){
		    					 	                     webDriver.findElement(By.xpath("//a[contains(text(),'Sort Ascending')]")).click();
		    					 	                     webDriver.manage().timeouts().implicitlyWait(80, TimeUnit.SECONDS);
		    					 	                     Framework_Utils.gtnPassLog("Successfully clicked on the Sort Ascending in grid "+desc);
		    					 	                     status=true;
		    					 	               }
		    					 	               else
		    					 	            	   Framework_Utils.gtnFailLog("Failed to click on the Sort Ascending in grid "+desc);
		    					 	               	   status=false;
		    							 	         }
		    						 	        else
		    						 	        	Framework_Utils.gtnFailLog("element is not present in the grid "+desc);						 	    
		    										break;
		    										
		    										
		    		 		case "dsc":
		    										if(gtnElementPresent(element)){
		    											new Actions(webDriver).moveToElement(element).build().perform();
		    											webDriver.findElement(By.xpath(xpathvalue + "//a")).click();
		    					 	                             
		    							 	               if(gtnElementPresent(gtnGetObjectByXpath("//a[contains(text(),'Sort Descending')]"))){
		    							 	                     webDriver.findElement(By.xpath("//a[contains(text(),'Sort Descending')]")).click();
		    							 	                     webDriver.manage().timeouts().implicitlyWait(80, TimeUnit.SECONDS);
		    							 	                     Framework_Utils.gtnPassLog("Successfully clicked on the Sort Descending in grid "+desc);
		    							 	                     status=true;
		    							 	               }
		    							 	               else
		    							 	            	   Framework_Utils.gtnFailLog("Failed to click on the Sort Descending in grid "+desc);
		    							 	               	   status=false;
		    									 	         }
		    								 	        else
		    								 	        	Framework_Utils.gtnFailLog("element is not present in the grid "+desc);			 	    
		    								 	        	break;	
		    								 	        	
		    					default: Framework_Utils.gtnInfoLog("Sorting type is wrong.");
		    					break;	
		    		 	}				 				
		    		}
		    		 	catch(Exception e){
		    			   System.out.println("exception encountered");
		    			   status = false;
		    		    }
		    				return status;
		    	}		     
		     
		     
		     
 //##########################################################################################	
	
 /**
  * This method is responsible to validate the gwt checkbox is checked or not.
  * @param Chkbox
  * @return
  */
 public boolean gtnGwtChkboxChkd(WebElement Chkbox){
		  
		  boolean Chkstatus=false;
	try{     
		      String chkboxval=Chkbox.getAttribute("class");
		      
		      if (chkboxval.contains("x-grid3-row-selected")) {
				System.out.println("Checkbox checked");
				Chkstatus=true;
			  }else{
				  System.out.println("Checkbox not checked");
				  Chkstatus=false;
			  }  
	  }catch(Exception e){
		   Chkstatus=false;
		   
	 }
	     return Chkstatus;
	 }
		    
 
 // New utility methods developed by shubha.
 
	/** This method is responsible for verifying the expected text with actual data.
	 * @author Shubha
	 * @param gridid
	 * @return
	 * @throws Exception
	 */

	public boolean gtnVerifyTextPresent(WebElement element, String ExpectedText) throws InterruptedException
	{
		boolean result = false;
		try{
			if(element.getText().contains(ExpectedText)){
				result = true;
			}
		}catch (Exception e) {
			e.getMessage();
		}
		Thread.sleep(5000L);
		webDriver.manage().timeouts().implicitlyWait(60l, TimeUnit.SECONDS);
		return result;

	}

   /**
     * @author Shubha
     * @return
     * @throws Exception
      */
	    public boolean gtnCheckItemPresenceInListbox(WebElement ele,String Value) throws InterruptedException {
	    	boolean valflag=false;
	    	try{
		    	List<WebElement> options = ele.findElements(By.tagName("option"));
				for (WebElement option : options) {
					if(Value.equals(option.getText())){
						valflag=true;
						Framework_Utils.gtnPassLog(Value + " - Item is present in the list box");
						break;
					}
				}

	    	}catch (Exception e) {
				e.getMessage();
			}
			Thread.sleep(5000);
			webDriver.manage().timeouts().implicitlyWait(60l, TimeUnit.SECONDS);
			return valflag;
	    }
	    
  
	/**
	 * @author Priyanka
	 * @param
	 * @return
	 * @throws Exception
	 */
	public boolean gtnselectCheckbox(WebElement element, String elementName) throws InterruptedException{

     boolean flag = false;

     if(gtnIsElementPresent(element, elementName)){
            if(!element.isSelected()){
                  element.click();
                  if(element.isSelected() == true){
                         Framework_Utils.gtnPassLog(elementName + " Checkbox  is selected");
                         flag = true;
                  }
            }else{
                  Framework_Utils.gtnFailLog(elementName + " Checkbox is either already selected or not required to be selected");
                  flag = true;
            }
     }else{
            Framework_Utils.gtnFailLog(elementName + " Checkbox is not available");
            flag = false;
     }
     return flag;
}
	
	
  /**
    * @author chethan kumar
    * @param MainWindowHanlde
    * @return
    */
    public boolean gtnSwitchWindow(String MainWindowHanlde){
           boolean swtflag = false;

      try{
           int cnter=0;
           while(webDriver.getWindowHandles().size()<=1 ){
                  Thread.sleep(200);
                  if(webDriver.getWindowHandles().size()>1||cnter>20){
                        break;
                  }
                  cnter++;
           }

           Set<String> handles = webDriver.getWindowHandles();
           for(String handle:handles){
                  if(!handle.equals(MainWindowHanlde)){
                        webDriver.switchTo().window(handle);
                        //Framework_Utils.gtnPassLog("Switched to new window successfully");
                        swtflag = true;
                        break;
                  }
           }

      }catch(Exception e){
             Framework_Utils.gtnFailLog("Failed to Switch to new window");
             return false;
      }
    return swtflag;
}
 
 
    /**
     * @author ckumar
     * This method is responsible for to check the given string is numeric or not.
     * @param str
     * @return
     */

	
	public static boolean isStringNumeric(String str )
	{
	    DecimalFormatSymbols currentLocaleSymbols = DecimalFormatSymbols.getInstance();
	    char localeMinusSign = currentLocaleSymbols.getMinusSign();

	    if ( !Character.isDigit( str.charAt( 0 ) ) && str.charAt( 0 ) != localeMinusSign ) return false;

	    boolean isDecimalSeparatorFound = false;
	    char localeDecimalSeparator = currentLocaleSymbols.getDecimalSeparator();

	    for ( char c : str.substring( 1 ).toCharArray() )
	    {
	        if ( !Character.isDigit( c ) )
	        {
	            if ( c == localeDecimalSeparator && !isDecimalSeparatorFound )
	            {
	                isDecimalSeparatorFound = true;
	                continue;
	            }
	            return false;
	        }
	    }
	    return true;
	}
    
    
	/**
	 * This method is responsible for get the system date based on the given date format.
	 * @param sFormat
	 * @param NoOfdays
	 * @return
	 * @throws ParseException
	 */
	
	public String GetFutureReqDateFormat(String sFormat,int NoOfdays) throws ParseException {
		
		sFormat=sFormat.replace("mm", "MM"); 
        DateFormat dateFormat = new SimpleDateFormat(sFormat);
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, NoOfdays);    
       // System.out.println("date--> "+cal.getTime());        
        String output = dateFormat.format(cal.getTime());
        System.out.println("datees-->"+dateFormat.format(dateFormat.parse(output)));
        String sFormatedDate=dateFormat.format(dateFormat.parse(output));          
        return sFormatedDate;
    }

	
    
	/**
	 * @author Shubha
	 * This method is used to insert text and to hit TAB or ENTER through keyboard action
	 * @param
	 * @return
	 * @throws Exception
	 */
	
	public boolean gtnInsertText (WebElement element,String elementName,String strText,String KeyboardAction) throws Exception{
		 	
	if(gtnGetElement(element)){		      
		  element.clear();
		  element.sendKeys(strText);
		  Thread.sleep(4000);
		  switch(KeyboardAction.toLowerCase()){
		  		case "tab" :
		  			element.sendKeys(Keys.TAB);
		  			Thread.sleep(2000);
		  			break;
		  			
		  		case "enter" :
		  			element.sendKeys(Keys.ENTER);
		  			Thread.sleep(2000);
		  			break;
		  }
		  Framework_Utils.gtnPassLog(strText + " -Inserted text successfully into Edit element << "+elementName+" >>");
		  return Boolean.TRUE;
		//}
	}else{
		Framework_Utils.gtnFailLog(strText + " -Failed to Insert text into Edit element << "+ elementName +" >>");
		return Boolean.FALSE;		  
     }
}
	

	/** 
	 * This method is responsible to execute the given query and return the records specific to column.
	 * @author Shubha	 	
	 * @since 24th February 2016
	 * @param Query					:	String variable containing Sql Query which will be executed in the DB. 
	 * @param ColName				:	String variable containing Column Name which needs to be fetched by executing sql query in the DB.
  	 * @return 
	 * @return						:	Returns Boolean value signifying whether query execution is successful or not
	 * @throws SQLException
	 */			
	
 public static String gtnFetchDBValue(String Query,String ColumnName,int maxtime)throws Exception {
		
		Connection con = null;	
		
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
						
			if (baseURL.contains("supportp")) {
				con = java.sql.DriverManager.getConnection(PropertiesHolder.properties.get("config").getProperty("SupportpDB"));				
			}			
			else if (baseURL.contains("rctp")) {
				con = java.sql.DriverManager.getConnection(PropertiesHolder.properties.get("config").getProperty("RCTpDB"));
			}
			else if (baseURL.contains("fte")) {
				con=java.sql.DriverManager.getConnection(PropertiesHolder.properties.get("config").getProperty("QASharedDB"));
			}
			System.out.println(Query);
			
			Statement statement = null;
			ResultSet rs = null;
			boolean status = false;
			int i = 0;
			Thread.sleep(2000);
			String val = null;

			while (status == false && i < maxtime) {
				if (con != null) {
					statement = con.createStatement();
					rs = statement.executeQuery(Query);					
				
					while (rs.next()) {
							val = rs.getString(ColumnName);
							//System.out.println(val);
							status=true;
					}
					rs.close();
					Thread.sleep(2000);
					i+=1;
					}				  						
			 }
			return val;
			
		} catch (Exception e) {
			Framework_Utils.gtnFailLog("Can not establish DB connection for the envt " + baseURL);
			e.printStackTrace();
			return null;
		}
		finally {
			con.close();
		}
	}		
		

 /** This method is responsible to return the column header values from the grid (including hidden columns in the grid).
	 * @author Shubha
	 * @param gridid
	 * @return column header values list.
	 * @throws Exception 
	 */
 
  //public String gtnGetGwtGridColumnHeaderValues(String gridId) throws Exception {

  public List<String> gtnGetGwtGridColumnHeaderValues(String gridId) throws Exception {
	  
	// String col_values=null;
	 
	 List<String> col_values = new ArrayList<String>();
	 
	 try {		 
		 webDriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		 
		// List<WebElement> col_headers=webDriver.findElements(By.xpath(GridXPath)); 
		 
		 List<WebElement> col_headers=webDriver.findElements(By.xpath("//div[@id='"+gridId+"']//tr[@class='x-grid3-hd-row']//td/div/span"));
		 
		 for (WebElement option : col_headers) {
			  String retval=option.getAttribute("textContent").trim();
			  col_values.add(retval);
			 // col_values=col_values+";"+retval;	
			 }		
		   return col_values;				 
		 
	} catch (Exception e) {
		return null;
	}		
	   	
}	
   
/**
 * This method is responsible to get the column number from the gwt grid based on the column name(including hidden columns in the grid).
 * @author Shubha
 * @param driver
 * @param Value
 * @return integer value.
 */
	public int gtnGetGridColNumByName(String gridId,String colName){
		int counter = 1;
		boolean flag = false;
	  try{
		List<WebElement> headers = webDriver.findElements(By.xpath("//div[@id='" + gridId + "']//div[@role='columnheader']//span"));
		for (WebElement option : headers) {
			if(colName.equalsIgnoreCase(option.getAttribute("textContent").trim())){
				//System.out.println("Column Header values are "+option);
				flag=true;
		       break;
			}else{
			   flag=false; 	
			}
			counter++;
		 }
		 if(flag==true){
			 return counter;
		 }else{
			 Framework_Utils.gtnFailLog("Col Name did not match in the grid");
			 return 0;
		 }
		}catch(Exception e){
			//e.printStackTrace();
			return 0;
		}
	} 
  
  
	/**
	 * This method is responsible to get all the values from specific column of the gwt grid.
	 * @author Shubha
	 * @return String list value.
	 * @throws Exception 
	 */	
	
	public List<String> gtnGwtGetGridTableColumnValues(String gridId,String colName) throws Exception{
		
		int col = 0;
		int rowCount = 0;		
		
		List<String> val = new ArrayList<String>();
		
		Thread.sleep(2000);
		gtnWaitForObjectByXpath(By.xpath("//div[@id='" + gridId + "']"));
		webElement = gtnGetObjectByXpath("//div[@id='"+ gridId + "']");
		
		if(webElement!=null){
			if (Framework_Utils.isInteger(colName)==true){
			 Integer colNum = new Integer(colName);
			 col = colNum;
			}else{
				int colNum = gtnGetGridColNumByName(gridId,colName);
				col = colNum;
			}
			if(col>=1){
				
				List<WebElement> GrdTbl=webDriver.findElements(By.xpath("//div[starts-with(@id,'"+gridId+"_row_')]"));
				rowCount = GrdTbl.size();
				
				for(int i=0;i<=rowCount-1;i++){
					WebElement GrdRow = webDriver.findElement(By.xpath("//div[@id='"+gridId+"_row_"+ i + "']//td["+ col +"]//div"));
					String rowVal=GrdRow.getAttribute("textContent").trim();
					val.add(rowVal);
				}			
				
				if (val!= null && !val.isEmpty()) {
				  //  Framework_Utils.gtnInfoLog("Stored the values to a list successfully");
				}else{
					Framework_Utils.gtnInfoLog("The Element not found");
				}
			}
		}else{
			Framework_Utils.gtnFailLog("The div element not found");
		}
		return val;
	}	
	
	 
	
/**
 * This method is responsible to check whether input is numeric or not.
 * @author Shubha
 * @return
 * @throws Exception 
 */
 
	public static boolean isNumeric(String str)
	  {
	    try
	    {
	      double d = Double.parseDouble(str);
	    }
	    catch(NumberFormatException nfe)
	    {
	      return false;
	    }
	    return true;
	 }		
	
	
/**
 * This method is repsonsible to return the two decimal values.
 * @author ckumar
 * @param val
 * @
 * @return two decimal values.
 */
	
  public static String getDeciVal(String val){
		
		String value =null;
		
		try {			   
			   
		   NumberFormat nf = NumberFormat.getNumberInstance(Locale.US); 
		   nf.setMinimumFractionDigits(2); 
		   nf.setMaximumFractionDigits(2); 
		   nf.setGroupingUsed(false); 
		   if (!val.isEmpty()) {
			   value = nf.format(Double.parseDouble(val));
		   }			    
			
		} catch (Exception e) {
			return "0";			
		}return value;
	}
  
  
      
         
  
/**
 * This method is repsonsible to return the two decimal values.
 * @author 
 * @param val
 * @
 * @return two decimal values.
 */ 
	public void checkPageIsReady() throws Exception { 
		  
			  JavascriptExecutor js = (JavascriptExecutor)webDriver;
		      if (js.executeScript("return document.readyState").toString().equals("complete")){ 	    	  
		          return; 
		       }	     
		      for (int i=0; i<500; i++){
	                try {
	                Thread.sleep(1000);
	                }catch (InterruptedException e) {}
	                //To check page ready state. 
		                if (js.executeScript("return document.readyState").toString().equals("complete")){
		                 break;
		                }
		      	}
		     // System.out.println("Stop");
	 }
	  
	  
  


/**
* @author ckumar2
*  This method is responsible to two compare the two dimension array values.
* @param data
* @return
*/  

public static boolean CheckLeastPriceAlloc(String data[][]){
	  
	boolean retVal=true;
	  	  	  	  
    Arrays.sort(data, new Comparator<String[]>() {
        @Override
        public int compare(final String[] entry1, final String[] entry2) {
            final String time1 = entry1[0];
            final String time2 = entry2[0];
            return time1.compareTo(time2);
            //return time2.compareTo(time1);
        }
    });

      
    int r=data.length;
    int c=data[0].length;
//----------------------------------------------------------        
    System.out.println("The Sorted Array:");
    for(int i=0;i<=r-1;i++)
    {           
         System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
      for(int j=i+1;j<=r-1;j++)
        {
           double pricetemp1=Double.parseDouble(data[i][0]);
           double pricetemp2=Double.parseDouble(data[j][0]);
                           
           Framework_Utils.gtnInfoLog("################################");
           Framework_Utils.gtnPassLog("price1 -->"+pricetemp1);
           Framework_Utils.gtnPassLog("price2 -->"+pricetemp2);
           Framework_Utils.gtnInfoLog("################################");
           
           if (pricetemp1<=pricetemp2){
          	 
	            double alloctemp1=Double.parseDouble(data[i][1]);
	            double alloctemp2=Double.parseDouble(data[j][1]);
	            	                
	            Framework_Utils.gtnInfoLog("################################");
	            Framework_Utils.gtnPassLog("allocation1 -->"+alloctemp1); 
            	Framework_Utils.gtnPassLog("allocation2 -->"+alloctemp2);
            	Framework_Utils.gtnInfoLog("################################");
            	
	             if (alloctemp1<alloctemp2){
                     	 
		            if (pricetemp1==pricetemp2) {
		            	Framework_Utils.gtnInfoLog("Price is identical.");		        
					}else{
						Framework_Utils.gtnInfoLog("################################"); 
		            	Framework_Utils.gtnFailLog("allocation1 -->"+alloctemp1); 
		            	Framework_Utils.gtnFailLog("allocation2 -->"+alloctemp2);
		            	Framework_Utils.gtnInfoLog("################################");
	                    retVal=false;
					}
	            	
	             }	           
	            
                  
           }           
        }
      //  System.out.println();
    }
	  
	return retVal;
	  
}


/**
 * This method is responsible to page down to respective element to perform the required action.
 * @param driver
 * @param element
 */
  

public static void gtnscrollTo(WebDriver driver, WebElement element) {
    ((JavascriptExecutor) driver).executeScript(
            "arguments[0].scrollIntoView();", element);
}	
	


/**
 * 	This method is responsible to check the duplicate items in an array.
 * @param inArray
 * @return
 */
		
 public static double hasDuplicatesInRows(String[][] inArray)
	 {
		int count=0,row1=0;
		double retVal=0;
		
		try {
			
			 for (int row = 0; row < inArray.length; row++)
			    {
				    String num = inArray[row][0];
				    	
				        for (row1 = 0; row1 < inArray.length; row1++){
				        	
				          String num1 = inArray[row1][0];
				            
			                 if (num1!=null) {				    
						               		                     		
		                     	if (row!=row1){
					                if (num.equalsIgnoreCase(num1)){
					                	System.out.println("inArray[row][otherCol]-->"+num1);
					                	count=count+1;			                    
					                }//if			
		                     	 }//if
					           } //if
				         }//for
			        
				        if (count>=1){
				        	System.out.println("Duplicate found and its value is "+num);
				        	//System.out.println("duplicate count1 "+inArray[row][1]);
				        	retVal=Double.parseDouble(inArray[row][1]);	        	
				        	//System.out.println("duplicate count2"+inArray[row1][1]);	        	
				        	break;
				        }
		        
			    }//for
			 			 
		} catch (Exception e) {
			return retVal;
		}
		
		 return retVal;	    
	}//method
		
	
	
 /**
  * @author Shubha.H
  * gtnCheckLinkPresence - This method is used to check link existence in a page
  * @return : Returns boolean value signifying whether link exists or not in the page
  * @param LinkName : Name of the link 
  * @throws : 
  * @since 26-September-2016
  */

 public boolean gtnCheckLinkPresence(String LinkName){
 	boolean LinkFound = false;
 	try{
 		
 		List<WebElement> arrLinks = webDriver.findElements(By.tagName("a"));
 		for(WebElement element : arrLinks){
 			String linkNam = element.getText();
 			if(linkNam.contains(LinkName)){
 				LinkFound = true;
 				break;
 			}
 		}			
 	}catch(Exception e){
 		LinkFound = false;
 	}
 	return LinkFound;	
 } 	
 	
 
 
 /**
  * @author Shubha.H
  * gtnCheckLinkPresence - This method is used to check link existence in a page
  * @return : Returns boolean value signifying whether link exists or not in the page
  * @param LinkName : Name of the link 
  * @throws : 
  * @since 26-September-2016
  */

 public int gtnCheckLinkPresenceCount(String LinkName){
	 int LinkFoundCount=0;
 	try{
 		
 		List<WebElement> arrLinks = webDriver.findElements(By.tagName("a"));
 		for(WebElement element : arrLinks){
 			String linkNam = element.getText();
 			if(linkNam.contains(LinkName)){
 				LinkFoundCount=LinkFoundCount+1;
 				//LinkFound = true;
 				//break;
 			}
 		}			
 	}catch(Exception e){
 		LinkFoundCount=0;
 	}
 	return LinkFoundCount;	
 } 	
 
 
 
 
 
 
 /**
  * @author Shubha.H
  * ClickAlert - To handle alert 
  * @return : Returns boolean value signifying whether button on alert box clicked or not
  * @param timeout : time to wait for the alert box to pop up 
  * @throws : 
  * @since 26-September-2016
  */
 		
 	public void acceptAlertIfAvailable(long timeout)
 	{
 		  long waitForAlert= System.currentTimeMillis() + timeout;
 		  boolean boolFound = false;
 		  do
 		  {
 		    try
 		    {
 		      Alert alert = webDriver.switchTo().alert();
 		      if (alert != null)
 		      {
 		        alert.accept();
 		        boolFound = true;
 		      }
 		    }
 		    catch (NoAlertPresentException ex) {}
 		  } while ((System.currentTimeMillis() < waitForAlert) && (!boolFound));
 	}
 
 

/**
 *  This method is responsible to get the date format based on the user.
 * @param param
 * @param errorList
 * @param tcid
 * @param keyword
 * @return
 * @throws Exception
 */
		
  public String gtnGetDateFormatfrmDB(String userid){																		
			
			String formatVal=null,loguserid=null;
						
			try {
				
				int maxtime=60;	
				List<String> useridVal=null;
				List<String> dateformatVal=null;					
				
				Framework_Utils.gtnInfoLog("Org User Name is -->"+userid);
								
				String Query="select user_id from tt_user where login like '"+userid+"'";				
				useridVal=gtnExecuteDBQuery(Query, maxtime);
				
				if (useridVal!=null && !useridVal.isEmpty()) {
					loguserid=useridVal.get(0).trim();
					System.out.println("loguserid-->"+loguserid);	
					Framework_Utils.gtnInfoLog("User ID value is -->"+loguserid);
					
					if (isStringNumeric(loguserid)) {
						String Query2="select strval from tt_userpref where user_id= '"+loguserid+"' and preftype= 1  order by preftype asc";
						dateformatVal=gtnExecuteDBQuery(Query2, maxtime);
						
						if (dateformatVal!=null && !dateformatVal.isEmpty()) {
							formatVal=dateformatVal.get(0).trim();
						}
					}
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Framework_Utils.gtnInfoLog("Exception encounterd.");
				formatVal="mm/dd/yyyy";
			}					
			
			return formatVal;
		
	}	
	
 /**
  * This method is responsible to return the two decimal value from the given decimal number.
  * @param acttotPriceVal
  * @return
  */
  
  public static String gtngetTwoDeciVal(String acttotPriceVal){
	  
	 String retval=null;
	  
	  try {
		  DecimalFormat  f = new DecimalFormat("##.00");
		  retval=f.format(acttotPriceVal);		  
	} 
	  catch (Exception e) {		  
		return retval;
	}	  
	  return retval;	
	
  }
 
  /** 
   * @author Chethan Kumar
   * This method is responsible to check the given string is decimal or not.
   * @param val
   * @return true or false
   */
 	 	
  public static boolean gtntoCheckStringIsDecimal(String val){
	  boolean retval=false;
	  try {
		  
			  retval=val.matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+");
			
		} catch (Exception e) {
			return retval;
		}
		  return retval;
  	}
 

  /**
   * This method is responsible to check the given string is special character or not.
   * @param s
   * @return
   */
        
     
  public static boolean gtnToCheckSpecialCharacter(String s) {
     	
     	boolean retVal=false;
     	
     try {
     	
     	if (s == null || s.trim().isEmpty()) {
             Framework_Utils.gtnInfoLog("Incorrect format of string");
             retVal=false;
         }
         Pattern p = Pattern.compile("[^A-Za-z0-9]");
         Matcher m = p.matcher(s);       
         boolean b = m.find();
         if (b == true){
         	 retVal=true;
         	 Framework_Utils.gtnInfoLog("There is a special character in my string "); }
         else{
         	 Framework_Utils.gtnInfoLog("There is no special char.");
         	 retVal=false;
           }        
 		
	 	} catch (Exception e) {
	 		 Framework_Utils.gtnInfoLog("Exception is encountered.");
	 		 retVal=false;
	 	}	    	
          return retVal;
     }  
  
  
  
  
 /**
  * This method is responsible to wait until the element is displayed. 
  * @param by
  * @param waitInMilliSeconds
  * @return
  * @throws Exception
  */
  
	  
 public boolean gtnwaitForElementToBePresent(WebElement ele, int waitInMilliSeconds) throws Exception
	
	{
	
	    int wait = waitInMilliSeconds;
	    int iterations  = (wait/250);
	    long startmilliSec = System.currentTimeMillis();
	    for (int i = 0; i < iterations; i++)
	    {
	        if((System.currentTimeMillis()-startmilliSec)>wait)
	            return false;
	        boolean eleRetVal=ele.isDisplayed();
	        if (eleRetVal)
	            return true;
	        Thread.sleep(250);
	    }
	    return false;
	}
  
  

/**
 * This method is responsible to close the secondary window and switch the control back to primary window.
 * @param ele
 * @param waitInMilliSeconds
 * @throws Exception
 */

public void gtnCloseSecondaryWindow() throws Exception

{
	   try {
		
		    Set<String> windows = webDriver.getWindowHandles();
			System.out.println("Window size-->"+windows.size());
			Iterator<String> itr = windows.iterator();
	
			//get the parent window name
			String parntWindName = itr.next();
									 
			// get the child window name
			String chldWindName = itr.next();
			
			//Switch to child window
			webDriver.switchTo().window(chldWindName);
	
			webDriver.close();
			Thread.sleep(5000);
	
			//Switch back to parent window
			webDriver.switchTo().window(parntWindName);
								 
			System.out.println("Close the secondary window.");
			Framework_Utils.gtnInfoLog("Close the secondary window.");
			
	} catch (Exception e) {
		Framework_Utils.gtnInfoLog("Exception encountered."+e.getMessage());
	}
	
  
}
  

  
  
  
  
  

  
 
 
	
}// class ends here.



