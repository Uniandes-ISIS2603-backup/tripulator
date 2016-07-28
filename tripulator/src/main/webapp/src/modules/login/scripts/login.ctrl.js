(function (ng) {
    var mod = ng.module('LoginModule');
    mod.controller('LoginController', ['$scope', '$mdDialog', '$mdToast', 'TravellerService', '$state', '$stateParams', function ($scope, $mdDialog, $mdToast, travellerService, $state, $stateParams) {
        function noUserCreated() {
            $mdToast.showSimple('No user was created.');
        }

        function userCreated() {
            $mdToast.showSimple('Registered a new user.');
        }

        function saveTraveller(traveller) {
            travellerService.saveTraveller(traveller).then(userCreated, noUserCreated);
        }

        $scope.user = {
            username: '',
            password: ''
        };

        $scope.showRegister = function (ev) {
            $mdDialog.show({
                    controller: RegisterController,
                    templateUrl: './src/modules/login/views/register.tpl.html',
                    parent: angular.element(document.body),
                    targetEvent: ev,
                    clickOutsideToClose: true,
                    fullscreen: true
                })
                .then(function (newTraveller) {
                    saveTraveller(newTraveller);
                }, noUserCreated);
        };

        $scope.logIn = function () {
            $stateParams.traveller = {};
            $stateParams.traveller.id = 1;
            $stateParams.traveller.name = $scope.user.username;
            $state.go('trips', $stateParams);
        };

    }]);
})(window.angular);
