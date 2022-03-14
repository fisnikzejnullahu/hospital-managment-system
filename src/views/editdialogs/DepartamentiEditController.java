/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views.editdialogs;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import model.Departamenti;

/**
 * FXML Controller class
 *
 * @author Fisnik Zejnullahu
 */
public class DepartamentiEditController implements Initializable {

    @FXML
    private JFXTextField depIdFld;
    @FXML
    private JFXTextField emriFld;
    @FXML
    private JFXTextField spitaliFld;
    @FXML
    private JFXTextField numriTotalFld;
    @FXML
    private JFXButton ruajeBtn;

    private Departamenti departamenti;
    @FXML
    private JFXDatePicker datakrijimitFld;
    @FXML
    private Label emriErrorLbl;
    @FXML
    private Label depErrorLbl;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
    public void setDepartamenti(Departamenti d){
        departamenti = d;
    }
    
    public void editDepartamenti(){
        depIdFld.setText(String.valueOf(departamenti.getId()));
        emriFld.setText(departamenti.getEmri());
        spitaliFld.setText(departamenti.getSpitaliId().getEmri());
        numriTotalFld.setText(String.valueOf(departamenti.getPunetoriCollection().size()));
        LocalDate datafillimitDate = new java.sql.Date(departamenti.getDataKrijimit().getTime()).toLocalDate();
        datakrijimitFld.setValue(datafillimitDate);
    }
    
    public Departamenti updateDepartamenti() {
        if (emriFld.getText().trim().isEmpty()){
            showErrorNameLabel("Ju lutem plotesoni kete fushe!");
            return null;
        }
        else {
            emriErrorLbl.setVisible(false);
            emriFld.setStyle("-fx-text-fill: #485460");
            emriFld.setFocusColor(Color.valueOf("#485460"));
        }
        if (datakrijimitFld.getValue() == null){
            depErrorLbl.setVisible(true);
            datakrijimitFld.setDefaultColor(Color.RED);
            datakrijimitFld.requestFocus();
            return null;
        }
        else {
            depErrorLbl.setVisible(false);
            datakrijimitFld.setDefaultColor(Color.valueOf("#4059a9"));
        }
        
        departamenti.setEmri(emriFld.getText());
        departamenti.setDataKrijimit(java.sql.Date.valueOf(datakrijimitFld.getValue()));
        return departamenti;
    }

    public JFXTextField getEmriFld() {
        return emriFld;
    }
    
    public void showErrorNameLabel(String errorTekst){
        emriErrorLbl.setText(errorTekst);
        emriErrorLbl.setVisible(true);
        emriFld.setFocusColor(Color.RED);
        emriFld.requestFocus();
        emriFld.setStyle("-fx-text-fill: #ff0000");
    }
    
    
}
