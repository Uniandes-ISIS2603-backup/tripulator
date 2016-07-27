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
    ]);

    app.config(['$logProvider', function ($logProvider) {
        $logProvider.debugEnabled(true);
    }]);

    app.config(['$stateProvider', '$urlRouterProvider', function ($stateProvider, $urlRouterProvider) {
        $stateProvider
            .state('login', {
                url: '/',
                templateUrl: './src/modules/login/views/login.tpl.html',
                controller: 'LoginController'
            })
            .state('trips', {
                url: '/trips',
                templateUrl: './src/modules/trips/views/trips.tpl.html',
                controller: 'TripsController',
                params: {
                    idTraveller: null
                }
            })
            .state('trip', {
                url: '/trip',
                templateUrl: './src/modules/trip/views/trip.tpl.html',
                controller: 'TripController',
                params: {
                    idTraveller: null,
                    idTrip: null
                }
            })
            .state('day', {
                url: '/day',
                templateUrl: './src/modules/day/views/day.tpl.html',
                controller: 'DayController',
                params: {
                    idTraveller: null,
                    idTrip: null,
                    idDay: null
                }
            })
            .state('day.create', {
                onEnter: function ($mdDialog, $state) {
                    $mdDialog.show({
                            controller: CreateEventController,
                            templateUrl: './src/modules/day/views/create.event.html',
                            parent: angular.element(document.body),
                            clickOutsideToClose: true,
                            fullscreen: true
                        })
                        .then(function (newTrip) {
                            $state.go('day');
                        }, function () {
                            $state.go('day');
                        });
                },
                params: {
                    idTraveller: null,
                    idTrip: null,
                    idDay: null
                }
            });

        $urlRouterProvider.otherwise('/');
    }]);

})(window.angular);
