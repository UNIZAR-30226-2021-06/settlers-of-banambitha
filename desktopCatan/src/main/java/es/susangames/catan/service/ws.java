package es.susangames.catan.service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import es.susangames.catan.controllers.MainMenu;
import es.susangames.catan.App;


import org.json.JSONObject;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import org.json.*;
import javafx.application.Platform;
//http://localhost:8080
//https://catan-backend-app.herokuapp.com/
public class ws {
    public static final StompSession session;
    private static final String productionURL = "https://catan-backend-app.herokuapp.com";
    public static Map<String,ArrayList<JSONObject>> msgs;
    private static final String appPrefix = "/app";
    private static final String  wsUrl = productionURL + "/catan-stomp-ws-ep";
    private static final String chatUrl = "/chat/";
    private static final String newFriendReqUrl = "/peticion/";
    private static final String playerActUrl = "/usuario-act/";
    private static final String sendFriendRequestUrl = "/app/enviar/peticion";
    private static final String acceptFriendRequestUrl = "/app/aceptar/peticion";
    private static final String declineFriendRequestUrl = "/app/rechazar/peticion";
    public  static final String proponerComercio =  "/app/partida/comercio/proponer";
    public  static final String aceptarComercio = appPrefix + "/partida/comercio/aceptar";
    public  static final String rechazarComercio = appPrefix + "/partida/comercio/rechazar";
    public  static final String enviarMensajePartida = appPrefix + "/enviar/partida";
    public static final String  borrarCuenta = appPrefix + "/usuario/eliminar";



    

    // Partida
    public static final String partidaTestComenzar = "/app/partida/test";
    public static final String partida_act_topic = "/partida-act/";
    public static final String partida_chat_topic = "/partida-chat/";
    public static final String partida_com_topic = "/partida-com/";
    public static final String partida_test_topicUrl = "/test-partida/";
    public static final String  partidaJugada= "/app/partida/jugada";
    




    static {
        msgs = new HashMap<String, ArrayList<JSONObject>>();

        WebSocketClient webSocketClient = new StandardWebSocketClient();
        List<Transport> transports = new ArrayList<Transport>(); 
        transports.add(new WebSocketTransport(webSocketClient)); 
        SockJsClient sockJsClient = new SockJsClient(transports);
        WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
        stompClient.setMessageConverter(new StringMessageConverter());

        try {
            session = stompClient
                    .connect(
                        wsUrl, 
                        new StompSessionHandlerAdapter() {})
                    .get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
              
        // Chat
        session.subscribe( chatUrl + UserService.getUsername(), new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return String.class;
            }
            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                msgReceived( payload);
            }
        });
       
        // New friend request
       session.subscribe( newFriendReqUrl + UserService.getUsername(), new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return String.class;
            }
            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                handlePetition(payload.toString());
            }
        });

        // Player act
       session.subscribe( playerActUrl + UserService.getUsername(), new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return String.class;
            }
            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                handlePetitionPlayerAct(payload.toString());
            }
        });
    }

    public static void initialize () {}


    private static void msgReceived(Object msgContent) {
        JSONObject object = new JSONObject((String) msgContent);
        ArrayList<JSONObject> itemsList = msgs.get(object.getString("from"));
        
        if(itemsList == null) {
            itemsList = new ArrayList<JSONObject>();
            itemsList.add(object);
            msgs.put(object.getString("from"), itemsList);
        } else {
             itemsList.add(object);
        }

        if(MainMenu.chatOpenned) {
            if(MainMenu.userChatOpenned.equals(object.getString("from"))) {
                Platform.runLater(new Runnable() {
                    @Override public void run() {
                        MainMenu._chatContent.appendText(object.getString("from") + 
                                                        ": "  + 
                                                        object.getString("body") + "\n");
                    }
                  });               
            }
        }
    }


    public static void sendFriendRequest(String friendName) {
        JSONObject myObject = new JSONObject();
        myObject.put("from", UserService.getUsername());
        myObject.put("to", friendName);
        session.send(sendFriendRequestUrl, myObject.toString());
    }

    private static void handlePetition(String msgContent) {
        JSONObject object = new JSONObject(msgContent);
        String type = object.getString("type");
        String from = object.getString("from");

        if(!MainMenu.chatOpenned) {
            Platform.runLater(new Runnable() {
                @Override public void run() {
                    MainMenu.getFriends();
                }
              });
        } 
    }

    public static void acceptFriendRequest(String friendName) {
        JSONObject myObject = new JSONObject();
        myObject.put("from", UserService.getUsername());
        myObject.put("to", friendName);
        session.send(acceptFriendRequestUrl, myObject.toString());
        try{
            Thread.sleep(500); // Esperamos a que se procese 
            Platform.runLater(new Runnable() {
                @Override public void run() {
                    MainMenu.getFriends();
                }
              });
        } catch(Exception e){}
       
        
       
    }

    public static void declineFriendRequest(String friendName) {
        JSONObject myObject = new JSONObject();
        myObject.put("from", UserService.getUsername());
        myObject.put("to", friendName);
        session.send(declineFriendRequestUrl, myObject.toString());
        try{
            Thread.sleep(500); // Esperamos a que se procese 
            Platform.runLater(new Runnable() {
                @Override public void run() {
                    MainMenu.getFriends();
                }
              });
        } catch(Exception e){}
    }



    public static void sendPrivateMsg(String friend, String body) {
        JSONObject object = new JSONObject();
        object.put("from", UserService.getUsername());
        object.put("to", friend);
        object.put("body", body); 

       ArrayList<JSONObject> itemsList = msgs.get(friend);
        
        if(itemsList == null) {
            itemsList = new ArrayList<JSONObject>();
            itemsList.add(object);
            msgs.put(friend, itemsList);
        } else {
            itemsList.add(object);
        }
        session.send("/app/enviar/privado", object.toString()); 
    }

    public static void borrarCuenta(String password) {
        JSONObject object = new JSONObject();
        object.put("nombre", UserService.getUsername());
        object.put("contrasenya", password);
        System.out.println(object);
        session.send(borrarCuenta, object.toString());
    }

    public static void handlePetitionPlayerAct(String petition) {
        JSONObject object = new JSONObject(petition);
        if(object.getString("status").equals("DELETED")) {
            try {
                App.nuevaPantalla("/view/Login.fxml");
            } catch(Exception e) {}
        }
    }
}