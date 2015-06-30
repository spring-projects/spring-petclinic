'use strict';

//Square brackets should only be defined once below (and not in other JS files)
angular.module('controllers', ['services', 'ngResource']);
angular.module('services', ['ngResource']);


/* App Module */
var petClinicApp = angular.module('petClinicApp', [
  'ui.router', 'controllers', 'services'
]);


		
petClinicApp.config(['$stateProvider', '$urlRouterProvider', function($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.otherwise('/');
    
    $stateProvider
    .state('app', {
    	url: '/',
    	controller: 'mainController',
    	views: {
            'header': 	{ templateUrl: 'scripts/app/fragments/bodyHeader.html'},
            'content': 	{ templateUrl: 'scripts/app/main/main.html'},
            'footer': 	{ templateUrl: 'scripts/app/fragments/footer.html'}
        }
      }).
      state('app.ownersearch', {
          url: 'owner/search',
          views: {
              'content@': {
            	  templateUrl: 'scripts/app/owner/ownerSearchForm.html',
                  controller: 'ownerSearchController'
              }
          }
   
      }).
      state('app.ownerlist', {
          url: 'owner/list?lastName',
          views: {
              'content@': {
                  templateUrl: 'scripts/app/owner/ownerList.html',
                  controller: 'ownerListController'
              }
          }
   
      }).
      state('app.ownerdetail', {
          url: 'owner/:id',
          views: {
              'content@': {
                  templateUrl: 'scripts/app/owner/ownerDetail.html',
                  controller: 'ownerDetailController'
              }
          }
   
      }).
      state('app.owneredit', {
          url: 'owner/:id/edit',
          views: {
              'content@': {
                  templateUrl: 'scripts/app/owner/ownerForm.html',
                  controller: 'ownerFormController'
              }
          }
   
      }).
      state('app.vets', {
          url: 'vets',
          views: {
              'content@': {
                  templateUrl: 'scripts/app/vet/vetList.html',
                  controller: 'vetController'
              }
          }
   
      });
  }]);





