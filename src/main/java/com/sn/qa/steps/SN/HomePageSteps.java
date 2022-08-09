package com.sn.qa.steps.SN;

import com.sn.qa.pages.SN.HomePage;
import com.sn.qa.steps.BaseSteps;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class HomePageSteps extends BaseSteps {

    String sourceDir = "./src/test/resources/";


    HomePage homePage = new HomePage();


    public HomePageSteps(){
    }


    @Given("^user lands on the homepage of the application$")
    public void user_lands_on_the_homepage_of_the_application() throws Throwable {
        try {
            homePage.goToGMHomePage();
        }catch (Exception e){
        
        }
    }

    @And("^verify application logo$")
    public void verify_application_logo() throws Throwable {
       homePage.validateAppLogoExistence();
    }

    @When("^user verifies the web page title is \"([^\"]*)\"$")
    public void verifyWebPageTitle(String title) throws Throwable {
        homePage.verifyWindowTitle(title);
    }

    @When("^user verifies that the home page title is displayed$")
    public void verifyHomePageTitle() throws Throwable {
        homePage.verifyPageTitleText();
    }

    @When("^user verifies that the background image for home page is displayed$")
    public void verifyHomePageBackgroundImage() throws Throwable {
        homePage.verifyBackgroundImageExistence();
    }

    @When("^user verifies that the expected texts of the page are displayed$")
    public void verifyHomePageTextsDisplayed() throws Throwable {
        homePage.verifyDonateTodayText();
        homePage.verifyDescriptionContent();
    }

    @When("^user verifies that the One Time values are displayed correctly$")
    public void verifyOneTimeValuesDisplayed() throws Throwable {
        homePage.verifyOneTimeValuesDisplayedCorrectly();
    }

    @When("^user clicks Monthly button for frequency$")
    public void userClicksMonthlyBtn() throws Throwable {
        homePage.clickMonthlyContainerBtn();
    }

    @Then("^user verifies that the Monthly values are displayed correctly$")
    public void verifyMonthlyValuesDisplayed() throws Throwable {
        homePage.verifyMonthlyValuesDisplayedCorrectly();
    }

    @Then("^user clciks close button to close Manage Cookies footer popup$")
    public void clickCLoseManageCookies() throws Throwable {
        homePage.verifyMonthlyValuesDisplayedCorrectly();
    }










}
