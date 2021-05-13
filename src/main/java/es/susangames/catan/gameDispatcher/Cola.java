package es.susangames.catan.gameDispatcher;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Cola {

	private static Map<String,Sala> individuos 	= new LinkedHashMap<String, Sala>();
	private static Map<String,Sala> parejas 	= new LinkedHashMap<String, Sala>();
	private static Map<String,Sala> grupos 		= new LinkedHashMap<String, Sala>();
	
	private static Object lock = new Object();
	
	public Cola() {}
	
	public void encolar(Sala sala) {
		
		synchronized (lock) {
			
			switch (sala.size()){
			case 1: {
				individuos.put(sala.getId(), sala);
				break;
			}
			case 2:{
				parejas.put(sala.getId(), sala);
				break;
			}
			case 3:{
				grupos.put(sala.getId(), sala);
				break;
			}
			default:
				throw new IllegalArgumentException("Unexpected Group Size: " + sala.size() + "for Room " + sala.getId());
			}
		}
	}
	
	public Sala desencolar(String salaId) {
		
		Sala sala;
		
		synchronized (lock) {
			
			sala = individuos.remove(salaId);
		
			if(sala == null) {
				
				sala = parejas.remove(salaId);
				
				if(sala == null) {
					
					sala = grupos.remove(salaId);
				}
			}
		}
		
		return sala;
	}
	
	public List<Sala> emparejar(){
		
		List<Sala> salas = new ArrayList<Sala>();
		
		synchronized (lock) {
			
			if(!grupos.isEmpty() && !individuos.isEmpty()) {
				
				Sala grupo = grupos.remove(grupos.keySet().toArray(new String[0])[0]);
				Sala indiv = individuos.remove(individuos.keySet().toArray(new String[0])[0]);
				
				salas.add(grupo);
				salas.add(indiv);
				
			} else if (parejas.size() >= 2) {
				
				String parejaIds[] = parejas.keySet().toArray(new String[0]);
				
				Sala p1 = parejas.remove(parejaIds[0]);
				Sala p2 = parejas.remove(parejaIds[1]);
				
				salas.add(p1);
				salas.add(p2);

			} else if (!parejas.isEmpty() && individuos.size() >= 2) {
				
				Sala pareja = parejas.remove(parejas.keySet().toArray(new String[0])[0]);
				
				String indivIds[] = individuos.keySet().toArray(new String[0]);
				
				Sala i1 = individuos.remove(indivIds[0]);
				Sala i2 = individuos.remove(indivIds[1]);
				
				salas.add(pareja);
				salas.add(i1);
				salas.add(i2);
				
			} else if (individuos.size() >= 4) {
				
				String indivIds[] = individuos.keySet().toArray(new String[0]);
				
				for(String id : indivIds) {
					
					Sala indiv = individuos.remove(id);
					salas.add(indiv);
				}
				
			} else {
				
				return null;
			}
		}
		
		return salas;
	}
	
	public void waitOnCola() {
		synchronized (lock) {
			try {
				lock.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void notifyOnCola() {
		synchronized (lock) {
			lock.notify();
		}
	}
}
