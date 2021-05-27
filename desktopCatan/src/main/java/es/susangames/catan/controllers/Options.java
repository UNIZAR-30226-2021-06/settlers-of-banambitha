package es.susangames.catan.controllers;

import es.susangames.catan.service.LangService;
import es.susangames.catan.service.ShopService;
import es.susangames.catan.service.UserService;
import es.susangames.catan.controllers.MainMenu;
import es.susangames.catan.service.ws;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.*;
import es.susangames.catan.App;
import javafx.event.ActionEvent;
import java.io.IOException;
import javafx.stage.Popup;
import javafx.scene.Node;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.shape.Circle;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import com.jfoenix.controls.JFXListView;
import javafx.scene.control.ToggleButton;
import org.json.*;
import javafx.scene.control.PasswordField;



public class Options {
    @FXML
    private Circle userImage;

    @FXML
    private Text userName;

    @FXML
    private Text userEmail;

    @FXML
    private Text numberVictory;

    private static Text _numberVictory = null;

    @FXML
    private Text numberDefeat;

    private static Text _numberDefeat = null;

    @FXML
    private ImageView imgWin;
    
    @FXML
    private Circle imgSettings;

    @FXML
    private Button buttonChangeMail;

    @FXML
    private Button changeUserImg;

    @FXML
    private Button buttonChangePsw;

    @FXML
    private Text defeat;

    @FXML
    private Text victory;

    @FXML
    private Text actual_streak;

    private static Text _actual_streak = null;

    @FXML
    private Text historic_streak;

    private static Text _historic_streak = null;

    @FXML
    private ToggleButton lang;

    @FXML
    private Text porcentaje;

    @FXML
    private Text numberPorcentaje;

    private static Text _numberPorcentaje = null;

    private Popup popupChangeMail, popupChangePsw;

    private Popup popupChangeUserImg;

    private Image userImg,win,settings;

    private ShopService shop;


    public Options() {
        win = new Image("/img/win.png");
        settings = new Image("/img/settings.png");
        shop = new ShopService();
    }

    private void deleteAccountPopUp() {
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefSize(210, 210);
        anchorPane.setStyle("-fx-background-color:  #c7956d; -fx-background-radius: 12px" );
        popupChangeMail = new Popup();
        popupChangeMail.getContent().add(anchorPane);
        popupChangeMail.setAutoHide(true);

         // Titulo
         Text title = new Text(10, 50, (LangService.getMapping("delete")));
         title.setFont(new Font(40));
         title.setLayoutX(anchorPane.getLayoutX() - 4);
         title.setLayoutY(anchorPane.getLayoutY() + 20);
         title.setFill(Color.WHITE);
         anchorPane.getChildren().add(title);

         PasswordField textField = new PasswordField();
         textField.setPromptText((LangService.getMapping("write_psw")));  
         textField.setStyle("-fx-background-radius: 12px" );
         textField.setPrefSize(340,40);
         textField.setLayoutX(anchorPane.getLayoutX() + 10 );
         textField.setLayoutY(anchorPane.getLayoutY() + 110);
         anchorPane.getChildren().add(textField);



         Button accept = new Button();
         accept.setPrefSize(100,30);
         accept.setLayoutX(anchorPane.getLayoutX() + 230);
         accept.setLayoutY(anchorPane.getLayoutY() + 170);
         accept.setStyle("-fx-background-color: #665d64; -fx-background-radius: 12px");
         accept.setText((LangService.getMapping("accept")));
         DropShadow shadow = new DropShadow();
         accept.setEffect(shadow);
         anchorPane.getChildren().add(accept);


         accept.setOnAction((ActionEvent event) -> {
            if(UserService.passwordMatches(textField.getText())) {
                ws.borrarCuenta(textField.getText());
                popupChangeMail.hide();
            } else {
                textField.clear();    
            }
         });

        buttonChangeMail.setOnAction((ActionEvent event) -> {
            
            if (!popupChangeMail.isShowing()) {
                deleteAccountPopUp();
                Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
                popupChangeMail.show(stage);
            }
            
         });
         buttonChangeMail.setText((LangService.getMapping("delete")));
    }


    private void changePasswordPopUp() {
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefSize(350, 300);
        anchorPane.setStyle("-fx-background-color:  #c7956d; -fx-background-radius: 12px" );
        popupChangePsw = new Popup();
        popupChangePsw.getContent().add(anchorPane);
        popupChangePsw.setAutoHide(true);

         // Titulo
         Text title = new Text(10, 50, (LangService.getMapping("new_psw")));
         title.setFont(new Font(40));
         title.setLayoutX(anchorPane.getLayoutX() - 4);
         title.setLayoutY(anchorPane.getLayoutY() + 15);
         title.setFill(Color.WHITE);
         anchorPane.getChildren().add(title);
         
         PasswordField oldPassword = new PasswordField();
         oldPassword.setPromptText((LangService.getMapping("write_old_psw")));         
         oldPassword.setStyle("-fx-background-radius: 12px" );
         oldPassword.setPrefSize(425,40);
         oldPassword.setLayoutX(anchorPane.getLayoutX() + 10 );
         oldPassword.setLayoutY(anchorPane.getLayoutY() + 130);
         anchorPane.getChildren().add(oldPassword);

         PasswordField newPassword = new PasswordField();
         newPassword.setPromptText((LangService.getMapping("write_new_psw")));       
         newPassword.setStyle("-fx-background-radius: 12px" );
         newPassword.setPrefSize(425,40);
         newPassword.setLayoutX(anchorPane.getLayoutX() + 10 );
         newPassword.setLayoutY(anchorPane.getLayoutY() + 190);
         anchorPane.getChildren().add(newPassword);


    
         Button accept = new Button();
         accept.setPrefSize(100,30);
         accept.setLayoutX(anchorPane.getLayoutX() + 330);
         accept.setLayoutY(anchorPane.getLayoutY() + 250);
         accept.setStyle("-fx-background-color: #665d64; -fx-background-radius: 12px");
         accept.setText((LangService.getMapping("accept")));
         DropShadow shadow = new DropShadow();
         accept.setEffect(shadow);
         anchorPane.getChildren().add(accept);
         
         accept.setOnAction((ActionEvent event) -> {
            if(UserService.passwordMatches(oldPassword.getText()) &&  
              UserService.passwordMatches(newPassword.getText())) {
                if(UserService.changePassword(oldPassword.getText(),newPassword.getText())) {
                    popupChangePsw.hide();
                } else {
                    Text titleError =  new Text(10, 50, (LangService.getMapping("psw_fails")) );
                    titleError.setFont(new Font(20));
                    titleError.setLayoutX(anchorPane.getLayoutX() + 120 );
                    titleError.setLayoutY(anchorPane.getLayoutY() + 50);
                    titleError.setFill(Color.BLACK);
                    anchorPane.getChildren().add(titleError);
                }
                oldPassword.clear();
                newPassword.clear(); 
              }
           
         });

         buttonChangePsw.setOnAction((ActionEvent event) -> {
            
            if (!popupChangeMail.isShowing()) {
                changePasswordPopUp();
                Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
                popupChangePsw.show(stage);
            }
         });
         buttonChangePsw.setText((LangService.getMapping("new_psw")));
    }

    private void changeUserImage() {
        JFXListView<AnchorPane> elementsList = new JFXListView<>();
        elementsList.getStylesheets().add("/css/shop.css"); 
        popupChangeUserImg = new Popup();
       
        popupChangeUserImg.setAutoHide(true);
        
        JSONArray skinList =  shop.obtenerProductosAdquiridos();
        elementsList.getItems().clear();
        for (int i = 0; i < skinList.length(); i++) {
            JSONObject object = skinList.getJSONObject(i);
            String aux = object.getString("tipo").toString();
            if(aux.equals("AVATAR")) {
                newShopElement(object.getString("nombre"),  
                                object.getString("url"),
                                elementsList);
            }
        }
         changeUserImg.setOnAction((ActionEvent event) -> {
            
            if (!popupChangeUserImg.isShowing()) {
                Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
                popupChangeUserImg.show(stage);
            }
            
         });
         popupChangeUserImg.getContent().add(elementsList);
         elementsList.setPrefSize(700,300);

    }

    private void newShopElement(String name, String imgURL, JFXListView<AnchorPane> elementsList) {
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefSize(150, 300);
        anchorPane.setStyle("-fx-background-color: #965d62");
        
        // Image icono
        Circle circle = new Circle();
        circle.setCenterX(110.0f);
        circle.setCenterY(110.0f);
        circle.setRadius(90.0f);
        circle.setLayoutX(anchorPane.getLayoutX() + 25);
        circle.setLayoutY(anchorPane.getLayoutY() + 25);
        Image imgSkin = new Image("/img/users/" + imgURL);
        circle.setFill(new ImagePattern(imgSkin));
        anchorPane.getChildren().add(circle);
        
        
        // Nombre icono
        Text tName = new Text(10, 50, name);
        tName.setFont(new Font(40));
        tName.setLayoutX(anchorPane.getLayoutX() + 300);
        tName.setLayoutY(anchorPane.getLayoutY() + 45);
        tName.setFill(Color.WHITE);
        anchorPane.getChildren().add(tName);

        // Boton compra
        Button selectButton = new Button();
        selectButton.setPrefSize(250,100);
        selectButton.setLayoutX(anchorPane.getLayoutX() + 300);
        selectButton.setLayoutY(anchorPane.getLayoutY() + 150);
        selectButton.setStyle("-fx-background-color: #c7956d; -fx-background-radius: 12px; -fx-font-size:20; -fx-text-fill: WHITE");
        selectButton.setText((LangService.getMapping("set_icon")));
        DropShadow shadow = new DropShadow();
        selectButton.setEffect(shadow);

        // TODO: Añadir accion cuando se hace click sobre el  boton 
        selectButton.setOnAction((ActionEvent event) -> {
            UserService.updateUser(UserService.getUsername(),"avatar",imgURL);
            MainMenu.updateUserImg();
            userImage.setFill(new ImagePattern(new Image(UserService.getUserImg(UserService.getUsername()))));
           popupChangeUserImg.hide();
        });

        anchorPane.getChildren().add(selectButton);
        elementsList.getItems().add(anchorPane);
    }


    public static void updateStats() {
        if(_numberDefeat != null) {
            Integer derrotas = (UserService.getPartidasJugadas() - UserService.getTotalDeVictorias());
            _numberDefeat.setText(derrotas.toString());
        } 
        if(_numberVictory != null) {
            _numberVictory.setText(UserService.getTotalDeVictorias().toString());
        } 
        if(_actual_streak != null) {
            _actual_streak.setText(LangService.getMapping("win_strike") + " " + 
                    UserService.getRachaDeVictoriasActual().toString());
        } 
        if(_historic_streak != null) {
            _historic_streak.setText(LangService.getMapping("historic_strike") + " " + 
                                UserService.getMayorRachaDeVictorias().toString());
        }
        if(_numberPorcentaje != null) {
            _numberPorcentaje.setText(UserService.getPorcentajeVictorias().toString() +
                                      " %");
        }
    }

    @FXML
    public void initialize()  {
        userImage.setFill(new ImagePattern(new Image(UserService.getUserImg(UserService.getUsername()))));
        userName.setText(UserService.getUsername());
        userEmail.setText(UserService.getMail());
        _numberDefeat = numberDefeat;
        _numberVictory = numberVictory;
        _actual_streak = actual_streak;
        _historic_streak = historic_streak;
        _numberPorcentaje = numberPorcentaje;
        
        numberVictory.setText(UserService.getTotalDeVictorias().toString());
        Integer derrotas = (UserService.getPartidasJugadas() - UserService.getTotalDeVictorias());
        numberDefeat.setText(derrotas.toString());
        imgWin.setImage(win);
        imgSettings.setFill(new ImagePattern(settings));
        victory.setText(LangService.getMapping("victory"));
        defeat.setText(LangService.getMapping("defeat"));
        actual_streak.setText(LangService.getMapping("win_strike") + " " + 
                                UserService.getRachaDeVictoriasActual().toString());
        historic_streak.setText(LangService.getMapping("historic_strike") + " " + 
                                UserService.getMayorRachaDeVictorias().toString());
        porcentaje.setText(LangService.getMapping("per_victory"));
        numberPorcentaje.setText(UserService.getPorcentajeVictorias().toString() + 
                                " %");

        deleteAccountPopUp();
        changePasswordPopUp();
        changeUserImage();


        //Botón para mostrar el cambio de idioma.
        lang.setText(UserService.getIdioma());
        
        
        lang.setOnAction(e ->{
            if (UserService.getIdioma().equals("Español")){
                lang.setText("English"); 
                LangService.changeLanguage(LangService.ENG);
                UserService.updateUser(UserService.getUsername(),"idioma","English");
            }else{
                lang.setText("Español"); 
                LangService.changeLanguage(LangService.ESP);
                UserService.updateUser(UserService.getUsername(),"idioma","Español");
            }
            victory.setText(LangService.getMapping("victory"));
            defeat.setText(LangService.getMapping("defeat"));
            actual_streak.setText(LangService.getMapping("win_strike") + " " + 
                                    UserService.getRachaDeVictoriasActual().toString());
            historic_streak.setText(LangService.getMapping("historic_strike") + " " + 
                                    UserService.getMayorRachaDeVictorias().toString());
            buttonChangeMail.setText((LangService.getMapping("delete")));
            buttonChangePsw.setText((LangService.getMapping("new_psw")));
            porcentaje.setText(LangService.getMapping("per_victory"));
            MainMenu.updateStrings();
            if(!MainMenu.chatOpenned) {
                MainMenu.getFriends();
            }
        });


    } 


}