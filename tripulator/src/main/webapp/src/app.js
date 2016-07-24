(function (ng) {

    var app = ng.module("tripulatorApp", [
        'ui.router',
        'ngResource',
        'ngAnimate',
        'ngMaterial',
        'ToolbarModule',
        'LoginModule',
        'TripsModule',
        'TripModule',
        'DayModule',
        'CreateModule'
    ]);

    app.config(['$logProvider', function ($logProvider) {
        $logProvider.debugEnabled(true);
    }]);

    app.config(['$stateProvider', '$urlRouterProvider', function ($stateProvider, $urlRouterProvider) {
        function createTripState(state) {
            return {
                onEnter: function ($mdDialog, $state) {
                    $mdDialog.show({
                            controller: CreateController,
                            templateUrl: './src/modules/create/views/create.tpl.html',
                            parent: angular.element(document.body),
                            clickOutsideToClose: true,
                            fullscreen: true
                        })
                        .then(function (newTrip) {
                            $state.go(state);
                        }, function () {
                            $state.go(state);
                        });
                }
            }
        }
        $stateProvider
            .state('login', {
                url: '/',
                templateUrl: './src/modules/login/views/login.tpl.html',
                controller: 'LoginController'
            })
            .state('trips', {
                url: '/trips',
                templateUrl: './src/modules/trips/views/trips.tpl.html',
                controller: 'TripsController'
            })
            .state('trip', {
                url: '/trip',
                templateUrl: './src/modules/trip/views/trip.tpl.html',
                controller: 'TripController'
            })
            .state('day', {
                url: '/day',
                templateUrl: './src/modules/day/views/day.tpl.html',
                controller: 'DayController'
            })
            .state('trips.create', createTripState('trips'));

        $urlRouterProvider.otherwise('/');
    }]);

})(window.angular);
