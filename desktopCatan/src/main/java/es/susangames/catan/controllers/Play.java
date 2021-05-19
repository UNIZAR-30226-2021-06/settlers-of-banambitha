package es.susangames.catan.controllers;

import java.util.ArrayList;
import es.susangames.catan.service.LangService;
import es.susangames.catan.service.RoomServices;
import es.susangames.catan.service.ws;
import es.susangames.catan.controllers.Gameplay;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.*;
import es.susangames.catan.App;
import javafx.event.ActionEvent;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.shape.Circle;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import org.json.*;
import es.susangames.catan.service.UserService;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import java.lang.reflect.Type;


public class Play {
 
    @FXML
    private Button playButton;

    @FXML
    private ChoiceBox<String> skinSelector;

    @FXML
    private ChoiceBox<String> typeGameSelector;

    private Button exitRoom;

    @FXML
    private Text player1Name;

    private static Text _player1Name;

    @FXML
    private Circle player1Icon;

    private static Circle _player1Icon;

    @FXML
    private Text player2Name;

    private static Text _player2Name;

    @FXML
    private Circle player2Icon;

    private static Circle _player2Icon;

    @FXML
    private Text player3Name;

    private static Text _player3Name;

    @FXML
    private Circle player3Icon;

    private static Circle _player3Icon;

    @FXML
    private Text player4Name;

    private static Text _player4Name;

    @FXML
    private Circle player4Icon;
    
    private static Circle _player4Icon;




    public Play() {
        
        _player1Name = player1Name;
        _player2Name = player2Name;
        _player3Name = player3Name;
        _player4Name = player4Name;

        _player1Icon = player1Icon;
        _player2Icon = player2Icon;
        _player3Icon = player3Icon;
        _player4Icon = player4Icon;

        skinSelector = new ChoiceBox<>();
        typeGameSelector = new ChoiceBox<>();
    }
    
    @FXML
    public void initialize() throws IOException {

        _player1Name = player1Name;
        _player2Name = player2Name;
        _player3Name = player3Name;
        _player4Name = player4Name;

        _player1Icon = player1Icon;
        _player2Icon = player2Icon;
        _player3Icon = player3Icon;
        _player4Icon = player4Icon;

        playButton.setText((LangService.getMapping("play_button")));
        if (!RoomServices.soyLider()) {
            playButton.setDisable(true);
        }

        skinSelector.getItems().add((LangService.getMapping("play_skin_space")));
        skinSelector.getItems().add("Normal");
        skinSelector.getItems().add((LangService.getMapping("play_skin_computer")));
        skinSelector.setValue("Normal");
        if (!RoomServices.soyLider()) {
            skinSelector.setDisable(true);
        }

        typeGameSelector.getItems().add((LangService.getMapping("play_game_public")));
        typeGameSelector.getItems().add((LangService.getMapping("play_game_private")));
        typeGameSelector.setValue((LangService.getMapping("play_game_public")));

        String users[] = RoomServices.room.toArrayStrings();
        if(users.length  > 0) {
            JSONObject info_player1 = UserService.getUserInfo(users[0]);
            System.out.println("Player 1:\n" + info_player1.toString(4));

            _player1Name.setText(info_player1.getString("nombre"));
            Image imgUser1 = new Image("/img/users/" + info_player1.getString("avatar"));
            _player1Icon.setFill(new ImagePattern(imgUser1));
        } else {
            _player1Name.setText("Jugador 1");
            Image imgUser1 = new Image("/img/users/user_profile_image_original.png");
            _player1Icon.setFill(new ImagePattern(imgUser1));
        }
        if (users.length > 1 ) {
            JSONObject info_player2 = UserService.getUserInfo(users[1]);
            System.out.println("Player 2:\n" + info_player2.toString(4));

            _player2Name.setText(info_player2.getString("nombre"));
            Image imgUser2 = new Image("/img/users/" + info_player2.getString("avatar"));
            _player2Icon.setFill(new ImagePattern(imgUser2));
        } else {
            _player2Name.setText("Jugador 2");
            Image imgUser2 = new Image("/img/users/user_profile_image_original.png");
            _player2Icon.setFill(new ImagePattern(imgUser2));
        }
        if (users.length > 2) {
            JSONObject info_player3 = UserService.getUserInfo(users[2]);
            System.out.println("Player 3:\n" + info_player3.toString(4));

            _player3Name.setText(info_player3.getString("nombre"));
            Image imgUser3 = new Image("/img/users/" + info_player3.getString("avatar"));
            _player3Icon.setFill(new ImagePattern(imgUser3));
        } else {
            _player3Name.setText("Jugador 3");
            Image imgUser3 = new Image("/img/users/user_profile_image_original.png");
            _player3Icon.setFill(new ImagePattern(imgUser3));
        }
        if (users.length > 3) {
            JSONObject info_player4 = UserService.getUserInfo(users[3]);
            System.out.println("Player 4:\n" + info_player4.toString(4));

            _player4Name.setText(info_player4.getString("nombre"));
            Image imgUser4 = new Image("/img/users/" + info_player4.getString("avatar"));
            _player4Icon.setFill(new ImagePattern(imgUser4));
        } else {
            _player4Name.setText("Jugador 4");
            Image imgUser4 = new Image("/img/users/user_profile_image_original.png");
            _player4Icon.setFill(new ImagePattern(imgUser4));
        }
        
    } 

    // TODO: ELIMINAR ESTA FUNCION.
    @FXML
    public static void recargarSalaPartida () {
        System.out.println("Intentando recargar la sala...");
        String users[] = RoomServices.room.toArrayStrings();
        System.out.println(users.length);
        if(users.length  > 0) {
            JSONObject info_player1 = UserService.getUserInfo(users[0]);
            System.out.println("Player 1:\n" + info_player1.toString(4));

            _player1Name.setText(info_player1.getString("nombre"));
            Image imgUser1 = new Image("/img/users/" + info_player1.getString("avatar"));
            _player1Icon.setFill(new ImagePattern(imgUser1));
        } else {
            _player1Name.setText("Jugador 1");
            Image imgUser1 = new Image("/img/users/user_profile_image_original.png");
            _player1Icon.setFill(new ImagePattern(imgUser1));
        }
        if (users.length > 1 ) {
            JSONObject info_player2 = UserService.getUserInfo(users[1]);
            System.out.println("Player 2:\n" + info_player2.toString(4));

            _player2Name.setText(info_player2.getString("nombre"));
            Image imgUser2 = new Image("/img/users/" + info_player2.getString("avatar"));
            _player2Icon.setFill(new ImagePattern(imgUser2));
        } else {
            _player2Name.setText("Jugador 2");
            Image imgUser2 = new Image("/img/users/user_profile_image_original.png");
            _player2Icon.setFill(new ImagePattern(imgUser2));
        }
        if (users.length > 2) {
            JSONObject info_player3 = UserService.getUserInfo(users[2]);
            System.out.println("Player 3:\n" + info_player3.toString(4));

            _player3Name.setText(info_player3.getString("nombre"));
            Image imgUser3 = new Image("/img/users/" + info_player3.getString("avatar"));
            _player3Icon.setFill(new ImagePattern(imgUser3));
        } else {
            _player3Name.setText("Jugador 3");
            Image imgUser3 = new Image("/img/users/user_profile_image_original.png");
            _player3Icon.setFill(new ImagePattern(imgUser3));
        }
        if (users.length > 3) {
            JSONObject info_player4 = UserService.getUserInfo(users[3]);
            System.out.println("Player 4:\n" + info_player4.toString(4));

            _player4Name.setText(info_player4.getString("nombre"));
            Image imgUser4 = new Image("/img/users/" + info_player4.getString("avatar"));
            _player4Icon.setFill(new ImagePattern(imgUser4));
        } else {
            _player4Name.setText("Jugador 4");
            Image imgUser4 = new Image("/img/users/user_profile_image_original.png");
            _player4Icon.setFill(new ImagePattern(imgUser4));
        }
    }

    @FXML
    void loadGameplay(ActionEvent event)  throws IOException {
        //Ejemplo para comprobar que se guardan la cfg de busqueda
        System.out.println(typeGameSelector.getValue());
        System.out.println(skinSelector.getValue());
        // Buscar partida
        System.out.println("___________Pulsacion___________");
        RoomServices.buscarPartida();
        // Cancelar
    }
   
}