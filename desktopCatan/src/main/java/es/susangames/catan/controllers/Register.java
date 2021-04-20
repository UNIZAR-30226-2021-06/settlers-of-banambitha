package es.susangames.catan.controllers;

import java.io.IOException;

import es.susangames.catan.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;


public class Register {

    @FXML
    private TextField email,username;
    @FXML
    private PasswordField password;
    @FXML
    private Label wrongRegister;
    
    public Register() {}


    public void register_user(ActionEvent event) throws IOException {
        comprobar_registro();
    }

    
    private void comprobar_registro() throws IOException {
    	if(email.getText().toString().equals("prueba") && 
    	   username.getText().toString().equals("prueba") &&
    	   password.getText().toString().equals("prueba")) {
    	            
    	           App.nuevaPantalla("/view/Login.fxml");
    	}
    	else if(email.getText().isEmpty() ||  password.getText().isEmpty() ||
    			username.getText().isEmpty()) {
             wrongRegister.setText("Rellene los campos");
        }
    	else {
             wrongRegister.setText("Nombre o Contraseña incorrectos!");
         }

    }
    
    public void account_exists(ActionEvent event) throws IOException {
    	App.nuevaPantalla("/view/Login.fxml");
    }
}
