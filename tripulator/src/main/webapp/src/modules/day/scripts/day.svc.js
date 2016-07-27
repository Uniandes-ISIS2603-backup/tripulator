(function (ng) {
    angular.module('DayModule')
        .service('DayService', ['$http', function ($http) {
            function buildRoute(idTraveller, idTrip) {
                return './api/travellers/' + idTraveller + '/trips/' + idTrip + '/days/';
            }

            this.saveDay = function (idTraveller, idTrip, day) {
                return $http.post(buildRoute(idTraveller, idTrip), day);
            };

            this.getDays = function (idTraveller, idTrip) {
                return $http.get(buildRoute(idTraveller, idTrip));
            };
}]);
})(window.angular);
