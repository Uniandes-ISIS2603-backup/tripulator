(function (ng) {
    var mod = ng.module('TripModule');
    mod.filter('daysInMonth', ['DateService', function (dateService) {
        return function (input, referenceDate) {
            let isDate = referenceDate instanceof Date;
            if (!isDate) {
                return input;
            }
            var out = [];
            let currentMonth = referenceDate.getMonth();
            let currentDay = referenceDate.getDay();

            for (let i = 0; i < currentDay; i++) {
                out.push(i);
            }

            angular.forEach(input, function (day) {
                let date = dateService.generateDate(day.date);
                if (date.getMonth() === currentMonth) {
                    out.push(day)
                }
            });

            return out;
        };
    }]);
})(window.angular);
