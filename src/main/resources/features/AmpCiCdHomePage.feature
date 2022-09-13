Feature: AmpCi/Cd Home Page

  Background:
    Given user lands on the homepage of the application

   @HP_1
  Scenario: User validates landing on AmpCiCd Home Page and expected elements of the page existence
#    When user verifies the web page title is ""
    Then verify application logo
#    Then user verifies that the home page title is displayed
    And user verifies that the expected texts of the page are displayed

