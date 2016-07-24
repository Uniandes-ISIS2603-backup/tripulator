(function (ng) {
    var mod = ng.module('TripModule');
    mod.controller('TripController', ['$scope', function ($scope) {
        $scope.weekDays = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];
        $scope.currentNavItem = 'page1';

        $scope.scrollBarWidth = function () {
            let child = document.querySelector('.calendar');
            let width = child.parentNode.offsetWidth - child.clientWidth;
            let attr = width + 'px';
            return {
                'padding-right': attr
            };
        };
    }]);
})(window.angular);
