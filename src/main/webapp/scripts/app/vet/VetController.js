'use strict';

/* Controllers */

angular.module('controllers').controller('vetController', ['$scope', '$resource', 
  function($scope, $resource) {
	var vetResource = $resource('vets');
	
    $scope.vetList = vetResource.query();
    $scope.orderProp = 'name';
  }]);


