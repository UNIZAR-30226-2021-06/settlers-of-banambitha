package es.susangames.catan.gameDispatcher;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import es.susangames.catan.WebSocketConfig;
import es.susangames.catan.service.UsuarioService;

public class GameControllerThread implements Runnable {
	
	private ArrayList<JSONObject> listaJugadas;
	private String partidaId;
	private List<String> jugadores;
	private SimpMessagingTemplate template;
	private UsuarioService usuarioService;
	//TODO controlador de la partida
	
	public GameControllerThread(ArrayList<JSONObject> listaJugadas, String partidaId, List<String> jugadores, SimpMessagingTemplate template, UsuarioService usuarioService) {
		this.listaJugadas = listaJugadas;
		this.partidaId = partidaId;
		this.jugadores = jugadores;
		this.template = template;
		this.usuarioService = usuarioService;
	}


	/* ******************************************************
	 * Returns: -JSON Message
	 * 			-Broadcast point:	/partida-act/<partidaId>
	 * 			-Format:
	 * 				{
	 * 					TODO formato de respuesta
	 *				}
	****************************************************** */
	@Override
	public void run() {
		
		boolean finalizada = false;
		
		//TODO inicializar el controlador de la partida
		
		JSONObject respuesta = new JSONObject();
		
		while(!finalizada) {
			
			JSONObject jugada;
			
			synchronized (listaJugadas) {
				
				jugada = listaJugadas.get(0);
			}
			
			System.out.print(jugada.toString());
			//TODO pasar la jugada al controlador y recibir la respuesta
			
			if(jugada.has("reload")) {
				
				//TODO Petición de reload, enviar toda la información de la partida al jugador que lo ha solicitado
				
			} else {
				
				//TODO pasar la jugada al controlador y recibir la respuesta
				
				respuesta = new JSONObject();
				respuesta.put("respuesta", true);
				template.convertAndSend(WebSocketConfig.TOPIC_PARTIDA_ACT + "/" + partidaId, respuesta.toString());
				
				synchronized (listaJugadas) {
					try {
						listaJugadas.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}	
				}
			}
		}
		
		//TODO Actualizar las estádisticas de los jugadores en función del resultado de la partida
		//Comunicar la finalización de la partida
		
		JSONArray puntuaciones = respuesta.getJSONArray("puntuaciones");
		
		List<Integer> puntuacionesList = new ArrayList<Integer>();
		
		for(Object punt : puntuaciones) {
			puntuacionesList.add(Integer.parseInt(punt.toString()));
		}
		
		for(int i=0 ; i<4 ; i++) {
			
			usuarioService.endPartida(jugadores.get(i), puntuacionesList.get(i));
		}
		
		MoveCarrierHeap.deleteGame(partidaId);
		
		System.out.println("Partida " + partidaId + "finalizada");
	}

}
