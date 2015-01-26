var app = angular.module('spring-petclinic', ['ngRoute', 'ngCookies', 'ngAnimate']);

app.controller('MainController', MainControllerDeclaration);
app.controller('VeterinarianController', VeterinarianControllerDeclaration);

app.config([ '$routeProvider', function($routeProvider) {
	$routeProvider.when('/', {
		templateUrl : 'components/main/main.html',
		controller : 'MainController'
	}).when('/vets', {
		templateUrl : 'components/veterinarians/veterinarians.html',
		controller : 'VeterinarianController'
	}).when('/about', {
		templateUrl : 'components/about/about.html',
		controller : 'AboutController'
	}).when('/pets', {
		templateUrl : 'components/pets/pets.html',
		controller : 'PetController'
	}).otherwise({
		redirectTo : '/'
	});
} ]);

