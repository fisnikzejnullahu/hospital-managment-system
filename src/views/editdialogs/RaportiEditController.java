/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views.editdialogs;

import alerts.CustomAlert;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import exceptions.SpitaliDbException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import model.Raporti;
import model.dao.RaportiDAO;
import model.dao.implementations.RaportiDAOImpl;
import model.tablemodels.RaportiTableModel;

/**
 * FXML Controller class
 *
 * @author Fisnik Zejnullahu
 */
public class RaportiEditController implements Initializable {

    @FXML
    private JFXButton ruajeBtn;
    @FXML
    private Label emriErrorLbl;
    @FXML
    private Label dataErrorLbl;
    @FXML
    private JFXDialogLayout mainPane;
    @FXML
    private JFXTextField rapIdFld;
    @FXML
    private JFXDatePicker dataLeshimitFld;
    @FXML
    private JFXTextField punetoriIdFld;
    @FXML
    private JFXComboBox<String> llojiRaportitComboBox;
    
    private RaportiTableModel raporti;
    @FXML
    private Label errorDataLbl;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    public void setRaporti(RaportiTableModel r){
        raporti = r;
    }
    
    public void editRaporti(){
        rapIdFld.setText(String.valueOf(raporti.getRaportiId()));
        punetoriIdFld.setText(raporti.getPunetori());
        LocalDate datafillimitDate = LocalDate.parse(raporti.getDataLeshimit());
        dataLeshimitFld.setValue(datafillimitDate);
    }
    
    public Raporti updateRaporti(){
        dataErrorLbl.setVisible(false);
        if (dataLeshimitFld.getValue() == null){
            dataErrorLbl.setVisible(true);
            return null;
        }
        RaportiDAO raportiDAO = new  RaportiDAOImpl();
        try {
            Raporti r = raportiDAO.get(Integer.parseInt(rapIdFld.getText()));
            r.setDataLeshimit((java.sql.Date.valueOf(dataLeshimitFld.getValue())));
            r.setLlojiRaportit(llojiRaportitComboBox.getValue());;
            raportiDAO.edit(r);
            return raportiDAO.get(r.getId());
        } catch (SpitaliDbException ex) {
            CustomAlert.showAlertError(ex, mainPane.getScene());
            return null;
        }
    }
    
    public void createRaporti(){
        RaportiDAO raportiDAO = new  RaportiDAOImpl();
        try {
            Raporti r = new Raporti();
            r.setDataLeshimit((java.sql.Date.valueOf(dataLeshimitFld.getValue())));
            r.setLlojiRaportit(llojiRaportitComboBox.getValue());
            raportiDAO.create(r);
//            SemundjetTabelaController.addSemundjaToTable(r);
        } catch (SpitaliDbException ex) {
            CustomAlert.showAlertError(ex, mainPane.getScene());
        }
    }
    
    public void deleteRaporti(int id){
        RaportiDAO raportiDAO = new  RaportiDAOImpl();
        try {
            raportiDAO.delete(id);
        } catch (SpitaliDbException ex) {
            CustomAlert.showAlertError(ex, mainPane.getScene());
        }
    }

    public JFXButton getRuajeBtn() {
        return ruajeBtn;
    }

    public void setRuajeBtn(JFXButton ruajeBtn) {
        this.ruajeBtn = ruajeBtn;
    }

    public Label getEmriErrorLbl() {
        return emriErrorLbl;
    }

    public void setEmriErrorLbl(Label emriErrorLbl) {
        this.emriErrorLbl = emriErrorLbl;
    }

    public Label getDataErrorLbl() {
        return dataErrorLbl;
    }

    public void setDataErrorLbl(Label dataErrorLbl) {
        this.dataErrorLbl = dataErrorLbl;
    }

    public JFXDialogLayout getMainPane() {
        return mainPane;
    }

    public void setMainPane(JFXDialogLayout mainPane) {
        this.mainPane = mainPane;
    }

    public JFXTextField getRapIdFld() {
        return rapIdFld;
    }

    public void setRapIdFld(JFXTextField rapIdFld) {
        this.rapIdFld = rapIdFld;
    }

    public JFXDatePicker getDataLeshimitFld() {
        return dataLeshimitFld;
    }

    public void setDataLeshimitFld(JFXDatePicker dataLeshimitFld) {
        this.dataLeshimitFld = dataLeshimitFld;
    }

    public JFXTextField getPunetoriIdFld() {
        return punetoriIdFld;
    }

    public void setPunetoriIdFld(JFXTextField punetoriIdFld) {
        this.punetoriIdFld = punetoriIdFld;
    }

    public JFXComboBox<String> getLlojiRaportitComboBox() {
        return llojiRaportitComboBox;
    }

    public void setLlojiRaportitComboBox(JFXComboBox<String> llojiRaportitComboBox) {
        this.llojiRaportitComboBox.setItems(llojiRaportitComboBox.getItems());
        this.llojiRaportitComboBox.setValue(raporti.getLlojiRaportit());
    }
    
    
    
    
}
