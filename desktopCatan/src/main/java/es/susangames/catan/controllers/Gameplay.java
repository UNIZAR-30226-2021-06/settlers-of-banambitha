package es.susangames.catan.controllers;

import es.susangames.catan.service.LangService;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.*;
import es.susangames.catan.App;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.util.Random;

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

    private Image imgSea, imgDes, imgMou, imgFie, imgFor, imgHil; 
 
    @FXML
    private AnchorPane mainAnchor;

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
    private Button outTrade; 

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

    // Elementos graficos adicionales
    private static Popup popupCards;
    private Popup popupInternalTrade;
    private Popup popupExternalTrade;
    private Popup popupSettings;
    private Popup popupBuildSettle;
    private ChoiceBox<String> offerMaterial;
    private ChoiceBox<String> offerPlayer;
    private ChoiceBox<String> receiveMaterial;
    private ChoiceBox<String> receivePlayer;
    private ChoiceBox<String> ratio;
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

    public static Boolean comenzarPartida(String idPartida, String[] jugadores) {
        Integer miTurno  = Arrays.asList(jugadores).indexOf(UserService.getUsername());
       
        if (jugadores.length == numberPlayers && miTurno >= 0) {
            esperandoTableroInicial = true;
            Partida.miTurno = miTurno++;
            Partida.id = idPartida;
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
            subscribeToTopics();
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
        if(Partida.id != null) {
            //Suscripción a los mensajes de la partida (acciones de los jugadores)
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
            ws.session.subscribe( ws.partida_chat_topic  + Partida.id, new StompFrameHandler() {
                @Override
                public Type getPayloadType(StompHeaders headers) {
                    return String.class;
                }
                @Override
                public void handleFrame(StompHeaders headers, Object payload) {
                    System.out.println("msg chat");
                }
            });
 
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
        }
        if(esperandoTableroInicial) {
            esperandoTableroInicial = false;
            try {
                App.nuevaPantalla("/view/gameplay.fxml");
            } catch(Exception e) {}
        }
        mostrarCambiosTablero();
        if(existeGanador(mensaje)) { 
            // TODO: Fin partida
        } 
        
    }

    private static Boolean existeGanador(String mensaje) {
        return false;
    }

    private static void actualizarPartida(String partida) throws Exception {
        JSONObject object = new JSONObject(partida);

        if(!object.get(MessageKeys.CLOCK).equals(null) &&
            object.getInt(MessageKeys.CLOCK) > Partida.clock) {

            Partida.clock =  object.getInt(MessageKeys.CLOCK);
            if(object.getInt(MessageKeys.EXIT_STATUS) <= 0) {
                // TODO: Enviar mensaje al chat de la partida


                // Nombre y miTurno
                try {
                    if(!object.get(MessageKeys.PLAYER_NAMES).equals(null)) {
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
                }
                 
                // Turno jugador
                try {
                    if(!object.get(MessageKeys.TURNO_JUGADOR).equals(null)) {
                        int turnoActual = object.getInt(MessageKeys.TURNO_JUGADOR);
                        Partida.turnoActual = turnoActual + 1 ;
                    } 
                } catch(Exception e) {
                    System.err.println("Falta el turno de la jugada");
                }
               
                // Turno acumulado
                try {
                    if(!object.get(MessageKeys.TURNO_ACUM).equals(null)) {
                        Partida.totalTurnos = object.getInt(MessageKeys.TURNO_ACUM);
                    }
                } catch(Exception e) {
                    System.err.println("Faltan los turnos totales");
                }
                 
                //Resultado tirada
                try  {
                    if(!object.get(MessageKeys.RESULTADO_TIRADA).equals(null)) {
                        Partida.resultadoTirada = object.getInt(MessageKeys.RESULTADO_TIRADA);
                    }
                } catch(Exception e) {
                    System.err.println("Falta el resultado de la tirada");
                }
            
                //Tablero
                try {
                    if(!object.get(MessageKeys.TAB_INFO).equals(null)) {
                        actualizarTablero(
                            new JSONObject(object.get(MessageKeys.TAB_INFO).toString()));
                    }
                } catch(Exception e) {
                    System.err.println("Falta la información del tablero");
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
                }

                //Pre-calculos
                actualizarPrecalculos();

            } else {
                // TODO: Recibido codigo erroneo
                _chatContent.appendText(object.getString(
                            MessageKeys.MENSAJE) + "\n");
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
        Partida.jugadores[player].recursos.mineral = recursosArr.getInt(1);
        Partida.jugadores[player].recursos.arcilla = recursosArr.getInt(2);
        Partida.jugadores[player].recursos.lana = recursosArr.getInt(3);
        Partida.jugadores[player].recursos.cereales = recursosArr.getInt(4);
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
                        primerosAsentamientos.getBoolean(MessageKeys.PLAYER_3);
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
                        primerosCaminos.getBoolean(MessageKeys.PLAYER_3);
            }
        } catch (Exception e) {
            System.err.println("Error procesando primeros caminos");
        }
    }



    private static void actualizarTablero(JSONObject tablero) {
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
        }
       
        // Aristas
        // TODO: Actualizar puertos
        try {
            if(!tablero.get(MessageKeys.TAB_INFO_ARISTAS).equals(null)) {
                actualizarAristas(tablero.get(MessageKeys.TAB_INFO_ARISTAS).toString());
            } 
        } catch (Exception e) {
            System.err.println("Faltan las aristas en el mensaje");
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
        chatContent.appendText("User: " + chatInput.getText().toString() + "\n"); 
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

            pol.setFill(Color.DEEPSKYBLUE);
        } 
    }

    private static void updateHexagonBackground() {
        for(int i = 0; i < numberofHexagons; i++) {
            if (Partida.tablero.hexagonos.tipo[i].equals(TipoTerreno.BOSQUE) ) {
                Partida.tablero.hexagonos.hexagons[i].setFill(Color.CADETBLUE);
            } else if (Partida.tablero.hexagonos.tipo[i].equals(TipoTerreno.CERRO)) {
                Partida.tablero.hexagonos.hexagons[i].setFill(Color.ORANGERED);
            } else if (Partida.tablero.hexagonos.tipo[i].equals(TipoTerreno.DESIERTO)) {
                Partida.tablero.hexagonos.hexagons[i].setFill(Color.BLANCHEDALMOND);
            } else if (Partida.tablero.hexagonos.tipo[i].equals(TipoTerreno.MONTANYA)) {
                Partida.tablero.hexagonos.hexagons[i].setFill(Color.MAROON);
            } else if (Partida.tablero.hexagonos.tipo[i].equals(TipoTerreno.PASTO)) {
                Partida.tablero.hexagonos.hexagons[i].setFill(Color.GREEN);
            } else if (Partida.tablero.hexagonos.tipo[i].equals(TipoTerreno.SEMBRADO)) {
                Partida.tablero.hexagonos.hexagons[i].setFill(Color.PERU);
            } else {
                Partida.tablero.hexagonos.hexagons[i].setFill(Color.GRAY);
            }
            Partida.tablero.hexagonos.numberOverHexagon[i].setText(
                Partida.tablero.hexagonos.valor[i].toString());
        }    
        Partida.tablero.hexagonos.numberOverHexagon[Partida.tablero.hexagonos.ladron].setDisable(true);

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
            if(Partida.tablero.aristas.puertos.arcilla == i ||
               Partida.tablero.aristas.puertos.madera == i  ||
               Partida.tablero.aristas.puertos.mineral == i ||
               Partida.tablero.aristas.puertos.lana == i    ||
               Partida.tablero.aristas.puertos.cereal == i  || 
               Partida.tablero.aristas.puertos.basico[0] == i ||
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
                Partida.tablero.aristas.roadsType[pos].equals(TipoCamino.NADA)) {
                posRoad = pos;
                buildRoadPopUp();
                if (!popupCards.isShowing()) {
                    Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
                    popupCards.show(stage);
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

    // Click sobre una carretera
    private void onClickSettlement(Button circle,Integer position) {
        circle.setOnMouseClicked( event -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && 
                (!Partida.jugadores[Partida.miTurno -1].primerosAsentamientos) 
                ||
                (Partida.jugadores[Partida.miTurno -1].primerosCaminos && 
                 Partida.tablero.vertices.posible_asentamiento[position]) 
                ||
                (Partida.jugadores[Partida.miTurno -1].primerosCaminos &&
                 !Partida.tablero.vertices.posible_asentamiento[position] &&
                  Partida.tablero.vertices.settlementsType[position].equals(
                    miTipoAsentamiento()))) {
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

            } else if(Partida.tablero.vertices.settlementsType[posSettle].equals(
                miTipoCiudad())) {

                buildSettle.setText(LangService.getMapping(
                    LangService.getMapping("no_upgrade")));
                buildSettle.setDisable(true);

            } else {
                buildSettle.setText(LangService.getMapping("build_town"));
                buildSettle.setDisable(!Partida.PobladoDisponible);
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
            " (" + Partida.jugadores[Partida.miTurno-1].puntos + ")");
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
        offerPlayer.setStyle("-fx-background-radius: 12px;" );
        offerPlayer.getItems().add(player2);
        offerPlayer.getItems().add(player3);
        offerPlayer.getItems().add(player4);
        offerPlayer.setValue(player2);
        offerPlayer.setLayoutX(anchorPane.getLayoutX() + 270);
        offerPlayer.setLayoutY(anchorPane.getLayoutY() + 127);
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
        offerMaterial.getItems().add("Lana");
        offerMaterial.getItems().add("Madera");
        offerMaterial.getItems().add("Cereales");
        offerMaterial.getItems().add("Arcilla");
        offerMaterial.getItems().add("Mineral");
        offerMaterial.setValue("Lana");
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



         // Spinner cantidad material ofrecido
         Spinner<Integer> spinnerGive = new Spinner(1, 250, 1);
         spinnerGive.setStyle("-fx-background-radius: 12px;" );
         spinnerGive.setPrefSize(75, 25);
         spinnerGive.setLayoutX(anchorPane.getLayoutX() + 270 );
         spinnerGive.setLayoutY(anchorPane.getLayoutY() + 290);
         anchorPane.getChildren().add(spinnerGive);
 
        

        // Selecciona un material para tradeo (recibir)
        Text treceiveMaterial = new Text(10, 50, "Material solicitado");
        treceiveMaterial.setFont(new Font(20));
        treceiveMaterial.setLayoutX(anchorPane.getLayoutX() + 10 );
        treceiveMaterial.setLayoutY(anchorPane.getLayoutY() + 343);
        treceiveMaterial.setFill(Color.WHITE);
        anchorPane.getChildren().add(treceiveMaterial);
        
        
        // Material solicitado
         receiveMaterial = new ChoiceBox<>();
         receiveMaterial.setStyle("-fx-background-radius: 12px;" );
         receiveMaterial.getItems().add("Lana");
         receiveMaterial.getItems().add("Madera");
         receiveMaterial.getItems().add("Cereales");
         receiveMaterial.getItems().add("Arcilla");
         receiveMaterial.getItems().add("Mineral");
         receiveMaterial.setValue("Lana");
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
        Spinner<Integer> spinnerReceive = new Spinner(1, 250, 1);
        spinnerReceive.setStyle("-fx-background-radius: 12px;" );
        spinnerReceive.setPrefSize(75, 25);
        spinnerReceive.setLayoutX(anchorPane.getLayoutX() + 270 );
        spinnerReceive.setLayoutY(anchorPane.getLayoutY() + 453);
        anchorPane.getChildren().add(spinnerReceive);
   
        


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
            System.out.println(spinnerGive.getValue().toString());
            System.out.println(spinnerReceive.getValue().toString());
            popupInternalTrade.hide();
        });

        anchorPane.getChildren().add(sendTrade);


        // Boton para acceder al popup
        inTrade.setOnAction((ActionEvent event) -> {
            
            if (!popupInternalTrade.isShowing()) {
                Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
                popupInternalTrade.show(stage);
            }
            
         });
         
         inTrade.setText((LangService.getMapping("internal_trade")));
    }

    private void externalTradePopUp() {
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefSize(500, 400);
        anchorPane.setStyle("-fx-background-color:  #965d62; -fx-background-radius: 12px" );
        popupExternalTrade = new Popup();
        popupExternalTrade.getContent().add(anchorPane);
        popupExternalTrade.setAutoHide(true);

        // Titulo
       Text title = new Text(10, 50, (LangService.getMapping("external_trade")));
        title.setFont(new Font(40));
        title.setLayoutX(anchorPane.getLayoutX() + 55 );
        title.setLayoutY(anchorPane.getLayoutY() + 50);
        title.setFill(Color.WHITE);
        anchorPane.getChildren().add(title);


        // Selecciona una Ratio
        Text tratio = new Text(10, 50, "Seleccione una ratio");
        tratio.setFont(new Font(20));
        tratio.setLayoutX(anchorPane.getLayoutX() + 10 );
        tratio.setLayoutY(anchorPane.getLayoutY() + 100);
        tratio.setFill(Color.WHITE);
        anchorPane.getChildren().add(tratio);


        // Select jugador elegido
        ratio = new ChoiceBox<>();
        ratio.setStyle("-fx-background-radius: 12px;" );
        ratio.getItems().add("2:1");
        ratio.getItems().add("3:1");
        ratio.getItems().add("4:1");
        ratio.setValue("2:1");
        ratio.setLayoutX(anchorPane.getLayoutX() + 270);
        ratio.setLayoutY(anchorPane.getLayoutY() + 127);
        anchorPane.getChildren().add(ratio);


        // Selecciona un material para tradeo
        Text tofferMaterial = new Text(10, 50, "Material ofrecido");
        tofferMaterial.setFont(new Font(20));
        tofferMaterial.setLayoutX(anchorPane.getLayoutX() + 10 );
        tofferMaterial.setLayoutY(anchorPane.getLayoutY() + 150);
        tofferMaterial.setFill(Color.WHITE);
        anchorPane.getChildren().add(tofferMaterial);

        // Select material ofrecido
       offerMaterial = new ChoiceBox<>();
       offerMaterial.setStyle("-fx-background-radius: 12px;" );
       offerMaterial.getItems().add("Lana");
       offerMaterial.getItems().add("Madera");
       offerMaterial.getItems().add("Cereales");
       offerMaterial.getItems().add("Arcilla");
       offerMaterial.getItems().add("Mineral");
       offerMaterial.setValue("Lana");
       offerMaterial.setLayoutX(anchorPane.getLayoutX() + 270);
       offerMaterial.setLayoutY(anchorPane.getLayoutY() + 180);
       anchorPane.getChildren().add(offerMaterial);

        // Selecciona un material para tradeo (recibir)
        Text treceiveMaterial = new Text(10, 50, "Material solicitado");
        treceiveMaterial.setFont(new Font(20));
        treceiveMaterial.setLayoutX(anchorPane.getLayoutX() + 10 );
        treceiveMaterial.setLayoutY(anchorPane.getLayoutY() + 200);
        treceiveMaterial.setFill(Color.WHITE);
        anchorPane.getChildren().add(treceiveMaterial);
        
        
        // Material solicitado
         receiveMaterial = new ChoiceBox<>();
         receiveMaterial.setStyle("-fx-background-radius: 12px;" );
         receiveMaterial.getItems().add("Lana");
         receiveMaterial.getItems().add("Madera");
         receiveMaterial.getItems().add("Cereales");
         receiveMaterial.getItems().add("Arcilla");
         receiveMaterial.getItems().add("Mineral");
         receiveMaterial.setValue("Lana");
         receiveMaterial.setLayoutX(anchorPane.getLayoutX() + 270);
         receiveMaterial.setLayoutY(anchorPane.getLayoutY() + 230);
         anchorPane.getChildren().add(receiveMaterial);

        // Boton enviar solicitud tradeo
        sendTradeExternal = new Button();
        sendTradeExternal.setPrefSize(180,90);
        sendTradeExternal.setLayoutX(anchorPane.getLayoutX() + 160);
        sendTradeExternal.setLayoutY(anchorPane.getLayoutY() + 300);
        sendTradeExternal.setStyle("-fx-background-color: #c7956d; -fx-background-radius: 12px");
        sendTradeExternal.setText("Aceptar");
        DropShadow shadow = new DropShadow();
        sendTradeExternal.setEffect(shadow);



        // TODO: Añadir accion cuando se hace click sobre boton compra
        sendTradeExternal.setOnAction((ActionEvent event) -> {
            popupExternalTrade.hide();
        });

        anchorPane.getChildren().add(sendTradeExternal);

        // Boton para acceder al popup
        outTrade.setOnAction((ActionEvent event) -> {
            
            if (!popupExternalTrade.isShowing()) {
                Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
                popupExternalTrade.show(stage);
            }
            
         });
         
         outTrade.setText((LangService.getMapping("external_trade")));


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
        _diceButton.setText(LangService.getMapping("dice") + ": " + 
            Partida.resultadoTirada.toString());
    }

    private static void updateUserMaterials() {
        _maderaCant.setText(Partida.jugadores[Partida.miTurno-1].recursos.madera.toString());
        _mineralCant.setText(Partida.jugadores[Partida.miTurno-1].recursos.mineral.toString());
        _arcillaCant.setText(Partida.jugadores[Partida.miTurno-1].recursos.arcilla.toString());
        _lanaCant.setText(Partida.jugadores[Partida.miTurno-1].recursos.lana.toString());
        _cerealesCant.setText(Partida.jugadores[Partida.miTurno-1].recursos.cereales.toString()); 
    }

    @FXML
    public void initialize() throws IOException {
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
        chatContent.setEditable(false);
        chatContent.setMouseTransparent(true);
        chatContent.setFocusTraversable(false);  
        buildSettlementPopUp();
        buildRoadPopUp();
        inTradePopUp();
        externalTradePopUp();
        updateDice();
        settingsPopup();
        passTurnButton.setText((LangService.getMapping("next_turn")));
       
       
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
                    if (event.getButton().equals(MouseButton.PRIMARY)) {
                        circle.setDisable(true);
                        for(Button aux : Partida.tablero.hexagonos.numberOverHexagon) {
                            if(aux != circle) {
                                aux.setDisable(false);
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
    } 
}

