(function (ng) {
    var mod = ng.module('ToolbarModule');
    mod.controller('ToolbarController', ['$scope', '$state', '$stateParams', function ($scope, $state, $stateParams) {

        let stateChange = function (state) {
            return function () {
                $state.go(state, $stateParams);
            };
        };

        let tripsState = stateChange('trips');
        let loginState = stateChange('login');

        $scope.options = [
            {
                name: 'View Trips',
                icon: 'flight_land',
                state: tripsState
            },
            {
                name: 'Log out',
                icon: 'exit_to_app',
                state: loginState
            }
        ];
        $scope.isStateLogin = function () {
            return $state.current.name === 'login';
        }
    }]);
})(window.angular);
