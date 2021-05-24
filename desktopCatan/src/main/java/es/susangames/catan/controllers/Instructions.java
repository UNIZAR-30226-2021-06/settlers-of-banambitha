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
import com.jfoenix.controls.JFXTextArea;

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
    private JFXButton buttonDevelopment;

    @FXML
    private AnchorPane anchorContent;

    @FXML
    private Text title;

    @FXML
    private JFXTextArea text1;


   
    public Instructions() {}
    
 
    @FXML
    public void initialize() throws IOException {
        menuText.setText((LangService.getMapping("instructions_menu")));
        rulesInstructions.setText((LangService.getMapping("instructions_rules_instructions")));
        buttonBasics.setText((LangService.getMapping("instructions_basics")));
        buttonFigures.setText((LangService.getMapping("instructions_figures")));
        buttonDevelopment.setText((LangService.getMapping("instructions_development")));
        text1.setMaxWidth(3500);
        text1.setFont(new Font(20));
        title.setText((LangService.getMapping("instructions_basics")));
        text1.setText((LangService.getMapping("instructions_basics_info_paragraph1")));
    } 
    
    @FXML
    public void loadDevelopment(ActionEvent event) throws IOException {
        text1.setMaxWidth(3500);
        text1.setFont(new Font(20));
        title.setText((LangService.getMapping("instructions_development")));
        text1.setText((LangService.getMapping("instructions_development_paragraph1")));
    }


    @FXML
    public void loadBasics(ActionEvent event) throws IOException {
        text1.setMaxWidth(3500);
        text1.setFont(new Font(20));
        title.setText((LangService.getMapping("instructions_basics")));
        text1.setText((LangService.getMapping("instructions_basics_info_paragraph1")));
    }

    @FXML
    public void loadFigures(ActionEvent event) throws IOException {
        
    }

}