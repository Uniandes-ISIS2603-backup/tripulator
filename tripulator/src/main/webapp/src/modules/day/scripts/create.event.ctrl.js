function CreateEventController($scope, $mdDialog, $mdToast, EventService, $stateParams) {

    function createEvent() {
        $mdToast.showSimple('Event created!');
        $mdDialog.hide();
    }

    function createError() {
        $mdToast.showSimple('Sorry! Event could not be created!');
    }

    $scope.newEvent = {
        name: '',
        arrivalDate: '',
        departureDate: '',
    };
    $scope.hide = function () {
        $mdDialog.hide();
    };
    $scope.cancel = function () {
        $mdDialog.cancel();
    };
    $scope.answer = function (event) {
        EventService.saveEvent($stateParams.idTraveller, $stateParams.idTrip, $stateParams.idDay, event).then(createEvent, createError);
    };

}
