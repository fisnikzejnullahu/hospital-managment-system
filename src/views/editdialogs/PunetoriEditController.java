/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views.editdialogs;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import controllers.PunetoriController;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import model.Adresa;
import model.Departamenti;
import model.Punetori;
import model.Qyteti;

/**
 * FXML Controller class
 *
 * @author Fisnik Zejnullahu
 */
public class PunetoriEditController implements Initializable {

    @FXML
    private JFXTextField nrPersonalFld;
    @FXML
    private JFXTextField idFld;
    @FXML
    private JFXTextField emriFld;
    @FXML
    private JFXTextField emriBabesFld;
    @FXML
    private JFXTextField mbiemriFld;
    @FXML
    private JFXTextField adresaFld;
    @FXML
    private JFXTextField emailFld;
    @FXML
    private JFXComboBox<String> qytetiComboBox;
    @FXML
    private JFXComboBox<String> depComboBox;
    @FXML
    private JFXTextField shtetiFld;
    @FXML
    private JFXDatePicker datelindjaFld;
    @FXML
    private JFXDatePicker datafillimitFld;
    @FXML
    private JFXDatePicker datambarimitFld;
    @FXML
    private ToggleGroup gjiniaToggleGroup;
    @FXML
    private JFXTextField nrTelFld;

    private List<Departamenti> departamentet;
    private List<Qyteti> qytetet;
    
    private Punetori p;
    @FXML
    private JFXButton ruajeBtn;
    
    private ArrayList<Node> JFXnodes;
    @FXML
    private AnchorPane mainPane;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         buildJFXNodes();
    }
    
    public void removeErrorLabels(){
        ObservableList<Node> children = mainPane.getChildren();
        for (Node n : children){
            if (n instanceof Label){
                ((Label) n).setVisible(false);
            }
        }
    }
    
    
    private void buildJFXNodes(){
        JFXnodes = new ArrayList<>();
        JFXnodes.add(nrPersonalFld);
        JFXnodes.add(emriFld);
        JFXnodes.add(emriBabesFld);
        JFXnodes.add(mbiemriFld);
        JFXnodes.add(nrTelFld);
        JFXnodes.add(adresaFld);
        JFXnodes.add(emailFld);
        PunetoriController.buildJFXNodes(JFXnodes);
    }
    
    public void setPunetori(Punetori p){
        this.p = p;
    }

    public void setDepartamentet(List<Departamenti> departamentet) {
        this.departamentet = departamentet;
        ObservableList<String> departamentetObservableList = FXCollections.observableArrayList();
        for (Departamenti d : departamentet){
            departamentetObservableList.add(d.getEmri());
        }
        depComboBox.setItems(departamentetObservableList);
    }

    public void setQytetet(List<Qyteti> qytetet) {
        this.qytetet = qytetet;
        ObservableList<String> qytetetObservableList = FXCollections.observableArrayList();
        for (Qyteti q : qytetet){
            qytetetObservableList.add(q.getEmri());
        }
        qytetiComboBox.setItems(qytetetObservableList);
    }

    public void buildQytetet(ObservableList<String> qytetiList){
        qytetiComboBox.setItems(qytetiList);
    }
    
    public void buildDepartamentet(ObservableList<String> depList){
        depComboBox.setItems(depList);
    }
    
    public void editPunetori(){
        nrPersonalFld.setText(String.valueOf(p.getNrPersonal()));
        idFld.setText(String.valueOf(p.getId()));
        emriFld.setText(p.getEmri());
        emriBabesFld.setText(p.getEmriBabes());
        mbiemriFld.setText(p.getMbiemri());
        adresaFld.setText(p.getAdresaId().getRruga());
        emailFld.setText(p.getEmail());
        nrTelFld.setText(p.getNrTel());
        shtetiFld.setText(p.getAdresaId().getQytetiId().getShtetiId().getEmri());
        qytetiComboBox.setValue(p.getAdresaId().getQytetiId().getEmri());
        depComboBox.setValue(p.getDepartamentiId().getEmri());
        if (p.getGjinia().startsWith("M")){
            gjiniaToggleGroup.selectToggle(gjiniaToggleGroup.getToggles().get(0));
        }
        else {
            gjiniaToggleGroup.selectToggle(gjiniaToggleGroup.getToggles().get(1));
        }
        LocalDate datelindjaDate = new java.sql.Date(p.getDatelindja().getTime()).toLocalDate();
        datelindjaFld.setValue(datelindjaDate);
        
        LocalDate datafillimitDate = new java.sql.Date(p.getDataFillimit().getTime()).toLocalDate();
        datafillimitFld.setValue(datafillimitDate);
        
        if (p.getDataMbarimit() == null){
            datambarimitFld.setDisable(true);
        }
        else {
            LocalDate datambarimit = new java.sql.Date(p.getDataMbarimit().getTime()).toLocalDate();
            datambarimitFld.setDisable(false);
            datambarimitFld.setValue(datambarimit);
        }
    }
    
    public Punetori updatePunetori() {
        p.setNrPersonal(Long.parseLong(nrPersonalFld.getText()));
        p.setEmri(emriFld.getText());
        p.setEmriBabes(emriBabesFld.getText());
        p.setMbiemri(mbiemriFld.getText());
        p.setNrTel(nrTelFld.getText());
        p.setGjinia(String.valueOf(((JFXRadioButton)gjiniaToggleGroup.getSelectedToggle()).getText().charAt(0)));
        p.setEmail(emailFld.getText());
        p.setDatelindja(java.sql.Date.valueOf(datelindjaFld.getValue()));
        p.setDataFillimit(java.sql.Date.valueOf(datafillimitFld.getValue()));
        if (!datambarimitFld.isDisable()){
            p.setDataMbarimit(java.sql.Date.valueOf(datambarimitFld.getValue()));        
        }
        p.setDepartamentiId(getDepartamentiId(depComboBox.getValue()));
        
        Qyteti q = getQytetiId(qytetiComboBox.getValue());
        Adresa adresa = new Adresa();
        adresa.setRruga(adresaFld.getText());
        adresa.setQytetiId(q);
        p.setAdresaId(adresa);
        
        return p;
    }
    
    private Departamenti getDepartamentiId(String depName){
        for (Departamenti d : departamentet){
            if (d.getEmri().equalsIgnoreCase(depName)){
                return d;
            }
        }
        return null;
    }
    
    private Qyteti getQytetiId(String qyteti){
        for (Qyteti q : qytetet){
            if (q.getEmri().equalsIgnoreCase(qyteti)){
                return q;
            }
        }
        return null;
    }

    public JFXTextField getNrPersonalFld() {
        return nrPersonalFld;
    }

    public JFXTextField getIdFld() {
        return idFld;
    }

    public JFXTextField getEmriFld() {
        return emriFld;
    }

    public JFXTextField getEmriBabesFld() {
        return emriBabesFld;
    }

    public JFXTextField getMbiemriFld() {
        return mbiemriFld;
    }

    public JFXTextField getAdresaFld() {
        return adresaFld;
    }

    public JFXTextField getEmailFld() {
        return emailFld;
    }

    public JFXComboBox<?> getQytetiComboBox() {
        return qytetiComboBox;
    }

    public JFXComboBox<?> getDepComboBox() {
        return depComboBox;
    }

    public JFXTextField getShtetiFld() {
        return shtetiFld;
    }

    public JFXDatePicker getDatelindjaFld() {
        return datelindjaFld;
    }

    public JFXDatePicker getDatafillimitFld() {
        return datafillimitFld;
    }

    public JFXDatePicker getDatambarimitFld() {
        return datambarimitFld;
    }


    
    
}
