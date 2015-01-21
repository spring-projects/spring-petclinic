var app = angular.module('spring-petclinic', [ 'ui.bootstrap', 'ngRoute', 'ngCookies',
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
	}).when('/about', {
		templateUrl : 'components/about/about.html',
		controller : 'AboutController'
	}).when('/pets', {
		templateUrl : 'components/pets/pets.html',
		controller : 'PetController'
	}).otherwise({
		redirectTo : '/landing'
	});
} ]);

