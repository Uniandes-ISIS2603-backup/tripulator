(function (ng) {
    var mod = ng.module("viajeroModule");
    mod.controller('SidebarController', ['$scope', '$element', '$window', '$mdDialog', 'viajeroS', 'countryService', '$stateParams', '$state',
        function ($scope, $element, $window, $mdDialog, svc, countryService, $stateParams, $state) {
            console.log($state.current.name);
            var self = this;
            var tripId;
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
                svc.deleteItinerario($stateParams.userId, tripId).then(function (response) {
                    self.getItinerarios();
                }, responseError);
            };

            /**
             * Fetches all the trips of the user.
             * @returns {undefined}
             */
            this.getItinerarios = function () {
                svc.getItinerarios($stateParams.userId).then(function (response) {
                    $scope.trips = response.data;
                    if ($scope.trips.length === 0) {
                        selectFromMenu($scope.menuOptions[0]);
                        $scope.showAlert("Create Trip", "It appears you have no trips created!");
                        toggleMenu();
                        setTimeout(function () {
                            initGeoChart();
                        }, 300);
                    } else {
                        self.getCachedItinerario($scope.trips[$scope.trips.length - 1]);
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
                tripId = trip.id;
                selectFromMenu($scope.menuActions[0]);
                selectView($scope.menuActions[0]);
            };

            /**
             * Gets a specific trip from the database.
             * @param {type} tripId
             * @returns {undefined}
             */
            this.getItinerario = function (tripId) {
                svc.getItinerario($stateParams.userId, tripId).then(function (response) {
                    $scope.currentTrip = response.data;
                    self.generateImage();
                    $scope.menuActions[0].active = true;
                    self.tripId = tripId;
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
                selectView(action);
            };

            /**
             * Activates a behaviour depending on the menu option that was selected.
             * @param {type} option
             * @returns {undefined}
             */
            $scope.selectOption = function (option) {
                switch (option.name) {
                    case "Create":
                        selectFromMenu(option);
                        $state.go('.create', {});               
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
            selectView = function (action) {
                console.log(action);
                switch (action.name) {
                    case "Calendar":
                        $state.go("^.itinerario", {tripId: tripId});
                        break;
                    case "Gallery":
                        $state.go("^.multimedia", {tripId: tripId});
                        break;
                    case "Map":
                        $state.go("^.mapa", {tripId: tripId});
                        break;
                    case "Overview":
                        $state.go("^.overview", {trip: $scope.currentTrip});
                        break;
                }
            };

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

            this.getItinerarios();
        }]);
})(window.angular);
