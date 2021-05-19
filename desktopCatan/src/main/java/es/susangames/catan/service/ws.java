package es.susangames.catan.service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import es.susangames.catan.controllers.Gameplay;
import es.susangames.catan.controllers.MainMenu;
import es.susangames.catan.controllers.Play;

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
import es.susangames.catan.App;
//import org.graalvm.compiler.lir.LIRInstruction.Use;
import org.json.*;
import javafx.application.Platform;
//import jdk.internal.jshell.tool.resources.version;
import es.susangames.catan.service.RoomServices;


//http://localhost:8080
//https://catan-backend-app.herokuapp.com/
public class ws {
    public static final StompSession session;
    public static Map<String,ArrayList<JSONObject>> msgs;
    private static final String appPrefix = "/app";
    private static final String  wsUrl = "http://localhost:8080/catan-stomp-ws-ep";
    private static final String chatUrl = "/chat/";
    private static final String newFriendReqUrl = "/peticion/";
    private static final String sendFriendRequestUrl = "/app/enviar/peticion";
    private static final String acceptFriendRequestUrl = "/app/aceptar/peticion";
    private static final String declineFriendRequestUrl = "/app/rechazar/peticion";
    private static final String invitationRequestUrl = "/invitacion/";
    private static final String createRoomRequestUrl = "/sala-crear/";
    public static final String salaActRequestUrl = "/sala-act/";
    public  static final String proponerComercio = appPrefix + "/partida/comercio/proponer";
    public  static final String aceptarComercio = appPrefix + "/partida/comercio/aceptar";
    public  static final String rechazarComercio = appPrefix + "/partida/comercio/rechazar";
    public  static final String enviarMensajePartida = appPrefix + "/enviar/partida";

    

    public static final String crearSala = "/app/sala/crear";
    public static final String salaCerrar = "/app/sala/cerrar";
    public static final String salaAbandonar = "/app/sala/abandonar";
    public static final String invitacionEnviar = "/app/invitacion/enviar";
    public static final String invitacionCancelar = "/app/invitacion/cancelar";
    public static final String invitacionAceptar = "/app/invitacion/aceptar";
    public static final String busquedaComenzar = "/app/partida/busqueda/comenzar";
    public static final String busquedaCancelar = "/app/partida/busqueda/cancelar";
    public static final String partidaTestComenzar = "/app/partida/test";
    public static final String partidaRecargar = "/app/partida/recargar";
    public static final String enviarMensajePrivado = "/app/enviar/privado";

    private static Subscription invitacion_topic_id;
    private static Subscription sala_crear_topic_id;
    private static Subscription sala_act_topic_id;
    // Partida
    public static final String partida_act_topic = "/partida-act/";
    public static final String partida_chat_topic = "/partida-chat/";
    public static final String partida_com_topic = "/partida-com/";
    public static final String partida_test_topicUrl = "/test-partida/";
    public static final String  partidaJugada = "/app/partida/jugada";
    




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
            System.out.println("procesarMensajeAccionSala");
            System.out.println(jsObj.toString(4));
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
                case "UPDATED-INVITES":
                    //Se ha actualizado la lista de usuarios inviatdos
                    ArrayList<String> invites = new ArrayList<String> ();
                    JSONArray invitesJSArray = jsObj.getJSONArray("invites");
                    for (int i = 0; i < invitesJSArray.length(); ++i) {
                        invites.add(invitesJSArray.getString(i));
                    }
                    RoomServices.room.setInvites(invites);
                    break;
                case "UPDATED-PLAYERS":
                    //Se ha actualizado la lista de jugadores de la sala
                    JSONArray playersJSArray = jsObj.getJSONArray("players");
                    System.out.println(playersJSArray.toString());
                    ArrayList<String> updated_players = new ArrayList<String> ();
                    for (int i = 0; i < playersJSArray.length(); ++i) {
                        System.out.println("Player: " + playersJSArray.getString(i));
                        updated_players.add(playersJSArray.getString(i));
                    }
                    RoomServices.updatePlayers(updated_players);

                    //MainMenu.loadPlay();
                    if(MainMenu.playOpenned) {
                        Play.recargarSalaPartida();
                    }

                    break;
                case "FOUND":
                    System.out.println("_____FOUND_____");
                    RoomServices.room = null;
                    RoomServices.buscandoPartida = false;
                    sala_act_topic_id.unsubscribe();

                    JSONArray players = jsObj.getJSONArray("players");
                    String stringPlayers[] = new String[players.length()];
                    for (int i = 0; i < players.length(); ++i) {
                        stringPlayers[i] = players.getString(i);
                    }
                    String a[] = new String[4];
                    a[0] = "1"; a[1] = "2"; a[2] = "3"; a[3] = "4";
                    System.out.println(jsObj.getString("game"));
                    System.out.println(stringPlayers);
                    Gameplay.comenzarPartida(jsObj.getString("game"), 
                        stringPlayers);
                    System.out.println("break");
                    break;
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

    /**
     * Cierra la sala en la que se encuentra el jugador (si es que se 
     * encuentra en alguna). Solo se puede cerrar la sala si el usuario
     * es el líder de la sala.
     * Si la sala estaba en estado SEARCHING, entonces cancela la búsqueda
     * y cierra la sala (solo si el usuario es el líder).
     * @param crearSala: indica si se debe tratar de crear una nueva sala o no.
     */
    public static void cerrarSala (Boolean crearSala) {
        if (RoomServices.soyLider()) {
            JSONObject myObject = new JSONObject();
            myObject.put("leader", RoomServices.room.getLeader());
            myObject.put("room", RoomServices.room.getId());
            session.send(salaCerrar, myObject.toString());
            sala_act_topic_id.unsubscribe();
            RoomServices.buscandoPartida = false;
            RoomServices.uniendoseASala = false;
            RoomServices.room = null;
            if ( crearSala ){
                RoomServices.crearSala();
            }
        }
    }

    /**
   * Abandona la sala en la que se encuentra el usuario (si es que se encuentra
   * en alguna). Si el usuario es el líder, entonces cierra la sala. 
   * Si el estado de la sala es SEARCHING, entonces cancela la búsqueda y cierra la 
   * sala conforme a lo especificado anteriormente. 
   * @param crearSala: indica si se debe tratar de crear una nueva sala o no.
   */
    public static void abandonarSala(Boolean crearSala) {
        if ( RoomServices.room != null && !RoomServices.soyLider() ){
            JSONObject myObject = new JSONObject();
            myObject.put("leader", RoomServices.room.getLeader());
            myObject.put("room", RoomServices.room.getId());
            myObject.put("player", UserService.getUsername());
            session.send(salaAbandonar, myObject.toString());
            sala_act_topic_id.unsubscribe();
            RoomServices.buscandoPartida = false;
            RoomServices.uniendoseASala = false;
            RoomServices.room = null;
            if ( crearSala ){
                RoomServices.crearSala();
            }   
        }
    }
    
    public static void sendInvitation (String friend) {
        JSONObject js = new JSONObject();
        js.put("leader", RoomServices.room.getLeader());
        js.put("room", RoomServices.room.getId());
        js.put("invite", friend);

        session.send(invitacionEnviar, js.toString());
    }

    public static void acceptMatchRequest(String friend, String room) {
        JSONObject js = new JSONObject();
        js.put("leader", friend);
        js.put("room", room);
        js.put("invite", UserService.getUsername());

        session.send(invitacionAceptar, js.toString());
        System.out.println("Enviar invitacion aceptar.");
        for (RoomServices.Invite i : RoomServices.invites) {
            if (i.getLeader().equals(friend)) {
                i.aceptarInvitacion();
                break;
            }
        }
        try{
            Thread.sleep(500); // Esperamos a que se procese 
            Platform.runLater(new Runnable() {
                @Override public void run() {
                    MainMenu.getFriends();
                }
              });
        } catch(Exception e){}
        // Entrar a la pantalla play
    }

    public static void declineMatchRequest(String friend, String room) {
        JSONObject js = new JSONObject();
        js.put("leader", friend);
        js.put("room", room);
        js.put("invite", UserService.getUsername());

        session.send(invitacionCancelar, js.toString());
        for (RoomServices.Invite i : RoomServices.invites) {
            if (i.getLeader().equals(friend)) {
                i.cancelarInvitacion();
                break;
            }
        }
        try{
            Thread.sleep(500); // Esperamos a que se procese 
            Platform.runLater(new Runnable() {
                @Override public void run() {
                    // Invitaciones
                    MainMenu.getFriends();
                }
              });
        } catch(Exception e){}
    }
}