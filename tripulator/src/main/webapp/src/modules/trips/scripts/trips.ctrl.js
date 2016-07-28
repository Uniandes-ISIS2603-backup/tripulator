(function (ng) {
    var mod = ng.module('TripsModule');
    mod.controller('TripsController', ['$scope', '$mdDialog', '$mdToast', 'TripService', '$state', '$stateParams', function ($scope, $mdDialog, $mdToast, tripService, $state, $stateParams) {

        function deletedTrip() {
            $mdToast.showSimple('The trip was deleted.');
            getTrips();
        }

        function noDeletedTrip() {
            $mdToast.showSimple('The trip could not be deleted.');
        }

        function noTrips() {
            $mdToast.showSimple("Sorry! We couldn't fetch your trips!");
        }

        function hasTrips(response) {
            $scope.trips = response.data;
            if ($scope.trips.length === 0) {
                $mdToast.showSimple('You have no trips! Please create one.')
            }
        }

        function getTrips() {
            tripService.getTrips($stateParams.traveller.id).then(hasTrips, noTrips);
        }

        function deleteTrip(idTrip) {
            tripService.deleteTrip($stateParams.traveller.id, idTrip).then(deletedTrip, noDeletedTrip);
        }

        $scope.trips = [];
        $scope.username = $stateParams.traveller.name;

        $scope.delete = function (trip) {
            let deleteWrapper = function () {
                deleteTrip(trip.id);
            };

            let confirm = $mdDialog.confirm()
                .title('Would you like to delete this trip?')
                .textContent('The trip and all its contents will be gone forever.')
                .ariaLabel('Delete')
                .ok('Delete')
                .cancel('Cancel');
            $mdDialog.show(confirm).then(deleteWrapper, noDeletedTrip);
        };

        $scope.showCreate = function () {
            $mdDialog.show({
                controller: CreateController,
                templateUrl: './src/modules/trips/views/create.trip.html',
                parent: angular.element(document.body),
                clickOutsideToClose: true,
                fullscreen: true
            }).then(getTrips);
        };

        $scope.showTrip = function (trip) {
            $stateParams.trip = {};
            $stateParams.trip.id = trip.id;
            $stateParams.trip.name = trip.name;
            $state.go('trip', $stateParams);
        };

        getTrips();
    }]);
})(window.angular);
