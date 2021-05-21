Feature: Add owners


  Scenario: Go to Add Owner page
    Given I go to the main page
    Then I go to the find-owners page
    When I click on the link with heading "Add Owner"
    Then I should see the "Owner" page

  Scenario: Add new owner
    Given I go to add owner page
    Then I fill the field named "firstName" with value "John"
    And I fill the field named "lastName" with value "Smith"
    And I fill the field named "address" with value "50th Street"
    And I fill the field named "city" with value "New York"
    And I fill the field named "telephone" with value "1234567"
    And I submit the form "add-owner-form"
    Then I should see the "Owner Information" page

    Scenario: Try and fail to add new owner without name
      Given I go to add owner page
      Then I fill the field named "firstName" with value ""
      And I fill the field named "lastName" with value "Smith"
      And I fill the field named "address" with value "50th Street"
      And I fill the field named "city" with value "New York"
      And I fill the field named "telephone" with value "1234567"
      And I submit the form "add-owner-form"
      Then I should see "must not be empty" error message

  Scenario: Try and fail to add new owner with name with length>32 characters
    Given I go to add owner page
    Then I fill the field named "firstName" with value "qwertyuiopasdfghjklzxcvbnmqwertyu"
    And I fill the field named "lastName" with value "Smith"
    And I fill the field named "address" with value "50th Street"
    And I fill the field named "city" with value "New York"
    And I fill the field named "telephone" with value "1234567"
    And I submit the form "add-owner-form"
    Then I should see "Something happened..." exception

  Scenario: Try and fail to add new owner with last name with length>32 characters
    Given I go to add owner page
    Then I fill the field named "firstName" with value "John"
    And I fill the field named "lastName" with value "qwertyuiopasdfghjklzxcvbnmqwertyu"
    And I fill the field named "address" with value "50th Street"
    And I fill the field named "city" with value "New York"
    And I fill the field named "telephone" with value "1234567"
    And I submit the form "add-owner-form"
    Then I should see "Something happened..." exception


  Scenario: Try and fail to add new owner without lastname
    Given I go to add owner page
    Then I fill the field named "firstName" with value "John"
    And I fill the field named "lastName" with value ""
    And I fill the field named "address" with value "50th Street"
    And I fill the field named "city" with value "New York"
    And I fill the field named "telephone" with value "1234567"
    And I submit the form "add-owner-form"
    Then I should see "must not be empty" error message

  Scenario: Try and fail to add new owner without address
    Given I go to add owner page
    Then I fill the field named "firstName" with value "John"
    And I fill the field named "lastName" with value "Smith"
    And I fill the field named "address" with value ""
    And I fill the field named "city" with value "New York"
    And I fill the field named "telephone" with value "1234567"
    And I submit the form "add-owner-form"
    Then I should see "must not be empty" error message

  Scenario: Try and fail to add new owner without city
    Given I go to add owner page
    Then I fill the field named "firstName" with value "John"
    And I fill the field named "lastName" with value "Smith"
    And I fill the field named "address" with value "50th Street"
    And I fill the field named "city" with value ""
    And I fill the field named "telephone" with value "1234567"
    And I submit the form "add-owner-form"
    Then I should see "must not be empty" error message

  Scenario: Try and fail to add new owner without telephone
    Given I go to add owner page
    Then I fill the field named "firstName" with value "John"
    And I fill the field named "lastName" with value "Smith"
    And I fill the field named "address" with value "50th Street"
    And I fill the field named "city" with value "New York"
    And I fill the field named "telephone" with value ""
    And I submit the form "add-owner-form"
    Then I should see "must not be empty" error message
    Then I should see "numeric value out of bounds (<10 digits>.<0 digits> expected)" error message

  Scenario: Try and fail to add new owner when fill literals instead of numbers in telephone field
    Given I go to add owner page
    Then I fill the field named "firstName" with value "John"
    And I fill the field named "lastName" with value "Smith"
    And I fill the field named "address" with value "50th Street"
    And I fill the field named "city" with value "New York"
    And I fill the field named "telephone" with value "hello"
    And I submit the form "add-owner-form"
    Then I should see "numeric value out of bounds (<10 digits>.<0 digits> expected)" error message
