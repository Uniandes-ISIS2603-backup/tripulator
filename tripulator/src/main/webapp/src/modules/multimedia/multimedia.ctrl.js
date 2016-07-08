(function (ng) {
    var mod = ng.module("multimediaModule");

    mod.controller("multimediaCtrl", ["$scope", "multimediaService",'$stateParams', function ($scope, svc,$stateParams) {

            var self = this;
            
            $scope.currentIndex = 0;

            var currentRecord = {
                id: Number,
                src: String
            };

            $scope.slides = [];

            $scope.splitSlides = [];

            $scope.showAll = false;
            
            $scope.newUrl="";

            $scope.changeView = function ()
            {
                $scope.showAll = !$scope.showAll;
            };


            this.splitSlides = function () {
                var tot = [];
                var size = 4;
                var bigarray = $scope.slides;
                for (var i = 0; i < bigarray.length; i += size) {
                    var smallarray = bigarray.slice(i, i + size);
                    tot.push(smallarray);
                }

                $scope.splitSlides = tot;

            };


            function responseError(response) {
                self.showError(response.data);
            };
           

            $scope.setCurrentSlideIndex = function (index) {

                $scope.currentIndex = index;

            };

            $scope.isCurrentSlideIndex = function (index) {
                return $scope.currentIndex === index;
            };

            $scope.prevSlide = function () {
                $scope.currentIndex = ($scope.currentIndex < $scope.slides.length - 1) ? ++$scope.currentIndex : 0;

            };

            $scope.nextSlide = function () {
                $scope.currentIndex = ($scope.currentIndex > 0) ? --$scope.currentIndex : $scope.slides.length - 1;

            };


            $scope.addSlide = function ()
            {
                if ($scope.newUrl !== null) {
                    currentRecord =
                            {
                                id: '',
                                src: $scope.newUrl
                            };
                         
                      return svc.saveRecord($stateParams.userId,$stateParams.tripId,currentRecord).then(function () { 
                    self.fetchRecords();
                }, responseError);
                    
                    //svc.saveRecord(1,1,currentRecord).then(function (response) {
                        
                     ///   self.fetchRecords();
                    //}, responseError);
                }

            };

            /*
             * Funcion fetchRecords consulta el servicio svc.fetchRecords con el fin de consultar 
             * todos los registros del modulo book.
             * Guarda los registros en la variable $scope.records
             * Muestra el template de la lista de records.
             */

            this.fetchRecords = function () {
                return svc.fetchRecords($stateParams.userId ,$stateParams.tripId).then(function (response) {
                    self.inicializarSlides(response.data);
                    self.splitSlides();
                }, responseError);
            };
            
            this.inicializarSlides=function(arreglo)
            {
                if(typeof $scope.slides !=='undefined' && $scope.slides.length > 0)
                {
                    $scope.slides=[];
                    
                }
                
                for(var i=0;i<arreglo.length;i++)
                {
                    var obj=
                            {
                                id: arreglo[i].id,
                                index: i,
                                src: arreglo[i].src
                            };
                            
                    $scope.slides.push(obj);
                    
                }
                
                
            };


            /*
             * Funcion deleteRecord hace un llamado al servicio svc.deleteRecord con el fin
             * de eliminar el registro asociado.
             * Muestra el template de la lista de records al finalizar el borrado del registro.
             */
            $scope.deleteRecord = function () {
               
                return svc.deleteRecord($stateParams.userId,$stateParams.tripId,self.darIdFoto($scope.currentIndex)).then(function () { 
                    self.fetchRecords();
                }, responseError);
            };
            
            this.darIdFoto=function(pIndex)
            {
                for(var i=0;i<$scope.slides.length;i++)
                {
                    var search=$scope.slides[i].index;
                    if(pIndex===search)
                    {
                        return $scope.slides[i].id;
                        
                    }
                    
                }
                
            };

            /*
             * Funcion fetchRecords consulta todos los registros del módulo book en base de datos
             * para desplegarlo en el template de la lista.
             */
            this.fetchRecords();


        }]);
})(window.angular);




