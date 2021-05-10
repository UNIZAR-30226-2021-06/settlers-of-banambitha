package es.susangames.catan.gameDispatcher;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import es.susangames.catan.WebSocketConfig;
import es.susangames.catan.logica.Tablero;
import es.susangames.catan.service.UsuarioService;

public class GameControllerThread implements Runnable {
	
	private ArrayList<JSONObject> listaJugadas;
	private String partidaId;
	private List<String> jugadores;
	private SimpMessagingTemplate template;
	private UsuarioService usuarioService;
	private Tablero tableroPartida; 
	
	public GameControllerThread(ArrayList<JSONObject> listaJugadas, String partidaId, List<String> jugadores, SimpMessagingTemplate template, UsuarioService usuarioService) {
		this.listaJugadas = listaJugadas;
		this.partidaId = partidaId;
		this.jugadores = jugadores;
		this.template = template;
		this.usuarioService = usuarioService;
	}


	/* ******************************************************
	 * <Color> ::= Azul | Rojo | Amarillo | Verde | Null
	 * length = array.length
	 * 
	 * Returns: -JSON Message
	 * 			-Broadcast point:	/partida-act/<partidaId>
	 * 			-Format:
	 * 			{
	 * 				"Message": "..."
	 *				"exit_status" : 0 (Salida correcta), 1, 2, .. (Salida erronea)
	 * 				"Tab_inf": {
	 * 					"hexagono": {
	 * 						"tipo":   ["Bosque" | "Pasto" | "Sembrado" | "Cerro" | "Montanya" | "Desierto" | "Vacio"],
	 * 						"valor":  [N], length -> 19 
	 * 						"ladron": id_hex //indice del hexágono en el que se encuentra el ladrón 
	 * 					},
	 * 
	 * 					"vertices": {
	 * 						"asentamiento": ["Nada" | "Poblado<Color>" | "Ciudad<Color>"] -> length = 52,
	 * 						"posibles_asentamiento": [ 
	 * 								                  [ "true" | "false" ] -> length = 52, //Asentamientos posibles j1
	 * 								                  [ "true" | "false" ] -> length = 52, //Asentamientos posibles j2
	 * 								                  [ "true" | "false" ] -> length = 52, //Asentamientos posibles j3
	 * 								                  [ "true" | "false" ] -> length = 52  //Asentamientos posibles j4
	 * 								                 ]
	 * 					},
	 * 
	 * 					"aristas": {
	 * 						"camino": [ "Nada" | "Camino<Color>" ] -> length = 72
	 * 						"posibles_camino": [ 
	 * 								            [ "true" | "false" ] -> length = 72, //Asentamientos posibles j1
	 * 								            [ "true" | "false" ] -> length = 72, //Asentamientos posibles j2
	 * 								            [ "true" | "false" ] -> length = 72, //Asentamientos posibles j3
	 * 								            [ "true" | "false" ] -> length = 72  //Asentamientos posibles j4
	 * 								           ]
	 * 						"puertos": {
	 *							puertoMadera: <id_arista> 
	 *							puertoArcilla: <id_arista> 
	 *							puertoMineral: <id_arista> 
	 *							puertoLana: <id_arista> 
	 *							puertoCereales: <id_arista>
	 *							puertosBasicos: [<id_arista>] -> length = 4
	 * 						}
	 * 					}
	 * 				},
	 * 
	 * 				"Resultado_Tirada": 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 10 | 11 | 12,
	 * 
	 * 				"Recursos": {
	 * 					"Player_1": [num_madera, num_mineral, num_arcilla, num_lana, num_cereales], // integer array
	 * 					"Player_2": [num_madera, num_mineral, num_arcilla, num_lana, num_cereales], // integer array
	 * 					"Player_3": [num_madera, num_mineral, num_arcilla, num_lana, num_cereales], // integer array
	 * 					"Player_4": [num_madera, num_mineral, num_arcilla, num_lana, num_cereales], // integer array
	 * 				},
	 * 
	 * 				"Cartas": { 
	 * 					"Player_1": [num_D1, num_D2, num_D3, num_D4, num_D5, num_E1, num_E2], // integer array
	 * 					"Player_2": [num_D1, num_D2, num_D3, num_D4, num_D5, num_E1, num_E2], // integer array
	 * 					"Player_3": [num_D1, num_D2, num_D3, num_D4, num_D5, num_E1, num_E2], // integer array
	 * 					"Player_4": [num_D1, num_D2, num_D3, num_D4, num_D5, num_E1, num_E2], // integer array
	 * 				}, 
	 * 
	 * 				"Puntuacion": {
	 * 					"Player_1": N,
	 * 					"Player_2": N,
	 * 					"Player_3": N,
	 * 					"Player_4": N,
	 * 				},	
	 * 
	 * 				"Turno": 1 | 2 | 3 | 4
	 *				"Ganador": N | 1 | 2 | 3 | 4
	 *				"Clock": N -> número de jugadas ejecutadas (útil para descartar mensajes antiguos)
	 * 			}
	****************************************************** */
	@Override
	public void run() {
		
		boolean finalizada = false;
		JSONObject respuesta;

		//Inicializar tablero
		tableroPartida = new Tablero(); 
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		//Enviar primer mensaje con el tablero vacío a todos los jugadores
		respuesta = tableroPartida.returnMessage(); 
		template.convertAndSend(WebSocketConfig.TOPIC_PARTIDA_ACT + "/" + partidaId, respuesta.toString());
		
		while(!finalizada) {
			
			JSONObject jugada;

			synchronized (listaJugadas) {
				try {
					listaJugadas.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}	
			}
			
			synchronized (listaJugadas) {
				
				jugada = listaJugadas.get(0);
				listaJugadas.remove(0);
			}
			
			System.out.println(jugada.toString());
			
			if(jugada.has("reload")) {
				
				respuesta = tableroPartida.returnMessage(); 
				respuesta.put("playerNames", this.jugadores);
				template.convertAndSend(WebSocketConfig.TOPIC_PARTIDA_RELOAD + "/" + jugada.getString("player"), respuesta.toString());
				
			} else if(jugada.has("left")) {
			
				//TODO Informar de que el jugador ha dejado la partida
			
			} else {
				
				respuesta = tableroPartida.JSONmessage(jugada); 
				respuesta.put("playerNames", this.jugadores);
				template.convertAndSend(WebSocketConfig.TOPIC_PARTIDA_ACT + "/" + partidaId, respuesta.toString());
				finalizada = tableroPartida.hayGanador(); 
				
			}
		}
		
		//Actualizar estadísticas de los jugadores
		JSONObject puntuaciones = (JSONObject) respuesta.get("Puntuacion"); 
		
		List<Integer> puntuacionesList = new ArrayList<Integer>();
		puntuacionesList.add(puntuaciones.getInt("Player_1")); 
		puntuacionesList.add(puntuaciones.getInt("Player_2")); 
		puntuacionesList.add(puntuaciones.getInt("Player_3")); 
		puntuacionesList.add(puntuaciones.getInt("Player_4")); 
		
		for(int i=0 ; i<4 ; i++) {
			
			usuarioService.endPartida(jugadores.get(i), puntuacionesList.get(i));
		}
		
		MoveCarrierHeap.deleteGame(partidaId);
		
		System.out.println("Partida " + partidaId + "finalizada");
	}

}
