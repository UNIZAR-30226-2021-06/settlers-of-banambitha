package es.susangames.catan.gameDispatcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import es.susangames.catan.service.UsuarioService;

public class MoveCarrierHeap {

	private static final Map<String, ArrayList<JSONObject>> conjuntoJugadas = new HashMap<String, ArrayList<JSONObject>>();
	
	private SimpMessagingTemplate template;
	private UsuarioService usuarioService;
	private AutoId partidaId;
	
	public MoveCarrierHeap(SimpMessagingTemplate template, UsuarioService usuarioService) {
		this.partidaId = new AutoId('[', ']');
		this.template = template;
		this.usuarioService = usuarioService;
	}
	
	
	public String newGame(List<String> jugadores) {
		
		String Id = partidaId.nextId();
		
		ArrayList<JSONObject> listaJugadas = new ArrayList<JSONObject>();
		
		GameControllerThread gameController = new GameControllerThread(listaJugadas, Id, jugadores, template, usuarioService);
		Thread gameControllerThread = new Thread(gameController, Id);
		gameControllerThread.start();
		
		synchronized (conjuntoJugadas) {
			conjuntoJugadas.put(Id, listaJugadas);
		}
		
		return Id;
	}
	
	public static boolean deleteGame(String partidaId) {
		
		synchronized (conjuntoJugadas) {
			return conjuntoJugadas.remove(partidaId)!=null;
		}
	}
	
	public static void newJugada(String partidaId, JSONObject jugada) {
		
		synchronized (conjuntoJugadas) {
			
			ArrayList<JSONObject> listaJugadas = conjuntoJugadas.get(partidaId);
		
			synchronized (listaJugadas) {
				
				listaJugadas.add(jugada);
				listaJugadas.notify();
			}
		}
	}
}
