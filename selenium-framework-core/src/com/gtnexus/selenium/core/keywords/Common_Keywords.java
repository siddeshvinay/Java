/**
 * @author chethan kumar
 * This class is used to invoke, login and/or logout application
 */

package com.gtnexus.selenium.core.keywords;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.gtnexus.selenium.core.model.PropertiesHolder;
import com.gtnexus.selenium.core.pagefactory.PageObj_AppStatus;
import com.gtnexus.selenium.core.pagefactory.PageObj_Common;
import com.gtnexus.selenium.core.pagefactory.PageObj_Login;
import com.gtnexus.selenium.core.pagefactory.PageObj_Logout;
import com.gtnexus.selenium.core.utils.Common_Utils;
import com.gtnexus.selenium.core.utils.Framework_Utils;
import com.gtnexus.selenium.core.utils.GlobalVariables;

public class Common_Keywords extends Common_Utils {
	private PageObj_Logout objLogoutPage = PageFactory.initElements(webDriver, PageObj_Logout.class);
	private PageObj_Login objLoginPage = PageFactory.initElements(webDriver, PageObj_Login.class);
	private PageObj_Common objCommonPage = PageFactory.initElements(webDriver, PageObj_Common.class);
	
	public Common_Keywords(WebDriver driver, String url) throws Exception {
		super(driver, url);
		// TODO Auto-generated constructor stub
	}
	
		/**
		 * @author chethan kumar
		 * @param param
		 * @return
		 * @throws Throwable
		 */
		public boolean kwdInvokeApp(List<String> param, List<String> errorList, String tcid, String keyword) throws Exception{
			
			String sURL = param.get(0);
			webDriver.manage().timeouts().implicitlyWait(8l, TimeUnit.SECONDS);
			webDriver.get(sURL);
			Thread.sleep(2000);
			objLoginPage.UserName_TextBox();
			if (gtnIsElementPresent(objLoginPage.UserName_TextBox(),"UserID")){
				Framework_Utils.gtnPassLog("Gtn Application invoked successfully");
				return Boolean.TRUE;
			}else{
				Framework_Utils.gtnFailLog("Failed to invoke application");
				Assert.assertTrue(Boolean.FALSE, "app invoke is unsuccessful");
				return false;
			}
				
		}
		
		
		/**
		 * @author chethan kumar
		 * @param param
		 * @return
		 * @throws Throwable
		 */
	public boolean kwdLogin(List<String> param, List<String> errorList, String tcid, String keyword) throws Exception{
			Thread.sleep(10000);										
															
			if(PropertiesHolder.properties.get("config").getProperty("APP_STATUS_CHECK").equalsIgnoreCase("true")){
				PageObj_AppStatus objAppStatPage = PageFactory.initElements(webDriver, PageObj_AppStatus.class);
				boolean Stat = objAppStatPage.kwdAppStatusCheck(baseURL);
				if(Stat == false){
					webDriver.quit();			 
			    }
			 }else{Framework_Utils.gtnWriteLog("INFO", "App status check is set as false in  config properties file"); }
			
			if (!gtnElementPresent(objLoginPage.UserName_TextBox())){
				kwdLogout(param, errorList, tcid, keyword);
			}		
			webDriver.navigate().to(baseURL);
			webDriver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
			gtnWaitForElementPresent(objLoginPage.UserName_TextBox(),"UserId");
						
			String nhUser=PropertiesHolder.properties.get("config").getProperty("NHUser").trim();
			String nhpwd=PropertiesHolder.properties.get("config").getProperty("NHPwd").trim();						//"bfqFDIZWkv6st@";
			
			gtnInsertText(objLoginPage.UserName_TextBox(),"UserName",nhUser);
			gtnInsertText(objLoginPage.Password_TextBox(),"Password",nhpwd);
			gtnClickObj(objLoginPage.Login_Button(),"Login Button");
			Thread.sleep(5000);			
			gtnClickElement(objLoginPage.iconele(), "Click on the Icon");
			Thread.sleep(10);
			
			if (gtnElementPresent(objLoginPage.gtnNhLink())) {
				gtnClickElement(objLoginPage.gtnNhLink(), "Switch to GTN NH Login");
				Thread.sleep(25000);
			}else{
				String URL1=baseURL.replace("/login.jsp", "");
			 	webDriver.navigate().to(URL1 + "/desk/welcome.jsp");
				Thread.sleep(30000);
			}			
			
			try{
				
				if (gtnElementPresent(objLoginPage.Tools_expand())) {
					gtnClickElement(objLoginPage.Tools_expand(), "Cloud tools expand image");
					Thread.sleep(5);
					gtnWaitForElementPresent(objLoginPage.ShadowUser(),"Shadow link");
				}
				
			}catch(NoSuchElementException nse){
				Framework_Utils.gtnInfoLog("Shadow link element not found");
			}						
					
			boolean NHUser=false;
			
			if (param.get(0).equalsIgnoreCase("dgilmore") || param.get(0).equalsIgnoreCase("vguthrie") || param.get(0).equalsIgnoreCase("anil") || param.get(0).trim().equalsIgnoreCase(nhUser.trim())) {
				NHUser=true;
			}	
			
			System.out.println("NHUser flag -->"+NHUser);
			
			if (NHUser==false) {
				
				  //  int retlinkVal=gtnCheckLinkPresenceCount("Shadow");
				  //  System.out.println("LinkFoundCount-->"+retlinkVal);
				
					 boolean retval=gtnElementPresent(objLoginPage.ShadowUser());
				    				
					if (gtnElementPresent(objLoginPage.ShadowUser()) && retval==true) {
						gtnClickElement(objLoginPage.ShadowUser(), "ShadowUser");
						Thread.sleep(20000);
						gtnWaitForElementPresent(objLoginPage.UserName_TextBox(),"UserId");
						gtnInsertText(objLoginPage.UserName_TextBox(),"UserName",param.get(0));
						gtnClickObj(objLoginPage.Login_Button(),"Login Button");
						Thread.sleep(40000);
					}else if(retval==false){
						gtnWaitForElementPresent(objLoginPage.UserName_TextBox(),"UserId");
						gtnInsertText(objLoginPage.UserName_TextBox(),"UserName",param.get(0));
						gtnClickObj(objLoginPage.loginAsButton(),"Login Button");
						Thread.sleep(40000);					
					}						
					
					gtnWaitForElementPresent(objLogoutPage.menuhome(),"Home Button");
					
					if (gtnIsElementPresent(objLogoutPage.menuhome(),"Home Button"))
						Framework_Utils.gtnPassLog("The user (" + param.get(0).toUpperCase() + ") has logged into the application successfully");			
					else
					{
						Framework_Utils.gtnFailLog("The user (" + param.get(0).toUpperCase() + ") has not logged into the application");
						//Assert.assertTrue(Boolean.FALSE, "Login is unsuccessful");
						Framework_Utils.gtnFailLog("The user has failed to login");
						return Boolean.FALSE;
					}
				
			}else{
				Framework_Utils.gtnInfoLog("Network host user is already logged in.");
				gtnWaitForElementPresent(objLoginPage.loginAsField(),"loginAsField");
				
				 if (gtnIsElementPresent(objLoginPage.loginAsField(),"loginAsField"))	{
					 Framework_Utils.gtnPassLog("The user (" + nhUser.toUpperCase() + ") has logged into the application successfully");			
				 }	else
					{
						Framework_Utils.gtnFailLog("The user (" + param.get(0).toUpperCase() + ") has not logged into the application");
						//Assert.assertTrue(Boolean.FALSE, "Login is unsuccessful");
						Framework_Utils.gtnFailLog("The user has failed to login");
						return Boolean.FALSE;
				   }
			}							
			
			if (NHUser==true) {
				GlobalVariables.loggedInUser=nhUser;
			}else{
				GlobalVariables.loggedInUser=param.get(0);
			}								
			
			return Boolean.TRUE;	
		}
	/**
	 * this method is used to login for Tradecard 
	 * @author dajay
	 * @param param
	 * @return boolean
	 * @throws Throwable
	 */
	
	public boolean kwdLoginTradecard(List<String> param, List<String> errorList, String tcid, String keyword) throws Exception{
		Thread.sleep(10000);										
		
		if(PropertiesHolder.properties.get("config").getProperty("APP_STATUS_CHECK").equalsIgnoreCase("true")){
			PageObj_AppStatus objAppStatPage = PageFactory.initElements(webDriver, PageObj_AppStatus.class);
			boolean Stat = objAppStatPage.kwdAppStatusCheck(baseURL);
			if(Stat == false){
				webDriver.quit();			 
		    }
		 }else{Framework_Utils.gtnWriteLog("INFO", "App status check is set as false in  config properties file"); }
		
		if (!gtnElementPresent(objLoginPage.UserName_TextBox())){
			kwdLogout(param, errorList, tcid, keyword);
		}		
		webDriver.navigate().to(baseURL);
		webDriver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		gtnWaitForElementPresent(objLoginPage.UserName_TextBox(),"UserId");
					
		String nhUser=PropertiesHolder.properties.get("config").getProperty("NHUser").trim();
		String nhpwd=PropertiesHolder.properties.get("config").getProperty("NHPwd").trim();						//"bfqFDIZWkv6st@";
		
			gtnInsertText(objLoginPage.UserName_TextBox(),"UserName",nhUser);
			gtnInsertText(objLoginPage.Password_TextBox(),"Password",nhpwd);
			gtnClickObj(objLoginPage.Login_Button(),"Login Button");
			Thread.sleep(5000);	
		
		
		return Boolean.TRUE;
	}

	
		/**
		 * @author ckumar
		 * @param param		
		 * @return
		 * @throws Throwable
		 */
 public boolean kwdLogout(List<String> param, List<String> errorList, String tcid, String keyword) throws Exception{
		
			webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);			
			boolean retlinkVal=gtnCheckLinkPresence("Return to Admin");
			 
			WebElement navmenu_logout = webDriver.findElement(By.id("navmenu__logout"));
			WebElement navmenu_user = webDriver.findElement(By.id("navmenu__user"));
			
			if (retlinkVal) {
				gtnClickElement(objLogoutPage.imglgout(), "imglgout link");
				Thread.sleep(100);
				gtnClickElement(objLogoutPage.returnToAdmin(), "returnToAdmin link");
				Thread.sleep(1000);	
			}else{
				gtnClickElement(navmenu_user, "Navmenu User");
				Thread.sleep(2000);
			}
																									
			if (gtnElementPresent(objLogoutPage.menuhome())==true) {				
								
					Framework_Utils.gtnInfoLog("newNavigation Page is found.");
					webDriver.manage().deleteAllCookies();
					String URL1=baseURL.replace("/login.jsp", "");
					System.out.println("URL1-->"+URL1);
					webDriver.navigate().to(URL1 + "/logout.jsp");
					Thread.sleep(30000);
					webDriver.manage().deleteAllCookies();
					Thread.sleep(5000);
					webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
					
					if(gtnIsElementPresent(objLoginPage.UserName_TextBox(),"User Name Edit")){
						Framework_Utils.gtnPassLog("The user (" + GlobalVariables.loggedInUser.toUpperCase() + ") has logged out of the application successfully");
						return Boolean.TRUE;
					}else{
						Framework_Utils.gtnFailLog("The user has failed to logout of the application");
						return Boolean.FALSE;				
					}
				
			}else if(gtnElementPresent(navmenu_logout)==true){
				webDriver.manage().timeouts().implicitlyWait(5,TimeUnit.SECONDS);
				gtnClickElement(navmenu_logout, "LogOut");
				webDriver.manage().timeouts().implicitlyWait(5,TimeUnit.SECONDS);
			}
		else if(gtnElementPresent(objLogoutPage.imglgout())){
			
					gtnClickElement(objLogoutPage.imglgout(), "imglgout link");
					Thread.sleep(100);
					gtnClickElement(objLogoutPage.Logout_Link(), "Logout");
					Thread.sleep(3000);
					gtnWaitForElementPresent(objLogoutPage.Login_Link(),"LogoutLink");
					if (gtnIsElementPresent(objLogoutPage.Login_Link(),"LoginLink")){
						Framework_Utils.gtnPassLog("The user (" + GlobalVariables.loggedInUser.toUpperCase() + ") has logged out of the application successfully");
						webDriver.manage().deleteAllCookies();
						Thread.sleep(5000);
						webDriver.navigate().to(baseURL);
						return Boolean.TRUE;
					}
			    }
			
		else{			
		
				webDriver.manage().deleteAllCookies();
				String URL1=baseURL.replace("/login.jsp", "");
				webDriver.navigate().to(URL1 + "/logout.jsp");
				Thread.sleep(30000);
				webDriver.manage().deleteAllCookies();
				Thread.sleep(5000);
				gtnClickObj(objLogoutPage.Login_Link(),"Login Link");
				webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
				if(gtnIsElementPresent(objLoginPage.UserName_TextBox(),"User Name Edit")){
					Framework_Utils.gtnPassLog("The user (" + GlobalVariables.loggedInUser.toUpperCase() + ") has logged out of the application successfully");
					return Boolean.TRUE;
				}else{
					Framework_Utils.gtnFailLog("The user has failed to logout of the application");
					return Boolean.FALSE;				
				}
		 }
		 return Boolean.TRUE;
		}
				
		
}
