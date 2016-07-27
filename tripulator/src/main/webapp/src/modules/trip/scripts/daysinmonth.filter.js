(function (ng) {
    var mod = ng.module('TripModule');
    mod.filter('daysInMonth', function () {
        return function (input, referenceDate) {
            let isDate = referenceDate instanceof Date;
            if (!isDate) {
                return input;
            }
            var out = [];
            let currentMonth = referenceDate.getMonth();

            angular.forEach(input, function (day) {
                let date = new Date(day.date.replace(/-/g, '\/').replace(/T.+/, ''));
                if (date.getMonth() === currentMonth) {
                    out.push(day)
                }
            });

            return out;
        };
    });
})(window.angular);
