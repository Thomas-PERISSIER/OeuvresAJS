'use strict';

var services = angular.module('services', []);

/**
* Définition des urls
*/
services.factory('Config', [function () {
    return {
        urlServer: 'http://localhost:8080/OeuvresRestFul/webresources/webservices',
        urlGetOeuvres: '/getOeuvres',
        urlGetOeuvre: '/getOeuvre',
        urlGetProprietaires: '/getProprietaires',
        urlUpdateOeuvre: '/modifierOeuvre',
        urlAddOeuvre: '/ajouterOeuvre',
        urlDeleteOeuvre: '/deleteOeuvre'
    };
}]);

/**
* Gestion de l'accès aux données, utilise le
* service Config qui contient les urls du serveur RestFul.
* Chaque méthode exposée retourne une promise qui
* sera exploitée dans les contrôleurs
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
        deleteOeuvre:deleteOeuvre
    };
    return oeuvreRest;
    
    function getOeuvres() {
        var url = Config.urlServer + Config.urlGetOeuvres;
        return $http.get(url);
    }
    
    function getOeuvre() {
        var url = Config.urlServer + Config.urlGetOeuvre;
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
}]);
    
/**
 * Gestion de l'authentification
 */
services.factory('Connection',
        function () {
            // Expose la méthode getConnexion
            var connection = {
                getConnection: getConnection
            };
            return connection;

            /**
             * Vérifie les valeurs fournies et retourne true ou false
             * @param login
             * @param mdp
             * @returns {Boolean}
             */
            function getConnection(login, mdp) {
                var OK = false;
                if ((login === "admin") && (mdp === "mdp"))
                    OK = true;
                return OK;
            }
        });