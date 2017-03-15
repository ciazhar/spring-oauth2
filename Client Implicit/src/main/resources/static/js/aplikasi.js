/**
 * Created by ciazhar on 3/13/17.
 */

var aplikasi = angular.module('OauthClientImpl',[]);

aplikasi.controller('DemoController',function ($scope, $window, $location) {
    var resourceServerUrl = "http://localhost:8080/api/halo";
    var authorizationServerUrl = "http://localhost:10000/oauth/authorize?client_id=jsclient&response_type=token";

    $scope.bukaLoginPage = function(){
        $window.location.href = authorizationServerUrl;
    };

    $scope.ambilTokenDariUrl = function () {
        var location = $location.url();
        console.log("Location : "+location);
        var params = location.split("&");
        console.log("Params : "+params);
        var tokenParam = params[0];
        var token = tokenParam.split("=")[1];
        console.log("Token : "+token);
        $window.sessionStorage.setItem('token',token);
    };

    $scope.requestKeResourceServer = function () {
        var token = $window.sessionStorage.getItem('token');
        if (!token){
            alert('Belum Login, silahkan klik login dulu. Kemudain klik ambil token dari url');
            return;
        }

        $http.get(resourceServerUrl+"?access_token="+token).then(
            function (response) {
                $scope.responseDariServer = response.data;
            },
            function (response) {
                alert('Error Code : '+response.status+', Error Text : '+response.statusText);
            }
        );
    };
});