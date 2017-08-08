Feature: Browsing around

  Scenario: The home page
    Given I am on the home page
    Then I should see "PetClinic"

  Scenario: Vets
    Given I am on the home page
    And I follow "/vets.html"
    Then I should be on the vets, "Veterinarians", page
    And I should see "Veterinarians" within h2

  Scenario: Owners
    Given I am on the home page
    And I follow "/owners/find.html"
    When I fill in "lastName" with "Franklin"
    And I press "Find Owners"
    Then I should see "George"
    And I should see "Franklin"