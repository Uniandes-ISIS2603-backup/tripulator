(function (ng) {

    var mod = ng.module("mainApp", [
        "googlechart",
        "ngMaterial",
        "ngMessages",
        "ui.router",
        "ngAnimate",
        "ui.bootstrap",
        "itinerarioModule",
        "planDiaModule",
        "multimediaModule",
        "eventoModule",
        "viajeroModule",
        "mapsApp",
        "inicioSesionModule"
    ]);

    mod.config(['$logProvider', function ($logProvider) {
            $logProvider.debugEnabled(true);
        }]);

    mod.config(['$stateProvider', '$urlRouterProvider', function ($stateProvider, $urlRouterProvider) {
            $urlRouterProvider.otherwise('/login');
            $stateProvider
                    .state('login', {
                        url: '/login',
                        controller: 'inicioSesionCtrl',
                        controllerAs: "ctrl",
                        templateUrl: "src/modules/iniciosesion/iniciosesion.tpl.html"
                    })
                    .state('viajero', {
                        params: {
                            userId: null,
                            tripId: null,
                            dayId: null
                        },
                        url: '/viajero',
                        controller: 'ViajeroC',
                        controllerAs: "ctrl",
                        templateUrl: "src/modules/viajero/viajero.tpl.html"
                    })
                    .state('viajero.itinerario', {
                        url: '/itinerario',
                        controller: 'ItinerarioController',
                        controllerAs: "ctrl",
                        templateUrl: "src/modules/itinerario/itinerario.tpl.html"
                    })
                    .state('viajero.multimedia', {
                        url: '/multimedia',
                        controller: 'multimediaCtrl',
                        templateUrl: "src/modules/multimedia/multimedia.tpl.html"
                    })
                    .state('viajero.mapa', {
                        url: '/mapa',
                        controller: 'MapController',
                        templateUrl: "src/modules/mapa/mapa.tpl.html"
                    })
                    .state('viajero.itinerario.plandia', {
                        url: '/plandia',
                        controller: 'PlanDiaController',
                        templateUrl: "src/modules/plandia/plandia.tpl.html"
                    });
        }]);
})(window.angular);
