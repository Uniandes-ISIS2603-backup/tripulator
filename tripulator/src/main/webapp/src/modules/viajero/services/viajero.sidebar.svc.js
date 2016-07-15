(function (ng) {
    var mod = ng.module("viajeroModule");
    mod.service('sidebarService', function () {
            var selectedItem = {};
            var items = [];
            var update = true;
            
            this.mustUpdate = function(){
                return update;
            };
            
            this.toggleUpdate = function(){
                update = !update;
            };
            
            this.getItems = function(){
                return items;
            };
            
            this.getSelectedItem = function(){
                return selectedItem;
            };
            
            this.setSelectedItem = function(item){
                selectedItem = item;
            };
            
            this.setItems = function(newItems){
                items.length = 0;
                items.push.apply(items, newItems);
            };
    });
})(window.angular);

