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
        })
        .when('/register', {
          templateUrl: 'app/views/register.html'
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
  $scope.logUser = function() {
    console.log("logged in", $scope.vm.formData.email);
  }
});

notifyMe.controller("RegisterCtrl", function ($scope) {
  $scope.vm = {
      formData: {
        email: '',
        password: '',
        lastName: '',
        firstName: '',
        login: ''
      }
  };
});
