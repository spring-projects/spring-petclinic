var VeterinarianController = function ($scope, $http) {
	$http.get('api/vets').success(function(data) {
		$scope.veterinarians = data;
	});
	
	$scope.$on('$viewContentLoaded', function(event){
		$('html, body').animate({
		    scrollTop: $("#veterinarians").offset().top
		}, 1000);
	});
	
}

var VeterinarianControllerDeclaration = ['$scope','$http',VeterinarianController];
