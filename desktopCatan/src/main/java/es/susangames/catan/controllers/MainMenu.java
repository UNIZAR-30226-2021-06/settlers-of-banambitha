package es.susangames.catan.controllers;

import es.susangames.catan.App;
import es.susangames.catan.controllers.Instructions;
import es.susangames.catan.service.UserService;
import es.susangames.catan.service.LangService;
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

    @FXML
    private Button btnShop; 

    @FXML
    private Button btnOpt; 

    @FXML
    private Button btnInst; 


    @FXML
    private JFXListView<AnchorPane> playerList;

    @FXML
    private Circle userImg;

    @FXML
    private ImageView goldImage;

    @FXML
    private Text numberCoins;

    @FXML
    private Text numberELO;

    @FXML
    private Text userName;

    @FXML
    private ImageView catanLogo;

    private static Text _numberCoins;


    private Image goldImg;
    private Image userImage;
    private Image catanLog;

    // Atributos busqueda de amigos

    private Circle circleUserSearch;
    private Button addFriend;
    private Text userSearchName;


    public MainMenu() {
        goldImg = new Image("/img/gold_icon.png");
        userImage = new Image("/img/users/user_profile_image_original.png");
        catanLog = new Image("/img/catan-logo.png");

    }


    /**
     * Método que actualiza los strings de esta pantalla.
     * TODO: puede que sea más óptimo utilizar el patrón observer para esto 
     * Dependerá de donde se encuentre el botón para cambiar de idioma
     * (recargar la pantalla es otra opción).
     */
    private void updateStrings(){
        btnPlay.setText(LangService.getMapping("lobby_play"));
        btnShop.setText(LangService.getMapping("lobby_shop"));
        btnOpt.setText(LangService.getMapping("lobby_options"));
        btnInst.setText(LangService.getMapping("lobby_instructions"));
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

    private void loadChat() {
        playerList.getItems().clear();
        playerList.getStylesheets().add("/css/shop.css"); 
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
                chatContent.appendText("User: " + chatInput.getText().toString() + "\n"); 
            }
            chatInput.clear();  
         });


        // Button leave chat
        Button leaveButton = new Button();
        leaveButton.setPrefSize(265,5);
        leaveButton.setStyle("-fx-background-color: #c7956d; -fx-background-radius: 12px");
        leaveButton.setText((LangService.getMapping("leave_chat")));
        

        leaveButton.setOnAction((ActionEvent event) -> {
            playerList.getItems().clear();
            loadFriend("Bigotes", "/img/users/user_profile_image_5.png");
            loadFriend("Laura", "/img/users/user_profile_image_6.png");
            loadFriend("David", "/img/users/user_profile_image_7.png");
            loadFriend("Developer", "/img/users/user_profile_image_8.png");
            loadFriend("Agente secreto", "/img/users/user_profile_image_9.png");
            loadFriend("World_champion", "/img/users/user_profile_image_10.png");
         });



        anchorPane.getChildren().add(chatContent);
        anchorPane.getChildren().add(chatInput);
        anchorPane.getChildren().add(sendButton);
        anchorPane.getChildren().add(leaveButton);

        playerList.getItems().add(anchorPane);

    }


    private void loadUserSearch() {
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
            if(chatInput.getText().toString().length() > 0 ) {
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
                            // TODO: Peticion de amistad
                            System.out.println("holaaa");
                    });
                    anchorPane.getChildren().add(addFriend);

                    // Mostrar nombre del usuario
                    userSearchName =  new Text(10, 10, user.get("nombre").toString());
                    userSearchName.setFont(new Font(15));
                    userSearchName.setLayoutX(anchorPane.getLayoutX() + 105 );
                    userSearchName.setLayoutY(anchorPane.getLayoutY() + 50);
                    userSearchName.setFill(Color.WHITE);
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
        playerList.getItems().add(anchorPane);

    }


    private Circle createCircleUserImg( JSONObject user) {
        Circle circle = new Circle();
        circle.setCenterX(10.0f);
        circle.setCenterY(10.0f);
        circle.setRadius(45.0f);
        // TODO: Obtener id de la imagen del JSON.
        Image imgSkin = new Image("/img/users/user_profile_image_original.png");
        circle.setFill(new ImagePattern(imgSkin));
        return circle;
    }


    private void loadFriend(String username, String imgURL) {
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
           loadChat();
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
           // TODO: Invitar a partida
         });

        anchorPane.getChildren().add(chatButton);
        anchorPane.getChildren().add(inviteButton);
        playerList.getItems().add(anchorPane);
    }


    /**
     * Metodo para actualizar el numero de monedas de la barra
     * de navegacion.
     */
    public static void updateCoins() {
        _numberCoins.setText(UserService.getSaldo().toString());
    }

    /**
     * Inicializa la scene del menu principal.
     */
    @FXML
    public void initialize(){
        updateStrings();
        _numberCoins = numberCoins;
        playerList.getStylesheets().add("/css/shop.css"); 

        mainMenuBP.prefHeightProperty().bind(mainMenu.heightProperty());
        mainMenuBP.prefWidthProperty().bind(mainMenu.widthProperty());
        mainMenuBP.setBottom(null);
        mainMenuBP.setLeft(null);
        mainMenuBP.setStyle("-fx-background-color: #534e52");

        goldImage.setImage(goldImg);
        userImg.setFill(new ImagePattern(userImage));

        loadUserSearch();

        //TODO: Cargar amigos. (Ejemplo de prueba)
        loadFriend("Bigotes", "/img/users/user_profile_image_5.png");
        loadFriend("Laura", "/img/users/user_profile_image_6.png");
        loadFriend("David", "/img/users/user_profile_image_7.png");
        loadFriend("Developer", "/img/users/user_profile_image_8.png");
        loadFriend("Agente secreto", "/img/users/user_profile_image_9.png");
        loadFriend("World_champion", "/img/users/user_profile_image_10.png");


        // Actualizar informacion del
        userName.setText(UserService.getUsername());
        numberCoins.setText(UserService.getSaldo().toString());
        numberELO.setText("835");
        catanLogo.setImage(catanLog);

       
        
        //Barra de navegación
        btnPlay.setOnAction( f -> {
            loadScene("/view/play.fxml", btnPlay);
            
        });

        btnShop.setOnAction( f -> {
            loadScene("/view/shop.fxml", btnShop);
        });

        btnOpt.setOnAction( f -> {
            loadScene("/view/options.fxml", btnOpt);
        });

        btnInst.setOnAction( f -> {
            loadScene("/view/instructions.fxml", btnInst);
        });

        loadScene("/view/shop.fxml", btnPlay);
    }

}
