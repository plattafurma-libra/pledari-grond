/*******************************************************************************
 * Copyright 2013 Sprachliche Informationsverarbeitung, University of Cologne
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uni_koeln.spinfo.pg2.selenium;

import static org.junit.Assert.fail;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.slf4j.LoggerFactory;

@Ignore
public class ITIndexPage {
	private WebDriver driver;
	private String baseUrl;
	private StringBuffer verificationErrors = new StringBuffer();
	
    @Rule public ScreenshotGenerator generator = new ScreenshotGenerator();
	
	@Before
	public void setUp() throws Exception {
		
		/*
		 * 
		 * To create new Tests: Use Selenium IDE (http://seleniumhq.org/projects/ide/)
		 * TODO: Continue documentation
		 * 
		 */
		driver = new FirefoxDriver();
		baseUrl = "http://localhost:9999/";
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		generator.setDriver(driver, true);
	}

	@Test
	public void testPlugInPage() throws Exception {
		driver.get(baseUrl + "/");
		driver.findElement(By.linkText("plug-in")).click();
	}
	
	@Test
	public void testLoginPage() throws Exception {
		driver.get(baseUrl + "/");
		driver.findElement(By.linkText("login")).click();
	}
	
	@Test
	public void testSearchPage() throws Exception {
		driver.get(baseUrl + "/");
		driver.findElement(By.linkText("tschertgar")).click();
	}
	
	@Test
	public void testInfoPage() throws Exception {
		driver.get(baseUrl + "/");
		driver.findElement(By.linkText("info")).click();
	}
	
	@Test
	public void testHelpPage() throws Exception {
		driver.get(baseUrl + "/");
		driver.findElement(By.linkText("hilfe")).click();
	}
	
	@Test
	public void testIndexPage() throws Exception {
		driver.get(baseUrl + "/");
		driver.findElement(By.linkText("glista")).click();
	}
	
	@Test
	public void testAbbreviationsPage() throws Exception {
		driver.get(baseUrl + "/");
		driver.findElement(By.linkText("abreviaziuns")).click();
	}
	
	@Test
	public void testMaalrPage() throws Exception {
		driver.get(baseUrl + "/");
		driver.findElement(By.linkText("Maalr")).click();
	}
	
	@Test
	public void testMaalrHomePage() throws Exception {
		driver.get(baseUrl + "/");
		driver.findElement(By.linkText("University of Cologne")).click();
	}

	@After
	public void tearDown() throws Exception {
		LoggerFactory.getLogger(getClass()).info("Teardown, quitting driver...");
		generator.createSnapshot();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}

	private boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}
}
