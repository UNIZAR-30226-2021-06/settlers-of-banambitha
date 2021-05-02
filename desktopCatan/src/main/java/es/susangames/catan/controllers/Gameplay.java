package es.susangames.catan.controllers;

import es.susangames.catan.service.LangService;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.*;
import es.susangames.catan.App;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.util.Random;

import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.shape.Polygon;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.control.ToggleButton;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Spinner;
import javafx.event.EventHandler;
import com.jfoenix.controls.JFXTextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Popup;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.scene.control.Label;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.effect.DropShadow;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;










public class Gameplay {
 
    private static double start_X_position = 250.0;
    private static double start_Y_position = 110.0;
    private static double horizontal_right_gap = 115;
    private static double horizontal_left_gap = 60;
    private static double vertical_gap = 120;

    private Polygon[] hexagons;
    private ToggleButton[] roads;
    private Image imgSea, imgDes, imgMou, imgFie, imgFor, imgHil; 
 
    @FXML
    private AnchorPane mainAnchor;

    @FXML
    private JFXTextArea chatContent;

    @FXML
    private TextField chatInput;

    @FXML
    private Button cards;

    @FXML
    private Button inTrade;    

    @FXML
    private Button outTrade; 

    @FXML
    private Text player1Name;

    @FXML
    private Text player2Name;

    @FXML
    private Text player3Name;

    @FXML
    private Text player4Name;

    @FXML
    private Button diceButton;

    @FXML
    private Button settingsButton;

    @FXML
    private Button passTurnButton;


    private Popup popupCards;

    private Popup popupInternalTrade;

    private Popup popupExternalTrade;

    private Popup popupSettings;


    private ChoiceBox<String> offerMaterial;

    private ChoiceBox<String> offerPlayer;

    private ChoiceBox<String> receiveMaterial;

    private ChoiceBox<String> receivePlayer;

    private ChoiceBox<String> ratio;

    private Button sendTrade;

    private Button sendTradeExternal;

    private Button leaveGame;

    private Button stopGame;




    private Integer offerAmountInt;

    private Integer receiveAmountInt;

    private String player1,player2,player3,player4;


    


    public Gameplay() {
        hexagons = new Polygon[37];
        roads = new ToggleButton[57];
        imgSea = new Image("/img/sea.png");
        imgDes = new Image("/img/sand-desert.jpg"); 
        imgMou = new Image("/img/mountain.png");  
        imgFie = new Image("/img/fields.jpeg"); 
        imgFor = new Image("/img/forest.png"); 
        imgHil = new Image("/img/hills.jpeg"); 

        // TODO: Conectar con backend
        player1 = "Martín";
        player2 = "Lucía";
        player3 = "Marta";
        player4 = "Pablo";

        offerAmountInt = 1;
        receiveAmountInt = 1;

    }
    

    @FXML
    void send_msg(ActionEvent event) {
        chatContent.appendText("User: " + chatInput.getText().toString() + "\n"); 
        chatInput.clear();    

    }

    private Polygon createHexagon(Integer numHexag) {
        Polygon pol = new Polygon();
        pol.getPoints().addAll(new Double[]{-50.0, 30.0, 0.0, 60.0, 50.0, 30.0,
                                             50.0,-30.0, 0.0,-60.0,-50.0,-30.0});
                                
        if(numHexag < 4) { // Primera fila
            pol.setLayoutX(start_X_position + (horizontal_right_gap * numHexag));
            pol.setLayoutY(start_Y_position); 

        } else if (numHexag < 9) { // Segunda fila
            pol.setLayoutX((start_X_position - horizontal_left_gap) + 
                                (horizontal_right_gap * (numHexag - 4)));
            pol.setLayoutY(start_Y_position + vertical_gap);

        } else if (numHexag < 15) { // Tercera fila
            pol.setLayoutX((start_X_position - (horizontal_left_gap * 2)) 
                            + (horizontal_right_gap * (numHexag - 9)));
            pol.setLayoutY(start_Y_position + (vertical_gap * 2));

        } else if (numHexag < 22) { // Cuarta fila
            pol.setLayoutX((start_X_position - (horizontal_left_gap * 3)) 
                            + (horizontal_right_gap * (numHexag - 15)) );
            pol.setLayoutY(start_Y_position + (vertical_gap * 3));

        } else if (numHexag < 28) { // Quinta fila
            pol.setLayoutX((start_X_position - (horizontal_left_gap * 2)) 
                            + (horizontal_right_gap * (numHexag - 22)));
            pol.setLayoutY(start_Y_position + (vertical_gap * 4));

        } else if (numHexag < 33) { // Sexta fila
            pol.setLayoutX((start_X_position - horizontal_left_gap) + 
                                (horizontal_right_gap * (numHexag - 28)));
            pol.setLayoutY(start_Y_position + (vertical_gap * 5));
            
        } else { // Septima fila
            pol.setLayoutX(start_X_position 
                                        + (horizontal_right_gap * (numHexag - 33  )));
            pol.setLayoutY(start_Y_position + (vertical_gap * 6));
        }

        return pol;
    }

   


    private void fillHexagon(Polygon pol, Integer i) {
        if ( i <= 4 || i == 8 || i == 9 || i == 14 ||
        i == 15 || i == 21 || i == 22 || i == 27 ||
        i == 28 || i >= 32) {

            pol.setFill(new ImagePattern(imgSea));
        } 
        else {
            int var = i % 5;
            //TODO: Modificar para rellenar segun codigo backend
            // Version estatica para frontend
            if (var == 0) {
                pol.setFill(new ImagePattern(imgDes));
            } else if (var == 1) {
                pol.setFill(new ImagePattern(imgMou));
            } else if (var == 2) {
                pol.setFill(new ImagePattern(imgFie));
            } else if (var == 3) {
                pol.setFill(new ImagePattern(imgFor));
            } else {
                pol.setFill(new ImagePattern(imgHil));
            }
        }

    }

    private Boolean hasRoads(Integer i) {
        return ( (i > 4  && i <=  7)  || 
                 (i > 9 && i <= 13)  ||
                 (i > 15 && i <= 20)  ||  
                 (i > 22 && i <= 26)  ||  
                 (i > 28 && i <= 31));  
    }

    private ToggleButton createRoadN(Polygon pol) {
        ToggleButton _road = new ToggleButton();
        _road.setPrefSize(1,30);
        _road.setLayoutX(pol.getLayoutX() - 65);
        _road.setLayoutY(pol.getLayoutY() - 15);
        setColorButonOnClick(_road);
        return _road;
    }

    private ToggleButton createRoadSE(Polygon pol) {
        ToggleButton _roadSE = new ToggleButton();
        _roadSE.setPrefSize(1,30);
        _roadSE.setLayoutX(pol.getLayoutX() + 23);
        _roadSE.setLayoutY(pol.getLayoutY() - 66);
        _roadSE.setRotate(120);
        setColorButonOnClick(_roadSE);
        return _roadSE;
    }

    private ToggleButton createRoadNE(Polygon pol) {
        ToggleButton _roadNE = new ToggleButton();
        _roadNE.setPrefSize(1,30);
        _roadNE.setLayoutX(pol.getLayoutX() - 40);
        _roadNE.setLayoutY(pol.getLayoutY() - 70);
        _roadNE.setRotate(60);
        setColorButonOnClick(_roadNE);
        return _roadNE;

    }

    private void setColorButonOnClick(ToggleButton button) {
        button.setOnAction((ActionEvent event) -> {
            button.setStyle("-fx-background-color: red");
        });
    }

    private void assignRoads(Polygon pol , Integer i) {
        if (hasRoads(i)) {
            mainAnchor.getChildren().add(createRoadN(pol)); 
            mainAnchor.getChildren().add(createRoadNE(pol)); 
            mainAnchor.getChildren().add(createRoadSE(pol)); 
        } else if (i == 8  || i == 14 || i == 21) {
            mainAnchor.getChildren().add(createRoadN(pol));
        } else if (i == 22) {
            mainAnchor.getChildren().add(createRoadSE(pol)); 
        } else if (i == 27) {
            mainAnchor.getChildren().add(createRoadN(pol)); 
            mainAnchor.getChildren().add(createRoadNE(pol));
        } else if (i == 28) {
            mainAnchor.getChildren().add(createRoadSE(pol)); 
        } else if (i == 34 || i == 35 ) {
            mainAnchor.getChildren().add(createRoadNE(pol));
            mainAnchor.getChildren().add(createRoadSE(pol));
        } else if (i == 32) {
            mainAnchor.getChildren().add(createRoadN(pol));
            mainAnchor.getChildren().add(createRoadNE(pol));
        } else if (i == 33) {
            mainAnchor.getChildren().add(createRoadSE(pol)); 
        } else if (i == 36) {
            mainAnchor.getChildren().add(createRoadNE(pol));
        }
    }

    private void assignSettlementNO(Polygon pol, Integer i) {
        Circle circle = new Circle();
        circle.setCenterX(100.0f);
        circle.setCenterY(100.0f);
        circle.setRadius(12.0f);
        circle.setLayoutX(pol.getLayoutX() - 158);
        circle.setLayoutY(pol.getLayoutY() - 135);
        onClickSettlement(circle);
        mainAnchor.getChildren().add(circle);
    }

    private void assignSettlementN(Polygon pol, Integer i) {
            Circle circle = new Circle();
            circle.setCenterX(100.0f);
            circle.setCenterY(100.0f);
            circle.setRadius(12.0f);
            circle.setLayoutX(pol.getLayoutX() - 101);
            circle.setLayoutY(pol.getLayoutY() - 170);
            onClickSettlement(circle);
            mainAnchor.getChildren().add(circle);
    }

    private void assignSettlements(Polygon pol, Integer i) {
        if(hasRoads(i) || i == 32) {
            assignSettlementN(pol, i);
            assignSettlementNO(pol, i);
        } else if (i == 8 || i == 14) {
            assignSettlementNO(pol, i);
        } else if (i == 22 || i == 27 || i == 28 || i > 32) {
            assignSettlementN(pol, i);
        } 
    }

    // Click sobre una carretera
    private void onClickSettlement(Circle circle) {
        circle.setFill(Color.WHITE);
        circle.setOnMouseClicked(
        new EventHandler<MouseEvent>(){
            public void handle(MouseEvent e){
                circle.setFill(Color.RED);
            }
        });
    }


    private void cardsPopUp() {
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefSize(500, 350);
        anchorPane.setStyle("-fx-background-color:  #965d62; -fx-background-radius: 12px" );
        popupCards = new Popup();
        popupCards.getContent().add(anchorPane);
        popupCards.setAutoHide(true);


        // Titulo
        Text title = new Text(10, 50, (LangService.getMapping("gameplay_cards")));
        title.setFont(new Font(40));
        title.setLayoutX(anchorPane.getLayoutX() + 55 );
        title.setLayoutY(anchorPane.getLayoutY() + 50);
        title.setFill(Color.WHITE);
        anchorPane.getChildren().add(title);
  
        // Carta caballero
        Text knight_card = new Text(10, 50, (LangService.getMapping("knight_card")) + "\t 2");
        knight_card.setFont(new Font(20));
        knight_card.setLayoutX(anchorPane.getLayoutX()+ 10);
        knight_card.setLayoutY(anchorPane.getLayoutY() + 100);
        knight_card.setFill(Color.WHITE);
        anchorPane.getChildren().add(knight_card);

        // Carta carretera
        Text road_cons_card = new Text(10, 50, (LangService.getMapping("road_cons_card")) + "\t 5");
        road_cons_card.setFont(new Font(20));
        road_cons_card.setLayoutX(anchorPane.getLayoutX()+ 10);
        road_cons_card.setLayoutY(anchorPane.getLayoutY() + 160);
        road_cons_card.setFill(Color.WHITE);
        anchorPane.getChildren().add(road_cons_card);

        // Carta descubrimiento
        Text discovery_card = new Text(10, 50, (LangService.getMapping("discovery_card")) + "\t 1");
        discovery_card.setFont(new Font(20));
        discovery_card.setLayoutX(anchorPane.getLayoutX()+ 10);
        discovery_card.setLayoutY(anchorPane.getLayoutY() + 220);
        discovery_card.setFill(Color.WHITE);
        anchorPane.getChildren().add(discovery_card);

        // Carta puntos victoria
        Text victory_points = new Text(10, 50, (LangService.getMapping("victory_points")) + "\t 8");
        victory_points.setFont(new Font(20));
        victory_points.setLayoutX(anchorPane.getLayoutX()+ 10);
        victory_points.setLayoutY(anchorPane.getLayoutY() + 280);
        victory_points.setFill(Color.WHITE);
        anchorPane.getChildren().add(victory_points);
       
        cards.setOnAction((ActionEvent event) -> {
            
            if (!popupCards.isShowing()) {
                Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
                popupCards.show(stage);
            }
            
         });
         
         cards.setText((LangService.getMapping("gameplay_cards")));

    }

    private void playersName() {
        player1Name.setText(player1);
        player2Name.setText(player2);
        player3Name.setText(player3);
        player4Name.setText(player4);
    }


    private void inTradePopUp() {
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefSize(400, 650);
        anchorPane.setStyle("-fx-background-color:  #965d62; -fx-background-radius: 12px" );
        popupInternalTrade = new Popup();
        popupInternalTrade.getContent().add(anchorPane);
        popupInternalTrade.setAutoHide(true);

        // Titulo
        Text title = new Text(10, 50, (LangService.getMapping("internal_trade")));
        title.setFont(new Font(40));
        title.setLayoutX(anchorPane.getLayoutX() + 10 );
        title.setLayoutY(anchorPane.getLayoutY() + 30);
        title.setFill(Color.WHITE);
        anchorPane.getChildren().add(title);

        // Selecciona un jugador para tradeo
        Text tofferPlayer = new Text(10, 50, "Seleccione un jugador");
        tofferPlayer.setFont(new Font(20));
        tofferPlayer.setLayoutX(anchorPane.getLayoutX() + 10 );
        tofferPlayer.setLayoutY(anchorPane.getLayoutY() + 100);
        tofferPlayer.setFill(Color.WHITE);
        anchorPane.getChildren().add(tofferPlayer);


        // Select jugador elegido
        offerPlayer = new ChoiceBox<>();
        offerPlayer.setStyle("-fx-background-radius: 12px;" );
        offerPlayer.getItems().add(player2);
        offerPlayer.getItems().add(player3);
        offerPlayer.getItems().add(player4);
        offerPlayer.setValue(player2);
        offerPlayer.setLayoutX(anchorPane.getLayoutX() + 270);
        offerPlayer.setLayoutY(anchorPane.getLayoutY() + 127);
        anchorPane.getChildren().add(offerPlayer);


         // Selecciona un material para tradeo
         Text tofferMaterial = new Text(10, 50, "Material ofrecido");
         tofferMaterial.setFont(new Font(20));
         tofferMaterial.setLayoutX(anchorPane.getLayoutX() + 10 );
         tofferMaterial.setLayoutY(anchorPane.getLayoutY() + 180);
         tofferMaterial.setFill(Color.WHITE);
         anchorPane.getChildren().add(tofferMaterial);

         // Select material ofrecido
        offerMaterial = new ChoiceBox<>();
        offerMaterial.setStyle("-fx-background-radius: 12px;" );
        offerMaterial.getItems().add("Lana");
        offerMaterial.getItems().add("Madera");
        offerMaterial.getItems().add("Cereales");
        offerMaterial.getItems().add("Arcilla");
        offerMaterial.getItems().add("Mineral");
        offerMaterial.setValue("Lana");
        offerMaterial.setLayoutX(anchorPane.getLayoutX() + 270);
        offerMaterial.setLayoutY(anchorPane.getLayoutY() + 210);
        anchorPane.getChildren().add(offerMaterial);

        // Cantidad de material ofrecido
        Text tofferAmmount = new Text(10, 50, "Cantidad ofrecida");
        tofferAmmount.setFont(new Font(20));
        tofferAmmount.setLayoutX(anchorPane.getLayoutX() + 10 );
        tofferAmmount.setLayoutY(anchorPane.getLayoutY() + 260);
        tofferAmmount.setFill(Color.WHITE);
        anchorPane.getChildren().add(tofferAmmount);



         // Spinner cantidad material ofrecido
         Spinner<Integer> spinnerGive = new Spinner(1, 250, 1);
         spinnerGive.setStyle("-fx-background-radius: 12px;" );
         spinnerGive.setPrefSize(75, 25);
         spinnerGive.setLayoutX(anchorPane.getLayoutX() + 270 );
         spinnerGive.setLayoutY(anchorPane.getLayoutY() + 290);
         anchorPane.getChildren().add(spinnerGive);
 
        

        // Selecciona un material para tradeo (recibir)
        Text treceiveMaterial = new Text(10, 50, "Material solicitado");
        treceiveMaterial.setFont(new Font(20));
        treceiveMaterial.setLayoutX(anchorPane.getLayoutX() + 10 );
        treceiveMaterial.setLayoutY(anchorPane.getLayoutY() + 343);
        treceiveMaterial.setFill(Color.WHITE);
        anchorPane.getChildren().add(treceiveMaterial);
        
        
        // Material solicitado
         receiveMaterial = new ChoiceBox<>();
         receiveMaterial.setStyle("-fx-background-radius: 12px;" );
         receiveMaterial.getItems().add("Lana");
         receiveMaterial.getItems().add("Madera");
         receiveMaterial.getItems().add("Cereales");
         receiveMaterial.getItems().add("Arcilla");
         receiveMaterial.getItems().add("Mineral");
         receiveMaterial.setValue("Lana");
         receiveMaterial.setLayoutX(anchorPane.getLayoutX() + 270);
         receiveMaterial.setLayoutY(anchorPane.getLayoutY() + 373);
         anchorPane.getChildren().add(receiveMaterial);
 
         // Cantidad de material ofrecido
         Text treceiveAmmount = new Text(10, 50, "Cantidad solicitada");
         treceiveAmmount.setFont(new Font(20));
         treceiveAmmount.setLayoutX(anchorPane.getLayoutX() + 10 );
         treceiveAmmount.setLayoutY(anchorPane.getLayoutY() + 423);
         treceiveAmmount.setFill(Color.WHITE);
         anchorPane.getChildren().add(treceiveAmmount);
 
 
        // Spinner cantidad material solicitado
        Spinner<Integer> spinnerReceive = new Spinner(1, 250, 1);
        spinnerReceive.setStyle("-fx-background-radius: 12px;" );
        spinnerReceive.setPrefSize(75, 25);
        spinnerReceive.setLayoutX(anchorPane.getLayoutX() + 270 );
        spinnerReceive.setLayoutY(anchorPane.getLayoutY() + 453);
        anchorPane.getChildren().add(spinnerReceive);
   
        


         // Boton enviar solicitud tradeo
        sendTrade = new Button();
        sendTrade.setPrefSize(180,90);
        sendTrade.setLayoutX(anchorPane.getLayoutX() + 100);
        sendTrade.setLayoutY(anchorPane.getLayoutY() + 520);
        sendTrade.setStyle("-fx-background-color: #c7956d; -fx-background-radius: 12px");
        sendTrade.setText("Aceptar");
        DropShadow shadow = new DropShadow();
        sendTrade.setEffect(shadow);



        // TODO: Añadir accion cuando se hace click sobre boton compra
        sendTrade.setOnAction((ActionEvent event) -> {
            System.out.println(spinnerGive.getValue().toString());
            System.out.println(spinnerReceive.getValue().toString());
            popupInternalTrade.hide();
        });

        anchorPane.getChildren().add(sendTrade);


        // Boton para acceder al popup
        inTrade.setOnAction((ActionEvent event) -> {
            
            if (!popupInternalTrade.isShowing()) {
                Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
                popupInternalTrade.show(stage);
            }
            
         });
         
         inTrade.setText((LangService.getMapping("internal_trade")));
    }


    private void externalTradePopUp() {
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefSize(500, 400);
        anchorPane.setStyle("-fx-background-color:  #965d62; -fx-background-radius: 12px" );
        popupExternalTrade = new Popup();
        popupExternalTrade.getContent().add(anchorPane);
        popupExternalTrade.setAutoHide(true);

        // Titulo
       Text title = new Text(10, 50, (LangService.getMapping("external_trade")));
        title.setFont(new Font(40));
        title.setLayoutX(anchorPane.getLayoutX() + 55 );
        title.setLayoutY(anchorPane.getLayoutY() + 50);
        title.setFill(Color.WHITE);
        anchorPane.getChildren().add(title);


        // Selecciona una Ratio
        Text tratio = new Text(10, 50, "Seleccione una ratio");
        tratio.setFont(new Font(20));
        tratio.setLayoutX(anchorPane.getLayoutX() + 10 );
        tratio.setLayoutY(anchorPane.getLayoutY() + 100);
        tratio.setFill(Color.WHITE);
        anchorPane.getChildren().add(tratio);


        // Select jugador elegido
        ratio = new ChoiceBox<>();
        ratio.setStyle("-fx-background-radius: 12px;" );
        ratio.getItems().add("2:1");
        ratio.getItems().add("3:1");
        ratio.getItems().add("4:1");
        ratio.setValue("2:1");
        ratio.setLayoutX(anchorPane.getLayoutX() + 270);
        ratio.setLayoutY(anchorPane.getLayoutY() + 127);
        anchorPane.getChildren().add(ratio);


        // Selecciona un material para tradeo
        Text tofferMaterial = new Text(10, 50, "Material ofrecido");
        tofferMaterial.setFont(new Font(20));
        tofferMaterial.setLayoutX(anchorPane.getLayoutX() + 10 );
        tofferMaterial.setLayoutY(anchorPane.getLayoutY() + 150);
        tofferMaterial.setFill(Color.WHITE);
        anchorPane.getChildren().add(tofferMaterial);

        // Select material ofrecido
       offerMaterial = new ChoiceBox<>();
       offerMaterial.setStyle("-fx-background-radius: 12px;" );
       offerMaterial.getItems().add("Lana");
       offerMaterial.getItems().add("Madera");
       offerMaterial.getItems().add("Cereales");
       offerMaterial.getItems().add("Arcilla");
       offerMaterial.getItems().add("Mineral");
       offerMaterial.setValue("Lana");
       offerMaterial.setLayoutX(anchorPane.getLayoutX() + 270);
       offerMaterial.setLayoutY(anchorPane.getLayoutY() + 180);
       anchorPane.getChildren().add(offerMaterial);

        // Selecciona un material para tradeo (recibir)
        Text treceiveMaterial = new Text(10, 50, "Material solicitado");
        treceiveMaterial.setFont(new Font(20));
        treceiveMaterial.setLayoutX(anchorPane.getLayoutX() + 10 );
        treceiveMaterial.setLayoutY(anchorPane.getLayoutY() + 200);
        treceiveMaterial.setFill(Color.WHITE);
        anchorPane.getChildren().add(treceiveMaterial);
        
        
        // Material solicitado
         receiveMaterial = new ChoiceBox<>();
         receiveMaterial.setStyle("-fx-background-radius: 12px;" );
         receiveMaterial.getItems().add("Lana");
         receiveMaterial.getItems().add("Madera");
         receiveMaterial.getItems().add("Cereales");
         receiveMaterial.getItems().add("Arcilla");
         receiveMaterial.getItems().add("Mineral");
         receiveMaterial.setValue("Lana");
         receiveMaterial.setLayoutX(anchorPane.getLayoutX() + 270);
         receiveMaterial.setLayoutY(anchorPane.getLayoutY() + 230);
         anchorPane.getChildren().add(receiveMaterial);

        // Boton enviar solicitud tradeo
        sendTradeExternal = new Button();
        sendTradeExternal.setPrefSize(180,90);
        sendTradeExternal.setLayoutX(anchorPane.getLayoutX() + 160);
        sendTradeExternal.setLayoutY(anchorPane.getLayoutY() + 300);
        sendTradeExternal.setStyle("-fx-background-color: #c7956d; -fx-background-radius: 12px");
        sendTradeExternal.setText("Aceptar");
        DropShadow shadow = new DropShadow();
        sendTradeExternal.setEffect(shadow);



        // TODO: Añadir accion cuando se hace click sobre boton compra
        sendTradeExternal.setOnAction((ActionEvent event) -> {
            popupExternalTrade.hide();
        });

        anchorPane.getChildren().add(sendTradeExternal);

        // Boton para acceder al popup
        outTrade.setOnAction((ActionEvent event) -> {
            
            if (!popupExternalTrade.isShowing()) {
                Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
                popupExternalTrade.show(stage);
            }
            
         });
         
         outTrade.setText((LangService.getMapping("external_trade")));


    }

 
    private void settingsPopup() throws IOException {
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefSize(200, 250);
        anchorPane.setStyle("-fx-background-color:  #965d62; -fx-background-radius: 12px" );
        popupSettings = new Popup();
        popupSettings.getContent().add(anchorPane);
        popupSettings.setAutoHide(true);

        leaveGame = new Button();
        leaveGame.setPrefSize(150,90);
        leaveGame.setLayoutX(anchorPane.getLayoutX() + 25);
        leaveGame.setLayoutY(anchorPane.getLayoutY() + 30);
        leaveGame.setStyle("-fx-background-color: #c7956d; -fx-background-radius: 12px");
        leaveGame.setText("Abandonar partida");
        DropShadow shadow = new DropShadow();
        leaveGame.setEffect(shadow);
        anchorPane.getChildren().add(leaveGame);

        stopGame = new Button();
        stopGame.setPrefSize(150,90);
        stopGame.setLayoutX(anchorPane.getLayoutX() + 25);
        stopGame.setLayoutY(anchorPane.getLayoutY() + 140);
        stopGame.setStyle("-fx-background-color: #c7956d; -fx-background-radius: 12px");
        stopGame.setText("Solicitud fin partida");
        stopGame.setEffect(shadow);
        anchorPane.getChildren().add(stopGame);


        leaveGame.setOnAction((ActionEvent event) -> {
            try {
                popupSettings.hide();
                App.nuevaPantalla("/view/mainMenu.fxml");
            } catch (IOException e) {
        
            }
            
        });

        stopGame.setOnAction((ActionEvent event) -> {
            popupSettings.hide();
        });

        settingsButton.setOnAction((ActionEvent event) -> {
            if (!popupSettings.isShowing()) {
                Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
                popupSettings.show(stage);
            }
         });
        settingsButton.setText((LangService.getMapping("settings"))); 
         
    }

    private void updateDice() {
        // TODO: Conectar con backend
        diceButton.setText("Dados: 8");
    }

    

    
    
    @FXML
    public void initialize() throws IOException {
        chatContent.setEditable(false);
        chatContent.setMouseTransparent(true);
        chatContent.setFocusTraversable(false);    
        cardsPopUp();
        inTradePopUp();
        externalTradePopUp();
        playersName();
        updateDice();
        settingsPopup();
        passTurnButton.setText((LangService.getMapping("next_turn")));
        
       
        // Crear hexagonos
        for(Integer i =0; i < hexagons.length; i++) {
            Polygon pol = createHexagon(i);
            fillHexagon(pol, i);    
            mainAnchor.getChildren().add(pol);
            if(hasRoads(i)) {
                Text t = new Text(10, 50, i.toString());
                t.setBoundsType(TextBoundsType.VISUAL); 
                t.setFont(new Font(20));
                t.setLayoutX(pol.getLayoutX() - 25);
                t.setLayoutY(pol.getLayoutY() - 49);
                t.setFill(Color.WHITE);
                mainAnchor.getChildren().add(t); 
            }

           // Text t = new Text(10, 50, "");
           // t.setLayoutX(mainAnchor.getLayoutX() + 1150);
           // t.setLayoutY(mainAnchor.getLayoutY() + 200);
           // mainAnchor.getChildren().add(t); 
            assignRoads(pol, i);
            assignSettlements(pol,i);
            hexagons[i] = pol;
        }
    } 

    

}

