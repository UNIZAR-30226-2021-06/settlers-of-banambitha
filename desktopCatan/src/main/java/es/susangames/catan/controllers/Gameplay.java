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
    private static Color[]          coloresPorId = new Color[]{Color.BLUE, Color.RED, Color.YELLOW, Color.GREEN};
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

    protected static class MsgJugada {
        protected static Integer player;
        protected static String game;
        protected static String jugada;
        protected static Object param;
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
        protected static TipoTerreno[]    tipo;
        protected static Integer[]        valor;            
        protected static Integer          ladron;
        protected static Polygon[]        hexagons;         
        private   static Button[]         numberOverHexagon; 
    }

    protected static class Vertices {
        protected static Button[]    settlements;
        protected static Boolean[][] posible_asentamiento;
    } 

    protected static class Puerto {
        protected  static Integer madera;
        protected  static Integer mineral;
        protected  static Integer arcilla;
        protected  static Integer lana;
        protected  static Integer cereal;
        protected  static Integer basico;
    }

    protected static class Aristas {
        protected static ToggleButton[] roads;
        protected static Boolean[][] posible_camino;
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
        protected static Integer resultadoTirada;
        protected static Jugador[] jugadores;
        protected static Mensaje[] mensajes;
        protected static Integer clock;
        protected static Boolean PobladoDisponible;
        protected static Boolean CaminoDisponible;
        protected static Boolean CiudadDisponible;
        protected static Boolean movioLadron;
        protected static Tablero tablero;
    }

    private Image imgSea, imgDes, imgMou, imgFie, imgFor, imgHil; 
 
    @FXML
    private AnchorPane mainAnchor;

    @FXML
    private JFXTextArea chatContent;

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

    @FXML
    private Text player2Name;

    @FXML
    private Text player3Name;

    @FXML
    private Text player4Name;

    @FXML
    private Button diceButton;

    @FXML
    private Button settingsButton;

    @FXML
    private Button passTurnButton;

    // Elementos graficos adicionales
    private Popup popupCards;
    private Popup popupInternalTrade;
    private Popup popupExternalTrade;
    private Popup popupSettings;
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
    
    public Gameplay() {
        offerAmountInt = 1;
        receiveAmountInt = 1;
    }
    
    public static void comenzarPartidaPrueba(String msg) {
        JSONObject message = new JSONObject(msg);
        String aux = message.getString("game");
        System.out.println("holaaa1");
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
            Partida.clock = -1;
            System.out.println("holaaa5");
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
                    actualizarTablero(payload.toString());
                    if(esperandoTableroInicial) {
                        try {
                            App.nuevaPantalla("/view/gameplay.fxml");
                        } catch(Exception e) {}
                        esperandoTableroInicial = false;
                    }
                    System.out.println("msg");
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

    private static void initTablero() {
        Partida.tablero.hexagonos.tipo = new TipoTerreno[numberofHexagons];
        Partida.tablero.hexagonos.valor = new Integer[numberofHexagons];
        Partida.tablero.hexagonos.hexagons = new Polygon[numberofHexagons];
        Partida.tablero.hexagonos.numberOverHexagon = new Button[numberofHexagons];
        Partida.tablero.vertices.settlements = new Button[numberSettlements];
        Partida.tablero.aristas.roads = new ToggleButton[numberRoads];  
    }


    private static void actualizarTablero(String tablero) {
        if(esperandoTableroInicial) {
            Partida.tablero = new Tablero();
            initTablero();

        } else {

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

    private void fillHexagon(Polygon pol, Integer i) {
        if ( i <= 4 || i == 8 || i == 9 || i == 14 ||
        i == 15 || i == 21 || i == 22 || i == 27 ||
        i == 28 || i >= 32) {

            pol.setFill(Color.DEEPSKYBLUE);
        } 
        else {
            int var = i % 5;
            //TODO: Modificar para rellenar segun codigo backend
            // Version estatica para frontend
            if (var == 0) {
                pol.setFill(Color.BLANCHEDALMOND);
            } else if (var == 1) {
                pol.setFill(Color.ORANGERED);
            } else if (var == 2) {
                pol.setFill(Color.GREEN);
            } else if (var == 3) {
                pol.setFill(Color.MAROON);
            } else {
                pol.setFill(Color.GRAY);
            }
        }

    }

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
        Tablero.aristas.roads[pos] =  _road;
        setColorButonOnClick(_road, pos);
        return _road;
    }

    private ToggleButton createRoadSE(Polygon pol, int pos) {
        ToggleButton _roadSE = new ToggleButton();
        _roadSE.setPrefSize(1,30);
        _roadSE.setLayoutX(pol.getLayoutX() + 23);
        _roadSE.setLayoutY(pol.getLayoutY() - 66);
        _roadSE.setRotate(120);
        Tablero.aristas.roads[pos] =  _roadSE;
        setColorButonOnClick(_roadSE, pos);
        return _roadSE;
    }

    private ToggleButton createRoadNE(Polygon pol,  int pos) {
        ToggleButton _roadNE = new ToggleButton();
        _roadNE.setPrefSize(1,30);
        _roadNE.setLayoutX(pol.getLayoutX() - 40);
        _roadNE.setLayoutY(pol.getLayoutY() - 70);
        _roadNE.setRotate(60);
        Tablero.aristas.roads[pos] =  _roadNE;
        setColorButonOnClick(_roadNE, pos);
        return _roadNE;

    }

    private void setColorButonOnClick(ToggleButton button, int pos) {
        button.setOnMouseClicked( event -> {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                button.setStyle("-fx-background-color: red");
            }   else if (event.getButton() == MouseButton.SECONDARY) {
                button.setStyle("-fx-background-color: yellow;");
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
        Tablero.vertices.settlements[position] = circle;
        onClickSettlement(circle,position);
        mainAnchor.getChildren().add(circle);
    }

    private void assignSettlementN(Polygon pol, Integer position) {
            Button circle = new Button();
            //Image image = new Image("/img/city_RED.png", 20, 20, true, true);
            //ImageView imageView = new ImageView(image);            
            //circle.setGraphic(imageView);
            //circle.setStyle("-fx-background-color:#414147f1;");
            circle.setShape(new Circle(settleSize));
            circle.setMinSize(2*settleSize, 2*settleSize);
            circle.setMaxSize(2*settleSize, 2*settleSize);
            circle.setLayoutX(pol.getLayoutX() - 12);
            circle.setLayoutY(pol.getLayoutY() - 88);
            Tablero.vertices.settlements[position] = circle;
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
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                circle.setStyle("-fx-background-color: red");
            }   else if (event.getButton() == MouseButton.SECONDARY) {
                circle.setStyle("-fx-background-color: yellow; -fx-border-color:blue;-fx-border-width: 3 3 3 3;");
            }     
        });
    }

    private void cardsPopUp() {
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefSize(500, 350);
        anchorPane.setStyle("-fx-background-color:  #965d62; -fx-background-radius: 12px" );
        popupCards = new Popup();
        popupCards.getContent().add(anchorPane);
        popupCards.setAutoHide(true);


        // Titulo
        Text title = new Text(10, 50, (LangService.getMapping("gameplay_cards")));
        title.setFont(new Font(40));
        title.setLayoutX(anchorPane.getLayoutX() + 55 );
        title.setLayoutY(anchorPane.getLayoutY() + 50);
        title.setFill(Color.WHITE);
        anchorPane.getChildren().add(title);
  
        // Carta caballero
        Text knight_card = new Text(10, 50, (LangService.getMapping("knight_card")) + "\t 2");
        knight_card.setFont(new Font(20));
        knight_card.setLayoutX(anchorPane.getLayoutX()+ 10);
        knight_card.setLayoutY(anchorPane.getLayoutY() + 100);
        knight_card.setFill(Color.WHITE);
        anchorPane.getChildren().add(knight_card);

        // Carta carretera
        Text road_cons_card = new Text(10, 50, (LangService.getMapping("road_cons_card")) + "\t 5");
        road_cons_card.setFont(new Font(20));
        road_cons_card.setLayoutX(anchorPane.getLayoutX()+ 10);
        road_cons_card.setLayoutY(anchorPane.getLayoutY() + 160);
        road_cons_card.setFill(Color.WHITE);
        anchorPane.getChildren().add(road_cons_card);

        // Carta descubrimiento
        Text discovery_card = new Text(10, 50, (LangService.getMapping("discovery_card")) + "\t 1");
        discovery_card.setFont(new Font(20));
        discovery_card.setLayoutX(anchorPane.getLayoutX()+ 10);
        discovery_card.setLayoutY(anchorPane.getLayoutY() + 220);
        discovery_card.setFill(Color.WHITE);
        anchorPane.getChildren().add(discovery_card);

        // Carta puntos victoria
        Text victory_points = new Text(10, 50, (LangService.getMapping("victory_points")) + "\t 8");
        victory_points.setFont(new Font(20));
        victory_points.setLayoutX(anchorPane.getLayoutX()+ 10);
        victory_points.setLayoutY(anchorPane.getLayoutY() + 280);
        victory_points.setFill(Color.WHITE);
        anchorPane.getChildren().add(victory_points);
       
        cards.setOnAction((ActionEvent event) -> {
            
            if (!popupCards.isShowing()) {
                Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
                popupCards.show(stage);
            }
            
         });
         
         cards.setText((LangService.getMapping("gameplay_cards")));

    }

    private void playersName() {
        
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

    private void updateDice() {
        // TODO: Conectar con backend
        diceButton.setText("Dados: 8");
    }

    @FXML
    public void initialize() throws IOException {
        chatContent.setEditable(false);
        chatContent.setMouseTransparent(true);
        chatContent.setFocusTraversable(false);    
        cardsPopUp();
        inTradePopUp();
        externalTradePopUp();
        playersName();
        updateDice();
        settingsPopup();
        passTurnButton.setText((LangService.getMapping("next_turn")));
        
       
        // Crear hexagonos
        Integer numberHexagonAux = 0;
        for(Integer i =0; i < 37; i++) {
            Polygon pol = createHexagon(i);
            fillHexagon(pol, i);    
            mainAnchor.getChildren().add(pol);
            if(hasRoads(i)) {
                Button circle = new Button();
                circle.setShape(new Circle(numberSize));
                circle.setMinSize(2*numberSize, 2*numberSize);
                circle.setMaxSize(2*numberSize, 2*numberSize);
                circle.setLayoutX(pol.getLayoutX() - 18);
                circle.setLayoutY(pol.getLayoutY() - 20);
                //circle.setText(numberHexagonAux.toString());
                
                
                circle.setOnMouseClicked( event -> {
                    if (event.getButton().equals(MouseButton.PRIMARY)) {
                        circle.setDisable(true);
                        for(Button aux : Tablero.hexagonos.numberOverHexagon) {
                            if(aux != circle) {
                                aux.setDisable(false);
                            }
                        }
                    }   
                });

                
                mainAnchor.getChildren().add(circle); 
                Tablero.hexagonos.hexagons[numberHexagonAux] = pol;
                Tablero.hexagonos.numberOverHexagon[numberHexagonAux] = circle;
                numberHexagonAux++;
            }

            assignRoads(pol, i);
            assignSettlements(pol,i);
            
        }
        System.out.println("crearrr");
    } 
}

