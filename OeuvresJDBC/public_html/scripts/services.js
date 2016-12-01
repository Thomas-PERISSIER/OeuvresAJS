'use strict';

var services = angular.module('services', []);

/**
* Définition des urls
*/
services.factory('Config', [function () {
    return {
        urlServer: 'http://localhost:8080/OeuvresRestFul/webresources/webservices',
        urlGetConnection:'/getConnexion/',
        urlGetOeuvres: '/getOeuvres',
        urlGetOeuvre: '/getOeuvre/',
        urlGetProprietaires: '/getProprietaires',
        urlUpdateOeuvre: '/modifierOeuvre',
        urlAddOeuvre: '/ajouterOeuvre',
        urlDeleteOeuvre: '/supprimerOeuvre/',
        urlGetAdherents: '/getAdherents',
        urlAddReservation:'/ajouterReservation',
        urlGetReservations: '/getReservations',
        urlDeleteReservation:'/supprimerReservation/',
        urlConfirmReservation:'/confirmerReservation/'
    };
}]);

/**
* Gestion de l'accès aux données, utilise le
* service Config qui contient les urls du serveur RestFul.
* Chaque méthode exposée retourne une promise qui
* sera exploitée dans les contrôleurs
* @param $http
* @param Config
*/
services.factory('OeuvresRest', ['$http', 'Config',
function ($http, Config) {
    // Liste des méthodes exposées
    var oeuvreRest = {
        getOeuvres: getOeuvres,
        getOeuvre: getOeuvre,
        getProprietaires:getProprietaires,
        updateOeuvre:updateOeuvre,
        addOeuvre:addOeuvre,
        deleteOeuvre:deleteOeuvre,
        getAdherents:getAdherents,
        addReservation:addReservation,
        getReservations:getReservations,
        deleteReservation:deleteReservation,
        confirmReservation:confirmReservation
    };
    return oeuvreRest;
    
    function getOeuvres() {
        var url = Config.urlServer + Config.urlGetOeuvres;
        return $http.get(url);
    }
    
    function getOeuvre(id) {
        var url = Config.urlServer + Config.urlGetOeuvre+id;
        return $http.get(url);
    }
    
    function getProprietaires() {
        var url = Config.urlServer + Config.urlGetProprietaires;
        return $http.get(url);
    }

    function updateOeuvre(oeuvre) {
        var url = Config.urlServer + Config.urlUpdateOeuvre;
        return $http.post(url, oeuvre);
    }

    function addOeuvre(oeuvre) {
        var url = Config.urlServer + Config.urlAddOeuvre;
        return $http.post(url, oeuvre);
    }

    function deleteOeuvre(id) {
        var url = Config.urlServer + Config.urlDeleteOeuvre + id;
        return $http.get(url);
    }
    
    function getAdherents() {
        var url = Config.urlServer + Config.urlGetAdherents;
        return $http.get(url);
    }
    
    function addReservation(reservation) {
        var url = Config.urlServer + Config.urlAddReservation;
        return $http.post(url, reservation);
    }
    
    function getReservations() {
        var url = Config.urlServer + Config.urlGetReservations;
        return $http.get(url);
    }
    
    function deleteReservation(idOeuvre, dateReservation) {
        var url = Config.urlServer + Config.urlDeleteReservation + idOeuvre+'-'+dateReservation;
        return $http.get(url);
    }
    
    function confirmReservation(idOeuvre, dateReservation) {
        var url = Config.urlServer + Config.urlConfirmReservation + idOeuvre+'-'+dateReservation;
        return $http.get(url);
    }
}]);
    
/**
 * Gestion de l'authentification
 * @param $http
*  @param Config
 */
services.factory('Connection',
        function ($http, Config) {
            // Expose la méthode getConnexion
            var connection = {
                getConnection: getConnection
            };
            return connection;

            /**
             * Vérifie les valeurs fournies et retourne true ou false
             * @param login
             * @returns {Boolean}
             */
            function getConnection(login) {
                var url = Config.urlServer + Config.urlGetConnection+login;
                return $http.get(url);
            }
        });