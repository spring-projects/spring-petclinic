'use strict';

angular.module('ownerList')
    .controller('OwnerListController', ['$http', function ($http) {
        var self = this;

        $http.get('owner/list').then(function (resp) {
            self.owners = resp.data;
        });
    }]);
