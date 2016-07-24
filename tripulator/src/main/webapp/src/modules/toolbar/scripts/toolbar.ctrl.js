(function (ng) {
    var mod = ng.module('ToolbarModule');
    mod.controller('ToolbarController', ['$scope', '$state', function ($scope, $state) {
        $scope.options = [
            {
                name: 'View Trips',
                icon: 'flight_land',
                state: 'trips'
            },
            {
                name: 'Log out',
                icon: 'exit_to_app',
                state: 'login'
            }
        ];
        $scope.isStateLogin = function () {
            return $state.current.name === 'login';
        }
    }]);
})(window.angular);
