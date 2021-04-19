package es.susangames.catan.controllers;

import es.susangames.catan.service.LangService;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.*;
import es.susangames.catan.App;
import javafx.event.ActionEvent;
import java.io.IOException;
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


    private Image userImg,win,record;

    public Options() {
        userImg = new Image("/img/users/user_profile_image_2.png");
        win = new Image("/img/win.png");
        record = new Image("/img/crown.png");
    }


    @FXML
    public void initialize()  {
        userImage.setFill(new ImagePattern(userImg));
        userName.setText("Dave");
        userEmail.setText("daveCatan@gmail.com");
        numberVictory.setText("23");
        numberDefeat.setText("7");
        imgWin.setImage(win);
        imgDefeat.setImage(record);


    } 


}