/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import alerts.CustomAlert;
import com.jfoenix.controls.IFXTextInputControl;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import exceptions.SpitaliDbException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Perdoruesi;
import model.dao.implementations.LoginValidator;
import net.sf.jasperreports.engine.JRException;
import reports.ReportGenerator;

/**
 *
 * @author Fisnik Zejnullahu
 */
public class LoginController implements Initializable{
    @FXML
    private StackPane mainStackPane;
    @FXML
    private JFXTextField usernameFld;
    @FXML
    private JFXPasswordField passwordFld;
    @FXML
    private AnchorPane loginPane1;
    @FXML
    private AnchorPane loginPane2;
    @FXML
    private Label perdoruesiLbl;

    private Perdoruesi loggedInPerdoruesi;
    @FXML
    private JFXButton vazhdoBtn, loginBtn;
    @FXML
    private Label errorLabel;
    @FXML
    private Label errorPwLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loginPane1.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                loginPane1.requestFocus();
                loginPane2.requestFocus();
            }
        });
        loginPane2.setOnMouseClicked(loginPane1.getOnMouseClicked());
        usernameFld.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (vazhdoBtn.getContentDisplay() != ContentDisplay.GRAPHIC_ONLY){
                    if (!newValue.trim().isEmpty()){
                        vazhdoBtn.setDisable(false);
                    }
                    else {
                        vazhdoBtn.setDisable(true);
                    }
                }
            }
        });
        passwordFld.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.trim().isEmpty()){
                    loginBtn.setDisable(false);
                }
                else {
                    loginBtn.setDisable(true);
                }
            }
        });
        usernameFld.setOnAction(e-> {vazhdoBtn.fire();});
        passwordFld.setOnAction(e-> {loginBtn.fire();});
        usernameFld.requestFocus();
    }
    @FXML
    public void tryLogin(){
        showRollingImg();
        errorLabel.setVisible(false);
        try {
            startThread();
        } catch (SpitaliDbException ex) {
            CustomAlert.showAlertError(ex, mainStackPane.getScene());
        }
    }

    private void next(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                hideRollingImg();
                if (loggedInPerdoruesi != null) {
                    perdoruesiLbl.setText(usernameFld.getText());
                    usernameFld.setFocusColor(Color.valueOf("#00b0fd"));

                    TranslateTransition swipeTransition = new TranslateTransition(Duration.millis(350));
                    swipeTransition.setNode(loginPane1);
                    swipeTransition.setToX(-loginPane2.getPrefWidth());
                    swipeTransition.play();
                    passwordFld.requestFocus();
                }
                else{
                    errorLabel.setText("   Perdoruesi nuk ekziston!");
                    errorLabel.setVisible(true);
                    clearInput(usernameFld);
                    usernameFld.requestFocus();;
                }
            }
        });
    }

    private void startThread() throws SpitaliDbException{
        LoginValidator validator = new LoginValidator();

        class ValidateThread extends Thread {
            @Override
            public void run(){
                try {
                    loggedInPerdoruesi = validator.validate(usernameFld.getText());
                    next();
                } catch (SpitaliDbException ex) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            errorLabel.setText(ex.getMessage());
                            errorLabel.setVisible(true);
                            hideRollingImg();
                        }
                    });
                }
            }
        }
        new ValidateThread().start();
    }

    private void showRollingImg(){
        vazhdoBtn.setDisable(true);
        vazhdoBtn.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
    }

    private void hideRollingImg(){
        vazhdoBtn.setDisable(false);
        vazhdoBtn.setContentDisplay(ContentDisplay.TEXT_ONLY);
    }

    @FXML
    private void backLogin1(MouseEvent event) {
        TranslateTransition swipeTransition = new TranslateTransition(Duration.millis(350));
        swipeTransition.setNode(loginPane1);
        swipeTransition.setToX(0);
        swipeTransition.play();
        usernameFld.requestFocus();
    }

    @FXML
    private void login(ActionEvent event) throws IOException {
        errorPwLabel.setVisible(false);
        if (loggedInPerdoruesi.getPassword().equals(passwordFld.getText())){
//            showLoadingImg();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/views/dashboard.fxml"));
            Parent homePane = fxmlLoader.load();
            DashboardController controller = fxmlLoader.getController();
            controller.setPerdoruesi(loggedInPerdoruesi);
            Scene scene = new Scene(homePane);
            Stage stage = (Stage) mainStackPane.getScene().getWindow();
            stage.hide();
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.setResizable(true);
            stage.show();
        }
        else{
            errorPwLabel.setVisible(true);
            clearInput(passwordFld);
            passwordFld.requestFocus();
        }
    }

    private void clearInput(Node input){
        TranslateTransition tt = new TranslateTransition(Duration.millis(120), input);
        tt.setFromX(-20f);
        tt.setToX(0);
        tt.setCycleCount(4);
        tt.playFromStart();
        TextField tf = (TextField)input;
        tf.clear();
        IFXTextInputControl ift = (IFXTextInputControl) input;
        ift.setFocusColor(Color.valueOf("#fc0d00"));
    }
    private void showLoadingImg() {
//        Thread thr = new Thread(){
//            @Override
//            public void run() {
//                generator = new ReportGenerator();
//                generator.setPunetori(punetoriTM);
//                try {
//                    generator.exportPunetoriReport();
//                } catch (JRException | SpitaliDbException | SQLException ex) {
//                    Platform.runLater(new Runnable(){
//                        @Override
//                        public void run(){
//                            hideLoadingImg();
//                            showAlertError(ex);
//                        }
//                    });
//                    return;
//                }
//                hideLoadingImg();
//                Platform.runLater(new Runnable(){
//                    @Override
//                    public void run(){
//                        hideLoadingImg();
//                        showConfirmReportDialog(generator);
//                    }
//                });
//            }
//        };
//        thr.start();
    }

    public JFXTextField getUsernameFld() {
        return usernameFld;
    }


}
