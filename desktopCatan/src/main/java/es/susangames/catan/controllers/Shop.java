package es.susangames.catan.controllers;

import es.susangames.catan.service.LangService;
import es.susangames.catan.service.ShopService;
import es.susangames.catan.service.UserService;
import es.susangames.catan.controllers.MainMenu;
import es.susangames.catan.App;
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
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import com.jfoenix.controls.JFXListView;
import javafx.scene.paint.Color;
import java.util.Random;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import org.json.*;






public class Shop {
 
    @FXML
    private JFXListView<AnchorPane> elementsList;

    @FXML
    private Button buttonSkins;

    @FXML
    private Button buttonGameAspects;

    private Image goldImage;

    private ShopService shop;


    public Shop() {
        goldImage = new Image("/img/gold_icon.png");
        shop = new ShopService();
    }
    

    private void newShopElement(String name, Integer price, String imgURL, 
                                Boolean avatar) {
        
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefSize(300, 300);
        anchorPane.setStyle("-fx-background-color: #965d62");
        
        // Image icono
        Circle circle = new Circle();
        circle.setCenterX(110.0f);
        circle.setCenterY(110.0f);
        circle.setRadius(90.0f);
        anchorPane.setTopAnchor(circle, 70.0);
        anchorPane.setLeftAnchor(circle, 50.0);
        Image imgSkin;
        if(avatar) {
            imgSkin = new Image("/img/users/" +  imgURL);
        } else {
            imgSkin = new Image("/img/board/" +  imgURL);
        }
        circle.setFill(new ImagePattern(imgSkin));
        anchorPane.getChildren().add(circle);
        
        
        // Nombre icono
        Text tName = new Text(10, 50, name);
        tName.setFont(new Font(40));
        anchorPane.setTopAnchor(tName, 40.0);
        anchorPane.setLeftAnchor(tName, 215.0);
        tName.setFill(Color.WHITE);
        anchorPane.getChildren().add(tName);


        // Imagen oro 
        ImageView imageView = new ImageView(goldImage);
        anchorPane.setBottomAnchor(imageView, 40.0);
        anchorPane.setLeftAnchor(imageView, 215.0);
        imageView.setFitHeight(90);
        imageView.setFitWidth(90);
        anchorPane.getChildren().add(imageView);

        // Precio icono
        Text tPrice = new Text(10, 50, price.toString());
        tPrice.setFont(new Font(40));
        anchorPane.setBottomAnchor(tPrice, 40.0);
        anchorPane.setLeftAnchor(tPrice, 320.0);
        tPrice.setFill(Color.WHITE);
        anchorPane.getChildren().add(tPrice);


        // Boton compra
        Button buyButton = new Button();
        buyButton.setPrefSize(250,100);
        anchorPane.setBottomAnchor(buyButton, 100.0);
        anchorPane.setRightAnchor(buyButton, 100.0);


       
        buyButton.setStyle("-fx-background-color: #c7956d; -fx-background-radius: 12px");
        buyButton.setText((LangService.getMapping("shop_buy")));
        DropShadow shadow = new DropShadow();
        buyButton.setEffect(shadow);

        // TODO: Añadir accion cuando se hace click sobre boton compra
        buyButton.setOnAction((ActionEvent event) -> {
           shop.adquirirProducto(imgURL);
           if(avatar) {
              loadUserSkins();
           } else {
               loadGameAspects();
           } 
           MainMenu.updateCoins();
        });

        if (UserService.getSaldo() < price) {
            buyButton.setDisable(true);
        }

        anchorPane.getChildren().add(buyButton);
        elementsList.getItems().add(anchorPane);
    }


    private void loadUserSkins() {
        JSONArray skinList =  shop.obtenerProductosDisponibles();
        elementsList.getItems().clear();
        for (int i = 0; i < skinList.length(); i++) {
            JSONObject object = skinList.getJSONObject(i);
            String aux = object.getString("tipo").toString();
            if(!object.getBoolean("adquirido") && 
                aux.equals("AVATAR")) {
                    newShopElement(object.getString("nombre"),
                                   object.getInt("precio"),
                               object.getString("url"),
                               true);
              }
        }
    }


    private void loadGameAspects() {
        JSONArray skinList =  shop.obtenerProductosDisponibles();
        elementsList.getItems().clear();
        for (int i = 0; i < skinList.length(); i++) {
            JSONObject object = skinList.getJSONObject(i);
            String aux = object.getString("tipo").toString();
            if(!object.getBoolean("adquirido") && 
                aux.equals("APARIENCIA")) {
                    newShopElement(object.getString("nombre"),
                                   object.getInt("precio"),
                               object.getString("url"),
                               false);
              }
        }
    }

    @FXML
    public void initialize() throws IOException {
        buttonSkins.setText((LangService.getMapping("shop_user_skins")));
        buttonSkins.setOnAction((ActionEvent event) -> {
            loadUserSkins();
         });
        
        buttonGameAspects.setText((LangService.getMapping("shop_game_aspects")));
        buttonGameAspects.setOnAction((ActionEvent event) -> {
            loadGameAspects();
         });
        
        elementsList.getStylesheets().add("/css/shop.css"); 
        loadUserSkins();
        
    } 

    

}