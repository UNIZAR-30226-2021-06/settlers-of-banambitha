package es.susangames.catan.controllers;

import es.susangames.catan.service.LangService;
import javafx.scene.text.*;
import javafx.fxml.FXML;




public class InstructionsFigures {
 
    @FXML
    private Text textContent;

    @FXML
    private Text textContent2;

    @FXML
    private Text textContent3;

    @FXML
    private Text textContent4;

    @FXML
    private Text menuText;

   
    public InstructionsFigures() {}
    

    @FXML
    public void initialize() {
        textContent.setText((LangService.getMapping("instructions_figures_info_paragraph1")));
        textContent2.setText((LangService.getMapping("instructions_figures_info_paragraph2")));
        textContent3.setText((LangService.getMapping("instructions_figures_info_paragraph3")));
        textContent4.setText((LangService.getMapping("instructions_figures_info_paragraph4")));
        menuText.setText((LangService.getMapping("instructions_figures")));
    } 

}