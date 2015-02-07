var Owner = ['$resource', function($resource) {
	return $resource('/petclinic/api/owners/:id');
}];

var OwnerPet = ['$resource', function($resource) {
	return $resource('/petclinic/api/owners/:ownerId/pets', {ownerId : '@ownerId'});
}];

var Pet = ['$resource', function($resource) {
	return $resource('/petclinic/api/pets/:id');
}];

var Vet = ['$resource', function($resource) {
	return $resource('/petclinic/api/vets/:vetId');
}];

var Visit = ['$resource', function($resource) {
	return $resource('/petclinic/api/pets/:petId/visits', {petId : '@id'});
}];

var MockService = ['$httpBackend', '$http', 'context', function($httpBackend, $http, context) {
	return {
		mock : function(useMockData) {
			
			if(useMockData) {
				$http.get(context + '/static/mock-data/pets.json').success(function(data) {
					console.log("Mocking /api/pets");
					$httpBackend.whenGET(context + '/api/pets').respond(data);
				});
				$http.get(context + '/static/mock-data/vets.json').success(function(data) {
					console.log("Mocking /api/vets");
					$httpBackend.whenGET(context + '/api/vets').respond(data);
				});
				$http.get(context + '/static/mock-data/owners.json').success(function(data) {
					console.log("Mocking /api/owners");
					$httpBackend.whenGET(context + '/api/owners').respond(data);
				});
			}
			
			console.log("Setting up passthrough for other urls");
			var passThroughRegex = new RegExp('/');
			$httpBackend.whenGET(passThroughRegex).passThrough();
		}
	}	
}];