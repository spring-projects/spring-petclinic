var MainController = function($scope, $rootScope, $sce, $timeout, $location,
    $route, $interval, $cookieStore, $window) {

  $scope.$route = $route;
  $scope.$location = $location;
  $scope.footerText = '© ' + new Date().getFullYear() + ' Pet Clinic, A Spring Framework Demonstration';
};

var MainControllerDeclaration = [ '$scope', '$rootScope', '$sce', '$timeout','$location', '$route', '$interval', '$cookieStore', '$window', MainController ];

