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
            	  controller: 'ownerSearchController',
            	  templateUrl: 'scripts/app/owner/ownerSearchForm.html'
              }
          }
   
      }).
      state('app.ownerlist', {
          url: 'owner/list?lastName',
          views: {
              'content@': {
                  controller: 'ownerListController',
                  templateUrl: 'scripts/app/owner/ownerList.html'
              }
          }
   
      }).
      state('app.ownerdetail', {
          url: 'owner/:id',
          views: {
              'content@': {
            	  controller: 'ownerDetailController',
                  templateUrl: 'scripts/app/owner/ownerDetail.html'
              }
          }
   
      }).
      state('app.ownercreate', {
          url: 'owner',
          views: {
              'content@': {
            	  controller: 'ownerFormController',
                  templateUrl: 'scripts/app/owner/ownerForm.html'
              }
          }
   
      }).
      state('app.owneredit', {
          url: 'owner/:id/edit',
          views: {
              'content@': {
                  controller: 'ownerFormController',
                  templateUrl: 'scripts/app/owner/ownerForm.html'
              }
          }
   
      }).
      state('app.vets', {
          url: 'vets',
          views: {
              'content@': {
            	  controller: 'vetController',
                  templateUrl: 'scripts/app/vet/vetList.html'
              }
          }
   
      }).
    state('app.petedit', {
        url: 'owner/:ownerid/pet/:petid',
        views: {
            'content@': {
            	controller: 'petFormController',
                templateUrl: 'scripts/app/pet/petForm.html'
            }
        }
 
    }).
    state('app.petcreate', {
        url: 'owner/:ownerid/pet',
        views: {
            'content@': {
            	controller: 'petFormController',
                templateUrl: 'scripts/app/pet/petForm.html'
            }
        }
 
    });
  }]);





