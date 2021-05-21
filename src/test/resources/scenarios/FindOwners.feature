Feature: Find owners

  Scenario: Find owners page
    Given I go to the main page
    When I click on the link with title "find owners"
    Then I should see the "Find Owners" page

  Scenario: Should find an owner
    Given I go to the find-owners page
    When I fill the field named "lastName" with value "Franklin"
    And I submit the form "search-owner-form"
    Then I should see the "Owner Information" page
    Then I should see owner named "Franklin"

  Scenario: Should find multiple owners
    Given I go to the find-owners page
    When I fill the field named "lastName" with value "Davis"
    And I submit the form "search-owner-form"
    Then I should see the "Owners" page
    Then I should see owners named "Davis"

    Scenario: Should find all users when input field is empty
      Given I go to the find-owners page
      When I fill the field named "lastName" with value ""
      And I submit the form "search-owner-form"
      Then I should see the "Owners" page

      Scenario: Should see error message when wrong lastname is entered
        Given I go to the find-owners page
        When I fill the field named "lastName" with value "Brown"
        And I submit the form "search-owner-form"
        Then I should see "has not been found" error

  Scenario: Should see error message when wrong few white spaces are entered
    Given I go to the find-owners page
    When I fill the field named "lastName" with value "Brown"
    And I submit the form "search-owner-form"
    Then I should see "has not been found" error

    Scenario: Should see error message when 'number' lastname is entered
    Given I go to the find-owners page
    When I fill the field named "lastName" with value "12345"
    And I submit the form "search-owner-form"
    Then I should see "has not been found" error

  Scenario: Should see error message when 'empty spaces' lastname is entered
    Given I go to the find-owners page
    When I fill the field named "lastName" with value "  "
    And I submit the form "search-owner-form"
    Then I should see "has not been found" error

