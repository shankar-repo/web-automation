package com.shan.web.utilities;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.shan.web.common.SubmitResponse;
import com.shan.web.tests.base.BaseTest;

public class UserAction {
	static Logger log = Logger.getLogger(UserAction.class.getName());

	BaseTest baseTest;
	WebDriver webDriver;
	WebDriverWait webDriverWait;

	public UserAction(BaseTest btest, WebDriver wd, WebDriverWait wdw) {
		this.baseTest = btest;
		this.webDriver = wd;
		this.webDriverWait = wdw;
	}

	public void goToURL(String url) {
		this.webDriver.get(url);
		baseTest.Info("URL <b>" + url + "</b> should be navigated", "URL <b>" + url + "</b> is navigated");
	}

	public void comparePageTitle(String expTitle) {
		if (this.webDriver.getTitle().equals(expTitle)) {
			baseTest.Pass("Page title should be " + expTitle, "Page title is " + expTitle);
		} else if (this.webDriver.getTitle().equalsIgnoreCase(expTitle)) {
			baseTest.Warning("Page title should be " + expTitle, "Page title is " + expTitle + " - case mismatched");
		} else {
			baseTest.Fail("Page title should be " + expTitle,
					"Page title is not " + expTitle + " - actual value is " + this.webDriver.getTitle());
		}
	}

	public void enterText(By byForElement, String dataToEnter) {
		enterText(byForElement, dataToEnter, "test");
	}

	public void enterText(By byForElement, String dataToEnter, String friendlyName) {
		if (StringUtils.isBlank(friendlyName)) {
			friendlyName = "test";
		}
		WebElement webElement = getElement(byForElement);
		if (webElement != null && webElement.isDisplayed()) {
			if (StringUtils.isNotBlank(dataToEnter)) {
				webElement.click();
				if (dataToEnter.equalsIgnoreCase("null")) {
					webElement.clear();
					baseTest.Done("User should be able to clear text in the <b>" + friendlyName + "</b> web element",
							"User is able to clear text in the <b>" + friendlyName + "</b> web element");
				} else {
					webElement.sendKeys(dataToEnter);
					baseTest.Done(
							"User should be able to enter <b>" + dataToEnter + "</b> text in the <b>" + friendlyName
									+ "</b> web element",
							"User is able to enter <b>" + dataToEnter + "</b> text in the <b>" + friendlyName
									+ "</b> web element");
				}
			} else {
				baseTest.Warning(
						"User should be able to enter <b>" + dataToEnter + "</b> text in the <b>" + friendlyName
								+ "</b> web element",
						"Data is empty and no text entered in the <b>" + friendlyName + "</b> web element");
			}

		} else {
			baseTest.Fail(
					"User should be able to enter <b>" + dataToEnter + "</b> text in the <b>" + friendlyName
							+ "</b> web element",
					"<b>" + friendlyName + "</b> web element is not present/available in the page " + getCurrentTitle()
							+ " to enter text <b>" + dataToEnter + "</b>");
		}
	}

	public void buttonClick(By byForElement) {
		buttonClick(getElement(byForElement), "test");
	}

	public void buttonClick(By byForElement, String friendlyName) {
		buttonClick(getElement(byForElement), friendlyName);
	}

	public void buttonClick(WebElement wElement) {
		buttonClick(wElement, "test");
	}

	public void buttonClick(WebElement webElement, String friendlyName) {
		if (StringUtils.isBlank(friendlyName)) {
			friendlyName = "test";
		}
		if (webElement != null && webElement.isDisplayed() && webElement.isEnabled()) {
			webElement.click();
			baseTest.Done("User should be able to click the <b>" + friendlyName + "</b> web element",
					"User is able to click the <b>" + friendlyName + "</b> web element");

		} else {
			baseTest.Fail("User should be able to click the <b>" + friendlyName + "</b> web element",
					"<b>" + friendlyName + "</b> web element is not present/available in the page " + getCurrentTitle()
							+ " to click");
		}
	}

	public void chkBoxClick(WebElement webElement, String friendlyName) {
		if (StringUtils.isBlank(friendlyName)) {
			friendlyName = "test";
		}
		if (webElement != null && webElement.isEnabled()) {
			webElement.click();
			baseTest.Done("User should be able to click the <b>" + friendlyName + "</b> web element",
					"User is able to click the <b>" + friendlyName + "</b> web element");

		} else {
			baseTest.Fail("User should be able to click the <b>" + friendlyName + "</b> web element",
					"<b>" + friendlyName + "</b> web element is not present/available in the page " + getCurrentTitle()
							+ " to click");
		}
	}

	public SubmitResponse waitForSubmit(By mainObjectToShow) {
		return waitForSubmit(mainObjectToShow, null);
	}

	public SubmitResponse waitForSubmit(By mainObjectToShow, By errorObject) {
		WebElement mainObj = getElement(mainObjectToShow);

		if (mainObj != null) {
			return SubmitResponse.HIDDEN;
		}

		if (errorObject == null) {
			return SubmitResponse.FAILURE;
		}
		WebElement errObj = getElement(errorObject);

		if (errObj != null) {
			return SubmitResponse.ERROR;
		}
		return SubmitResponse.FAILURE;
	}

	public WebElement getElement(By element) {
		try {
			return webDriverWait.until(ExpectedConditions.presenceOfElementLocated(element));
		} catch (TimeoutException e) {
			baseTest.Fail("User should be able to find a element using locator <b>" + element.toString() + "</b>",
					"Useris not able to find a element using locator <b>" + element.toString() + "</b>");
			return null;
		}
	}

	public List<WebElement> getElements(By parentObject, By element) {
		try {
			return webDriverWait.until(ExpectedConditions.presenceOfNestedElementsLocatedBy(parentObject, element));
		} catch (TimeoutException e) {
			baseTest.Fail("User should be able to find a element using locator <b>" + element.toString() + "</b>",
					"Useris not able to find a element using locator <b>" + element.toString() + "</b>");
			return null;
		}
	}

	public List<WebElement> getElements(WebElement parentObject, By element) {
		try {
			if (parentObject != null && parentObject.isDisplayed()) {
				return parentObject.findElements(element);
			}
		} catch (TimeoutException e) {
			baseTest.Fail("User should be able to find a element using locator <b>" + element.toString() + "</b>",
					"Useris not able to find a element using locator <b>" + element.toString() + "</b>");
		}
		return null;
	}

	public WebElement getElementFromParent(By parentElement, By childElement) {
		try {
			return webDriverWait
					.until(ExpectedConditions.presenceOfNestedElementLocatedBy(parentElement, childElement));
		} catch (TimeoutException e) {
			baseTest.Fail(
					"User should be able to find a element using locator <b>" + childElement.toString()
							+ "</b> from the parent element <b>" + parentElement.toString() + "</b>",
					"Useris not able to find a element using locator <b>" + childElement.toString()
							+ "</b> from the parent element <b>" + parentElement.toString() + "</b>");
			return null;
		}
	}

	public WebElement getElementFromParent(WebElement webElement, By childElement) {
		try {
			return webDriverWait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(webElement, childElement));
		} catch (TimeoutException e) {
			baseTest.Fail(
					"User should be able to find a element using locator <b>" + childElement.toString()
							+ "</b> from the parent element <b>" + webElement.getText() + "</b>",
					"Useris not able to find a element using locator <b>" + childElement.toString()
							+ "</b> from the parent element <b>" + webElement.getText() + "</b>");
			return null;
		}
	}

	public String getCurrentTitle() {
		return webDriver.getTitle();
	}

	private By ddm_showHideButton = By.xpath("button/span[text()='Show/Hide Filters']");
	private By ddm_MenuObject = By.xpath("div[contains(@class,'dropdown-menu')]");
	private By ddm_MO_SelectAll = By.xpath("//button[text()='Select all']");
	private By ddm_MO_DeselectAll = By.xpath("//button[text()='Deselect all']");
	private By ddm_MO_ML_Input = By.tagName("input");

	private By get_ddm_MO_ML_div(String menuName) {
		return By.xpath("//li//label[contains(text(),'" + menuName + "')]/parent::div");
	}

	public void selectDropDownMenuCheckbox(By prtObjDropDownMenu, String itemToSelect) {
		selectDropDownMenuCheckbox(getElement(prtObjDropDownMenu), itemToSelect);
	}

	public void selectDropDownMenuCheckbox(WebElement prtObjDropDownMenu, String itemToSelect) {
		List<String> itemsToSelect = new ArrayList<>();
		itemsToSelect.add(itemToSelect);
		selectDropDownMenuCheckbox(prtObjDropDownMenu, itemsToSelect, false, false);
	}

	public void selectDropDownMenuCheckbox(WebElement prtObjDropDownMenu, List<String> itemsToSelect,
			boolean isSelectAll, boolean isDeSellectAll) {

		if (prtObjDropDownMenu != null && prtObjDropDownMenu.isDisplayed()) {

			if (ddm_ShowDropDownMenu(prtObjDropDownMenu)) {
				WebElement dropDownMenuElement = getElementFromParent(prtObjDropDownMenu, ddm_MenuObject);
				if (isSelectAll) {
					ddm_SelectAll(dropDownMenuElement);
					return;
				}
				if (isDeSellectAll) {
					ddm_DeselectAll(dropDownMenuElement);
					return;
				}
				if (itemsToSelect != null && itemsToSelect.size() > 0) {
					ddm_DeselectAll(dropDownMenuElement);

					for (String menuName : itemsToSelect) {
						if(!checkUnCheckLiItem(dropDownMenuElement, menuName, true)) {
							baseTest.Fail(menuName + " item checked is not selected");
						}
					}
				}
				ddm_HideDropDownMenu(prtObjDropDownMenu);
			}
		}
	}

	private boolean checkUnCheckLiItem(WebElement dropDownMenu, String name, boolean isCheck) {
		WebElement divMenu = getElementFromParent(dropDownMenu, get_ddm_MO_ML_div(name));

		if (divMenu != null && divMenu.isDisplayed()) {
			WebElement chkBox = getElementFromParent(divMenu, ddm_MO_ML_Input);
			boolean isItemChecked = chkBox.isSelected();
			if (isCheck == isItemChecked) {
				baseTest.Done(name + " item checked is set to " + Boolean.toString(isItemChecked));
			} else {
				chkBoxClick(getElementFromParent(divMenu, By.tagName("label")), name);
			}
			return getElementFromParent(divMenu, ddm_MO_ML_Input).isSelected() == isCheck;
		}
		return false;
	}

	private boolean ddm_ShowDropDownMenu(WebElement paretMenuObject) {
		if (paretMenuObject != null) {
			WebElement dropDownMenu = getElementFromParent(paretMenuObject, ddm_MenuObject);
			if (dropDownMenu != null) {
				if (dropDownMenu.isDisplayed()) {
					return true;
				}
			}

			buttonClick(getElementFromParent(paretMenuObject, ddm_showHideButton));
			WebElement afterDropDownMenu = getElementFromParent(paretMenuObject, ddm_MenuObject);
			return afterDropDownMenu != null && afterDropDownMenu.isDisplayed();
		}
		return false;
	}

	private boolean ddm_HideDropDownMenu(WebElement paretMenuObject) {

		if (paretMenuObject != null) {
			WebElement dropDownMenu = getElementFromParent(paretMenuObject, ddm_MenuObject);
			if (dropDownMenu != null) {
				if (dropDownMenu.isDisplayed()) {
					buttonClick(getElementFromParent(paretMenuObject, ddm_showHideButton));
				}
			}

			WebElement afterDropDownMenu = getElementFromParent(paretMenuObject, ddm_MenuObject);
			if(afterDropDownMenu != null) {
				if(!afterDropDownMenu.isDisplayed()) {
					return true;
				}
			}else {
				return true;
			}
		}
		return false;
	}

	private void ddm_SelectAll(WebElement objMenuList) {
		if (objMenuList != null && objMenuList.isDisplayed()) {
			WebElement selectAll = getElementFromParent(objMenuList, ddm_MO_SelectAll);
			if (selectAll != null && !selectAll.isEnabled()) {
				buttonClick(getElementFromParent(objMenuList, ddm_MO_DeselectAll));
			}
			buttonClick(selectAll);
		}
	}

	private void ddm_DeselectAll(WebElement objMenuList) {
		if (objMenuList != null && objMenuList.isDisplayed()) {
			WebElement DeselectAll = getElementFromParent(objMenuList, ddm_MO_DeselectAll);
			if (DeselectAll != null && !DeselectAll.isEnabled()) {
				buttonClick(getElementFromParent(objMenuList, ddm_MO_SelectAll));
			}
			buttonClick(DeselectAll);
		}
	}

}
