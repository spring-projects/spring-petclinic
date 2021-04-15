Feature: Find owners

  Scenario: Find owners page
    When I click on the link with title "find owners"
    Then I should see the "Find Owners" page

  Scenario: Should find an owner
    When I fill the field named "lastName" with value "Franklin"
    Then I should see the "Owner Information" page

  Scenario: Should find multiple owners
    When I fill the field named "lastName" with value "Davis"
    Then I should see the "Owners" page
    # Add additional checks here.
