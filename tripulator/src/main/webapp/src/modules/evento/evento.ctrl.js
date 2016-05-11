(function (ng) {
    var mod = ng.module("eventoModule");
    mod.controller('EventosController', ['$scope', 'EventosInfoService', 'dataSvc', function ($scope, svc, dataSvc) {

            $scope.events = [];
            $scope.eventoActual = {id:1};
            $scope.commentTemplate = {
                user: dataSvc.userId,
                stars: 0,
                comment: ""
            };
            $scope.commentariosActuales=[];
            $scope.starSelected = false;
            $scope.newComment = angular.copy($scope.commentTemplate);


            $scope.crearEstrellas = function (s) {
                var resp = "";
                var estrellasFaltantes = 5;
                var i = Number(s);
                for (; i > 0; i--) {
                    resp += "&#9733";
                    estrellasFaltantes--;
                }
                for (; estrellasFaltantes > 0; estrellasFaltantes--) {
                    resp += "&#9734";
                }
                var txt = document.createElement("textarea");
                txt.innerHTML = resp;
                return txt.value;
            };
            $scope.comentarioActualStars = function (numb) {
                var i;
                numb = Number(numb);
                var txt = document.createElement("textarea");
                txt.innerHTML = "&#9733";
                var txt2 = document.createElement("textarea");
                txt2.innerHTML = "&#9734";
                for (i = 1; i < numb + 1; i++) {
                    document.getElementById(i).innerHTML = txt.value;
                }
                for (; i < 6; i++) {
                    document.getElementById(i).innerHTML = txt2.value;
                }
                $scope.starSelected = !$scope.starSelected;
                if ($scope.starSelected) {
                    $scope.newComment.stars = numb;
                } else {
                    $scope.newComment.stars = 0;
                }
            };
            var self = this;
            $scope.finComentarioActual = function () {
                self.update($scope.newComment,$scope.eventoActual.id);
                $scope.reset();
            };
            $scope.reset = function () {
                $scope.newComment = angular.copy($scope.commentTemplate);
                $scope.starSelected = false;
                $scope.newComment.comment = "";
                $scope.resetPaint();
            };
            $scope.paintStar = function (numb) {
                if (!$scope.starSelected) {
                    var i;
                    numb = Number(numb);
                    var txt = document.createElement("textarea");
                    txt.innerHTML = "&#9733";
                    var txt2 = document.createElement("textarea");
                    txt2.innerHTML = "&#9734";
                    for (i = 1; i < numb + 1; i++) {
                        document.getElementById(i).innerHTML = txt.value;
                    }
                    for (; i < 6; i++) {
                        document.getElementById(i).innerHTML = txt2.value;
                    }
                }
            };
            $scope.resetPaint = function () {
                if (!$scope.starSelected) {
                    var i = 1;
                    var txt2 = document.createElement("textarea");
                    txt2.innerHTML = "&#9734";
                    for (; i < 6; i++) {
                        document.getElementById(i).innerHTML = txt2.value;
                    }
                }
            };
            var self=this;
            this.fetchEventos = function () {
                return svc.fetchEventos().then(function (response) {
                    $scope.comentariosActuales=[];
                    $scope.events = response.data;

                    if($scope.events.length>=1){
                        $scope.eventoActual=$scope.events[$scope.eventoActual.id-1];
                        console.log($scope.eventoActual);
                        self.getComments();
                    }
                    return response;
                }, function (response){ console.log(response);});
            };

            this.fetchEventosCiudadDia = function (ciudad,dia) {
                return svc.fetchEventosCiudadDia(ciudad,dia).then(function (response) {
                    $scope.comentariosActuales=[];
                    $scope.events = response.data;

                    if($scope.events.length>=1){
                        $scope.eventoActual=$scope.events[$scope.eventoActual.id-1];
                        self.getComments();
                    }
                    return response;
                }, function (response){ console.log(response);});
            };

            this.getComments = function (){
              return svc.getComments($scope.eventoActual.id).then(function (response) {
                    $scope.comentariosActuales = response.data;
                    return response;
                }, function (response){
                    $scope.comentariosActuales=[];
                    console.log(response);
                });
            };
            this.update = function(cmt,idEvento){
                cmt.id_evento=idEvento;
                cmt.id=$scope.comentariosActuales.length+1;
                return svc.saveRecord(cmt,idEvento).then(function () {
                        self.fetchEventos();
                    }, function (response){ console.log(response);});
            };
            $scope.actualizarInfo = function (e) {
                $scope.eventoActual = e;
                self.getComments();
            };

            this.anadirEvento = function (record) {

                $scope.$emit("anadir_e", record);
            };

            function fetchECD(event, args) {
                this.fetchEventosCiudadDia(args.ciudad,args.dia);
            }

            $scope.$on("getEventos", fetchECD);
            this.fetchEventos();
            if($scope.events.length==0){
                $scope.eventoActual.title="No encontramos ningun evento :(";
                $scope.eventoActual.description="Lamentablemente no hay eventos para esta ciudad en este día. ¡Prueba con otro!";
            }
        }]);
})(window.angular);
//TODO: 1)Poblar con eventos
//      2)Hacer fetch de eventos
//      3)Lograr mandarle evento al dia