package es.susangames.catan.service;

import org.json.*;
import java.io.IOException;
import java.util.regex.Pattern;

//http://localhost:8080/
//https://catan-backend-app.herokuapp.com/
public class UserService {
    private static HttpService netService;
    private static final String productionURL = "https://catan-backend-app.herokuapp.com";
    private static final String baseUrl = productionURL + "/usuario";
    private static final String addUrl = baseUrl + "/add";
    private static final String validateUrl = baseUrl + "/validate";
    private static final String updateUrl = baseUrl + "/update";
    private static final String findUrl = baseUrl + "/find";
    private static final String baseFriendUrl = productionURL + "/amigo";
    private static final String friendsListUrl = baseFriendUrl + "/list";
    private static final String pendigReqUrl = baseFriendUrl + "/pending-r";
    private static final String estadisticasUrl = baseUrl + "/stats";
    private static final String newPasswordUrl = baseUrl + "/new-password";


    // Informacion basica
    private static String username;
    private static String mail;
    private static String idioma;
    private static String avatar;
    private static String apariencia;
    private static Integer saldo;
    private static String partida;

    // Estadisticas
    private static Integer totalDeVictorias;
    private static Integer partidasJugadas;
    private static Integer rachaDeVictoriasActual;
    private static Integer mayorRachaDeVictorias;
    
    // Regex comprobaciones register
    private static String regexPassword = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,32}$";
    private static String regexEmail = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    private static String regexName = "^[a-zA-Z0-9_-]{5,}$";
    
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

    public static Integer getTotalDeVictorias() {
        return totalDeVictorias;
    }

    public static Integer getPartidasJugadas() {
        return partidasJugadas;
    }

    public static Integer getRachaDeVictoriasActual() {
        return rachaDeVictoriasActual;
    }

    public static Integer getMayorRachaDeVictorias() {
        return mayorRachaDeVictorias;
    }

    public static Boolean validate(String name, String pass) {
        JSONObject myObject = new JSONObject();
        myObject.put("nombre", name);
        myObject.put("contrasenya", pass);
        JSONObject response;
        try {
            response = netService.post(validateUrl, myObject.toString());
            System.out.println(response);
        } catch(IOException e) {
            return false;
        }
        if (response != null && !response.isNull("nombre") && response.isNull("bloqueado")) {
            fillData(response);
            fillStatsData(getUserStats());
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

    /* */
    public static Boolean register(String name, String mail, String pass) {
        if(Pattern.matches(regexEmail, mail) && Pattern.matches(regexPassword, pass) 
          && Pattern.matches(regexName, name)) {
            JSONObject myObject = new JSONObject();
            myObject.put("nombre", name);
            myObject.put("email", mail);
            myObject.put("contrasenya", pass);
            JSONObject response;
            try {
                response = netService.post(addUrl, myObject.toString());
            } catch(IOException e) {
                System.out.println("Error al registrar el usuario");
                return false;
            }   
            return (!response.has("error"));
          } 
        return false;
        
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

    public static JSONObject getUserStats() {
        JSONObject myObject;
        try {
            myObject = netService.get(estadisticasUrl + "/" + username);
        } catch(Exception e) {
            return null;
        }
        return myObject;
    }

    public static JSONObject getFriendStats(String friendName) {
        JSONObject myObject;
        try {
            myObject = netService.get(estadisticasUrl + "/" + friendName);
        } catch(Exception e) {
            return null;
        }
        return myObject;
    }

    private static void fillStatsData( JSONObject stats) {
        totalDeVictorias = stats.getInt("totalDeVictorias");
        partidasJugadas = stats.getInt("partidasJugadas");
        rachaDeVictoriasActual = stats.getInt("rachaDeVictoriasActual");
        mayorRachaDeVictorias = stats.getInt("mayorRachaDeVictorias");
    }

    public static Boolean passwordMatches(String password) {
        return Pattern.matches(regexPassword, password);
    }

    public static Boolean changePassword(String oldPassword, String newPassword)  {
        JSONObject myObject = new JSONObject();
        myObject.put("userId", username);
        myObject.put("oldPassw", oldPassword);
        myObject.put("newPassw", newPassword);
        String response;
        JSONObject myObjectResponse;
        try {
            response = netService.put(newPasswordUrl, myObject.toString());
            myObjectResponse = new JSONObject(response);
        } catch(Exception e) {
            return false;
        }
        return !myObjectResponse.isNull("nombre");
    }
}