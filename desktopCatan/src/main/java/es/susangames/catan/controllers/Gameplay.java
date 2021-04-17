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
//import java.awt.*;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.scene.input.MouseEvent;

import javafx.event.EventHandler;


public class Gameplay {
 
    private static double start_X_position = 780.0;
    private static double start_Y_position = 110.0;
    private static double horizontal_right_gap = 115;
    private static double horizontal_left_gap = 60;
    private static double vertical_gap = 120;

    private Polygon[] hexagons;
    private ToggleButton[] roads;
    private Image imgSea, imgDes, imgMou, imgFie, imgFor, imgHil; 
 
    @FXML
    private AnchorPane mainAnchor;

    public Gameplay() {
        hexagons = new Polygon[37];
        roads = new ToggleButton[57];
        imgSea = new Image("/img/sea.png");
        imgDes = new Image("/img/sand-desert.jpg"); 
        imgMou = new Image("/img/mountain.png");  
        imgFie = new Image("/img/fields.jpeg"); 
        imgFor = new Image("/img/forest.png"); 
        imgHil = new Image("/img/hills.jpeg"); 

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

    private void onClickSettlement(Circle circle) {
        circle.setFill(Color.WHITE);
        circle.setOnMouseClicked(
        new EventHandler<MouseEvent>(){
            public void handle(MouseEvent e){
                circle.setFill(Color.RED);
            }
        });
    }


    @FXML
    public void initialize() throws IOException {
       
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
           
            
            assignRoads(pol, i);
            assignSettlements(pol,i);
            hexagons[i] = pol;
        }
    } 

}

