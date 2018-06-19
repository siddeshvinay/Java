
/**
 * @author chethan kumar
 * This class is used drive and control the execution through different browsers defined in TestNG xml
 */
package com.gtnexus.selenium.core.driver;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.gtnexus.selenium.core.model.PropertiesHolder;
import com.gtnexus.selenium.core.utils.GlobalVariables;


public class DriverFactory {
	
	protected WebDriver webDriver = null;
	public WebDriver getDriver(String browser, String server, String port) throws Exception {
	
	  String fileName=System.getProperty("user.dir")+File.separator+"src"+"//log4j.properties";	
	  fileName=fileName.replace("\\", "//");
	  
	//  System.out.println("fileName-->"+fileName);	  
	  PropertyConfigurator.configure(fileName);
	  			
	  //PropertyConfigurator.configure("/log4j.properties");
			
		if(webDriver == null) {
			
			if (server.equals("NA")) 
			{
				DesiredCapabilities capability = new DesiredCapabilities();
				capability.setBrowserName(browser);
				
				if (browser.equalsIgnoreCase(GlobalVariables.FIREFOX_BROWSER)){																		
					/*FirefoxProfile fxProfile = new FirefoxProfile();					
					fxProfile.setPreference("browser.download.folderList",2);                                           
                    fxProfile.setPreference("browser.download.manager.showWhenStarting",false);
                    fxProfile.setPreference("browser.download.dir",System.getProperty("user.dir").concat("\\resources\\datafiles\\temp"));
                    fxProfile.setPreference("browser.helperApps.neverAsk.saveToDisk","image/jpg,text/csv,text/xml,application/xml,application/vnd.ms-excel,application/x-excel,application/x-msexcel,application/excel,application/pdf");
                    /webDriver = new FirefoxDriver(fxProfile); */ 
                    										
					String geckpath=System.getProperty("user.dir")+PropertiesHolder.properties.get("config").getProperty("EXECUTABLES_PATH") + PropertiesHolder.properties.get("config").getProperty("GECKO_DRIVER");
                	System.out.println("Gecko Path-->"+geckpath);
                    System.setProperty("webdriver.gecko.driver",geckpath);		
                    //System.setProperty("webDriver.firefox.profile","default");
                    
                    FirefoxProfile fxProfile = new FirefoxProfile();	
                    DesiredCapabilities descap = DesiredCapabilities.firefox();
                    fxProfile.setPreference("browser.download.folderList",2);                                           
                    fxProfile.setPreference("browser.download.manager.showWhenStarting",false);
                    fxProfile.setPreference("browser.download.dir",System.getProperty("user.dir").concat("\\resources\\datafiles\\temp"));
                    fxProfile.setPreference("browser.helperApps.neverAsk.saveToDisk","image/jpg,text/csv,text/xml,application/xml,application/vnd.ms-excel,application/x-excel,application/x-msexcel,application/excel,application/pdf");
                    descap.setCapability(FirefoxDriver.PROFILE, fxProfile);
                    descap.setCapability("marionette", true);
                    webDriver =  new FirefoxDriver(descap);                    
                    
                	//webDriver = new FirefoxDriver();                     	
              				                    					
				}
				else if (browser.equalsIgnoreCase(GlobalVariables.IE_BROWSER)){
					File file = new File(System.getProperty("user.dir")+ PropertiesHolder.properties.get("config").getProperty("EXECUTABLES_PATH") + PropertiesHolder.properties.get("config").getProperty("IE_DRIVER"));						
					System.setProperty("webdriver.ie.driver", file.getAbsolutePath());
					//DesiredCapabilities capabilities = new DesiredCapabilities();
					//capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);     
					webDriver = new InternetExplorerDriver();	
				}
				else if (browser.equalsIgnoreCase("*chrome")){
					File file = new File(System.getProperty("user.dir")+ PropertiesHolder.properties.get("config").getProperty("EXECUTABLES_PATH") + PropertiesHolder.properties.get("config").getProperty("CHROME_DRIVER"));						
					System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());
                    ChromeOptions options = new ChromeOptions();
                    Map<String, Object> prefs = new HashMap<String, Object>();
                    prefs.put("download.default_directory",System.getProperty("user.dir").concat("\\resources\\datafiles\\temp"));
                    options.setExperimentalOption("prefs", prefs);        
                    DesiredCapabilities capabilities = DesiredCapabilities.chrome();
                    capabilities.setCapability("chrome.prefs", prefs);
                    webDriver = new ChromeDriver(options);

				}
				else {
					HtmlUnitDriver driver = new HtmlUnitDriver();
					driver.setJavascriptEnabled(true);
					webDriver=driver;
				}
			}
			else 
			{
				DesiredCapabilities descap = DesiredCapabilities.firefox();
				descap.setBrowserName("firefox");
//				descap.setPlatform(Platform.WIN10);

				String geckpath=System.getProperty("user.dir")+PropertiesHolder.properties.get("config").getProperty("EXECUTABLES_PATH") + PropertiesHolder.properties.get("config").getProperty("GECKO_DRIVER");
				//System.out.println("Gecko Path-->"+geckpath);
				System.setProperty("webdriver.gecko.driver",geckpath);		
				//System.setProperty("webDriver.firefox.profile","default");

				FirefoxProfile fxProfile = new FirefoxProfile();		                 
				fxProfile.setPreference("browser.download.folderList",2);                                           
				fxProfile.setPreference("browser.download.manager.showWhenStarting",false);
				fxProfile.setPreference("browser.download.dir",System.getProperty("user.dir").concat("\\resources\\datafiles\\temp"));
				fxProfile.setPreference("browser.helperApps.neverAsk.saveToDisk","image/jpg,text/csv,text/xml,application/xml,application/vnd.ms-excel,application/x-excel,application/x-msexcel,application/excel,application/pdf");
				descap.setCapability(FirefoxDriver.PROFILE, fxProfile);
				descap.setCapability("marionette", true);

				String serverURL = "http://" + server + ":".concat(port).concat("/wd/hub");
				System.out.println("serverURL-->"+serverURL);
				webDriver = new RemoteWebDriver(new URL(serverURL),	descap); 
			}
		}
		return webDriver;
	}
}
