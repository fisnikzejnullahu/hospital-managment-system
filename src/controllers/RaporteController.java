/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import alerts.CompletedReportDialog;
import alerts.CustomAlert;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import exceptions.SpitaliDbException;
import java.io.IOException;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Pacienti;
import model.Perdoruesi;
import model.Raporti;
import model.dao.PacientiDAO;
import model.dao.RaportiDAO;
import model.dao.implementations.PacientiDAOImpl;
import model.dao.implementations.RaportiDAOImpl;
import model.dao.implementations.StoredProcedureExecuter;
import model.tablemodels.RaportiTableModel;
import net.sf.jasperreports.engine.JRException;
import reports.ReportGenerator;
import reports.modeljrxml.Item;
import views.editdialogs.RaportiEditController;

/**
 * FXML Controller class
 *
 * @author Fisnik Zejnullahu
 */
public class RaporteController implements Initializable {
    
    @FXML
    private StackPane mainStackPane;
    @FXML
    private JFXDatePicker startDateFld;
    @FXML
    private JFXDatePicker endDateFld;
    @FXML
    private Button gjeneroBtn;
    @FXML
    private JFXTextField idPersonFld;
    @FXML
    private TextField emriPersonFld;
    @FXML
    private JFXComboBox<String> llojiRaportitComboBox;
    private static Perdoruesi loggedInPerdoruesi;
    
    private List<Pacienti> pacientetList;

    private ReportGenerator generator;
    
    @FXML
    private TableView<RaportiTableModel> tabelaRaporteve;
    
    private static List<Raporti> raportetList;
    private ObservableList<RaportiTableModel> obsRaportetList;
//    private FilteredList<RaportiTableModel> raportetFilteredList;
//    private Predicate<RaportiTableModel> seeAllRaportetPredict;
    
    private ArrayList<Label> errorLabels;
    @FXML
    private AnchorPane mainAnchorPane;
    @FXML
    private Label errorIdLbl;
    @FXML
    private JFXCheckBox selectAllCheckBox;
    @FXML
    private AnchorPane loadingReportPane;
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        buildTable();
        buildLlojetComboBox();
        buildListaPacienteve();
        errorLabels = new ArrayList<>();
    }
    
    private void buildTable() {
        RaportiDAO raportiDAO = new RaportiDAOImpl();
        try {
            raportetList = raportiDAO.getRaportetList();
        } catch (SpitaliDbException ex) {
            CustomAlert.showAlertError(ex, mainStackPane.getScene());
            return;
        }
        obsRaportetList = FXCollections.observableArrayList();
        for (Raporti r : raportetList) {
            obsRaportetList.add(
                new RaportiTableModel(
                        r.getId(), r.getDataLeshimit().toString(), r.getLlojiRaportit(), r.getPunetoriId().getEmri()+ " "+r.getPunetoriId().getMbiemri()
                )
            );
        }
//        raportetFilteredList = new FilteredList<>(obsRaportetList, seeAllRaportetPredict);
        
        TableColumn<RaportiTableModel, String> idCol = new TableColumn<>("ID E RAPORTIT");
        idCol.setResizable(false);
        idCol.setPrefWidth(327);
        idCol.setCellValueFactory(new PropertyValueFactory<>("raportiId"));
        
        TableColumn<RaportiTableModel, String> dataLeshimitCol = new TableColumn<>("DATA E LESHIMIT");
        dataLeshimitCol.setResizable(false);
        dataLeshimitCol.setPrefWidth(327);
        dataLeshimitCol.setCellValueFactory(new PropertyValueFactory<>("dataLeshimit"));
        
        TableColumn<RaportiTableModel, String> llojiRaportitCol = new TableColumn<>("LLOJI RAPORTIT");
        llojiRaportitCol.setResizable(false);
        llojiRaportitCol.setPrefWidth(327);
        llojiRaportitCol.setCellValueFactory(new PropertyValueFactory<>("llojiRaportit"));
        
        TableColumn<RaportiTableModel, String> punetoriCol = new TableColumn<>("LESHUAR NGA");
        punetoriCol.setResizable(false);
        punetoriCol.setPrefWidth(327);
        punetoriCol.setCellValueFactory(new PropertyValueFactory<>("punetori"));
        
        tabelaRaporteve.getColumns().addAll(idCol, dataLeshimitCol, llojiRaportitCol, punetoriCol);
        tabelaRaporteve.setItems(obsRaportetList);
    }
    
    private void buildLlojetComboBox() {
        String s = "Raporti i semundjeve te pacientit";
        ObservableList<String> llojet = FXCollections.observableArrayList();
        llojet.add(s);
        llojiRaportitComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                mainAnchorPane.getChildren().removeAll(errorLabels);
                errorLabels.clear();
                if (newValue.equals("Raporti i semundjeve te pacientit")){
                    selectAllCheckBox.setVisible(true);
                    startDateFld.setVisible(true);
                    endDateFld.setVisible(true);
                }
            }
        });
        llojiRaportitComboBox.setItems(llojet);
    }
    
    @FXML
    public void buildSemundjetReport(){
        mainAnchorPane.getChildren().removeAll(errorLabels);
        errorLabels.clear();
        if (!validatePacienti()){
            return;
        }
        if (!validateFields()){
            mainAnchorPane.getChildren().addAll(errorLabels);
            return;
        }
        StoredProcedureExecuter procedureExecuter = new StoredProcedureExecuter();
        
        List<Item> items = new ArrayList<>();
//        List<String[]> lista = new ArrayList<>();
        try {
            CallableStatement semundjetSt;
            int pacientiId = Integer.parseInt(idPersonFld.getText());
            if (selectAllCheckBox.isSelected()){
                semundjetSt = procedureExecuter.executeGetSemundjet(null, null, pacientiId);
            }
            else {
                semundjetSt = procedureExecuter.executeGetSemundjet(startDateFld.getValue(), endDateFld.getValue(), pacientiId);
            }
            ResultSet rs = null;
            try {
                rs = semundjetSt.executeQuery();
                while (rs.next()){
                    String dataSemundjes = rs.getString(1);
                    String emriSemundjes = rs.getString(2);
                    String niveliRrezikut = SemundjetController.getStringNiveliRrezikut(Integer.parseInt(rs.getString(3)));
                    items.add(new Item(dataSemundjes, emriSemundjes, niveliRrezikut));
//                    String[] row = {dataSemundjes, emriSemundjes, niveliRrezikut};
//                    lista.add(row);
                }
            }
            catch (Exception ex ){
                CustomAlert.showAlertError(ex, mainStackPane.getScene());
            }
            finally {
                if (semundjetSt != null){
                    semundjetSt.close();
                }
                if (rs != null){
                    rs.close();
                }
            }
        } catch (SpitaliDbException | SQLException ex) {
            ex.printStackTrace();
            CustomAlert.showAlertError(ex, mainStackPane.getScene());
        }
        
        Raporti raporti = new Raporti();
        raporti.setDataLeshimit(new Date());
        raporti.setPunetoriId(loggedInPerdoruesi.getPunetoriId());
        raporti.setLlojiRaportit(llojiRaportitComboBox.getValue());
        
        
        try {
            RaportiDAO raportiDAO = new RaportiDAOImpl();
            raportiDAO.create(raporti);
            addRaportiTableModel(raporti);
        } catch (SpitaliDbException ex) {
            CustomAlert.showAlertError(ex, mainStackPane.getScene());
        }
        
//        buildReportFrame(lista);
        buildReportFrame(items);
        
    }
    
    private void buildListaPacienteve(){
        PacientiDAO pacientiDAO = new PacientiDAOImpl();
        try {
            this.pacientetList = pacientiDAO.getPacientetList();
        } catch (SpitaliDbException ex) {
            CustomAlert.showAlertError(ex, mainStackPane.getScene());
        }
    }
    
    private void addRaportiTableModel(Raporti r){
        SimpleDateFormat dt = new SimpleDateFormat("YYYY-MM-dd"); 
        RaportiTableModel rp = new RaportiTableModel(
            r.getId(), dt.format(r.getDataLeshimit()), r.getLlojiRaportit(), r.getPunetoriId().getEmri()+ " "+r.getPunetoriId().getMbiemri()
        );
        obsRaportetList.add(rp);
    }
    
    private void updateRaportiTableModel(RaportiTableModel rp, Raporti r){
        rp.setDataLeshimit(r.getDataLeshimit().toString());
        rp.setLlojiRaportit(r.getLlojiRaportit());
        rp.setPunetori(r.getPunetoriId().getEmri()+ " "+r.getPunetoriId().getMbiemri());
    }

    private void buildReportFrame(List<Item> lista) {
        if (lista.isEmpty()){
            CustomAlert.showSimpleInformationAlert(mainStackPane.getScene(), "NUK U GJETEN SEMUNDJE", "Ne baze te filtrimit nuk u gjet asnje semundje per kete pacient!");
            return;
        }
        showLoadingImg();

        class ReportBuilderThread extends Thread{
            @Override
            public void run() {
                generator = new ReportGenerator();
                try {
                    generator.exportSemundjetPacientitReport(lista, emriPersonFld.getText());
                } catch (JRException | SpitaliDbException | SQLException ex) {
                    Platform.runLater(new Runnable(){
                        @Override
                        public void run(){
                            hideLoadingImg();
                            CustomAlert.showAlertError(ex, mainStackPane.getScene());
                        }
                    });
                    return;
                }
                hideLoadingImg();
                Platform.runLater(new Runnable(){
                    @Override
                    public void run(){
                        hideLoadingImg();
                        showConfirmReportDialog(generator, "raport-semundjeve.pdf");
                    }
                });
            }
        }
        new ReportBuilderThread().start();
    }
    
    
    private void showConfirmReportDialog(ReportGenerator generator, String fileName) {
        CompletedReportDialog.showDialog(mainStackPane, generator, fileName);
    }
    
    private void showLoadingImg(){
        loadingReportPane.setVisible(true);
        mainAnchorPane.setOpacity(0.3);
    }
    private void hideLoadingImg(){
        loadingReportPane.setVisible(false);
        mainAnchorPane.setOpacity(1);
    }
    
    public void setPerdoruesi(Perdoruesi loggedInPerdoruesi) {
        RaporteController.loggedInPerdoruesi = loggedInPerdoruesi;
    }

    @FXML
    private void editRaporti() {
        RaportiTableModel selected = tabelaRaporteve.getSelectionModel().getSelectedItem();
        if (selected == null){
            CustomAlert.showSimpleInformationAlert(
                mainStackPane.getScene(), "Nuk ka raport te selektuar!", "Ju lutem zgjidhni nga lista!"
            );
            return;
        }
        
        FXMLLoader fxmlLoader = new FXMLLoader();
        
        fxmlLoader.setLocation(getClass().getResource("/views/editdialogs/raportiedit.fxml"));
        JFXDialogLayout dialogLayout = null;
        try {
            dialogLayout = fxmlLoader.load();
        } catch (IOException ex) {
            ex.printStackTrace();
            return;
        }
        JFXDialog dialog = new JFXDialog(mainStackPane, dialogLayout, JFXDialog.DialogTransition.CENTER);
        RaportiEditController controller = fxmlLoader.getController();
        JFXButton ruajeBtn = controller.getRuajeBtn();
        
        controller.setRaporti(selected);
        controller.setLlojiRaportitComboBox(llojiRaportitComboBox);
        controller.editRaporti();
        ruajeBtn.setOnAction(e -> {
            Raporti r = controller.updateRaporti();
            if (r == null){
                return;
            }
            updateRaportiTableModel(selected, r);
            dialog.close();
        });
        dialog.show();
    }

    @FXML
    private void deleteRaporti() {
        RaportiTableModel selected = tabelaRaporteve.getSelectionModel().getSelectedItem();
        if (selected == null){
            CustomAlert.showSimpleInformationAlert(
                mainStackPane.getScene(), "Nuk ka raport te selektuar!", "Ju lutem zgjidhni nga lista!"
            );
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Sistemi per kujdesin mjekësor - 2018");
        alert.setHeaderText("Fshij Raport");
        alert.setContentText("A jeni te sigurt se doni te fshini raportin (id="+selected.getRaportiId()+")"+ ", lloji="+selected.getLlojiRaportit());
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("file:src/images/logo.png"));
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK){
            
            RaportiDAO raportiDAO = new RaportiDAOImpl();
            try {
                raportiDAO.delete(selected.getRaportiId());
                CustomAlert.showFinishedDialog(mainStackPane, "Raporti (id="+selected.getRaportiId()+")"+ ", lloji=\""+selected.getLlojiRaportit()+"\", ", "u fshi me sukses!");
                obsRaportetList.remove(selected);
            } catch (SpitaliDbException ex) {
                CustomAlert.showAlertError(ex, mainStackPane.getScene());
            }
        }
    }
    
    private boolean validatePacienti(){
        try{
            Integer.parseInt(idPersonFld.getText());
        }catch(NumberFormatException e){
            errorIdLbl.setText("Vetem numra lejohen!");
            errorIdLbl.setVisible(true);
            return false;
        }
        for (Pacienti p : pacientetList){
            int value = Integer.parseInt(idPersonFld.getText());
            if (p.getId() == value){
                emriPersonFld.setText(p.getEmri() + " " + p.getMbiemri());
                return true;
            }
        }
        errorIdLbl.setText("Pacienti me kete id nuk ekziston!");
        errorIdLbl.setVisible(true);
        return false;
    }
    private boolean validateFields(){
        errorIdLbl.setVisible(false);
//        mainAnchorPane.getChildren().removeAll(errorLabels);
//        errorLabels.clear();
        int countErrors = 0;
        if (idPersonFld.getText().trim().isEmpty()){
            errorIdLbl.setText("Plotesoni kete fushe!");
            errorIdLbl.setVisible(true);
            countErrors++;
        }
        
        if (llojiRaportitComboBox.getValue() == null){
            Label idErrorLbl = createErrorLabel(llojiRaportitComboBox, "Plotesoni kete fushe!");
            errorLabels.add(idErrorLbl);
            countErrors++;
        }
        else {
            if (llojiRaportitComboBox.getValue().equals("Raporti i semundjeve te pacientit")){
                if (!selectAllCheckBox.isSelected()){
                    if ((startDateFld.getValue() == null && endDateFld.getValue() == null)
                            ||(startDateFld.getValue() == null && endDateFld.getValue() != null)
                            || (startDateFld.getValue() != null && endDateFld.getValue() == null)){
                        Label idErrorLbl = createErrorLabel(startDateFld, "Ju lutem plotesoni te dyja datat ose selekto te gjitha!");
                        errorLabels.add(idErrorLbl);
                        countErrors++;
                    }
                }
            }
        }
        return countErrors == 0;
    }
    
    private Label createErrorLabel(Node n, String errorText){
        Label label = new Label(errorText);
        label.setTextFill(Color.RED);
        label.setLayoutX(n.getLayoutX());
        label.setLayoutY(n.getLayoutY()+49);
        return label;
    }
    
}
