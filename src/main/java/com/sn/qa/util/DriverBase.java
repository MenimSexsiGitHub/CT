package com.sn.qa.util;

import com.sn.core.buildDriver.AppManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

public abstract class DriverBase {

    protected WebDriver wd;
    static PropertyFileReader reader = new PropertyFileReader("config.properties", "config/");


    private static final Logger log = Logger.getLogger(DriverBase.class.getName());
    AppManager app = new AppManager((System.getProperty("browser", reader.get("browserName"))));

    public DriverBase(){

        wd = app.getWebDriver();



    }





}
