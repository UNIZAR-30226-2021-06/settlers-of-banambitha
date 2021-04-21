package es.susangames.catan.controllers;

import es.susangames.catan.service.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javafx.event.ActionEvent;

import java.io.IOException;

import es.susangames.catan.App;

public class Login {
 
    @FXML
    private Label wrongLogIn;
    @FXML
    private TextField email;
    @FXML
    private PasswordField password;
    @FXML
    private Label wrongRegister;
    
    private UserService userService;

    public Login() {
        userService = new UserService();
    }


    public void login_user(ActionEvent event) throws IOException {
        comprobar_login();
    }
    
    public void reset_password_link(ActionEvent event) throws IOException {
    	App.nuevaPantalla("/view/ResetPassword.fxml");
    }
    
    public void register_link(ActionEvent event) throws IOException {
        App.nuevaPantalla("/view/Register.fxml");
    }
    
    

    private void comprobar_login() throws IOException {
        if(UserService.validate(email.getText().toString(),password.getText().toString())) {
            App.nuevaPantalla("/view/mainMenu.fxml");
        }

        else if(email.getText().isEmpty() ||  password.getText().isEmpty()) {
            wrongLogIn.setText("Rellene los campos");
        }

        else {
            wrongLogIn.setText("Email o Contraseña incorrectos!");
        }
    }


}