/**
 * @author chethan kumar

 * This class is used drive and control the execution through different keywords based on the excel flow defined
 */


package com.gtnexus.selenium.core;

import org.openqa.selenium.WebDriver;

import com.gtnexus.selenium.core.utils.Framework_Utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Controller {

	public boolean mainController (String keyword, String classname, List<String> param_array, String tcid, Object obj) throws Exception {

		Boolean flg = Boolean.FALSE;
		Method method = obj.getClass().getMethod("getWebDriver");
		WebDriver driver= (WebDriver)method.invoke(obj);
		
		method = obj.getClass().getMethod("getBaseURL");
		String url = (String)method.invoke(obj);
		
		Method method2 = obj.getClass().getMethod("getErrorList");
		Object newObj = method2.invoke(obj);
		List<String> errorList = (ArrayList<String>) newObj;
		
		try {
			
			Class<?> newClass = Class.forName(classname);
			Constructor<?> ctor = newClass.getConstructor(WebDriver.class, String.class);
			ctor.setAccessible(true);
			Object newInstance = ctor.newInstance(driver, url);
			Method newmethod = newInstance.getClass().getMethod(keyword, List.class, List.class, String.class, String.class);
			Object boolflg = newmethod.invoke(newInstance, param_array, errorList, tcid, keyword);
			if ((Boolean)boolflg)
				flg =  Boolean.TRUE;
			else
				flg =  Boolean.FALSE;
			}catch (Exception e) {
				Framework_Utils.gtnFailLog("The test case failed due to: " + e.getMessage());
				flg =  Boolean.FALSE;
			}
		return flg;
	}
}
