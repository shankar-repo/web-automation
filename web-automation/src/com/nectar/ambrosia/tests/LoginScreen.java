package com.nectar.ambrosia.tests;

import org.openqa.selenium.By;
import org.testng.annotations.Test;

import com.nectar.ambrosia.common.SubmitResponse;
import com.nectar.ambrosia.tests.base.BaseTest;
import com.nectar.ambrosia.utilities.CommonUtilities;

public class LoginScreen extends BaseTest {

	public final By txtUserName = By.name("login");
	public final By txtPassword = By.name("password");
	public final By divPasswordVi = By.xpath("//div[@class='dui-login-inputs-password']/div");
	public final By lblErrorMessage = By.xpath("//div[contains(@class,'dui-login-error-message')]");
	public final By btnSubmit = By.xpath("//button[@type='submit']");

	@Test
	public void test() {
		
		String url = getTestData("url");
		String username = getTestData("username");
		String password = getTestData("password");
		String errormessage = getTestData("errormessage");
		boolean iserrorvalidation = CommonUtilities.getBoolValue(getTestData("iserrorvalidation"));

		By byLoggedInUsername = By.xpath("//*[name()='svg' and @class='svg-user-dims']/following-sibling::span");
		
		userAction.goToURL(url);
		userAction.enterText(txtUserName, username, "User name");	
		userAction.getElement(divPasswordVi).click();
		userAction.getElement(divPasswordVi).click();
		userAction.enterText(txtPassword, password, "Password");
		userAction.buttonClick(btnSubmit, "Login");

		if (iserrorvalidation) {
			if (userAction.waitForSubmit(lblErrorMessage) == SubmitResponse.HIDDEN) {
				String actErrorMessage = userAction.getElement(lblErrorMessage).getText();
				if (errormessage.equals(actErrorMessage)) {
					Pass("Verify whether the error message <b>" + errormessage + "</b> is displayed on the login page",
							"Error message <b>" + errormessage + "</b> is displayed on the login page");
				} else {
					Fail("Verify whether the error message <b>" + errormessage + "</b> is displayed on the login page",
							"Error message <b>" + actErrorMessage + "</b> is displayed on the login page instead of <b>"
									+ errormessage + "</b>");
				}
			} else {
				Fail("Verify whether the error message <b>" + errormessage + "</b> is displayed on the login page",
						"Error label is not displayed on the login page");
			}
		} else {
			SubmitResponse sr = userAction.waitForSubmit(byLoggedInUsername, lblErrorMessage);
			if (sr == SubmitResponse.HIDDEN) {
				Pass("Verify whether the user is able to login", "User is able to login");
				Title("User name validation");

				String loggedInUserName = userAction.getElement(byLoggedInUsername).getText();
				if (username.equals(loggedInUserName)) {
					Pass("Verify whether the user <b> " + username
							+ "</b> is logged in by checking the top right corner label",
							"<b> " + username + "</b> is logged in", true);
					
					userAction.selectDropDownMenuCheckbox(By.xpath("/html/body/app-root/div/app-summary/app-global-filter-component/section/div/div[1]/app-filter-select-component/div"), "Alert Level");
					
				} else {
					Fail("Verify whether the user <b> " + username
							+ "</b> is logged in by checking the top right corner label",
							"<b> " + loggedInUserName + "</b> is logged in instead of <b>" + username + "</b>", true);
				}

			} else if (sr == SubmitResponse.ERROR) {
				Fail("Verify whether the user is able to login",
						"User is not able to login and error message is displayed", true);
			} else if (sr == SubmitResponse.FAILURE) {
				Fail("Verify whether the user is able to login",
						"User is not able to login and either error label nor main object is not displayed", true);
			}
		}
	}

}
