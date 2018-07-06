package com.epam.atm.module5;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.epam.atm.module5.MailRuTestBase;

public class MailRuTest extends MailRuTestBase {
	private static final String MAILRU_LOGIN = "vra_atmmodule5";
	private static final String MAILRU_PASSWORD = "123456789_Vra";
	private static final String MAILTO_ADDRESS = "vra_atmmodule5@mail.ru";

	private static final String SUBJECT = "TestSubject" + currentKey;
	private static final String TEXT_BODY = "TestTextBody" + currentKey;

	@Test(description = "Login to mail.ru")
	public void mailRuLoginTest() throws InterruptedException {

		WebElement mailSearchLogin = driver.findElement(By.id("mailbox:login"));
		highlightElement(mailSearchLogin);
		mailSearchLogin.clear();
		mailSearchLogin.sendKeys(MAILRU_LOGIN);

		WebElement mailSearchPassword = driver.findElement(By
				.id("mailbox:password"));
		highlightElement(mailSearchPassword);
		mailSearchPassword.clear();
		mailSearchPassword.sendKeys(MAILRU_PASSWORD);

		WebElement mailSearchSubmit = driver.findElement(By
				.id("mailbox:submit"));
		highlightElement(mailSearchSubmit);
		mailSearchSubmit.click();

		new WebDriverWait(driver, 20).until(ExpectedConditions
				.visibilityOfElementLocated(By
						.xpath("//*[@id='PH_user-email']")));

		WebElement mailCheckAutentification = driver.findElement(By
				.xpath("//*[@id='PH_user-email']"));
		highlightElement(mailCheckAutentification);
		Assert.assertEquals(mailCheckAutentification.getText(),
				"vra_atmmodule5@mail.ru");

	}

	@Test(description = "Mail creation", dependsOnMethods = { "mailRuLoginTest" })
	public void mailRuMailCreationTest() throws InterruptedException {
		List<WebElement> mailCreationBtn = driver.findElements(By
				.xpath("//a[(@data-name = 'compose')]"));
		highlightElement(mailCreationBtn.get(0));
		mailCreationBtn.get(0).click();

		new WebDriverWait(driver, 20).until(ExpectedConditions
				.visibilityOfElementLocated(By
						.cssSelector("textarea[data-original-name = 'To']")));

		WebElement mailToAddress = driver.findElement(By
				.cssSelector("textarea[data-original-name = 'To']"));
		highlightElement(mailToAddress);
		mailToAddress.clear();
		mailToAddress.sendKeys(MAILTO_ADDRESS);

		WebElement mailSubject = driver.findElement(By.name("Subject"));
		highlightElement(mailSubject);
		mailSubject.clear();
		mailSubject.sendKeys(SUBJECT);

		WebElement mailBody = driver.findElement(By
				.xpath("//iframe[starts-with(@id,'toolkit')]"));
		driver.switchTo().frame(mailBody);
		driver.findElement(By.id("tinymce")).clear();
		driver.findElement(By.id("tinymce")).sendKeys(TEXT_BODY);

		driver.switchTo().defaultContent();
		List<WebElement> saveDraft = driver.findElements(By
				.cssSelector("[data-name='saveDraft']"));
		highlightElement(saveDraft.get(0));
		saveDraft.get(0).click();

		new WebDriverWait(driver, 20).until(ExpectedConditions
				.visibilityOfElementLocated(By
						.cssSelector("[data-mnemo='saveStatus']")));

		driver.findElement(
				By.xpath("//*[contains(@class,'ico_folder_drafts')]")).click();

		List<WebElement> draftMails = driver.findElements(By
				.xpath("//*[contains(@href,'https://e.mail.ru/compose/')]"));
		WebElement draftMail = draftMails.get(0);

		Assert.assertEquals(draftMail.getAttribute("data-subject"), SUBJECT);

		draftMail.click();

		WebElement checkMailTo = driver.findElement(By
				.xpath("//*[@id='compose_to']"));
		Assert.assertEquals(checkMailTo.getAttribute("value"), MAILTO_ADDRESS
				+ ",");

		boolean checkMailBody = driver.findElement(
				By.xpath("//*[text()= '" + SUBJECT + "']")).isEnabled();

		Assert.assertTrue(checkMailBody,
				"Required text body has not been found");
	}

	@Test(description = "Mail sending", dependsOnMethods = { "mailRuMailCreationTest" })
	public void mailRuMailSendingTest() throws InterruptedException {
		WebElement mailSendBtn = driver.findElement(By
				.cssSelector("[data-name='send']"));
		mailSendBtn.click();

		new WebDriverWait(driver, 20).until(ExpectedConditions
				.visibilityOfElementLocated(By
						.cssSelector("[class='message-sent__title']")));

		driver.findElement(
				By.xpath("//*[contains(@class,'ico_folder_drafts')]")).click();
		
		//Thread.sleep(5000);
		driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);
		
		boolean isEmailInDraft = isElementPresent(By.xpath("//*[text()='"
				+ SUBJECT + "']"));
		Assert.assertFalse(isEmailInDraft, "Email exists in Draft folder");

		WebElement mailSendingLink = driver.findElement(By
				.xpath("//*[contains(@class,'ico_folder_send')]"));
		mailSendingLink.click();

		new WebDriverWait(driver, 20).until(ExpectedConditions
				.visibilityOfElementLocated(By.xpath("//*[text()='" + SUBJECT
						+ "']")));

		boolean isEmailInSend = isElementPresent(By.xpath("//*[text()='"
				+ SUBJECT + "']"));
		Assert.assertTrue(isEmailInSend, "Email does not exist in Send folder");

		driver.findElement(By.id("PH_logoutLink")).click();

	}

}
