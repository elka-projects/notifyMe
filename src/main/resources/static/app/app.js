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

notifyMe.service('sharedProperties', function ($http) {
        var logged = false;
        var jwt = '0';
        var login = '';
        var data;
        var detail = {}
        var projects;
        var projectID;

        return {
            getLogged: function () {
                return logged;
            },
            setLogged: function(value) {
                logged = value;
            },
            setJwt: function(value) {
              jwt = value;
            },
            getJwt: function() {
              return jwt;
            },
            getLogin: function() {
              return login;
            },
            setLogin: function(value) {
              login = value;
            },
            getData: function() {
              return data;
            },
            setData: function(value) {
              data = value;
            },
            setDetail: function(value) {
              detail = value;
            },
            getDetail: function() {
              return detail;
            },
            getProjects: function() {
              return projects;
            },
            setProjects: function(value) {
              projects = value;
            },
            setProjectID: function(value) {
              projectID = value;
            },
            getProjectID: function() {
              return projectID;
            }
        };
    });

notifyMe.controller('MyController', function($scope, $mdSidenav, sharedProperties, $http) {
  $scope.openLeftMenu = function() {
    $mdSidenav('left').toggle();
  };

  $scope.logged = function() {
    return sharedProperties.getLogged();
  }

  $scope.checkLogin = function(path) {
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

  $scope.add = {
    formData: {
      project: '',
      title: '',
      content: ''
    }
  };

  $scope.checkLogin = function(path) {
    if(!sharedProperties.getLogged()){
      window.location='#!/login';
      return false;
    } else {
      window.location='#!/' + path;
      return true;
    }
  }

  $scope.logUser = function() {
    var data = {
      "login": $scope.vm.formData.email,
      "password": $scope.vm.formData.password
    };
    $http({
        method: 'POST',
        url: '/authenticate',
        data: data
    }).then(function (response) {
      if(response.data.jwt !== '0'){
        sharedProperties.setLogged(true);
        sharedProperties.setJwt(response.data.jwt);
        sharedProperties.setLogin($scope.vm.formData.email);
        window.location='#!/main';
      }
    }, function (error) {
            console.log('error', error);
    });
  }

  $scope.notifications = [{title: 'Loading...'}];// = getData();
  $scope.projects = [{title: 'Loading...'}];

  if(sharedProperties.getLogged()){
    $http({
        method: 'GET',
        url: '/data/' + sharedProperties.getLogin(),
        headers: {"jwt": sharedProperties.getJwt()}
    }).then(function (response) {
      if(response.data){
        $scope.notifications = response.data;
        }
    }, function (error) {
      console.log('error', error);
    });
  }

  $scope.detail = sharedProperties.getDetail();

  $scope.showDetails = function(index){
    console.log(index);
    sharedProperties.setDetail($scope.notifications[index]);
    window.location="#!/details"
  }

  $scope.sendNotification = function(){
    var data = {
      "project": $scope.add.formData.project,
      "author": sharedProperties.getLogin(),
      "date": new Date().toString,
      "title": $scope.add.formData.title,
      "content": $scope.add.formData.content
    };
    console.log('dataPOST',data);
    $http({
        method: 'POST',
        url: '/saveTemplate',
        data: data,
        headers: {"jwt": sharedProperties.getJwt()}
    }).then(function (response) {
      $http({
          method: 'POST',
          url: '/sendNotification',
          data: {"jwt": sharedProperties.getJwt(), "id": response.data.id}
      })
      console.log('response',response.data.id)
    }, function (error) {

    });
  }





  $scope.logout = function() {
    sharedProperties.setLogged(false);
    window.location="#!/details";
  }

});

notifyMe.controller('ProjectsController', function($scope, $http, sharedProperties) {

  $scope.projects = [{title: 'Loading...'}];
  if(sharedProperties.getLogged()){
    $http({
        method: 'POST',
        url: '/getUserProjects',
        data: {"login": sharedProperties.getLogin()},
        headers: {"jwt": sharedProperties.getJwt()}
    }).then(function (response) {
      $scope.projects = response.data;
      console.log('data get from post get user projects', response.data);
    }, function (error) {
      console.log('error', error);
    });
  }

  $scope.edit = function(index){
    sharedProperties.setProjectID($scope.projects[index].title);
    window.location="#!/followers";
  }



  $scope.delete = function(index) {
    var data = {
      "projectID": $scope.projects[index].projectID,
    };
    $http({
        method: 'POST',
        url: '/delProject',
        data: data,
        headers: {"jwt": sharedProperties.getJwt()}
    }).then(function (response) {
      console.log('res', response);
      $scope.projects.splice(index,1);
    }, function (error) {
      console.log('err', error);
    });
  }

  $scope.vm = {
      formData: {
        channel: ''
      }

  };

  $scope.add = function() {
    var data = {
      "title": $scope.vm.formData.channel,
      "author": sharedProperties.getLogin()
    };
    $http({
        method: 'POST',
        url: '/saveProject',
        data: data,
        headers: {"jwt": sharedProperties.getJwt()}
    }).then(function (response) {
      $scope.projects.push(data);
      console.log('res', response);
    }, function (error) {
      console.log('err', error);
    });
  }

})

notifyMe.controller('FollowersController', function($scope, $http, sharedProperties){
    $scope.projectName = sharedProperties.getProjectID();
    $scope.users = [{login: 'Loading...'}];
    $scope.vm = {
        formData: {
          name: ''
        }
      };
    $http({
        method: 'GET',
        url: '/projects/' + sharedProperties.getProjectID(),
        headers: {"jwt": sharedProperties.getJwt()}
    }).then(function (response) {
      console.log('res', response);
      $scope.users = response.data;
    }, function (error) {
      console.log('err', error);
    });

    $scope.addUser = function () {
      var data = {
        "login": $scope.vm.formData.name,
        "title": $scope.projectName
      };
      $http({
          method: 'POST',
          url: '/subscribe',
          data: data,
          headers: {"jwt": sharedProperties.getJwt()}
      }).then(function (response) {
        console.log('res', response);
        users.push(data);
      }, function (error) {
        console.log('err', error);
      });
    }

    $scope.removeUser = function (index) {
      var data = {
        "userId": $scope.users[index].id,
        "title": $scope.users[index].login
      };
      $http({
          method: 'POST',
          url: '/unsubscribe',
          data: data,
          headers: {"jwt": sharedProperties.getJwt()}
      }).then(function (response) {
        console.log('res', response);
        $scope.users.splice(index, 1);
      }, function (error) {
        console.log('err', error);
      });
    }
})

notifyMe.controller('DataController', function($scope, sharedProperties, $http) {

  $scope.checkLogin = function(path) {
    if(!sharedProperties.getLogged()){
      window.location='#!/login';
      return false;
    } else {
      window.location='#!/' + path;
      return true;
    }
  }

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


notifyMe.controller("RegisterCtrl", function ($scope, sharedProperties, $http) {
  $scope.vm = {
      formData: {
        email: '',
        password: '',
        lastName: '',
        firstName: '',
        login: ''
      }
  };

  ///rest

  // var data = {
  //   "login": sharedProperties.getLogin(),
  //   "jwt": sharedProperties.getJwt()
  // };
  // $http({
  //     method: 'POST',
  //     url: '/logout',
  //     data: data
  // }).then(function (response) {
  //
  // }, function (error) {
  //
  // });

  // $http({
  //     method: 'GET',
  //     url: '/getUserProjects',
  //     headers: {"jwt": sharedProperties.getJwt(), "login": sharedProperties.getLogin()}
  // }).then(function (response) {
  //
  // }, function (error) {
  //
  // });

  // $http({
  //     method: 'GET',
  //     url: '/projects/' + sharedProperties.getProjectTitle(),
  //     headers: {"jwt": sharedProperties.getJwt()}
  // }).then(function (response) {
  //
  // }, function (error) {
  //
  // });

  // $http({
  //     method: 'GET',
  //     url: '/projectNotifications/' + sharedProperties.getProjectTitle(),
  //     headers: {"jwt": sharedProperties.getJwt()}
  // }).then(function (response) {
  //
  // }, function (error) {
  //
  // });

  // var data = {
  //   "userID": sharedProperties.getuserID(),
  //   "jwt": sharedProperties.getJwt(),
  //   "title": sharedProperties.getProjectTitle()
  // };
  // $http({
  //     method: 'POST',
  //     url: '/subscribe',
  //     data: data
  // }).then(function (response) {
  //
  // }, function (error) {
  //
  // });

  // var data = {
  //   "userID": sharedProperties.getuserID(),
  //   "jwt": sharedProperties.getJwt(),
  //   "title": sharedProperties.getProjectTitle()
  // };
  // $http({
  //     method: 'POST',
  //     url: '/unsubscribe',
  //     data: data
  // }).then(function (response) {
  //
  // }, function (error) {
  //
  // });

  // var data = {
  //   "projectID": sharedProperties.getuserID(),
  //   "jwt": sharedProperties.getJwt(),
  //   "title": sharedProperties.getProjectTitle(),
  //   "author": sharedProperties.getLogin()
  // };
  // $http({
  //     method: 'POST',
  //     url: '/saveProject',
  //     data: data
  // }).then(function (response) {
  //
  // }, function (error) {
  //
  // });

  // var data = {
  //   "projectID": sharedProperties.getProjectID(),
  //   "jwt": sharedProperties.getJwt()
  // };
  // $http({
  //     method: 'POST',
  //     url: '/delProject',
  //     data: data
  // }).then(function (response) {
  //
  // }, function (error) {
  //
  // });

  // var data = {
  //   "jwt": sharedProperties.getJwt(),
  //   "project": sharedProperties.getProjectTitle(),
  //   "author": sharedProperties.getLogin(),
  //   "date": new Date(),
  //   "title": sharedProperties.getTitle(),
  //   "content": sharedProperties.getContent()
  // };
  // $http({
  //     method: 'POST',
  //     url: '/saveTemplate',
  //     data: data
  // }).then(function (response) {
  //
  // }, function (error) {
  //
  // });

  // var data = {
  //   "id": sharedProperties.getTemplateID(),
  //   "jwt": sharedProperties.getJwt()
  // };
  // $http({
  //     method: 'POST',
  //     url: '/delTempalte',
  //     data: data
  // }).then(function (response) {
  //
  // }, function (error) {
  //
  // });

  // var data = {
  //   "userID": sharedProperties.getuserID(),
  //   "jwt": sharedProperties.getJwt(),
  //   "title": sharedProperties.getProjectTitle(),
  //   "author": sharedProperties.getLogin()
  // };
  // $http({
  //     method: 'POST',
  //     url: '/saveProject',
  //     data: data
  // }).then(function (response) {
  //
  // }, function (error) {
  //
  // });

  $scope.register = function() {
    var data = {
      "login": $scope.vm.formData.login,
      "password": $scope.vm.formData.password,
      "mail": $scope.vm.formData.email,
      "lastName": $scope.vm.formData.lastName,
      "firstName": $scope.vm.formData.firstName

    };
          $http({
              method: 'POST',
              url: '/register',
              data: data
          }).then(function (response) {
            if(response.data.result === "true"){
              console.log('registered');
              window.location='#!/home';
            } else {
              console.log("not registered, result false", response);
              window.location='#!/home';
            }
          }, function (error) {
              console.log("not registered", error);
          });
  }

});
