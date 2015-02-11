var OwnerController = ['$scope','$state','Owner',function($scope,$state,Owner) {
	$scope.$on('$viewContentLoaded', function(event){
		$('html, body').animate({
		    scrollTop: $("#owners").offset().top
		}, 1000);
	});

	$scope.owners = Owner.query();
}];

var OwnerDetailsController = ['$scope','$rootScope','$stateParams','Owner', function($scope,$rootScope,$stateParams,Owner) {
	
	var currentId = $stateParams.id;
	var nextId = parseInt($stateParams.id) + 1;
	var prevId = parseInt($stateParams.id) - 1;
	
	$scope.prevOwner = Owner.get({id:prevId});
	$scope.nextOwner = Owner.get({id:nextId});
	$scope.currentOwner = Owner.get($stateParams);
	
	$scope.saveOwner = function(){
		owner = $scope.currentOwner;
		Owner.save(owner);
	}
	
	$scope.setCurrentOwner = function() {
		$rootScope.currentOwner = $scope.currentOwner;
	}
	
}];

var AddOwnerController = ['$scope','Owner', function($scope,Owner) {
	
	$scope.owner={id:0,pets:[]};
	
	$scope.addOwner = function(){
		Owner.save($scope.owner);
	}
}];