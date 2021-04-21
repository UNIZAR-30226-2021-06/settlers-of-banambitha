package es.susangames.catan.service;

import org.json.*;
import java.io.IOException;


public class UserService {
    private static HttpService netService;
    private static final String baseUrl = "http://localhost:8080/usuario";
    private static final String addUrl = baseUrl + "/add";
    private static final String validate = baseUrl + "/validate";

    private static String username;
    private static String mail;
    private static String idioma;
    private static String avatar;
    private static String apariencia;
    private static Integer saldo;

    public UserService() {
        netService = new HttpService();
    }



    public static Boolean validate(String name, String pass) {
        JSONObject myObject = new JSONObject();
        myObject.put("nombre", name);
        myObject.put("contrasenya", pass);
        JSONObject response;
        try {
            response = netService.post(validate, myObject.toString());
        } catch(IOException e) {
            return false;
        }
        if (response.get("nombre").toString() != "null") {
            fillData(response);
            return true;
        } else {
            return false;
        }
    }


    private static void fillData(JSONObject data) {
        username = data.get("nombre").toString();
        mail = data.get("email").toString();
        idioma = data.get("idioma").toString();
        avatar = data.get("avatar").toString();
        apariencia = data.get("apariencia").toString();
        saldo = Integer.parseInt(data.get("saldo").toString());
        
    }
}