'use strict';

/**
 * Déclaration de la variable app qui sera utilisée après.
 * Injection des modules externes (principalement Angular) et
 * des modules internes (ceux qu'on a développés)
 */

var app = angular.module('app', ['ngRoute', 'ngAnimate', 'ui.bootstrap']);

/**
 * Définition des constantes de configuration et injection des modules
 * externes nécessaires : $routeProvider => routage
 * @param $routeProvider
 */
app.config(['$routeProvider',
    function ($routeProvider) {
        $routeProvider
                .when('/home', {
                    templateUrl: 'partials/home.html'
                })
                .when('/connect', {
                    templateUrl: 'partials/connect.html',
                    controller: 'ConnectionCtrl as connectionCtrl'
                })
                .when('/disconnect', {
                    templateUrl: 'partials/home.html',
                    controller: 'MainCtrl as mainCtrl'
                })
                .when('/getOeuvres', {
                    redirectTo: '/home'
                })
                .when('/addOeuvre', {
                    redirectTo: '/home'
                })
                .when('/getReservations', {
                    redirectTo: '/home'
                })
                .otherwise({
                    redirectTo: '/home'
                });
    }
]);