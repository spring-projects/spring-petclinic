var app = angular.module('spring-petclinic', ['ui.router','ui.router.stateHelper','ngAnimate','ngCookies','ngResource']);

app.config(['stateHelperProvider','$urlRouterProvider','$urlMatcherFactoryProvider',function(stateHelperProvider,$urlRouterProvider,$urlMatcherFactoryProvider) {

	$urlRouterProvider.otherwise("/");
	
	$urlMatcherFactoryProvider.strictMode(false)
	
	stateHelperProvider.state({
		name: "landing",
		url: "/",
		templateUrl: "components/landing/landing.html",
		controller: "MainController",
	}).state({
		name: "vets",
		url: "/vets",
		templateUrl: "components/veterinarians/veterinarians.html",
		controller: "VeterinarianController",
	}).state({
		name: "pets",
		url: "/pets",
		templateUrl: "components/pets/pets.html",
		controller: "PetController"
	}).state({
		name: "owners",
		url: "/owners",
		templateUrl: "components/owners/owners.html",
		controller: "OwnerController"
	});
	
} ]);

/** Controllers **/
app.controller('MainController', MainController);
app.controller('VeterinarianController', VeterinarianController);
app.controller('PetController', PetController);
app.controller('OwnerController', OwnerController);
app.controller('VisitController', VisitController);

/** Services **/
app.factory('Owner', Owner);
app.factory('Pet', Pet);
app.factory('Vet', Vet);
app.factory('Visit', Visit);

/** Directives **/

app.directive('scrollToTarget', function() {
  return function(scope, element) {
    element.bind('click', function() {
    	angular.element('html, body').stop().animate({
			scrollTop: angular.element(angular.element(element).attr('href')).offset().top - 20
		}, 1500);
		return false;
    });
  };
});