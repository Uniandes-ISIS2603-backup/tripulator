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
                        abstract: true,
                        url: '/viajero',
                        params: {
                            userId: null,
                            tripId: null,
                            dayId: null,
                            trip: null
                        },
                        views: {
                            "": {
                                templateUrl: "src/modules/viajero/views/viajero.tpl.html"
                            }
                        }
                    })
                    .state('viajero.wrapper', {
                        views: {
                            'sidebar@viajero': {
                                controller: 'SidebarController',
                                controllerAs: "ctrl",
                                templateUrl: "src/modules/viajero/views/viajero.sidebar.html"
                            }
                        }
                    })
                    .state('viajero.wrapper.overview', {
                        url: '/overview',
                        views: {
                            'content@viajero': {
                                templateUrl: "src/modules/viajero/views/viajero.overview.html",
                                controller: "OverviewController",
                                controllerAs: "ctrl"
                            }
                        }
                    })
                    .state('viajero.wrapper.create', {
                        url: '/create',
                        views: {
                            'content@viajero': {
                                templateUrl: "src/modules/viajero/views/viajero.create.html",
                                controller: "CreateController",
                                controllerAs: "ctrl"
                            }
                        }
                    })
                    .state('viajero.wrapper.itinerario', {
                        url: '/itinerario',
                        views: {
                            'content@viajero': {
                                controller: 'ItinerarioController',
                                controllerAs: "ctrl",
                                templateUrl: "src/modules/itinerario/itinerario.tpl.html"
                            }
                        }
                    })
                    .state('viajero.wrapper.multimedia', {
                        url: '/multimedia',
                        views: {
                            'content@viajero': {
                                controller: 'multimediaCtrl',
                                templateUrl: "src/modules/multimedia/multimedia.tpl.html"
                            }
                        }
                    })
                    .state('viajero.wrapper.mapa', {
                        url: '/mapa',
                        views: {
                            'content@viajero': {
                                controller: 'MapController',
                                templateUrl: "src/modules/mapa/mapa.tpl.html"
                            }
                        }
                    })
                    .state('viajero.wrapper.itinerario.plandia', {
                        url: '/plandia',
                        views: {
                            'plan@viajero.itinerario': {
                                controller: 'PlanDiaController',
                                templateUrl: "src/modules/plandia/plandia.tpl.html"
                            }
                        }
                    });
        }]);
})(window.angular);
