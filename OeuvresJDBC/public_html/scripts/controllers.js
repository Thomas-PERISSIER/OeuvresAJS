'use strict';

/**
 * Déclaration du module controllers qui rassemblera tous les contrôleurs
 */

var controllers = angular.module('controllers', []);

/**
 * Pilote la déconnexion
 * @param $rootScope
 * @param $location
 * 
 */
controllers.controller('MainCtrl', ['$rootScope', '$location',
    function ($rootScope, $location) {
        var mainCtrl = this;
        
        // On référence les méthodes exposées
        mainCtrl.disConnect = disConnect;
        
        // Par defaut on n'est pas autentifié
        $rootScope.isConnected = false;
        
        /**
         * Déconnexion : isConnected passe à faux => le menu disparaîtra
         * On recharge la page principale
         */
        function disConnect() {
            $rootScope.isConnected = false;
            $location.path('/home');
        }
    }
]);

/**
* Contrôleur de la page connect.html qui 
* permet de s'authentifier et d'accéder aux
* fonctionnalités
* @param $rootScope
* @param Connection
* @param $location
*/
controllers.controller('ConnectionCtrl', ['$rootScope', 'Connection', '$location',
    function ($rootScope, Connection, $location) {
        var connectionCtrl = this;
        
        // On référence les méthodes exposées
        connectionCtrl.signIn = signIn;
        connectionCtrl.login = "";
        connectionCtrl.mdp = "";
        
        /**
        * Appelle le service Connection avec le login
        * et le pwd. Si Ok redirige vers la page d'accueil
        * sinon affiche un message d'erreur dans la langue en cours
        * @param login
        * @param pwd
        */
        function signIn(login, pwd) {
            connectionCtrl.error = "";
            $rootScope.isConnected = Connection.getConnection(login, pwd);
            if ($rootScope.isConnected)
                $location.path('/home');
            else
                connectionCtrl.error = "Impossible de se connecter avec ces identifiants...";
        }
}]);