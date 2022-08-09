Feature: ACS Home Page

  Background:
    Given user lands on the homepage of the application

   @HP_1
  Scenario: User validates landing on ACS Home Page and expected elements of the page existence
    When user verifies the web page title is "Donate Today | The American Cancer Society"
    Then verify application logo
    And user verifies that the background image for home page is displayed
    Then user verifies that the home page title is displayed
    And user verifies that the expected texts of the page are displayed
    Then user verifies that the One Time values are displayed correctly
    Then user clicks Monthly button for frequency
    And user verifies that the Monthly values are displayed correctly
