/**
 * @author chethan kumar
 * This class is used as a page factory to store objects on GT Nexus logout functionality
 */

package com.gtnexus.selenium.core.pagefactory;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class PageObj_Logout {
	
	@FindBy(xpath="//ul[@id='headernav']/li/a")
	private WebElement headernav;

	@FindBy(xpath="//a[@id='navmenu__logout' or @href='/logout.jsp']")	//a[contains(text(),'Log Out')]
	private WebElement logoutlink;

	@FindBy(xpath="//p[@class='text1']/a")
	private WebElement loginlink;

	@FindBy(xpath="//a[@id='usericon']")
	private WebElement usericon;
	
	@FindBy(xpath="//a[@id='navmenu__logout']")
	private WebElement menulogout;

	@FindBy(xpath="//*[@id='navmenu__home' or @href='/desk/welcome.jsp']")
	private WebElement menuhome;
		
	@FindBy(xpath="//img[@src='/images/caret.gif']")
	private WebElement imglgout;
	
	@FindBy(xpath="//a[@id='navmenu__returnToAdmin']")
	private WebElement returnToAdmin;
	
		
	//========================================================================
	
	public WebElement imglgout() {
		return imglgout;
	}	
	
	public WebElement usericon() {
		return usericon;
	}	
	
	public WebElement menulogout() {
		return menulogout;
	}
	
	public WebElement menuhome() {
		return menuhome;
	}
	
	public WebElement HeaderNav() {
		return headernav;
	}
	
	public WebElement Logout_Link() {
		return logoutlink;
	}
	
	public WebElement Login_Link() {
		return loginlink;
	}
	
	public WebElement returnToAdmin() {
		return returnToAdmin;
	}
	
		
	
	
}
