package com.sn.core.buildDriver;


import com.sn.qa.util.PropertyFileReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class AppManager {
    public static WebDriver wd;
    private static String browser;
    static PropertyFileReader reader = new PropertyFileReader("config.properties", "config/");


    public AppManager() {
    }

    public AppManager(String browser) {
        this.browser = browser;
    }


    public static WebDriver getWebDriver() {
        if (wd == null) {
            if ("".equals(reader.get("selenium.server"))) {
            if (browser.equalsIgnoreCase("IE")) {
                System.setProperty("webdriver.ie.driver","./IEDriverServer.exe");
                DesiredCapabilities caps = DesiredCapabilities.internetExplorer();
                caps.setCapability("ignoreProtectedModeSettings", true);
                caps.setCapability("ie.ensureCleanSession", true);
                caps.setCapability("enableElementCacheCleanup", true);
                caps.setCapability("ignoreZoomSetting", true);
                caps.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);
                wd = new InternetExplorerDriver(caps);
                wd.manage().deleteAllCookies();
            } else if (browser.equalsIgnoreCase("EDGE")) {
                System.setProperty("webdriver.edge.driver","./msedgedriver.exe");
                EdgeOptions edgeOptions = new EdgeOptions();
                wd = new EdgeDriver(edgeOptions);
            } else if (browser.equalsIgnoreCase("CHROME")) {
                System.setProperty("webdriver.Chrome", "./chromedriver");
//                System.setProperty("webdriver.Chrome", "./chromedriver.exe");
//                Map<String, Object> chromePrefs = new HashMap<String, Object>();
//                chromePrefs.put("profile.default_content_settings.popups", 0);
//                chromePrefs.put("profile.default_content_setting_values.notifications", 2);
////                chromePrefs.put("download.default_directory", file);
//                chromePrefs.put("profile.default_content_setting_values.automatic_downloads", 1);
//                chromePrefs.put("download.prompt_for_download", false);
//                chromePrefs.put("--ignore-ssl-errors",true);
//                chromePrefs.put("--ignore-certificate-errors",true);
//                chromePrefs.put("--allow-insecure-localhost",true);
                ChromeOptions options = new ChromeOptions();
//                options.setExperimentalOption("prefs", chromePrefs);
//                DesiredCapabilities cap = DesiredCapabilities.chrome();
//                cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
//                cap.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS,true);
//                cap.setCapability(ChromeOptions.CAPABILITY, options);

                options.addArguments("--disable-dev-shm-usage");
                options.addArguments("--headless");
                options.addArguments("--no-sandbox");

                wd = new ChromeDriver(options);
                wd.manage().deleteAllCookies();
            } else if (browser.equalsIgnoreCase("FIREFOX")) {
                System.setProperty("webdriver.gecko.driver", "./drivers/geckodriver.exe");
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                wd = new FirefoxDriver(firefoxOptions);
            }

        } else {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setBrowserName(browser);
            try {
                wd = new RemoteWebDriver(new URL(reader.get("")), capabilities);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        wd.manage().window().maximize();
        wd.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);

        return wd;
        }else{
            return wd;
        }
    }

    public static void stop() {
        if (wd != null) {
            wd.quit();
            wd = null;
        }
    }




    public static void robotType(Robot robot, String characters) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection stringSelection = new StringSelection(characters);
        clipboard.setContents(stringSelection, null);
        robot.delay(1000);
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.delay(1000);
    }















}

