
var app = angular.module("mainApp", ["ui.router","ngAnimate"]);

app.config(['$logProvider', function ($logProvider) {
        $logProvider.debugEnabled(true);
    }]);

app.config(['$stateProvider', '$urlRouterProvider', function ($stateProvider, $urlRouterProvider) {
        $urlRouterProvider.otherwise("/");
        $stateProvider
                .state('/', {
                    url: '/',
                    templateUrl: "src/modules/landingpage/landingpage.tpl.html"
                })
                .state('home', {
                    url: '/home',
                    controller: 'HomeController',
                    templateUrl: "src/modules/home/home.tpl.html"
                })
                .state('calendar', {
                    url: '/calendar',
                    controller: 'CalendarController',
                    templateUrl: "src/modules/calendar/calendar.tpl.html"
                })
                .state('calendar.day', {
                    url: '/day',
                    templateUrl: 'src/modules/calendar/calendar.day.tpl.html'
                })
                .state('dayinformation', {
                    url: '/dayinformation',
                    templateUrl: "src/modules/dayinformation/dayinformation.tpl.html"
                })
                .state('photogallery', {
                    url: '/photogallery',
                    controller: 'PhotoGalleryController',
                    templateUrl: "src/modules/photogallery/photogallery.tpl.html"
                })
                .state('event', {
                    url: '/event',
                    controller: 'EventsController',
                    templateUrl: "src/modules/event/event.tpl.html",
                    resolve:{
                    load:function(eventsInfoService){
                        return eventsInfoService.LoadData();
                    }
                }
                });
    }]);