package es.susangames.catan.service;

import org.json.*;
import java.io.IOException;

//http://localhost:8080
//https://catan-backend-app.herokuapp.com/
public class ShopService {
    private static HttpService netService;
    //private static final String baseUrl = "http://localhost:8080/producto";
    private static final String baseUrl = "https://catan-backend-app.herokuapp.com/producto";
    private static final String adquiridosUrl = baseUrl + "/adquiridos";
    private static final String adquirirUrl = baseUrl + "/adquirir";
    private static final String disponiblesUrl = baseUrl + "/disponibles";

    public ShopService() {
        netService = new HttpService();
    }

   public static JSONArray obtenerProductosDisponibles() {
    JSONArray response;
        try {
            response = netService.getArr(disponiblesUrl + "/" + UserService.getUsername());
        } catch(Exception e) {
            return null;
        }
        return response;
    }

    public static void adquirirProducto(String id_producto) {
        JSONObject myObject = new JSONObject();
        myObject.put("usuario_id", UserService.getUsername());
        myObject.put("producto_id", id_producto);
        String response;
        try {
            response = netService.put(adquirirUrl, myObject.toString());
            JSONObject arr = new JSONObject(response);
            JSONObject aux = new JSONObject(arr.get("usuario").toString());
            UserService.fillData(aux);
        } catch(Exception e) {}
        
    }


    public static JSONArray obtenerProductosAdquiridos() {
        JSONArray response;
        try {
            response = netService.getArr(adquiridosUrl + "/" + UserService.getUsername());
        } catch(Exception e) {
            return null;
        }
        return response;
    }

}