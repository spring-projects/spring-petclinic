'use strict';
/* App Module */
var petClinicApp = angular.module('petClinicApp', [
    'ngRoute', 'layoutNav', 'layoutFooter', 'layoutWelcome',
    'ownerList', 'ownerDetails', 'ownerForm', 'petForm', 'visits', 'vetList']);

petClinicApp.config(['$locationProvider', '$routeProvider', function ($locationProvider, $routeProvider) {

    $locationProvider.hashPrefix('!');

    $routeProvider.when('/welcome', {
        template: '<layout-welcome></layout-welcome>'
    }).when('/owners/:ownerId', {
        template: '<owner-details></owner-details>'
    }).when('/owners', {
        template: '<owner-list></owner-list>'
    }).when('/owners/:ownerId/edit', {
        template: '<owner-form></owner-form>'
    }).when('/new-owner', {
        template: '<owner-form></owner-form>'
    }).when('/owners/:ownerId/new-pet', {
        template: '<pet-form></pet-form>'
    }).when('/owners/:ownerId/pets/:petId', {
        template: '<pet-form></pet-form>'
    }).when('/owners/:ownerId/pets/:petId/visits', {
        template: '<visits></visits>'
    }).when('/vets', {
        template: '<vet-list></vet-list>'
    }).otherwise('/welcome');

}]);

['welcome', 'nav', 'footer'].forEach(function(c) {
    var mod = 'layout' + c.toUpperCase().substring(0, 1) + c.substring(1);
    angular.module(mod, []);
    angular.module(mod).component(mod, {
        templateUrl: "scripts/app/fragments/" + c + ".html"
    });
});