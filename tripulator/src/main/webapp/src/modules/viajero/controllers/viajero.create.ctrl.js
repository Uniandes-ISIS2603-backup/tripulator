(function (ng) {
    var mod = ng.module("viajeroModule");
    mod.controller('ViajeroC', ['$scope', '$element', '$window', '$mdDialog', 'viajeroS', 'countryService', '$stateParams', '$state',
        function ($scope, $element, $window, $mdDialog, svc, countryService, $stateParams, $state) {
            console.log($state.current.name);
            
            var self = this;
            
            /**
             * Tracks whether the current chart has already a listener attached to it.
             * @type Boolean
             */
            var firstClick;

            /**
             * Tiene todos los dias que se deben crear luego de haber creado el itinerario.
             * @type Array
             */
            var planDias = [];

            /**
             * Holds all chart elements.
             */
            $scope.chart;

            /**
             * Tracks the current create trip page the user is on.
             */
            $scope.optionScreen;

            /**
             * Object that holds an array of cities for each country the trip has.
             */
            $scope.tripDetails;
            
            function responseError(response){
                alert(response.data);
            }
            
            /**
             * Adds a trip to the user.
             * @param {type} trip
             * @returns {undefined}
             */
            this.addItinerario = function (trip) {
                svc.addItinerario($stateParams.userId, trip).then(function (response) {
                    self.addDays(response.data.id, planDias);
                    self.getItinerarios();
                    finishCreation();
                }, responseError);
            };

            this.addDays = function (tripId, planDias) {
                for (var i = 0; i < planDias.length; i++) {
                    svc.addDia($stateParams.userId, tripId, planDias[i]).then(function (response) {

                    }, responseError);
                }
            };

            /**
             * Initializes all variables associated with creating a new trip.
             * @returns {undefined}
             */
            function initGeoChart() {
                $scope.optionScreen = 1;
                $scope.tripDetails = {};
                firstClick = false;
                $scope.chart = {
                    type: "GeoChart",
                    data: [
                        ['Locale']
                    ],
                    options: {
                        width: "100%",
                        height: 500,
                        datalessRegionColor: "#b4d1ff",
                        chartArea: {
                            left: 10,
                            top: 10,
                            bottom: 0
                        },
                        colorAxis: {
                            colors: ['#aec7e8', '#1f77b4']
                        },
                        displayMode: 'regions',
                        enableRegionInteractivity: true
                    }
                };
            }

            /**
             * Checks whether the new city can be added taking into account
             * several factors.
             * @param {type} country
             * @param {type} city
             * @param {type} arrivalDate
             * @param {type} departureDate
             * @returns {Boolean}
             */
            function addConditions(city, arrivalDate, departureDate) {
                if (city == null)
                    return "Please name the city!";
                if (arrivalDate == null)
                    return "Dates must have a value";
                if (departureDate == null)
                    return "Dates must have a value";
                if (arrivalDate > departureDate)
                    return "The arrival date must come before the departure date";
                for (var property in $scope.tripDetails) {
                    if ($scope.tripDetails.hasOwnProperty(property)) {
                        var tripSegment = $scope.tripDetails[property].slice(1, $scope.tripDetails[property].length);
                        for (var i = 0; i < tripSegment.length; i++) {
                            if (arrivalDate <= tripSegment[i].departureDate &&
                                    departureDate >= tripSegment[i].arrivalDate) {
                                return "The range of dates you entered are in conflict with another range of dates";
                            }
                        }
                    }
                }
                return "OK";
            }
            /**
             * 
             * @param {String} country
             * @param {String} city
             * @param {String} arrivalDate
             * @param {String} departureDate
             * @returns {undefined}
             */
            $scope.addCity = function (country, city, arrivalDate, departureDate) {
                var errorMsg = addConditions(city, arrivalDate, departureDate);
                if (errorMsg !== "OK") {
                    $scope.showAlert("Form Error", errorMsg);
                    return false;
                }
                var citiesInCountry = $scope.tripDetails[country];

                citiesInCountry.push({
                    country: country,
                    city: city,
                    arrivalDate: arrivalDate,
                    departureDate: departureDate
                });
                return true;
            };

            /**
             * Deletes a city it had already added.
             * @param {type} city
             * @returns {undefined}
             */
            $scope.deleteCity = function (city) {
                var citiesInCountry = $scope.tripDetails[city.country];
                if (city.city === 'Add a city to visit')
                    return;
                for (var i = 0; i < citiesInCountry.length; i++) {
                    if (citiesInCountry[i].city === city.city &&
                            citiesInCountry[i].arrivalDate === city.arrivalDate &&
                            citiesInCountry[i].departureDate === city.departureDate) {
                        citiesInCountry.splice(i, 1);
                    }
                }
            };

            /**
             * Removes the first element from the data array since it contains
             * the chart's metainfo header.
             * @returns {Array}
             */
            $scope.getCountryData = function () {
                return $scope.chart.data.slice(1, $scope.chart.data.length);
            };

            /**
             * Returns true if the city was already added. Otherwise, false.
             * @param {type} city
             * @returns {Boolean}
             */
            $scope.disableForm = function (city) {
                return !(city === 'Add a city to visit');
            };

            /**
             * Decides whether the geochart should be currently displayed.
             * @returns {Boolean}
             */
            $scope.showChart = function () {
                return $scope.optionScreen === 1;
            };

            /**
             * Decides whether the arrow that moves back in the create trip page
             * is shown.
             * @returns {Boolean}
             */
            $scope.showPrevArrow = function () {
                return $scope.optionScreen > 1;
            };
            /**
             * Decides whether the arrow that moves forward in the create trip
             * page is shown.
             * @returns {Boolean}
             */
            $scope.showNextArrow = function () {
                return $scope.optionScreen < 3;
            };

            /**
             * Checks whether there is at least 1 country selected
             * @returns {String}
             */
            function areCountriesSelected() {
                if ($scope.chart.data.length === 1)
                    return "Please select a country";
                return "YES";
            }
            /**
             * Moves to the next create trip page.
             * @returns {undefined}
             */
            $scope.nextPage = function (ev) {
                firstClick = false;
                if ($scope.optionScreen === 2) {
                    $scope.showPrompt(ev);
                } else {
                    var msg = areCountriesSelected();
                    if (msg !== "YES") {
                        $scope.showAlert("Whoops!", msg);
                    } else {
                        $scope.optionScreen++;
                    }
                }
            };
            /**
             * Moves to the previous create trip page.
             * @returns {undefined}
             */
            $scope.prevPage = function () {
                if ($scope.optionScreen > 1) {
                    $scope.optionScreen--;
                }
            };

            /**
             * Creates a listener for the chart.
             * @param {type} chartWrapper
             * @returns {undefined}
             */
            $scope.readyHandler = function (chartWrapper) {
                if (!firstClick) {
                    $window.google.visualization.events.addListener(chartWrapper.getChart(), 'regionClick', function (r) {
                        $scope.regionClick(r);
                    });
                    firstClick = true;
                }
            };

            /**
             * Function to be called every time an event is triggered on the geochart.
             * @param {type} region
             * @returns {undefined}
             */
            $scope.regionClick = function (region) {
                var index = -1;
                var curCountry = getCountryName(region.region.toString());
                for (var i = 1; i < $scope.chart.data.length; i++) {
                    var clickedCountry = $scope.chart.data[i].toString();
                    if (curCountry === clickedCountry) {
                        index = i;
                        break;
                    }
                }
                if (index > -1) {
                    var deleted = $scope.chart.data.splice(index, 1).toString();
                    $scope.tripDetails[deleted] = [];
                } else {
                    $scope.chart.data.push([curCountry]);
                    var regionString = curCountry;
                    if (typeof $scope.tripDetails[regionString] === 'undefined') {
                        $scope.tripDetails[regionString] = [];
                        $scope.tripDetails[regionString].push({
                            country: regionString,
                            city: 'Add a city to visit',
                            arrivalDate: 'Arrival date',
                            departureDate: 'Departure date'
                        });
                    }
                }
            };
            /**
             * When the user has finished creating a trip, this toggles the menu.
             * And changes the view to overview.
             * @returns {undefined}
             */
            function finishCreation() {
                $state.go('viajero.overview', {});
            }

            function generateDays(days, tripObject) {
                var i = new Date(tripObject.arrivalDate);
                while (i.getTime() <= tripObject.departureDate.getTime()) {
                    days.push({
                        pais: tripObject.country,
                        fecha: i,
                        ciudad: tripObject.city,
                        eventos: []
                    });
                    var j = new Date(i);
                    j.setDate(j.getDate() + 1);
                    i = j;
                }
            }
            /**
             * Given all the input, it will organize the data and generate a
             * trip. (Compatible with ItinerarioDTO)
             * @param tripName
             * @returns {undefined}
             */
            function getRelevantData(tripName) {
                planDias = [];
                var completeTrip = [];
                for (var property in $scope.tripDetails) {
                    if ($scope.tripDetails.hasOwnProperty(property)) {
                        var countryDetails = $scope.tripDetails[property];
                        countryDetails = countryDetails.slice(1, countryDetails.length);
                        completeTrip = completeTrip.concat(countryDetails);
                        for (var i = 0; i < countryDetails.length; i++) {
                            generateDays(planDias, countryDetails[i]);
                        }
                    }
                }

                completeTrip.sort(function (x, y) {
                    return x.arrivalDate - y.arrivalDate;
                });

                var fechaInicio = completeTrip[0].arrivalDate;

                completeTrip.sort(function (x, y) {
                    return x.departureDate - y.departureDate;
                });
                var fechaFin = completeTrip[completeTrip.length - 1].departureDate;

                var mapa = [];
                var multimedia = [];

                var itinerario = {
                    nombre: tripName,
                    fechaInicio: fechaInicio,
                    fechaFin: fechaFin,
                    planDias: [],
                    multimedia: multimedia,
                    mapa: mapa
                };

                return itinerario;
            }

            $scope.showPrompt = function (ev) {
                // Appending dialog to document.body to cover sidenav in docs app
                var confirm = $mdDialog.prompt()
                        .title('What would you name your trip?')
                        .textContent('Please be a little creative.')
                        .placeholder('trip name')
                        .ariaLabel('Dog name')
                        .targetEvent(ev)
                        .ok('Okay!')
                        .cancel('I\'m not ready yet');
                $mdDialog.show(confirm).then(function (result) {
                    var itinerario = getRelevantData(result);
                    self.addItinerario(itinerario);
                }, function () {
                });
            };
            /*
             * Maps country code to country name using the array.
             * @param {type} countryCode
             * @returns {viajeroC_ctrl_L1.viajeroC_ctrl_L4.isoCountries|isoCountries}
             */
            function getCountryName(countryCode) {
                return countryService.getCountryName(countryCode);
            }

            initGeoChart();
        }]);
})(window.angular);

