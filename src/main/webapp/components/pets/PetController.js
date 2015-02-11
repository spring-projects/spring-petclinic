var PetController = ['$scope', 'Pet', function($scope, Pet) {

	$scope.$on('$viewContentLoaded', function(event){
		$('html, body').animate({
		    scrollTop: $("#pets").offset().top
		}, 1000);
	});

	$scope.pets = Pet.query();

}];

var AddPetController = ['$scope','$rootScope','PetType','OwnerPet',function($scope,$rootScope,PetType,OwnerPet) {
	$scope.petTypes = PetType.query();
	$scope.currentPet = {type:{}};

	$scope.save = function(){
		currentOwnerId = $scope.currentOwner.id;

		for (i=0; i<$scope.petTypes.length; i++){
			if ($scope.petTypes[i].id == $scope.currentPet.type.id){
				$scope.currentPet.type.name = $scope.petTypes[i].name;
			}
		}
		OwnerPet.save({ownerId:currentOwnerId},$scope.currentPet, function(pet) {
			$scope.currentOwner.pets.push(pet);
		});
	};
}];