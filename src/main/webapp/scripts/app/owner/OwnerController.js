'use strict';

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

function loadOwner($scope, $resource, $stateParams) {
	var ownerResource = $resource('/petclinic/owner/' + $stateParams.id);
	$scope.owner =  ownerResource.get();
}

/*
 * Owner Edit Form
 */
angular.module('controllers').controller('ownerFormController', ['$scope', '$resource', '$http', '$stateParams', '$state',
function($scope, $resource, $http, $stateParams, $state) {
	
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
		var restUrl = "/petclinic/owner/" + $stateParams.id;
		$http.put(restUrl, data);
		$state.go('app.ownerlist');
	}
	
	loadOwner($scope, $resource, $stateParams);

}]);



















