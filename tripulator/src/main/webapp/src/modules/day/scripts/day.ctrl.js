(function (ng) {
    var mod = ng.module('DayModule');
    mod.controller('DayController', ['$scope', '$mdDialog', '$state', function ($scope, $mdDialog, $state) {
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
    }]);
})(window.angular);
