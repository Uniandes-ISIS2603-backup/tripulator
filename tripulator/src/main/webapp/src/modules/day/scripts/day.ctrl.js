(function (ng) {
    var mod = ng.module('DayModule');
    mod.controller('DayController', ['$scope', '$mdDialog', '$mdToast', 'EventService', '$state', '$stateParams', function ($scope, $mdDialog, $mdToast, eventService, $state, $stateParams) {

        function deletedEvent() {
            $mdToast.showSimple('The event was deleted.');
            getEvents();
        }

        function noDeletedEvent() {
            $mdToast.showSimple('The event could not be deleted.');
        }

        function noEvents() {
            $mdToast.showSimple("Sorry! We couldn't fetch your events!");
        }

        function hasEvents(response) {
            $scope.events = response.data;
            if ($scope.events.length === 0) {
                $mdToast.showSimple('You have no events planned for today! Please create one.')
            }
        }

        function getEvents() {
            eventService.getEvents($stateParams.traveller.id, $stateParams.trip.id, $stateParams.day.id).then(hasEvents, noEvents);
        }

        function deleteEvent(idEvent) {
            eventService.deleteEvent($stateParams.traveller.id, $stateParams.trip.id, $stateParams.day.id, idEvent).then(deletedEvent, noDeletedEvent);
        }

        $scope.events = [];
        $scope.tripname = $stateParams.trip.name;
        $scope.date = $stateParams.day.date;

        $scope.delete = function (event) {
            let deleteWrapper = function () {
                deleteEvent(event.id);
            };

            let confirm = $mdDialog.confirm()
                .title('Would you like to delete this event?')
                .textContent('The event will be gone forever.')
                .ariaLabel('Delete')
                .ok('Delete')
                .cancel('Cancel');
            $mdDialog.show(confirm).then(deleteWrapper, noDeletedEvent);
        };

        $scope.showCreate = function () {
            $mdDialog.show({
                controller: CreateEventController,
                templateUrl: './src/modules/day/views/create.event.html',
                parent: angular.element(document.body),
                clickOutsideToClose: true,
                fullscreen: true
            }).then(getEvents);
        };

        $scope.goBack = function () {
            $state.go('trip', $stateParams);
        };

        getEvents();
    }]);
})(window.angular);
