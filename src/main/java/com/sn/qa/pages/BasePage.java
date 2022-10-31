package com.sn.qa.pages;

import com.sn.core.buildDriver.AppManager;
import com.sn.core.constant.CoreEnums;
import com.sn.core.exceptionhandling.ExceptionHandling;
import com.sn.core.util.Utilities;
import com.sn.qa.util.CucumberFormatter;
import com.sn.qa.util.DriverBase;
import com.sn.qa.util.PropertyFileReader;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.Point;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;



public class BasePage extends DriverBase {

    static final Logger log = Logger.getLogger(BasePage.class.getName());
    public static int timeoutForCoreMethods = 1;
    static long startTime = System.currentTimeMillis();
    Actions actions = new Actions(wd);
    static PropertyFileReader reader = new PropertyFileReader("config.properties", "config/");

    @FindBy()
    WebElement imgSpinner;


    public BasePage() {
        PageFactory.initElements(wd,this);
    }
    public void goToGMHomePage() throws Exception{
        wd.get(reader.get("snAppURL"));
    }

    public void verifyWindowTitle(String title){
        if(AppManager.getWebDriver().getTitle().contains(title)){
            System.out.println("Expected window page is displayed");
        }else {
            Assert.assertFalse("Expected web page isn't displayed", true);
        }
    }

    public void verifyElementDisplayed(WebElement el){
        if (el.isDisplayed()){
            System.out.println("Element Displayed");
        }else{
            takeScreenshot();
            Assert.assertTrue("Element not displayed", false);
        }
    }

    public void waitDelayTime(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        }catch (Exception e){}

    }

    public void takeScreenshot(){
        CucumberFormatter.takeScreenshot();
    }

    public void captureScreenShot(){
        CucumberFormatter.captureScreenShot("Screenshots");
    }


    public void mouseHOverOnElement(WebElement element){
        actions.moveToElement(element).build().perform();
    }

    public void selectByValueFromDropDown(WebElement element, String value){
        Select item = new Select(element);
        item.selectByValue(value);
    }
    public void selectByIndexFromDropDown(WebElement element, String text){
        int index = Integer.parseInt(text);
        Select item = new Select(element);
        item.selectByIndex(index);
    }

    public void selectByTextFromDropDown(WebElement element, String text){
        Select item = new Select(element);
        item.selectByVisibleText(text);
    }

    public String  getTextOfSelectedValue(WebElement element, String text){
        Select item = new Select(element);
        String valueText = item.getFirstSelectedOption().getText();
        return valueText;
    }

    public boolean isAlertPresent(){
        boolean rootValue = false;
        try {
            if ((new WebDriverWait(AppManager.getWebDriver(), 5L).until(ExpectedConditions.alertIsPresent()) != null)){
                rootValue = true;
            }
        }catch (Exception var2){log.info("Exception details: " + var2);}
        return rootValue;
    }

    public  void acceptAlert(){
        wd.switchTo().alert().accept();
    }

    public void dismissAlert(){
        wd.switchTo().alert().dismiss();
    }

    public String getTextOfAlert(){
        String alertText = wd.switchTo().alert().getText();
        return alertText;
    }

    public void verifyTextOfAlert(String text){
        String alertText = wd.switchTo().alert().getText();
        if (alertText.contains(text)){
            log.info("Expected pop up alert displayed");
            System.out.println("Expected pop up alert displayed: "+ alertText);
        }
    }



    public static Set<String> getWindowHandles(){
        return AppManager.getWebDriver().getWindowHandles();
    }

    public static String getCurrentWindowHandle(){ return AppManager.getWebDriver().getWindowHandle(); }


    public void switchToWindow(String windowTitle){
        try {
            Set<String> windows = AppManager.getWebDriver().getWindowHandles();
            System.out.println("window size: "+windows.size());
            log.info("window size: "+windows.size());
            Iterator var2 = windows.iterator();
            while (var2.hasNext()){
                String windHandle = (String)var2.next();
                AppManager.getWebDriver().switchTo().window(windHandle);
                log.info("title: "+ AppManager.getWebDriver().getTitle());
                if (AppManager.getWebDriver().getTitle().contains(windowTitle)){
                    break;
                }
            }
        }catch (Exception var4){
            ExceptionHandling.displayCustomErrorMessage(var4.getMessage(),"Window title is: "+ windowTitle,"switchToWindow",startTime, CoreEnums.ERROR.ELEMENT_ERROR);
        }
    }
    public void switchToNewTab(){
        try {
            wd.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL + "t");
        }catch (Exception var4){
            ExceptionHandling.displayCustomErrorMessage(var4.getMessage(),"Tab title is: ","switchToTab",startTime,CoreEnums.ERROR.ELEMENT_ERROR);
        }
    }
    public void switchFocusToNewTab(){
        try {
            wd.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL, Keys.SHIFT,Keys.TAB);
        }catch (Exception var4){
            ExceptionHandling.displayCustomErrorMessage(var4.getMessage(),"Tab title is: ","switchToTab",startTime,CoreEnums.ERROR.ELEMENT_ERROR);
        }
    }

    public void switchToFrame(WebElement element){
        System.out.println("Switching focus to iFrame");
        wd.switchTo().frame(element);
    }
    public void switchToParentFrame(){
        wd.switchTo().parentFrame();
    }

    public void switchToFrameByName(String frameName){
        System.out.println("Switching the iFrame by name");
        wd.switchTo().frame(frameName);
    }

    public void switchToDefaultContent(){
        System.out.println("Switching focus to the default content");
        wd.switchTo().defaultContent();
    }

    public void javascriptClick(WebElement element){
        JavascriptExecutor js = (JavascriptExecutor)wd;
        js.executeScript("arguments[0].click",element);
    }

    public void switchFocusToFrameIfVisible(WebElement element){
        if (element.isDisplayed()) {
            System.out.println("Switching focus to iFrame");
            wd.switchTo().frame(element);
        }else {
            Assert.assertTrue("iFrame is not visible", false);
        }
    }


    public void mouseHoverAndClick(WebElement el){
        actions.moveToElement(el).click(el).build().perform();
    }

    public void waitForPageLoad(long timeout){
        long currentM = System.currentTimeMillis();
        String exl = "";
        while (true){
            try {
                ((JavascriptExecutor) wd).executeScript("return document.readyState", new Object[0].equals("complete"));
                exl = null;
                break;
            }catch (Exception var7){
                exl = var7.getMessage();
                if (System.currentTimeMillis() - currentM <= timeout * 1000L){
                    break;
                }
            }
        }
    }


    public void doubleClick(WebElement el){
        actions.doubleClick(el).perform();
    }

    public void scrollTrueView(WebElement uiElement){
        startTime = System.currentTimeMillis();
        try {
            JavascriptExecutor js = (JavascriptExecutor)wd;
            js.executeScript("argument[0].scrollIntoView(true);", new Object[]{uiElement});
            Utilities.waitTime(3L);
        }catch (Exception var2){
            ExceptionHandling.displayCustomErrorMessage(var2.getMessage(),(uiElement.toString()),"Scroll", startTime, CoreEnums.ERROR.ELEMENT_ERROR);
        }
    }

    public void scrollIntoView(WebElement element){
        startTime = System.currentTimeMillis();
        try {
            Point coordination = element.getLocation();
            ((JavascriptExecutor)wd).executeScript("window.scrollBy(0,"+ (coordination.getY() - 100)+")", new Object[]{""});
        }catch (Exception var2){
            ExceptionHandling.displayCustomErrorMessage(var2.getMessage(),(element.toString()),"Scroll", startTime, CoreEnums.ERROR.ELEMENT_ERROR);
        }
    }

    public void scrollIntoViewOfElement(WebElement el){
        try {
            JavascriptExecutor js = (JavascriptExecutor)wd;
            js.executeScript("arguments[0].scrollIntoView(true);", el);
            Thread.sleep(500);
        }catch (Exception e){

        }
    }

    public void pageScroll(){
        long sTime = System.currentTimeMillis();
        try {
            ((JavascriptExecutor)wd).executeScript("window.scrollBy(0,250)", new Object[]{""});
        }catch (Exception var3){
            ExceptionHandling.displayCustomErrorMessage(var3.getMessage(),"window.scrollBy(0,250)","pageScroll", startTime, CoreEnums.ERROR.ELEMENT_ERROR);
        }
    }

    public void scrollUpObject(){
        JavascriptExecutor js =(JavascriptExecutor)wd;
        js.executeScript("scroll(0, -250)", new Object[0]);
    }

    // scroll vertically down by 1000 pixel
    public void scrollDownByPixel(String pixel){
        JavascriptExecutor js =(JavascriptExecutor)wd;
        js.executeScript("window.scrollBy(0,"+pixel+")");
    }

    public void scrollDown(){
        JavascriptExecutor js =(JavascriptExecutor)wd;
        js.executeScript("window.scrollBy(0,250)");
    }

    // scroll vertically down till end
    public void scrollDownTillEnd(){
        JavascriptExecutor js =(JavascriptExecutor)wd;
        js.executeScript("window.scrollBy(0,document.body.scrollHeight)");
    }

    public void scrollUpInVerticalDirection(){
        JavascriptExecutor js =(JavascriptExecutor)wd;
        js.executeScript("window.scrollTo(document.body.scrollHeight,0)");
    }

    public void scrollUp(){
        JavascriptExecutor js =(JavascriptExecutor)wd;
        js.executeScript("window.scrollBy(0, -250)");
    }

    public void scrollHorizontallyInRight(){
        JavascriptExecutor js =(JavascriptExecutor)wd;
        js.executeScript("window.scrollBy(2000, 0)");
    }

    public void scrollHorizontallyInTheLeft(){
        JavascriptExecutor js =(JavascriptExecutor)wd;
        js.executeScript("window.scrollBy(-2000, 0)");
    }

    public void verifyIFrame(String frameTitle){
        String actFrameTitle = wd.switchTo().frame("iframe").getTitle();
        if (actFrameTitle.equalsIgnoreCase(frameTitle)){
            System.out.println("");
        }
    }


//    public void takeScreenShot(){
//        CucumberFormatter.takeScreenshot();
//    }

    public void takeScreenshotOfWholePage(){
        CucumberFormatter.takeWholePageScreenshot();
    }

    public void takeScreenshotOfDeckTop(){
        CucumberFormatter.takeDeckTopScreenshot();
    }

    public void waitForVisibilityOfElement(WebElement el){
        WebDriverWait wait = new WebDriverWait(wd,10);
        wait.until(ExpectedConditions.visibilityOf(el));
    }

    public void waitForVisibilityOfElement(WebElement el, int timeout){
        WebDriverWait wait = new WebDriverWait(wd,timeout);
        wait.until(ExpectedConditions.visibilityOf(el));
    }

    public void waitForElementToBeClickable(WebElement el){
        WebDriverWait wait = new WebDriverWait(wd,10);
        wait.until(ExpectedConditions.elementToBeClickable(el));
    }

    public void waitForElementToBeClickable(By by){
        WebDriverWait wait = new WebDriverWait(wd,10);
        wait.until(ExpectedConditions.elementToBeClickable(by));
    }

    public void getTextFromAlertPopUpAndValidate(String expText){
        takeScreenshot();
        if(isAlertPresent()){
            String textOfAlert = getTextOfAlert();
            if (textOfAlert.contains(expText)){
                System.out.println("Expected alert displayed");
                acceptAlert();
            }else {
                System.out.println("Expected alert isn't displayed");
            }
        }
    }

    public static void waitForAlertDismiss() {
        for (int i = 0; i < 10; i++) {
            try {
                WebDriver wd = AppManager.getWebDriver();
                wd.switchTo().defaultContent();
                break;
            } catch (UnhandledAlertException ex) {

            }
        }
    }

    public void refreshBrowser() {
        log.info("Refreshing the page");
        wd.navigate().refresh();
    }

    public void openUrlInNewTab(String Url) {
        log.info("Opening New Window Tab");
        String linkOpeningNewTab = Keys.chord(Keys.CONTROL, Keys.RETURN);
        wd.findElement(By.linkText(Url)).sendKeys(linkOpeningNewTab);

    }

    public void openUrlInANewTab(String Url) {
        WebDriver driver = AppManager.getWebDriver();
        log.info("Opening New Window Tab");
        wd.findElement(By.tagName("body")).sendKeys(Keys.chord(Keys.CONTROL + "t"));
        List<String> tabs = new ArrayList<>(wd.getWindowHandles());
        wd.switchTo().window(tabs.get(1));
        wd.get(Url);
//        Set<String> windowHandles = getWindowHandles();
//        for (String windowHandle : driver.getWindowHandles()) {
//            driver.switchTo().window(windowHandle);
//            driver.navigate().to(Url);
//
//        }
    }

//    public void openNewTab(String Url) {
//        log.info("Opening New Window Tab");
//        String linkOpeningNewTab = Keys.chord(Keys.CONTROL + "t");
//        wd.findElement(By.linkText(Url)).sendKeys(linkOpeningNewTab);
//    }

    public void openNewTab(String Url) {
        log.info("Opening New Window Tab");
        String linkOpeningNewTab = Keys.chord(Keys.CONTROL + "t");
        wd.findElement(By.tagName("body")).sendKeys(linkOpeningNewTab);
    }

    public void openANewTab(String URL) {
        log.info("Opening URL in New Window Tab");
        JavascriptExecutor js = (JavascriptExecutor)wd;
        js.executeScript("window.open('"+URL+"','_blank');");
        List<String> tabs = new ArrayList<>(wd.getWindowHandles());
        wd.switchTo().window(tabs.get(1));

    }



    public void tabOut() {
        try {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_TAB);
            robot.keyRelease(KeyEvent.VK_TAB);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public void tabOutFrom(WebElement element){
        actions.sendKeys(element,Keys.TAB).build().perform();
        actions.sendKeys(element,Keys.RETURN).build().perform();

//        actions.sendKeys(element,Keys.TAB).perform();
//        actions.sendKeys(element,Keys.RETURN).perform();

        waitDelayTime(3000);
    }



    public static boolean waitForLoadingCompleted() {
        if (AppManager.getWebDriver().findElement(By.xpath("//*[contains(@class,'spinner')]")).isDisplayed()) {
            return elementToBeInvisible(By.xpath("//*[contains(@class,'spinner')]"));
        } else {
            return true;
        }
    }

    public static boolean elementToBeInvisible(By by) {
        try {
            WebDriverWait wait = new WebDriverWait(AppManager.getWebDriver(),80);
            wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public void verifyTextMessage(String message){
        String xpath ="//*[text()=\""+ message +"\"]";
        WebElement textMessage = wd.findElement(By.xpath(xpath));
        if (textMessage.isDisplayed()){
            takeScreenshot();
            System.out.println("Expected text message displayed. Text message : " + textMessage.getText());
        }else{
            System.out.println("Expected text message not displayed. Text message: " + textMessage);
//            log.info("Expected text message isn't displayed. Text message : " + textMessage.getText());
//            Assert.assertTrue("Expected text message displayed", false);
        }
//        takeScreenshot();
    }

    public void verifyTextContains(String text){
        String xpath ="//*[contains(text(),'"+ text +"')]";
        WebElement textMessage = wd.findElement(By.xpath(xpath));
//        takeScreenshot();
        if (textMessage.isDisplayed()){
            System.out.println("Expected text message displayed. Text message : " + textMessage.getText());
        }else{
            System.out.println("Expected text message not displayed. Text message: " + textMessage);
//            Assert.assertTrue("Expected text message displayed", false);
//            log.info("Expected text message isn't displayed. Text message : " + textMessage.getText());
        }
//        takeScreenshot();
    }

    public void verifyText(String text){
        String xpath ="//*[text()='"+ text +"']";
        WebElement textMessage = wd.findElement(By.xpath(xpath));
        if (textMessage.isDisplayed()){
            System.out.println("Expected text message displayed. Text message : " + textMessage.getText());
        }else{
//            Assert.assertTrue("Expected text message displayed", false);
            log.info("Expected text message isn't displayed. Text message : " + textMessage.getText());
        }
    }

    public void fileUpload(String filepath){
        try {
//            if (isAlertPresent()) {
                Alert alert = wd.switchTo().alert();
                alert.sendKeys(filepath);
                alert.accept();
                Robot robot = new Robot();
                robot.keyPress(KeyEvent.VK_ENTER);
                robot.keyRelease(KeyEvent.VK_ENTER);
                waitForAlertDismiss();
//            }

        } catch (Exception e) {
        }

    }

    public void uploadFile(String file) throws AWTException{
        StringSelection ss = new StringSelection(file);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss,null);
        Robot robot = new Robot();
        robot.delay(250);
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.delay(90);
        robot.keyRelease(KeyEvent.VK_ENTER);
    }

    public void locateAndClickElementByPartialText(String partialText){
        wd.findElement(By.xpath("//*[contains(text(),'"+partialText+"')]")).click();
    }



    public void verifyElementDisabled(WebElement el){
        if (!el.isEnabled()){
            System.out.println("Element Disabled");
        }else{
            takeScreenshot();
            Assert.assertTrue("Element not disabled", false);
        }
    }





















}
