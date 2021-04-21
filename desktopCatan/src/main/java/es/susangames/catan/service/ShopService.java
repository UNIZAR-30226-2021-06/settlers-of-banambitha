package es.susangames.catan.service;

import org.json.*;
import java.io.IOException;


public class ShopService {
    private static HttpService netService;
    private static final String baseUrl = "http://localhost:8080/producto";
    private static final String adquiridosUrl = baseUrl + "/adquiridos";
    private static final String adquirirUrl = baseUrl + "/adquirir";
    private static final String disponiblesUrl = baseUrl + "/disponibles";

    public ShopService() {
        netService = new HttpService();
    }

   public JSONArray obtenerProductosDisponibles() {
    JSONArray response;
        try {
            response = netService.getArr(disponiblesUrl + "/" + UserService.getUsername());
        } catch(IOException e) {
            return null;
        }
        return response;
    }
}