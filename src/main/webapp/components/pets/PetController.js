var PetController = function($scope, $rootScope, $sce, $timeout, $location,
		$route, $interval, $cookieStore, $window) {

	$scope.$route = $route;
    $scope.$location = $location;
};

var PetControllerDeclaration = [ '$scope', '$rootScope', '$sce', '$timeout',
		'$location', '$route', '$interval', '$cookieStore', '$window',
		PetController ];