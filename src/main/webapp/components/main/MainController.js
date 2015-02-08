var MainController =  ['$scope','$rootScope','$state','$sessionStorage', 'context', function($scope, $rootScope, $state, $sessionStorage, context) {
	
	$scope.$storage = $sessionStorage;
	
	$scope.getSession = function() {
		return $scope.$storage.session;
	};
	
	$scope.login = function() {
		$scope.$storage.session = { 'username' : 'test' };
		$state.go('dashboard');
	};
	
	$scope.logout = function() {
		$scope.$storage.session = null;
		$state.go('landing');
	};
	
	$scope.goHome = function() {
		if ($scope.getSession() == null) {
			$state.go('landing');
		} else {
			$state.go('dashboard');
		}
	}
	
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
	
	$scope.context = context;
	
	$scope.footerText = '© ' + new Date().getFullYear() + ' Pet Clinic, A Spring Framework Demonstration';
	
	$rootScope.$state = $state;
	
	$rootScope.$on('$stateChangeStart', function (event, toState, toParams) {
		var requireLogin = toState.data.requireLogin;

		if (requireLogin && $scope.getSession() == null) {
			event.preventDefault();
			$state.go('landing');
		}
	});
}];

