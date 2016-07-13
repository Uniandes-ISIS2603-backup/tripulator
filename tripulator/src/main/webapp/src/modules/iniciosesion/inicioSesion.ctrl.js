(function (ng) {

    // es parte del módulo "personModule"
    var mod = ng.module("inicioSesionModule");

    // crea el controlador con dependencias a $scope y a personService
    mod.controller("inicioSesionCtrl", ["$scope","dataSvc", "InicioSesionService", '$mdDialog', "$state", "$stateParams",
        function ($scope,dataSvc ,svc, $mdDialog,$state) {




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



            var encontro = false;
            $scope.buscar = -1;
            var self = this;
            $scope.currentRecord = {
                id: 0,
                nombre: '',
                apellido: '',
                usuario: '',
                email: '',
                password: ''
            };

            $scope.loginRecord = {
                email: '',
                password: ''
            };



            $scope.searchRecord = {};
            $scope.records = [];


            /*
             * Funcion createRecord emite un evento a los $scope hijos del controlador por medio de la
             * sentencia &broadcast ("nombre del evento", record), esto con el fin cargar la información de modulos hijos
             * al actual modulo.
             * Habilita el modo de edicion. El template de la lista cambia por el formulario.
             *
             */

            $scope.link = "/";
            $scope.iniciarSesion = function () {

                for (var i = 0; i < $scope.records.length && !encontro; i++)
                {
                    $scope.currentRecord = $scope.records[i];
                    if (($scope.currentRecord.email === $scope.loginRecord.email)
                            && ($scope.currentRecord.password === $scope.loginRecord.password))
                    {
                        dataSvc.userId = $scope.currentRecord.id;
                        $state.go("viajero", {userId: $scope.currentRecord.id});
                        return;
                    }
                }

                encontro = false;
                $scope.showAlert("Error","Usuario o contraseña incorrecta")
            };

            $scope.validar = function () {
                if (encontro)
                {
                    return "viajero";
                } else
                {
                    return "/";
                }
            };



            this.saveRecord = function () {
                return svc.saveRecord($scope.currentRecord).then(function () {
                    self.fetchRecords();
                });
            };




            /*
             * Funcion fetchRecords consulta el servicio svc.fetchRecords con el fin de consultar
             * todos los registros del modulo book.
             * Guarda los registros en la variable $scope.records
             * Muestra el template de la lista de records.
             */

            this.fetchRecords = function () {
                return svc.fetchRecords().then(function (response) {
                    $scope.records = response.data;
                    $scope.currentRecord = {};
                    return response;
                });
            };
            this.fetchRecord = function () {
                return svc.fetchRecord($scope.buscar).then(function (response) {
                    $scope.searchRecord = response.data;
                    return response;
                });
            };
            this.deleteRecord = function (username) {
                return svc.deleteRecord(username).then(function () {
                    self.fetchRecords();
                });
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

            this.fetchRecords();

        }]);


})(window.angular);
