Scenario: Check response body for vets webservice
Then response from vets webservice contains parameter vets.vetList.id[0] with value 1
Then response from vets webservice contains parameter vets.vetList.firstName[0] with value James