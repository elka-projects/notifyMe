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
        })
        .when('/main', {
          templateUrl: 'app/views/mainView.html'
        })
        .when('/details', {
          templateUrl: 'app/views/details.html'
        })
        .when('/logout', {
          templateUrl: 'app/views/logout.html'
        })
        .when('/add', {
          templateUrl: 'app/views/add.html'
        })
        .when('/topics', {
          templateUrl: 'app/views/topics.html'
        })
        .when('/followers', {
          templateUrl: 'app/views/followers.html'
        })
        .otherwise({
           redirectTo: '/home'
        });
});

notifyMe.service('sharedProperties', function () {
        var logged = false;

        return {
            getLogged: function () {
                return logged;
            },
            setLogged: function(value) {
                logged = value;
            }
        };
    });

notifyMe.controller('MyController', function($scope, $mdSidenav, sharedProperties) {
  $scope.openLeftMenu = function() {
    $mdSidenav('left').toggle();
  };

  $scope.logged = function() {
    return sharedProperties.getLogged();
  }

  $scope.checkLogin = function(path) {
    console.log($scope.logged);
    if(!sharedProperties.getLogged())
      window.location='#!/login';
    else
      window.location='#!/' + path;
  }

  $scope.vm = {
      formData: {
        email: '',
        password: ''
      }

  };

  $scope.logUser = function() {
    sharedProperties.setLogged(true);
    console.log("logged in", $scope.vm.formData.email, $scope.logged);
  }

  $scope.logout = function() {
    sharedProperties.setLogged(false);
  }

});

notifyMe.controller('DataController', function($scope) {
  $scope.notifications = [
    {
      author: 'Wiktor Grzesiuk',
      date: '2012-04-23T18:25:43.511Z',
      channel: 'Projekt PIK',
      topic: 'Projekt PIK, nie zdazymy',
      id: '123131312312321321'
    },
    {
      author: 'Michal Glinka',
      date: '2013-04-23T18:25:43.511Z',
      channel: 'Moje zycie',
      topic: 'Jestem Pedalem',
      id: '123131312312321321'
    },
    {
      author: 'Wiktor Grzesiuk',
      date: '2014-04-23T18:25:43.511Z',
      channel: 'Cytaty Zbigniewa Stonogi',
      topic: 'Was sie powinno jebac tak jak was tylko moze jebac pis',
      id: '123131312312321321'
    }
  ];

  $scope.detail = {
    author: 'Wiktor Grzesiuk',
    date: '2012-04-23T18:25:43.511Z',
    channel: 'Projekt PIK',
    topic: 'Projekt PIK, nie zdazymy',
    id: '123131312312321321',
    message: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.'
  };

  $scope.channels = [
      'Projekt PIK', 'Cytaty Zbigniewa Stonogi', 'Moje zycie'
    ];

  $scope.users = [
    'Wiktor Grzesiuk', 'Michal Glinka'
  ];

  $scope.addUser = function() {
    $scope.users.push($scope.vm.formData.name);
  }

  $scope.addChannel = function() {
    $scope.channels.push($scope.vm.formData.channel);
  }

  $scope.vm = {
      formData: {
        name: '',
        channel: ''
      }

  };
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
