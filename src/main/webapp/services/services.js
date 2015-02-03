var Owner = ['$resource', function($resource) {
	return $resource('/petclinic/api/owners/:id');
}];

var Pet = ['$resource', function($resource) {
	return $resource('/petclinic/api/owners/:ownerId/pets', {ownerId : '@ownerId'});
}];

var Vet = ['$resource', function($resource) {
	return $resource('/petclinic/api/vets/:vetId');
}];

var Visit = ['$resource', function($resource) {
	return $resource('/petclinic/api/pets/:petId/visits', {petId : '@id'});
}];

