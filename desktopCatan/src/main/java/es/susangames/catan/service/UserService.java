package es.susangames.catan.service;

import org.json.*;
import java.io.IOException;


public class UserService {
    private static HttpService netService;
    private static final String baseUrl = "http://localhost:8080/usuario";
    private static final String addUrl = baseUrl + "/add";
    private static final String validateUrl = baseUrl + "/validate";

    private static String username;
    private static String mail;
    private static String idioma;
    private static String avatar;
    private static String apariencia;
    private static Integer saldo;

    public UserService() {
        netService = new HttpService();
    }

    public static String getUsername() {
        return username;
    }

    public static String getMail() {
        return mail;
    }

    public static String getIdioma() {
        return idioma;
    }
    public static String getAvatar() {
        return avatar;
    }

    public static String getApariencia() {
        return apariencia;
    }
    public static Integer getSaldo() {
        return saldo;
    }



    public static Boolean validate(String name, String pass) {
        JSONObject myObject = new JSONObject();
        myObject.put("nombre", name);
        myObject.put("contrasenya", pass);
        JSONObject response;
        try {
            response = netService.post(validateUrl, myObject.toString());
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

    public static Boolean userExists(String name) {
        JSONObject response;
        try {
            response = netService.get(baseUrl + "/find/" + name);
        } catch(IOException e) {
            return false;
        }   
        return (!response.has("error"));
    }


    public static Boolean register(String name, String mail, String pass) {
        JSONObject myObject = new JSONObject();
        myObject.put("nombre", name);
        myObject.put("email", mail);
        myObject.put("contrasenya", pass);
        JSONObject response;
        try {
            response = netService.post(addUrl, myObject.toString());
        } catch(IOException e) {
            return false;
        }   
        return (!response.has("error"));
    }

    public static void fillData(JSONObject data) {
        username = data.get("nombre").toString();
        mail = data.get("email").toString();
        idioma = data.get("idioma").toString();
        avatar = data.get("avatar").toString();
        apariencia = data.get("apariencia").toString();
        saldo = Integer.parseInt(data.get("saldo").toString());
        
    }
    
}