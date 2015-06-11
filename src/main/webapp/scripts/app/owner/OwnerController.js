'use strict';

/*
 * Owner Search
 */
angular.module('controllers').controller('ownerSearchController', ['$scope', '$rootScope', '$resource', '$location',
                                                            function($scope, $rootScope, $resource, $location) {
	
	$scope.submitOwnerFindForm = function(item, event) {
	    
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
               function($scope, $resource, $rootScope) {
					var ownerResource = $resource('/petclinic/owner/' + $rootScope.ownerId);
					$scope.owner =  ownerResource.get();
}]);
	
	
	
	