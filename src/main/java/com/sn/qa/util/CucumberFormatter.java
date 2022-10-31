package com.sn.qa.util;


import com.assertthat.selenium_shutterbug.core.Shutterbug;
import com.assertthat.selenium_shutterbug.utils.web.ScrollStrategy;
import com.sn.core.buildDriver.AppManager;
import com.google.common.base.Verify;
import com.relevantcodes.extentreports.*;
import cucumber.runtime.CucumberException;
import cucumber.runtime.io.URLOutputStream;
import gherkin.formatter.Formatter;
import gherkin.formatter.Reporter;
import gherkin.formatter.model.*;
import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.*;


/**
 * Cucumber custom format listener which generates ExtentsReport html file
 */
public class CucumberFormatter implements Reporter, Formatter {
    public static int currentStep;
    public static boolean testFailure = false;
    public static int failureNo;
    public static boolean windowReadyStateStatus = true;
    public static String outputDirectory;
    public static WebDriver driver;
    public static ExtentTest child;
    private static ExtentReports extent;
    private ExtentTest featureTest;
    private static ExtentTest scenarioTest;
    private LinkedList<Step> testSteps = new LinkedList<Step>();
    private static File htmlReportDir;
    private static Map systemInfo;
    private boolean scenarioOutlineTest;
    public static boolean screenShotSwitch = false;
    private static AppManager driverManager = new AppManager();
    private static final Map<String, String> MIME_TYPES_EXTENSIONS = new HashMap() {
        {
            this.put("image/bmp", "bmp");
            this.put("image/gif", "gif");
            this.put("image/jpeg", "jpg");
            this.put("image/png", "png");
            this.put("image/svg+xml", "svg");
            this.put("video/ogg", "ogg");
        }
    };


    public CucumberFormatter(File filePath) {
        if (!filePath.getPath().equals("")) {
            String reportPath = filePath.getPath();
            this.htmlReportDir = new File(reportPath);
            this.extent = new ExtentReports(reportPath);
        } else {
            String reportDir = createOutputDirectory();
            this.htmlReportDir = new File(reportDir + "/report.html");
            this.extent = new ExtentReports(reportDir + "/report.html");
        }
    }

    public CucumberFormatter() {
    }

    public static void initiateExtentCucumberFormatter(File filePath, Boolean replaceExisting,
                                                       DisplayOrder displayOrder, NetworkMode networkMode,
                                                       Locale locale) {
        htmlReportDir = filePath;
        extent = new ExtentReports(filePath.getAbsolutePath(), replaceExisting, displayOrder, networkMode, locale);
    }


    public static void initiateExtentCucumberFormatter(File filePath) {
        initiateExtentCucumberFormatter(filePath, null, null, NetworkMode.OFFLINE, null);
    }

    public static void initiateExtentCucumberFormatter() {
        String reportDir = createOutputDirectory();
        File file = new File(reportDir);
        file.mkdir();
        initiateExtentCucumberFormatter(new File(reportDir + File.separator + "report.html"));
    }

    public static void setTestRunnerOutput(String s) {
        extent.setTestRunnerOutput(s);
    }

    public static void loadConfig(File configFile) {
        extent.loadConfig(configFile);
    }

    public static void addSystemInfo(String param, String value) {
        if (systemInfo == null) {
            systemInfo = new HashMap();
        }
        systemInfo.put(param, value);
    }

    public static void addSystemInfo(Map<String, String> info) {
        if (systemInfo == null) {
            systemInfo = new HashMap();
        }
        systemInfo.putAll(info);
    }

    public void before(Match match, Result result) {

    }

    public void result(Result result) {
        if (!scenarioOutlineTest) {
            Step step = testSteps.poll();
            if ("passed".equals(result.getStatus())) {
                scenarioTest.log(LogStatus.PASS, "<font color=blue> <b>" + step.getKeyword() + "</b>:" + step.getName() + "</font><br/>");
            } else if ("failed".equals(result.getStatus())) {
                scenarioTest.log(LogStatus.FAIL, "<font color=red>" + step.getKeyword() + ":" + step.getName() + "</font><br/>");
                failureNo += 1;
                captureScreenShot("TestFailure" + failureNo);
                embededScreenshot("./Screenshots/TestFailure" + failureNo + ".png");
            } else if ("skipped".equals(result.getStatus())) {
                scenarioTest.log(LogStatus.SKIP, "<font color=orange>" + step.getKeyword() + ":" + step.getName() + "</font><br/>");
            } else if ("undefined".equals(result.getStatus())) {
                scenarioTest.log(LogStatus.UNKNOWN, step.getKeyword() + ":" + step.getName());
            }
        }
    }

    public static void testReporter(String color, String report) {
        colorTypes ct = colorTypes.valueOf(color.toLowerCase());
        if (!color.contains("white")) {
            currentStep += 1;
        }
        switch (ct) {
            case green:
                scenarioTest.log(LogStatus.PASS, "<font color=green>" + report + "</font><br/>");
                break;
            case blue:
                scenarioTest.log(LogStatus.INFO, "<font color=blue>" + report + "</font><br/>");
                break;
            case orange:
                scenarioTest.log(LogStatus.WARNING, "<font color=orange>" + report + "</font><br/>");
            case red:
                scenarioTest.log(LogStatus.FAIL, "<font color=red>" + report + "</font><br/>");
                break;
            case white:
                scenarioTest.log(LogStatus.INFO, report);

        }
    }


    public static enum colorTypes {
        green,
        red,
        blue,
        orange,
        white;

    }

    public static void testStepPassed(String msg) {
        testReporter("Green", msg);
        takeScreenshot();
        allureLog(msg);
    }

    public static void testStepInfo(String msg) {
        scenarioTest.log(LogStatus.INFO, msg);
        allureLog(msg);
    }

    public static void takeScreenShot(cucumber.api.Scenario scenario){
        String pathToSave = "";
        File file = new File(pathToSave);
        TakesScreenshot screenshot = ((TakesScreenshot)AppManager.getWebDriver());
        byte[] screenShot = screenshot.getScreenshotAs(OutputType.BYTES);
        scenario.embed(screenShot,"image/png");

    }

            public static void takeSnapShot(){
        try {
            File screenshotFile = (File) ((TakesScreenshot) AppManager.getWebDriver()).getScreenshotAs(OutputType.FILE);
            byte[] ex = FileUtils.readFileToByteArray(screenshotFile);
            File screenPath = new File(outputDirectory + "\\Screenshots");
            String fileName = "screenshot" + System.currentTimeMillis();
            embededScreenshot("./Screenshots/" + fileName + ".png");
            allureAttachScreenShot("/Screenshots/" + fileName + ".png");
        }catch (Exception e){

        }
    }

    public static void captureScreenShot(String filename) {
        String result = scenarioTest.getTest().getStatus().toString();
        if (screenShotSwitch == false || result.contains("fail")) {
            File scrFile = null;
            String scrPath = createOutputDirectory() + "\\Screenshots";
            File file = new File(scrPath);
            file.mkdir();
            try {
                Shutterbug.shootPage(driverManager.getWebDriver(), ScrollStrategy.BOTH_DIRECTIONS)
                        .withName(filename)
                        .save(scrPath);

            } catch (Exception ex) {
                testReporter("Red", ex.toString());
            }
        }
    }

    public static void captureScreen(String filename) {
        String result = scenarioTest.getTest().getStatus().toString();
        if (screenShotSwitch == false || result.contains("fail")) {
            File scrFile = null;
            String scrPath = outputDirectory + "\\Screenshots";
            File file = new File(scrPath);
            file.mkdir();
            try {
                Shutterbug.shootPage(driverManager.getWebDriver())
                        .withName(filename)
                        .save(scrPath);

            } catch (Exception ex) {
                testReporter("Red", ex.toString());
            }
        }
    }



    public static void captureDesktopScreenShot(String filename) {
        String result = scenarioTest.getTest().getStatus().toString();
        if (screenShotSwitch == false || result.contains("fail")) {
            String scrPath = outputDirectory + "\\Screenshots";
            File file = new File(scrPath);
            file.mkdir();
            try {
                Robot robot = new Robot();
                Rectangle captureSize = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
                BufferedImage bufferedImage = robot.createScreenCapture(captureSize);
                String path = scrPath + "\\" + filename + ".png";
                File outputfile = new File(path);
                ImageIO.write(bufferedImage, "png", outputfile);
            } catch (Exception e) {
                System.out.println("ERROR  While taking screenshot of the failing test case " + e);
            }
        }
    }

    public static void embededScreenshot(String pathAndFile) {
        String result = scenarioTest.getTest().getStatus().toString();
        if (screenShotSwitch == false || result.contains("fail")) {
            scenarioTest.log(LogStatus.INFO, "Manual Verification Point" + scenarioTest.addScreenCapture(pathAndFile));

        }
    }

    public static void embededVideoRecording(String pathAndFile) {
        scenarioTest.log(LogStatus.INFO, ",<div><b>" + scenarioTest.getTest().getName() + " recording. </b></div>" + scenarioTest.addScreencast(pathAndFile));

    }

    public static void testStepFailed(String errMessage) {
        try {
            testFailure = true;
            failureNo += 1;
            testReporter("Red", errMessage);
//            captureScreenShot("TestFailure" + failureNo);
            captureScreen("TestFailure" + failureNo);
            allureErrorLog(errMessage);
            embededScreenshot("./Screenshots/TestFailure" + failureNo + ".png");
            allureAttachScreenShot("/Screenshots/TestFailure" + failureNo + ".png");
            Assert.fail(errMessage);
            Verify.verify(false);
        } catch (Exception ex) {
            testStepFailed("Exception caught  Message is->" + ex.getMessage());
        }
    }

    public static void testStepFailed(String errMessage, String[] jiraIDs) {
        try {
            testFailure = true;
            failureNo += 1;
            testReporter("Red", errMessage);
            captureScreenShot("TestFailure" + failureNo);
            allureErrorLog(errMessage);
            embededScreenshot("./Screenshots/TestFailure" + failureNo + ".png");
            allureAttachScreenShot("/Screenshots/TestFailure" + failureNo + ".png");
            PropertyFileReader properties = new PropertyFileReader("config.properties","config/");
            for (String jiraID : jiraIDs) {
                Allure.link("JIRA-" + jiraID, "issue", properties.get("jira.url") + jiraID);
            }
            Assert.fail(errMessage);
            Verify.verify(false);
        } catch (Exception ex) {
            testStepFailed("Exception caught  Message is->" + ex.getMessage());
        }
    }


    public static void testStepException(String method, Exception ex) {
        try {
            testFailure = true;
            failureNo += 1;
            testReporter("Red", "Exception Caught in method " + method + ", and Error Message is->" + ex.getMessage());
            captureScreenShot("TestFailure" + failureNo);
            allureErrorLog(method + ex);
            embededScreenshot("./Screenshots/TestFailure" + failureNo + ".png");
            allureAttachScreenShot("/Screenshots/TestFailure" + failureNo + ".png");
            Assert.fail(method);
            Verify.verify(false);

        } catch (Exception ex1) {
            testStepFailed("Exception caught  Message is->" + ex1.getMessage());
        }
    }


    public static void  takeScreenshot() {

            String fileName = "screenshot" + System.currentTimeMillis();
            captureScreen(fileName);
            embededScreenshot("./Screenshots/" + fileName + ".png");
            allureAttachScreenShot("/Screenshots/" + fileName + ".png");
    }

    public static void  takeWholePageScreenshot() {

        String fileName = "screenshot" + System.currentTimeMillis();
        captureScreenShot(fileName);
        embededScreenshot("./Screenshots/" + fileName + ".png");
        allureAttachScreenShot("/Screenshots/" + fileName + ".png");
    }

    public static void  takeDeckTopScreenshot() {

        String fileName = "screenshot" + System.currentTimeMillis();
        captureDesktopScreenShot(fileName);
        embededScreenshot("./Screenshots/" + fileName + ".png");
        allureAttachScreenShot("/Screenshots/" + fileName + ".png");
    }

    public static void  takeScreenShot() {

        String fileName = "screenshot" + System.currentTimeMillis();
        captureNormalScreenShot(fileName);
        embededScreenshot("./Screenshots/" + fileName + ".png");
        allureAttachScreenShot("/Screenshots/" + fileName + ".png");
    }

    public void after(Match match, Result result) {

    }

    public void match(Match match) {

    }

    public void embedding(String s, byte[] bytes) {
        if (!scenarioOutlineTest) {
            String extension = (String) MIME_TYPES_EXTENSIONS.get(s);
            String fileName = "screenshot-" + System.currentTimeMillis() + "." + extension;
            this.writeBytesAndClose(bytes, this.reportFileOutputStream(fileName));
            scenarioTest.log(LogStatus.INFO, scenarioTest.addScreenCapture(fileName));
        }
    }

    public void write(String s) {
        if (!scenarioOutlineTest)
            scenarioTest.log(LogStatus.INFO, s);
    }

    public void syntaxError(String s, String s1, List<String> list, String s2, Integer integer) {
    }

    public void uri(String s) {
    }

    public void feature(Feature feature) {
        featureTest = extent.startTest("Feature: " + feature.getName());
    }

    public void scenarioOutline(ScenarioOutline scenarioOutline) {
        scenarioOutlineTest = true;
    }

    public void examples(Examples examples) {
    }

    public void startOfScenarioLifeCycle(Scenario scenario) {
        scenarioTest = extent.startTest("Scenario: " + scenario.getName());

        for (Tag tag : scenario.getTags()) {
            scenarioTest.assignCategory(tag.getName());
        }
        scenarioOutlineTest = false;
    }

    public void background(Background background) {
    }

    public void scenario(Scenario scenario) {
    }

    public void step(Step step) {
        if (!scenarioOutlineTest)
            testSteps.add(step);
    }

    public void endOfScenarioLifeCycle(Scenario scenario) {
        if (!scenarioOutlineTest) {
            extent.endTest(scenarioTest);
            featureTest.appendChild(scenarioTest);
            extent.flush();
        }

    }

    public void done() {
    }

    public void close() {
        extent.addSystemInfo(systemInfo);
        extent.close();
    }

    public void eof() {
        extent.endTest(featureTest);
        extent.flush();
    }

    private OutputStream reportFileOutputStream(String fileName) {
        try {
            return new URLOutputStream(new URL(this.htmlReportDir.toURI().toURL(), fileName));
        } catch (IOException var3) {
            throw new CucumberException(var3);
        }
    }

    private void writeBytesAndClose(byte[] buf, OutputStream out) {
        try {
            out.write(buf);
        } catch (IOException var4) {
            throw new CucumberException("Unable to write to report file item: ", var4);
        }
    }

    public static void captureNormalScreenShot(String filename) {
        File scrFile = null;
        String scrPath = outputDirectory + "\\Screenshots";
        File file = new File(scrPath);
        file.mkdir();
        try {
            TakesScreenshot scrShot = ((TakesScreenshot) driverManager.getWebDriver());
            scrFile = (File) (scrShot.getScreenshotAs(OutputType.FILE));
            FileUtils.copyFile(scrFile, new File(scrPath + "\\" + filename + ".png"));

            File DestFile = new File(scrPath + "/" + filename);
            FileUtils.copyFile(scrFile, DestFile);
        } catch (Exception ex) {
            testReporter("Red", ex.toString());
        }
    }

    public static void takeNormalScreenshot() {

        String fileName = "screenshot" + System.currentTimeMillis();
        captureNormalScreenShot(fileName);
        embededScreenshot("./Screenshots/" + fileName + ".png");
    }

    public static void takeDesktopScreenshot() {

        String fileName = "screenshot" + System.currentTimeMillis();
        captureDesktopScreenShot(fileName);
        embededScreenshot("./Screenshots/" + fileName + ".png");
    }

    public static String createOutputDirectory() {
        String resultPath = "";
        String appName = "";
        try {
            PropertyFileReader localreader = new PropertyFileReader("config.properties","config/");
            resultPath = localreader.get("resultPath");
            appName = localreader.get("application.name");
        } catch (Exception ex) {
        }
        String am_pm;
        String min;
        String hr;
        String sec;
        int yr;
        String mon;
        String day;
        String timeStamp;

        File curdir = new File(".");

        Calendar calendar = new GregorianCalendar();

        hr = "0" + calendar.get(10);
        hr = hr.substring(hr.length() - 2);

        min = "0" + calendar.get(12);
        min = min.substring(min.length() - 2);

        sec = "0" + calendar.get(13);
        sec = sec.substring(sec.length() - 2);

        yr = calendar.get(1);

        mon = "0" + (calendar.get(2) + 1);
        mon = mon.substring(mon.length() - 2);

        day = "0" + calendar.get(5);
        day = day.substring(day.length() - 2);

        if (calendar.get(9) == 0) {
            am_pm = "AM";
        } else {
            am_pm = "PM";
        }
        File file = new File(curdir + File.separator + "Reports");
        file.mkdir();
        if (appName.trim().equals("")) {
            timeStamp = yr + "_" + mon + "_" + day + "_" + hr + "_" + min + "_" + sec + "_" + am_pm;
        } else {
            timeStamp = appName + "_" + yr + "_" + mon + "_" + day + "_" + hr + "_" + min + "_" + sec + "_" + am_pm;
        }
        if (resultPath.trim().equals("")) {
            outputDirectory = curdir + File.separator + "Reports" + File.separator + timeStamp;
        } else {
            outputDirectory = resultPath + File.separator + timeStamp;
        }
        return outputDirectory;
    }



    public static void allureLog(final String message) {
        Allure.step("LOG INFO : " + message);
    }

    public static void allureErrorLog(final String message) {
        Allure.step("LOG ERROR:" + message, Status.FAILED);
    }


    public static void allureAttachScreenShot(String path) {
        String result = scenarioTest.getTest().getStatus().toString();
        if (screenShotSwitch == false || result.contains("fail")) {
            Path content = Paths.get(outputDirectory + path);
            try (InputStream is = Files.newInputStream(content)) {
                Allure.step(("Manual verification point"), () -> Allure.addAttachment("SCREENSHOT", "image/png", is, "png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



}
