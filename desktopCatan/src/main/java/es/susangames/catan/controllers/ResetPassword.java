package es.susangames.catan.controllers;

import java.io.IOException;

import es.susangames.catan.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ResetPassword {
	@FXML
    private TextField email;
	@FXML
	private Label wrongEmail;
	
	public ResetPassword() {}
	
	public void reset_password(ActionEvent event) throws IOException {
        comprobar_reset_password();
    }


	private void comprobar_reset_password() throws IOException {
		if(email.getText().toString().equals("prueba")) {
			App.nuevaPantalla("/view/Login.fxml");
		}
		else if(email.getText().isEmpty()) {
			wrongEmail.setText("Rellene los campos");
		}
		else {
			wrongEmail.setText("Email incorrecto");
		}
	}
}
