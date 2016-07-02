(function (ng) {
    var mod = ng.module("viajeroModule");
    mod.controller('ViajeroC', ['$scope', '$element', '$window', '$mdDialog', 'viajeroS', 'dataSvc', 'Restangular','countryService',
        function ($scope, $element, $window, $mdDialog, svc, dataSvc, Restangular, countryService) {
            var self = this;
            var userData = dataSvc;
            $scope.trips = [];
            $scope.currentTrip;
            $scope.today = new Date();
            Restangular.one('tripulator/api/viajeros', userData.userId);
            /**
             * Array with all of the possible menu options.
             */
            $scope.menuOptions = [
                {
                    name: "Create",
                    active: false
                },
                {
                    name: "Delete",
                    active: false
                }
            ];
            /**
             * Array with all of the possible menu actions.
             */
            $scope.menuActions = [
                {
                    name: "Overview",
                    active: false
                },
                {
                    name: "Calendar",
                    active: false
                },
                {
                    name: "Map",
                    active: false
                },
                {
                    name: "Gallery",
                    active: false
                }
            ];

            /**
             * Displays an error.
             * @param {type} response
             * @returns {undefined}
             */
            function responseError(response) {
                self.showError(response);
            }
            /**
             * Activates the menu option/action that was selected.
             * Deactivates all of the other options/actions.
             * @param {type} element
             * @returns {undefined}
             */
            function selectFromMenu(element) {

                for (var i = 0; i < $scope.menuActions.length; i++) {
                    if ($scope.menuActions[i] === element)
                        $scope.menuActions[i].active = true;
                    else
                        $scope.menuActions[i].active = false;
                }
                for (var i = 0; i < $scope.menuOptions.length - 1; i++) {
                    if ($scope.menuOptions[i] === element)
                        $scope.menuOptions[i].active = true;
                    else
                        $scope.menuOptions[i].active = false;
                }
            }

            /**
             * If a menu option is selected, this method adds a css style to the menu item.
             * @param {type} element
             * @returns different background style if the menu option is selected. Otherwise, nothing happens.
             */
            function isMenuOptionSelected(element) {
                if (element.active) {
                    return {"background": "rgba(180, 209, 255, 0.5)"};
                }
                return {};
            }

            /**
             * Script that hides/shows the menu.
             * @returns {undefined}
             */
            function toggleMenu() {
                angular.element("#wrapper").toggleClass("toggled");
            }

            /**
             * Displays an error message on the screen.
             * @param {type} data
             * @returns {undefined}
             */
            this.showError = function (data) {
                $scope.showAlert("Error", data);
            };

            /**
             * Prototype method that adds 4 photos to the carrousel.
             * In production, this method should take photos of the countries the user is visiting.
             * @returns {undefined}
             */
            this.generateImage = function () {
                $scope.currentTrip.multimedia = [];
                for (var i = 0; i < 4; i++) {
                    $scope.currentTrip.multimedia.push({
                        id: i,
                        src: 'http://lorempixel.com/' + ($element.width() + i) + '/' + (screen.height - i),
                        name: "image title"
                    });
                }
            };


            /**
             * Deletes a trip from the users trip.
             * @returns {undefined}
             */
            this.deleteItinerario = function () {                
                $scope.currentTrip.remove().then(function () {
                    self.getItinerarios();
                });
            };

            /**
             * Adds a trip to the user.
             * @param {type} trip
             * @returns {undefined}
             */
            this.addItinerario = function (trip) {
                svc.addItinerario(userData.userId, trip).then(function (response) {
                    self.addDays(response.data.id, planDias);
                    self.getItinerarios();
                    finishCreation();
                }, responseError);
            };

            this.addDays = function (tripId, planDias) {
                for (var i = 0; i < planDias.length; i++) {
                    svc.addDia(userData.userId, tripId, planDias[i]).then(function (response) {

                    }, responseError);
                }
            };


            /**
             * Fetches all the trips of the user.
             * @returns {undefined}
             */
            this.getItinerarios = function () {
                Restangular.one('tripulator/api/viajeros', userData.userId).getList('itinerarios').then(function(trips){
                    $scope.trips = trips;
                    if ($scope.trips.length === 0) {
                        selectFromMenu($scope.menuOptions[0]);
                        $scope.showAlert("Create Trip", "It appears you have no trips created!");
                        toggleMenu();
                        setTimeout(function(){
                            initGeoChart();
                        },300);
                    } else {
                        self.getCachedItinerario($scope.trips[$scope.trips.length - 1]);
                        $scope.$apply();
                        return trips;
                    }   
                }, responseError);
            };

            /**
             * Once the trips have been retrieved from the db. They are cached.
             * This method returns the cached trips.
             * @param {type} trip
             * @returns {undefined}
             */
            this.getCachedItinerario = function (trip) {
                $scope.currentTrip = trip;
                self.generateImage();
                selectFromMenu($scope.menuActions[0]);
                userData.tripId = trip.id;
            };

            /**
             * Gets a specific trip from the database.
             * @param {type} tripId
             * @returns {undefined}
             */
            this.getItinerario = function (tripId) {
                svc.getItinerario(userData.userId, tripId).then(function (response) {
                    $scope.currentTrip = response.data;
                    self.generateImage();
                    $scope.menuActions[0].active = true;
                    userData.tripId = tripId;
                }, responseError);
            };

            /**
             * If a trip is selected, the background must be changed.
             * @param {type} trip
             * @returns css style.
             */
            $scope.isTripSelected = function (trip) {
                if (trip === $scope.currentTrip) {
                    return {"background": "rgba(180, 209, 255, 0.5)"};
                }
                return {};
            };

            /**
             * Selects an action from the menu. Sets its .active object property to true.
             * @param {type} action
             * @returns {undefined}
             */
            $scope.selectAction = function (action) {
                selectFromMenu(action);
            };

            /**
             * Activates a behaviour depending on the menu option that was selected.
             * @param {type} option
             * @returns {undefined}
             */
            $scope.selectOption = function (option) {
                switch (option.name) {
                    case "Create":
                        toggleMenu();
                        setTimeout(function(){
                            initGeoChart();
                        },300);
                        $scope.showAlert("Create trip", "Lets start!");
                        selectFromMenu(option);
                        break;
                    case "Edit":
                        $scope.showAlert("Under development", "This feature will be available soon.");
                        break;
                    case "Delete":
                        $scope.showDeleteConfirm();
                        break;

                }
            };

            /**
             * 
             * @param {type} action
             * @returns true if the action is selected. Otherwise, false.
             */
            $scope.isActionSelected = function (action) {
                return isMenuOptionSelected(action);
            };

            /**
             * @param {type} option
             * @returns true if the option in selected. False otherwise.
             */
            $scope.isOptionSelected = function (option) {
                return isMenuOptionSelected(option);
            };

            /**
             * ui-sref routing for menu actions.
             * @param {type} action
             * @returns {String}
             */
            $scope.selectView = function (action) {
                switch (action.name) {
                    case "Calendar":
                        return "viajero.itinerario()";
                        break;
                    case "Gallery":
                        return "viajero.multimedia()";
                        break;
                    case "Map":
                        return "viajero.mapa()";
                        break;
                    default:
                        return "viajero";
                }
            };

            // CREATE A TRIP MODULE

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
                $scope.$apply();
            };
            /**
             * When the user has finished creating a trip, this toggles the menu.
             * And changes the view to overview.
             * @returns {undefined}
             */
            function finishCreation() {
                toggleMenu();
                $scope.menuOptions[0].active = false;
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

            // DIALOG WINDOWS CONSTRUCTION
            $scope.showAlert = function (title, info) {
                // Appending dialog to document.body to cover sidenav in docs app
                // Modal dialogs should fully cover application
                // to prevent interaction outside of dialog
                $mdDialog.show(
                        $mdDialog.alert()
                        .parent(angular.element(document.querySelector('#popupContainer')))
                        .clickOutsideToClose(true)
                        .title(title)
                        .textContent(info)
                        .ariaLabel('Alert Dialog Demo')
                        .ok('Got it!')
                        .targetEvent(info)
                        );
            };

            $scope.showDeleteConfirm = function (ev) {
                // Appending dialog to document.body to cover sidenav in docs app
                var confirm = $mdDialog.confirm()
                        .title('Are you sure you want to delete?')
                        .textContent('All the information about ' + $scope.currentTrip.nombre
                                + ' would be permanetly lost')
                        .ariaLabel('Lucky day')
                        .targetEvent(ev)
                        .ok('Yes!')
                        .cancel('Not yet');
                $mdDialog.show(confirm).then(function () {
                    self.deleteItinerario();
                }, function () {

                });
            };

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

            this.getItinerarios();

            /**
             * Maps country code to country name using the array.
             * @param {type} countryCode
             * @returns {viajeroC_ctrl_L1.viajeroC_ctrl_L4.isoCountries|isoCountries}
             */
            function getCountryName(countryCode) {
                return countryService.getCountryName(countryCode);
            }
        }]);
})(window.angular);
