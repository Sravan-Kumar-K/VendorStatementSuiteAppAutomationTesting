Feature: Automation Testing on Vendor Statement Suite App

Scenario: Test scenario for Statement date & Start date field validations
Given User is in Vendor Statement page
Then verify all the field validations
Then Verify Start date exceed Statement Date error

#Scenario: Test scenario for Vendor List verifications
#Given User is in Vendor Statement page
#Then Verify the vendors in the vendor sublist based on vendor category using excel data at "C:\Users\Sravan Kumar\Desktop\Vendor Statement Data.xlsx,Vendor List Validations"
#Then Verify Mark All functionality with Print & Email functionality
#Then Verify Unmark All functionality with Print & Email functionality
#
#Scenario: Test scenario for verifying the Print functionality
#Given User is in Vendor Statement page
#Then Select the vendors and print statement using excel data at "C:\Users\Sravan Kumar\Desktop\Vendor Statement Data.xlsx,Print Functionality"
#
#Scenario: Verification of Email functionality
#Given User is in Vendor Statement page
#Then Verification of No Email template error
#Then Verification of No Email Receipients error
#Then Verification of Email to field with invalid emailId
#Then Verification of Email functionality with valid data using excel data at "C:\Users\Sravan Kumar\Desktop\Vendor Statement Data.xlsx,Email Functionality"