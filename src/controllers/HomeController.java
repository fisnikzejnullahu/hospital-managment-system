/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import com.jfoenix.controls.JFXRippler;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

/**
 * FXML Controller class
 *
 * @author Fisnik Zejnullahu
 */
public class HomeController implements Initializable{

    @FXML
    private AnchorPane homePane;
    @FXML
    private AnchorPane pane1;
//    @FXML
//    private AnchorPane pane2;
//    @FXML
//    private AnchorPane pane3;
//    private AnchorPane[] panes = {pane1, pane2, pane3};

    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        buildRipplers(panes);
    }   
    
//    private void buildRipplers(AnchorPane[] panes) {
//        for (int i=0; i<panes.length; i++){
//            JFXRippler rippler = new JFXRippler(panes[i]);
//            rippler.setLayoutX(panes[i].getLayoutX());
//            rippler.setLayoutY(panes[i].getLayoutY());
//            rippler.setRipplerFill(Color.valueOf("#ff8c8c"));
//            homePane.getChildren().add(rippler);
//        }
//    }
}
