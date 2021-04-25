package es.susangames.catan.gameDispatcher;

import java.util.ArrayList;
import java.util.List;

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
		
		while(!finalizada) {
			
			JSONObject jugada;
			
			synchronized (listaJugadas) {
				
				jugada = listaJugadas.get(0);
			}
			
			System.out.print(jugada.toString());
			//TODO pasar la jugada al controlador y recibir la respuesta
			JSONObject respuesta = new JSONObject();
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
		
		//TODO Actualizar las estádisticas de los jugadores en función del resultado de la partida
		//Comunicar la finalización de la partida
		
		for(String jugador : jugadores) {
			usuarioService.endPartida(jugador);
		}
		
		MoveCarrierHeap.deleteGame(partidaId);
		
		System.out.println("Partida " + partidaId + "finalizada");
	}

}
