'use strict';

angular.module('ownerDetails', [
    'ngRoute'
]);

angular.module("ownerDetails").component("ownerDetails", {
    templateUrl: "scripts/owner-details/owner-details.template.html",
    controller: ["$http", '$routeParams', function($http, $routeParams) {
        var self = this;

        $http.get('owner/' + $routeParams.ownerId).then(function(resp) {
            self.owner = resp.data;
        });
    }]
});