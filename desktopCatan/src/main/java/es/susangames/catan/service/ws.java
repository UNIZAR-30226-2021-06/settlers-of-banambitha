package es.susangames.catan.service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import es.susangames.catan.controllers.MainMenu;

import org.json.JSONObject;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSession.Subscription;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
//import org.graalvm.compiler.lir.LIRInstruction.Use;
import org.json.*;
import javafx.application.Platform;
//import jdk.internal.jshell.tool.resources.version;
import es.susangames.catan.service.RoomServices;


public class ws {

    public static Map<String,ArrayList<JSONObject>> msgs;
    private static final String  wsUrl = "http://localhost:8080/catan-stomp-ws-ep";
    private static final String chatUrl = "/chat/";
    private static final String newFriendReqUrl = "/peticion/";
    private static final String sendFriendRequestUrl = "/app/enviar/peticion";
    private static final String acceptFriendRequestUrl = "/app/aceptar/peticion";
    private static final String declineFriendRequestUrl = "/app/rechazar/peticion";
    private static final String invitationRequestUrl = "/invitacion/";
    private static final String createRoomRequestUrl = "/sala-crear/";
    public static final String salaActRequestUrl = "/sala-act/";
    public static final StompSession session;

    public static final String crearSala = "/app/sala/crear";


    private static Subscription invitacion_topic_id;
    private static Subscription sala_crear_topic_id;
    private static Subscription sala_act_topic_id;

    static {
        msgs = new HashMap<String, ArrayList<JSONObject>>();

        WebSocketClient webSocketClient = new StandardWebSocketClient();
        List<Transport> transports = new ArrayList<Transport>(); 
        transports.add(new WebSocketTransport(webSocketClient)); 
        SockJsClient sockJsClient = new SockJsClient(transports);
        WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
        stompClient.setMessageConverter(new StringMessageConverter());

        WebSocketHttpHeaders wshh = new WebSocketHttpHeaders ();
        StompHeaders sh = new StompHeaders ();
        sh.add("user-id", UserService.getUsername());
        //wshh.add("user-id", UserService.getUsername());

        try {
            session = stompClient
                    .connect(
                        wsUrl, wshh, sh, 
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

        // invitacion
        invitacion_topic_id = session.subscribe( invitationRequestUrl + UserService.getUsername(), new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return String.class;
            }
            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                RoomServices.procesarMensajeInvitacion(payload.toString());
            }
        });

        // sala crear createRoomRequestUrl
        sala_crear_topic_id = session.subscribe( createRoomRequestUrl + UserService.getUsername(), new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return String.class;
            }
            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                RoomServices.procesarMensajeCreacionSala (payload.toString());
            }
        });

        RoomServices.crearSala();

        JSONObject JSUserInfo = UserService.getUserInfo(UserService.getUsername());
        System.out.println(JSUserInfo.toString(4));
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


    public static void SubscribeSalaAct () {
        sala_act_topic_id = session.subscribe(salaActRequestUrl + RoomServices.room.getId(), new StompFrameHandler () {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return String.class;
            }
            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                procesarMensajeAccionSala(payload.toString());
            }
        });
        RoomServices.creandoSala = false;
        System.out.println("sala creada");
    }

    public static void procesarMensajeAccionSala (Object payload) {
        if (RoomServices.room != null) {
            JSONObject jsObj = new JSONObject(payload.toString());
            String status = jsObj.getString("status");
            switch (status) {
                case "CREATED":
                    if (RoomServices.room == null) {
                        RoomServices.procesarMensajeCreacionSala(payload);
                    }
                    break;
                case "CLOSE":
                    //El líder ha cerrado la sala
                    RoomServices.liderCerroSala = true;
                    sala_act_topic_id.unsubscribe();
                    break;
                case "SEARCHING":
                    //Ha iniciado la búsqueda de partida
                    RoomServices.buscandoPartida = true;
                    RoomServices.room.setStatus("SEARCHING");
                    System.out.println("buscando partida");
                    break;
                case "UPDATED_INVITES":
                    //Se ha actualizado la lista de usuarios inviatdos
                    ArrayList<String> invites = new ArrayList<String> ();
                    JSONArray invitesJSArray = jsObj.getJSONArray("invites");
                    for (int i = 0; i < invitesJSArray.length(); ++i) {
                        invites.add(invitesJSArray.getString(i));
                    }
                    RoomServices.room.setInvites(invites);
                    break;
                case "UPDATED_PLAYERS":
                    //Se ha actualizado la lista de jugadores de la sala
                    ArrayList<String> updated_players = new ArrayList<String> ();
                    JSONArray playersJSArray = jsObj.getJSONArray("players");
                    for (int i = 0; i < playersJSArray.length(); ++i) {
                        updated_players.add(playersJSArray.getString(i));
                    }
                    RoomServices.updatePlayers(updated_players);
                    break;
                case "FOUND":
                    RoomServices.room = null;
                    RoomServices.buscandoPartida = false;
                    sala_act_topic_id.unsubscribe();
                    //this.router.navigate(["/board"])
                case "FAILED":
                    RoomServices.room = null;
                    RoomServices.buscandoPartida = false;
                    RoomServices.crearSala();
                    break;
                case "CANCELLED":
                    RoomServices.room.setStatus("CREATED");
                    RoomServices.buscandoPartida = false;
                    break;
                default:
            }
        }
    }
}