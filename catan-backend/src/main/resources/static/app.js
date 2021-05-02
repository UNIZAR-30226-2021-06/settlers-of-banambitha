var stompClient = null;

function connect() {
    var socket = new SockJS('/catan-stomp-ws-ep');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
    });
}

function suscribe(where){
	
	stompClient.subscribe(where, function (mensaje) {
		console.log('Message:' + mensaje);
		var obj = JSON.parse(mensaje.body);
		
		if(obj.to!=null){
			console.log("Privado");
			mostratMsg(obj.from, obj.body, obj.time);
		} else {
			console.log("Partida");
			mostratMsg(obj.from, obj.body, obj.time);
		}
    });
}

function mostratMsg(from, body, time) {
	console.log('Message from ' + from + ": " + body);
    $("#mensajes").append("<tr><td>" + from + ": " + body + " at [" + time + "]</td></tr>");
}

function enviarMensaje(from, to, body) {
    stompClient.send("/app/enviar/privado", {}, JSON.stringify({'from': from, 'to': to, "body": body}));
}

function enviarMensajePartida(from, game, body) {
    stompClient.send("/app/enviar/partida", {}, JSON.stringify({'from': from, 'game': game, "body": body}));
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
 	$( "#suscribe" ).click(function() { suscribe($("#where").val()); });
    $( "#send_private" ).click(function() { enviarMensaje($("#name").val(), $("#to").val(), $("#body").val()); });
    $( "#send_game" ).click(function() { enviarMensajePartida($("#name").val(), $("#game").val(), $("#body").val()); });
});