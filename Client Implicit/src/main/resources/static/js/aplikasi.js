/**
 * Created by ciazhar on 3/16/17.
 */

var app = angular.module('ImplicitApp',[]);

app.controller('DummyController',function($http, $scope, $window, $location){
    var urlResourceServer = "http://localhost:8080/api/halo";
    var urlAuthServer = "http://localhost:10000/oauth/authorize?client_id=jsclient&response_type=token";

    $scope.bukaLoginPage = function () {
        $window.location.href = urlAuthServer;
    };

    $scope.ambilTokenDariServer = function () {
        var location = $location.url(); /// ngambil hash yang isinya #access_token=f2b50438-2c3a-4637-b6b9-e469543ff26d&token_type=bearer&expires_in=86399&scope=read%20write
        console.log("Location : "+location);
        var params = location.split("&");///jadi array yang isinya [access_token=f2b50438-2c3a-4637-b6b9-e469543ff26d , token_type=bearer , expires_in=86399 , scope=read%20write]
        console.log("Param : "+params);
        var tokenParam = params[0];///ambile param indek ke 0 yaitu access token
        console.log("token Param : "+tokenParam);
        var token = tokenParam.split("=")[1];
        console.log("token : "+token);
        $window.sessionStorage.setItem('token',token);
    };

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