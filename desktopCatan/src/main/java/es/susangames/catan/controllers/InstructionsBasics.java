package es.susangames.catan.controllers;

import es.susangames.catan.service.LangService;
import javafx.scene.text.*;
import javafx.fxml.FXML;




public class InstructionsBasics {
 
    @FXML
    private Text textContent;

    @FXML
    private Text textContent2;

    @FXML
    private Text menuText;

   
    public InstructionsBasics() {}
    

    @FXML
    public void initialize() {
        //textContent.setText((LangService.getMapping("instructions_basics_info_paragraph1")));
        //textContent2.setText((LangService.getMapping("instructions_basics_info_paragraph2")));
        menuText.setText((LangService.getMapping("instructions_basics")));
    } 

 



}