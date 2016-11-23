'use strict';

var services = angular.module('services', []);

/**
 * Définition des urls
 */
services.factory('Config', [function () {
        return {
            
        };
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