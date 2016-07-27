(function (ng) {
    angular.module('DayModule')
        .service('DateService', [function () {
            this.generateDate = function (dateString) {
                return new Date(dateString.replace(/-/g, '\/').replace(/T.+/, ''));
            };
        }]);
})(window.angular);
