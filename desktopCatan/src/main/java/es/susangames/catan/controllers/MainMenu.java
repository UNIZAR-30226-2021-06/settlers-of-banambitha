package es.susangames.catan.controllers;

import es.susangames.catan.App;
import es.susangames.catan.controllers.Instructions;
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


    private Image goldImg;
    private Image userImage;
    private Image catanLog;


    public MainMenu() {
        goldImg = new Image("/img/gold_icon.png");
        userImage = new Image("/img/users/user_profile_image_2.png");
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

        // Boton invitar partida
        Button buyButton = new Button();
        buyButton.setPrefSize(80,15);
        buyButton.setLayoutX(anchorPane.getLayoutX() + 120);
        buyButton.setLayoutY(anchorPane.getLayoutY() + 65);
        buyButton.setStyle("-fx-background-color: #c7956d; -fx-background-radius: 12px");
        buyButton.setText((LangService.getMapping("friends_invite")));
        DropShadow shadow = new DropShadow();
        buyButton.setEffect(shadow);
        anchorPane.getChildren().add(buyButton);



        playerList.getItems().add(anchorPane);
    }

    /**
     * Inicializa la scene del menu principal.
     */
    @FXML
    public void initialize(){
        updateStrings();
        playerList.getStylesheets().add("/css/shop.css"); 

        mainMenuBP.prefHeightProperty().bind(mainMenu.heightProperty());
        mainMenuBP.prefWidthProperty().bind(mainMenu.widthProperty());
        mainMenuBP.setBottom(null);
        mainMenuBP.setLeft(null);
        mainMenuBP.setStyle("-fx-background-color: #534e52");

        goldImage.setImage(goldImg);
        userImg.setFill(new ImagePattern(userImage));


        //TODO: Cargar amigos. (Ejemplo de prueba)
        loadFriend("Bigotes", "/img/users/user_profile_image_5.png");
        loadFriend("Laura", "/img/users/user_profile_image_6.png");
        loadFriend("David", "/img/users/user_profile_image_7.png");
        loadFriend("Developer", "/img/users/user_profile_image_8.png");
        loadFriend("Agente secreto", "/img/users/user_profile_image_9.png");
        loadFriend("World_champion", "/img/users/user_profile_image_10.png");



        // TODO: Conectar con backend. Sirve como ejemplo
        userName.setText("Dave");
        numberCoins.setText("761231");
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

        loadScene("/view/play.fxml", btnPlay);
    }

}
