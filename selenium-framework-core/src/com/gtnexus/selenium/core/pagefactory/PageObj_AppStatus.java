/**
 * @author chethan kumar
 * This class is used as a page factory to store objects in an application status check jsp page and
 * method to validate the application status
 */

package com.gtnexus.selenium.core.pagefactory;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.FindBy;
import org.testng.Reporter;

import com.gtnexus.selenium.core.model.PropertiesHolder;
import com.gtnexus.selenium.core.utils.GlobalVariables;


public class PageObj_AppStatus {
		
	@FindBy(xpath="//table/tbody/tr[3]/td[1]")
	private WebElement appServer;
	
	@FindBy(xpath="//table/tbody/tr[3]/td[2]")
	private WebElement appServerName;
	
	@FindBy(xpath="//table/tbody/tr[4]/td/b")
	private WebElement appServerStatus;
	
	
	public WebElement AppServer(){
		return appServer;
	}
	
	public WebElement AppServerName(){
		return appServerName;
	}
	
	public WebElement AppServerStatus(){
		return appServerStatus;
	}
	
	/**
	 * @author Chethan Kumar
	 */
	public boolean kwdAppStatusCheck(String url){
		String appServerName;
		String appServerStatus;
		WebDriver appStatDriver = null;
		
		boolean status = false;
		try{
				
				String url1=url.replace("/login.jsp", "/appstatus.jsp");						
				String appStatusURL =url1;						
				HtmlUnitDriver driver = new HtmlUnitDriver();
				driver.setJavascriptEnabled(true);
				appStatDriver=driver;											
		
				appStatDriver.manage().window().maximize();
				appStatDriver.get(appStatusURL);
									
			
			 if("App Status".equalsIgnoreCase(appStatDriver.getTitle())){
				 appServerName = appStatDriver.findElement(By.xpath("//table/tbody/tr[3]/td[2]")).getText();
				 GlobalVariables.appServerName = appServerName;
				 
				 Reporter.log(" -- > Application Server Name : \t" + appServerName + "\n");
				// System.out.println(" -- > Application Server Name : \t" + appServerName);
				 				 
				 appServerStatus = appStatDriver.findElement(By.xpath("//table/tbody/tr[4]/td")).getText();
				 int counter = 0;
				 while(!appServerStatus.endsWith("Up")){
					 Thread.sleep(60000);
					 appServerStatus = appStatDriver.findElement(By.xpath("//table/tbody/tr[4]/td")).getText();
					 if(counter >= 60){
						break;
					 }
					 counter++;
				 }
				 
				 if(appServerStatus.endsWith("Up")){
					 GlobalVariables.appServerStatus = "Up";
					 status = true;
					 Reporter.log(" -- > Application Server Status : \t Up \n");
					// System.out.println(" -- > Application Server Status : \tUp");
					// System.out.println("Application Server Status is Up for automated execution");
					// System.out.println();
					 appStatDriver.quit();
				 }else{
					 GlobalVariables.appServerStatus = "Down";
					 Reporter.log(" -- > Application Server Status : \t Down \n");
					 System.out.println(" -- > Application Server Status : \tDown");
					 System.out.println("Application Server Status is Down so cannot execution automated scripts");
					 System.out.println();
					 appStatDriver.quit();
				 }
			 }
		
		}catch(Exception e){
			
		}
		return status;
	}
	
	
	/**
	 * This method is responsible to get the build number from the application.
	 * @author Chethan kumar
	 * @param url
	 * @return
	 */
	public boolean kwdGetBuildNumber(String url){
		int counter = 0;
        boolean appstat =  true;
       	
		try{
							
				 String appStatusURL=url;
			
				 //HtmlUnitDriver appStatDriver1 = new HtmlUnitDriver();
				 
				 //String nhuser="User1-automation_script_ execution";
				// String nhpwd="GTNautouser11";
				 
				 String nhuser=PropertiesHolder.properties.get("config").getProperty("NHUser").trim();
				 String nhpwd=PropertiesHolder.properties.get("config").getProperty("NHPwd").trim();						//"bfqFDIZWkv6st@";
					 				 
	             WebDriver driver = new HtmlUnitDriver();							
	             driver.manage().window().fullscreen();
	             driver.navigate().to(appStatusURL);
	             // driver.get(appStatusURL);
	             
	             
	             driver.manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS);
	            // System.out.println(driver.getTitle());
	             driver.findElement(By.xpath("//input[@name='login' or @name='userid']")).sendKeys(nhuser);
			     driver.findElement(By.xpath("//input[@id='password' or @name='password']")).sendKeys(nhpwd);			
			     driver.findElement(By.xpath("//input[@alt='Login' or @value='Login']")).click(); 
			     driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);   		
			     			    			  			     
			 	 String URL1=appStatusURL.replace("/login.jsp", "");
			 	 driver.navigate().to(URL1 + "/desk/welcome.jsp");
				 Thread.sleep(20000);
				 
				 WebElement builele=driver.findElement(By.xpath("//span[contains(text(),'Dev Header')]"));
				 System.out.println("Login successfully during the build capture.");
				 
				 //if(appstat==true){
			     if(builele.isDisplayed()){
					 String[] bNum=driver.findElement(By.xpath("//span[contains(text(),'Dev Header')]")).getText().split(":");
					 GlobalVariables.buildNumber=bNum[1].trim();					 
					 System.out.println("Build Number : "+bNum[1].trim());					 
					 System.out.println("Release Number : "+GlobalVariables.release);
					 
				 }else{
					 Reporter.log("Login was not successfull and could not capture the build number");
					 System.out.println("Login was not successfull and could not capture the build number");
					 GlobalVariables.buildNumber=null;
					 GlobalVariables.release=null;
				 }
			     driver.quit(); 	 				
		}catch(Exception e){
			 Reporter.log("could not capture the build number");
			 System.out.println("could not capture the build number");			 
			 GlobalVariables.buildNumber=null;
			 GlobalVariables.release=null;		
		}
		return true;
	}
	
	
}
