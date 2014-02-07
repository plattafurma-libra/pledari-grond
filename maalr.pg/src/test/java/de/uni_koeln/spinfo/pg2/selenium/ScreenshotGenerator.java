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

import java.io.File;
import java.io.FileOutputStream;

import org.junit.rules.TestName;
import org.junit.runner.Description;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScreenshotGenerator extends TestName {
	
	 private Logger logger = LoggerFactory.getLogger(getClass());

	private WebDriver driver;

	private boolean closeAfterScreenshot;

	private boolean failed;

	@Override
	protected void succeeded(Description description) {
		failed = false;
		super.succeeded(description);
	}


	@Override
	protected void failed(Throwable e, Description description) {
		failed = true;
		super.failed(e, description);
	}


	private void captureScreenshot(String fileName, boolean hasFailed) {
        try {
        	File base = new File("target/failsafe-reports/selenium/");
        	File all = new File(base, "all");
        	all.mkdirs();
        	File failed = new File(base, "failed");
        	failed.mkdirs();
        	logger.info("Accessing driver..." + getMethodName());
        	byte[] screenShot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            FileOutputStream out = new FileOutputStream(new File(all,fileName + ".png"));
            out.write(screenShot);
            out.close();
            if(hasFailed) {
            	File screenShotFile = new File(failed,fileName + ".png");
            	out = new FileOutputStream(screenShotFile);
                out.write(screenShot);
                out.close();
                logger.warn("Test failed - screenshot saved as " + screenShotFile.getAbsolutePath());
            }
        } catch (Exception e) {
        	logger.error("Failed to generate screenshot", e);
        } finally {
        	if(closeAfterScreenshot) {
        		driver.close();
        	}
        }
    }

	public void setDriver(WebDriver driver, boolean closeAfterScreenshot) {
		this.driver = driver;
		this.closeAfterScreenshot = closeAfterScreenshot;
	}

	public void createSnapshot() {
		captureScreenshot(getMethodName(), failed);
	};



}
