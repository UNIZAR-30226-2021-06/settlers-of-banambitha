package es.susangames.catan.service;

import org.json.*;
import java.io.IOException;


public class UserService {
    private static HttpService netService;
    private static final String baseUrl = "http://localhost:8080/usuario";
    private static final String addUrl = baseUrl + "/add";
    private static final String validateUrl = baseUrl + "/validate";
    private static final String updateUrl = baseUrl + "/update";
    private static final String findUrl = baseUrl + "/find";
    private static final String baseFriendUrl = "http://localhost:8080/amigo";
    private static final String friendsListUrl = baseFriendUrl + "/list";
    private static final String pendigReqUrl = baseFriendUrl + "/pending-r";

    private static String username;
    private static String mail;
    private static String idioma;
    private static String avatar;
    private static String apariencia;
    private static Integer saldo;
    private static String partida;

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
    public static String getPartida() {
        return partida;
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
        partida = data.get("partida").toString();
    }

    
    public static JSONObject getUserInfo(String name) {
        JSONObject response;
        try {
            response = netService.get(baseUrl + "/find/" + name);
        } catch(IOException e) {
            return null;
        }   
        return response;
    }

    public static void updateUser(String name, String field, String value) {
        JSONObject myObject = new JSONObject();
        myObject.put("nombre", name);
        myObject.put(field, value);
        String response;
        try {
            response = netService.put(updateUrl, myObject.toString());
            UserService.fillData(new JSONObject(response));
        } catch(Exception e) {}
    }
    
    public static JSONArray getFriends() {
        JSONArray response;
        try {
            response = netService.getArr(friendsListUrl + "/" + username);
        } catch(Exception e) {
            return null;
        }
        return response;
    }


    // Name debe ser el nombre de un usuario valido
    public static String getUserImg(String name) {
        JSONObject myObject;
        try {
            myObject = netService.get(findUrl + "/" + name);
        } catch(Exception e) {
            return null;
        }
        return "/img/users/" + myObject.getString("avatar").toString();
    }


    public static JSONArray pendingFriendReq() {
        JSONArray response;
        try {
            response = netService.getArr(pendigReqUrl + "/" + username);
        } catch(Exception e) {
            return null;
        }
        return response;
    }
}