package com.shan.web.pom;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.shan.web.tests.base.BaseTest;

public class POM_LoginPage extends POM_Base{
	
	
	
		
	public POM_LoginPage(WebDriver wDriver,WebDriverWait wDriverWait,BaseTest btest) {
		super(wDriver,wDriverWait,btest);
	}
	
	public void enterLoginData(String userName,String password) {
		wDriver.get("as");
		
	}
	
	
	
	
	
}
