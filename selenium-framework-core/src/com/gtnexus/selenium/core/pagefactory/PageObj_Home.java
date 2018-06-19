/**
 * @author chethan kumar
 * This class is used as a page factory to store objects on GT Nexus home/welcome page
 */


package com.gtnexus.selenium.core.pagefactory;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class PageObj_Home {	
	
	@FindBy(xpath="//select[@name='searchtype']")
	private WebElement searchType;
	
	@FindBy(xpath="//select[@name='searchfieldrow1']")
	private WebElement searchBy;
	
	@FindBy(xpath="//input[@name='searchvaluerow1']")
	private WebElement searchValue;
	
	@FindBy(xpath="//*[contains(text(), 'Advanced Search')]")
	private WebElement advancedSearchLink;
	
	@FindBy(xpath="//a[contains(text(), 'Administration')]")
	private WebElement administration;
		
	
	public WebElement SearchType_ListBox(){
		return searchType;
	}
	
	public WebElement SearchBy_ListBox(){
		return searchBy;
	}
	
	public WebElement SearchVal_EditBox(){
		return searchValue;
	}
	
	public WebElement AdvancedSearchLink(){
		return advancedSearchLink;
	}
	
	public WebElement Administration_Link(){
		return advancedSearchLink;
	}
}
