/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.Perdoruesi;

/**
 * FXML Controller class
 *
 * @author Fisnik Zejnullahu
 */
public class DashboardController implements Initializable{

    @FXML
    private StackPane mainPane;
    @FXML
    private JFXButton ballinaBtn;
    @FXML
    private JFXButton pacientatBtn;
    
    private JFXButton[] navButtons;
    @FXML
    private AnchorPane showPane;
    
    private Node homePane, pacientetPane, punetoretPane, departamentetPane, raportetPane, semundjetTabelaPane;
    @FXML
    private JFXButton punetoretBtn;
    @FXML
    private JFXButton departamentiBtn;
    @FXML
    private JFXButton raporteBtn;
    @FXML
    private Label adminLbl;
    
    private Perdoruesi loggedInPerdoruesi;
    @FXML
    private JFXButton semundjetBtn;
    @FXML
    private JFXButton logoutBtn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        navButtons = new JFXButton[7];
        navButtons[0] = ballinaBtn;
        navButtons[1] = pacientatBtn;
        navButtons[2] = punetoretBtn;
        navButtons[3] = departamentiBtn;
        navButtons[4] = semundjetBtn;
        navButtons[5] = raporteBtn;
        navButtons[6] = logoutBtn;
        
        loadPanes();
    }    

    @FXML
    private void showPane(ActionEvent event) {
        for (JFXButton jfx : navButtons){
            jfx.getStyleClass().remove("selected");
        }
        if (event.getSource() == ballinaBtn){
            showPane.getChildren().clear();
            showPane.getChildren().add(homePane);
            ballinaBtn.getStyleClass().add("selected");
        }
        else if (event.getSource() == pacientatBtn){
            showPane.getChildren().clear();
            showPane.getChildren().add(pacientetPane);
            pacientatBtn.getStyleClass().add("selected");
        }
        else if (event.getSource() == punetoretBtn){
            if (!showPane.getChildren().contains(punetoretPane)){
                PunetoriController.reset();
            }
            showPane.getChildren().clear();
            showPane.getChildren().add(punetoretPane);
            punetoretBtn.getStyleClass().add("selected");
        }
        else if (event.getSource() == semundjetBtn){
            showPane.getChildren().clear();
            showPane.getChildren().add(semundjetTabelaPane);
            semundjetBtn.getStyleClass().add("selected");
        }
        else if (event.getSource() == departamentiBtn){
            showPane.getChildren().clear();
            showPane.getChildren().add(departamentetPane);
            departamentiBtn.getStyleClass().add("selected");
        }
        else if (event.getSource() == logoutBtn){
            showPane.getChildren().clear();
            showPane.getChildren().add(homePane);
            ballinaBtn.getStyleClass().add("selected");
            logout();
        }
        else{
            if (event.getSource() == raporteBtn){
                showPane.getChildren().clear();
                showPane.getChildren().add(raportetPane);
                raporteBtn.getStyleClass().add("selected");
            }
        }
    }

    private void loadPanes(){
        try {
            homePane = FXMLLoader.load(getClass().getResource("/views/home.fxml"));
            showPane(homePane);
            pacientetPane = FXMLLoader.load(getClass().getResource("/views/pacientet.fxml"));
            punetoretPane = FXMLLoader.load(getClass().getResource("/views/punetori.fxml"));
            departamentetPane = FXMLLoader.load(getClass().getResource("/views/departamentet.fxml"));
            semundjetTabelaPane = FXMLLoader.load(getClass().getResource("/views/semundjet_tabela.fxml"));
            raportetPane = FXMLLoader.load(getClass().getResource("/views/gjenero_raporte.fxml"));
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void showPane(Node node) {
        showPane.getChildren().clear();
        showPane.getChildren().add(node);
    }

    public void setPerdoruesi(Perdoruesi loggedInPerdoruesi) {
        this.loggedInPerdoruesi = loggedInPerdoruesi;
        adminLbl.setText(loggedInPerdoruesi.getUsername());
        
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/views/gjenero_raporte.fxml"));
        try {
            fxmlLoader.load();
        } catch (IOException ex) {}
        RaporteController controller = fxmlLoader.getController();
        controller.setPerdoruesi(loggedInPerdoruesi);
    }

    public Perdoruesi getLoggedInPerdoruesi() {
        return loggedInPerdoruesi;
    }    

    public JFXButton getPacientatBtn() {
        return pacientatBtn;
    }

    public AnchorPane getShowPane() {
        return showPane;
    }

    private void logout() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/views/login.fxml"));
        Parent loginPane = null;
        try {
            loginPane = fxmlLoader.load();
        } catch (IOException ex) {}
        LoginController controller = fxmlLoader.getController();
        controller.getUsernameFld().setText(loggedInPerdoruesi.getUsername());
        Scene scene = new Scene(loginPane);
        Stage stage = (Stage) mainPane.getScene().getWindow();
        stage.hide();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.show();
    }
}
