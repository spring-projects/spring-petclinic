var app = angular.module('xi-km', [ 'ui.bootstrap', 'ngRoute', 'ngCookies',
		'ngAnimate', 'ngTagsInput', 'angularFileUpload' ]);


app.controller('MenuController', MenuControllerDeclaration);
app.controller('LandingController', LandingControllerDeclaration);
app.controller('VeterinarianController', VeterinarianControllerDeclaration);

app.config([ '$routeProvider', function($routeProvider) {
	$routeProvider.when('/landing', {
		templateUrl : 'components/landing/landing.html',
		controller : 'LandingController'
	}).when('/veterinarians', {
		templateUrl : 'components/veterinarians/veterinarians.html',
		controller : 'VeterinarianController'
	}).when('/help', {
		templateUrl : 'components/help/help.html',
		controller : 'HelpController'
	}).otherwise({
		redirectTo : '/landing'
	});
} ]);

