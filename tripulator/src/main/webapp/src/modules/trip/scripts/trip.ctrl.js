(function (ng) {
    var mod = ng.module('TripModule');
    mod.controller('TripController', ['$scope', '$mdToast', 'DayService', 'DateService', '$state', '$stateParams', function ($scope, $mdToast, dayService, dateService, $state, $stateParams) {

        function dayComparison(day1, day2) {
            let date1 = dateService.generateDate(day1.date);
            let date2 = dateService.generateDate(day2.date);
            if (date1 < date2) {
                return -1;
            } else if (date1 > date2) {
                return 1;
            } else return 0;
        }

        function getMonths(days) {
            let currentMonth = -1;
            for (let i = 0; i < days.length; i++) {
                let date = dateService.generateDate(days[i].date);
                if (date.getMonth() !== currentMonth) {
                    $scope.months.push(date);
                    currentMonth = date.getMonth();
                }
            }
            $scope.currentTab = $scope.months[0];
        }

        function hasDays(response) {
            let days = response.data;
            days.sort(dayComparison);
            getMonths(days);
            $scope.days = days;
        }

        function noDays() {
            $mdToast.showSimple('We are having trouble fetching your trip.');
        }

        function getDays() {
            dayService.getDays($stateParams.traveller.id, $stateParams.trip.id).then(hasDays, noDays);
        }

        $scope.months = [];
        $scope.days = [];
        $scope.weekDays = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];
        $scope.currentTab;
        $scope.tripname = $stateParams.trip.name;

        $scope.scrollBarWidth = function () {
            let child = document.querySelector('.calendar');
            let width = child.parentNode.offsetWidth - child.clientWidth;
            let attr = width + 'px';
            return {
                'padding-right': attr
            };
        };

        $scope.selectTab = function (month) {
            $scope.currentTab = month;
        };

        $scope.showDay = function (day) {
            $stateParams.day = {};
            $stateParams.day.id = day.id;
            $stateParams.day.date = day.date;
            $state.go('day', $stateParams);
        };

        $scope.goBack = function () {
            $state.go('trips', $stateParams);
        };

        $scope.isDay = function (day) {
            return typeof day.city !== 'undefined' && typeof day.date !== 'undefined' && typeof day.id !== 'undefined';
        };

        getDays();
    }]);
})(window.angular);
