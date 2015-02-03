var app = angular.module('spring-petclinic', ['ui.router','ui.router.stateHelper','ngAnimate','ngCookies']);

app.controller('MainController', MainControllerDeclaration);
app.controller('VeterinarianController', VeterinarianControllerDeclaration);
app.controller('PetController', PetControllerDeclaration);
app.controller('OwnerController', OwnerControllerDeclaration);
app.controller('VisitController', VisitControllerDeclaration);
app.controller('AboutController', AboutControllerDeclaration);

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
