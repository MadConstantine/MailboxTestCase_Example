package pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import io.qameta.allure.Step;

/*
 * login to mail page
 */
public class LoginPage extends Page {
	
	private By idLocator = By.name("identifier");
	private By proceedBtnLocator = By.id("identifierNext");
	private By passLocator = By.xpath(".//input[@type='password']");
	private By submitBtnLocator = By.id("passwordNext");
	
	public LoginPage(WebDriver browser) {
		super(browser);
	}
	
	@Step("Log in mailbox.")
	public InboxPage doLogin(String login, String password) {
		
		// fill in user id
		WebElement id = browser.findElement(idLocator);
		id.sendKeys(login);
		// click on proceed button
		WebElement proceedBtn = browser.findElement(proceedBtnLocator);
		proceedBtn.click();
		// wait until animation finishes
		wait.until(ExpectedConditions.visibilityOfElementLocated(passLocator));
		//fill in password
		WebElement pass = browser.findElement(passLocator);
		pass.click();
		pass.sendKeys(password);
		// click on submit button
		WebElement submitBtn = browser.findElement(submitBtnLocator);
		submitBtn.click();
		
		return new InboxPage(browser);
	}
	
}
