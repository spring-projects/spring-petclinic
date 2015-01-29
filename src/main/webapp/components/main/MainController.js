var MainController = function($scope, $rootScope, $sce, $timeout, $location,
    $route, $interval, $cookieStore, $window) {

  $scope.$route = $route;
  $scope.$location = $location;
  $scope.footerText = '© ' + new Date().getFullYear() + ' Pet Clinic, A Spring Framework Demonstration';
  
  $scope.scrollToVet = function(){
		$('html, body').animate({
			scrollTop : $("#veterianarians").offset().top
		}, 1000);
  }
};

var MainControllerDeclaration = [ '$scope', '$rootScope', '$sce', '$timeout','$location', '$route', '$interval', '$cookieStore', '$window', MainController ];

