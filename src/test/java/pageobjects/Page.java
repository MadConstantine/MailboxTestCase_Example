package pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

/*
 * Abstract superclass for every page
 */
public abstract class Page {
	/*
	 * attributes of page
	 */
	public final WebDriver browser;
	protected Actions action;
	protected WebDriverWait wait;
	
	/*
	 * Page should receive a link to some WebDriver browser instance
	 */
	public Page(WebDriver browser) {
		this.browser = browser;
		action = new Actions(this.browser);
		wait = new WebDriverWait(this.browser, 15);
	}
}
