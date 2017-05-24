/**
 * Created by ciazhar on 3/16/17.
 */

var app = angular.module('UserPassApp',[]);

app.controller('DummyController',function($http, $scope, $window, $location){
    var urlResourceServer = "http://localhost:8080/api/halo";
    var urlAuthServer = "http://localhost:10000/oauth/authorize?client_id=jsclient&response_type=token";
    var username

    var postData = {
        client_id : "clientapp",
        grant_type : "password",
        username : $scope.username,
        password : $scope.password
    }

    $http.post().then(
        function (response){

        },
        function (response){

        }
    );

    $scope.requestKeResourceServer = function () {

        var token = $window.sessionStorage.getItem('token');
        if (!token){
            alert('Belum Login');
            return;
        }
        $http.get(urlResourceServer+"?access_token="+token).then(
            function (response) {
                $scope.responseDariServer = response.data;
            },
            function (response) {
                alert('Error Code'+response.status+', Error Text : '+response.statusText);
            }
        );
    };
});