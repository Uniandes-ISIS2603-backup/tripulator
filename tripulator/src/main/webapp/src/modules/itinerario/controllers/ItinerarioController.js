(function (ng) {

    var mod = ng.module("itinerarioModule");

    mod.controller('ItinerarioController', ['$scope', '$window', 'itinerarioService', '$stateParams', '$state',
        function ($scope, $window, svc, $stateParams, $state) {
            $scope.days = [];
            $scope.showDayInfo = false;
            $scope.daynames = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];
            $scope.selectedDay = {};
            $scope.newDayId;
            $scope.itinerario = {
                id: Number, //número de identificación de un itinerario,  
                nombre: String, // Nombre dado al itinerario.  
                fechaInicio: Date, // Fecha de inicio del itinerario en cuestión.  
                fechaFin: Date, // Fecha de fin del itinerario en cuestión.  
                planDias: [], // Arreglo que contiene todos los id de los días del itinerario.  
                multimedia: [], // Arreglo que contiene todos los id del multidemia asociada al itinerario.  
                mapa: [] // Arreglo que contiene todos los id necesarios para desplegar el mapa.
            };
            var self = this;
            var scrollY = 0;
            /**
             * Le agrega propiedades a los días del backend.
             * @param {type} days
             * @returns {undefined}
             */
            function modifyDays(days) {
                for (var i = 0; i < days.length; i++) {
                    days[i].valid = true;
                    days[i].fecha = new Date(days[i].fecha);
                }
            }

            /**
             * There could be gaps between the days you are on a trip.
             * These gaps must be filled with non valid days for the calendar
             * to display correctly.
             * @param {type} days
             * @returns {undefined}
             */
            function fixGaps(days) {
                for (var i = 0; i < days.length - 1; i++) {
                    var startDate = days[i].fecha;
                    var endDate = days[i + 1].fecha;
                    var tempDate = new Date(startDate);
                    tempDate.setDate(tempDate.getDate() + 1);
                    if (tempDate < endDate) {
                        var x = 1;
                        while (tempDate < endDate) {
                            days.splice(i + x, 0, {
                                valid: false,
                                fecha: new Date(tempDate)
                            });
                            var j = new Date(tempDate);
                            j.setDate(j.getDate() + 1);
                            tempDate = j;
                            x++;
                        }
                    }
                }
            }
            /**
             * Le da formato a la entrada recibida por el backend.
             * Crea una matriz donde las filas son meses y las columnas dias de ese mes.
             * Si el mes no comienza en domingo, se le agrega un relleno a la matriz.
             * @param {type} days
             * @returns {undefined}
             */
            function formatDays(days) {
                var formattedDays = [];
                for (var i = 0; i < days.length; ) {
                    var daysInMonth = [];
                    var monthStart = days[i].fecha;
                    setCalendarOffset(daysInMonth, monthStart);
                    while (i < days.length && days[i].fecha.getMonth() === monthStart.getMonth()) {
                        daysInMonth.push(days[i]);
                        i++;
                    }
                    formattedDays.push(daysInMonth);
                }
                return formattedDays;
            }
            /**
             * Agrega días invalidos al principio del mes si este no empieza en domingo.
             * @param {type} array
             * @param {type} monthStart
             * @returns {undefined}
             */
            function setCalendarOffset(array, monthStart) {
                for (var i = 0; i < monthStart.getDay(); i++) {
                    var newDate = new Date(monthStart);
                    newDate.setDate(monthStart.getDate() - (monthStart.getDay() - i));
                    array.push({
                        fecha: newDate,
                        valid: false
                    });
                }
            }

            /**
             * Le pide todos los datos de un viaje al servicio.
             * @param id
             * @returns {undefined}
             */
            this.getDias = function () {
                svc.getDias($stateParams.userId, $stateParams.tripId).then(function (resolve) {
                    $scope.days = resolve.data;

                    modifyDays($scope.days);

                    $scope.days.sort(function (x, y) {
                        return x.fecha - y.fecha;
                    });

                    fixGaps($scope.days);

                    $scope.days = formatDays($scope.days);

                }, responseError);
            };

            this.showError = function (data) {
                alert(data);
            };

            function responseError(response) {
                self.showError(response.data);
            }

            /**
             * Sets the square background depending of a series of tests.
             * @param {type} day
             * @param {type} highlight
             * @returns {ItinerarioController_L1.ItinerarioController_L5.$scope.squareBackground.ItinerarioControllerAnonym$7|ItinerarioController_L1.ItinerarioController_L5.$scope.squareBackground.ItinerarioControllerAnonym$8|ItinerarioController_L1.ItinerarioController_L5.$scope.squareBackground.ItinerarioControllerAnonym$9|ItinerarioController_L1.ItinerarioController_L5.$scope.squareBackground.ItinerarioControllerAnonym$5|ItinerarioController_L1.ItinerarioController_L5.$scope.squareBackground.ItinerarioControllerAnonym$6}
             */
            $scope.squareBackground = function (day, highlight) {

                if (!day.valid && !highlight) {
                    return{
                        "background-color": "rgba(180, 209, 255, 0.1)"
                    };
                } else if (!day.valid && highlight) {
                    return{
                        "background-color": "rgba(180, 209, 255, 0.1)"
                    };
                } else if (day.valid && !highlight) {
                    return{
                        "background-color": "rgba(94, 168, 167, 0.5)"
                    };
                } else if (day.valid && highlight) {
                    return{
                        "background-color": "rgba(94, 168, 167, 1)"
                    };
                }
            };
            /**
             * Activates day view.
             * @param {type} day
             * @returns {undefined}
             */
            $scope.toggleDayInfo = function (day) {
                $scope.newDayId = day.id;

                if (!$scope.showDayInfo)
                    scrollY = $window.scrollY;

                $scope.showDayInfo = !day.invisible && day.valid && !$scope.showDayInfo;
                $scope.selectedDay = day;

                if ($scope.showDayInfo)
                    $window.scrollTo($window.scrollX, 0);
                else
                    $window.scrollTo($window.scrollX, scrollY);
                
                $scope.selectView('Events');
            };
            
            $scope.selectView = function(name){
                switch(name){
                    case 'Events':
                        $state.go('viajero.itinerario.plandia', {dayId: $scope.newDayId});
                        break;
                };
            };

            this.getDias();
        }]);
})(window.angular);