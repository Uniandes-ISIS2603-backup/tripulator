(function (ng) {
    angular.module('LoginModule')
        .service('TravellerService', ['$http', function ($http) {
            let route = './api/travellers/';

            this.saveTraveller = function (traveller) {
                return $http.post(route, traveller);
            };

            this.getTraveller = function (idTraveller) {
                return $http.get(route + idTraveller);
            };

            this.getTravellers = function () {
                return $http.get(route);
            };
}]);
})(window.angular);
