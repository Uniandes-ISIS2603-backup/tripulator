(function (ng) {
    var mod = ng.module('DayModule');
    mod.controller('DayController', ['$scope', '$mdDialog', '$state', '$stateParams', function ($scope, $mdDialog, $state, $stateParams) {
        console.log($stateParams);
        $scope.delete = function (ev, event) {
            var confirm = $mdDialog.confirm()
                .title('Would you like to delete this event?')
                .textContent('The event will be gone forever.')
                .ariaLabel('Delete')
                .targetEvent(ev)
                .ok('Delete')
                .cancel('Cancel');
            $mdDialog.show(confirm).then(function () {}, function () {});
        };

        $scope.showCreate = function () {
            $state.go('day.create', $stateParams);
        };
    }]);
})(window.angular);
