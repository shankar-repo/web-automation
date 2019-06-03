package com.nectar.ambrosia.pom;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.nectar.ambrosia.tests.base.BaseTest;
import com.nectar.ambrosia.utilities.UserAction;

public class POM_Base {
	WebDriver wDriver;
	WebDriverWait wDriverWait;
	UserAction userAction;
	BaseTest baseTest;
	
	public POM_Base() {
		
	}
	
	public POM_Base(WebDriver wDriver,WebDriverWait wDriverWait,BaseTest btest){
		this.wDriver = wDriver;
		this.wDriverWait = wDriverWait;
		this.baseTest = btest;
		this.userAction = this.baseTest.userAction;
	}
	
}
