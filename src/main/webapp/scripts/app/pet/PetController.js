'use strict';

function loadPet($scope, $resource, $stateParams) {
	var petResource = $resource('/petclinic/owner/' + $stateParams.ownerid +"/pet/" +$stateParams.petid);
	$scope.pet =  petResource.get();
}




/*
 * Form used to create and edit pets
 */
angular.module('controllers').controller('petFormController', ['$scope', '$http', '$resource', '$stateParams', '$state',
function($scope, $http, $resource, $stateParams, $state) {
	
	$scope.submitPetForm = {};
	
	$scope.submitPetForm = function() {
		var form = $scope.pet;
		
		// Creating a Javascript object
		var data = {
				name:		form.name,
				birthDate: 	form.birthDate,
				type: 		form.type
		};
		
		if ($state.current.name == 'app.petedit') {
			var restUrl = "/petclinic/owner/" + $stateParams.ownerid +"/pet/" +$stateParams.petid;
			$http.put(restUrl, data);
			$state.go('app.ownerdetail');			
		}
		else { // in case of pet creation
			var restUrl = "/petclinic/owner/" + $stateParams.ownerid +"/pet";
			$http.post(restUrl, data);
			$state.go('app.ownerdetail');						
		}
	}
	
	if ($state.current.name == 'app.petedit') {
		loadPet($scope, $resource, $stateParams);
	}

}]);



















