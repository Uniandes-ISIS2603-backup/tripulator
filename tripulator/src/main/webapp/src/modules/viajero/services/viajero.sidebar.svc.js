(function (ng) {
    var mod = ng.module("viajeroModule");
    mod.service('sidebarService', function () {
            var update = true;
            
            this.mustUpdate = function(){
                return update;
            };
            
            this.toggleUpdate = function(){
                update = !update;
            };
    });
})(window.angular);

