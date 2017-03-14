/**
 * Created by ciazhar on 3/13/17.
 */

var aplikasi = angular.module('OauthClientCred',[]);

aplikasi.controller('DemoController',function ($scope, $window, $location) {
    var resourceServerUrl = "http://localhost:8080/api/halo";
    var authorizationServerUrl = "http://localhost:10000/oauth/authorize?client_id=jsclient&response_type=token";

    $scope.bukaLoginPage = function(){
        $window.location.href = authorizationServerUrl;
    };

});