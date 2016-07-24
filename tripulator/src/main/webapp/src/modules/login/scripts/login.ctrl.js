(function (ng) {
    var mod = ng.module('LoginModule');
    mod.controller('LoginController', ['$scope', '$mdDialog', '$state', function ($scope, $mdDialog, $state) {
        $scope.user = {
            username: '',
            password: ''
        };

        $scope.showRegister = function (ev) {
            $mdDialog.show({
                    controller: RegisterController,
                    templateUrl: './modules/login/views/register.tpl.html',
                    parent: angular.element(document.body),
                    targetEvent: ev,
                    clickOutsideToClose: true,
                    fullscreen: true
                })
                .then(function (newUser) {
                    console.log(newUser);
                }, function () {
                    console.log('You cancelled the dialog.');
                });
        };

        $scope.logIn = function () {
            $state.go('trips', {});
        };
    }]);
})(window.angular);
