package tests;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import io.qameta.allure.Step;
import io.qameta.allure.Story;
import pageobjects.LoginPage;
import pageobjects.InboxPage;

public class TestSuiteOne {
	
	private WebDriver browser;
	private String login;
	private String password;
	private String reportTo;
	private String reportTheme;
	private String addressToSearch;
	private URL selenGridURL;
	
	@Before
	public void setUp() throws MalformedURLException {
		
		//look for settings and load them up
		loadSettings("src/test/resources/robot-settings.xml");
		//set up selenium
		ChromeOptions options = new ChromeOptions();
		browser = new RemoteWebDriver(selenGridURL, options);
		openPage("https://mail.google.com");
	}
	
	@After
	public void tearDown() {
		browser.close();
	}
	
	/*
	 * Main sequence
	 */
	@Story("Search inbox messages by sender adress, count them and write a letter with count result.")
	@Test
	public void searchAndSendTest() {
		int numberOfLetters;
		String sequenceResult;
		String text = "Quantity of letters found from '" + addressToSearch + "': ";
		
		// logging in and getting inbox mail page
		LoginPage loginPage = new LoginPage(browser);
		InboxPage mail = loginPage.doLogin(login, password);
		
		// do search and send report
		numberOfLetters = mail.doSearchBySender(addressToSearch).countMessagesFound(addressToSearch);
		sequenceResult = mail.sendLetter(reportTo, reportTheme, text + numberOfLetters);
		
		Assert.assertTrue(sequenceResult.equals("Письмо отправлено."));
	}
	
	/*
	 * Opens page needed
	 */
	@Step("Open {0} page.")
	private void openPage(String url) {
		browser.get(url);
	}
	
	/*
	 * Looks through .xml file for specific tags and collects information, that needed to control mail account,
	 * to search particular messages and to send reports to someone.
	 */
	@Step("Load settings from file.")
	private void loadSettings(String settingsPath) {
		
		try {
			//finding settings file
			File settingsFile = new File(settingsPath);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			//parsing DOM
			Element settings = dBuilder.parse(settingsFile).getDocumentElement();
			Element robotEmail = (Element) settings.getElementsByTagName("robot-email").item(0);
			Element report = (Element) settings.getElementsByTagName("report").item(0);
			// setting variables with needed information
			login = robotEmail.getElementsByTagName("login").item(0).getTextContent();
			password = robotEmail.getElementsByTagName("password").item(0).getTextContent();
			reportTo = report.getAttribute("reportTo");
			reportTheme = report.getAttribute("reportTheme");
			addressToSearch = settings.getElementsByTagName("addressToSearch").item(0).getTextContent();
			selenGridURL = new URL(settings.getElementsByTagName("selenGridURL").item(0).getTextContent());
		} 
		catch (FileNotFoundException e) {
			System.out.println("Test settings file not found!");
			e.printStackTrace();
		}
		catch (IOException ioEx) {
			System.out.println("Test settings file corrupted!");
			ioEx.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}

}
