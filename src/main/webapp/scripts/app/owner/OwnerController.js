'use strict';

function loadOwner($scope, $resource, $stateParams) {
	var ownerResource = $resource('/petclinic/owner/' + $stateParams.id);
	$scope.owner =  ownerResource.get();
}



/*
 * Owner Search
 */
angular.module('controllers').controller('ownerSearchController', ['$scope', '$state',
                                                            function($scope, $state) {
	
	$scope.ownerSearchForm = {}; 
	// form always needs to be initialised
	// otherwise we can't read $scope.ownerSearchForm.lastName

	$scope.submitOwnerSearchForm = function() {
		var lastNameValue;
		$state.go('app.ownerlist', {lastName: $scope.ownerSearchForm.lastName});
}}]);

/*
 * Owners List
 */
angular.module('controllers').controller('ownerListController', ['$scope', '$resource', '$stateParams',
             function($scope, $resource, $stateParams) {
	
	var destUrl = '/petclinic/owner/list?lastName=';
	if(angular.isDefined($stateParams.lastName)) {
		destUrl += $stateParams.lastName;
	}
    var ownerResource = $resource(destUrl);
    $scope.ownerList = ownerResource.query();	
}]);

/*
 * Owners detail (used for both Editable and non-editable pages)
 */
angular.module('controllers').controller('ownerDetailController', ['$scope', '$resource', '$stateParams',
               loadOwner
]);



/*
 * Form used to create and edit owners
 */
angular.module('controllers').controller('ownerFormController', ['$scope', '$http', '$resource', '$stateParams', '$state',
function($scope, $http, $resource, $stateParams, $state) {
	
	$scope.submitOwnerForm = {};
	
	$scope.submitOwnerForm = function() {
		var form = $scope.owner;
		
		// Creating a Javascript object
		var data = {
				firstName:	form.firstName,
				lastName: 	form.lastName,
				address: 	form.address,
				city: 		form.city,
				telephone:	form.telephone	
		};
		
		if ($state.current.name == 'app.owneredit') {
			var restUrl = "/petclinic/owner/" + $stateParams.id;
			$http.put(restUrl, data);
			$state.go('app.ownerlist');			
		}
		else { // in case of owner creation
			var restUrl = "/petclinic/owner";
			$http.post(restUrl, data);
			$state.go('app.ownerlist');						
		}
	}
	
	if ($state.current.name == 'app.owneredit') {
		loadOwner($scope, $resource, $stateParams);
	}

}]);



















