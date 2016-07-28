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
                    traveller: null
                }
            })
            .state('trip', {
                url: '/trip',
                templateUrl: './src/modules/trip/views/trip.tpl.html',
                controller: 'TripController',
                params: {
                    traveller: null,
                    trip: null
                }
            })
            .state('day', {
                url: '/day',
                templateUrl: './src/modules/day/views/day.tpl.html',
                controller: 'DayController',
                params: {
                    traveller: null,
                    trip: null,
                    day: null
                }
            });
        $urlRouterProvider.otherwise('/');
    }]);

})(window.angular);
