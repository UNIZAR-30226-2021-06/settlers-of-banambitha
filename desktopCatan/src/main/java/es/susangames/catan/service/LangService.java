package es.susangames.catan.service;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


/**
 * Clase que implementa un servidor de lenguaje. 
 * Proporciona las cadenas de caracteres a toda la aplicación. 
 */
public class LangService {
   
    //Contenido del fichero strings.json procesado
    private static Map<String,Map<String,String>> strings;

    //Lenguaje actual
    private static String language; 

    //Constantes
    public static String ESP = "esp"; 
    public static String ENG = "eng";


    //Constructor estático
    static {
        language = ESP; //Español es el lenguaje por defecto.
        readStringsJSON();
    }


    /**
     * Procesa el fichero JSON que contiene las cadenas de caracteres
     * de la aplicación. 
     */
    @SuppressWarnings("unchecked")
    private static void readStringsJSON(){
        JSONParser parser = new JSONParser();
		try {
            InputStream isr =
                LangService.class.getResourceAsStream("/lang/strings.json"); 

            JSONObject jsonObject =
                (JSONObject) parser.parse(new InputStreamReader(isr));

            strings = 
                (Map<String,Map<String,String>>) new ObjectMapper().
                readValue(jsonObject.toString(), HashMap.class);
 
		} catch (Exception e) {
			e.printStackTrace();
		} catch (Error e){
            e.printStackTrace();
        }
    }


    /**
     * Devuelve la cadena de caracteres mapeada a la clave dada. 
     * 
     * @param key clave de la cadena de caracteres deseada.
     * @return cadena de caracteres deseada en el idioma actual del servicio.
     */
    public static String getMapping(String key){
        Map<String,String> keyMap = strings.get(key); 
        if (keyMap != null){
            return strings.get(key).get(language); 
        }
        return "NO_STRING"; 
    }


    /**
     * Cambia el lenguaje del servicio
     * @param newLanguage nuevo lenguaje del servicio
     * @return True si el lenguaje está disponible y se puede cambiar el 
     *         lenguaje. En caso contrario devuelve false y el lenguaje permanece
     *         como estaba. 
     */
    public static boolean changeLanguage(String newLanguage){
        if ( !available(newLanguage)){
            return false; 
        }
        language = newLanguage; 
        return true; 
    }


    /**
     * Devuelve la cadendena de caracteres que simboliza el lenguaje actual del servicio
     * @return La cadena de caracteres que codifica el lenguaje actual del servicio
     */
    public static String getLang(){
        return language;
    }


    /**
     * Comprueba si un determinado lenguaje está disponible. No se esperan valores 
     * distintos de español o inglés. 
     * @param language string que codifica el lenguaje
     * @return True si el lenguaje está disponible, false en caso contrario. 
     */
    private static boolean available(String language){
        return language.equals(ESP) || language.equals(ENG); 
    }


}
