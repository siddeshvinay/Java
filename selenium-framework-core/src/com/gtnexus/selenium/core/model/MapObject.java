/**
 * @author chethan kumar
 * This class is used to generate map objects for storing excel data and keywords for further use in the framework
 */

package com.gtnexus.selenium.core.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MapObject {

	private LinkedHashMap<String, String> tcMap;
	private Map<String, List<ParamDataObject>> keyMap;	
	
	public LinkedHashMap<String, String> getTcMap() {
		return tcMap;
	}
	public void setTcMap(LinkedHashMap<String, String> tcMap) {
		this.tcMap = tcMap;
	}
			
	public Map<String, List<ParamDataObject>> getKeyMap() {
		return keyMap;
	}
	public void setKeyMap(Map<String, List<ParamDataObject>> keyMap) {
		this.keyMap = keyMap;
	}
	

	
	
}
