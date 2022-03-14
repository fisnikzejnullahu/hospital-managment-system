/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views.editdialogs;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleGroup;
import model.Adresa;
import model.GrupiGjakut;
import model.Pacienti;
import model.Profesioni;
import model.Qyteti;

/**
 * FXML Controller class
 *
 * @author Leutrim Osmani
 */
public class PacientiEditController implements Initializable {
    
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
    private JFXComboBox<String> gjakuEditComboBox;
    @FXML
    private JFXComboBox<String> profesioniEditComboBox;
    @FXML
    private JFXTextField shtetiFld;
    @FXML
    private JFXDatePicker datelindjaFld;
    @FXML
    private ToggleGroup gjiniaToggleGroup;
    @FXML
    private JFXTextField nrtelFld;
    
    private List<Qyteti> qytetet;
    private List<GrupiGjakut> grupetGjakut;
    private List<Profesioni> profesionet;
    
    private Pacienti p;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    
    public void setPacienti(Pacienti p){
        this.p = p;
    }

    public void setGrupiGjakut(List<GrupiGjakut> gupetGjakut) {
        this.grupetGjakut = gupetGjakut;
        ObservableList<String> grupetGjakutObservableList = FXCollections.observableArrayList();
        for (GrupiGjakut g : grupetGjakut){
            grupetGjakutObservableList.add(g.getTipi());
        }
        gjakuEditComboBox.setItems(grupetGjakutObservableList);
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
    
    public void buildGrupetGjakut(ObservableList<String> gjakuList){
        gjakuEditComboBox.setItems(gjakuList);
    }
    public void buildProfesionet(ObservableList<String> proList){
        profesioniEditComboBox.setItems(proList);
    }
    
    public void editPacienti(){
        nrPersonalFld.setText(String.valueOf(p.getNrPersonal()));
        idFld.setText(String.valueOf(p.getId()));
        emriFld.setText(p.getEmri());
        emriBabesFld.setText(p.getEmriBabes());
        mbiemriFld.setText(p.getMbiemri());
        adresaFld.setText(p.getAdresaId().getRruga());
        emailFld.setText(p.getEmail());
        nrtelFld.setText(p.getNrTel());
        shtetiFld.setText(p.getAdresaId().getQytetiId().getShtetiId().getEmri());
        qytetiComboBox.setValue(p.getAdresaId().getQytetiId().getEmri());
        gjakuEditComboBox.setValue(p.getGrupiGjakutId().getTipi());
        profesioniEditComboBox.setValue(p.getProfesioniId().getEmri());
        if (p.getGjinia().startsWith("M")){
            gjiniaToggleGroup.selectToggle(gjiniaToggleGroup.getToggles().get(0));
        }
        else {
            gjiniaToggleGroup.selectToggle(gjiniaToggleGroup.getToggles().get(1));
        }
        LocalDate datelindjaDate = new java.sql.Date(p.getDatelindja().getTime()).toLocalDate();
        datelindjaFld.setValue(datelindjaDate);
        
    }
    
    public Pacienti updatePacienti() {
        p.setNrPersonal(Integer.parseInt(nrPersonalFld.getText()));
        p.setEmri(emriFld.getText());
        p.setEmriBabes(emriBabesFld.getText());
        p.setMbiemri(mbiemriFld.getText());
        p.setNrTel(nrtelFld.getText());
        p.setGjinia(String.valueOf(((JFXRadioButton)gjiniaToggleGroup.getSelectedToggle()).getText().charAt(0)));
        p.setEmail(emailFld.getText());
        p.setDatelindja(java.sql.Date.valueOf(datelindjaFld.getValue()));
        
        p.setProfesioniId(getProfesioniId(profesioniEditComboBox.getValue()));
        p.setGrupiGjakutId(getGjakuId(gjakuEditComboBox.getValue()));
        
        Qyteti q = getQytetiId(qytetiComboBox.getValue());
        Adresa adresa = new Adresa();
        adresa.setRruga(adresaFld.getText());
        adresa.setQytetiId(q);
        p.setAdresaId(adresa);
        
        return p;
    }
    
    private GrupiGjakut getGjakuId(String gjakuName){
        for (GrupiGjakut g : grupetGjakut){
            if (g.getTipi().equalsIgnoreCase(gjakuName)){
                return g;
            }
        }
        return null;
    }
    private Profesioni getProfesioniId(String proName){
        for (Profesioni p : profesionet){
            if (p.getEmri().equalsIgnoreCase(proName)){
                return p;
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

    public JFXComboBox<?> getGjakuComboBox() {
        return gjakuEditComboBox;
    }
    public JFXComboBox<?> getProComboBox() {
        return profesioniEditComboBox;
    }
    public JFXTextField getShtetiFld() {
        return shtetiFld;
    }

    public JFXDatePicker getDatelindjaFld() {
        return datelindjaFld;
    }

    public void setProfesionet(List<Profesioni> profesionet) {
        this.profesionet = profesionet;
        ObservableList<String> profesionetObservableList = FXCollections.observableArrayList();
        for (Profesioni p : profesionet){
            profesionetObservableList.add(p.getEmri());
        }
        profesioniEditComboBox.setItems(profesionetObservableList);
    }
}
