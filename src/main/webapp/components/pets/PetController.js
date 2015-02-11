var PetController = ['$scope', 'Pet', function($scope, Pet) {

	$scope.$on('$viewContentLoaded', function(event){
		$('html, body').animate({
		    scrollTop: $("#pets").offset().top
		}, 1000);
	});
	
	$scope.pets = Pet.query();
	
}];

var AddPetController = ['$scope','$rootScope','PetType','Pet',function($scope,$rootScope,PetType,Pet) {
	$scope.petTypes = PetType.query();
	
	$scope.save = function(){
		Pet.$save($scope.currentPet);
	};
}];