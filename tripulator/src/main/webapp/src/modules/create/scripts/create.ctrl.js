function CreateController($scope, $mdDialog) {
    $scope.newTrip = {
        name: '',
        arrivalDate: '',
        departureDate: '',
        country: ''
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
