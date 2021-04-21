package es.susangames.catan.controllers;

import java.io.IOException;
import es.susangames.catan.service.UserService;

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
        if(UserService.userExists(username.getText().toString())) {
            wrongRegister.setText("Ya existe un usuario con esas credenciales");
        }
        else if(email.getText().isEmpty() ||  password.getText().isEmpty() ||
    			username.getText().isEmpty()) {
             wrongRegister.setText("Rellene los campos");
        }
        else if(UserService.register( username.getText().toString(),
                                 email.getText().toString(), 
                                 password.getText().toString())) {
    	    App.nuevaPantalla("/view/Login.fxml");
    	}
    	else {
             wrongRegister.setText("Nombre o Contraseña incorrectos!");
         }

    }
    
    public void account_exists(ActionEvent event) throws IOException {
    	App.nuevaPantalla("/view/Login.fxml");
    }
}
