(function (ng) {
    var mod = ng.module("viajeroModule");
    mod.controller('OverviewController', ['$scope', '$element', '$stateParams', 'viajeroS',
        function ($scope, $element, $stateParams, svc) {
            var self = this;
            $scope.currentTrip;
            
            /**
             * Displays an error.
             * @param {type} response
             * @returns {undefined}
             */
            function responseError(response) {
                self.showError(response);
            }
            
            function showError(response){
                alert(response.data);
            }
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
            /*
             * Fetches the trip of the user.
             * @returns {undefined}
             */
            this.getItinerario = function () {
                svc.getItinerario($stateParams.userId, $stateParams.tripId).then(function (response) {
                    $scope.currentTrip = response.data;
                    self.generateImage();
                }, responseError);
            };
            
            self.getItinerario();
        }]);
})(window.angular);


