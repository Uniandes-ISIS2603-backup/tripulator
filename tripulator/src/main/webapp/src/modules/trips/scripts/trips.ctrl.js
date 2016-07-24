(function (ng) {
    var mod = ng.module('TripsModule');
    mod.controller('TripsController', ['$scope', '$mdDialog', '$state', function ($scope, $mdDialog, $state) {
        $scope.delete = function (ev, trip) {
            var confirm = $mdDialog.confirm()
                .title('Would you like to delete this trip?')
                .textContent('The trip and all its contents will be gone forever.')
                .ariaLabel('Delete')
                .targetEvent(ev)
                .ok('Delete')
                .cancel('Cancel');
            $mdDialog.show(confirm).then(function () {}, function () {});
        };

        $scope.showTrip = function () {
            $state.go('trip');
        };
    }]);
})(window.angular);
