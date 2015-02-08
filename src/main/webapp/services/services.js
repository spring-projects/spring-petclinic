var Owner = ['$resource','context', function($resource, context) {
	return $resource(context + '/api/owners/:id');
}];

var OwnerPet = ['$resource','context', function($resource, context) {
	return $resource(context + '/api/owners/:ownerId/pets', {ownerId : '@ownerId'});
}];

var Pet = ['$resource','context', function($resource, context) {
	return $resource(context + '/api/pets/:id');
}];

var Vet = ['$resource','context', function($resource, context) {
	return $resource(context + '/api/vets/:vetId');
}];

var Visit = ['$resource','context', function($resource, context) {
	return $resource(context + '/api/pets/:petId/visits', {petId : '@id'});
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