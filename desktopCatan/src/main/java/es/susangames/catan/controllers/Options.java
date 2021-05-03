package es.susangames.catan.controllers;

import es.susangames.catan.service.LangService;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.*;
import es.susangames.catan.App;
import javafx.event.ActionEvent;
import java.io.IOException;
import javafx.stage.Popup;
import javafx.scene.Node;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.shape.Circle;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import com.jfoenix.controls.JFXListView;
import javafx.scene.control.ToggleButton;



public class Options {
    @FXML
    private Circle userImage;

    @FXML
    private Text userName;

    @FXML
    private Text userEmail;

    @FXML
    private Text numberVictory;

    @FXML
    private Text numberDefeat;

    @FXML
    private ImageView imgWin;

    @FXML
    private ImageView imgDefeat;

    @FXML
    private Button buttonChangeMail;

    @FXML
    private Button changeUserImg;

    @FXML
    private Button buttonChangePsw;

    @FXML
    private ToggleButton lang;

    private Popup popupChangeMail, popupChangePsw;

    private Popup popupChangeUserImg;

    private Image userImg,win,record;

    public Options() {
        userImg = new Image("/img/users/user_profile_image_2.png");
        win = new Image("/img/win.png");
        record = new Image("/img/win.png");
    }

    private void changeMailPopUp() {
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefSize(210, 210);
        anchorPane.setStyle("-fx-background-color:  #c7956d; -fx-background-radius: 12px" );
        popupChangeMail = new Popup();
        popupChangeMail.getContent().add(anchorPane);
        popupChangeMail.setAutoHide(true);

         // Titulo
         Text title = new Text(10, 50, (LangService.getMapping("new_mail")));
         title.setFont(new Font(40));
         title.setLayoutX(anchorPane.getLayoutX() - 4);
         title.setLayoutY(anchorPane.getLayoutY() + 20);
         title.setFill(Color.WHITE);
         anchorPane.getChildren().add(title);

         TextField textField = new TextField();
         textField.setStyle("-fx-background-radius: 12px" );
         textField.setPrefSize(340,40);
         textField.setLayoutX(anchorPane.getLayoutX() + 10 );
         textField.setLayoutY(anchorPane.getLayoutY() + 110);
         anchorPane.getChildren().add(textField);



         Button accept = new Button();
         accept.setPrefSize(100,30);
         accept.setLayoutX(anchorPane.getLayoutX() + 220);
         accept.setLayoutY(anchorPane.getLayoutY() + 170);
         accept.setStyle("-fx-background-color: #665d64; -fx-background-radius: 12px");
         accept.setText((LangService.getMapping("accept")));
         DropShadow shadow = new DropShadow();
         accept.setEffect(shadow);
         anchorPane.getChildren().add(accept);


         accept.setOnAction((ActionEvent event) -> {
            System.out.println(textField.getText().toString());
            popupChangeMail.hide();
            textField.clear();    
         });

        buttonChangeMail.setOnAction((ActionEvent event) -> {
            
            if (!popupChangeMail.isShowing()) {
                Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
                popupChangeMail.show(stage);
            }
            
         });
         
         buttonChangeMail.setText((LangService.getMapping("new_mail")));
    }


    private void changePasswordPopUp() {
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefSize(210, 210);
        anchorPane.setStyle("-fx-background-color:  #c7956d; -fx-background-radius: 12px" );
        popupChangePsw = new Popup();
        popupChangePsw.getContent().add(anchorPane);
        popupChangePsw.setAutoHide(true);

         // Titulo
         Text title = new Text(10, 50, (LangService.getMapping("new_psw")));
         title.setFont(new Font(40));
         title.setLayoutX(anchorPane.getLayoutX() - 4);
         title.setLayoutY(anchorPane.getLayoutY() + 20);
         title.setFill(Color.WHITE);
         anchorPane.getChildren().add(title);

         TextField textField = new TextField();
         textField.setStyle("-fx-background-radius: 12px" );
         textField.setPrefSize(425,40);
         textField.setLayoutX(anchorPane.getLayoutX() + 10 );
         textField.setLayoutY(anchorPane.getLayoutY() + 110);
         anchorPane.getChildren().add(textField);



         Button accept = new Button();
         accept.setPrefSize(100,30);
         accept.setLayoutX(anchorPane.getLayoutX() + 330);
         accept.setLayoutY(anchorPane.getLayoutY() + 170);
         accept.setStyle("-fx-background-color: #665d64; -fx-background-radius: 12px");
         accept.setText((LangService.getMapping("accept")));
         DropShadow shadow = new DropShadow();
         accept.setEffect(shadow);
         anchorPane.getChildren().add(accept);


         accept.setOnAction((ActionEvent event) -> {
            System.out.println(textField.getText().toString());
            popupChangePsw.hide();
            textField.clear();    
         });

         buttonChangePsw.setOnAction((ActionEvent event) -> {
            
            if (!popupChangeMail.isShowing()) {
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
        for(Integer i = 0; i < 12; i++) {
            newShopElement("Skin " + i.toString(),  
                            "/img/users/user_profile_image_" + i.toString() + ".png",
                            elementsList);
         } 

         changeUserImg.setOnAction((ActionEvent event) -> {
            
            if (!popupChangeUserImg.isShowing()) {
                Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
                popupChangeUserImg.show(stage);
            }
            
         });
         popupChangeUserImg.getContent().add(elementsList);
         elementsList.setPrefSize(700,700);

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
        Image imgSkin = new Image(imgURL);
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

        // TODO: Añadir accion cuando se hace click sobre boton compra
        selectButton.setOnAction((ActionEvent event) -> {
           userImage.setFill(new ImagePattern( new Image(imgURL)));
           popupChangeUserImg.hide();
        });

        anchorPane.getChildren().add(selectButton);
        elementsList.getItems().add(anchorPane);
    }


    @FXML
    public void initialize()  {
        userImage.setFill(new ImagePattern(userImg));
        userName.setText("Dave");
        userEmail.setText("daveCatan@gmail.com");
        numberVictory.setText("23");
        numberDefeat.setText("7");
        imgWin.setImage(win);
        //imgDefeat.setImage(record);
       // imgDefeat.setStyle("-fx-background-color: transparent;");
        changeMailPopUp();
        changePasswordPopUp();
        changeUserImage();


        //Botón para mostrar el cambio de idioma.
        lang.setText("Español");
        lang.setOnAction(e ->{
            if ( lang.isSelected() ){
                lang.setText("English"); 
                //LangService.changeLanguage(LangService.ENG);
            }else{
                lang.setText("Español"); 
                //LangService.changeLanguage(LangService.ESP);
            }
        });


    } 


}