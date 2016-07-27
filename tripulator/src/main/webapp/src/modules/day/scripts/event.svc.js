(function (ng) {
    angular.module('DayModule')
        .service('EventService', ['$http', function ($http) {
            function buildRoute(idTraveller, idTrip, idDay) {
                return './api/travellers/' + idTraveller + '/trips/' + idTrip + '/days/' + idDay + '/events/';
            }

            this.saveEvent = function (idTraveller, idTrip, idDay, event) {
                return $http.post(buildRoute(idTraveller, idTrip, idDay), event);
            };

            this.getEvents = function (idTraveller, idTrip, idDay) {
                return $http.get(buildRoute(idTraveller, idTrip, idDay));
            };

            this.deleteEvent = function (idTraveller, idTrip, idDay, idEvent) {
                return $http.delete(buildRoute(idTraveller, idTrip, idDay) + idEvent);
            };
}]);
})(window.angular);
