'use strict';

angular.module('petForm', ['ui.router'])
    .config(['$stateProvider', function ($stateProvider) {
        $stateProvider
            .state('petNew', {
                parent: 'app',
                url: '/owners/:ownerId/new-pet',
                template: '<pet-form></pet-form>'
            })
            .state('petEdit', {
                parent: 'app',
                url: '/owners/:ownerId/pets/:petId',
                template: '<pet-form></pet-form>'
            })
    }]);
