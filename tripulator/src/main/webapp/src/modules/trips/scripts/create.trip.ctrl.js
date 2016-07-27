function CreateController($scope, $mdDialog, $mdToast, TripService, DayService, $stateParams) {

    function createError(idTrip) {
        if (idTrip >= 0) {
            TripService.deleteTrip($stateParams.idTraveller, idTrip);
        }
        $mdToast.showSimple('Sorry, we experienced an error while creating trip.');
    }

    function createSuccessful() {
        $mdToast.showSimple('Your trip has been created.');
        $mdDialog.hide();
    }

    function createDay(idTrip, arrivalDate, departureDate) {
        if (arrivalDate > departureDate) {
            createSuccessful();
            return;
        }
        let day = {
            events: [],
            city: '',
            date: arrivalDate
        };
        let createDayWrapper = function () {
            arrivalDate.setDate(arrivalDate.getDate() + 1);
            createDay(idTrip, arrivalDate, departureDate);
        };
        let createErrorWrapper = function () {
            createError(idTrip);
        };
        DayService.saveDay($stateParams.idTraveller, idTrip, day).then(createDayWrapper, createErrorWrapper);
    }

    function createDays(response) {
        let trip = response.data;
        createDay(trip.id, new Date(trip.arrivalDate), new Date(trip.departureDate));
    }

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
    $scope.answer = function (trip) {
        let createErrorWrapper = function () {
            createError(-1);
        };
        TripService.saveTrip($stateParams.idTraveller, trip).then(createDays, createErrorWrapper);
    };
}
