'use strict';

angular.module('ownerList', ['ui.router'])
    .config(['$stateProvider', function ($stateProvider) {
        $stateProvider
            .state('owners', {
                parent: 'app',
                url: '/owners',
                template: '<owner-list></owner-list>'
            })
    }]);