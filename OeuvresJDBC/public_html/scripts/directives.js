'use strict';

var directives = angular.module('directives', []);

directives.directive('ngConfirmBoxClick', [
    function () {
        return {
            link: function (scope, element, attr) {
                var msg = attr.ngConfirmBoxClick;
                var clickAction = attr.confirmClick;
                element.bind('click', function (event) {
                    if (window.confirm(msg)) {
                        scope.$eval(clickAction);
                    }
                });
            }
        };
    }
]);