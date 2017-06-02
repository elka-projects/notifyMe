var notifyMe = angular.module('notifyMe', ['ngMaterial', 'ngRoute','ngAnimate','ngAria','ngMessages']);
notifyMe.config(function($mdThemingProvider, $routeProvider){
      $mdThemingProvider
        .theme('default')
        .primaryPalette('amber')
        .accentPalette('red');
      $routeProvider
        .when('/home', {
           templateUrl: 'app/views/home.html'
        })
        .when('/login',{
          templateUrl: 'app/views/login.html'
        }).otherwise({
           redirectTo: '/home'
        });
});

notifyMe.controller('MyController', function($scope, $mdSidenav) {
  $scope.openLeftMenu = function() {
    $mdSidenav('left').toggle();
  };
});

notifyMe.controller('Ctrl', function($scope){
  $scope.vm = {
      formData: {
        email: '',
       	password: ''
      }
  };
});
