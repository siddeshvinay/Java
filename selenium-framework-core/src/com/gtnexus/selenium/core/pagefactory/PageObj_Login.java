/**
 * @author chethan kumar
 * This class is used as a page factory to store objects on GT Nexus login page
 */

package com.gtnexus.selenium.core.pagefactory;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;


public class PageObj_Login {
		
	@FindBy(xpath="//input[@name='login' or @name='userid' or @name='loginAsField']")
	private WebElement userName;
	
	@FindBy(xpath="//input[@name='password' or @name='uPassword']")
	private WebElement password;
	
	@FindBy(xpath="//input[@alt='Login' or @value='Login']")
	private WebElement loginButton;
	
	@FindBy(xpath="//input[@value='Search']")
	private WebElement searchButton;	
		
	@FindBy(xpath="//div[@class='logintext_error']")
	private WebElement loginFailed;
	
	@FindBy(xpath="//input[@name='loginAsField']")
	private WebElement loginAsField;
	
	@FindBy(xpath="//button[contains(text(),'Login')]")
	private WebElement loginAsButton;
	
	@FindBy(xpath="//a[@gtnid='Shadow']")
	private WebElement ShadowUser;
	
	@FindBy(xpath="//img[@gtnid='Cloud Tools_expand']")
	private WebElement Tools_expand;
	
	@FindBy(how=How.XPATH,using="//a[@id='navmenu__user']")	
	private WebElement iconele;
	
	@FindBy(how=How.XPATH,using="//a[@id='navmenu__switch to gtn nh' or href='/desk/welcome.jsp']")	
	private WebElement gtnNhLink;
		
	
	
	@FindBy(how=How.XPATH,using="//*[@id='personalizePageLink']/a")
	private WebElement personalizePageLink;

	
// --------- WebElement Declaration ------------------	
		
	public WebElement UserName_TextBox() {
	  return userName;
	}

	public WebElement Password_TextBox() {
		return password;
	}

	public WebElement Login_Button() {
		return loginButton;
	}
	
	public WebElement Search_Button() {
		return searchButton;
	}
	
	public WebElement LoginFailed() {
		return loginFailed;
	}
	
	public WebElement loginAsField() {
		return loginAsField;
	}
	
	public WebElement loginAsButton() {
		return loginAsButton;
	}
		
	public WebElement ShadowUser() {
		return ShadowUser;
	}
		
	public WebElement Tools_expand() {
		return Tools_expand;
	}
	
	public WebElement personalizePageLink() {
		return personalizePageLink;
	}
	
	public WebElement iconele() {
		return iconele;
	}
	
	public WebElement gtnNhLink() {
		return gtnNhLink;
	}
			
	
	
}

