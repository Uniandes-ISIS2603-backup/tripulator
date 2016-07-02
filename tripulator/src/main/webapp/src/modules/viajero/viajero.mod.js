(function (ng){
    var mod = ng.module("viajeroModule",["ui.bootstrap", "googlechart", "ngMaterial", "restangular"]);
    mod.constant("viajeroContext", "http://localhost:8080/tripulator/api/viajeros");
})(window.angular);