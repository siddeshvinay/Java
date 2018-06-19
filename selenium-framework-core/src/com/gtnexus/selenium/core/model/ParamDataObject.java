/**
 * @author chethan kumar
 * This class is used to retrieve method/keyword name, related class name and related parameter lists 
 * from excel data sheet for further use in the framework
 */

package com.gtnexus.selenium.core.model;

import java.util.List;

public class ParamDataObject {
	private String keywordName;
	private String classname;
	private List<String> paramList;
	
	public String getClassname() {
		return classname;
	}
	public void setClassname(String classname) {
		this.classname = classname;
	}
	
	public String getKeywordName() {
		return keywordName;
	}
	public void setKeywordName(String keywordName) {
		this.keywordName = keywordName;
	}
	public List<String> getParamList() {
		return paramList;
	}
	public void setParamList(List<String> paramList) {
		this.paramList = paramList;
	}
	
	
	
	/*public String getKeywordName() {
		return keywordName;
	}
	public void setKeywordName(String keywordName) {
		this.keywordName = keywordName;
	}
	public List<String> getParamList() {
		return paramList;
	}
	public void setParamList(List<String> paramList) {
		this.paramList = paramList;
	}*/
		
}
