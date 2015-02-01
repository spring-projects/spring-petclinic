var PetController = function($scope) {

	$scope.$on('$viewContentLoaded', function(event){
		$('html, body').animate({
		    scrollTop: $("#pets").offset().top
		}, 1000);
	});
	
};

var PetControllerDeclaration = ['$scope',PetController];