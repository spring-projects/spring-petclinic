'use strict';

angular.module('vetList', ['ui.router'])
    .config(['$stateProvider', function ($stateProvider) {
        $stateProvider
            .state('vets', {
                parent: 'app',
                url: '/vets',
                template: '<vet-list></vet-list>'
            })
    }]);