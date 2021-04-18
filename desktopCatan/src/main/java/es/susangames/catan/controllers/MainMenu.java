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

    private Button btnPlay; 

    private Button btnShop; 

    private Button btnOpt; 

    private Button btnInst; 

    private Button activeButton; 

    private ToggleButton lang;

    private Instructions ins;

    @FXML
    private JFXListView<AnchorPane> playerList;

    @FXML
    private Circle userImg;

    @FXML
    private ImageView goldImage;

    private Image goldImg;
    private Image userImage;


    public MainMenu() {
        goldImg = new Image("/img/gold_icon.png");
        userImage = new Image("/img/users/user_profile_image_2.png");
    }


    /**
     * Método que actualiza los strings de esta pantalla.
     * TODO: puede que sea más óptimo utilizar el patrón observer para esto 
     * Dependerá de donde se encuentre el botón para cambiar de idioma
     * (recargar la pantalla es otra opción).
     */
    private void updateStrings(){
       /* btnPlay.setText(LangService.getMapping("lobby_play"));
        btnShop.setText(LangService.getMapping("lobby_shop"));
        btnOpt.setText(LangService.getMapping("lobby_options"));
        btnInst.setText(LangService.getMapping("lobby_instructions"));*/
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
            //activeButton.setDisable(false);
            //activeButton = clicked;
            //activeButton.setDisable(true);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Inicializa la scene del menu principal.
     */
    @FXML
    public void initialize(){

        mainMenuBP.prefHeightProperty().bind(mainMenu.heightProperty());
        mainMenuBP.prefWidthProperty().bind(mainMenu.widthProperty());
        mainMenuBP.setBottom(null);
        mainMenuBP.setLeft(null);
        mainMenuBP.setStyle("-fx-background-color: #534e52");

        goldImage.setImage(goldImg);
        userImg.setFill(new ImagePattern(userImage));


        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefSize(5, 500);
        anchorPane.setStyle("-fx-background-color: blue");

        AnchorPane anchorPane2 = new AnchorPane();
        anchorPane2.setPrefSize(5, 500);
        anchorPane2.setStyle("-fx-background-color: green");

        AnchorPane anchorPane3 = new AnchorPane();
        anchorPane3.setPrefSize(5, 500);
        anchorPane3.setStyle("-fx-background-color: green");
       
       
        
        playerList.getItems().add(anchorPane);
        playerList.getItems().add(anchorPane2);
        playerList.getItems().add(anchorPane3);
       
        
        //Barra de navegación

        /*btnPlay = new Button(LangService.getMapping("lobby_play")); 
        btnPlay.setMaxWidth(Double.MAX_VALUE);
        btnPlay.setOnAction( f -> {
            loadScene("/view/play.fxml", btnPlay);
        });

        btnShop = new Button(LangService.getMapping("lobby_shop")); 
        btnShop.setMaxWidth(Double.MAX_VALUE);
        btnShop.setOnAction( f -> {
            loadScene("/view/shop.fxml", btnShop);
        });

        btnOpt = new Button(LangService.getMapping("lobby_options")); 
        btnOpt.setMaxWidth(Double.MAX_VALUE);
        btnOpt.setOnAction( f -> {
            loadScene("/view/options.fxml", btnOpt);
        });

        btnInst = new Button(LangService.getMapping("lobby_instructions"));
        btnInst.setMaxWidth(Double.MAX_VALUE);
        btnInst.setOnAction( f -> {
            loadScene("/view/instructions.fxml", btnInst);
        });

        HBox navbar = new HBox(); 
        navbar.setSpacing(10);
        navbar.setPadding(new Insets(15, 22, 15, 50));
        navbar.getChildren().add(btnPlay); 
        navbar.getChildren().add(btnShop); 
        navbar.getChildren().add(btnOpt); 
        navbar.getChildren().add(btnInst); 
        navbar.setId("navbar");
        navbar.getStylesheets().add("/css/navbar.css"); 

        StackPane sp = new StackPane(navbar);
        sp.setEffect(new DropShadow());
        mainMenuBP.setTop(sp);
        BorderPane.setMargin(sp, new Insets(0));

        //Social
        ListView<String> social = new ListView<String>(); 
        social.getItems().add("Carlos");
        social.getItems().add("Jaime");
        social.getItems().add("Fabián");
        social.getItems().add("Anyiel");
        social.getItems().add("Fernando");
        social.getItems().add("Victor");
        social.setId("social");
        social.getStylesheets().add("/css/social.css");

        StackPane socialSP = new StackPane(social);
        DropShadow dp = new DropShadow();
        dp.setOffsetY(10);
        socialSP.setEffect(dp);

        mainMenuBP.setRight(socialSP); 
        BorderPane.setMargin(socialSP, new Insets(0));*/

        //Activar ventana de jugar
        //activeButton = btnPlay;
        loadScene("/view/instructions.fxml", btnPlay);


        //Aux

        //Botón de prueba para mostrar el cambio de idioma.
        //TODO: este botón deberá eliminarse cuando se haya
        //  implemetado la pantalla de opciones
        /*lang = new ToggleButton("Español"); 
        lang.setOnAction(e ->{
            if ( lang.isSelected() ){
                lang.setText("English"); 
                LangService.changeLanguage(LangService.ENG);
                updateStrings();
            }else{
                lang.setText("Español"); 
                LangService.changeLanguage(LangService.ESP);
                updateStrings();
            }
        });
        navbar.getChildren().add(lang); */
    }
}
