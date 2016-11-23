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

controllers.controller('OeuvresCtrl', ['OeuvresRest', '$route', '$location',
    function (OeuvresRest, $route, $location) {
        var oeuvresCtrl = this;
        oeuvresCtrl.deleteOeuvre = deleteOeuvre;
        
        // Récupère une promise
        // On référence les méthodes exposées
        var oeuvresPromise = OeuvresRest.getOeuvres();
        
        // Si la requête aboutit (code 200) on affecte le jSon retourné
        // à la variable oeuvresCtrl.oeuvres qui sera affichée
        // par la vue oeuvres.html
        oeuvresPromise.success(function (data) {
            if (data.length > 0) // si la liste n'est pas vide
                oeuvresCtrl.oeuvres = data;
        }).error(function (data) { // Si la requête a provoqué une erreur (ex. 404)
            oeuvresCtrl.error = data; // On affiche l'erreur brute,
            alert(oeuvresCtrl.error);
        });
        
        /**
         * Suppression d'une oeuvre
         * @param {type} id de l'oeuvre à supprimer
         */
        
        function deleteOeuvre(id) {
            if (id) {
                OeuvresRest.supprimerOeuvre(id).success(function (data, status) {
                    if (status === 200) {
                        $location.path('/getOeuvres');
                        $route.reload();
                    }
                }).error(function (data) {
                   oeuvresCtrl.error = data;
                   alert(oeuvresCtrl.error);
                });
            }
        }
    }
]);

controllers.controller('OeuvreCtrl', ['OeuvresRest', '$routeParams', '$location',
    function (OeuvresRest, $routeParams, $location) {
        // Définition du scope
        var oeuvreCtrl = this;
        // On référence les méthodes exposées
        oeuvreCtrl.validateOeuvre = validateOeuvre;
        oeuvreCtrl.cancel = cancel;
        
        // On récupère l'id de l'employé
        oeuvreCtrl.pageTitle = 'une oeuvre';
        oeuvreCtrl.oeuvre_id = $routeParams.id;
        // Si l'id est défini, c'est modification
        // sinon ce sera un ajout
        if (oeuvreCtrl.oeuvre_id)
            oeuvreCtrl.pageTitle = 'Mettre à jour ' +
                oeuvreCtrl.pageTitle;
        else
            oeuvreCtrl.pageTitle = 'Ajouter ' +
                oeuvreCtrl.pageTitle;
        
        // Récupère la liste des propriétaires
        OeuvresRest.getProprietaires().success(function (data) {
            oeuvreCtrl.proprietaires = data;
        });
        
        // S'il s'agit d'une demande de modification, il faut lire l'oeuvre,
        // positionner la liste déroulante propriétaires 
        if (oeuvreCtrl.oeuvre_id > 0) {
            var oeuvreR = OeuvresRest.getOeuvre($routeParams.id);
            oeuvreR.success(function (data, status) {
                if (status === 200) {
                    oeuvreCtrl.oeuvre = data;
                    oeuvreCtrl.selectedOptionProprio = oeuvreCtrl.oeuvre.proprietaire;
                }
            }).error(function (data) {
                oeuvreCtrl.error = data;
                alert(oeuvreCtrl.error);
            });
        }
        
        // On a cliqué sur le bouton Annuler
        function cancel() {
            $location.path('/getOeuvres');
        }
        
        /**
        * On a cliqué sur le bouton valider
        * @param {type} id : id de l'oeuvre modifiée
        * @param {type} form : le formulaire complet
        */
        function validateOeuvre(id, form) {
            // Si tout a été saisi, pas de zone oubliée
            if (form.$valid) {
                // On récupère l'objet oeuvre dans le scope de la vue
                var oeuvre = oeuvreCtrl.oeuvre;

                // La marque décimale doit être le point
                oeuvre.prix = oeuvreCtrl.oeuvre.prix.replace(',', '.');
                
                // Récupération du propriétaire sélectionné
                oeuvre.proprietaire = oeuvreCtrl.selectedOptionProprio;

                // si on a un id => c'est une modification
                if (id) {
                    // Demande de mise à jour de l'oeuvre
                    OeuvresRest.modifierOeuvre(oeuvre).success(function (data, status) {
                        // Si c'est OK on consulte la nouvelle liste des oeuvres
                        // Sinon on affiche l'erreur
                        if (status === 200) {
                            $location.path('/getOeuvres');
                        }
                    }).error(function (data) {
                        oeuvreCtrl.error = data;
                        alert(oeuvreCtrl.error);
                    });
                }
                // Sinon c'est la création d'une nouvelle oeuvre
                else {
                    // Demande d'ajout de l'oeuvre
                    OeuvresRest.ajouterOeuvre(oeuvre).success(function (data, status) {
                        // Si c'est OK on consulte la nouvelle liste des oeuvres
                        // Sinon on affiche l'erreur
                        if (status === 200) {
                            $location.path('/getOeuvres');
                        }
                    }).error(function (data) {
                        oeuvreCtrl.error = data;
                        alert(oeuvreCtrl.error);
                    });
                }
            } else { // On affiche un message d'erreur type
                oeuvreCtrl.error = "Le formulaire n'est pas correct";
            }
        }
    }
]);