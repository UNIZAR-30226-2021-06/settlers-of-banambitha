package es.susangames.catan.controllers;

import es.susangames.catan.service.LangService;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.*;
import es.susangames.catan.App;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import java.io.IOException;
import javafx.fxml.FXMLLoader;



public class Instructions {
 
    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Text menuText;
    
    @FXML
    private Text rulesInstructions;

    @FXML
    private JFXButton buttonBasics;

    @FXML
    private JFXButton buttonFigures;

    @FXML
    private AnchorPane anchorContent;

   
    public Instructions() {}
    

    @FXML
    public void initialize() {
        menuText.setText((LangService.getMapping("instructions_menu")));
        rulesInstructions.setText((LangService.getMapping("instructions_rules_instructions")));
        buttonBasics.setText((LangService.getMapping("instructions_basics")));
        buttonFigures.setText((LangService.getMapping("instructions_figures")));
    } 
    
    public void updateText() {
        initialize();
    } 


    public void loadBasics(ActionEvent event) throws IOException {
        anchorContent.getChildren().clear();
        anchorContent.getChildren().add(FXMLLoader.load(getClass().getResource("/view/basics.fxml")));
    }

    public void loadFigures(ActionEvent event) throws IOException {
        anchorContent.getChildren().clear();
        anchorContent.getChildren().add(FXMLLoader.load(getClass().getResource("/view/figures.fxml")));
    }

}