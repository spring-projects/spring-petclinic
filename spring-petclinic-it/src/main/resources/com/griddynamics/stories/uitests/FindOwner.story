Meta:
@smoke


Scenario: Check Find Owner Page

Given customer is on Pet Clinic Home Page
When customer clicks Find owners menu
Then Pet Clinic Find Owners Page is opened

When customer clicks Find Owner button
Then Owners table is displayed on page
