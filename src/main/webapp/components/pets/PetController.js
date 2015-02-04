var PetController = ['$scope', 'Pet', function($scope, Pet) {

	$scope.$on('$viewContentLoaded', function(event){
		$('html, body').animate({
		    scrollTop: $("#pets").offset().top
		}, 1000);
	});
	
	$scope.pets = Pet.query();
	
}];