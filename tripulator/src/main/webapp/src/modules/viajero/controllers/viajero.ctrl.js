(function (ng) {
    var mod = ng.module("viajeroModule");
    mod.controller('ViajeroController', ['viajeroS', '$stateParams', '$state', 'sidebarService',
        function (svc, $stateParams, $state, sidebarService) {
            console.log($state.current.name);
            
            var self = this;
            /**
             * Displays an error.
             * @param {type} response
             * @returns {undefined}
             */
            function responseError(response) {
                self.showError(response);
            }

            /**
             * Fetches all the trips of the user.
             * @returns {undefined}
             */
            this.getItinerarios = function () {
                svc.getItinerarios($stateParams.userId).then(function (response) {
                    sidebarService.setItems(response.data);
                }, responseError);
            };
            if(sidebarService.mustUpdate()){
                this.getItinerarios();
                sidebarService.toggleUpdate();
                sidebarService.setSelectedItem(sidebarService.getItems()[0]);
            }
        }]);
})(window.angular);

