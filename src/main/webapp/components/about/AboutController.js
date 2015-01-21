var AboutController = function($scope, $rootScope, $sce, $timeout, $location,
		$route, $interval, $cookieStore, $window) {

	$scope.$route = $route;
    $scope.$location = $location;
};

var AboutControllerDeclaration = [ '$scope', '$rootScope', '$sce', '$timeout',
		'$location', '$route', '$interval', '$cookieStore', '$window',
		AboutController ];