package es.susangames.catan.controllers;

import es.susangames.catan.service.LangService;
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

    @FXML
    private Text player1Name;

    @FXML
    private Circle player1Icon;

    @FXML
    private Text player1ELO;

    @FXML
    private Text player2Name;

    @FXML
    private Circle player2Icon;

    @FXML
    private Text player2ELO;

    @FXML
    private Text player3Name;

    @FXML
    private Circle player3Icon;

    @FXML
    private Text player3ELO;

    @FXML
    private Text player4Name;

    @FXML
    private Circle player4Icon;

    @FXML
    private Text player4ELO;




    public Play() {
        skinSelector = new ChoiceBox<>();
        typeGameSelector = new ChoiceBox<>();
    }
    
    @FXML
    public void initialize() throws IOException {
        playButton.setText((LangService.getMapping("play_button")));

        skinSelector.getItems().add((LangService.getMapping("play_skin_space")));
        skinSelector.getItems().add("Normal");
        skinSelector.getItems().add((LangService.getMapping("play_skin_computer")));
        skinSelector.setValue("Normal");

        typeGameSelector.getItems().add((LangService.getMapping("play_game_public")));
        typeGameSelector.getItems().add((LangService.getMapping("play_game_private")));
        typeGameSelector.setValue((LangService.getMapping("play_game_public")));

        // TODO: Cambiar para integrar con backend. Sirve como ejemplo 
        player1Name.setText("Martín");
        player2Name.setText("Lucía");
        player3Name.setText("Marta");
        player4Name.setText("Pablo");
        Image imgUser1 = new Image("/img/users/user_profile_image_0.png");
        Image imgUser2 = new Image("/img/users/user_profile_image_1.png");
        Image imgUser3 = new Image("/img/users/user_profile_image_2.png");
        Image imgUser4 = new Image("/img/users/user_profile_image_3.png");
        player1Icon.setFill(new ImagePattern(imgUser1));
        player2Icon.setFill(new ImagePattern(imgUser2));
        player3Icon.setFill(new ImagePattern(imgUser3));
        player4Icon.setFill(new ImagePattern(imgUser4));
        player1ELO.setText("55");
        player2ELO.setText("738");
        player3ELO.setText("81");
        player4ELO.setText("919");

    } 

    @FXML
    void loadGameplay(ActionEvent event)  throws IOException {
        //Ejemplo para comprobar que se guardan la cfg de busqueda
        ws.session.subscribe( ws.partida_test_topicUrl  + UserService.getUsername(), new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return String.class;
            }
            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                Gameplay.comenzarPartidaPrueba(payload.toString());
            }
        });
        JSONObject object = new JSONObject();
        object.put("from", UserService.getUsername());
        object.put("simulate", false); 
        ws.session.send(ws.partidaTestComenzar, object.toString());
    }
   

}