package com.qa.vendorStatement.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.qa.vendorStatement.util.TestBase;

public class LoginPage extends TestBase{
	
	@FindBy(id = "userName")
	WebElement username;
	
	@FindBy(id = "password")
	WebElement password;
	
	@FindBy(id = "submitButton")
	WebElement submitBtn;
	
	public LoginPage() {
		PageFactory.initElements(driver, this);
	}
	
	public AuthenticationPage login() {
		username.sendKeys(prop.getProperty("username"));
		password.sendKeys(prop.getProperty("password"));
		submitBtn.click();
		
		return new AuthenticationPage();
	}
	
}
