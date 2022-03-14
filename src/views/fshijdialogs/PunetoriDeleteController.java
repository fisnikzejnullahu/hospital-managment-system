/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views.fshijdialogs;

import com.jfoenix.controls.JFXButton;
import exceptions.SpitaliDbException;
import java.util.Date;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import model.Punetori;
import model.dao.PunetoriDAO;
import model.dao.implementations.PunetoriDAOImpl;
import model.tablemodels.PunetoriTableModel;

/**
 *
 * @author Fisnik Zejnullahu
 */
public class PunetoriDeleteController {
    
    @FXML
    private JFXButton fshijBtn;
    @FXML
    private JFXButton mbyllBtn;
    @FXML
    private CheckBox fshijCheckBox;
    @FXML
    private Label emriPerTuFshireLbl;
    
    private PunetoriTableModel punetoriTM;
    private boolean uFshi;

    public void setPunetoriTableModel(PunetoriTableModel punetoriTM) {
        this.punetoriTM = punetoriTM;
        emriPerTuFshireLbl.setText(punetoriTM.getEmri() + " " +punetoriTM.getMbiemri());
        if (!punetoriTM.getDataMbarimit().equals("-")){
            fshijCheckBox.setSelected(true);
            fshijCheckBox.setDisable(true);
            fshijCheckBox.requestFocus();
        }
    }
    
    public boolean isSelectedFshijCheckBox(){
        return fshijCheckBox.isSelected();
    }
    
    public boolean uFshi(){
        return uFshi;
    }

    public void buildDialog(Dialog<ButtonType> dialog) throws SpitaliDbException{
        PunetoriDAO punetoriDAO = new PunetoriDAOImpl();
        fshijBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try 
                {
                    if (fshijCheckBox.isSelected()){
                        punetoriDAO.delete(punetoriTM.getId());
                        uFshi = true;
                    }
                    else {
                        Punetori get = punetoriDAO.get(punetoriTM.getId());
                        get.setDataMbarimit(new Date());
                        punetoriDAO.edit(get);
                        uFshi = true;
                    }
                }
                catch(SpitaliDbException ex){
                    try {
                        throw ex;
                    } catch (SpitaliDbException ex1) {}
                }
                dialog.close();
            }
        });
        
        mbyllBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialog.close();
            }
        });
    }
}
