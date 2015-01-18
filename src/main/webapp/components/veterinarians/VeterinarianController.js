var VeterinarianController = function ($scope, $http) {
	$http.get('api/vets').success(function(data) {
		$scope.veterinarians = data;
	});
	
}

var VeterinarianControllerDeclaration = ['$scope', '$http', VeterinarianController];
