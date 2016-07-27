function CreateEventController($scope, $mdDialog) {
    $scope.newEvent = {
        name: '',
        arrivalTime: '',
        departureTime: '',
    };
    $scope.hide = function () {
        $mdDialog.hide();
    };
    $scope.cancel = function () {
        $mdDialog.cancel();
    };
    $scope.answer = function (answer) {
        $mdDialog.hide(answer);
    };
}
