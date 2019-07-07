package pageobjects;

import java.util.LinkedList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import io.qameta.allure.Step;

/*
 * Inbox mail page
 */
public class InboxPage extends Page {
	
	private By searchInputLocator = By.xpath(".//*[@id=\"gs_lc50\"]/input[1]");
	private By tableWithLettersBodyLocator = By.xpath(".//div[5]/div[1]/div[1]/table/tbody");
	private By lettersLocator = By.xpath(".//div[5]/div[1]/div[1]/table/tbody/tr");
	private By senderAdressLocator = By.xpath(".//td[5]/div[2]/span/span");
	private By writeBtnLocator = By.cssSelector(".T-I-KE");
	private By toLocator = By.xpath(".//textarea[@name='to']");
	private By subjectLocator = By.xpath(".//input[@name='subjectbox']");
	private By letterBodyLocator = By.xpath(".//div[@class = 'Am Al editable LW-avf']");
	private By dialogWrapperLocator = By.xpath(".//div[@role='dialog']");
	private By sendBtnLocator = By.className("dC");
	private By resultBannerLocator = By.xpath(".//span[@class='bAq']");
	private By escapeLinkLocator = By.xpath(".//span[@class='ag a8k']");
	
	/*
	 * constructor gets link to browser instance and sets up implicit wait property and actions
	 */
	public InboxPage(WebDriver browser) {
		super(browser);
	}
	
	/*
	 * Performs search in messages by sender address with native gmail search 
	 */
	@Step("Search messages by sender= {0}.")
	public InboxPage doSearchBySender(String senderEmail) {
		
		// wait until search input field becomes visible and search subject
		wait.until(ExpectedConditions.visibilityOfElementLocated(searchInputLocator));
		WebElement searchInput = browser.findElement(searchInputLocator);
		searchInput.sendKeys(senderEmail);
		searchInput.sendKeys(Keys.ENTER);
		return this;
	}
	
	/*
	 * looks through list of messages and counts only ones with particular sender address
	 */
	@Step("Counts messages from {0} in message list from page.")
	public int countMessagesFound(String senderEmail) {
		
		// get list of rows, representing letters
		List<WebElement> lettersList = new LinkedList<WebElement>();
		wait.until(ExpectedConditions.visibilityOfElementLocated(tableWithLettersBodyLocator));
		lettersList = browser.findElements(lettersLocator);
		
		// count messages
		int count = 0;
		for(WebElement letter : lettersList) {
			String from = letter.findElement(senderAdressLocator).getAttribute("email");
			if (from.equals(senderEmail)) {count++;}
		}
		return count;
	}
	
	/*
	 * simply sends a message
	 */
	@Step("Send {1} message to {0}.")
	public String sendLetter(String emailTo, String theme, String text) {
		WebElement writeBtn;
		WebElement to;
		WebElement subject;
		WebElement letterBody;
		WebElement sendBtn;
		WebElement dialogWrapper;
		
		// create letter
		wait.until(ExpectedConditions.visibilityOfElementLocated(writeBtnLocator));
		writeBtn = browser.findElement(writeBtnLocator);
		writeBtn.click();
		
		// waiting on building of message composing dialog and control elements initialization
		wait.until(ExpectedConditions.visibilityOfElementLocated(dialogWrapperLocator));
		dialogWrapper = browser.findElement(dialogWrapperLocator);
		to = browser.findElement(toLocator);
		subject = browser.findElement(subjectLocator);
		letterBody = browser.findElement(letterBodyLocator);
		sendBtn = dialogWrapper.findElement(sendBtnLocator);
		
		// fill in letter and send it
		to.sendKeys(emailTo);
		subject.sendKeys(theme);
		letterBody.sendKeys(text);
		wait.until(ExpectedConditions.elementToBeClickable(sendBtn));
		action.moveToElement(sendBtn).click().perform();
		
		// catching the results banner...
		wait.until(ExpectedConditions.visibilityOfElementLocated(resultBannerLocator));
		//need to wait until banner text changes to "Письмо отправлено."
		wait.until(ExpectedConditions.invisibilityOfElementLocated(escapeLinkLocator));
		WebElement resultBanner = browser.findElement(resultBannerLocator);
		return resultBanner.getText();
	}
	
}
