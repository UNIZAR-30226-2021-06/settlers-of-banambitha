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


public class Play {
 
    @FXML
    private Button playButton;

    public Play() {}
    

    @FXML
    void loadGameplay(ActionEvent event)  throws IOException {
        App.nuevaPantalla("/view/gameplay.fxml");
    }
   

}