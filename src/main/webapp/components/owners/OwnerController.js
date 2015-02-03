var OwnerController = ['$scope',function($scope) {
	$scope.$on('$viewContentLoaded', function(event){
		$('html, body').animate({
		    scrollTop: $("#owners").offset().top
		}, 1000);
	});
}];