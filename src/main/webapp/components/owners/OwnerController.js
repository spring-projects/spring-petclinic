var OwnerController = function($scope) {
	$scope.$on('$viewContentLoaded', function(event){
		$('html, body').animate({
		    scrollTop: $("#owners").offset().top
		}, 1000);
	});
};

var OwnerControllerDeclaration = ['$scope',OwnerController];