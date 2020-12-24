package com.qa.vendorStatement.pages;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.aventstack.extentreports.ExtentTest;
import com.qa.vendorStatement.util.TestBase;

public class VendorStatementPage extends TestBase{
	
	@FindBy(xpath = "//input[@id='custpage_enddate']")
	WebElement statementDate;
	
	@FindBy(xpath = "//input[@id='custpage_startdate']")
	WebElement startDate;
	
	@FindBy(xpath = "//input[@id='inpt_custpage_emailtemplate1']")
	WebElement emailTemplate;
	
	@FindBy(xpath = "//input[@id='inpt_custpage_vendorcategory2']")
	WebElement vendorCategory;
	
	@FindBy(xpath = "//input[@id='inpt_Vendor_CATEGORY1']")
	WebElement vendorCategoryInSearch;
	
	@FindBy(xpath = "//span[@id='Vendor_CATEGORY_fs_lbl']//a[@class='smalltextnolink']")
	WebElement vendorCategoryLabelInSearch;
	
	@FindBy(xpath = "//table[@id='div__bodytab']//tr[contains(@class,'uir-list-row-tr')]//a[contains(@id,'qsTarget_')]")
	List<WebElement> vendorsInSearch;
	
	@FindBy(xpath = "//input[@id='custpage_queue']")
	WebElement queue;
	
	@FindBy(xpath = "//span[@id='custpage_check1_fs']/img")
	WebElement checkbox;
	
	@FindBy(xpath = "//input[@id='custpage_print']")
	WebElement printBtn;
	
	@FindBy(xpath = "//tr[contains(@id,'custpage_detailsrow')]//td[2]//a")
	List<WebElement> vendorList;
	
	@FindBy(xpath = "//span[text()='Lists']")
	WebElement listsLink;
	
	@FindBy(xpath = "//span[text()='Relationships']")
	WebElement relationshipsLink;
	
	@FindBy(xpath = "//span[text()='Vendors']")
	WebElement vendorsLink;
	
	@FindBy(xpath = "//input[contains(@id,'inpt_searchid')]")
	WebElement viewDropdown;
	
	@FindBy(xpath = "//span[@id='searchid_fs_lbl']//a[@class='smalltextnolink']")
	WebElement viewLabel;
	
	@FindBy(xpath = "//span[@class='ns-icon ns-filters-onoff-button']")
	WebElement filtersDiv;
	
	@FindBy(xpath = "//div[@class='dropdownDiv']//div")
	List<WebElement> categoryOptions;
	
	@FindBy(xpath = "//input[@id='custpage_markall']")
	WebElement markAllBtn;
	
	@FindBy(xpath = "//input[@id='custpage_unmarkall']")
	WebElement unMarkAllBtn;
	
	@FindBy(xpath = "//input[@id='custpage_send']")
	WebElement emailBtn;
	
	@FindBy(xpath = "//td[contains(@class,'uir-list-row-cell listtext')]//a")
	List<WebElement> vendorsList;
	
	@FindBy(xpath = "//select[@id='currency']")
	WebElement currencyDropdown;
	
	@FindBy(xpath = "//input[@type='button']")
	WebElement saveBtn;
	
	@FindBy(xpath = "//a[@id='custpage_vendorstxt']")
	WebElement vendorSublist;
	
	public VendorStatementPage() {
		PageFactory.initElements(driver, this);
	}
	
	public void verifyEmail(String vendorsData, ExtentTest logInfo) throws InterruptedException {
		// Verify the confirmation alert
		if(isAlertPresent()) {
			Thread.sleep(1000);
			Alert alert = driver.switchTo().alert();
			String alertMsg = alert.getText();
			if(alertMsg.contains("cannot")) {
				System.out.println("Alert occured: "+alertMsg);
				logInfo.fail("Email unable to send for the vendors:"+vendorsData.replace("/", ", ")+", Alert occured: "+alertMsg);
			}else {
				System.out.println("Alert occured: "+alertMsg);
				logInfo.pass("Alert occured: "+alertMsg);
			}
			alert.accept();
		}else {
			System.out.println("Alert not occured");
			logInfo.fail("Alert not occured");
		}
	}
	
	public void enterReceipients(String vendorsData, String receipientsData) {
		action = new Actions(driver);
		JavascriptExecutor executor = (JavascriptExecutor)driver;
		if(receipientsData.contains("/")) {
			String[] multipleReceipients = receipientsData.split("/");
			String[] multipleVendors = vendorsData.split("/");
			if(multipleVendors.length == multipleReceipients.length) {
				for (int j = 0; j < multipleReceipients.length; j++) {
					// Select Items in the click to add list
					for(int i=0;i<vendorsList.size();i++) {
						WebElement currentVendor = vendorsList.get(i);
						String text = currentVendor.getText().trim();
						if(text.equals(multipleVendors[j])) {
							if(i==0)
								executor.executeScript("arguments[0].scrollIntoView(true);", vendorSublist);
							else
								executor.executeScript("arguments[0].scrollIntoView(true);", vendorList.get(i-1));
							WebElement parentTd = (WebElement)executor.executeScript("return arguments[0].parentNode;", currentVendor);
							WebElement parentTr = (WebElement)executor.executeScript("return arguments[0].parentNode;", parentTd);
							String trId = parentTr.getAttribute("id");
							
							// Enter Receipient email
							WebElement emailToTextArea = driver.findElement(By.xpath("//tr[@id='"+trId+"']//textarea"));
							action.click(emailToTextArea).keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).sendKeys(Keys.BACK_SPACE).sendKeys(emailToTextArea, multipleReceipients[j]).build().perform();							
						}
					}
				}
			}else {
				System.out.println("No.of vendors selected & no.of receipients emails given does'nt match");
			}
		}else {
			for(int i=0;i<vendorsList.size();i++) {
				WebElement currentVendor = vendorsList.get(i);
				String text = currentVendor.getText().trim();
				if(text.equals(vendorsData)) {
					if(i==0)
						executor.executeScript("arguments[0].scrollIntoView(true);", vendorSublist);
					else
						executor.executeScript("arguments[0].scrollIntoView(true);", vendorList.get(i-1));
					WebElement parentTd = (WebElement)executor.executeScript("return arguments[0].parentNode;", currentVendor);
					WebElement parentTr = (WebElement)executor.executeScript("return arguments[0].parentNode;", parentTd);
					String trId = parentTr.getAttribute("id");
					
					// Enter Receipient email
					WebElement emailToTextArea = driver.findElement(By.xpath("//tr[@id='"+trId+"']//textarea"));
					action.click(emailToTextArea).keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).sendKeys(Keys.BACK_SPACE).sendKeys(emailToTextArea, receipientsData).build().perform();
				}
			}
		}
	}
	
	public void selectEmailTemplateAndEmailVendorStatement(String emailTemplateData) {
		// Select Email template
		JavascriptExecutor executor = (JavascriptExecutor)driver;
		executor.executeScript("window.scrollTo(0,0)");
		emailTemplate.sendKeys(emailTemplateData);
		queue.click();
		
		// Click Email button
		emailBtn.click();
	}
	
	public void verifyNoEmailReceipientsError(ExtentTest logInfo) throws InterruptedException {
		// Select email template
		emailTemplate.sendKeys("Invoice Email Template");
		queue.click();
		
		// Remove 1st vendor receipient email text
		driver.findElement(By.xpath("//tr[@id='custpage_detailsrow0']//textarea")).clear();
		
		// Select first vendor "Print" checkbox
		driver.findElement(By.xpath("//tr[@id='custpage_detailsrow0']//img")).click();
		emailBtn.click();
		
		// Verify the alert
		if(isAlertPresent()) {
			Thread.sleep(1000);
			Alert alert = driver.switchTo().alert();
			String alertMsg = alert.getText();
			System.out.println("Alert occured: "+alertMsg);
			logInfo.pass("Alert occured: "+alertMsg);
			alert.accept();
		}else {
			System.out.println("Alert not occured");
			logInfo.fail("Alert not occured");
		}
	}
	
	public void verifyInvalidEmailError(ExtentTest logInfo) throws InterruptedException {
		action = new Actions(driver);
		// Select email template
		emailTemplate.sendKeys("Invoice Email Template");
		queue.click();
		
		// Enter invalid email Id in Email to field
		WebElement emailToTextArea = driver.findElement(By.xpath("//tr[@id='custpage_detailsrow0']//textarea"));
		action.click(emailToTextArea).keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).sendKeys(Keys.BACK_SPACE).sendKeys(emailToTextArea, "testEmail").build().perform();
		
		// Select first vendor "Print" checkbox & click email button
		driver.findElement(By.xpath("//tr[@id='custpage_detailsrow0']//img")).click();
		emailBtn.click();
		
		// Verify the alert
		if(isAlertPresent()) {
			Thread.sleep(1000);
			Alert alert = driver.switchTo().alert();
			String alertMsg = alert.getText();
			System.out.println("Alert occured: "+alertMsg);
			logInfo.pass("Alert occured: "+alertMsg);
			alert.accept();
		}else {
			System.out.println("Alert not occured");
			logInfo.fail("Alert not occured");
		}
	}
	
	public void verifyNoEmailTemplateError(ExtentTest logInfo) throws InterruptedException {
		// Select first vendor "Print" checkbox & click email button
		driver.findElement(By.xpath("//tr[@id='custpage_detailsrow0']//img")).click();
		emailBtn.click();
		
		// Verify the alert
		if(isAlertPresent()) {
			Thread.sleep(1000);
			Alert alert = driver.switchTo().alert();
			String alertMsg = alert.getText();
			System.out.println("Alert occured: "+alertMsg);
			logInfo.pass("Alert occured: "+alertMsg);
			alert.accept();
		}else {
			System.out.println("Alert not occured");
			logInfo.fail("Alert not occured");
		}
	}
	
	public void printVendorStatement(String vendorsData, ExtentTest logInfo) throws InterruptedException {
		JavascriptExecutor executor = (JavascriptExecutor)driver;
		// Click Print Button
		executor.executeScript("window.scrollTo(0,0)");
		printBtn.click();
		
		String poPageWindow = driver.getWindowHandle();
		Set<String> allWindows = driver.getWindowHandles();
		for(String child: allWindows) {
			if(!poPageWindow.equals(child)) {
				driver.switchTo().window(child);
			}
		}
		if(driver.getTitle().isBlank()) {
			Thread.sleep(4000);
			driver.close();
			// Switch to PO Window
			driver.switchTo().window(poPageWindow);
			if(vendorsData.contains("/")) {
				System.out.println("Vendor statement print successfull for vendors: "+vendorsData.replace("/", ", "));
				logInfo.pass("Vendor statement print successfull for vendors: "+vendorsData.replace("/", ", "));
			}
			else {
				System.out.println("Vendor statement print successfull for vendor: "+vendorsData);
				logInfo.pass("Vendor statement print successfull for vendor: "+vendorsData);
			}
		}else {
			logInfo.fail("Vendor Statement not printed");
		}
	}
	
	public void verifyStatementsInQueue(int countInVsPage, ExtentTest logInfo) {
		String queueValue = queue.getAttribute("value");
		int queueCount = Integer.parseInt(queueValue);
		if(countInVsPage == queueCount) {
			System.out.println("Statements in queue field value is displaying correctly");
			logInfo.pass("Statements in queue field value is displaying correctly");
		}else {
			System.out.println("Statements in queue field value is nots displaying correctly");
			logInfo.fail("Statements in queue field value is nots displaying correctly");
		}
	}
	
	public int selectVendors(String vendorCategoryData, String vendorsData, String currencyData) throws InterruptedException {
		int vendorCount = 0;
		JavascriptExecutor executor = (JavascriptExecutor)driver;
		// Select Vendor category
		String selectedCategory = vendorCategory.getAttribute("value");
		if(vendorCategoryData.isBlank()) {
			if(!selectedCategory.isBlank()) {
				vendorCategory.click();
				for(int i=0;i<categoryOptions.size();i++) {
					String text = categoryOptions.get(i).getText().trim();
					if(text.isBlank())
						categoryOptions.get(i).click();
				}
			}
		}
		else
			vendorCategory.sendKeys(vendorCategoryData);
		
		queue.click();
		
		// Select Currency & Vendors
		if(vendorsData.contains("/")) {
			String[] multipleVendors = vendorsData.split("/");
			String[] multipleCurrencies = currencyData.split(",");
			for (int j = 0; j < multipleVendors.length; j++) {
				// Select Items in the click to add list
				for(int i=0;i<vendorsList.size();i++) {
					WebElement currentVendor = vendorsList.get(i);
					String text = currentVendor.getText().trim();
					if(text.equals(multipleVendors[j])) {
						if(i==0)
							executor.executeScript("arguments[0].scrollIntoView(true);", vendorSublist);
						else
							executor.executeScript("arguments[0].scrollIntoView(true);", vendorList.get(i-1));
						WebElement parentTd = (WebElement)executor.executeScript("return arguments[0].parentNode;", currentVendor);
						WebElement parentTr = (WebElement)executor.executeScript("return arguments[0].parentNode;", parentTd);
						String trId = parentTr.getAttribute("id");
						
						// Select currency
						if(!multipleCurrencies[j].equals("null")) {
							driver.findElement(By.xpath("//tr[@id='"+trId+"']//button")).click();
							String vsPageWindow = driver.getWindowHandle();
							Set<String> allWindows = driver.getWindowHandles();
							for(String child: allWindows) {
								if(!vsPageWindow.equals(child)) {
									driver.switchTo().window(child);
									currencyDropdown.sendKeys(multipleCurrencies[j]);
									saveBtn.click();
								}
							}
							driver.switchTo().window(vsPageWindow);
						}else {
							driver.findElement(By.xpath("//tr[@id='"+trId+"']//img")).click();
						}
						vendorCount++;
					}
				}
			}
		}else {
			for(int i=0;i<vendorsList.size();i++) {
				WebElement currentVendor = vendorsList.get(i);
				String text = currentVendor.getText().trim();
				if(text.equals(vendorsData)) {
					if(i==0)
						executor.executeScript("arguments[0].scrollIntoView(true);", vendorSublist);
					else
						executor.executeScript("arguments[0].scrollIntoView(true);", vendorList.get(i-1));
					WebElement parentTd = (WebElement)executor.executeScript("return arguments[0].parentNode;", currentVendor);
					WebElement parentTr = (WebElement)executor.executeScript("return arguments[0].parentNode;", parentTd);
					String trId = parentTr.getAttribute("id");
					Thread.sleep(2000);
					// Select currency
					if(!currencyData.equals("null")) {
						driver.findElement(By.xpath("//tr[@id='"+trId+"']//button")).click();
						String vsPageWindow = driver.getWindowHandle();
						Set<String> allWindows = driver.getWindowHandles();
						for(String child: allWindows) {
							if(!vsPageWindow.equals(child)) {
								driver.switchTo().window(child);
								currencyDropdown.sendKeys(currencyData);
								saveBtn.click();
							}
						}
						driver.switchTo().window(vsPageWindow);
					}else {
						driver.findElement(By.xpath("//tr[@id='"+trId+"']//img")).click();
					}
					vendorCount++;
				}
			}
		}
		return vendorCount;
	}
	
	public void unmarkAllWithPrintAndEmail(ExtentTest logInfo) throws InterruptedException {
		JavascriptExecutor je = (JavascriptExecutor) driver;
		je.executeScript("window.scrollTo(0,0)");
		
		// Select template
		emailTemplate.sendKeys("Invoice Email Template");
		
		// Select Category
		vendorCategory.click();
		for(int i=0;i<categoryOptions.size();i++) {
			String text = categoryOptions.get(i).getText().trim();
			if(text.isBlank())
				categoryOptions.get(i).click();
		}
		
		// Click on Mark all button
		unMarkAllBtn.click();
		Thread.sleep(1500);
		
		// Click on print button
		printBtn.click();
		
		if(isAlertPresent()) {
			Thread.sleep(1000);
			Alert alert = driver.switchTo().alert();
			String alertMsg = alert.getText();
			System.out.println("Alert occured: "+alertMsg);
			logInfo.pass("Alert occured: "+alertMsg);
			alert.accept();
		}else {
			System.out.println("Alert not occured");
			logInfo.fail("Alert not occured");
		}
		
		// Click on email button
		emailBtn.click();
		
		if(isAlertPresent()) {
			Thread.sleep(1000);
			Alert alert = driver.switchTo().alert();
			String alertMsg = alert.getText();
			System.out.println("Alert occured: "+alertMsg);
			logInfo.pass("Alert occured: "+alertMsg);
			alert.accept();
		}else {
			System.out.println("Alert not occured");
			logInfo.fail("Alert not occured");
		}
	}
	
	public void markAllWithPrintAndEmail(ExtentTest logInfo) throws InterruptedException {
		JavascriptExecutor je = (JavascriptExecutor) driver;
		je.executeScript("window.scrollTo(0,0)");
		
		// Select template
		emailTemplate.sendKeys("Invoice Email Template");
		
		// Select Category
		vendorCategory.click();
		for(int i=0;i<categoryOptions.size();i++) {
			String text = categoryOptions.get(i).getText().trim();
			if(text.isBlank())
				categoryOptions.get(i).click();
		}
		
		// Click on Mark all button
		markAllBtn.click();
		Thread.sleep(1500);
		
		if(isAlertPresent()) {
			Thread.sleep(1000);
			Alert alert = driver.switchTo().alert();
			String alertMsg = alert.getText();
			System.out.println("Alert occured: "+alertMsg);
			alert.accept();
		}
		
		// Click on print button
		printBtn.click();
		
		String poPageWindow = driver.getWindowHandle();
		Set<String> allWindows = driver.getWindowHandles();
		for(String child: allWindows) {
			if(!poPageWindow.equals(child)) {
				driver.switchTo().window(child);
				System.out.println("Vendor statement print successfull");
				logInfo.pass("Vendor statement print successfull");
			}
		}
		if(driver.getTitle().isBlank()) {
			Thread.sleep(4000);
			driver.close();
			// Switch to PO Window
			driver.switchTo().window(poPageWindow);
		}
		// Click on Email button
		emailBtn.click();
		if(isAlertPresent()) {
			Thread.sleep(1000);
			Alert alert = driver.switchTo().alert();
			String alertMsg = alert.getText();
			if(alertMsg.contains("cannot")) {
				System.out.println("Alert occured: "+alertMsg);
				logInfo.fail("Alert occured: "+alertMsg);
			}else {
				System.out.println("Alert occured: "+alertMsg);
				logInfo.pass("Alert occured: "+alertMsg);
			}
			alert.accept();
		}
	}
	
	public void verifyVendorListAfterFilter(String category, String template, ExtentTest logInfo) throws InterruptedException {
		action = new Actions(driver);
		List<String> vendorListAfterFilterInVsPage = new ArrayList<>();
		List<String> vendorListAfterFilterInNs = new ArrayList<>();
		JavascriptExecutor je = (JavascriptExecutor) driver;
		je.executeScript("window.scrollTo(0,0)");
		// Get vendor names from Vendor Statement Page
		emailTemplate.sendKeys(template);
		if(category.isBlank()) {
			vendorCategory.click();
			for(int i=0;i<categoryOptions.size();i++) {
				String text = categoryOptions.get(i).getText().trim();
				if(text.isBlank())
					categoryOptions.get(i).click();
			}
		}
		else
			vendorCategory.sendKeys(category);
		queue.click();
		Thread.sleep(3000);
//		je.executeScript("window.scrollBy(0,document.body.scrollHeight)");
		for(int j=0;j<vendorList.size();j++) {
			action.moveToElement(vendorList.get(j)).build().perform();
			String text = vendorList.get(j).getText().trim();
			vendorListAfterFilterInVsPage.add(text);
		}
		
		// Get vendor names in Netsuite
		// Navigate to vendors list
		Thread.sleep(2000);
		eleAvailability(driver, By.xpath("//span[text()='Lists']"), 10);
		action.moveToElement(listsLink).build().perform();
		eleAvailability(driver, By.xpath("//span[text()='Relationships']"), 10);
		action.moveToElement(relationshipsLink).build().perform();
		eleAvailability(driver, By.xpath("//span[text()='Vendors']"), 10);
		action.keyDown(Keys.CONTROL).click(vendorsLink).keyUp(Keys.CONTROL).build().perform();
		
		String poPageWindow = driver.getWindowHandle();
		Set<String> allWindows = driver.getWindowHandles();
		for(String child: allWindows) {
			if(!poPageWindow.equals(child)) {
				driver.switchTo().window(child);
				
				// Change the view
				viewDropdown.sendKeys(prop.getProperty("search"));
				viewLabel.click();
				
				// Expand the filters section
				eleClickable(driver, By.xpath("//span[@class='ns-icon ns-filters-onoff-button']"), 5);
				String flag = filtersDiv.getAttribute("aria-expanded");
				if(flag.equals("false")) {
					action.moveToElement(filtersDiv).build().perform();
					filtersDiv.click();
				}
				
				// Select category
				if(!category.isEmpty()) {
					eleAvailability(driver, By.xpath("//input[@id='inpt_Vendor_CATEGORY1']"), 3);
					vendorCategoryInSearch.sendKeys(category);
					vendorCategoryLabelInSearch.click();
					Thread.sleep(2000);
				}
				else {
					eleAvailability(driver, By.xpath("//input[@id='inpt_Vendor_CATEGORY1']"), 3);
					vendorCategoryInSearch.sendKeys("- All -");
					vendorCategoryLabelInSearch.click();
					Thread.sleep(2000);
				}
				
				// Get vendors list in NS
				for(int j=0;j<vendorsInSearch.size();j++) {
					String text = vendorsInSearch.get(j).getText().trim();
					vendorListAfterFilterInNs.add(text);
				}
			}
		}
		
		driver.close();
		// Switch to PO Window
		driver.switchTo().window(poPageWindow);
		action.sendKeys(Keys.ESCAPE).build().perform();
		
		// Compare both the lists
		if(category.isBlank())
			category="NULL";
		
		boolean flag = true;
		if(vendorListAfterFilterInVsPage.size() == vendorListAfterFilterInNs.size()) {
			for (int i = 0; i < vendorListAfterFilterInVsPage.size(); i++) {
				if(!vendorListAfterFilterInVsPage.get(i).contains(vendorListAfterFilterInNs.get(i)))
					flag = false;
			}
		}
		else
			flag = false;
		
		if(flag) {
			System.out.println("Vendors are displayed correctly in the Vendors sublist based on the Vendor category = "+category);
			logInfo.pass("Vendors are displayed correctly in the Vendors sublist based on the Vendor category = "+category);
		}
		else {
			System.out.println("Vendors are not displayed correctly in the Vendors sublist based on the Vendor category = "+category);
			logInfo.fail("Vendors are not displayed correctly in the Vendors sublist based on the Vendor category = "+category);
		}
	}
	
	public void verifyAlertForInvalidDateRange(ExtentTest logInfo) throws InterruptedException {
		action.click(statementDate).keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).sendKeys(Keys.BACK_SPACE).sendKeys(statementDate, "9/28/2020").build().perform();
		queue.click();
		eleClickable(driver, By.xpath("//input[@id='custpage_startdate']"), 5);
		action.click(startDate).keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).sendKeys(Keys.BACK_SPACE).sendKeys(startDate, "11/6/2020").build().perform();
		queue.click();
		eleClickable(driver, By.xpath("//input[@id='inpt_custpage_emailtemplate1']"), 5);
		emailTemplate.sendKeys("79 Consulting");
		checkbox.click();
		printBtn.click();
		if(isAlertPresent()) {
			Thread.sleep(1000);
			Alert alert = driver.switchTo().alert();
			String alertMsg = alert.getText();
			System.out.println("Alert occured: "+alertMsg);
			logInfo.pass("Alert occured for invalid date range: "+alertMsg);
			alert.accept();
		}else {
			System.out.println("No alert occured");
			logInfo.fail("No alert occured");
		}
	}
	
	public void verifyStartDateFieldValidations(ExtentTest logInfo) throws InterruptedException {
		action = new Actions(driver);
		Thread.sleep(2000);
		// Verification of Start Date Field with alphabets
		action.click(startDate).keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).sendKeys(Keys.BACK_SPACE).sendKeys(startDate, "abc").build().perform();
		queue.click();
		if(isAlertPresent()) {
			Thread.sleep(1000);
			Alert alert = driver.switchTo().alert();
			String alertMsg = alert.getText();
			System.out.println("Alert occured: "+alertMsg);
			logInfo.pass("Alert occured for verification of Start Date Field with alphabets: "+alertMsg);
			alert.accept();
		}else {
			System.out.println("No alert occured");
			logInfo.fail("No alert occured");
		}
		
		// Verification of Start Date Field with numbers
		startDate.sendKeys("12");
		queue.click();
		if(isAlertPresent()) {
			Thread.sleep(1000);
			Alert alert = driver.switchTo().alert();
			String alertMsg = alert.getText();
			System.out.println("Alert occured: "+alertMsg);
			logInfo.pass("Alert occured for verification of Start Date Field with numbers: "+alertMsg);
			alert.accept();
		}else {
			System.out.println("No alert occured");
			logInfo.fail("No alert occured");
		}
		
		// Verification of Start Date Field with special characters
		startDate.sendKeys("@#$");
		queue.click();
		if(isAlertPresent()) {
			Thread.sleep(1000);
			Alert alert = driver.switchTo().alert();
			String alertMsg = alert.getText();
			System.out.println("Alert occured: "+alertMsg);
			logInfo.pass("Alert occured for verification of Start Date Field with special characters: "+alertMsg);
			alert.accept();
		}else {
			System.out.println("No alert occured");
			logInfo.fail("No alert occured");
		}
		
		// Verification of Start Date Field with alpha-numerics
		startDate.sendKeys("abc123");
		queue.click();
		if(isAlertPresent()) {
			Thread.sleep(1000);
			Alert alert = driver.switchTo().alert();
			String alertMsg = alert.getText();
			System.out.println("Alert occured: "+alertMsg);
			logInfo.pass("Alert occured for verification of Start Date Field with alpha-numerics: "+alertMsg);
			alert.accept();
		}else {
			System.out.println("No alert occured");
			logInfo.fail("No alert occured");
		}
		
		// Verification of Start Date Field with alphabets & special characters
		startDate.sendKeys("abc@#$");
		queue.click();
		if(isAlertPresent()) {
			Thread.sleep(1000);
			Alert alert = driver.switchTo().alert();
			String alertMsg = alert.getText();
			System.out.println("Alert occured: "+alertMsg);
			logInfo.pass("Alert occured for verification of Start Date Field with alphabets & special characters: "+alertMsg);
			alert.accept();
		}else {
			System.out.println("No alert occured");
			logInfo.fail("No alert occured");
		}
		
		// Verification of Start Date Field with numbers & special characters
		startDate.sendKeys("123@#$");
		queue.click();
		if(isAlertPresent()) {
			Thread.sleep(1000);
			Alert alert = driver.switchTo().alert();
			String alertMsg = alert.getText();
			System.out.println("Alert occured: "+alertMsg);
			logInfo.pass("Alert occured for verification of Start Date Field with numbers & special characters: "+alertMsg);
			alert.accept();
		}else {
			System.out.println("No alert occured");
			logInfo.fail("No alert occured");
		}
		
		// Verification of Start Date Field with value of the month
		startDate.sendKeys("13/13/2020");
		queue.click();
		Thread.sleep(3000);
		String date1 = startDate.getAttribute("value");
		if(date1.equals("12/13/2020")) {
			logInfo.pass("Value of the month adjusted to 12");
			System.out.println("Value of the month adjusted to 12");
		}else {
			logInfo.fail("Value of the month is not adjusted to 12");
			System.out.println("Value of the month is not adjusted to 12");
		}
		
		// Verification of Start Date Field with value of the day
		action.click(startDate).keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).sendKeys(Keys.BACK_SPACE).sendKeys(startDate, "1/40/2020").build().perform();
		queue.click();
		Thread.sleep(3000);
		String date2 = startDate.getAttribute("value");
		if(date2.equals("1/31/2020")) {
			logInfo.pass("Value of the date adjusted to 31");
			System.out.println("Value of the date adjusted to 31");
		}else {
			logInfo.fail("Value of the date is not adjusted to 31");
			System.out.println("Value of the date is not adjusted to 31");
		}
		
		// Verification of Start Date Field when no statement date is entered
		startDate.clear();
		startDate.sendKeys("");
		queue.click();
		Thread.sleep(3000);
		String date3 = startDate.getAttribute("value");
		if(date3.isBlank()) {
			logInfo.fail("Date is empty");
			System.out.println("Date is empty");
		}else {
			logInfo.pass("Start date is taken as current date");
			System.out.println("Start date is taken as current date");
		}
	}
	
	public void verifyStatementDateFieldValidations(ExtentTest logInfo) throws InterruptedException {
		action = new Actions(driver);
		// Verification of Statement Date Field with alphabets
		action.keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).sendKeys(Keys.BACK_SPACE).sendKeys(statementDate, "abc").build().perform();
		queue.click();
		if(isAlertPresent()) {
			Thread.sleep(1000);
			Alert alert = driver.switchTo().alert();
			String alertMsg = alert.getText();
			System.out.println("Alert occured: "+alertMsg);
			logInfo.pass("Alert occured for verification of Statement Date Field with alphabets: "+alertMsg);
			alert.accept();
		}else {
			System.out.println("No alert occured");
			logInfo.fail("No alert occured");
		}
		
		// Verification of Statement Date Field with numbers
		statementDate.sendKeys("12");
		queue.click();
		if(isAlertPresent()) {
			Thread.sleep(1000);
			Alert alert = driver.switchTo().alert();
			String alertMsg = alert.getText();
			System.out.println("Alert occured: "+alertMsg);
			logInfo.pass("Alert occured for verification of Statement Date Field with numbers: "+alertMsg);
			alert.accept();
		}else {
			System.out.println("No alert occured");
			logInfo.fail("No alert occured");
		}
		
		// Verification of Statement Date Field with special characters
		statementDate.sendKeys("@#$");
		queue.click();
		if(isAlertPresent()) {
			Thread.sleep(1000);
			Alert alert = driver.switchTo().alert();
			String alertMsg = alert.getText();
			System.out.println("Alert occured: "+alertMsg);
			logInfo.pass("Alert occured for verification of Statement Date Field with special characters: "+alertMsg);
			alert.accept();
		}else {
			System.out.println("No alert occured");
			logInfo.fail("No alert occured");
		}
		
		// Verification of Statement Date Field with alpha-numerics
		statementDate.sendKeys("abc123");
		queue.click();
		if(isAlertPresent()) {
			Thread.sleep(1000);
			Alert alert = driver.switchTo().alert();
			String alertMsg = alert.getText();
			System.out.println("Alert occured: "+alertMsg);
			logInfo.pass("Alert occured for verification of Statement Date Field with alpha-numerics: "+alertMsg);
			alert.accept();
		}else {
			System.out.println("No alert occured");
			logInfo.fail("No alert occured");
		}
		
		// Verification of Statement Date Field with alphabets & special characters
		statementDate.sendKeys("abc@#$");
		queue.click();
		if(isAlertPresent()) {
			Thread.sleep(1000);
			Alert alert = driver.switchTo().alert();
			String alertMsg = alert.getText();
			System.out.println("Alert occured: "+alertMsg);
			logInfo.pass("Alert occured for verification of Statement Date Field with alphabets & special characters: "+alertMsg);
			alert.accept();
		}else {
			System.out.println("No alert occured");
			logInfo.fail("No alert occured");
		}
		
		// Verification of Statement Date Field with numbers & special characters
		statementDate.sendKeys("123@#$");
		queue.click();
		if(isAlertPresent()) {
			Thread.sleep(1000);
			Alert alert = driver.switchTo().alert();
			String alertMsg = alert.getText();
			System.out.println("Alert occured: "+alertMsg);
			logInfo.pass("Alert occured for verification of Statement Date Field with numbers & special characters: "+alertMsg);
			alert.accept();
		}else {
			System.out.println("No alert occured");
			logInfo.fail("No alert occured");
		}
		
		// Verification of Statement Date Field with value of the month
		statementDate.sendKeys("13/13/2020");
		queue.click();
		Thread.sleep(3000);
		String date1 = statementDate.getAttribute("value");
		if(date1.equals("12/13/2020")) {
			logInfo.pass("Value of the month adjusted to 12");
			System.out.println("Value of the month adjusted to 12");
		}else {
			logInfo.fail("Value of the month is not adjusted to 12");
			System.out.println("Value of the month is not adjusted to 12");
		}
		
		// Verification of Statement Date Field with value of the day
		action.click(statementDate).keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).sendKeys(Keys.BACK_SPACE).sendKeys(statementDate, "1/40/2020").build().perform();
		queue.click();
		Thread.sleep(3000);
		String date2 = statementDate.getAttribute("value");
		if(date2.equals("1/31/2020")) {
			logInfo.pass("Value of the date adjusted to 31");
			System.out.println("Value of the date adjusted to 31");
		}else {
			logInfo.fail("Value of the date is not adjusted to 31");
			System.out.println("Value of the date is not adjusted to 31");
		}
		
		// Verification of Statement Date Field when no statement date is entered
		statementDate.clear();
		statementDate.sendKeys("");
		queue.click();
		Thread.sleep(3000);
		String date3 = statementDate.getAttribute("value");
		if(date3.isBlank()) {
			logInfo.fail("Date is empty");
			System.out.println("Date is empty");
		}else {
			logInfo.pass("Statement date is taken as current date");
			System.out.println("Statement date is taken as current date");
		}
	}
}
