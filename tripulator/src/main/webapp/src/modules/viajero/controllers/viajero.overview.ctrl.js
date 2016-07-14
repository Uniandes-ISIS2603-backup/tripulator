(function (ng) {
    var mod = ng.module("viajeroModule");
    mod.controller('OverviewController', ['$scope', '$element', '$window', '$mdDialog', 'viajeroS', 'countryService', '$stateParams', '$state',
        function ($scope, $element, $window, $mdDialog, svc, countryService, $stateParams, $state) {
            console.log($state.current.name);
            
            $scope.currentTrip = $stateParams.trip;

            /**
             * Prototype method that adds 4 photos to the carrousel.
             * In production, this method should take photos of the countries the user is visiting.
             * @returns {undefined}
             */
            this.generateImage = function () {
                $scope.currentTrip.multimedia = [];
                for (var i = 0; i < 4; i++) {
                    $scope.currentTrip.multimedia.push({
                        id: i,
                        src: 'http://lorempixel.com/' + ($element.width() + i) + '/' + (screen.height - i),
                        name: "image title"
                    });
                }
            };
        }]);
})(window.angular);


