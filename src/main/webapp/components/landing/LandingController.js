var LandingController = function($scope, $rootScope, $sce, $timeout, $location,
		$route, $interval, $cookieStore, $window) {

	$scope.$route = $route;
    $scope.$location = $location;
};

var LandingControllerDeclaration = [ '$scope', '$rootScope', '$sce', '$timeout',
		'$location', '$route', '$interval', '$cookieStore', '$window',
		LandingController ];

