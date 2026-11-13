Function: Delete flows with dependency safety checks

  As a clinic staff user
  I want to delete Visits, Pets, and Owners safely
  So that I can correct mistakes without losing dependent records

  Background:
    Given the PetClinic application is running

  @smoke
  Scenario: Delete a visit successfully
    Given I have created an owner with a pet and a visit
    When I delete that visit with confirmation
    Then the visit is no longer shown on the owner details page

  @smoke
  Scenario: Prevent deleting a pet when visits exist
    Given I have created an owner with a pet and a visit
    When I attempt to delete the pet with confirmation
    Then the pet is still present on the owner details page
    And I see a message that deletion is blocked because visits exist

  @smoke
  Scenario: Prevent deleting an owner when pets exist
    Given I have created an owner with a pet without visits
    When I attempt to delete the owner with confirmation
    Then the owner is still accessible
    And I see a message that deletion is blocked because pets exist
