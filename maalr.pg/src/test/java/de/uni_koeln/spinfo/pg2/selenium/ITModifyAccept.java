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

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import com.thoughtworks.selenium.Selenium;

@Ignore
public class ITModifyAccept {
	
	private Selenium selenium;
	
	@Rule public RCScreenshotGenerator generator = new RCScreenshotGenerator();
	
	@Before
	public void setUp() throws Exception {
		selenium = SeleniumHelper.setUp();
	}
	
	@After
	public void tearDown() throws Exception {
		SeleniumHelper.tearDown(selenium, generator);
	}

	@Test
	public void testITModifyAccept() throws Exception {
		selenium.open("/");
		selenium.click("css=span");
		selenium.waitForPageToLoad("30000");
		selenium.type("css=input.gwt-TextBox", "adler");
		selenium.click("css=a.btn > span");
		selenium.mouseOver("//div[@id='content']/div/div/div[2]/div/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr[5]/td/div");
		selenium.click("//button[@type='button']");
		selenium.type("id=maalr_editor_DStichwort", "Adlerhase");
		selenium.type("id=maalr_editor_RStichwort", "has aquilin");
		selenium.select("id=maalr_editor_DGenus", "label=m");
		selenium.type("css=div.well > table > tbody > tr > td > textarea.gwt-TextArea", "some test comment");
		selenium.click("css=a.btn.btn-primary > span");
		selenium.click("id=gwt-uid-20");
		assertTrue(selenium.isTextPresent("Adlerhase"));
		selenium.click("link=login");
		selenium.waitForPageToLoad("30000");
		selenium.type("id=uname", "editor");
		selenium.type("id=upwd", "editor");
		selenium.click("id=internal_login");
		selenium.waitForPageToLoad("30000");
		selenium.click("//div[@id='main-content']/div/div/div/div/table/tbody/tr[2]/td/div/div/div[2]/div/div[3]/div/div[2]/div/div/table/tbody/tr/td[4]/div/div/div/div[2]");
		selenium.click("//div[@id='main-content']/div/div/div/div[2]/div[2]/table/tbody/tr[5]/td/table/tbody/tr/td[2]/div/a[2]/span");
		selenium.click("//div[@id='main-content']/div/div/div/div/table/tbody/tr[2]/td/div/div/div[2]/div/div[3]/div/div[2]/div/div/table/tbody/tr/td[4]/div/div/div/div");
		selenium.click("//div[@id='gwt-uid-1']/div/div/ul/li[3]/a/span");
		selenium.waitForPageToLoad("30000");
		selenium.click("css=span");
		selenium.waitForPageToLoad("30000");
		selenium.type("css=input.gwt-TextBox", "adlerhase");
		selenium.click("css=a.btn > span");
		assertTrue(selenium.isTextPresent("Adlerhase"));
	}
}
