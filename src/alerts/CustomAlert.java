/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alerts;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import exceptions.SpitaliDbException;
import java.util.Calendar;
import javafx.animation.PauseTransition;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author Fisnik Zejnullahu
 */
public class CustomAlert {
    
    public static void showAlertError(Exception exception, Scene scene){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Sistemi per kujdesin mjekësor - " + Calendar.getInstance().get(Calendar.YEAR));
        Stage stage = (Stage) scene.getWindow();
        stage.getIcons().add(new Image("file:src/images/logo.png"));
        
        if (exception instanceof SpitaliDbException){
            SpitaliDbException ex = (SpitaliDbException)exception;
            alert.setHeaderText("NDODHI ERROR!!!");
            if (ex.getErrorCode() != -9999){
                alert.setHeaderText("NDODHI ERROR!!!, ERROR CODE: "+ex.getErrorCode());
            }
            alert.setContentText(ex.getMessage());
            alert.show();
        }
        else {
            alert.setHeaderText("NDODHI ERROR!!!");
            alert.setContentText(exception.getMessage());
            alert.show();
        }
    }
    
    public static void showSimpleInformationAlert(Scene scene, String headerText, String contentText){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("file:src/images/logo.png"));
        alert.showAndWait();
    }
    
    public static void showFinishedDialog(StackPane stackPane, String text, String message){
        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(2.2));
        Text successTxt = new Text(message);
        successTxt.setFill(Color.valueOf("#55ba50"));
        
        Text emriPun = new Text();
        emriPun.setText(text);
        
        HBox hbox = new HBox(emriPun, successTxt);
        
        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        dialogLayout.setBody(hbox);
        dialogLayout.setPrefWidth(250);
        dialogLayout.setPrefHeight(140);
                
        JFXDialog dialog = new JFXDialog(stackPane, dialogLayout, JFXDialog.DialogTransition.TOP);
        stackPane.setDisable(true);
        dialog.show();
        pauseTransition.setOnFinished(e -> {
            dialog.close(); 
            stackPane.setDisable(false);
        });
        pauseTransition.play();
    }
}
