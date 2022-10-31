package com.sn.qa.pages.SN;

import com.sn.qa.pages.BasePage;
import com.sn.qa.util.PropertyFileReader;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

public class HomePage extends BasePage {

    static PropertyFileReader textToVerify = new PropertyFileReader("textToVerify.properties", "data/");

    @FindBy(xpath = "//img[@src='resources/images/headerlogo.jpg']")
    private WebElement logoAmpcus;

    @FindBy(xpath = "//*[@href=\"https://www.ampcus.com/\"]")
    private WebElement linkAmpcusInLogo;

    @FindBy(xpath = "//*[@title='Ampcus']")
    private WebElement logoAmpcusInAmpcusHome;


    //header bar elements

    @FindBy(xpath = "(//span[contains(text(),'CI/CD Demo Prototype')])[1]")
    private WebElement labelPageTitle;
    @FindBy(xpath = "(//span[contains(text(),'CI/CD Demo Prototype')])[1]")
    private WebElement labelCiCdDemo;
    @FindBy(xpath = "//*[contains(text(),'This is a simple prototype to demonstrate the kick off of the deployment')]")
    private WebElement labelDescription;





   

    public HomePage(){
        PageFactory.initElements(wd,this);
    }



    public void validateAppLogoExistence(){
        if(logoAmpcus.isDisplayed()) {
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

    public void verifyCiCdPageHeader(){
        if(labelCiCdDemo.isDisplayed()){
            System.out.println("Label is displayed as expected");
        }else {
            Assert.assertFalse(true, "Label isn't displayed");
        }
    }

    public void verifyDescriptionContent(){
        if(labelDescription.isDisplayed()){
        }else {
            Assert.assertFalse(true, "Page title isn't displayed");
        }
    }

    public void clickAmpcusLinkFromLogo(){
        linkAmpcusInLogo.click();
        waitForPageLoad(5);
    }

    public void verifyLandedOnAmpcusHome(){
        if (logoAmpcusInAmpcusHome.isDisplayed()){
        }else {
            Assert.assertFalse(true, "Not landed on Ampcus home");
        }
    }










}
