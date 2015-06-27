'use strict';

/*
 * Owner Search
 */
angular.module('controllers').controller('ownerSearchController', ['$scope', '$rootScope', '$resource', '$state',
                                                            function($scope, $rootScope, $resource, $state) {

	$scope.submitOwnerFindForm = function() {

		var destUrl = '/petclinic/owner/list?lastName='
		if(angular.isDefined($scope.ownerFindForm)) {
			destUrl += $scope.ownerFindForm.lastName;
		}

	    var ownerResource = $resource(destUrl);
	    $rootScope.owners = ownerResource.query();
	    $state.go('app.ownerlist'); //updating URL in address bar
	}}]);

/*
 * Owners List
 */
angular.module('controllers').controller('ownerListController', ['$scope', '$rootScope',
             function($scope, $rootScope, $location) {
               	if ($rootScope.owners!=null){
               		$scope.ownerList = $rootScope.owners;
               	}              
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



















