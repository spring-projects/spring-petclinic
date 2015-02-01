var app = angular.module('spring-petclinic', ['ui.router','ui.router.stateHelper','ngAnimate','ngCookies']);

app.controller('MainController', MainControllerDeclaration);
app.controller('VeterinarianController', VeterinarianControllerDeclaration);
app.controller('PetController', PetControllerDeclaration);
app.controller('OwnerController', OwnerControllerDeclaration);
app.controller('VisitController', VisitControllerDeclaration);
app.controller('AboutController', AboutControllerDeclaration);

app.config(['stateHelperProvider','$urlRouterProvider','$urlMatcherFactoryProvider',function(stateHelperProvider,$urlRouterProvider,$urlMatcherFactoryProvider) {

	$urlRouterProvider.when("/","/about").otherwise("/about");
	
	$urlMatcherFactoryProvider.strictMode(false)
	
	stateHelperProvider.state({
		name: "home",
		url: "",
		templateUrl: "components/main/main.html",
		controller: "MainController",
		abstract: true
	}).state({
		name: "home.vets",
		url: "/vets",
		templateUrl: "components/veterinarians/veterinarians.html",
		controller: "VeterinarianController",
	}).state({
		name: "home.about",
		url: "/about",
		templateUrl: "components/about/about.html",
		controller: "AboutController"
	}).state({
		name: "home.pets",
		url: "/pets",
		templateUrl: "components/pets/pets.html",
		controller: "PetController"
	}).state({
		name: "home.owners",
		url: "/owners",
		templateUrl: "components/owners/owners.html",
		controller: "OwnerController"
	}).state({
		name: "home.visits",
		url: "/visits",
		templateUrl: "components/visits/visits.html",
		controller: "VisitController"
	});
	
} ]);
