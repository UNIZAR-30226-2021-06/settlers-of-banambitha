package es.susangames.catan.controllers;
import es.susangames.catan.service.RoomServices;
import es.susangames.catan.App;
import es.susangames.catan.controllers.Instructions;
import es.susangames.catan.service.UserService;
import es.susangames.catan.service.LangService;
import es.susangames.catan.service.ws;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.*;
import com.jfoenix.controls.JFXListView;
import javafx.scene.shape.Circle;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import com.jfoenix.controls.JFXListView;
import javafx.fxml.FXML;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.event.ActionEvent;
import com.jfoenix.controls.JFXTextArea;
import javafx.scene.control.TextField;
import org.json.*;
import java.util.ArrayList;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.scene.Node;







/**
 * Controlador de la ventana principal del lobby
 * TODO: Esta clase no está terminada, tanto la barra
 *   de navegación como el área social son solamente temporales, 
 *   sirven de marco para empezar a desarrollar la aplicación
 *   pero deben terminar de implementarse.
 */
public class MainMenu {
    
    @FXML
    private StackPane mainMenu; 

    @FXML
    private BorderPane mainMenuBP;

    private HBox navbar; 

    @FXML
    private Button btnPlay; 

    private static Button _btnPlay;

    @FXML
    private Button btnShop; 

    private static Button _btnShop;

    @FXML
    private Button btnOpt; 

    private static Button _btnOpt;
 
    @FXML
    private Button btnInst; 

    private static Button _btnInst;


    @FXML
    private JFXListView<AnchorPane> playerList;

    @FXML
    private Circle userImg;

    private static Circle _userImg;

    @FXML
    private ImageView goldImage;

    @FXML
    private Text numberCoins;



    @FXML
    private Text userName;

    @FXML
    private ImageView catanLogo;

    private static Text _numberCoins;
    public static JFXListView<AnchorPane> _playerList;

    private Image goldImg;
    private Image userImage;
    private Image catanLog;

    // Atributos busqueda de amigos
    private static Popup _popupFriendInfo;
    private static Circle circleUserSearch;
    private static Button addFriend;
    private static Text userSearchName;
    private static String userSearched;
    public static Boolean chatOpenned;
    public static Boolean playOpenned;
    public static String userChatOpenned;
    public static JFXTextArea _chatContent;
    
    public static String skinSelected;


    public MainMenu() {
        goldImg = new Image("/img/gold_icon.png");
        catanLog = new Image("/img/catan-logo.png");
        chatOpenned = false;
        playOpenned = false;
        userChatOpenned ="";
    }
    

    /**
     * Método que actualiza los strings de esta pantalla.
     * TODO: puede que sea más óptimo utilizar el patrón observer para esto 
     * Dependerá de donde se encuentre el botón para cambiar de idioma
     * (recargar la pantalla es otra opción).
     */
    public static  void updateStrings(){
        _btnPlay.setText(LangService.getMapping("lobby_play"));
        _btnShop.setText(LangService.getMapping("lobby_shop"));
        _btnOpt.setText(LangService.getMapping("lobby_options"));
        _btnInst.setText(LangService.getMapping("lobby_instructions"));
    }


    /**
     * Carga en el centro de mainMenuBP la escena con 
     * el fichero fxml dado. Si no se puede encontrar el 
     * fichero fxml no hace nada. 
     * Desactiva el botón pulsado y activa el botón
     * "despulsado".
     * @param fxml fxml a cargar 
     * @param clicked botón clicado
     */
    private void loadScene(String fxml, Button clicked){

        try {
            Pane scene = FXMLLoader.load(App.class.getResource(fxml));
            mainMenuBP.setCenter(scene);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void loadChat(String friendname) {
        userChatOpenned = friendname;
        _playerList.getItems().clear();
        _playerList.getStylesheets().add("/css/shop.css"); 
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefSize(265, 818);
        anchorPane.setStyle("-fx-background-color: #965d62; -fx-background-radius: 12px");

        // Chat content
        JFXTextArea chatContent = new JFXTextArea();
        chatContent.setStyle("-fx-text-inner-color: white; -fx-font-size: 16px;");
        chatContent.setPrefSize(263, 750);
        chatContent.setLayoutY(anchorPane.getLayoutY() + 27); //
        chatContent.setEditable(false);
        chatContent.setMouseTransparent(true);
        chatContent.setFocusTraversable(false);   
        _chatContent = chatContent;

        ArrayList<JSONObject> itemsList = ws.msgs.get(friendname);
        if(itemsList != null) {
            if (itemsList.size() > 0 ){
                for(JSONObject aux : itemsList) {
                    chatContent.appendText(aux.getString("from") + ": "  + 
                                            aux.getString("body") + "\n");
                }
            }
        }
        
       
        
        
        // Chat input
        TextField chatInput = new TextField();
        chatInput.setPrefSize(200, 30);
        chatInput.setStyle("-fx-background-color: #665d64; -fx-text-inner-color: white; -fx-background-radius: 12px;");
        chatInput.setLayoutX(anchorPane.getLayoutX() + 5);
        chatInput.setLayoutY(anchorPane.getLayoutY() + 785);

        // Button send
        Button sendButton = new Button();
        sendButton.setPrefSize(40,25);
        sendButton.setLayoutX(anchorPane.getLayoutX() + 210);
        sendButton.setLayoutY(anchorPane.getLayoutY() + 788);
        sendButton.setStyle("-fx-background-color: #c7956d; -fx-background-radius: 12px");
        sendButton.setText("->");
        DropShadow shadow = new DropShadow();
        sendButton.setEffect(shadow);


        sendButton.setOnAction((ActionEvent event) -> {
            if(chatInput.getText().toString().length() > 0 ) {
                chatContent.appendText(UserService.getUsername()  + ": " + 
                                        chatInput.getText().toString() + "\n"); 
                ws.sendPrivateMsg(friendname,chatInput.getText().toString());

            }
            chatInput.clear();  
         });


        // Button leave chat
        Button leaveButton = new Button();
        leaveButton.setPrefSize(265,5);
        leaveButton.setStyle("-fx-background-color: #c7956d; -fx-background-radius: 12px");
        leaveButton.setText((LangService.getMapping("leave_chat")));
        

        leaveButton.setOnAction((ActionEvent event) -> {
            _playerList.getItems().clear();
            getFriends();
            chatOpenned = false;
         });



        anchorPane.getChildren().add(chatContent);
        anchorPane.getChildren().add(chatInput);
        anchorPane.getChildren().add(sendButton);
        anchorPane.getChildren().add(leaveButton);
        _playerList.getItems().add(anchorPane);

    }


    private static void loadUserSearch() {
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefSize(5, 150);
        anchorPane.setStyle("-fx-background-color: #965d62");

        // TextField para introducir busqueda
        TextField chatInput = new TextField();
        chatInput.setPromptText(LangService.getMapping("find_user")); 
        chatInput.setPrefSize(180, 30);
        chatInput.setStyle("-fx-background-color: #665d64; -fx-text-inner-color: white; -fx-background-radius: 12px;");
        chatInput.setLayoutX(anchorPane.getLayoutX() + 5);
        chatInput.setLayoutY(anchorPane.getLayoutY() + 10);

        // Boton enviar solicitud
        Button searchButton = new Button();
        searchButton.setPrefSize(35,30);
        searchButton.setLayoutX(anchorPane.getLayoutX() + 190);
        searchButton.setLayoutY(anchorPane.getLayoutY() + 10);
        searchButton.setStyle("-fx-background-color: #c7956d; -fx-background-radius: 12px");
        searchButton.setText("->");
        DropShadow shadow = new DropShadow();
        searchButton.setEffect(shadow);

        searchButton.setOnAction((ActionEvent event) -> {
            if(chatInput.getText().toString().length() > 0 && 
               !chatInput.getText().toString().equals(UserService.getUsername())) {
                anchorPane.getChildren().remove(circleUserSearch);
                anchorPane.getChildren().remove(addFriend);
                anchorPane.getChildren().remove(userSearchName);
                

                if(UserService.userExists(chatInput.getText().toString())) {
                    
                    JSONObject user = UserService.getUserInfo(chatInput.getText().toString());
                    circleUserSearch = createCircleUserImg(user);
                    circleUserSearch.setLayoutX(anchorPane.getLayoutX() + 45);
                    circleUserSearch.setLayoutY(anchorPane.getLayoutY() + 75);
                    anchorPane.getChildren().add(circleUserSearch);

                    // Buton para enviar solicitud
                    addFriend = new Button();
                    addFriend.setPrefSize(80,15);
                    addFriend.setLayoutX(anchorPane.getLayoutX() + 110);
                    addFriend.setLayoutY(anchorPane.getLayoutY() + 85);
                    addFriend.setStyle("-fx-background-color: #c7956d; -fx-background-radius: 12px");
                    addFriend.setText((LangService.getMapping("new_friend"))); 
                    addFriend.setEffect(shadow);

                    addFriend.setOnAction((ActionEvent event_add) -> {
                            ws.sendFriendRequest(userSearched);
                            anchorPane.getChildren().remove(circleUserSearch);
                            anchorPane.getChildren().remove(addFriend);
                            anchorPane.getChildren().remove(userSearchName);
                    });
                    anchorPane.getChildren().add(addFriend);

                    // Mostrar nombre del usuario
                    userSearchName =  new Text(10, 10, user.get("nombre").toString());
                    userSearchName.setFont(new Font(15));
                    userSearchName.setLayoutX(anchorPane.getLayoutX() + 105 );
                    userSearchName.setLayoutY(anchorPane.getLayoutY() + 50);
                    userSearchName.setFill(Color.WHITE);
                    userSearched =  user.get("nombre").toString();
                    anchorPane.getChildren().add(userSearchName);


                } else {
                    userSearchName =  new Text(10, 10, (LangService.getMapping("no_user")));
                    userSearchName.setFont(new Font(15));
                    userSearchName.setLayoutX(anchorPane.getLayoutX() + 35 );
                    userSearchName.setLayoutY(anchorPane.getLayoutY() + 50);
                    userSearchName.setFill(Color.WHITE);
                    anchorPane.getChildren().add(userSearchName);
                }
                
            }
            chatInput.clear();
        });



        anchorPane.getChildren().add(chatInput);
        anchorPane.getChildren().add(searchButton);
        _playerList.getItems().add(anchorPane);

    }


    private static Circle createCircleUserImg( JSONObject user) {
        Circle circle = new Circle();
        circle.setCenterX(10.0f);
        circle.setCenterY(10.0f);
        circle.setRadius(45.0f);
        // TODO: Obtener id de la imagen del JSON.
        Image imgSkin = new Image("/img/users/user_profile_image_original.png");
        circle.setFill(new ImagePattern(imgSkin));
        return circle;
    }


    private static void popUpFriendInfo(String username) {
        JSONObject stats = UserService.getFriendStats(username);
        Integer victorias = stats.getInt("totalDeVictorias");
        Integer derrotas = stats.getInt("partidasJugadas") - victorias;
        Integer rachaActual = stats.getInt("rachaDeVictoriasActual");
        Integer rachaHistorica = stats.getInt("mayorRachaDeVictorias");

        JSONObject info = UserService.getUserInfo(username);
        String email = info.getString("email");
        
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefSize(350, 280);
        anchorPane.setStyle("-fx-background-color:  #414147f1;; -fx-background-radius: 12px" );
        _popupFriendInfo = new Popup();
        _popupFriendInfo.getContent().add(anchorPane);
        _popupFriendInfo.setAutoHide(true);
        
        // Titulo
        Text title = new Text(10, 50, username);
        title.setFont(new Font(25));
        title.setLayoutX(anchorPane.getLayoutX()+ 105);
        title.setLayoutY(anchorPane.getLayoutY() + 5);
        title.setFill(Color.WHITE);
        anchorPane.getChildren().add(title);
        
        Text tofferPlayer = new Text(10, 50, "Email: " + email );
        tofferPlayer.setFont(new Font(20));
        tofferPlayer.setLayoutX(anchorPane.getLayoutX() + 10 );
        tofferPlayer.setLayoutY(anchorPane.getLayoutY() + 55);
        tofferPlayer.setFill(Color.WHITE);
        anchorPane.getChildren().add(tofferPlayer);
        
       
     
        // Victorias
        Text victory = new Text(10, 50,(LangService.getMapping("victory")) + ": " +
                                victorias.toString());
        victory.setFont(new Font(20));
        victory.setLayoutX(anchorPane.getLayoutX()+ 10);
        victory.setLayoutY(anchorPane.getLayoutY() + 95);
        victory.setFill(Color.WHITE);
        anchorPane.getChildren().add(victory);
        
        
        // Derrotas
        Text defeat = new Text(10, 50, (LangService.getMapping("defeat")) + ": " +
                                derrotas.toString());
        defeat.setFont(new Font(20));
        defeat.setLayoutX(anchorPane.getLayoutX()+ 10);
        defeat.setLayoutY(anchorPane.getLayoutY() + 135);
        defeat.setFill(Color.WHITE);
        anchorPane.getChildren().add(defeat);

        // Racha actual
        Text actualStreak = new Text(10, 50, (LangService.getMapping("win_strike")) + " " +
                                        rachaActual.toString());
        actualStreak.setFont(new Font(20));
        actualStreak.setLayoutX(anchorPane.getLayoutX() + 10);
        actualStreak.setLayoutY(anchorPane.getLayoutY() + 175);
        actualStreak.setFill(Color.WHITE);
        anchorPane.getChildren().add(actualStreak);

         // Racha historica
         Text historicStreak = new Text(10, 50, (LangService.getMapping("historic_strike")) + " " +
                                        rachaHistorica.toString());
        historicStreak.setFont(new Font(20));
        historicStreak.setLayoutX(anchorPane.getLayoutX() + 10);
        historicStreak.setLayoutY(anchorPane.getLayoutY() + 215);
        historicStreak.setFill(Color.WHITE);
        anchorPane.getChildren().add(historicStreak);
        
       
    }

    private static void loadFriend(String username, String imgURL) {
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefSize(5, 150);
        anchorPane.setStyle("-fx-background-color: #965d62");

        // Image usuario
        Circle circle = new Circle();
        circle.setCenterX(10.0f);
        circle.setCenterY(10.0f);
        circle.setRadius(45.0f);
        circle.setLayoutX(anchorPane.getLayoutX() + 45);
        circle.setLayoutY(anchorPane.getLayoutY() + 45);
        Image imgSkin = new Image(imgURL);
        circle.setFill(new ImagePattern(imgSkin));
        anchorPane.getChildren().add(circle);

        circle.setOnMouseClicked(event -> {
            popUpFriendInfo(username);
            Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            _popupFriendInfo.show(stage);
           
        });



        // Nombre usuario
        Text tName = new Text(10, 10, username);
        tName.setFont(new Font(15));
        tName.setLayoutX(anchorPane.getLayoutX() + 85 );
        tName.setLayoutY(anchorPane.getLayoutY() + 15);
        tName.setFill(Color.WHITE);
        anchorPane.getChildren().add(tName);


        // Boton iniciar chat
        Button chatButton = new Button();
        chatButton.setPrefSize(80,15);
        chatButton.setLayoutX(anchorPane.getLayoutX() + 120);
        chatButton.setLayoutY(anchorPane.getLayoutY() + 65);
        chatButton.setStyle("-fx-background-color: #c7956d; -fx-background-radius: 12px");
        chatButton.setText("Chat");
        DropShadow shadow = new DropShadow();
        chatButton.setEffect(shadow);

        chatButton.setOnAction((ActionEvent event) -> {
           loadChat(username);
           chatOpenned = true;
         });


        // Boton invitar a partida
        Button inviteButton = new Button();
        inviteButton.setPrefSize(80,15);
        inviteButton.setLayoutX(anchorPane.getLayoutX() + 120);
        inviteButton.setLayoutY(anchorPane.getLayoutY() + 105);
        inviteButton.setStyle("-fx-background-color: #c7956d; -fx-background-radius: 12px");
        inviteButton.setText((LangService.getMapping("friends_invite"))); 
        inviteButton.setEffect(shadow);

        inviteButton.setOnAction((ActionEvent event) -> {
           // invitacionEnviar
           ws.enviarInvitacion(username);
         });

        anchorPane.getChildren().add(chatButton);
        anchorPane.getChildren().add(inviteButton);
        _playerList.getItems().add(anchorPane);
    }



    public static void loadPendingReq(String username, String imgURL) {
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefSize(5, 150);
        anchorPane.setStyle("-fx-background-color: #965d62");

        // Image usuario
        Circle circle = new Circle();
        circle.setCenterX(10.0f);
        circle.setCenterY(10.0f);
        circle.setRadius(45.0f);
        circle.setLayoutX(anchorPane.getLayoutX() + 45);
        circle.setLayoutY(anchorPane.getLayoutY() + 45);
        Image imgSkin = new Image(imgURL);
        circle.setFill(new ImagePattern(imgSkin));
        anchorPane.getChildren().add(circle);

        // Nombre usuario
        Text tName = new Text(10, 10, username);
        tName.setFont(new Font(15));
        tName.setLayoutX(anchorPane.getLayoutX() + 85 );
        tName.setLayoutY(anchorPane.getLayoutY() + 15);
        tName.setFill(Color.WHITE);
        anchorPane.getChildren().add(tName);


        // Boton aceptar invitacion 
        Button chatButton = new Button();
        chatButton.setPrefSize(80,15);
        chatButton.setLayoutX(anchorPane.getLayoutX() + 120);
        chatButton.setLayoutY(anchorPane.getLayoutY() + 65);
        chatButton.setStyle("-fx-background-color: #c7956d; -fx-background-radius: 12px");
        chatButton.setText(LangService.getMapping("accept_friend"));
        DropShadow shadow = new DropShadow();
        chatButton.setEffect(shadow);

        chatButton.setOnAction((ActionEvent event) -> {
           ws.acceptFriendRequest(username);
         });


         Button declineButton = new Button();
         declineButton.setPrefSize(80,15);
         declineButton.setLayoutX(anchorPane.getLayoutX() + 120);
         declineButton.setLayoutY(anchorPane.getLayoutY() + 105);
         declineButton.setStyle("-fx-background-color: #c7956d; -fx-background-radius: 12px");
         declineButton.setText(LangService.getMapping("decline_friend"));
         declineButton.setEffect(shadow);
 
         declineButton.setOnAction((ActionEvent event) -> {
            ws.declineFriendRequest(username);
          });


        anchorPane.getChildren().add(chatButton);
        anchorPane.getChildren().add(declineButton);
        _playerList.getItems().add(anchorPane);
    }


    /**
     * Metodo para actualizar el numero de monedas de la barra
     * de navegacion.
     */
    public static void updateCoins() {
        _numberCoins.setText(UserService.getSaldo().toString());
    }

    public static void updateUserImg() {
        _userImg.setFill(new ImagePattern(new Image(UserService.getUserImg(UserService.getUsername()))));
    }
    
    public static void getFriends() {
        _playerList.getItems().clear();
        System.out.println("Cargando amigos....");
        loadUserSearch();

        ArrayList<RoomServices.Invite> invitaciones = RoomServices.invites;

        for (RoomServices.Invite i : invitaciones) {
                System.out.println("Invitacion de " + i.getLeader());
                loadInvitation(i.getLeader(), i.getId(), UserService.getUserImg(i.getLeader()));
        }

        JSONArray pendingReqs = UserService.pendingFriendReq();
        for (int i = 0; i < pendingReqs.length(); i++) {
            JSONObject object = pendingReqs.getJSONObject(i);
            String nombreAmigo = object.getString("from").toString();
            String urlAvatar = UserService.getUserImg(nombreAmigo);
            loadPendingReq(nombreAmigo, urlAvatar);
        }



        JSONArray friends = UserService.getFriends();
        for (int i = 0; i < friends.length(); i++) {
            JSONObject object = friends.getJSONObject(i);
            String nombreAmigo = object.getString("usuario2_id").toString();
            if(nombreAmigo.length() > 4) {
                String urlAvatar = UserService.getUserImg(nombreAmigo);
                loadFriend(nombreAmigo, urlAvatar);
            }
        }

    }
    
    /**
     * Inicializa la scene del menu principal.
     */
    @FXML
    public void initialize(){
        _numberCoins = numberCoins;
        _playerList = playerList;
        _userImg = userImg;
        _btnPlay = btnPlay;
        _btnShop = btnShop;
        _btnOpt = btnOpt;
        _btnInst = btnInst;
        updateStrings();
        _playerList.getStylesheets().add("/css/shop.css"); 

        mainMenuBP.prefHeightProperty().bind(mainMenu.heightProperty());
        mainMenuBP.prefWidthProperty().bind(mainMenu.widthProperty());
        mainMenuBP.setBottom(null);
        mainMenuBP.setLeft(null);
        mainMenuBP.setStyle("-fx-background-color: #534e52");


        goldImage.setImage(goldImg);
        userImg.setFill(new ImagePattern(new Image(UserService.getUserImg(UserService.getUsername()))));

        //TODO: Cargar amigos. (Ejemplo de prueba)
        getFriends();

        skinSelected = "Normal";

        // Actualizar informacion del
        userName.setText(UserService.getUsername());
        numberCoins.setText(UserService.getSaldo().toString());
        catanLogo.setImage(catanLog);

       
        
        //Barra de navegación
        btnPlay.setOnAction( f -> {
            loadScene("/view/play.fxml", btnPlay);
            playOpenned = true;
        });

        btnShop.setOnAction( f -> {
            loadScene("/view/shop.fxml", btnShop);
            playOpenned = false;
        });

        btnOpt.setOnAction( f -> {
            loadScene("/view/options.fxml", btnOpt);
            playOpenned = false;
        });

        btnInst.setOnAction( f -> {
            loadScene("/view/instructions.fxml", btnInst);
            playOpenned = false;
        });

        loadScene("/view/shop.fxml", btnPlay);


        ws.initialize();

    }


    private static void loadInvitation (String username, String room, String imgURL) {
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefSize(5, 150);
        anchorPane.setStyle("-fx-background-color: #965d62");

        // Image usuario
        Circle circle = new Circle();
        circle.setCenterX(10.0f);
        circle.setCenterY(10.0f);
        circle.setRadius(45.0f);
        circle.setLayoutX(anchorPane.getLayoutX() + 45);
        circle.setLayoutY(anchorPane.getLayoutY() + 45);
        Image imgSkin = new Image(imgURL);
        circle.setFill(new ImagePattern(imgSkin));
        anchorPane.getChildren().add(circle);

        // Nombre usuario
        Text tName = new Text(10, 10, username);
        tName.setFont(new Font(15));
        tName.setLayoutX(anchorPane.getLayoutX() + 85 );
        tName.setLayoutY(anchorPane.getLayoutY() + 15);
        tName.setFill(Color.WHITE);
        anchorPane.getChildren().add(tName);


        // Boton aceptar invitacion 
        Button chatButton = new Button();
        chatButton.setPrefSize(80,15);
        chatButton.setLayoutX(anchorPane.getLayoutX() + 120);
        chatButton.setLayoutY(anchorPane.getLayoutY() + 65);
        chatButton.setStyle("-fx-background-color: #c7956d; -fx-background-radius: 12px");
        chatButton.setText(LangService.getMapping("accept_invitation"));
        DropShadow shadow = new DropShadow();
        chatButton.setEffect(shadow);

        chatButton.setOnAction((ActionEvent event) -> {
           ws.aceptarInvitacion(username, room);
         });


         Button declineButton = new Button();
         declineButton.setPrefSize(80,15);
         declineButton.setLayoutX(anchorPane.getLayoutX() + 120);
         declineButton.setLayoutY(anchorPane.getLayoutY() + 105);
         declineButton.setStyle("-fx-background-color: #c7956d; -fx-background-radius: 12px");
         declineButton.setText(LangService.getMapping("decline_invitation"));
         declineButton.setEffect(shadow);
 
         declineButton.setOnAction((ActionEvent event) -> {
            //ws.declineMatchRequest(username, room);
            RoomServices.deleteInvite(room);
          });


        anchorPane.getChildren().add(chatButton);
        anchorPane.getChildren().add(declineButton);
        _playerList.getItems().add(anchorPane);
    }
}
