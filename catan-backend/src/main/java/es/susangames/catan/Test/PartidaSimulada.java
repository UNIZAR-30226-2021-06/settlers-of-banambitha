package es.susangames.catan.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;

import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONObject;

import es.susangames.catan.gameDispatcher.MoveCarrierHeap;

/**
 * Clase que simula una partida de catán.
 * Sirve únicamente para Test desde el front-end. 
 * 
 */
public class PartidaSimulada implements Runnable{
   
    private static String ficheroJugadas = "/test/jugadasSimuladas.json"; 
    private static long tiempoEsperaMs = 3000; 
    private String idPartida; 
    private MoveCarrierHeap mch;
    private ArrayList<Object> jugadas; 


    /**
     * Constructor
     * @param idPartida identificador de la partida en la que se inyectarán jugadas
     * @param mch MoveHeapCarrier del servidor
     */
    public PartidaSimulada(String idPartida, MoveCarrierHeap mch){
        System.out.println("ID_partida_PRUEBA: " + idPartida); 
        this.mch = mch; 
        this.idPartida = idPartida; 
    }


    /**
     * Método que ejecuta el thread. 
     * Cada tiempoEsperaMs milisegundos genera una nueva jugada para
     * la partida. Las jugadas son leídas del fichero ficheroJugadas. 
     * Una vez se terminan las jugadas del fichero, se termina la partida. 
     */
    @Override
    @SuppressWarnings("unchecked")
    public void run() {
        try {
            if ( leerJugadas() ) {
                //Tiempo de espera antes de enviar la primera jugada
                Thread.sleep(tiempoEsperaMs);
                for ( Object obj: jugadas ){
                    Map<String,Object> jugada = (Map<String,Object>) obj; 

                    Thread.sleep(tiempoEsperaMs);

                    JSONObject jugadaJSON =  new JSONObject(jugada);
                     
                    System.out.println("---> Simulando jugada"); 
                    System.out.println(jugadaJSON); 

                    mch.newJugada(idPartida,jugadaJSON);
                }
            }
            //Esperar antes de cerrar la partida
            Thread.sleep(tiempoEsperaMs * 2);

        } catch (Exception e){
            e.printStackTrace();
        }

        mch.deleteGame(idPartida); 
    } 


    /**
     * Lee las jugadas que se simularán de un fichero JSON. 
     * 
     * @return true si ha podido leer las jugadas, false en caso 
     * contrario. 
     */
    @SuppressWarnings("unchecked")
    private boolean leerJugadas(){
        try {
            InputStream isr =
                PartidaSimulada.class.getResourceAsStream(ficheroJugadas); 

            JSONParser parser = new JSONParser(new InputStreamReader(isr));

            Map<String,Object> jsonObject =
                (Map<String,Object>) parser.parse();

            jugadas= (ArrayList<Object>) jsonObject.get("jugadas"); 
            return true; 
 
		} catch (Exception e) {
            e.printStackTrace();
        }
        return false; 
    }


    /**
     * Muestra todas las jugadas leídas (jugadas a simular)
     * por la salida estándar
     */
    @SuppressWarnings("unchecked")
    public void printJugadas(){
        for ( Object obj: jugadas ){
            Map<String,Object> jugada = (Map<String,Object>) obj; 

            try {
                Thread.sleep(1000);
            } catch (Exception e) { e.printStackTrace(); }

            System.out.println(new JSONObject(jugada)); 
        }
    }

}
