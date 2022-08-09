package com.sn.qa.pages.SN;

import com.sn.qa.pages.BasePage;
import com.sn.qa.util.PropertyFileReader;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

public class HomePage extends BasePage {

    static PropertyFileReader textToVerify = new PropertyFileReader("textToVerify.properties", "data/");

    @FindBy(xpath = "//img[@class='logo']")
    private WebElement logoAmericanCancerSociety;

    //header bar elements

    @FindBy(xpath = "//*[@class='title']/h2/strong")
    private WebElement labelPageTitle;
    @FindBy(xpath = "//*[text()='Donate Today.']")
    private WebElement labelDonateToday;
    @FindBy(xpath = "//*[contains(text(),'1 in 2 men and 1 in 3 women will be diagnosed')]")
    private WebElement labelDescription;
    @FindBy(id="frequency_onetime")
    private WebElement btnOneTimeContainer;
    @FindBy(id="frequency_monthly")
    private WebElement btnMonthlyContainer;
    @FindBy(xpath = "//*[@class='section-title' and contains(text(),'Choose Your Donation Amount')]")
    private WebElement labelChooseYourDonationAmount;
    @FindBy(xpath = "//*[@class='section-title' and contains(text(),'Frequency')]")
    private WebElement labelFrequency;
    @FindBy(xpath = "(//*[@class='amount-item' and text()='$50'])[1]")
    private WebElement btnOneTime$50;
    @FindBy(xpath = "//*[@class='amount-item active' and text()='$100']")
    private WebElement btnOneTime$100;
    @FindBy(xpath = "//*[@class='amount-item' and text()='$250']")
    private WebElement btnOneTime$250;
    @FindBy(xpath = "//*[@class='amount-item' and text()='$500']")
    private WebElement btnOneTime$500;
    @FindBy(xpath = "(//*[@class='amount-item' and text()='$15'])[1]")
    private WebElement btnMonthly$15;
    @FindBy(xpath = "//*[@class='amount-item active' and text()='$25']")
    private WebElement btnMonthly$25;
    @FindBy(xpath = "(//*[@class='amount-item' and text()='$50'])[2]")
    private WebElement btnMonthly$50;
    @FindBy(xpath = "//*[@class='amount-item' and text()='$100']")
    private WebElement btnMonthly$100;
    @FindBy(xpath ="(//*[contains(text(),'background-image:')])[2]")
    private WebElement imgBackgroundPatientsFamilies;
    @FindBy(xpath ="//button[@class='optanon-alert-box-close banner-close-button' and @title='Close']")
    private WebElement btnCloseManageCookies;




   

    public HomePage(){
        PageFactory.initElements(wd,this);
    }



    public void validateAppLogoExistence(){
        if(logoAmericanCancerSociety.isDisplayed()) {
            System.out.println("app logo is visible");
        }else {
            org.junit.Assert.assertFalse("Logo isn't displayed", true);
        }
    }


    public void verifyPageTitleText(){
        if(labelPageTitle.isDisplayed()){
            Assert.assertEquals(labelPageTitle.getText(),textToVerify.get("homePageTitle"));
        }else {
            Assert.assertFalse(true, "Page title isn't displayed");
        }
    }

    public void verifyDonateTodayText(){
        if(labelDonateToday.isDisplayed()){
            System.out.println("Donate Today label is displayed as expected");
        }else {
            Assert.assertFalse(true, "Donate Today label isn't displayed");
        }
    }

    public void verifyDescriptionContent(){
        if(labelDescription.isDisplayed()){
            Assert.assertEquals(labelDescription.getText(),textToVerify.get("homePageDescriptionTxt"));
        }else {
            Assert.assertFalse(true, "Page title isn't displayed");
        }
    }

    public void verifyBackgroundImageExistence(){
        waitDelayTime(6000);
        if(imgBackgroundPatientsFamilies.isDisplayed()){
            System.out.println("Patients families background image is displayed");
        }else {
            takeScreenshot();
            System.out.println("Patients families background image is not displayed");
        }
    }

    public void clickOneTimeContainerBtn(){
            btnOneTimeContainer.click();
    }

    public void clickMonthlyContainerBtn(){
            btnMonthlyContainer.click();
    }

    public void verifyOneTimeValuesDisplayedCorrectly(){
        verifyElementDisplayed(btnOneTime$50);
        verifyElementDisplayed(btnOneTime$100);
        verifyElementDisplayed(btnOneTime$250);
        verifyElementDisplayed(btnOneTime$500);
    }

    public void verifyMonthlyValuesDisplayedCorrectly(){
        verifyElementDisplayed(btnMonthly$15);
        verifyElementDisplayed(btnMonthly$25);
        verifyElementDisplayed(btnMonthly$50);
        verifyElementDisplayed(btnMonthly$100);
    }

    public void clickBtnCloseManageCookies(){
        btnCloseManageCookies.click();
    }









}
