# MailboxTestCase_Example
Example of using Selenium Grid, JUnit and Allure framework.
This project implements Page Object test automation pattern.
Your Selenium GRID server should have Chrome on some of it's nodes.
All settings to configure this progect you can find in robot-settings.xml in src/test/resources directory.
Also, you must use jdk 1.8 to build this project, otherwise allure won't make report.
Use "$mvn test" to compile and run test and "$mvn allure:serve" to make and see html report.
