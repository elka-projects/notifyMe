var notifyMe = angular.module('notifyMe', ['ngMaterial', 'ngRoute']);
notifyMe.config(function($mdThemingProvider, $routeProvider){
      $mdThemingProvider
        .theme('default')
        .primaryPalette('amber')
        .accentPalette('red');
      $routeProvider
        .when('/home', {
           templateUrl: 'app/views/home.html'
        }).otherwise({
           redirectTo: '/home'
        })
});
