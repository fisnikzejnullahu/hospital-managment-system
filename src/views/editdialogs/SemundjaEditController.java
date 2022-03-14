/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views.editdialogs;

import alerts.CustomAlert;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import controllers.SemundjetController;
import controllers.SemundjetTabelaController;
import exceptions.SpitaliDbException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import model.Pacienti;
import model.PacientiSemundja;
import model.PacientiSemundjaPK;
import model.Semundja;
import model.dao.PacientiSemundjaDAO;
import model.dao.SemundjaDAO;
import model.dao.implementations.PacientiSemundjaDAOImpl;
import model.dao.implementations.SemundjaDAOImpl;
import model.tablemodels.PacientiSemundjaTableModel;

/**
 * FXML Controller class
 *
 * @author Fisnik Zejnullahu
 */
public class SemundjaEditController implements Initializable {

    @FXML
    private JFXTextField emriFld;
    @FXML
    private JFXButton ruajeBtn;
    @FXML
    private JFXDatePicker datakrijimitFld;
    @FXML
    private Label emriErrorLbl;
    @FXML
    private Label dataErrorLbl;
    @FXML
    private JFXTextField semIdFld;
    @FXML
    private ToggleGroup niveliRrezikutToggle;
    @FXML
    private Label regjistrimLbl;
    
    @FXML
    private JFXDialogLayout mainPane;
    @FXML
    private JFXButton fshijeBtn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    //kur vjen ID null dmth osht tu u thirr prej semundjeveTabelaController qi na kallxon mos me bo visible daten se veq semundje osht
    //kur svjen NULL dmth osht prej semundjeveController te te dhenave mjekesore te pacientit
    public void editSemundja(PacientiSemundjaTableModel pm, String id, int niveliPosition){
        emriFld.setText(pm.getEmri());
        if (id == null){
            semIdFld.setText(String.valueOf(pm.getIdSemundjes()));
            datakrijimitFld.setVisible(false);
            ruajeBtn.setPrefWidth(354);
            ruajeBtn.setPrefHeight(60);
            ruajeBtn.setLayoutX(51);
            ruajeBtn.setLayoutY(ruajeBtn.getLayoutY()-60);
        }
        else {
            semIdFld.setText(id);
            datakrijimitFld.setValue(LocalDate.parse(pm.getDataSemundjes()));
        }
        ObservableList<Toggle> toggles = niveliRrezikutToggle.getToggles();
        niveliRrezikutToggle.selectToggle(toggles.get(niveliPosition-1));
    }
    
    public Semundja updateSemundja(){
        SemundjaDAO semundjaDAO = new  SemundjaDAOImpl();
        try {
            Semundja s = semundjaDAO.get(Integer.parseInt(semIdFld.getText()));
            s.setEmri(emriFld.getText());
            s.setNiveliRrezikut(getIntNiveliRr());
            semundjaDAO.edit(s);
            return semundjaDAO.get(s.getId());
        } catch (SpitaliDbException ex) {
            CustomAlert.showAlertError(ex, mainPane.getScene());
            return null;
        }
    }
    
    public void updatePacientiSemundja(Semundja semundja, Pacienti pacienti){
        PacientiSemundjaDAO pd = new PacientiSemundjaDAOImpl();
        try {
            PacientiSemundja ps = new PacientiSemundja();
            ps.setPacientiSemundjaPK(new PacientiSemundjaPK(pacienti.getId(), semundja.getId()));
            ps.setSemundja(semundja);
            ps.setPacienti(pacienti);
            ps.setDataSemundjes(java.sql.Date.valueOf(datakrijimitFld.getValue()));
            pd.edit(ps);
        } catch (SpitaliDbException ex) {
            CustomAlert.showAlertError(ex, mainPane.getScene());
        }
    }
    
    private int getIntNiveliRr(){
        String niveliRr = String.valueOf(((JFXRadioButton)niveliRrezikutToggle.getSelectedToggle()).getText());
        switch (niveliRr){
            case "I ulte":
                return 1;
            case "I mesem":
                return 2;
            case "I larte":
                return 3;
        }
        return -9;
    }
    
    public void createSemundja(){
        SemundjaDAO semundjaDAO = new  SemundjaDAOImpl();
        try {
            Semundja s = new Semundja();
            s.setEmri(emriFld.getText());
            s.setNiveliRrezikut(getIntNiveliRr());
            semundjaDAO.create(s);
            SemundjetTabelaController.addSemundjaToTable(s);
            SemundjetController.added.set(true);
        } catch (SpitaliDbException ex) {
            CustomAlert.showAlertError(ex, mainPane.getScene());
        }
    }
    
    public void deleteSemundja(int id){
        SemundjaDAO semundjaDAO = new  SemundjaDAOImpl();
        try {
            semundjaDAO.delete(id);
        } catch (SpitaliDbException ex) {
            CustomAlert.showAlertError(ex, mainPane.getScene());
        }
    }
    
    public void deleteSemundjaFromPacienti(int semundjaId, int pacientiId){
        PacientiSemundjaDAO pacientiSemundjaDAO = new PacientiSemundjaDAOImpl();
        try {
            pacientiSemundjaDAO.delete(semundjaId, pacientiId);
        } catch (SpitaliDbException ex) {
            CustomAlert.showAlertError(ex, mainPane.getScene());
        }
    }

    public JFXTextField getEmriFld() {
        return emriFld;
    }

    public JFXButton getRuajeBtn() {
        return ruajeBtn;
    }

    public JFXDatePicker getDatakrijimitFld() {
        return datakrijimitFld;
    }

    public Label getEmriErrorLbl() {
        return emriErrorLbl;
    }

    public Label getDataErrorLbl() {
        return dataErrorLbl;
    }

    public JFXTextField getSemIdFld() {
        return semIdFld;
    }

    public ToggleGroup getNiveliRrezikutToggle() {
        return niveliRrezikutToggle;
    }

    public Label getRegjLbl() {
        return regjistrimLbl;
    }

    public JFXButton getFshijeBtn() {
        return fshijeBtn;
    }
    
    
}
