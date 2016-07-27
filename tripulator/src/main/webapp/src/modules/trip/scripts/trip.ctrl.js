(function (ng) {
    var mod = ng.module('TripModule');
    mod.controller('TripController', ['$scope', '$mdToast', 'DayService', '$state', '$stateParams', function ($scope, $mdToast, dayService, $state, $stateParams) {

        function dayComparison(day1, day2) {
            let date1 = new Date(day1.date.replace(/-/g, '\/').replace(/T.+/, ''));
            let date2 = new Date(day2.date.replace(/-/g, '\/').replace(/T.+/, ''));
            if (date1 < date2) {
                return -1;
            } else if (date1 > date2) {
                return 1;
            } else return 0;
        }

        function getMonths(days) {
            let currentMonth = -1;
            for (let i = 0; i < days.length; i++) {
                let date = new Date(days[i].date.replace(/-/g, '\/').replace(/T.+/, ''));
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
            dayService.getDays($stateParams.idTraveller, $stateParams.idTrip).then(hasDays, noDays);
        }

        $scope.months = [];
        $scope.days = [];
        $scope.weekDays = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];
        $scope.currentTab;

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
            $stateParams.dayId = 1;
            $state.go('day', $stateParams);
        };

        $scope.goBack = function () {
            $state.go('trips', $stateParams);
        };

        getDays();
    }]);
})(window.angular);
