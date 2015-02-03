var MainController = function($scope, $rootScope, $state) {
	
	$scope.getSession = function() {
		return $scope.session;
	};
	
	$scope.login = function() {
		$scope.session = { 'username' : 'test' };
	};
	
	$scope.logout = function() {
		$scope.session = null;
	};
	
	$scope.menuTabs = [ {
		'name' : 'Main Page',
		'url' : '#',
		'font' : 'fa fa-home'
	}, {
		'name' : 'Services',
		'url' : '#services',
		'font' : 'fa fa-eyedropper'
	}, {
		'name' : 'Pets',
		'url' : '#pets',
		'font' : 'fa fa-paw'
	}, {
		'name' : 'Veterinarians',
		'url' : '#vets',
		'font' : 'fa fa-user'
	}, {
		'name' : 'About',
		'url' : '#about',
		'font' : 'fa fa-question'
	} ];
	
	$scope.footerText = '© ' + new Date().getFullYear() + ' Pet Clinic, A Spring Framework Demonstration';
	
	$rootScope.$state = $state;
};

var MainControllerDeclaration = ['$scope','$rootScope','$state',MainController];

