package com.gtnexus.selenium.core.driver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

import com.gtnexus.selenium.core.model.PropertiesHolder;
import com.gtnexus.selenium.core.pagefactory.PageObj_AppStatus;
import com.gtnexus.selenium.core.utils.Framework_Utils;
import com.gtnexus.selenium.core.utils.GlobalVariables;
import com.gtnexus.selenium.core.utils.VideoRecorder;
import com.thoughtworks.selenium.Selenium;

public class StartDriver {

	protected WebDriver webDriver;
	protected Selenium selenium;
	protected long timeout = 90;
	protected WebElement webElement;
	protected boolean isFirefox = false;
	public String baseURL;
	protected String BrowserType;
	protected String product;	
	protected String keywordsDataFile;	
	protected String configFile;
	
	public File DF=null;
	Date start, end;
		
	VideoRecorder  videoRecord=null;
		
	
	@BeforeClass
	@Parameters({ "browser","server","port","URL","product","configFile","keywordsDataFile","releaseNumber","UpdateDB","SendEmail","CopyReports","ScreenCapture"})
	

			
	public void instantiateWebDriver(String browser,String server,String port,String url,String product,String configFile,String keywordsDataFile, String releaseNumber, boolean UpdateDB,boolean SendEmail,boolean CopyReports,boolean ScreenCapture) throws Exception{
		this.product = product;
		GlobalVariables.PRODUCT = product;
		ClearFiles();
							
		try {
			Properties config = new Properties();
			config.load(new FileInputStream(System.getProperty("user.dir")+ configFile));						
			PropertiesHolder.properties.put("config", config);			
			this.keywordsDataFile = System.getProperty("user.dir") + keywordsDataFile;
			
			GlobalVariables.KEYWORKFILEPATH = System.getProperty("user.dir") + keywordsDataFile;	
												
			GlobalVariables.release=releaseNumber;	
			GlobalVariables.UpdateDB=UpdateDB;
			GlobalVariables.SendEmail=SendEmail;
			GlobalVariables.CopyReports=CopyReports;
			GlobalVariables.ScreenCapture=ScreenCapture;
						
			System.out.println("Starting the test case script execution.....");
						
			Date st = new Date();
			start = st;			
												
			//if (PropertiesHolder.properties.get("config").getProperty("SCREEN_CAPTURE").trim().equalsIgnoreCase("true")) {
			if (GlobalVariables.ScreenCapture){	
			  videoRecord = new VideoRecorder();	
			  videoRecord.startRecording();			
			}	
												
			PageObj_AppStatus objAppStatPage=PageFactory.initElements(webDriver, PageObj_AppStatus.class);
			boolean appstatus =objAppStatPage.kwdAppStatusCheck(url);
			System.out.println("Check the Application appstatus --> "+appstatus);
						
			if(appstatus){
				System.out.println("Current Application URL is --> "+url);
				//objAppStatPage.kwdGetBuildNumber(url);
		
				webDriver = new DriverFactory().getDriver(browser, server, port);
				selenium = new com.thoughtworks.selenium.webdriven.WebDriverBackedSelenium(webDriver, url);
				webDriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
				EventFiringWebDriver EFWebDriver = new EventFiringWebDriver(webDriver);	
				webDriver.get(url);
				baseURL = url;
				BrowserType = browser;				
				gtnGetBrowserDetails();
															
				String[] envValue1=baseURL.split("//");
				String envValue2=envValue1[1].trim();
				String [] envValue3=envValue2.split("/");
				String envValue4=envValue3[0].replace("qa.gtnexus.com", "").trim().replace(".", "");
				System.out.println("Environment Name-->"+envValue4);								
				
				if (baseURL.contains("https://platform-rctp.qa.gtnexus.com/login.jsp") || baseURL.contains("https://network-rctp.qa.gtnexus.com/login.jsp")) {
					GlobalVariables.environment=envValue4.trim();
				}else if (baseURL.contains("https://commerce-alphaq.qa.gtnexus.com/login.jsp")) {
					GlobalVariables.environment=envValue4.trim();
				}else if (baseURL.contains("https://platform-supportp.qa.gtnexus.com/login.jsp") || baseURL.contains("https://network-supportp.qa.gtnexus.com/login.jsp")) {
					GlobalVariables.environment=envValue4.trim();
				} 
				else {
					GlobalVariables.environment="RCTP";
				}
				
				GlobalVariables.exeMachineName = java.net.InetAddress.getLocalHost().getHostName();
					
				DateFormat dateFormat = new SimpleDateFormat("MM_dd_yyyy_hh_mm");
				Date date = new Date();
				//maximiseWindow();
				
			}else{
				System.out.println();
				System.out.println("Application is down. Hence, Stopping the test case script execution.....");
				System.out.println();
				System.out.println();
			}
			
		} catch (Exception e) {
			Framework_Utils.gtnFailLog("Couldnot run due to: " + e.getMessage());
		}
		Framework_Utils.gtnCreateLogHeader();
	}
	

	@AfterClass(alwaysRun = true)
	public void stopWebDriver() throws IOException {
		System.out.println();
		System.out.println("Stopping the test case script execution.....");
		System.out.println();
		System.out.println();
		webDriver.manage().deleteAllCookies();
		//webDriver.close();
		webDriver.quit();
		
		// add the new code to Stop the Video recorder.		
		//videoRecord!=null && 
			
		//if(PropertiesHolder.properties.get("config").getProperty("SCREEN_CAPTURE").trim().equalsIgnoreCase("true")){
		
		if (GlobalVariables.ScreenCapture){	
			try {			
				videoRecord.stopRecording();
				Framework_Utils.gtnInfoLog("Screen Capture set to true, Movie clip is placed in the folder-->."+PropertiesHolder.properties.get("tmsprops").getProperty("FileName"));
			} catch (Exception e) {				
				e.printStackTrace();
			}
		}else{
			Framework_Utils.gtnInfoLog("Screen capture is set to false, Movie clip is not generated.");
		}
			
		//kill ie process
		Framework_Utils.gtnkillProcess(BrowserType);
		Date et = new Date();
		end = et;
        long diff = end.getTime() - start.getTime();
        long diffSeconds = diff / 1000 % 60;  
        long diffMinutes = diff / (60 * 1000) % 60; 
        long diffHours = diff / (60 * 60 * 1000);                     
        String totalDiff= Long.toString(diffHours) +":"+Long.toString(diffMinutes) +":"+Long.toString(diffSeconds);
        Framework_Utils.gtnUpdateLog("*******************************************************************************");
        Framework_Utils.gtnUpdateLog("Total time taken for execution: " + Long.toString(diffHours)+"hrs:" + Long.toString(diffMinutes) + "mins:" +Long.toString(diffSeconds)+ "secs.");
        Framework_Utils.gtnUpdateLog("*******************************************************************************" + System.getProperty("line.separator"));
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

	public void maximiseWindow(){
		webDriver.manage().window().maximize();
	}
	
	public void gtnGetBrowserDetails() throws Exception{
		
		Thread.sleep(2000);	
		
		String browser=(String) ((JavascriptExecutor) webDriver).executeScript("return navigator.userAgent;");
		//System.out.println("Browser name : " + browser);
		//System.out.println(browser.split("/")[3]); 
		
		if(browser.contains("IE")){
			GlobalVariables.browserName = "IE";
			GlobalVariables.browserVersion = browser.split("MSIE")[1].split(";")[0];
		}
		if(browser.contains("Firefox")){
			GlobalVariables.browserName = "FIREFOX";
			int len = browser.split("/").length;
			String t=browser.split("/")[len-1];
			System.out.println("t"+t);
			GlobalVariables.browserVersion = browser.split("/")[len-1];
		}
		if(browser.contains("Chrome")){
			GlobalVariables.browserName = "CHROME";
			int len = browser.split("/").length;
			String drv = browser.split("/")[len-2];
			GlobalVariables.browserVersion = drv.split(" ")[0];
		}	
		
				
		
		/*String browser = selenium.getEval("navigator.userAgent");					//selenium.getEval("navigator.userAgent");
		//System.out.println(browser);
		if(browser.contains("IE")){
			GlobalVariables.browserName = "IE";
			GlobalVariables.browserVersion = browser.split("MSIE")[1].split(";")[0];
		}
		if(browser.contains("Firefox")){
			GlobalVariables.browserName = "FIREFOX";
			int len = browser.split("/").length;
			String t=browser.split("/")[len-1];
			System.out.println("t"+t);
			GlobalVariables.browserVersion = browser.split("/")[len-1];
		}
		if(browser.contains("Chrome")){
			GlobalVariables.browserName = "CHROME";
			int len = browser.split("/").length;
			String drv = browser.split("/")[len-2];
			GlobalVariables.browserVersion = drv.split(" ")[0];
		}		*/
	}
	
	public void ClearFiles(){
		try {
		Framework_Utils.gtnDeleteAllFilesFromFolder(System.getProperty("user.dir") + "//resources//datafiles//temp");
		} catch (Exception e) {
		System.out.println("Could not delete files from temp folder");
		}
		}

}
