var VeterinarianController = ['$scope','$http','Vet', function ($scope, $http, Vet) {
	
	$scope.$on('$viewContentLoaded', function(event){
		$('html, body').animate({
		    scrollTop: $("#veterinarians").offset().top
		}, 1000);
	});
	
	$scope.vets = Vet.query();
	
}];