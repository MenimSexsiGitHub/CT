package com.sn.qa;


import com.sn.core.buildDriver.AppManager;
import com.sn.qa.util.CucumberFormatter;
import com.sn.qa.util.PropertyFileReader;
import cucumber.api.CucumberOptions;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
// import cucumber.api.junit.Cucumber;
import cucumber.api.testng.AbstractTestNGCucumberTests;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;
// import org.junit.runner.RunWith;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import java.io.File;
import java.util.ArrayList;

// @RunWith(Cucumber.class)
@CucumberOptions(
        strict=true,
        glue = {"com.sn.qa.steps"},
        features = {"src/main/resources/features/"},
        tags={"@HP_1"},
//       plugin = {"pretty","json:target/cucumber.json","pretty:target/cucumber-pretty.txt","html:target/cucumber-html",
//               "rerun:target/rerun.txt"}

         plugin = {"io.qameta.allure.cucumberjvm.AllureCucumberJvm","com.sn.qa.util.CucumberFormatter"}
)


public class Run extends AbstractTestNGCucumberTests {
    String sourceDir = "./src/main/resources/";

    public Run() {
    }

    private static CucumberFormatter formatter;
    private PropertyFileReader localReader = new PropertyFileReader("config.properties","config/");
    static ArrayList<String> listOfScenarios = new ArrayList<>();
    static ArrayList<String> results = new ArrayList<>();
    private static final Regex myRegex = new Regex("[^\\\\p{Alpha}\\\\p{Digit}]+");


    @BeforeSuite
    public void setUp() {
        formatter = new CucumberFormatter();
        CucumberFormatter.initiateExtentCucumberFormatter();
        CucumberFormatter.loadConfig(new File(sourceDir + "extent-config.xml"));

    }

    @AfterSuite(alwaysRun = true)
    public void tearDown() {
        AppManager.stop();
    }


    @Before
    public void startScenario(Scenario scenario) {

        String fileName = scenario.getName().split(" ")[0];
        String[] tagsToBeRun = localReader.get("tagsForVideoCapture").replaceAll("\\s+", "").split(",");
        for (String tag : scenario.getSourceTagNames()) {
            for (String tagToBeRun : tagsToBeRun) {
                if (tag.equalsIgnoreCase(tagToBeRun)) {
                    System.out.println("================================= " + tag + " ==========================================");
                } else {
                }
            }
        }

    }

    @After
    public void endScenario(Scenario scenario) {
        AppManager.stop();
        listOfScenarios.add(scenario.getStatus().toUpperCase() + " - " + scenario.getName());
    }

}
