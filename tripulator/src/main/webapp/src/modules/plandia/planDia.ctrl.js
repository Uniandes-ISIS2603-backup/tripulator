(function (ng) {
    var mod = ng.module("planDiaModule");
    
    mod.controller('PlanDiaController', ['$scope', 'planDiaService', 'dataSvc', function ($scope, svc, dataSvc) {
            
            var self = this;
            
            $scope.events = [];
            
            $scope.editMode = false;
            
            $scope.currentEvent = {
                "title": "",
                "type": "",
                "image": "",
                "start": "",
                "end": "",
                "description": "",
                "comments": []
            };
            
            $scope.style = function(b){
              if (b){
                  return "timeline-inverted";
              }
              else {
                  return "timeline-not-inverted";
              }
            };
            
            this.showError = function (data) {
                alert(data);
            };

            function responseError(response) {
                self.showError(response);
            }
            
            $scope.fetchEvents = function () {
                alert('fetching: '+ dataSvc.userId+', '+dataSvc.tripId+', '+dataSvc.dayId);
                return svc.getEvents(dataSvc.userId, dataSvc.tripId, dataSvc.dayId).then(function (response) {
                    $scope.events = response.data;
                    console.log('fetch events answer: ' + response.data);
                    $scope.currentEvent = {};
                    $scope.editMode = false;
                    return response;
                }, responseError); 
            };
            
            $scope.createEvent = function (){
                $scope.editMode = true;
                $scope.currentEvent = {};
                $scope.$broadcast("post-create", $scope.currentRecord);
            };
            
            $scope.saveEvent = function () {
                alert('saving: ' + dataSvc.userId+', '+dataSvc.tripId+', '+dataSvc.dayId);
                var newEvent = $scope.currentEvent;
                svc.saveRecord(dataSvc.userId, dataSvc.tripId, newEvent).then(function(){
                    $scope.fetchEvents();
                }, responseError);
                $scope.fetchEvents();
            };
            
            $scope.editEvent = function (event) {
                console.log(dataSvc.userId+', '+dataSvc.tripId+', '+dataSvc.dayId+', '+event.id);
                return svc.getEvent(dataSvc.userId, dataSvc.tripId, dataSvc.dayId, event.id).then(function (response) {
                    $scope.currentRecord = response.data;
                    $scope.editMode = true;
                    $scope.$broadcast("post-edit", $scope.currentRecord);
                    return response;
                }, responseError);
            };
            
            $scope.deleteEvent = function(event) {
                console.log(dataSvc.userId+', '+dataSvc.tripId+', '+dataSvc.dayId+', '+event.id);
                return svc.removeEvent(dataSvc.userId, dataSvc.tripId, dataSvc.dayId, event.id).then(function () {
                    $scope.fetchEvents();
                }, responseError);
            };
            
            $scope.fetchEvents();
            
            /**
                $scope.events = [                
                {
                    "id": "2",
                    "title": "Ev2",
                    "image": "",
                    "type": "",
                    "start": "11:00",
                    "end": "13:00",
                    "description": "mhm mhm mmmmmhm mhm",
                    "comments": []                    
                },
                {
                    "id": "1",
                    "title": "Ev1",
                    "image": "",
                    "type": "",
                    "start": "07:00",
                    "end": "10:00",
                    "description": "blabla blablabla blabla",
                    "comments": []
                },
                {
                    "id": "3",
                    "title": "Ev3",
                    "image": "",
                    "type": "",
                    "start": "15:00",
                    "end": "19:00",
                    "description": "sisi sisisisi sisi",
                    "comments": []                   
                },
                                {
                    "id": "4",
                    "title": "Ev4",
                    "image": "",
                    "type": "",
                    "start": "12:00",
                    "end": "12:30",
                    "description": "adsfasdfasdfasdfasdf",
                    "comments": []                   
                },
                                {
                    "id": "5",
                    "title": "Ev5",
                    "image": "",
                    "type": "",
                    "start": "05:00",
                    "end": "06:00",
                    "description": "el madrugón",
                    "comments": []                   
                }
            ];
             */
        }]);
})(window.angular);