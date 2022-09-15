package com.sn.qa.steps;

import com.sn.core.buildDriver.AppManager;
import com.sn.qa.util.CucumberFormatter;
import cucumber.api.Scenario;
import org.junit.After;
import org.junit.Before;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

public class MainAutomationHooks {
    public static List<String> listOfExecutingScenarios = new LinkedList<String>();
    public static boolean isNeedToFail = false;
    public static Scenario sharedScenarioObj = null;
    private static Logger log = Logger.getLogger(MainAutomationHooks.class.getName());
    private static CucumberFormatter formatter;
    String sourceDir = "./src/test/resources/";

    public MainAutomationHooks(){

    }

    @Before
    public void beforeScenario(Scenario scenario) throws Exception{
        System.setProperty("Test_Run_ProjectName", "");
        sharedScenarioObj = scenario;
        listOfExecutingScenarios.add(scenario.getName());
        failHereIfRequired();

            formatter = new CucumberFormatter();
            CucumberFormatter.initiateExtentCucumberFormatter();
            CucumberFormatter.loadConfig(new File(sourceDir + "extent-config.xml"));
    }
    private static void failHereIfRequired(){
        if (isNeedToFail){
            throw new NullPointerException("Please ignore this exception");
        }
    }
    @After
    public void afterScenario(Scenario scenario) throws Throwable{
        failHereIfRequired();
        AppManager.stop();
    }


}





















