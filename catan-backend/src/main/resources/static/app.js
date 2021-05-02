var stompClient = null;

function connect() {
    var socket = new SockJS('/catan-stomp-ws-ep');
    stompClient = Stomp.over(socket);
    stompClient.connect({"user-id":"PACO"}, function (frame) {
        console.log('Connected: ' + frame);
    });
}

function suscribe(where){
	
	stompClient.subscribe(where, function (mensaje) {
		console.log('Message:' + mensaje);
		var obj = JSON.parse(mensaje.body);
		mostratMsg(obj);
    }, {});
}

function unsuscribe(where){
	
	stompClient.unsubscribe(where);
}

function mostratMsg(msg) {
    $("#mensajes").append("<tr><td>" + JSON.stringify(msg) + "</td></tr>");
}

function enviarMensaje(where, msg) {
    stompClient.send(where, {}, msg);
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
 	$( "#suscribe" ).click(function() { suscribe($("#where").val()); });
 	$( "#unsuscribe" ).click(function() { unsuscribe($("#where").val()); });
    $( "#send" ).click(function() { enviarMensaje($("#where").val(), $("#msg").val()); });
});