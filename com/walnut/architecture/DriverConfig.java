/*
  Author: Ssire Kumar
  This Class is mainly to configure the Selenium Webdriver with java
  
*/
package com.walnut.architecture;

import java.io.File;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.walnut.regular.CommonFunctions;

public class DriverConfig {
	
	private static WebDriver _driver = null;
	private static WebDriverWait _wait = null;
	private static Properties _props = CommonFunctions.loadPropertyFile("PropertieFiles/Environment.properties");
	private static String _globalTimeOUt = CommonFunctions.getValueFromProperty(_props, "GLOBAL_TIMEOUT");
	private static String _appUrl = CommonFunctions.getValueFromProperty(_props, "APP_URL");
	
	/**
	 * Function to get WebDriver object
	 * @author Ssire Kumar Puttagunta
	 * @return WebDriver object if set else null*/
	public static WebDriver getDriver() {
		try {
			if (_driver != null) {
				return _driver;
			} else {
				System.err.println("First set the Browser driver before getting it");
				return _driver;
			}
		} catch (Exception e) {
			System.err.println(e.getStackTrace());
			System.exit(0);
			return _driver;
		}
	}
    
	/**
	 * Function to set WebDriver object
	 * @author Ssire Kumar Puttagunta
	 * @param String - name of browser - (ie/firefox/chrome)*/
	public static void setDriver(String browser) {
		if (browser.equalsIgnoreCase("ie")) {
			if (CommonFunctions.is64Bit()) {
				File _file = new File("BrowserDrivers/IEDriverServer_x64_.exe");
				System.setProperty("webdriver.ie.driver", _file.getAbsolutePath());
	 			DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
    			capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
				_driver = new InternetExplorerDriver(capabilities);
				System.out.println(browser.toUpperCase()+ " is initiated");
			} else {
				File _file = new File("BrowserDrivers/IEDriverServer_Win32_.exe");
				System.setProperty("webdriver.ie.driver", _file.getAbsolutePath());
	 			DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
    			capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
				_driver = new InternetExplorerDriver(capabilities);
				System.out.println(browser.toUpperCase()+ " is initiated");
			}
		} else if (browser.equalsIgnoreCase("chrome")) {
			File _file = new File("BrowserDrivers/chromedriver.exe");
			System.setProperty("webdriver.chrome.driver", _file.getAbsolutePath());
			DesiredCapabilities desiredCapabilities= DesiredCapabilities.chrome();
			ChromeOptions options=new ChromeOptions();
			desiredCapabilities.setCapability(ChromeOptions.CAPABILITY, options);
			_driver = new ChromeDriver(desiredCapabilities);
			System.out.println(browser.toUpperCase()+ " is initiated");
		} else if (browser.equalsIgnoreCase("firefox")) {
			_driver = new FirefoxDriver();
			System.out.println(browser.toUpperCase()+ " is initiated");
		} else {
			System.err.println(browser.toUpperCase()+ " is not supported in framework");
			System.exit(0);
		}
		if (_driver != null && _appUrl != null) {
			_driver.get(_appUrl);
			_driver.manage().timeouts().implicitlyWait(Long.parseLong(_globalTimeOUt),TimeUnit.SECONDS);
			_driver.manage().window().maximize();
		}
	}
	
	/**
	 * Function to destroy WebDriver object set
	 * @author Ssire Kumar Puttagunta
	 */
	public static void destroyDriver() {
		_driver.quit();
		_driver = null;
	}
	
	/**
	 * Function to wait till the element is present as per it's locator
	 * @author Ssire Kumar Puttagunta
	 * @param Locator - for the element
	 * @return WebElement element if operation is successfully performed else null*/
	public static WebElement waitTillElementPresent(By property) {
		try {
			WebDriverWait _wait = DriverConfig.getWebDriverWait();
			return _wait.until(ExpectedConditions.presenceOfElementLocated(property));
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return null;
	}
	
	/**
	 * Function to set default web driver wait (= GLOBAL_TIMEOUT)
	 * @author Ssire Kumar Puttagunta*/
	public static void setDefaultWebDriverWait() {
		_wait = new WebDriverWait(DriverConfig.getDriver(),Long.parseLong(_globalTimeOUt));
	}

	/**
	 * Function to set custom web driver wait
	 * @author Ssire Kumar Puttagunta
	 * @param int - wait timeout value*/
	public static void setWebDriverWait(int waitTimeInSeconds) {
		_wait = new WebDriverWait(DriverConfig.getDriver(), waitTimeInSeconds);
	}

	/**
	 * Function to get web driver wait object
	 * @author Ssire Kumar Puttagunta
	 * WebDriverWait object*/
	public static WebDriverWait getWebDriverWait() {
		setDefaultWebDriverWait();
		return _wait;
	}
    
}

