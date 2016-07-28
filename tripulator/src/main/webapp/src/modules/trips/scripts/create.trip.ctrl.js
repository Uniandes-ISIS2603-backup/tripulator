function CreateController($scope, $mdDialog, $mdToast, TripService, DayService, DateService, $stateParams) {

    function createError(idTrip) {
        if (idTrip >= 0) {
            TripService.deleteTrip($stateParams.traveller.id, idTrip);
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
        DayService.saveDay($stateParams.traveller.id, idTrip, day).then(createDayWrapper, createErrorWrapper);
    }

    function createDays(response) {
        let trip = response.data;
        createDay(trip.id, DateService.generateDate(trip.arrivalDate), DateService.generateDate(trip.departureDate));
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
        TripService.saveTrip($stateParams.traveller.id, trip).then(createDays, createErrorWrapper);
    };
}
