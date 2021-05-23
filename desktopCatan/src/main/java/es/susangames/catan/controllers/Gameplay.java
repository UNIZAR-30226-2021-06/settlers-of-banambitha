package es.susangames.catan.controllers;

import es.susangames.catan.service.LangService;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.*;
import es.susangames.catan.App;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.util.Random;

import javax.security.auth.callback.ChoiceCallback;

import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.shape.Polygon;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.control.ToggleButton;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Spinner;
import javafx.event.EventHandler;
import com.jfoenix.controls.JFXTextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Popup;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.scene.control.Label;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.effect.DropShadow;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;
import javafx.scene.input.MouseButton;
import javafx.scene.image.ImageView;
import org.json.*;
import es.susangames.catan.service.UserService;
import java.util.Arrays;
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
import es.susangames.catan.service.ws;
import java.lang.reflect.Type;
import es.susangames.catan.App;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.application.Platform;
import java.util.Timer;
import org.springframework.messaging.simp.stomp.StompSession.Subscription;
import es.susangames.catan.service.RoomServices;


public class Gameplay {
    private static double           start_X_position = 250.0;
    private static double           start_Y_position = 110.0;
    private static double           horizontal_right_gap = 115; 
    private static double           horizontal_left_gap = 60;
    private static double           vertical_gap = 120;
    private static int              numberRoads = 72;
    private static int              settleSize = 12;
    private static int              numberSize = 18;
    private static int              numberSettlements = 54;
    private static int              numberofHexagons = 19;
    private static int              numberPlayers = 4;
    private static int              numberBasicPorts = 4;
    private static Color[]          coloresPorId = new Color[]{Color.BLUE, 
                                                               Color.RED, 
                                                               Color.YELLOW, 
                                                               Color.GREEN};
    private static Boolean          esperandoTableroInicial;

    protected static class MessageKeys {
        protected static String  MENSAJE                         = "Message";
        protected static String  EXIT_STATUS                     = "exit_status";
        protected static String  TAB_INFO                        = "Tab_inf";
        protected static String  TAB_INFO_HEXAGONOS              = "hexagono";
        protected static String  HEXAGONOS_TIPOS                 = "tipo";
        protected static String  HEXAGONOS_VALORES               = "valor";
        protected static String  HEXAGONOS_LADRON                = "ladron";
        protected static String  TAB_INFO_VERTICES               = "vertices";
        protected static String  VERTICES_ASENTAMIENTOS          = "asentamiento";
        protected static String  VERTICES_POSIBLES_ASENTAMIENTOS = "posibles_asentamiento";
        protected static String  TAB_INFO_ARISTAS                = "aristas";
        protected static String  ARISTAS_CAMINOS                 = "camino";
        protected static String  ARISTAS_POSIBLES_CAMINOS        = "posibles_camino";
        protected static String  ARISTAS_PUERTOS                 = "puertos";
        protected static String  RESULTADO_TIRADA                = "Resultado_Tirada";
        protected static String  RECURSOS                        = "Recursos";
        protected static String  CARTAS                          = "Cartas";
        protected static String  PUNTUACIONES                    = "Puntuacion";
        protected static String  TURNO_ACUM                      = "Turno";
        protected static String  TURNO_JUGADOR                   = "Turno_Jugador";
        protected static String  GANADOR                         = "Ganador";
        protected static String  PLAYER_1                        = "Player_1";
        protected static String  PLAYER_2                        = "Player_2";
        protected static String  PLAYER_3                        = "Player_3";
        protected static String  PLAYER_4                        = "Player_4";
        protected static String  PUERTO_MADERA                   = "puertoMadera";
        protected static String  PUERTO_LANA                     = "puertoLana";
        protected static String  PUERTO_MINERAL                  = "puertoMineral";
        protected static String  PUERTO_CEREAL                   = "puertoCereales";
        protected static String  PUERTO_ARCILLA                  = "puertoArcilla";
        protected static String  PUERTOS_BASICOS                 = "puertosBasicos";
        protected static String  CLOCK                           = "Clock";
        protected static String  PRIMEROS_CAMINOS                = "primerosCaminos";
        protected static String  PRIMEROS_ASENTAMIENTOS          = "primerosAsentamiento";
        protected static String  PLAYER_NAMES                    = "playerNames";
    }
    

    protected static class MsgComercioStatus {
        protected static String REQUEST = "REQUEST";
        protected static String ACCEPT  = "ACCEPT";
        protected static String DECLINE = "DECLINE";
    }

    protected static class  MsgReporteStatus {
        protected static String REPORT_SENT = "REPORT_SENT"; 
        protected static String REPORT_REJECTED = "REPORT_REJECTED";
        protected static String REPORT_RECEIVED = "REPORT_RECEIVED";
}


    protected static class Jugada {
        protected static String CONSTRUIR_POBLADO   = "construir poblado";
        protected static String MEJORAR_POBLADO     = "mejorar poblado";
        protected static String CONSTRUIR_CAMINO    = "construir camino"; 
        protected static String MOVER_LADRON        = "mover ladron";
        protected static String PASAR_TURNO         = "finalizar turno";
        protected static String PRIMER_ASENTAMIENTO = "primer asentamiento"; 
        protected static String PRIMER_CAMINO       = "primer camino";
        protected static String COMERCIAR           = "comerciar";
        protected static String COMERCIAR_PUERTO    = "comerciar con puerto";
    }

    protected static class Colores {
        protected static String AMARILLO = "Amarillo";
        protected static String ROJO     = "Rojo";
        protected static String VERDE    = "Verde"; 
        protected static String AZUL     = "Azul";
    }


    protected static class TipoAsentamiento {
        protected static String NADA             = "Nada";
        protected static String POBLADO_PLAYER_1 = "PuebloAzul";
        protected static String POBLADO_PLAYER_2 = "PuebloRojo"; 
        protected static String POBLADO_PLAYER_3 = "PuebloAmarillo"; 
        protected static String POBLADO_PLAYER_4 = "PuebloVerde";
        protected static String CIUDAD_PLAYER_1  = "CiudadAzul";
        protected static String CIUDAD_PLAYER_2  = "CiudadRojo"; 
        protected static String CIUDAD_PLAYER_3  = "CiudadAmarillo"; 
        protected static String CIUDAD_PLAYER_4  = "CiudadVerde"; 
    }

    protected static class TipoTerreno {
        protected static String BOSQUE   = "Bosque";
        protected static String PASTO    = "Pasto";
        protected static String SEMBRADO = "Sembrado"; 
        protected static String CERRO    = "Cerro";
        protected static String MONTANYA = "Montanya"; 
        protected static String DESIERTO = "Desierto"; 
        protected static String VACIO    = "Vacio";
    }

    protected static class TipoCamino {
        protected static String NADA     = "Nada";
        protected static String  PLAYER_1 = "CaminoAzul";
        protected static String  PLAYER_2 = "CaminoRojo"; 
        protected static String  PLAYER_3 = "CaminoAmarillo";
        protected static String  PLAYER_4 = "CaminoVerde";
    }

    protected class MsgJugada {
        protected  Integer player;
        protected  String game;
        protected  String name;
        protected  Object param;
    }

    protected  class CartasJugador {
        protected  Integer D1; 
        protected  Integer D2;
        protected  Integer D3; 
        protected  Integer D4; 
        protected  Integer D5; 
        protected  Integer E1; 
        protected  Integer E2; 
    }

    protected  class RecursosJugador {
        protected  Integer madera;
        protected  Integer mineral;
        protected  Integer arcilla;
        protected  Integer lana;
        protected  Integer cereales;
    }
    

    protected class Jugador {
        protected String nombre;
        protected Integer turno;
        protected Color color;
        protected Integer puntos;
        protected CartasJugador cartas;
        protected RecursosJugador recursos;
        protected Boolean primerosAsentamientos;
        protected Boolean primerosCaminos;
    }

    protected class Mensaje {
        protected Boolean esError;
        protected Jugador remitente;
        protected String body;
        protected String timeStamp;
    }

    protected static class Hexagonos {
        protected static String[]    tipo;
        protected static Integer[]        valor;            
        protected static Integer          ladron;
        protected static Polygon[]        hexagons;         
        private   static Button[]         numberOverHexagon; 
    }

    protected static class Vertices {
        protected static Button[]    settlements;
        protected static Color[]     colorSettlement;
        protected static Boolean[] posible_asentamiento;
        protected static String[]    settlementsType;
    } 
    
    protected static class Puerto {
        protected  static Integer madera;
        protected  static Integer mineral;
        protected  static Integer arcilla;
        protected  static Integer lana;
        protected  static Integer cereal;
        protected  static Integer[] basico;
    }

    protected static class Aristas {
        protected static ToggleButton[] roads;
        protected static Color[]     colorRoads;
        protected static String[] roadsType;
        protected static Boolean[] posible_camino;
        protected static Puerto puertos;
    }

    protected static class Tablero {
        protected static Hexagonos hexagonos;
        protected static Vertices vertices;
        protected static Aristas aristas;
    } 

    protected static class Partida {
        protected static String id = null;
        protected static Integer miTurno;
        protected static Integer totalTurnos;
        protected static Integer turnoActual;
        protected static Integer resultadoTirada;
        protected static Jugador[] jugadores;
        protected static Mensaje[] mensajes;
        protected static Integer clock;
        protected static Boolean PobladoDisponible;
        protected static Boolean CaminoDisponible;
        protected static Boolean CiudadDisponible;
        protected static Boolean movioLadron;
        protected static Boolean yaComercio;
        protected static Tablero tablero;
    }

    /**
     * Solicitud de comercio
     */
    protected static class SolicitudComercio {
        protected static Integer from;
        protected static ProductoComercio res1;
        protected static ProductoComercio res2;
        protected static String timeStamp;
    }


    protected class ProductoComercio {
        protected String recurso; 
        protected Integer cuan;
    }

    private Image imgSea, imgDes, imgMou, imgFie, imgFor, imgHil; 
 
    @FXML
    private AnchorPane mainAnchor;

    private static AnchorPane _mainAnchor;

    @FXML
    private JFXTextArea chatContent;

    private static JFXTextArea _chatContent;

    @FXML
    private TextField chatInput;

    @FXML
    private Button cards;

    @FXML
    private Button inTrade;    

    @FXML
    private Text player1Name;

    private static Text _player1Name;

    @FXML
    private Text player2Name;

    private static Text _player2Name;

    @FXML
    private Text player3Name;

    private static Text _player3Name;

    @FXML
    private Text player4Name;

    private static Text _player4Name;

    @FXML
    private Button diceButton;

    private static Button _diceButton;

    @FXML
    private Button settingsButton;

    @FXML
    private Button passTurnButton;

    private static Button _passTurnButton;

    @FXML
    private Text lanaCant;

    private static Text _lanaCant;

    @FXML
    private Text arcillaCant;

    private static Text _arcillaCant;

    @FXML
    private Text cerealesCant;

    private static Text _cerealesCant;

    @FXML
    private Text mineralCant;

    private static Text _mineralCant;

    @FXML
    private Text maderaCant;

    private static Text _maderaCant;

    @FXML
    private Button turnPlayer;

    private static Button _turnPlayer;

    private static Text _knight_card;
    
    @FXML
    private Button reportPLayer1;

    @FXML
    private Button reportPLayer2;

    @FXML
    private Button reportPLayer3;

    @FXML
    private Button reportPLayer4;


    // Elementos graficos adicionales
    private static Popup popupCards;
    private Popup popupInternalTrade;
    private Popup popupExternalTrade;
    private Popup popupSettings;
    private Popup popupBuildSettle;
    private Popup popupResources;
    private static Popup popupNewTradeOffer;

    private ChoiceBox<String> offerMaterial;
    private ChoiceBox<String> offerPlayer;
    private ChoiceBox<String> offerPlayerResources;
    private ChoiceBox<String> receiveMaterial;
    private ChoiceBox<String> receivePlayer;
    private ChoiceBox<String> ratio;
    private Spinner<Integer>  spinnerReceive;
    private Spinner<Integer> spinnerGive;
    private Button sendTrade;
    private Button sendTradeExternal;
    private Button leaveGame;
    private Button stopGame;
    private Integer offerAmountInt;
    private Integer receiveAmountInt;
    private String player1,player2,player3,player4;
    

    // Variables controlador partida
    private static Boolean cargandoPartida = false;
    private static String idPartida;
    private static Integer posRoad;
    private static Integer posSettle;
    private static Integer idPuerto;

    // Subscriptions
    private static Subscription partida_act_topic_id;
    private static Subscription partida_chat_topic_id;
    private static Subscription partida_com_topic_id;
    private static Subscription partida_reload_topic_id;
    private static Subscription partida_usuaio_act_topic_id;
    
    
    public Gameplay() {
        offerAmountInt = 1;
        receiveAmountInt = 1;
    }
    
    public static void comenzarPartidaPrueba(String msg) {
        JSONObject message = new JSONObject(msg);
        String aux = message.getString("game");
        comenzarPartida(aux, new String[] {UserService.getUsername(),
                                                "Jugador_2",
                                                "Jugador_3",
                                                "Jugador_4"});

    }

    public static void reanudarPartida (String idPartida) {
        
        esperandoTableroInicial = true;
        Partida.miTurno = 1;
        Partida.id = idPartida;
        String jugadores[] = new String[4];
        jugadores[0] = ""; jugadores[1] = ""; jugadores[2] = ""; jugadores[3] = "";
        Partida.jugadores = inicializarJugadores(jugadores);
        Partida.tablero = new Tablero();
        initTablero();

        Partida.clock = -1;
        Partida.turnoActual = 0;
        Partida.totalTurnos = 0;
        Partida.resultadoTirada = 0;
        Partida.PobladoDisponible = false;
        Partida.CiudadDisponible = false;
        Partida.CaminoDisponible = false;
        Partida.movioLadron = false;
        Partida.yaComercio = false;

        SolicitudComercio.res1 = new Gameplay().new ProductoComercio();
        SolicitudComercio.res2 = new Gameplay().new ProductoComercio();

        partida_reload_topic_id = 
            ws.session.subscribe( ws.partida_reload_topic  + UserService.getUsername(), new StompFrameHandler() {
                @Override
                public Type getPayloadType(StompHeaders headers) {
                    return String.class;
                }
                @Override
                public void handleFrame(StompHeaders headers, Object payload) {
                    procesarMensajeRecarga (payload.toString());
                }
            });

        JSONObject jsObj = new JSONObject();
        jsObj.put("player", UserService.getUsername());
        jsObj.put("game", idPartida);
        jsObj.put("reload", true);

        System.out.println(jsObj.toString(4));
        
        ws.session.send(ws.partidaRecargar, jsObj.toString());

        cargandoPartida = true;
    }

    private static void procesarMensajeRecarga (String mensaje) {
        System.out.println("Ahora la partida es: " + Partida.id);
        JSONObject obj = new JSONObject(mensaje);

        JSONArray playerNames = obj.getJSONArray(MessageKeys.PLAYER_NAMES);
        System.out.println(playerNames.toString());
        int miTurno = 0;
        for (int i = 0; i < playerNames.length(); ++i) {
            if (playerNames.getString(i).equals(UserService.getUsername())) {
                miTurno = i;
            }
        }

        if (  miTurno >= 0 ){
            System.out.println("comenzando partida");
            Partida.miTurno = miTurno + 1;
            Partida.clock = -1;
      
            subscribeToTopics();
            procesarMensaje(mensaje);
            System.out.println("la partida: " + Partida.id);
      
          } else{      
            System.out.println("no estabas en esa partida!");
        }
        
    }

    public static Boolean comenzarPartida(String idPartida, String[] jugadores) {
        System.out.println("HEY");
        Integer miTurno  = Arrays.asList(jugadores).indexOf(UserService.getUsername());
       
        if (jugadores.length == numberPlayers && miTurno >= 0) {
            System.out.println("Entra en el if");
            esperandoTableroInicial = true;
            Partida.miTurno = miTurno++;
            Partida.id = idPartida;
            Partida.jugadores = inicializarJugadores(jugadores);
            Partida.tablero = new Tablero();
            initTablero();
            System.out.println("Se ha iniciado el tablero");
            Partida.clock = -1;
            Partida.turnoActual = 0;
            Partida.totalTurnos = 0;
            Partida.resultadoTirada = 0;
            Partida.PobladoDisponible = false;
            Partida.CiudadDisponible = false;
            Partida.CaminoDisponible = false;
            Partida.movioLadron = false;
            Partida.yaComercio = false;
            SolicitudComercio.res1 = new Gameplay().new ProductoComercio();
            SolicitudComercio.res2 = new Gameplay().new ProductoComercio();

            subscribeToTopics();
            System.out.println("Subscripcion realizada.");
            return true;
        }    
        return false;
    }

    private static Jugador[] inicializarJugadores (String[] jugadores) {
        Jugador[] jugadoresAux = new Jugador[numberPlayers];
        for(int i = 0; i < jugadores.length; i++) {
            Jugador aux = new Gameplay().new Jugador();
            aux.nombre = jugadores[i];
            aux.turno = i + 1;
            aux.color = coloresPorId[i];
            aux.puntos = 0;
            aux.recursos = recursosIniciales();
            aux.cartas = cartasIniciales();
            aux.primerosAsentamientos = false;
            aux.primerosCaminos = false; 
            jugadoresAux[i] = aux;
        } 
        return jugadoresAux;
    }

    private static RecursosJugador recursosIniciales() {
        RecursosJugador recursosAux = new Gameplay().new RecursosJugador();
        recursosAux.madera = 0; 
        recursosAux.arcilla = 0;
        recursosAux.cereales = 0; 
        recursosAux.lana = 0;
        recursosAux.mineral = 0;
        return recursosAux;
    }

    private static CartasJugador cartasIniciales() {
        CartasJugador cartasAux = new Gameplay().new CartasJugador();
        cartasAux.D1 = 0; 
        cartasAux.D2 = 0;
        cartasAux.D3 = 0;
        cartasAux.D4 = 0;
        cartasAux.D5 = 0;
        return cartasAux;
    }

    private static void subscribeToTopics() {
        System.out.println("subscribeToTopics() " + Partida.id);
        if(Partida.id != null) {
            //Suscripción a los mensajes de la partida (acciones de los jugadores)
            partida_act_topic_id = 
            ws.session.subscribe( ws.partida_act_topic  + Partida.id, new StompFrameHandler() {
                @Override
                public Type getPayloadType(StompHeaders headers) {
                    return String.class;
                }
                @Override
                public void handleFrame(StompHeaders headers, Object payload) {
                    procesarMensaje(payload.toString());
                }
            });
            //Suscripción al chat de la partida
            partida_chat_topic_id = 
            ws.session.subscribe( ws.partida_chat_topic  + Partida.id, new StompFrameHandler() {
                @Override
                public Type getPayloadType(StompHeaders headers) {
                    return String.class;
                }
                @Override
                public void handleFrame(StompHeaders headers, Object payload) {
                    procesarMnesajeChat(payload.toString());
                }
            });

            //Suscripción a las peticiones de comercio
            partida_com_topic_id = 
            ws.session.subscribe( ws.partida_com_topic  + Partida.id + "/" + 
                                Partida.miTurno, new StompFrameHandler() {
                @Override
                public Type getPayloadType(StompHeaders headers) {
                    return String.class;
                }
                @Override
                public void handleFrame(StompHeaders headers, Object payload) {
                    procesarMensajeComercio(payload.toString());
                }
            });

            partida_usuaio_act_topic_id = 
            ws.session.subscribe( ws.usuario_act  + UserService.getUsername(),  
                                new StompFrameHandler() {
                @Override
                public Type getPayloadType(StompHeaders headers) {
                    return String.class;
                }
                @Override
                public void handleFrame(StompHeaders headers, Object payload) {
                    procesarMensajeReporte(payload.toString());
                }
            });

        }
        
    }

    private static void unsubscribeToTopic () {
        if (Partida.id != null) {
            System.out.println("1-11111");
            partida_act_topic_id.unsubscribe();
            System.out.println("1-22222");
            partida_chat_topic_id.unsubscribe();
            System.out.println("1-33333");
            partida_com_topic_id.unsubscribe();
            System.out.println("1-44444");
            if(partida_reload_topic_id != null) {
                partida_reload_topic_id.unsubscribe();
                System.out.println("1-555555");
            }
            partida_usuaio_act_topic_id.unsubscribe();
            System.out.println("1-666666");
        }
    }

    private static void procesarMensajeReporte(String mensaje) {
        JSONObject object = new JSONObject(mensaje);
        try {
            if(object.getString("status").equals(MsgReporteStatus.REPORT_RECEIVED)) {
                _chatContent.appendText("¡" + object.getString("player") + 
                                        " te ha reportado!" + "\n");

            } else if (object.getString("status").equals(MsgReporteStatus.REPORT_REJECTED)) {
                _chatContent.appendText("¡No puedes reportar a " + object.getString("player") + 
                                        " mas de una vez por partida!" + "\n");

            } else if (object.getString("status").equals(MsgReporteStatus.REPORT_SENT)) {
                _chatContent.appendText("¡Has reportado a " + object.getString("player") + 
                                        "!" + "\n");
            }

        } catch (Exception e) {
            System.out.println("Error procesando mensaje reporte");
        }
    }


    private static void procesarMnesajeChat(String mensaje) {
        JSONObject object = new JSONObject(mensaje);
        try {
            Integer user = object.getInt("from");
            String body = object.getString("body");
            switch (user) {
                case 1:
                _chatContent.appendText( Partida.jugadores[0].nombre + ": " +
                                         body + "\n");
                    break;
                case 2:
                _chatContent.appendText( Partida.jugadores[1].nombre + ": " +
                                         body + "\n");
                    break;
                case 3:
                _chatContent.appendText( Partida.jugadores[2].nombre + ": " + 
                                         body + "\n");
                    break;
                case 4:
                _chatContent.appendText( Partida.jugadores[3].nombre + ": " + 
                                         body + "\n");
                    break;
                default:
                    System.out.println("Jugador erroneo");
                    break;
            }

        } catch (Exception e) {
            System.out.println("Mensaje chat erroneo");
        }
    }


    // Actualizacion del tablero
    private static void initTablero() {
        // Hexagonos
        Partida.tablero.hexagonos.tipo = new String[numberofHexagons];
        Partida.tablero.hexagonos.valor = new Integer[numberofHexagons];
        Partida.tablero.hexagonos.hexagons = new Polygon[numberofHexagons];
        Partida.tablero.hexagonos.numberOverHexagon = new Button[numberofHexagons];
        // Vertices
        Partida.tablero.vertices.settlements = new Button[numberSettlements];
        Partida.tablero.vertices.posible_asentamiento = 
                                new Boolean[numberSettlements];
        Partida.tablero.vertices.colorSettlement = 
                                new Color[numberSettlements];
        Partida.tablero.vertices.settlementsType =
                                new String[numberSettlements];

        // Aristas
        Partida.tablero.aristas.roads = new ToggleButton[numberRoads];
        Partida.tablero.aristas.posible_camino = 
                                new Boolean[numberRoads]; 
        Puerto puerto = new Puerto();
        puerto.basico = new Integer[numberBasicPorts]; 
        Partida.tablero.aristas.puertos = puerto;
        Partida.tablero.aristas.colorRoads = 
                                new Color[numberRoads];
        Partida.tablero.aristas.roadsType =
                                new String[numberRoads];
    }

    private static void procesarMensaje(String mensaje) {
        try {
            actualizarPartida(mensaje);
        } catch (Exception e) {
            System.err.println("No se pudo procesar el mensaje");
            System.err.println("Exception e: " + e.toString());
        }
        if(esperandoTableroInicial) {
            esperandoTableroInicial = false;
            try {
                App.nuevaPantalla("/view/gameplay.fxml");
            } catch(Exception e) {
                System.err.println("Exception e: " + e.toString());
                System.err.println("Exception e: " + e.getMessage());
                e.printStackTrace();
            }
        }
        mostrarCambiosTablero();
        if(existeGanador(mensaje)) { 
            // TODO: Fin partida
            // Desubscripcion peticiones partida.
            unsubscribeToTopic();
            RoomServices.crearSala();
            try {
                JSONObject object = UserService.getUserInfo(UserService.getUsername());
                UserService.fillData(object);
                App.nuevaPantalla("/view/mainMenu.fxml");
            } catch (IOException e) {
                System.err.println("Exception e: " + e.toString());
            }
        }    
    }

    private static Boolean existeGanador(String mensaje) {
        JSONObject object = new JSONObject(mensaje);
        Integer ganador = object.getInt(MessageKeys.GANADOR);
        return ganador > 0;
    }


    private static void procesarMensajeComercio(String mensajeComercio) {
        JSONObject object = new JSONObject(mensajeComercio);
        System.out.println(object.toString(4));
        try {
            String type = object.get("type").toString();
            System.out.println("Hola");
            if(type.equals(MsgComercioStatus.REQUEST)) {
                System.out.println("Mensaje Request");
                if(Partida.turnoActual != Partida.miTurno) {
                    System.out.println("No es mi turno");
                    SolicitudComercio.from = object.getInt("from");
                    String _res1 = object.get("res1").toString();
                    String _res2 = object.get("res2").toString();
                    JSONObject res1_object = new JSONObject(_res1);
                    JSONObject res2_object = new JSONObject(_res2);
                    SolicitudComercio.res1.cuan = res1_object.getInt("cuan");
                    SolicitudComercio.res1.recurso = res1_object.getString("type");
                    SolicitudComercio.res2.cuan = res2_object.getInt("cuan");
                    SolicitudComercio.res2.recurso = res2_object.getString("type");
                    System.out.println(res1_object);
                    System.out.println(res2_object);
                    try {
                        System.out.println("tty");
                        //if (!popupNewTradeOffer.isShowing()) {
                            System.out.println("1dsd");
                            newTradePopUp();
                            System.out.println("2dsfgd");
                            Stage stage = (Stage) _mainAnchor.getScene().getWindow();
                            System.out.println("3dfd");
                            try{
                                Platform.runLater(new Runnable() {
                                    @Override public void run() {
                                        popupNewTradeOffer.show(stage);
                                    }
                                  });
                            } catch(Exception e){
                                System.out.println(e.toString());
                                e.printStackTrace();
                            }
                            System.out.println("4sdfa");
                        //}
                    } catch(Exception e) {
                        System.out.println("Fallo cargando pop up tradeo recibido");
                        System.out.println(e.toString());
                        e.printStackTrace();
                    }
                    
                }
            } else if(type.equals(MsgComercioStatus.ACCEPT)) {
                Integer turnoJugador = object.getInt("from");
                _chatContent.appendText("!" + 
                             Partida.jugadores[turnoJugador-1].nombre + 
                             " ha ACEPTADO tu solicitud de comercio!" +  "\n");
            } else if(type.equals(MsgComercioStatus.DECLINE)) {
                Integer turnoJugador = object.getInt("from");
                _chatContent.appendText("!" + 
                             Partida.jugadores[turnoJugador-1].nombre + 
                             " ha RECHAZADO tu solicitud de comercio!" +  "\n");
            } else {
                System.out.println("No era de ningun tipo");
            }

        } catch(Exception e) {
            System.out.println("Error al procesar comercio");
        }
    }


    private static void actualizarPartida(String partida) throws Exception {
        JSONObject object = new JSONObject(partida);
        System.out.println(object.toString(4));

        if(!object.get(MessageKeys.CLOCK).equals(null) &&
            object.getInt(MessageKeys.CLOCK) > Partida.clock) {

            Partida.clock =  object.getInt(MessageKeys.CLOCK);
            if(!object.isNull(MessageKeys.EXIT_STATUS) 
                && (object.getInt(MessageKeys.EXIT_STATUS) <= 0 || esperandoTableroInicial)) {
                // TODO: Enviar mensaje al chat de la partida
                System.out.println("MessageKeys.EXIT_STATUS <= 0");

                // Nombre y miTurno
                try {
                    if(object.has(MessageKeys.PLAYER_NAMES)) {
                        JSONArray nombresJugadores = object.getJSONArray(MessageKeys.PLAYER_NAMES);
                        for (int i = 0; i < nombresJugadores.length(); i++) {
                            Partida.jugadores[i].nombre = nombresJugadores.getString(i);
                            if(nombresJugadores.getString(i).equals(UserService.getUsername())) {
                                Partida.miTurno = i + 1;
                            }
                        }   
                    }
                } catch(Exception e) {
                    System.err.println("Faltan los nombres de los jugadores");
                    System.err.println(e.toString());
                }
                 
                // Turno jugador
                try {
                    if(!object.get(MessageKeys.TURNO_JUGADOR).equals(null)) {
                        int turnoActual = object.getInt(MessageKeys.TURNO_JUGADOR);
                        Partida.turnoActual = turnoActual + 1 ;
                    } 
                } catch(Exception e) {
                    System.err.println("Falta el turno de la jugada");
                    System.err.println(e.toString());
                }
               
                // Turno acumulado
                try {
                    if(!object.get(MessageKeys.TURNO_ACUM).equals(null)) {
                        Partida.totalTurnos = object.getInt(MessageKeys.TURNO_ACUM);
                    }
                } catch(Exception e) {
                    System.err.println("Faltan los turnos totales");
                    System.err.println(e.toString());
                }
                 
                //Resultado tirada
                try  {
                    if(!object.get(MessageKeys.RESULTADO_TIRADA).equals(null)) {
                        Partida.resultadoTirada = object.getInt(MessageKeys.RESULTADO_TIRADA);
                    }
                } catch(Exception e) {
                    System.err.println("Falta el resultado de la tirada");
                    System.err.println(e.toString());
                }
            
                //Tablero
                try {
                    if(!object.get(MessageKeys.TAB_INFO).equals(null)) {
                        actualizarTablero(
                            new JSONObject(object.get(MessageKeys.TAB_INFO).toString()));
                    }
                } catch(Exception e) {
                    System.err.println("Falta la información del tablero");
                    System.err.println(e.toString());
                }   
                 
                // TODO: Falta terminar y editar parte visual en el tablero
                actualizarJugadores(new JSONObject(partida));

                //Primeros Asentamientos
                try {
                    if(!object.get(MessageKeys.PRIMEROS_ASENTAMIENTOS).equals(null)) {
                        actualizarPrimerosAsentamientos(
                            object.get(MessageKeys.PRIMEROS_ASENTAMIENTOS).toString()
                        );
                    }
                } catch(Exception e) {
                    System.err.println("Faltan los primeros asentamientos");
                    System.err.println(e.toString());
                }
                // Primeros caminos
                try {
                    if(!object.get(MessageKeys.PRIMEROS_CAMINOS).equals(null)) {
                        actualizarPrimerosCaminos(
                            object.get(MessageKeys.PRIMEROS_CAMINOS).toString()
                        );
                    }
                } catch(Exception e) {
                    System.err.println("Faltan los primeros caminos");
                    System.err.println(e.toString());
                }

                //Pre-calculos
                try {
                    actualizarPrecalculos();
                } catch(Exception e) {
                    System.err.println("Error precalculos");
                    System.err.println(e.toString());
                }
                

            } else {
                // TODO: Recibido codigo erroneo
                _chatContent.appendText(object.getString(
                            MessageKeys.MENSAJE) + "\n");
                if(object.getString(MessageKeys.MENSAJE).contains("ladron")) {
                    Partida.movioLadron = false;
                }
            }
        }

    }

    private static void actualizarJugadores(JSONObject partida) {
        try  {
            if(!partida.get(MessageKeys.RECURSOS).equals(null)) {
                actualizarRecursosJugadores(
                    partida.get(MessageKeys.RECURSOS).toString());
            }
        } catch(Exception e) {
            System.err.println("Faltan los recursos en el mensaje");
            System.err.println("Exception e: " + e.toString());
        }
        
        try {
            if(!partida.get(MessageKeys.CARTAS).equals(null)) {
                //TODO: Implementar
                actualizarCartasJugadores(
                    new JSONObject (partida.get(MessageKeys.CARTAS)));
            } 
        } catch (Exception e) {
            System.err.println("Faltan las cartas en el mensaje");
        }
         
        try {
            if(!partida.get(MessageKeys.PUNTUACIONES).equals(null)) {
                // TODO: Implementar
                actualizarPuntuacionesJugadores(
                    partida.get(MessageKeys.PUNTUACIONES).toString());
            } 
        } catch (Exception e) {
            System.err.println("Faltan las puntuaciones en el mensaje");
        }
    }

    private static void actualizarRecursosJugadores(String _recursos) {
        JSONObject recursos = new JSONObject(_recursos);
        try {
            if(!recursos.get(MessageKeys.PLAYER_1).equals(null)) {
                actualizarRecursoJugador(
                    recursos.get(MessageKeys.PLAYER_1).toString(),0);
            }
            if(!recursos.get(MessageKeys.PLAYER_2).equals(null)) {
                actualizarRecursoJugador(
                    recursos.get(MessageKeys.PLAYER_2).toString(),1);
            }
            if(!recursos.get(MessageKeys.PLAYER_3).equals(null)) {
                actualizarRecursoJugador(
                    recursos.get(MessageKeys.PLAYER_3).toString(),2);
            }
            if(!recursos.get(MessageKeys.PLAYER_4).equals(null)) {
                actualizarRecursoJugador(
                    recursos.get(MessageKeys.PLAYER_4).toString(),3);
            }

        } catch (Exception e) {
            System.err.println("Error procesando recursos");
        }
    }

    private static void actualizarRecursoJugador(String recursos, int player) {
        JSONArray recursosArr = new JSONArray(recursos);
        Partida.jugadores[player].recursos.madera = recursosArr.getInt(0);
        Partida.jugadores[player].recursos.lana = recursosArr.getInt(1);
        Partida.jugadores[player].recursos.cereales = recursosArr.getInt(2);
        Partida.jugadores[player].recursos.arcilla = recursosArr.getInt(3);
        Partida.jugadores[player].recursos.mineral = recursosArr.getInt(4);
    }


    private static void actualizarCartasJugadores(JSONObject cartas) {
        
    }

    private static void actualizarPuntuacionesJugadores(String _puntuaciones) {
        try {
            JSONObject puntuaciones = new JSONObject(_puntuaciones);
            Partida.jugadores[0].puntos = puntuaciones.getInt(MessageKeys.PLAYER_1);
            Partida.jugadores[1].puntos = puntuaciones.getInt(MessageKeys.PLAYER_2);
            Partida.jugadores[2].puntos = puntuaciones.getInt(MessageKeys.PLAYER_3);
            Partida.jugadores[3].puntos = puntuaciones.getInt(MessageKeys.PLAYER_4);
        } catch (Exception e) {
            System.err.println("Error procesando puntuaciones");
        }
    }

    private static void actualizarPrimerosAsentamientos(String asentamientos) {
        try {
            JSONObject primerosAsentamientos = new JSONObject(asentamientos);
            if(!primerosAsentamientos.get(MessageKeys.PLAYER_1).equals(null)) {
                Partida.jugadores[0].primerosAsentamientos = 
                        primerosAsentamientos.getBoolean(MessageKeys.PLAYER_1);
            }
            if(!primerosAsentamientos.get(MessageKeys.PLAYER_2).equals(null)) {
                Partida.jugadores[1].primerosAsentamientos = 
                        primerosAsentamientos.getBoolean(MessageKeys.PLAYER_2);
            }
            if(!primerosAsentamientos.get(MessageKeys.PLAYER_3).equals(null)) {
                Partida.jugadores[2].primerosAsentamientos = 
                        primerosAsentamientos.getBoolean(MessageKeys.PLAYER_3);
            }
            if(!primerosAsentamientos.get(MessageKeys.PLAYER_4).equals(null)) {
                Partida.jugadores[3].primerosAsentamientos = 
                        primerosAsentamientos.getBoolean(MessageKeys.PLAYER_4);
            }
        } catch (Exception e) {
            System.err.println("Error procesando primeros asentamientos");
        }
    }

    private static void actualizarPrimerosCaminos(String caminos) {
        try {
            JSONObject primerosCaminos = new JSONObject(caminos);
            if(!primerosCaminos.get(MessageKeys.PLAYER_1).equals(null)) {
                Partida.jugadores[0].primerosCaminos = 
                        primerosCaminos.getBoolean(MessageKeys.PLAYER_1);
            }
            if(!primerosCaminos.get(MessageKeys.PLAYER_2).equals(null)) {
                Partida.jugadores[1].primerosCaminos = 
                        primerosCaminos.getBoolean(MessageKeys.PLAYER_2);
            }
            if(!primerosCaminos.get(MessageKeys.PLAYER_3).equals(null)) {
                Partida.jugadores[2].primerosCaminos = 
                        primerosCaminos.getBoolean(MessageKeys.PLAYER_3);
            }
            if(!primerosCaminos.get(MessageKeys.PLAYER_4).equals(null)) {
                Partida.jugadores[3].primerosCaminos = 
                        primerosCaminos.getBoolean(MessageKeys.PLAYER_4);
            }
        } catch (Exception e) {
            System.err.println("Error procesando primeros caminos");
        }
    }



    private static void actualizarTablero(JSONObject tablero) {
        //System.out.println(tablero);
        // Hexagonos
        try {
            if(!tablero.get(MessageKeys.TAB_INFO_HEXAGONOS).equals(null)) {
                actualizarHexagonos(tablero.get(MessageKeys.TAB_INFO_HEXAGONOS).toString());
            }
        } catch (Exception e) {
            System.err.println("Faltan los hexágonos en el mensaje");
        }
        
        // Vertices
        try {
            if(!tablero.get(MessageKeys.TAB_INFO_VERTICES).equals(null)) {
                actualizarVertices(tablero.get(MessageKeys.TAB_INFO_VERTICES).toString());
            }
        } catch (Exception e) {
            System.err.println("Faltan los vértices en el mensaje");
            System.err.println(e.toString());
        }
       
        // Aristas
        // TODO: Actualizar puertos
        try {
            if(!tablero.get(MessageKeys.TAB_INFO_ARISTAS).equals(null)) {
                actualizarAristas(tablero.get(MessageKeys.TAB_INFO_ARISTAS).toString());
            } 
        } catch (Exception e) {
            System.err.println("Faltan las aristas en el mensaje");
            System.err.println(e.toString());
        }      
    }

    private static void actualizarVertices(String vertices) {
        JSONObject verticesOb = new JSONObject(vertices);
        JSONArray verticesArr = verticesOb.getJSONArray(MessageKeys.VERTICES_ASENTAMIENTOS);
        for (int i = 0; i < verticesArr.length(); i++) {
          modificarAsentamiento(verticesArr.getString(i), i);
        } 
        // Posibles asentamientos
        String verticesPosibles = verticesOb.get(
            MessageKeys.VERTICES_POSIBLES_ASENTAMIENTOS).toString();
        JSONArray verticesPosArr = new JSONArray(verticesPosibles);
        String aux = verticesPosArr.getJSONArray(Partida.miTurno - 1).toString();
        JSONArray posiblesMijugadorArr = new JSONArray(aux);    
        for(int i = 0; i < posiblesMijugadorArr.length(); i++) {
            Partida.tablero.vertices.posible_asentamiento[i] = 
                                            posiblesMijugadorArr.getBoolean(i);  
        }

    }

    private static void actualizarAristas(String aristas) {
        JSONObject aristasOb = new JSONObject(aristas);
        JSONArray aristasArr = aristasOb.getJSONArray(MessageKeys.ARISTAS_CAMINOS);
        for (int i = 0; i < aristasArr.length(); i++) {
            modificarCarretera(aristasArr.getString(i), i);
        } 
        // Posibles caminos
        String aristasPosibles = aristasOb.get(
            MessageKeys.ARISTAS_POSIBLES_CAMINOS).toString();
        JSONArray aristasPosArr = new JSONArray(aristasPosibles);
        String aux = aristasPosArr.getJSONArray(Partida.miTurno - 1).toString();
        JSONArray posiblesMijugadorArr = new JSONArray(aux);    
        for(int i = 0; i < posiblesMijugadorArr.length(); i++) {
            Partida.tablero.aristas.posible_camino[i] = posiblesMijugadorArr.getBoolean(i);  
        }
        // Puertos
        String puertos = aristasOb.get(MessageKeys.ARISTAS_PUERTOS).toString();
        JSONObject puertosOb = new JSONObject(puertos);
        Partida.tablero.aristas.puertos.arcilla = puertosOb.getInt(
                                                    MessageKeys.PUERTO_ARCILLA);
        Partida.tablero.aristas.puertos.madera = puertosOb.getInt(
                                                    MessageKeys.PUERTO_MADERA);
        Partida.tablero.aristas.puertos.mineral = puertosOb.getInt(
                                                    MessageKeys.PUERTO_MINERAL);
        Partida.tablero.aristas.puertos.lana = puertosOb.getInt(
                                                    MessageKeys.PUERTO_LANA);
        Partida.tablero.aristas.puertos.cereal = puertosOb.getInt(
                                                    MessageKeys.PUERTO_CEREAL);
        
        JSONArray puertosBasicos = puertosOb.getJSONArray(
                                                MessageKeys.PUERTOS_BASICOS);
        for(int i = 0; i<numberBasicPorts; i++) {
            Partida.tablero.aristas.puertos.basico[i] = puertosBasicos.getInt(i);
        }
       

    }



    private static void modificarCarretera(String carretera, int numArista) {
        if(carretera.equals(TipoCamino.PLAYER_1)) {
            Partida.tablero.aristas.colorRoads[numArista] = coloresPorId[0];    
        } else if(carretera.equals(TipoCamino.PLAYER_2)) {
            Partida.tablero.aristas.colorRoads[numArista] = coloresPorId[1];  
        } else if(carretera.equals(TipoCamino.PLAYER_3)) {
            Partida.tablero.aristas.colorRoads[numArista] = coloresPorId[2];  
        } else if(carretera.equals(TipoCamino.PLAYER_4)) {
            Partida.tablero.aristas.colorRoads[numArista] = coloresPorId[3];  
        } else {
            Partida.tablero.aristas.colorRoads[numArista] = null;  
        }
        Partida.tablero.aristas.roadsType[numArista] = carretera;     
    }

    private static void modificarAsentamiento(String asentamiento, int numVertice) {
        if(asentamiento.equals(TipoAsentamiento.POBLADO_PLAYER_1)) {
            Partida.tablero.vertices.colorSettlement[numVertice] = coloresPorId[0];         
        } else if(asentamiento.equals(TipoAsentamiento.POBLADO_PLAYER_2)) {
            Partida.tablero.vertices.colorSettlement[numVertice] = coloresPorId[1];
        } else if(asentamiento.equals(TipoAsentamiento.POBLADO_PLAYER_3)) {
            Partida.tablero.vertices.colorSettlement[numVertice] = coloresPorId[2];
        } else if(asentamiento.equals(TipoAsentamiento.POBLADO_PLAYER_4)) {
            Partida.tablero.vertices.colorSettlement[numVertice] = coloresPorId[3];
        } else if(asentamiento.equals(TipoAsentamiento.CIUDAD_PLAYER_1)) {
            Partida.tablero.vertices.colorSettlement[numVertice] = coloresPorId[0];
        } else if(asentamiento.equals(TipoAsentamiento.CIUDAD_PLAYER_2)) {
            Partida.tablero.vertices.colorSettlement[numVertice] = coloresPorId[1];
        } else if(asentamiento.equals(TipoAsentamiento.CIUDAD_PLAYER_3)) {
            Partida.tablero.vertices.colorSettlement[numVertice] = coloresPorId[2];
        } else if(asentamiento.equals(TipoAsentamiento.CIUDAD_PLAYER_4)) {
            Partida.tablero.vertices.colorSettlement[numVertice] = coloresPorId[3];
        } else {
            Partida.tablero.vertices.colorSettlement[numVertice] = null;
        }
        // TODO:
        Partida.tablero.vertices.settlementsType[numVertice] = asentamiento;
    }


    private static void actualizarHexagonos(String hexagonos) {
        JSONObject object = new JSONObject(hexagonos);
        String auxHexagonosTipo = object.get(MessageKeys.HEXAGONOS_TIPOS).toString();
        String auxHexagonosValor = object.get(MessageKeys.HEXAGONOS_VALORES).toString();
        Partida.tablero.hexagonos.ladron = object.getInt(MessageKeys.HEXAGONOS_LADRON);
        
        JSONArray hexagonoTipo = new JSONArray(auxHexagonosTipo);
        JSONArray hexagonoValor = new JSONArray(auxHexagonosValor);
        for (int i = 0; i < hexagonoTipo.length(); i++) {
            String tipo = hexagonoTipo.getString(i);
            int valor = hexagonoValor.getInt(i);
            if (tipo.equals(TipoTerreno.BOSQUE)) {
                Partida.tablero.hexagonos.tipo[i] = TipoTerreno.BOSQUE;
            } else if(tipo.equals(TipoTerreno.CERRO)) {
                Partida.tablero.hexagonos.tipo[i] = TipoTerreno.CERRO;
            } else if(tipo.equals(TipoTerreno.DESIERTO)) {
                Partida.tablero.hexagonos.tipo[i] = TipoTerreno.DESIERTO;
            } else if(tipo.equals(TipoTerreno.MONTANYA)) {
                Partida.tablero.hexagonos.tipo[i] = TipoTerreno.MONTANYA;
            } else if (tipo.equals(TipoTerreno.PASTO)) {
                Partida.tablero.hexagonos.tipo[i] = TipoTerreno.PASTO;
            } else if (tipo.equals(TipoTerreno.SEMBRADO)) {
                Partida.tablero.hexagonos.tipo[i] = TipoTerreno.SEMBRADO;
            } else {
                Partida.tablero.hexagonos.tipo[i] = TipoTerreno.VACIO;
            }
            Partida.tablero.hexagonos.valor[i] = valor;
        }
        
        
    }

    private static void mostrarCambiosTablero() {
        updateHexagonBackground();
        updateVertix();
        updateArist();
        updateplayersName();
        updateDice();
        updateUserMaterials();
    }

  /****************************************************************
  *                     ACCIONES POSIBLES
  *****************************************************************/
    // TODO: Comprobacion arista valida
    private static Boolean construirPrimerCamino(int arista) {
        if(esMiTurno() && Partida.tablero.aristas.roadsType[arista].equals(
            TipoCamino.NADA)) {
    
            JSONObject jugada = new JSONObject();
            JSONObject move = new JSONObject();
            move.put("name", Jugada.PRIMER_CAMINO);
            move.put("param", arista);
            jugada.put("player", Partida.miTurno);
            jugada.put("game", Partida.id);
            jugada.put("move",move);
            ws.session.send(ws.partidaJugada, jugada.toString());
            return true;
        }
        return false;
    }

    // TODO: Comprobacion vertice valida
    private static Boolean construirPrimerAsentamiento(int vertice) {
        if(esMiTurno() && Partida.tablero.vertices.settlementsType[vertice].equals(
            TipoAsentamiento.NADA)) {

            JSONObject jugada = new JSONObject();
            JSONObject move = new JSONObject();
            move.put("name", Jugada.PRIMER_ASENTAMIENTO);
            move.put("param", vertice);
            jugada.put("player", Partida.miTurno);
            jugada.put("game", Partida.id);
            jugada.put("move",move);
            ws.session.send(ws.partidaJugada, jugada.toString());
            return true;
        }
        return false;
    }

    // TODO: Comprobacion arista valida
    private static Boolean construirCamino(int arista) {
        if(esMiTurno() && Partida.tablero.aristas.roadsType[arista].equals(
            TipoCamino.NADA) && puedoConstruirCamino()) {

            JSONObject jugada = new JSONObject();
            JSONObject move = new JSONObject();
            move.put("name", Jugada.CONSTRUIR_CAMINO);
            move.put("param", arista);
            jugada.put("player", Partida.miTurno);
            jugada.put("game", Partida.id);
            jugada.put("move",move);
            ws.session.send(ws.partidaJugada, jugada.toString());
            return true;
        }
        return false;
    }

    private static Boolean construirAsentamiento(int vertice) {
        if(esMiTurno() && Partida.tablero.vertices.settlementsType[vertice].equals(
            TipoAsentamiento.NADA) && puedoConstruirPoblado()) {
            
            JSONObject jugada = new JSONObject();
            JSONObject move = new JSONObject();
            move.put("name", Jugada.CONSTRUIR_POBLADO);
            move.put("param", vertice);
            jugada.put("player", Partida.miTurno);
            jugada.put("game", Partida.id);
            jugada.put("move",move);
            ws.session.send(ws.partidaJugada, jugada.toString());
            return true;
        }
        return false;
    }

    private static Boolean mejorarPueblo(int vertice) {
        if(esMiTurno() && Partida.tablero.vertices.settlementsType[vertice].equals(
            miTipoAsentamiento()) && puedoConstruirCiudad()) {
            
            JSONObject jugada = new JSONObject();
            JSONObject move = new JSONObject();
            move.put("name", Jugada.MEJORAR_POBLADO);
            move.put("param", vertice);
            jugada.put("player", Partida.miTurno);
            jugada.put("game", Partida.id);
            jugada.put("move",move);
            ws.session.send(ws.partidaJugada, jugada.toString());
            return true;
        }
        return false;
    }

  /****************************************************************
  * FUNCIONES AUXILIARES PARA TRADUCIR EL MENSAJE DEL
  * TABLERO A LAS ESTRUCTURAS DE DATOS INTERNAS E INICIALIZARLAS
  *****************************************************************/

    
    private static void actualizarPrecalculos() {
        Partida.CaminoDisponible = puedoConstruirCamino();
        Partida.CiudadDisponible = puedoConstruirCiudad();
        Partida.PobladoDisponible = puedoConstruirPoblado();

        if(!esMiTurno()) {
            Partida.movioLadron = false;
            Partida.yaComercio = false;
        }
    }

    private static Boolean esMiTurno() {
        return Partida.miTurno == Partida.turnoActual;
    }

    private static Boolean puedoConstruirPoblado() {
        Integer arcilla = Partida.jugadores[Partida.miTurno - 1].recursos.arcilla;
        Integer madera = Partida.jugadores[Partida.miTurno - 1].recursos.madera;
        Integer lana = Partida.jugadores[Partida.miTurno - 1].recursos.lana;
        Integer cereal = Partida.jugadores[Partida.miTurno - 1].recursos.cereales;

        return (!Partida.jugadores[Partida.miTurno -1].primerosAsentamientos ||
                arcilla > 0 && cereal > 0 && lana > 0 && madera > 0);
    }


    private static Boolean puedoConstruirCiudad() {
        Integer mineral = Partida.jugadores[Partida.miTurno - 1].recursos.mineral;
        Integer cereal = Partida.jugadores[Partida.miTurno - 1].recursos.cereales;

        return mineral > 2 && cereal > 1;
    }


    private static Boolean puedoConstruirCamino() {
        Integer arcilla = Partida.jugadores[Partida.miTurno - 1].recursos.arcilla;
        Integer madera = Partida.jugadores[Partida.miTurno - 1].recursos.madera;

        return (!Partida.jugadores[Partida.miTurno -1].primerosCaminos ||
            arcilla > 0 && madera > 0);
    }

    private static String miTipoAsentamiento() {
        switch (Partida.miTurno) {
            case 1:
                return TipoAsentamiento.POBLADO_PLAYER_1;
            case 2:
                return TipoAsentamiento.POBLADO_PLAYER_2;
            case 3:
                return TipoAsentamiento.POBLADO_PLAYER_3;
            case 4:
                return TipoAsentamiento.POBLADO_PLAYER_4;
            default:
                return TipoAsentamiento.NADA;
        }
    }

    private static String miTipoCiudad() {
        switch (Partida.miTurno) {
            case 1:
                return TipoAsentamiento.CIUDAD_PLAYER_1;
            case 2:
                return TipoAsentamiento.CIUDAD_PLAYER_2;
            case 3:
                return TipoAsentamiento.CIUDAD_PLAYER_3;
            case 4:
                return TipoAsentamiento.CIUDAD_PLAYER_4;
            default:
                return TipoAsentamiento.NADA;
        }
    }

    public static String miTipoCamino(){
        switch (Partida.miTurno) {
            case 1:
                return TipoCamino.PLAYER_1;
            case 2:
                return TipoCamino.PLAYER_2;
            case 3:
                return TipoCamino.PLAYER_3;
            case 4:
                return TipoCamino.PLAYER_4;
            default:
                return TipoCamino.NADA;
        }
      }


    @FXML
    void send_msg(ActionEvent event) {
        JSONObject msg = new JSONObject();
        msg.put("from", Partida.miTurno);
        msg.put("game", Partida.id);
        msg.put("body", chatInput.getText().toString() );
        ws.session.send(ws.enviarMensajePartida, msg.toString());  
        chatInput.clear();    

    }

    private Polygon createHexagon(Integer numHexag) {
        Polygon pol = new Polygon();
        pol.getPoints().addAll(new Double[]{-50.0, 30.0, 0.0, 60.0, 50.0, 30.0,
                                             50.0,-30.0, 0.0,-60.0,-50.0,-30.0});
                                
        if(numHexag < 4) { // Primera fila
            pol.setLayoutX(start_X_position + (horizontal_right_gap * numHexag));
            pol.setLayoutY(start_Y_position); 

        } else if (numHexag < 9) { // Segunda fila
            pol.setLayoutX((start_X_position - horizontal_left_gap) + 
                                (horizontal_right_gap * (numHexag - 4)));
            pol.setLayoutY(start_Y_position + vertical_gap);

        } else if (numHexag < 15) { // Tercera fila
            pol.setLayoutX((start_X_position - (horizontal_left_gap * 2)) 
                            + (horizontal_right_gap * (numHexag - 9)));
            pol.setLayoutY(start_Y_position + (vertical_gap * 2));

        } else if (numHexag < 22) { // Cuarta fila
            pol.setLayoutX((start_X_position - (horizontal_left_gap * 3)) 
                            + (horizontal_right_gap * (numHexag - 15)) );
            pol.setLayoutY(start_Y_position + (vertical_gap * 3));

        } else if (numHexag < 28) { // Quinta fila
            pol.setLayoutX((start_X_position - (horizontal_left_gap * 2)) 
                            + (horizontal_right_gap * (numHexag - 22)));
            pol.setLayoutY(start_Y_position + (vertical_gap * 4));

        } else if (numHexag < 33) { // Sexta fila
            pol.setLayoutX((start_X_position - horizontal_left_gap) + 
                                (horizontal_right_gap * (numHexag - 28)));
            pol.setLayoutY(start_Y_position + (vertical_gap * 5));
            
        } else { // Septima fila
            pol.setLayoutX(start_X_position 
                                        + (horizontal_right_gap * (numHexag - 33  )));
            pol.setLayoutY(start_Y_position + (vertical_gap * 6));
        }

        return pol;
    }

    private void fillHexagonSea(Polygon pol, Integer i) {
        if ( i <= 4 || i == 8 || i == 9 || i == 14 ||
        i == 15 || i == 21 || i == 22 || i == 27 ||
        i == 28 || i >= 32) {

            pol.setFill(Color.web("5c86ae"));
        } 
    }

    private static void updateHexagonBackground() {
        for(int i = 0; i < numberofHexagons; i++) {
            if (Partida.tablero.hexagonos.tipo[i].equals(TipoTerreno.BOSQUE) ) {
                Partida.tablero.hexagonos.hexagons[i].setFill(Color.web("536d35"));
            } else if (Partida.tablero.hexagonos.tipo[i].equals(TipoTerreno.CERRO)) {
                Partida.tablero.hexagonos.hexagons[i].setFill(Color.web("636363"));
            } else if (Partida.tablero.hexagonos.tipo[i].equals(TipoTerreno.DESIERTO)) {
                Partida.tablero.hexagonos.hexagons[i].setFill(Color.web("ba9e5c"));
            } else if (Partida.tablero.hexagonos.tipo[i].equals(TipoTerreno.MONTANYA)) {
                Partida.tablero.hexagonos.hexagons[i].setFill(Color.web("97593c"));
            } else if (Partida.tablero.hexagonos.tipo[i].equals(TipoTerreno.PASTO)) {
                Partida.tablero.hexagonos.hexagons[i].setFill(Color.web("93b248"));
            } else if (Partida.tablero.hexagonos.tipo[i].equals(TipoTerreno.SEMBRADO)) {
                Partida.tablero.hexagonos.hexagons[i].setFill(Color.YELLOW);
            } else {
                Partida.tablero.hexagonos.hexagons[i].setFill(Color.GRAY);
            }

        }    
        Partida.tablero.hexagonos.numberOverHexagon[Partida.tablero.hexagonos.ladron].setDisable(true);
        for(int i = 0; i < numberofHexagons; i++) {
            if(i != Partida.tablero.hexagonos.ladron) {
                Partida.tablero.hexagonos.numberOverHexagon[i].setDisable(false);
            }
        }
        // runLater debe usar variables final.
        Platform.runLater(new Runnable() {
            @Override public void run() {
                Partida.tablero.hexagonos.numberOverHexagon[0].setText(
                Partida.tablero.hexagonos.valor[0].toString());
                Partida.tablero.hexagonos.numberOverHexagon[1].setText(
                Partida.tablero.hexagonos.valor[1].toString());
                Partida.tablero.hexagonos.numberOverHexagon[2].setText(
                Partida.tablero.hexagonos.valor[2].toString());
                Partida.tablero.hexagonos.numberOverHexagon[3].setText(
                Partida.tablero.hexagonos.valor[3].toString());
                Partida.tablero.hexagonos.numberOverHexagon[4].setText(
                Partida.tablero.hexagonos.valor[4].toString());
                Partida.tablero.hexagonos.numberOverHexagon[5].setText(
                Partida.tablero.hexagonos.valor[5].toString());
                Partida.tablero.hexagonos.numberOverHexagon[6].setText(
                Partida.tablero.hexagonos.valor[6].toString());
                Partida.tablero.hexagonos.numberOverHexagon[7].setText(
                Partida.tablero.hexagonos.valor[7].toString());
                Partida.tablero.hexagonos.numberOverHexagon[8].setText(
                Partida.tablero.hexagonos.valor[8].toString());
                Partida.tablero.hexagonos.numberOverHexagon[9].setText(
                Partida.tablero.hexagonos.valor[9].toString());
                Partida.tablero.hexagonos.numberOverHexagon[10].setText(
                Partida.tablero.hexagonos.valor[10].toString());
                Partida.tablero.hexagonos.numberOverHexagon[11].setText(
                Partida.tablero.hexagonos.valor[11].toString());
                Partida.tablero.hexagonos.numberOverHexagon[12].setText(
                Partida.tablero.hexagonos.valor[12].toString());
                Partida.tablero.hexagonos.numberOverHexagon[13].setText(
                Partida.tablero.hexagonos.valor[13].toString());
                Partida.tablero.hexagonos.numberOverHexagon[14].setText(
                Partida.tablero.hexagonos.valor[14].toString());
                Partida.tablero.hexagonos.numberOverHexagon[15].setText(
                Partida.tablero.hexagonos.valor[15].toString());
                Partida.tablero.hexagonos.numberOverHexagon[16].setText(
                Partida.tablero.hexagonos.valor[16].toString());
                Partida.tablero.hexagonos.numberOverHexagon[17].setText(
                Partida.tablero.hexagonos.valor[17].toString());
                Partida.tablero.hexagonos.numberOverHexagon[18].setText(
                Partida.tablero.hexagonos.valor[18].toString());
            }
        });
    }

    private static void updateVertix() {
        for(int i = 0; i < numberSettlements; i++) {
           if(!Partida.tablero.vertices.settlementsType[i].equals(TipoAsentamiento.NADA)) {
            if(esCiudad(Partida.tablero.vertices.settlementsType[i])) {
                Partida.tablero.vertices.settlements[i].setStyle(
                    "-fx-border-color:white;-fx-border-width: 3 3 3 3;");   
            } 
            Partida.tablero.vertices.settlements[i].
                setBackground(new Background(new BackgroundFill(
                    Partida.tablero.vertices.colorSettlement[i], null, null)));   
           }
        }
    }

    private static Boolean esCiudad(String posibleCiudad) {
        return  posibleCiudad.equals(TipoAsentamiento.CIUDAD_PLAYER_1) ||
                posibleCiudad.equals(TipoAsentamiento.CIUDAD_PLAYER_2) || 
                posibleCiudad.equals(TipoAsentamiento.CIUDAD_PLAYER_3) ||
                posibleCiudad.equals(TipoAsentamiento.CIUDAD_PLAYER_4);
    }

    private static void updateArist() {
        for(int i = 0; i < numberRoads; i++) {
            if(Partida.tablero.aristas.colorRoads[i] != null) {
             Partida.tablero.aristas.roads[i].
                         setBackground(new Background(new BackgroundFill(
                             Partida.tablero.aristas.colorRoads[i], null, null)));
            }
            if(Partida.tablero.aristas.puertos.arcilla == i) {
                Partida.tablero.aristas.roads[i].setStyle(
                    "-fx-border-color:#636363;-fx-border-width: 3;");

            } else if (Partida.tablero.aristas.puertos.madera == i) {
                Partida.tablero.aristas.roads[i].setStyle(
                    "-fx-border-color:#536d35;-fx-border-width: 3;");

            } else if (Partida.tablero.aristas.puertos.mineral == i) {
                Partida.tablero.aristas.roads[i].setStyle(
                    "-fx-border-color:#97593c;-fx-border-width: 3;");

            } else if (Partida.tablero.aristas.puertos.lana == i ) {
                Partida.tablero.aristas.roads[i].setStyle(
                    "-fx-border-color:#93b248;-fx-border-width: 3;");

            } else if (Partida.tablero.aristas.puertos.cereal == i) {
                Partida.tablero.aristas.roads[i].setStyle(
                    "-fx-border-color:yellow;-fx-border-width: 3;");

            } else if (Partida.tablero.aristas.puertos.basico[0] == i ||
                       Partida.tablero.aristas.puertos.basico[1] == i ||
                       Partida.tablero.aristas.puertos.basico[2] == i ||
                       Partida.tablero.aristas.puertos.basico[3] == i) {
                Partida.tablero.aristas.roads[i].setStyle(
                    "-fx-border-color:#BA55D3;-fx-border-width: 3;");
            }
         }
    }
    // Creacion del tablero

    private Boolean hasRoads(Integer i) {
        return ( (i > 4  && i <=  7)  || 
                 (i > 9 && i <= 13)  ||
                 (i > 15 && i <= 20)  ||  
                 (i > 22 && i <= 26)  ||  
                 (i > 28 && i <= 31));  
    }

    private ToggleButton createRoadN(Polygon pol, int pos) {
        ToggleButton _road = new ToggleButton();
        _road.setPrefSize(1,30);
        _road.setLayoutX(pol.getLayoutX() - 65);
        _road.setLayoutY(pol.getLayoutY() - 15);
        Partida.tablero.aristas.roads[pos] =  _road;
        setColorButonOnClick(_road, pos);
        return _road;
    }

    private ToggleButton createRoadSE(Polygon pol, int pos) {
        ToggleButton _roadSE = new ToggleButton();
        _roadSE.setPrefSize(1,30);
        _roadSE.setLayoutX(pol.getLayoutX() + 23);
        _roadSE.setLayoutY(pol.getLayoutY() - 66);
        _roadSE.setRotate(120);
        Partida.tablero.aristas.roads[pos] =  _roadSE;
        setColorButonOnClick(_roadSE, pos);
        return _roadSE;
    }

    private ToggleButton createRoadNE(Polygon pol,  int pos) {
        ToggleButton _roadNE = new ToggleButton();
        _roadNE.setPrefSize(1,30);
        _roadNE.setLayoutX(pol.getLayoutX() - 40);
        _roadNE.setLayoutY(pol.getLayoutY() - 70);
        _roadNE.setRotate(60);
        Partida.tablero.aristas.roads[pos] =  _roadNE;
        setColorButonOnClick(_roadNE, pos);
        return _roadNE;

    }

    private void setColorButonOnClick(ToggleButton button, int pos) {
        button.setOnMouseClicked( event -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && 
                Partida.tablero.aristas.posible_camino[pos] &&
                Partida.tablero.aristas.roadsType[pos].equals(TipoCamino.NADA) &&
                esMiTurno()) {
                posRoad = pos;
                buildRoadPopUp();
                //if (!popupCards.isShowing()) {
                    Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
                    popupCards.show(stage);
                //}
            } else if((Partida.tablero.aristas.puertos.arcilla == pos ||
                      Partida.tablero.aristas.puertos.madera == pos  ||
                      Partida.tablero.aristas.puertos.mineral == pos ||
                      Partida.tablero.aristas.puertos.lana == pos    ||
                      Partida.tablero.aristas.puertos.cereal == pos  || 
                      Partida.tablero.aristas.puertos.basico[0] == pos ||
                      Partida.tablero.aristas.puertos.basico[1] == pos ||
                      Partida.tablero.aristas.puertos.basico[2] == pos ||
                      Partida.tablero.aristas.puertos.basico[3] == pos) && 
                      event.getButton().equals(MouseButton.SECONDARY) && 
                      esMiTurno()) {
                idPuerto = pos;
                System.out.println("externalTradePopUp");
                externalTradePopUp();   
                if (!popupExternalTrade.isShowing()) {
                    
                    Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
                    popupExternalTrade.show(stage);
                }
            }   
        });
    }

    private void assignRoads(Polygon pol , Integer i) {
        int nRoadN,nRoadNE,nRoadSE;

        if (hasRoads(i)) {
            switch (i) {
                case 5:
                    nRoadN = 3;  nRoadNE = 4;    nRoadSE = 5;
                    break;
                case 6:
                    nRoadN = 0;  nRoadNE = 9;    nRoadSE = 10;
                    break;
                case 7:
                    nRoadN = 6;  nRoadNE = 14;   nRoadSE = 15;
                    break;
                case 10:
                    nRoadN = 19; nRoadNE = 20;   nRoadSE = 2;
                    break;
                case 11:
                    nRoadN = 16; nRoadNE = 1;    nRoadSE = 8;
                    break;
                case 12:
                    nRoadN = 21; nRoadNE = 7;    nRoadSE = 13;
                    break;
                case 13:
                    nRoadN = 24; nRoadNE = 12;   nRoadSE = 30;
                    break;
                case 16:
                    nRoadN = 34; nRoadNE = 35;   nRoadSE = 18;
                    break;
                case 17:
                    nRoadN = 31; nRoadNE = 17;   nRoadSE = 23;
                    break;
                case 18:
                    nRoadN = 36; nRoadNE = 22;   nRoadSE = 26;
                    break;
                case 19:
                    nRoadN = 39; nRoadNE = 25;   nRoadSE = 29;
                    break;
                case 20:
                    nRoadN = 42; nRoadNE = 28;   nRoadSE = 48;
                    break;
                case 23:
                    nRoadN = 52; nRoadNE = 32;   nRoadSE = 38;
                    break;
                case 24:
                    nRoadN = 49; nRoadNE = 37;   nRoadSE = 41;
                    break;
                case 25:
                    nRoadN = 53; nRoadNE = 40;   nRoadSE = 44;
                    break;
                case 26:
                    nRoadN = 56; nRoadNE = 43;   nRoadSE = 47;
                    break;
                case 29:
                    nRoadN = 65; nRoadNE = 50;   nRoadSE = 55;
                    break;
                case 30:
                    nRoadN = 62; nRoadNE = 54;   nRoadSE = 58;
                    break;
                case 31:
                    nRoadN = 66; nRoadNE = 57;   nRoadSE = 61;
                    break;
                default:
                    nRoadN = -1; nRoadNE = -1;   nRoadSE = -1;
                    break;
            }
            mainAnchor.getChildren().add(createRoadN(pol,nRoadN)); 
            mainAnchor.getChildren().add(createRoadNE(pol,nRoadNE)); 
            mainAnchor.getChildren().add(createRoadSE(pol,nRoadSE)); 
        } else if (i == 8) {
            mainAnchor.getChildren().add(createRoadN(pol, 11));
        } else if (i == 14) {
            mainAnchor.getChildren().add(createRoadN(pol, 27));
        } else if (i == 21) {
            mainAnchor.getChildren().add(createRoadN(pol, 45));
        }
        
        else if (i == 22) {
            mainAnchor.getChildren().add(createRoadSE(pol, 33)); 
        } else if (i == 27) {
            mainAnchor.getChildren().add(createRoadN(pol, 59)); 
            mainAnchor.getChildren().add(createRoadNE(pol, 46));
        } else if (i == 28) {
            mainAnchor.getChildren().add(createRoadSE(pol, 51)); 
        } else if (i == 34) {
            mainAnchor.getChildren().add(createRoadNE(pol, 63));
            mainAnchor.getChildren().add(createRoadSE(pol, 68));
        } else if (i == 35 ) {
            mainAnchor.getChildren().add(createRoadNE(pol, 67));
            mainAnchor.getChildren().add(createRoadSE(pol, 71));
        } else if (i == 32) {
            mainAnchor.getChildren().add(createRoadN(pol, 69));
            mainAnchor.getChildren().add(createRoadNE(pol, 60));
        } else if (i == 33) {
            mainAnchor.getChildren().add(createRoadSE(pol, 64)); 
        } else if (i == 36) {
            mainAnchor.getChildren().add(createRoadNE(pol, 70));
        }
    }

    private void assignSettlementNO(Polygon pol, Integer position) {
        Button circle = new Button();
        circle.setShape(new Circle(settleSize));
        circle.setMinSize(2*settleSize, 2*settleSize);
        circle.setMaxSize(2*settleSize, 2*settleSize);
        circle.setLayoutX(pol.getLayoutX() - 68);
        circle.setLayoutY(pol.getLayoutY() - 45);
        Partida.tablero.vertices.settlements[position] = circle;
        onClickSettlement(circle,position);
        mainAnchor.getChildren().add(circle);
    }

    private void assignSettlementN(Polygon pol, Integer position) {
            Button circle = new Button();
            circle.setShape(new Circle(settleSize));
            circle.setMinSize(2*settleSize, 2*settleSize);
            circle.setMaxSize(2*settleSize, 2*settleSize);
            circle.setLayoutX(pol.getLayoutX() - 12);
            circle.setLayoutY(pol.getLayoutY() - 88);
            Partida.tablero.vertices.settlements[position] = circle;
            onClickSettlement(circle,position);
            mainAnchor.getChildren().add(circle);
    }

    private void assignSettlements(Polygon pol, Integer i) {
        int settleN, settleNO;
        if(hasRoads(i) || i == 32) {
            switch (i) {
                case 5:
                    settleN = 5;  settleNO = 4;    
                    break;
                case 6:
                    settleN = 9;  settleNO = 0;    
                    break;
                case 7:
                    settleN = 13;  settleNO = 6;   
                    break;
                case 10:
                    settleN = 3; settleNO = 17;   
                    break;
                case 11:
                    settleN = 1; settleNO = 2;    
                    break;
                case 12:
                    settleN = 7; settleNO = 8;    
                    break;
                case 13:
                    settleN = 11; settleNO = 12;   
                    break;
                case 16:
                    settleN = 16; settleNO = 28;   
                    break;
                case 17:
                    settleN = 14; settleNO = 15;   
                    break;
                case 18:
                    settleN = 18; settleNO = 19;   
                    break;
                case 19:
                    settleN = 20; settleNO = 21;   
                    break;
                case 20:
                    settleN = 23; settleNO = 24;   
                    break;
                case 23:
                    settleN = 25; settleNO = 26;   
                    break;
                case 24:
                    settleN = 29; settleNO = 30;   
                    break;
                case 25:
                    settleN = 31; settleNO = 32;   
                    break;
                case 26:
                    settleN = 33; settleNO = 34;   
                    break;
                case 29:
                    settleN = 38; settleNO = 39;  
                    break;
                case 30:
                    settleN = 41; settleNO = 42;   
                    break;
                case 31:
                    settleN = 43; settleNO = 44;   
                    break;
                case 32:
                    settleN = 45; settleNO = 46; 
                    break;
                default:
                    settleN = -1; settleNO = -1;   
                    break;
            }
            assignSettlementN(pol, settleN);
            assignSettlementNO(pol, settleNO);
        } else if (i == 8) {
            assignSettlementNO(pol, 10);
        } else if(i == 14) {
            assignSettlementNO(pol, 22);
        } else if(i == 21) {
            assignSettlementNO(pol, 35);
        }
        else if (i == 22) {
            assignSettlementN(pol, 27);
        } else if(i == 27) {
            assignSettlementN(pol, 36);
            assignSettlementNO(pol, 37);
        } else if(i == 28) {
            assignSettlementN(pol, 40);
        } else if(i == 33) {
            assignSettlementN(pol, 49);
        } else if(i == 34) {
            assignSettlementN(pol, 47);
            assignSettlementNO(pol, 48);
        } else if(i == 35) {
            assignSettlementN(pol, 50);
            assignSettlementNO(pol, 51);
        } else if(i == 36) {
            assignSettlementN(pol, 52);
            assignSettlementNO(pol, 53);
        }
    }

    // Click sobre un asentamiento
    private void onClickSettlement(Button circle,Integer position) {
        circle.setOnMouseClicked( event -> {
            System.out.println(Partida.tablero.vertices.posible_asentamiento[position]);
            if (event.getButton().equals(MouseButton.PRIMARY) && esMiTurno()    
                && 
                ((!Partida.jugadores[Partida.miTurno -1].primerosAsentamientos) 
                ||
                (Partida.jugadores[Partida.miTurno -1].primerosAsentamientos && 
                 Partida.tablero.vertices.posible_asentamiento[position]) 
                ||
                (Partida.jugadores[Partida.miTurno -1].primerosAsentamientos &&
                 !Partida.tablero.vertices.posible_asentamiento[position] &&
                  Partida.tablero.vertices.settlementsType[position].equals(
                    miTipoAsentamiento())))) {
                    posSettle = position;
                    buildSettlementPopUp();
                    if (!popupBuildSettle.isShowing()) {
                        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
                        popupBuildSettle.show(stage);
                    }
            }     
        });
    }

    private void buildRoadPopUp() {
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefSize(310, 115);
        anchorPane.setStyle("-fx-background-color:  #965d62; -fx-background-radius: 12px" );
        popupCards = new Popup();
        popupCards.getContent().add(anchorPane);
        popupCards.setAutoHide(true);

        Button buildRoad = new Button();
        buildRoad.setPrefSize(300,90);
        buildRoad.setLayoutX(anchorPane.getLayoutX() + 5);
        buildRoad.setLayoutY(anchorPane.getLayoutY() + 10);

        buildRoad.setStyle("-fx-background-color: #c7956d; -fx-background-radius: 12px");
        if(Partida.jugadores[Partida.miTurno -1].primerosCaminos){
            buildRoad.setText(LangService.getMapping("build_road"));
            buildRoad.setDisable(!Partida.CaminoDisponible);
            buildRoad.setOnMouseClicked( event -> {
                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    construirCamino(posRoad);
                    popupCards.hide();
                }    
            });
        } else {
            buildRoad.setText(LangService.getMapping("first_road"));
            buildRoad.setOnMouseClicked( event -> {
                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    construirPrimerCamino(posRoad);
                    popupCards.hide();
                }    
            });
        }

        DropShadow shadow = new DropShadow();
        buildRoad.setEffect(shadow);
        anchorPane.getChildren().add(buildRoad);

    }

    private void resourcesPopUp() {
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefSize(350, 115);
        anchorPane.setStyle("-fx-background-color:  #965d62; -fx-background-radius: 12px" );
        popupResources = new Popup();
        popupResources.getContent().add(anchorPane);
        popupResources.setAutoHide(true);
        
        // Titulo
        Text title = new Text(10, 50, (LangService.getMapping("player_resources")));
        title.setFont(new Font(40));
        title.setLayoutX(anchorPane.getLayoutX());
        title.setLayoutY(anchorPane.getLayoutY() + 5);
        title.setFill(Color.WHITE);
        anchorPane.getChildren().add(title);
        
        Text tofferPlayer = new Text(10, 50, LangService.getMapping("choose_player"));
        tofferPlayer.setFont(new Font(20));
        tofferPlayer.setLayoutX(anchorPane.getLayoutX() + 10 );
        tofferPlayer.setLayoutY(anchorPane.getLayoutY() + 65);
        tofferPlayer.setFill(Color.WHITE);
        anchorPane.getChildren().add(tofferPlayer);
        
        // Select jugador elegido
        offerPlayerResources = new ChoiceBox<>();
        offerPlayerResources.setStyle("-fx-background-radius: 12px;");
        for(int i = 0; i < numberPlayers; i++) {
            if(i != (Partida.miTurno - 1) ) {
                offerPlayerResources.getItems().add(Partida.jugadores[i].nombre);
            }
        }
        
        Integer _offerPlayer = ((Partida.miTurno - 1) + 1) % 4;
        offerPlayerResources.setValue(Partida.jugadores[_offerPlayer].nombre);
        Integer _arcilla = Partida.jugadores[_offerPlayer].recursos.arcilla;
        Integer _madera = Partida.jugadores[_offerPlayer].recursos.madera;
        Integer _cereal =  Partida.jugadores[_offerPlayer].recursos.cereales;
        Integer _mineral = Partida.jugadores[_offerPlayer].recursos.mineral;
        Integer _lana = Partida.jugadores[_offerPlayer].recursos.lana;
        
        // Arcilla
        Text arcilla = new Text(10, 50, (LangService.getMapping("clay")) + "\t\t\t\t\t\t " 
                                            + _arcilla.toString());
        arcilla.setFont(new Font(20));
        arcilla.setLayoutX(anchorPane.getLayoutX()+ 10);
        arcilla.setLayoutY(anchorPane.getLayoutY() + 105);
        arcilla.setFill(Color.WHITE);
        anchorPane.getChildren().add(arcilla);
        
        // Madera
        Text madera = new Text(10, 50, (LangService.getMapping("wood")) + "\t\t\t\t\t\t " 
                                            + _madera.toString());
        madera.setFont(new Font(20));
        madera.setLayoutX(anchorPane.getLayoutX()+ 10);
        madera.setLayoutY(anchorPane.getLayoutY() + 145);
        madera.setFill(Color.WHITE);
        anchorPane.getChildren().add(madera);
 
        // Cereal
        Text cereal = new Text(10, 50, (LangService.getMapping("cereal")) + "\t\t\t\t\t\t " 
                                            + _cereal.toString());
        cereal.setFont(new Font(20));
        cereal.setLayoutX(anchorPane.getLayoutX()+ 10);
        cereal.setLayoutY(anchorPane.getLayoutY() + 185);
        cereal.setFill(Color.WHITE);
        anchorPane.getChildren().add(cereal);
        
        // Mineral
        Text mineral = new Text(10, 50, (LangService.getMapping("mineral")) + "\t\t\t\t\t\t " 
                                            + _mineral.toString());
        mineral.setFont(new Font(20));
        mineral.setLayoutX(anchorPane.getLayoutX()+ 10);
        mineral.setLayoutY(anchorPane.getLayoutY() + 225);
        mineral.setFill(Color.WHITE);
        anchorPane.getChildren().add(mineral);

        // Lana
        Text lana = new Text(10, 50, (LangService.getMapping("wool")) + "\t\t\t\t\t\t\t " 
                                        + _lana.toString());
        lana.setFont(new Font(20));
        lana.setLayoutX(anchorPane.getLayoutX()+ 10);
        lana.setLayoutY(anchorPane.getLayoutY() + 265);
        lana.setFill(Color.WHITE);
        anchorPane.getChildren().add(lana);
    

        offerPlayerResources.getSelectionModel().selectedItemProperty().addListener( (v, oldValue, newValue) -> 
        {
               String name = (String) newValue;
               Integer player;
               if(name.equals(Partida.jugadores[0].nombre)) {
                   player = 0;
               } else if(name.equals(Partida.jugadores[1].nombre)) {
                   player = 1;
               } else if(name.equals(Partida.jugadores[2].nombre)) {
                   player = 2;
               } else {
                   player = 3;
               }
               final Integer arcilla_aux = Partida.jugadores[player].recursos.arcilla;
               final Integer madera_aux = Partida.jugadores[player].recursos.madera;
               final Integer cereal_aux =  Partida.jugadores[player].recursos.cereales;
               final Integer mineral_aux = Partida.jugadores[player].recursos.mineral;
               final Integer lana_aux = Partida.jugadores[player].recursos.lana;


               Platform.runLater(new Runnable() {
                   @Override public void run() {
                       arcilla.setText((LangService.getMapping("clay")) + "\t\t\t\t\t\t " 
                                       + arcilla_aux.toString());
                       madera.setText((LangService.getMapping("wood")) + "\t\t\t\t\t\t " 
                                       + madera_aux.toString());
                       mineral.setText((LangService.getMapping("mineral")) + "\t\t\t\t\t\t " 
                                       + cereal_aux.toString());
                       cereal.setText((LangService.getMapping("cereal")) + "\t\t\t\t\t\t " 
                                       + mineral_aux.toString());
                       lana.setText((LangService.getMapping("wool")) + "\t\t\t\t\t\t\t " 
                                       + lana_aux.toString());
                   }
               });
               offerPlayerResources.setValue(newValue);    
        }
       );
       offerPlayerResources.setLayoutX(anchorPane.getLayoutX() + 270);
       offerPlayerResources.setLayoutY(anchorPane.getLayoutY() + 90);
       anchorPane.getChildren().add(offerPlayerResources);
    }
    
    private void buildSettlementPopUp() {
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefSize(310, 115);
        anchorPane.setStyle("-fx-background-color:  #965d62; -fx-background-radius: 12px" );
        popupBuildSettle = new Popup();
        popupBuildSettle.getContent().add(anchorPane);
        popupBuildSettle.setAutoHide(true);

        Button buildSettle = new Button();
        buildSettle.setPrefSize(300,90);
        buildSettle.setLayoutX(anchorPane.getLayoutX() + 5);
        buildSettle.setLayoutY(anchorPane.getLayoutY() + 10);

        buildSettle.setStyle("-fx-background-color: #c7956d; -fx-background-radius: 12px");
        if(Partida.jugadores[Partida.miTurno -1].primerosAsentamientos){
            if(Partida.tablero.vertices.settlementsType[posSettle].equals(
                miTipoAsentamiento())) {

                buildSettle.setText(LangService.getMapping("city_upgrade"));
                buildSettle.setDisable(!Partida.CiudadDisponible);
                buildSettle.setOnMouseClicked( event -> {
                    if (event.getButton().equals(MouseButton.PRIMARY)) {
                        mejorarPueblo(posSettle);
                        popupBuildSettle.hide();
                    }    
                });

            } else if(Partida.tablero.vertices.settlementsType[posSettle].equals(
                miTipoCiudad())) {

                buildSettle.setText(LangService.getMapping(
                    LangService.getMapping("no_upgrade")));
                buildSettle.setDisable(true);

            } else {
                buildSettle.setText(LangService.getMapping("build_town"));
                buildSettle.setDisable(!Partida.PobladoDisponible);
                buildSettle.setOnMouseClicked( event -> {
                    if (event.getButton().equals(MouseButton.PRIMARY)) {
                        construirAsentamiento(posSettle);
                        popupBuildSettle.hide();
                    }    
                });
            }
        } else {
            buildSettle.setText(LangService.getMapping("first_settle"));
            buildSettle.setOnMouseClicked( event -> {
                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    construirPrimerAsentamiento(posSettle);
                    popupBuildSettle.hide();
                }    
            });
        }

        DropShadow shadow = new DropShadow();
        buildSettle.setEffect(shadow);
        anchorPane.getChildren().add(buildSettle);
    }

    private static void updateplayersName() {
        _player1Name.setText(Partida.jugadores[0].nombre  +
            " (" + Partida.jugadores[0].puntos + ")"); 
        
        _player2Name.setText(Partida.jugadores[1].nombre  +
            " (" + Partida.jugadores[1].puntos + ")");
        _player3Name.setText(Partida.jugadores[2].nombre  +
            " (" + Partida.jugadores[2].puntos + ")");
        _player4Name.setText(Partida.jugadores[3].nombre + 
            " (" + Partida.jugadores[3].puntos + ")");

        Platform.runLater(new Runnable() {
            @Override public void run() {
                _turnPlayer.setText(LangService.getMapping("turn") + ": " + 
                        Partida.jugadores[Partida.turnoActual-1].nombre);
            }
        });
        _passTurnButton.setDisable(Partida.miTurno != Partida.turnoActual);
    }
    
    private void inTradePopUp() {
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefSize(400, 650);
        anchorPane.setStyle("-fx-background-color:  #965d62; -fx-background-radius: 12px" );
        popupInternalTrade = new Popup();
        popupInternalTrade.getContent().add(anchorPane);
        popupInternalTrade.setAutoHide(true);

        // Titulo
        Text title = new Text(10, 50, (LangService.getMapping("internal_trade")));
        title.setFont(new Font(40));
        title.setLayoutX(anchorPane.getLayoutX() + 10 );
        title.setLayoutY(anchorPane.getLayoutY() + 30);
        title.setFill(Color.WHITE);
        anchorPane.getChildren().add(title);

        // Selecciona un jugador para tradeo
        Text tofferPlayer = new Text(10, 50, "Seleccione un jugador");
        tofferPlayer.setFont(new Font(20));
        tofferPlayer.setLayoutX(anchorPane.getLayoutX() + 10 );
        tofferPlayer.setLayoutY(anchorPane.getLayoutY() + 100);
        tofferPlayer.setFill(Color.WHITE);
        anchorPane.getChildren().add(tofferPlayer);
        
        // Select jugador elegido
        offerPlayer = new ChoiceBox<>();
        offerPlayer.setStyle("-fx-background-radius: 12px;");
        for(int i = 0; i < numberPlayers; i++) {
            if(i != (Partida.miTurno - 1) ) {
                offerPlayer.getItems().add(Partida.jugadores[i].nombre);
            }
        }
        offerPlayer.setLayoutX(anchorPane.getLayoutX() + 270);
        offerPlayer.setLayoutY(anchorPane.getLayoutY() + 127);
        Integer _offerPlayer = ((Partida.miTurno - 1) + 1) % 4;
        offerPlayer.setValue(Partida.jugadores[_offerPlayer].nombre);
        Integer _arcilla = Partida.jugadores[_offerPlayer].recursos.arcilla;
        Integer _madera = Partida.jugadores[_offerPlayer].recursos.madera;
        Integer _cereal =  Partida.jugadores[_offerPlayer].recursos.cereales;
        Integer _mineral = Partida.jugadores[_offerPlayer].recursos.mineral;
        Integer _lana = Partida.jugadores[_offerPlayer].recursos.lana;
        anchorPane.getChildren().add(offerPlayer);
        

         // Selecciona un material para tradeo
         Text tofferMaterial = new Text(10, 50, "Material ofrecido");
         tofferMaterial.setFont(new Font(20));
         tofferMaterial.setLayoutX(anchorPane.getLayoutX() + 10 );
         tofferMaterial.setLayoutY(anchorPane.getLayoutY() + 180);
         tofferMaterial.setFill(Color.WHITE);
         anchorPane.getChildren().add(tofferMaterial);
         
         // Select material ofrecido
        offerMaterial = new ChoiceBox<>();
        offerMaterial.setStyle("-fx-background-radius: 12px;" );
        offerMaterial.getItems().add(LangService.getMapping("clay"));
        offerMaterial.getItems().add(LangService.getMapping("wood"));
        offerMaterial.getItems().add(LangService.getMapping("cereal"));
        offerMaterial.getItems().add(LangService.getMapping("wool"));
        offerMaterial.getItems().add(LangService.getMapping("mineral"));
        offerMaterial.setValue(LangService.getMapping("clay"));
        offerMaterial.setLayoutX(anchorPane.getLayoutX() + 270);
        offerMaterial.setLayoutY(anchorPane.getLayoutY() + 210);
        anchorPane.getChildren().add(offerMaterial);


        // Cantidad de material ofrecido
        Text tofferAmmount = new Text(10, 50, "Cantidad ofrecida");
        tofferAmmount.setFont(new Font(20));
        tofferAmmount.setLayoutX(anchorPane.getLayoutX() + 10 );
        tofferAmmount.setLayoutY(anchorPane.getLayoutY() + 260);
        tofferAmmount.setFill(Color.WHITE);
        anchorPane.getChildren().add(tofferAmmount);
        

         // Spinner cantidad material ofrecido.
         // TODO: Listener
         spinnerGive  = new Spinner(0, 
                        Partida.jugadores[Partida.miTurno -1].recursos.arcilla, 
                        1);
         spinnerGive.setStyle("-fx-background-radius: 12px;" );
         spinnerGive.setPrefSize(75, 25);
         spinnerGive.setLayoutX(anchorPane.getLayoutX() + 270 );
         spinnerGive.setLayoutY(anchorPane.getLayoutY() + 290);
         anchorPane.getChildren().add(spinnerGive);

         offerMaterial.getSelectionModel().selectedItemProperty().addListener( (v, oldValue, newValue) -> 
         {
            offerMaterial.setValue(newValue);
            final String materialSeleccionado =  new String(offerMaterial.getValue());
            Platform.runLater(new Runnable() {
                @Override public void run() {
                    anchorPane.getChildren().remove(spinnerGive);
                    if(materialSeleccionado.equals("Lana")) {
                        spinnerGive = 
                                    new Spinner(0,
                                    Partida.jugadores[Partida.miTurno -1].recursos.lana,
                                     1);
                    } else if(materialSeleccionado.equals("Madera")) {
                        spinnerGive = 
                                    new Spinner(0, 
                                    Partida.jugadores[Partida.miTurno -1].recursos.madera,
                                     1);
                    } else if(materialSeleccionado.equals("Mineral")) {
                        spinnerGive = 
                                    new Spinner(0,
                                    Partida.jugadores[Partida.miTurno -1].recursos.mineral,
                                    1);
                    } else if(materialSeleccionado.equals("Cereal")) {
                        spinnerGive = 
                                    new Spinner(0,
                                    Partida.jugadores[Partida.miTurno -1].recursos.cereales,
                                    1);
                    } else {
                        spinnerGive = 
                                    new Spinner(0,
                                    Partida.jugadores[Partida.miTurno -1].recursos.arcilla,
                                     1);
                    }
                    spinnerGive.setStyle("-fx-background-radius: 12px;" );
                    spinnerGive.setPrefSize(75, 25);
                    spinnerGive.setLayoutX(anchorPane.getLayoutX() + 270 );
                    spinnerGive.setLayoutY(anchorPane.getLayoutY() + 290);
                    anchorPane.getChildren().add(spinnerGive);
                }
            });
         }
        );
 
        // Selecciona un material para tradeo (recibir)
        Text treceiveMaterial = new Text(10, 50, "Material solicitado");
        treceiveMaterial.setFont(new Font(20));
        treceiveMaterial.setLayoutX(anchorPane.getLayoutX() + 10 );
        treceiveMaterial.setLayoutY(anchorPane.getLayoutY() + 343);
        treceiveMaterial.setFill(Color.WHITE);
        anchorPane.getChildren().add(treceiveMaterial);
        
        // Material solicitado
         // TODO: Listener
         receiveMaterial = new ChoiceBox<>();
         receiveMaterial.setStyle("-fx-background-radius: 12px;" );
         receiveMaterial.getItems().add(LangService.getMapping("clay"));
         receiveMaterial.getItems().add(LangService.getMapping("wood"));
         receiveMaterial.getItems().add(LangService.getMapping("cereal"));
         receiveMaterial.getItems().add(LangService.getMapping("wool"));
         receiveMaterial.getItems().add(LangService.getMapping("mineral"));
         receiveMaterial.setValue(LangService.getMapping("wool"));
         receiveMaterial.setLayoutX(anchorPane.getLayoutX() + 270);
         receiveMaterial.setLayoutY(anchorPane.getLayoutY() + 373);
         anchorPane.getChildren().add(receiveMaterial);

         // Cantidad de material ofrecido
         Text treceiveAmmount = new Text(10, 50, "Cantidad solicitada");
         treceiveAmmount.setFont(new Font(20));
         treceiveAmmount.setLayoutX(anchorPane.getLayoutX() + 10 );
         treceiveAmmount.setLayoutY(anchorPane.getLayoutY() + 423);
         treceiveAmmount.setFill(Color.WHITE);
         anchorPane.getChildren().add(treceiveAmmount);
 
 
        // Spinner cantidad material solicitado
        spinnerReceive = new Spinner(0, 250, 1);
        spinnerReceive.setStyle("-fx-background-radius: 12px;" );
        spinnerReceive.setPrefSize(75, 25);
        spinnerReceive.setLayoutX(anchorPane.getLayoutX() + 270 );
        spinnerReceive.setLayoutY(anchorPane.getLayoutY() + 453);
        anchorPane.getChildren().add(spinnerReceive);
   
        receiveMaterial.getSelectionModel().selectedItemProperty().addListener( (v, oldValue, newValue) -> 
         {
            receiveMaterial.setValue(newValue);
            final String materialSeleccionado =  new String(receiveMaterial.getValue());
            String name = offerPlayer.getValue();
            Integer player;
            if(name.equals(Partida.jugadores[0].nombre)) {
                player = 0; // Jugador 1
            } else if(name.equals(Partida.jugadores[1].nombre)) {
                player = 1; // Jugador 2
            } else if(name.equals(Partida.jugadores[2].nombre)) {
                player = 2; // Jugador 3
            } else {
                player = 3; // Jugador 4
            }
            final Integer arcilla_aux = Partida.jugadores[player].recursos.arcilla;
            final Integer madera_aux = Partida.jugadores[player].recursos.madera;
            final Integer cereal_aux =  Partida.jugadores[player].recursos.cereales;
            final Integer mineral_aux = Partida.jugadores[player].recursos.mineral;
            final Integer lana_aux = Partida.jugadores[player].recursos.lana;
            
            Platform.runLater(new Runnable() {
                @Override public void run() {
                    anchorPane.getChildren().remove(spinnerReceive);
                    if(materialSeleccionado.equals("Lana")) {
                        spinnerReceive = 
                                    new Spinner(0,lana_aux,1);
                    } else if(materialSeleccionado.equals("Madera")) {
                        spinnerReceive = 
                                    new Spinner(0,madera_aux,1);
                    } else if(materialSeleccionado.equals("Mineral")) {
                        spinnerReceive = 
                                    new Spinner(0,mineral_aux,1);
                    } else if(materialSeleccionado.equals("Cereal")) {
                        spinnerReceive = 
                                    new Spinner(0,cereal_aux,1);
                    } else {
                        spinnerReceive = 
                                    new Spinner(0,arcilla_aux,1);
                    }
                    spinnerReceive.setStyle("-fx-background-radius: 12px;" );
                    spinnerReceive.setPrefSize(75, 25);
                    spinnerReceive.setLayoutX(anchorPane.getLayoutX() + 270 );
                    spinnerReceive.setLayoutY(anchorPane.getLayoutY() + 453);
                    anchorPane.getChildren().add(spinnerReceive);
                }
            });
         }
        );


        // Boton enviar solicitud tradeo
        sendTrade = new Button();
        sendTrade.setPrefSize(180,90);
        sendTrade.setLayoutX(anchorPane.getLayoutX() + 100);
        sendTrade.setLayoutY(anchorPane.getLayoutY() + 520);
        sendTrade.setStyle("-fx-background-color: #c7956d; -fx-background-radius: 12px");
        sendTrade.setText("Aceptar");
        DropShadow shadow = new DropShadow();
        sendTrade.setEffect(shadow);
       
        // TODO: Añadir accion cuando se hace click sobre boton compra
        sendTrade.setOnAction((ActionEvent event) -> {
            enviarMensajeComercioInterno(offerPlayer.getValue(),
                                         offerMaterial.getValue(),
                                         spinnerGive.getValue(),
                                         receiveMaterial.getValue(),
                                         spinnerReceive.getValue());
            popupInternalTrade.hide();
        });
        anchorPane.getChildren().add(sendTrade);

        offerPlayer.getSelectionModel().selectedItemProperty().addListener( (v, oldValue, newValue) -> 
        {
               String name = (String) newValue;
               Integer player;
               if(name.equals(Partida.jugadores[0].nombre)) {
                   player = 0;
               } else if(name.equals(Partida.jugadores[1].nombre)) {
                   player = 1;
               } else if(name.equals(Partida.jugadores[2].nombre)) {
                   player = 2;
               } else {
                   player = 3;
               }
                final Integer  arcilla_aux = 
                                    Partida.jugadores[player].recursos.arcilla;
                final Integer  madera_aux  = 
                                    Partida.jugadores[player].recursos.madera;
                final Integer  cereal_aux  = 
                                    Partida.jugadores[player].recursos.cereales;
                final Integer mineral_aux = 
                                    Partida.jugadores[player].recursos.mineral;
                final Integer lana_aux    = 
                                    Partida.jugadores[player].recursos.lana;
               
                final String materialSeleccionado =  new String(receiveMaterial.getValue());
                offerPlayer.setValue(newValue);

                //TODO: Añadir comprobacion en español
                Platform.runLater(new Runnable() {
                    @Override public void run() {
                        anchorPane.getChildren().remove(spinnerReceive);
                        if(materialSeleccionado.equals("Lana")) {
                            spinnerReceive = 
                                        new Spinner(0, lana_aux, 1);
                        } else if(materialSeleccionado.equals("Madera")) {
                            spinnerReceive = 
                                        new Spinner(0, madera_aux, 1);
                        } else if(materialSeleccionado.equals("Mineral")) {
                            spinnerReceive = 
                                        new Spinner(0, mineral_aux, 1);
                        } else if(materialSeleccionado.equals("Cereal")) {
                            spinnerReceive = 
                                        new Spinner(0, cereal_aux, 1);
                        } else {
                            spinnerReceive = 
                                        new Spinner(0, arcilla_aux, 1);
                        }
                        spinnerReceive.setStyle("-fx-background-radius: 12px;" );
                        spinnerReceive.setPrefSize(75, 25);
                        spinnerReceive.setLayoutX(anchorPane.getLayoutX() + 270 );
                        spinnerReceive.setLayoutY(anchorPane.getLayoutY() + 453);
                        anchorPane.getChildren().add(spinnerReceive);
                    }
                });

        }
       );

       // Boton para acceder al popup
       // Hacer comprobacion fuera para bloquear si no es su turno o ya comercio
       inTrade.setOnAction((ActionEvent event) -> {
        
        if (!popupInternalTrade.isShowing()) {
            inTradePopUp();
            Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            popupInternalTrade.show(stage);
        }
        
     });
     inTrade.setText((LangService.getMapping("internal_trade")));


    }

    private static void enviarMensajeComercioInterno(String jugador,
                            String materialOfrece, Integer cantidadOfrece, 
                            String materialSolicita,Integer cantidadSolicita) {

        int player = 0;
        if(jugador.equals(Partida.jugadores[0].nombre)) {
            player = 1;
        } else if(jugador.equals(Partida.jugadores[1].nombre)) {
            player = 2;
        } else if(jugador.equals(Partida.jugadores[2].nombre)) {
            player = 3;
        } else {
            player = 4;
        }
    
        JSONObject mensaje = new JSONObject();
        JSONObject res1 = new JSONObject();
        if(materialOfrece.equals("Lana")) {
            res1.put("type","lana");
        } else if(materialOfrece.equals("Mineral")) {
            res1.put("type","mineral");
        } else if(materialOfrece.equals("Cereal")) {
            res1.put("type","cereales");
        } else if(materialOfrece.equals("Madera")) {
            res1.put("type","madera");
        } else {
            res1.put("type","arcilla");
        }
        res1.put("cuan",cantidadOfrece);
        JSONObject res2 = new JSONObject();
        if(materialSolicita.equals("Lana")) {
            res2.put("type","lana");
        } else if(materialSolicita.equals("Mineral")) {
            res2.put("type","mineral");
        } else if(materialSolicita.equals("Cereal")) {
            res2.put("type","cereales");
        } else if(materialSolicita.equals("Madera")) {
            res2.put("type","madera");
        } else {
            res2.put("type","arcilla");
        }
        res2.put("cuan",cantidadSolicita);
        mensaje.put("from", Partida.miTurno);
        mensaje.put("to", player);
        mensaje.put("game", Partida.id);
        mensaje.put("res1",res1);
        mensaje.put("res2",res2);
        System.out.println(mensaje.toString(4));
        ws.session.send(ws.proponerComercio, mensaje.toString());


    }

    private static void newTradePopUp() {
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefSize(150, 150);
        anchorPane.setStyle("-fx-background-color:  #965d62; -fx-background-radius: 12px" );
        popupNewTradeOffer = new Popup();
        popupNewTradeOffer.getContent().add(anchorPane);
        popupNewTradeOffer.setAutoHide(true);

        Text title = new Text(10, 20, Partida.jugadores[SolicitudComercio.from-1].nombre + 
         " ofrece " + SolicitudComercio.res1.cuan.toString() + " de " + 
         SolicitudComercio.res1.recurso + " por " + 
         SolicitudComercio.res2.cuan.toString() + " de " +
         SolicitudComercio.res2.recurso);
        title.setFont(new Font(20));
        title.setLayoutX(anchorPane.getLayoutX());
        title.setLayoutY(anchorPane.getLayoutY() + 20);
        title.setFill(Color.WHITE);
        anchorPane.getChildren().add(title);

        // Boton aceptar solicitud tradeo
        Button accepTrade = new Button();
        accepTrade.setPrefSize(90,90);
        accepTrade.setLayoutX(anchorPane.getLayoutX() + 80);
        accepTrade.setLayoutY(anchorPane.getLayoutY() + 50);
        accepTrade.setStyle("-fx-background-color: #c7956d; -fx-background-radius: 12px");
        accepTrade.setText("Aceptar");
        accepTrade.setOnAction((ActionEvent event) -> {
           aceptarComercioJugador();
            popupNewTradeOffer.hide();
        });


        DropShadow shadow = new DropShadow();
        accepTrade.setEffect(shadow);


        // Boton aceptar solicitud tradeo
        Button denegarTrade = new Button();
        denegarTrade.setPrefSize(90,90);
        denegarTrade.setLayoutX(anchorPane.getLayoutX() + 240);
        denegarTrade.setLayoutY(anchorPane.getLayoutY() + 50);
        denegarTrade.setStyle("-fx-background-color: #c7956d; -fx-background-radius: 12px");
        denegarTrade.setText("Denegar");
        denegarTrade.setEffect(shadow);
        denegarTrade.setOnAction((ActionEvent event) -> {
            rechazarComercioJugador();
             popupNewTradeOffer.hide();
         });

        anchorPane.getChildren().add(accepTrade);
        anchorPane.getChildren().add(denegarTrade);

    } 


    private static void aceptarComercioJugador() {
        if(Partida.miTurno != Partida.turnoActual) {
            JSONObject mensaje = new JSONObject();
            
            JSONObject res1 = new JSONObject();
            res1.put("type", SolicitudComercio.res1.recurso);
            res1.put("cuan", SolicitudComercio.res1.cuan);

            JSONObject res2 = new JSONObject();
            res2.put("type", SolicitudComercio.res2.recurso);
            res2.put("cuan", SolicitudComercio.res2.cuan);

            mensaje.put("from", Partida.miTurno);
            mensaje.put("to", SolicitudComercio.from);
            mensaje.put("game", Partida.id);
            mensaje.put("res1",res1);
            mensaje.put("res2",res2);


            ws.session.send(ws.aceptarComercio, mensaje.toString());
        }
    }

    private static void rechazarComercioJugador() {
        JSONObject mensaje = new JSONObject();
        mensaje.put("from", Partida.miTurno);
        mensaje.put("to", SolicitudComercio.from);
        mensaje.put("game", Partida.id);
      
        ws.session.send(ws.rechazarComercio, mensaje.toString());
    }

    private void externalTradePopUp() {
        System.out.println("AnchorPane");
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefSize(500, 400);
        anchorPane.setStyle("-fx-background-color:  #965d62; -fx-background-radius: 12px" );
        popupExternalTrade = new Popup();
        popupExternalTrade.getContent().add(anchorPane);
        popupExternalTrade.setAutoHide(true);

        // Titulo
        System.out.println("titulo");
        Text title = new Text(10, 50, (LangService.getMapping("external_trade")));
        title.setFont(new Font(40));
        title.setLayoutX(anchorPane.getLayoutX() + 55 );
        title.setLayoutY(anchorPane.getLayoutY() + 50);
        title.setFill(Color.WHITE);
        anchorPane.getChildren().add(title);


        // Selecciona una material
        System.out.println("Selecciona una material");
        Text tratio = new Text(10, 50, "Material solicitado");
        tratio.setFont(new Font(20));
        tratio.setLayoutX(anchorPane.getLayoutX() + 10 );
        tratio.setLayoutY(anchorPane.getLayoutY() + 100);
        tratio.setFill(Color.WHITE);
        anchorPane.getChildren().add(tratio);


        // Select material solicitado
        System.out.println("ChoiceBox");
        ratio = new ChoiceBox<>();
        ratio.setStyle("-fx-background-radius: 12px;" );
        ratio.getItems().add(LangService.getMapping("clay") );
        ratio.getItems().add(LangService.getMapping("wood") );
        ratio.getItems().add(LangService.getMapping("cereal"));
        ratio.getItems().add(LangService.getMapping("wool") );
        ratio.getItems().add(LangService.getMapping("mineral"));
        ratio.setValue(LangService.getMapping("wool"));
        ratio.setLayoutX(anchorPane.getLayoutX() + 270);
        ratio.setLayoutY(anchorPane.getLayoutY() + 127);
        anchorPane.getChildren().add(ratio);


        ratio.getSelectionModel().selectedItemProperty().addListener( (v, oldValue, newValue) -> 
       {
        ratio.setValue(newValue);
       }
       );

        // Lana
        System.out.println("Lana");
        Text lanaText = new Text(10, 50,LangService.getMapping("wool") );
        lanaText.setFont(new Font(20));
        lanaText.setLayoutX(anchorPane.getLayoutX() + 10 );
        lanaText.setLayoutY(anchorPane.getLayoutY() + 150);
        lanaText.setFill(Color.WHITE);
        anchorPane.getChildren().add(lanaText);

        Spinner<Integer> spinnerLana = new Spinner(0,
                        Partida.jugadores[Partida.miTurno -1].recursos.lana,1);
        spinnerLana.setStyle("-fx-background-radius: 12px;" );
        spinnerLana.setPrefSize(75, 25);
        spinnerLana.setLayoutX(anchorPane.getLayoutX() + 270 );
        spinnerLana.setLayoutY(anchorPane.getLayoutY() + 180);
        anchorPane.getChildren().add(spinnerLana);


         // Madera
         System.out.println("Madera");
         Text maderaText = new Text(10, 50,LangService.getMapping("wood") );
         maderaText.setFont(new Font(20));
         maderaText.setLayoutX(anchorPane.getLayoutX() + 10 );
         maderaText.setLayoutY(anchorPane.getLayoutY() + 180);
         maderaText.setFill(Color.WHITE);
         anchorPane.getChildren().add(maderaText);

        Spinner<Integer> spinnerMadera = new Spinner(0,
        Partida.jugadores[Partida.miTurno -1].recursos.madera,1);
        spinnerMadera.setStyle("-fx-background-radius: 12px;" );
        spinnerMadera.setPrefSize(75, 25);
        spinnerMadera.setLayoutX(anchorPane.getLayoutX() + 270 );
        spinnerMadera.setLayoutY(anchorPane.getLayoutY() + 210);
        anchorPane.getChildren().add(spinnerMadera);

      
        // Cereal
        System.out.println("Cereal");
        Text cerealText = new Text(10, 50,LangService.getMapping("cereal") );
        cerealText.setFont(new Font(20));
        cerealText.setLayoutX(anchorPane.getLayoutX() + 10 );
        cerealText.setLayoutY(anchorPane.getLayoutY() + 210);
        cerealText.setFill(Color.WHITE);
        anchorPane.getChildren().add(cerealText);

        Spinner<Integer> spinnerCereal = new Spinner(0,
        Partida.jugadores[Partida.miTurno -1].recursos.cereales,1);
        spinnerCereal.setStyle("-fx-background-radius: 12px;" );
        spinnerCereal.setPrefSize(75, 25);
        spinnerCereal.setLayoutX(anchorPane.getLayoutX() + 270 );
        spinnerCereal.setLayoutY(anchorPane.getLayoutY() + 240);
        anchorPane.getChildren().add(spinnerCereal);


        // Mineral
        System.out.println("Mineral");
        Text mineralText = new Text(10, 50,LangService.getMapping("mineral") );
        mineralText.setFont(new Font(20));
        mineralText.setLayoutX(anchorPane.getLayoutX() + 10 );
        mineralText.setLayoutY(anchorPane.getLayoutY() + 240);
        mineralText.setFill(Color.WHITE);
        anchorPane.getChildren().add(mineralText);

        
        Spinner<Integer> spinnerMineral = new Spinner(0,
        Partida.jugadores[Partida.miTurno -1].recursos.mineral,1);
        spinnerMineral.setStyle("-fx-background-radius: 12px;" );
        spinnerMineral.setPrefSize(75, 25);
        spinnerMineral.setLayoutX(anchorPane.getLayoutX() + 270 );
        spinnerMineral.setLayoutY(anchorPane.getLayoutY() + 270);
        anchorPane.getChildren().add(spinnerMineral);

        // Arcilla
        System.out.println("Arcilla");
        Text arcillaText = new Text(10, 50,LangService.getMapping("clay") );
        arcillaText.setFont(new Font(20));
        arcillaText.setLayoutX(anchorPane.getLayoutX() + 10 );
        arcillaText.setLayoutY(anchorPane.getLayoutY() + 270);
        arcillaText.setFill(Color.WHITE);
        anchorPane.getChildren().add(arcillaText);

        
        Spinner<Integer> spinnerArcilla = new Spinner(0,
        Partida.jugadores[Partida.miTurno -1].recursos.arcilla,1);
        spinnerArcilla.setStyle("-fx-background-radius: 12px;" );
        spinnerArcilla.setPrefSize(75, 25);
        spinnerArcilla.setLayoutX(anchorPane.getLayoutX() + 270 );
        spinnerArcilla.setLayoutY(anchorPane.getLayoutY() + 300);
        anchorPane.getChildren().add(spinnerArcilla);

        // Boton enviar solicitud tradeo
        System.out.println("Boton enviar solicitud tradeo");
        sendTradeExternal = new Button();
        sendTradeExternal.setPrefSize(200,40);
        sendTradeExternal.setLayoutX(anchorPane.getLayoutX() + 150);
        sendTradeExternal.setLayoutY(anchorPane.getLayoutY() + 350);
        sendTradeExternal.setStyle("-fx-background-color: #c7956d; -fx-background-radius: 12px");
        sendTradeExternal.setText(LangService.getMapping("send"));
        DropShadow shadow = new DropShadow();
        sendTradeExternal.setEffect(shadow);



        // TODO: Añadir accion cuando se hace click sobre boton compra
        sendTradeExternal.setOnAction((ActionEvent event) -> {
            crearMensajeComercioPuerto(idPuerto,
            spinnerArcilla.getValue(), spinnerCereal.getValue(),
            spinnerLana.getValue(),spinnerMadera.getValue(),
            spinnerMineral.getValue(),ratio.getValue());

            popupExternalTrade.hide();
        });

        anchorPane.getChildren().add(sendTradeExternal);
    }

    private static void crearMensajeComercioPuerto(Integer idPuerto, 
    Integer numArcilla, Integer numCereal, Integer numLana, Integer numMadera,
    Integer numMineral, String recursoSolicitado) {
        JSONObject mensaje = new JSONObject();
        JSONObject materiales = new JSONObject();
        mensaje.put("id_puerto", idPuerto);
        materiales.put("madera", numMadera);
        materiales.put("lana", numLana);
        materiales.put("arcilla", numArcilla);
        materiales.put("mineral", numMineral);
        materiales.put("cereales", numCereal);
        mensaje.put("materiales",materiales);

        //TODO: Añadir en ingles
        if(recursoSolicitado.equals("Lana")) {
            mensaje.put("material_que_recibe","lana");
        } else if(recursoSolicitado.equals("Mineral")) {
            mensaje.put("material_que_recibe","mineral");
        } else if(recursoSolicitado.equals("Cereal")) {
            mensaje.put("material_que_recibe","cereales");
        } else if(recursoSolicitado.equals("Madera")) {
            mensaje.put("material_que_recibe","madera");
        } else {
            mensaje.put("material_que_recibe","arcilla");
        }

        JSONObject jugada = new JSONObject();
        JSONObject move = new JSONObject();
        move.put("name", Jugada.COMERCIAR_PUERTO);
        move.put("param", mensaje);
        jugada.put("player", Partida.miTurno);
        jugada.put("game", Partida.id);
        jugada.put("move",move);

        ws.session.send(ws.partidaJugada, jugada.toString());

    }

    private void settingsPopup() throws IOException {
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefSize(200, 250);
        anchorPane.setStyle("-fx-background-color:  #965d62; -fx-background-radius: 12px" );
        popupSettings = new Popup();
        popupSettings.getContent().add(anchorPane);
        popupSettings.setAutoHide(true);

        leaveGame = new Button();
        leaveGame.setPrefSize(150,90);
        leaveGame.setLayoutX(anchorPane.getLayoutX() + 25);
        leaveGame.setLayoutY(anchorPane.getLayoutY() + 30);
        leaveGame.setStyle("-fx-background-color: #c7956d; -fx-background-radius: 12px");
        leaveGame.setText("Abandonar partida");
        DropShadow shadow = new DropShadow();
        leaveGame.setEffect(shadow);
        anchorPane.getChildren().add(leaveGame);

        stopGame = new Button();
        stopGame.setPrefSize(150,90);
        stopGame.setLayoutX(anchorPane.getLayoutX() + 25);
        stopGame.setLayoutY(anchorPane.getLayoutY() + 140);
        stopGame.setStyle("-fx-background-color: #c7956d; -fx-background-radius: 12px");
        stopGame.setText("Solicitud fin partida");
        stopGame.setEffect(shadow);
        anchorPane.getChildren().add(stopGame);


        leaveGame.setOnAction((ActionEvent event) -> {
            try {
                popupSettings.hide();
                App.nuevaPantalla("/view/mainMenu.fxml");
            } catch (IOException e) {
        
            }
            
        });

        stopGame.setOnAction((ActionEvent event) -> {
            popupSettings.hide();
        });

        settingsButton.setOnAction((ActionEvent event) -> {
            if (!popupSettings.isShowing()) {
                Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
                popupSettings.show(stage);
            }
         });
        settingsButton.setText((LangService.getMapping("settings"))); 
         
    }

    private static void updateDice() {
        Platform.runLater(new Runnable() {
            @Override public void run() {
            _diceButton.setText(LangService.getMapping("dice") + ": " + 
                    Partida.resultadoTirada.toString());
            }
        });

       
    }

    private static void updateUserMaterials() {
        Platform.runLater(new Runnable() {
            @Override public void run() {
                _maderaCant.setText(Partida.jugadores[Partida.miTurno-1].recursos.madera.toString());
                _mineralCant.setText(Partida.jugadores[Partida.miTurno-1].recursos.mineral.toString());
                _arcillaCant.setText(Partida.jugadores[Partida.miTurno-1].recursos.arcilla.toString());
                _lanaCant.setText(Partida.jugadores[Partida.miTurno-1].recursos.lana.toString());
                _cerealesCant.setText(Partida.jugadores[Partida.miTurno-1].recursos.cereales.toString()); 
            }
        });

       
    }

    private static void reportarJugador(Integer playerId) {
        JSONObject report = new JSONObject();
        report.put("from", UserService.getUsername());
        report.put("to", Partida.jugadores[playerId].nombre);
        ws.session.send(ws.usuarioReportar, report.toString());
    }

    @FXML
    public void initialize() throws IOException {
        System.out.println("Initialize");
         _player1Name = player1Name; 
         _player2Name = player2Name; 
         _player3Name = player3Name; 
         _player4Name = player4Name; 
         _diceButton = diceButton;
         _maderaCant = maderaCant;
         _mineralCant = mineralCant;
         _arcillaCant = arcillaCant;
         _lanaCant = lanaCant;
         _cerealesCant = cerealesCant;
         _chatContent = chatContent;
         _turnPlayer = turnPlayer;
         _passTurnButton = passTurnButton;
         _mainAnchor = mainAnchor;

         System.out.println("Pass Turn Button");
         passTurnButton.setOnAction((ActionEvent event) -> {
            if(esMiTurno()) {
                JSONObject jugada = new JSONObject();
                JSONObject move = new JSONObject();
                move.put("name", Jugada.PASAR_TURNO);
                move.put("param", "");
                jugada.put("player", Partida.miTurno);
                jugada.put("game", Partida.id);
                jugada.put("move",move);
                ws.session.send(ws.partidaJugada, jugada.toString());
            }
        });

        System.out.println("Pass Turn Button");
        reportPLayer1.setOnAction((ActionEvent event) -> {
                reportarJugador(0);
                reportPLayer1.setDisable(true);
         });

        reportPLayer2.setOnAction((ActionEvent event) -> {
                reportarJugador(1);
                reportPLayer2.setDisable(true);
         });


        reportPLayer3.setOnAction((ActionEvent event) -> {
                reportarJugador(2);
                reportPLayer3.setDisable(true);
         });

        reportPLayer4.setOnAction((ActionEvent event) -> {
                reportarJugador(3);
                reportPLayer4.setDisable(true);
         });

        reportPLayer1.setDisable(1 == Partida.miTurno);
        reportPLayer2.setDisable(2 == Partida.miTurno);
        reportPLayer3.setDisable(3 == Partida.miTurno);
        reportPLayer4.setDisable(4 == Partida.miTurno);
        
        reportPLayer1.setText(LangService.getMapping("report"));
        reportPLayer2.setText(LangService.getMapping("report"));
        reportPLayer3.setText(LangService.getMapping("report"));
        reportPLayer4.setText(LangService.getMapping("report"));

        System.out.println("Report system");
        chatContent.setEditable(false);
        chatContent.setMouseTransparent(true);
        chatContent.setFocusTraversable(false);  

        resourcesPopUp();
        //buildSettlementPopUp();
        //buildRoadPopUp();
        inTradePopUp();
        //externalTradePopUp();
        updateDice();
        System.out.println("updateDice");
        settingsPopup();
        System.out.println("settingsPopup");
        passTurnButton.setText((LangService.getMapping("next_turn")));
        cards.setText(LangService.getMapping("player_resources"));

        System.out.println("Cartas xD");
        cards.setOnAction((ActionEvent event) -> {
            if (!popupResources.isShowing()) {
                Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
                popupResources.show(stage);
            }
        });
       
        System.out.println("Crear hexagonos");
        // Crear hexagonos
        Integer numberHexagonAux = 0;
        for(Integer i =0; i < 37; i++) {
            Polygon pol = createHexagon(i);
            fillHexagonSea(pol, i);    
            mainAnchor.getChildren().add(pol);
            if(hasRoads(i)) {
                Button circle = new Button();
                circle.setShape(new Circle(numberSize));
                circle.setMinSize(2*numberSize, 2*numberSize);
                circle.setMaxSize(2*numberSize, 2*numberSize);
                circle.setLayoutX(pol.getLayoutX() - 18);
                circle.setLayoutY(pol.getLayoutY() - 20);
               
                circle.setOnMouseClicked( event -> {
                    if (event.getButton().equals(MouseButton.PRIMARY) && 
                        esMiTurno() && Partida.resultadoTirada == 7 && 
                        !Partida.movioLadron) {
                        Partida.movioLadron = true;
                        circle.setDisable(true);
                        for(int j = 0; j < numberofHexagons; j++) {
                            Button aux = Partida.tablero.hexagonos.numberOverHexagon[j];
                            if(aux != circle) {
                                aux.setDisable(false);
                            } else {
                                JSONObject jugada = new JSONObject();
                                JSONObject move = new JSONObject();
                                move.put("name", Jugada.MOVER_LADRON);
                                move.put("param", j);
                                jugada.put("player", Partida.miTurno);
                                jugada.put("game", Partida.id);
                                jugada.put("move",move);
                                ws.session.send(ws.partidaJugada, jugada.toString());
                            }
                        }
                

                        
                    }   
                });

                
                mainAnchor.getChildren().add(circle); 
                Partida.tablero.hexagonos.hexagons[numberHexagonAux] = pol;
                Partida.tablero.hexagonos.numberOverHexagon[numberHexagonAux] = circle;
                numberHexagonAux++;
            }
            assignRoads(pol, i);
            assignSettlements(pol,i);        
        }

        System.out.println("Hexagonos colocados");
    } 
}

