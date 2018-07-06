package com.epam.atm.module5;


import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public class MailRuTestBase {
	protected WebDriver driver;
	
	private static final String MAILRU_URL = "https://mail.ru/";
	protected static long currentKey = System.currentTimeMillis();
	
	@BeforeClass(description = "Start browser")
	public void startBrowser(){
		System.setProperty("webdriver.chrome.driver", "D:\\_webdriver\\chromedriver\\chromedriver.exe");
		ChromeOptions options = new ChromeOptions();
		options.addArguments("start-maximized");
		driver = new ChromeDriver(options);
		driver.get(MAILRU_URL);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
	}
	
	@AfterClass(description = "Stop Browser")
	public void stopBrowser() {
		driver.quit();
		System.out.println("Browser was successfully quited.");
	}
	
	public void highlightElement(WebElement element) throws InterruptedException {
		String bg = element.getCssValue("backgroundColor");
		JavascriptExecutor js = ((JavascriptExecutor) driver);
		js.executeScript("arguments[0].style.backgroundColor = '" + "yellow" + "'", element);
		// take screenshot here
		// or just pause/blink
		Thread.sleep(500);
		js.executeScript("arguments[0].style.backgroundColor = '" + bg + "'", element);

	}
	
	public boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

	
	

	

}
