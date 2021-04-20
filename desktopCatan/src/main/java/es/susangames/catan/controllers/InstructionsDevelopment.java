package es.susangames.catan.controllers;

import es.susangames.catan.service.LangService;
import javafx.scene.text.*;
import javafx.fxml.FXML;




public class InstructionsDevelopment {
 
    @FXML
    private Text textContent;

    @FXML
    private Text textContent2;

    @FXML
    private Text textContent3;

    @FXML
    private Text textContent4;


    @FXML
    private Text textContent5;


    @FXML
    private Text textContent6;


    @FXML
    private Text menuText;

   
    public InstructionsDevelopment() {}
    

    @FXML
    public void initialize() {
        textContent.setText((LangService.getMapping("instructions_development_paragraph1")));
        textContent2.setText((LangService.getMapping("instructions_development_paragraph2")));
        textContent3.setText((LangService.getMapping("instructions_development_paragraph3")));
        textContent4.setText((LangService.getMapping("instructions_development_paragraph4")));
        textContent5.setText((LangService.getMapping("instructions_development_paragraph5")));
        textContent6.setText((LangService.getMapping("instructions_development_paragraph6")));

        menuText.setText((LangService.getMapping("instructions_development")));
    } 

}