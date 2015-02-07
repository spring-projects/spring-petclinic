var app = angular.module('spring-petclinic', ['ui.router','ui.router.stateHelper','ngAnimate','ngCookies','ngResource','ngMockE2E']);


/** Start of Configurable constants **/
app.constant('useMockData', true);
app.constant('context', '/petclinic');
/** End of Configurable constants **/

app.config(['stateHelperProvider','$urlRouterProvider','$urlMatcherFactoryProvider',function(stateHelperProvider,$urlRouterProvider,$urlMatcherFactoryProvider) {

	$urlRouterProvider.otherwise("/");
	
	$urlMatcherFactoryProvider.strictMode(false)
	
	stateHelperProvider.state({
		name: "landing",
		url: "/",
		templateUrl: "components/landing/landing.html",
		controller: "MainController",
		data: { requireLogin : false }
	}).state({
		name: "dashboard",
		url: "/dashboard",
		templateUrl: "components/dashboard/dashboard.html",
		controller: "DashboardController",
		data: { requireLogin : true }
	}).state({
		name: "vets",
		url: "/vets",
		templateUrl: "components/veterinarians/veterinarians.html",
		controller: "VeterinarianController",
		data: { requireLogin : true }
	}).state({
		name: "pets",
		url: "/pets",
		templateUrl: "components/pets/pets.html",
		controller: "PetController",
		data: { requireLogin : true }
	}).state({
		name: "owners",
		url: "/owners",
		templateUrl: "components/owners/owners.html",
		controller: "OwnerController",
		data: { requireLogin : true }
	});
	
} ]);

/** Controllers **/
app.controller('MainController', MainController);
app.controller('DashboardController', DashboardController);
app.controller('VeterinarianController', VeterinarianController);
app.controller('PetController', PetController);
app.controller('OwnerController', OwnerController);
app.controller('VisitController', VisitController);

/** Services **/
app.factory('Owner', Owner);
app.factory('Pet', Pet);
app.factory('Vet', Vet);
app.factory('Visit', Visit);
app.factory('MockService', MockService);

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

app.run(function(useMockData, MockService) {
	MockService.mock(useMockData);
});