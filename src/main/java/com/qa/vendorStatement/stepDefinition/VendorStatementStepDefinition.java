package com.qa.vendorStatement.stepDefinition;

import java.util.Map;

import com.aventstack.extentreports.ExtentTest;
import com.qa.vendorStatement.pages.AuthenticationPage;
import com.qa.vendorStatement.pages.HomePage;
import com.qa.vendorStatement.pages.LoginPage;
import com.qa.vendorStatement.pages.VendorStatementPage;
import com.qa.vendorStatement.util.ExcelDataToDataTable;
import com.qa.vendorStatement.util.TestBase;

import cucumber.api.DataTable;
import cucumber.api.Transform;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

public class VendorStatementStepDefinition extends TestBase{
	
	LoginPage loginPage;
	AuthenticationPage authPage;
	HomePage homePage;
	VendorStatementPage vsPage;
	cucumber.api.Scenario scenario;
	
	@Before
	public void login(cucumber.api.Scenario scenario){
	    this.scenario = scenario;
		ExtentTest loginfo = null;
		try {
			test = extent.createTest(scenario.getName());
			loginfo = test.createNode("Login Test");
			
			TestBase.init();
			loginPage = new LoginPage();
			authPage = loginPage.login();
			homePage = authPage.Authentication();
			homePage.changeRole(prop.getProperty("roleText"), prop.getProperty("roleType"));
			Thread.sleep(1000);
			
			loginfo.pass("Login Successful in Netsuite");
		} catch (AssertionError | Exception e) {
			testStepHandle("FAIL", driver, loginfo, e, scenario.getName());
		}
	}
	
	@After
	public void closeBrowser() {
		driver.close();
	}
	
	@Given("^User is in Vendor Statement page$")
	public void user_is_in_Vendor_Statement_page() {
		ExtentTest loginfo = null;
		try {
			loginfo = test.createNode("User is in Vendor Statement page");
			vsPage = homePage.clickOnVendorStatmentLink();
			loginfo.pass("Navigated to Vendor Statement page");
		} catch (AssertionError | Exception e) {
			testStepHandle("FAIL", driver, loginfo, e, scenario.getName());
		}
	}

	@Then("^verify all the field validations$")
	public void verify_all_the_field_validationss() throws InterruptedException {
		ExtentTest loginfo = null;
		try {
			loginfo = test.createNode("Verify Statement date field validations");
			vsPage.verifyStatementDateFieldValidations(loginfo);
		} catch (AssertionError | Exception e) {
			testStepHandle("FAIL", driver, loginfo, e, scenario.getName());
		}
		ExtentTest loginfo2 = null;
		try {
			loginfo2 = test.createNode("Verify Start date field validations");
			vsPage.verifyStartDateFieldValidations(loginfo2);
		} catch (AssertionError | Exception e) {
			testStepHandle("FAIL", driver, loginfo2, e, scenario.getName());
		}
	}
	
	@Then("^Verify Start date exceed Statement Date error$")
	public void verify_Start_date_exceed_Statement_Date_error() throws InterruptedException {
		ExtentTest loginfo = null;
		try {
			loginfo = test.createNode("Verify Start date exceeds Statement date error");
			vsPage.verifyAlertForInvalidDateRange(loginfo);
		}catch (AssertionError | Exception e) {
			testStepHandle("FAIL", driver, loginfo, e, scenario.getName());
		}
	}
	
	@Then("^Verify the vendors in the vendor sublist based on vendor category using excel data at \"([^\"]*)\"$")
	public void verify_the_vendors_in_the_vendor_sublist_based_on_vendor_category_using_excel_data_at(@Transform(ExcelDataToDataTable.class) DataTable vendorStatementData) throws InterruptedException {
		ExtentTest loginfo = null;
		try {
			loginfo = test.createNode("Verification of Vendors in the Vendor sublist based on Vendor category selected");
			for(Map<String,String> data: vendorStatementData.asMaps(String.class, String.class)) {
				String vendorCategory = data.get("Vendor Category");
				String emailTemplate = data.get("Email Template");
				vsPage.verifyVendorListAfterFilter(vendorCategory, emailTemplate, loginfo);
			}
		}catch (AssertionError | Exception e) {
			testStepHandle("FAIL", driver, loginfo, e, scenario.getName());
		}
	}
	
	@Then("^Verify Mark All functionality with Print & Email functionality$")
	public void verify_Mark_All_functionality_with_Print_Email_functionality() throws InterruptedException {
		ExtentTest loginfo = null;
		try {
			loginfo = test.createNode("Verify Mark all functionality with Print & Email");
			vsPage.markAllWithPrintAndEmail(loginfo);
		}catch (AssertionError | Exception e) {
			testStepHandle("FAIL", driver, loginfo, e, scenario.getName());
		}
	}

	@Then("^Verify Unmark All functionality with Print & Email functionality$")
	public void verify_Unmark_All_functionality_with_Print_Email_functionality() throws InterruptedException {
		ExtentTest loginfo = null;
		try {
			loginfo = test.createNode("Verify Unmark all functionality with Print & Email");
			vsPage.unmarkAllWithPrintAndEmail(loginfo);
		}catch (AssertionError | Exception e) {
			testStepHandle("FAIL", driver, loginfo, e, scenario.getName());
		}
	}
	
	@Then("^Select the vendors and print statement using excel data at \"([^\"]*)\"$")
	public void select_the_vendors_and_print_statement_using_excel_data_at(@Transform(ExcelDataToDataTable.class) DataTable vendorStatementData) throws InterruptedException {
		ExtentTest loginfo = null;
		try {
			loginfo = test.createNode("Verification of Print functionality for the vendor(s) selected");
			for(Map<String,String> data: vendorStatementData.asMaps(String.class, String.class)) {
				int count = 0;
				String vendorCategory = data.get("Category");
				String currency = data.get("Currency");
				String vendors = data.get("Vendor");
				count = vsPage.selectVendors(vendorCategory, vendors, currency);
				vsPage.verifyStatementsInQueue(count, loginfo);
				vsPage.printVendorStatement(vendors, loginfo);
				homePage.clickOnVendorStatmentLink();
			}
		}catch (AssertionError | Exception e) {
			testStepHandle("FAIL", driver, loginfo, e, scenario.getName());
		}
	}
	
	@Then("^Verification of No Email template error$")
	public void verification_of_No_Email_template_error() throws InterruptedException {
		ExtentTest loginfo = null;
		try {
			loginfo = test.createNode("Verification of No Email template error");
			vsPage.verifyNoEmailTemplateError(loginfo);
		}catch (AssertionError | Exception e) {
			testStepHandle("FAIL", driver, loginfo, e, scenario.getName());
		}
	}
	
	@Then("^Verification of No Email Receipients error$")
	public void verification_of_No_Email_Receipients_error() throws InterruptedException {
		ExtentTest loginfo = null;
		try {
			loginfo = test.createNode("Verification of No Email receipients error");
			homePage.clickOnVendorStatmentLink();
			vsPage.verifyNoEmailReceipientsError(loginfo);
		}catch (AssertionError | Exception e) {
			testStepHandle("FAIL", driver, loginfo, e, scenario.getName());
		}
	}
	
	@Then("^Verification of Email to field with invalid emailId$")
	public void verification_of_Email_to_field_with_invalid_emailId() throws InterruptedException {
		ExtentTest loginfo = null;
		try {
			loginfo = test.createNode("Verification of Email to field with invalid Email Id");
			homePage.clickOnVendorStatmentLink();
			vsPage.verifyInvalidEmailError(loginfo);
		}catch (AssertionError | Exception e) {
			testStepHandle("FAIL", driver, loginfo, e, scenario.getName());
		}
	}
	
	@Then("^Verification of Email functionality with valid data using excel data at \"([^\"]*)\"$")
	public void verification_of_Email_functionality_with_valid_data_using_excel_data_at(@Transform(ExcelDataToDataTable.class) DataTable vendorStatementData) throws InterruptedException {
		ExtentTest loginfo = null;
		try {
			loginfo = test.createNode("Verification of Email functionality for the vendor(s) selected");
			for(Map<String,String> data: vendorStatementData.asMaps(String.class, String.class)) {
				String emailTemplate = data.get("Email Template");
				String vendorCategory = data.get("Vendor Category");
				String currency = data.get("Currency");
				String vendors = data.get("Vendor");
				String receipients = data.get("Receipient Email");
				homePage.clickOnVendorStatmentLink();
				vsPage.selectVendors(vendorCategory, vendors, currency);
				vsPage.enterReceipients(vendors, receipients);
				vsPage.selectEmailTemplateAndEmailVendorStatement(emailTemplate);
				vsPage.verifyEmail(vendors, loginfo);
			}
		}catch (AssertionError | Exception e) {
			testStepHandle("FAIL", driver, loginfo, e, scenario.getName());
		}
	}
	
}
