'use strict';
/* App Module */
var petClinicApp = angular.module('petClinicApp', [
    'ngRoute', 'layoutNav', 'layoutFooter', 'layoutWelcome',
    'ownerList', 'ownerDetails', 'ownerForm','petForm', 'vetList']);

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
    }).when('/vets', {
        template: '<vet-list></vet-list>'
    }).otherwise('/welcome');

}]);

angular.module('layoutWelcome', []);

angular.module("layoutWelcome").component("layoutWelcome", {
    templateUrl: "scripts/app/fragments/welcome.html"
});

angular.module('layoutNav', []);

angular.module("layoutNav").component("layoutNav", {
    templateUrl: "scripts/app/fragments/nav.html"
});

angular.module('layoutFooter', []);

angular.module("layoutFooter").component("layoutFooter", {
    templateUrl: "scripts/app/fragments/footer.html"
});