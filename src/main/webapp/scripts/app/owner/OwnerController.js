'use strict';

/*
 * Owner Search
 */
angular.module('controllers').controller('ownerSearchController', ['$scope', '$rootScope', '$resource', '$location',
                                                            function($scope, $rootScope, $resource, $location) {

	$scope.submitOwnerFindForm = function() {

		var destUrl = '/petclinic/owner/list?lastName='
		if(angular.isDefined($scope.ownerFindForm)) {
			destUrl += $scope.ownerFindForm.lastName;
		}

	    var ownerResource = $resource(destUrl);
	    $rootScope.owners = ownerResource.query();
	    $location.path('/owner/list');
	}}]);

/*
 * Owners List
 */
angular.module('controllers').controller('ownerListController', ['$scope', '$rootScope', '$location',
             function($scope, $rootScope, $location) {
               	if ($rootScope.owners!=null){
               		$scope.ownerList = $rootScope.owners;
               	}

               	$scope.displayOwnerDetail = function(id) {
					var url = "owner/" + id + "/view";
					$rootScope.ownerId = id;
                    $location.path(url);
                }
             }]);

/*
 * Owners detail (used for both Editable and non-editable pages)
 */
angular.module('controllers').controller('ownerDetailController', ['$scope', '$resource', '$rootScope',
               loadOwner
]);

function loadOwner($scope, $resource, $rootScope) {
	var ownerResource = $resource('/petclinic/owner/' + $rootScope.ownerId);
	$scope.owner =  ownerResource.get();
}

/*
 * Owner Edit Form
 */
angular.module('controllers').controller('ownerFormController', ['$scope', '$resource', '$http', '$rootScope', '$location',
function($scope, $resource, $http, $rootScope, $location) {
	
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
		var restUrl = "/petclinic/owner/" + $rootScope.ownerId;
		$http.post(restUrl, data);
		$location.path('/owner/list');
	}
	
	loadOwner($scope, $resource, $rootScope);

}]);



















