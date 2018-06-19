/**
 * @author chethan kumar
 * This class is used as a page factory to store common objects in an application like loading object, etc.
 */

package com.gtnexus.selenium.core.pagefactory;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class PageObj_Common {

	
	@FindBy(xpath="//*[contains(text(),'Loading')]")
	private WebElement loadingElmt;
	
	@FindBy(xpath="//a[@id='navmenu__home']")
	private WebElement navmenuhome;
	
//========================================================================
	
	public WebElement Loading(){		
		return loadingElmt;
	}
	
	public WebElement navmenuhome(){		
		return navmenuhome;
	}
	
	
	
}
