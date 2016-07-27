(function (ng) {
    angular.module('TripsModule')
        .service('TripsService', ['$http', function ($http) {
            let route = './api/travellers/';

            this.saveTrip = function (idTraveller, trip) {
                return $http.post(route + idTraveller + '/trips/', trip);
            };

            this.getTrips = function (idTraveller) {
                return $http.get(route + idTraveller + '/trips/');
            };

            this.deleteTrip = function (idTraveller, idTrip) {
                return $http.delete(route + idTraveller + '/trips/' + idTrip);
            };
}]);
})(window.angular);
