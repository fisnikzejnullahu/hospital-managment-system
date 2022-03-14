/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import alerts.CustomAlert;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import exceptions.SpitaliDbException;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.Departamenti;
import model.Semundja;
import model.dao.SemundjaDAO;
import model.dao.implementations.SemundjaDAOImpl;
import model.tablemodels.PacientiSemundjaTableModel;
import views.editdialogs.SemundjaEditController;

/**
 * FXML Controller class
 *
 * @author Fisnik Zejnullahu
 */
public class SemundjetTabelaController implements Initializable {

    @FXML
    private JFXDialogLayout mainPane;
    @FXML
    private StackPane mainStackPane;
    @FXML
    private TableView<PacientiSemundjaTableModel> tabelaPS;

    private static List<Semundja> semundjetList;
    private static ObservableList<PacientiSemundjaTableModel> obsSemundjetList;
    private FilteredList<PacientiSemundjaTableModel> semundjetFilteredList;
    private Predicate<PacientiSemundjaTableModel> seeAllSemundjetPredict;
    @FXML
    private JFXTextField searchFld;
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        seeAllSemundjetPredict = new Predicate<PacientiSemundjaTableModel>() {
            @Override
            public boolean test(PacientiSemundjaTableModel t) {
                return true;
            }
        };
        buildTable();
        buildSearchProperty();
    }    
    
    private void buildTable() {   
        SemundjaDAO semundjaDAO = new SemundjaDAOImpl();
        try {
            semundjetList = semundjaDAO.getSemundjetList();
        } catch (SpitaliDbException ex) {
            CustomAlert.showAlertError(ex, mainPane.getScene());
            return;
        }
        obsSemundjetList = FXCollections.observableArrayList();
        for (Semundja s : semundjetList) {
            String niveliRrezikut = getStringNiveliRrezikut(s.getNiveliRrezikut());
            
            obsSemundjetList.add(
                new PacientiSemundjaTableModel(s.getEmri(), niveliRrezikut, s.getId())
            );
        }
        semundjetFilteredList = new FilteredList<>(obsSemundjetList, seeAllSemundjetPredict);
        
        TableColumn<PacientiSemundjaTableModel, String> semundjaCol = new TableColumn<>("SEMUNDJA");
        semundjaCol.setResizable(false);
        semundjaCol.setPrefWidth(665);
        semundjaCol.setCellValueFactory(new PropertyValueFactory<>("emri"));
        
        TableColumn<PacientiSemundjaTableModel, String> niveliRrCol = new TableColumn<>("NIVELI I RREZIKUT");
        niveliRrCol.setResizable(false);
        niveliRrCol.setPrefWidth(665);
        
        niveliRrCol.setCellValueFactory(new PropertyValueFactory<>("niveliRrezikut"));
        
        tabelaPS.getColumns().addAll(semundjaCol, niveliRrCol);
        tabelaPS.setItems(semundjetFilteredList);
        tabelaPS.getSelectionModel().clearSelection();
//        tabelaPS.getSelectionModel().selectFirst();
    }
    
    private static String getStringNiveliRrezikut(int nr){
        switch (nr){
            case 1:
                return "I ulte";
            case 2:
                return "I mesem";
            case 3:
                return "I larte";
        }
        return "";
    }
    
    private void buildSearchProperty(){
        searchFld.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.trim().isEmpty())
                {
                    semundjetFilteredList.setPredicate(seeAllSemundjetPredict);
                    if (!tabelaPS.getItems().isEmpty()){
                        tabelaPS.getSelectionModel().selectFirst();        
                    }
                }
                else 
                {
                    searchSemundje();
                }
            }
        });
    }

    @FXML
    private void searchSemundje() {
        if (!searchFld.getText().isEmpty()){
            String searched = searchFld.getText().toLowerCase();
            Predicate<PacientiSemundjaTableModel> predict = new Predicate<PacientiSemundjaTableModel>() {
                @Override
                public boolean test(PacientiSemundjaTableModel t) {
                    return t.getEmri().toLowerCase().startsWith(searched);
                }
            };

            semundjetFilteredList.setPredicate(predict);
            if (!tabelaPS.getItems().isEmpty()){
                tabelaPS.getSelectionModel().selectFirst();
            }
        }
    }
    
    @FXML
    private void addSemundja() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        
        fxmlLoader.setLocation(getClass().getResource("/views/editdialogs/semundjaedit.fxml"));
        JFXDialogLayout dialogLayout = null;
        try {
            dialogLayout = fxmlLoader.load();
        } catch (IOException ex) {
            ex.printStackTrace();
            return;
        }
        JFXDialog dialog = new JFXDialog(mainStackPane, dialogLayout, JFXDialog.DialogTransition.CENTER);
        SemundjaEditController controller = fxmlLoader.getController();
        JFXButton ruajeBtn = controller.getRuajeBtn();
        
        controller.getFshijeBtn().setVisible(false);
        
        
        JFXTextField emriFld = controller.getEmriFld();
        Label emriErrorLbl = controller.getEmriErrorLbl();
        
        controller.getDatakrijimitFld().setVisible(false);
        ruajeBtn.setPrefWidth(354);
        ruajeBtn.setPrefHeight(60);
        ruajeBtn.setLayoutX(51);
        ruajeBtn.setLayoutY(ruajeBtn.getLayoutY()-60);
        
        controller.getSemIdFld().setVisible(false);
        controller.getRegjLbl().setVisible(true);
        
        ruajeBtn.setOnAction(e -> {
            emriErrorLbl.setVisible(false);

            if (!validateFields(null, emriFld, null, emriErrorLbl, null)){
                return;
            }

            controller.createSemundja();
            dialog.close();
            CustomAlert.showFinishedDialog(mainStackPane, "Semunja \""+emriFld.getText()+"\", ", "u regjistrua me sukses!");
        });
        dialog.show();
    }
    
    @FXML
    private void editSemundja() {        
        PacientiSemundjaTableModel selectedSemundja = tabelaPS.getSelectionModel().getSelectedItem();
        if (selectedSemundja == null){
            CustomAlert.showSimpleInformationAlert(
                mainPane.getScene(), "Nuk ka semundje te selektuar!", "Ju lutem zgjidhni nga lista!"
            );
            return;
        }
        
        FXMLLoader fxmlLoader = new FXMLLoader();
        
        fxmlLoader.setLocation(getClass().getResource("/views/editdialogs/semundjaedit.fxml"));
        JFXDialogLayout dialogLayout = null;
        try {
            dialogLayout = fxmlLoader.load();
        } catch (IOException ex) {
            ex.printStackTrace();
            return;
        }
        JFXDialog dialog = new JFXDialog(mainStackPane, dialogLayout, JFXDialog.DialogTransition.CENTER);
        SemundjaEditController controller = fxmlLoader.getController();
        JFXButton ruajeBtn = controller.getRuajeBtn();
        
        controller.getFshijeBtn().setVisible(false);
        
        JFXTextField emriFld = controller.getEmriFld();
        Label emriErrorLbl = controller.getEmriErrorLbl();
        
        controller.getSemIdFld().setVisible(true);
        controller.getRegjLbl().setVisible(false);
        int intNiveliRr = getIntNiveliRr(selectedSemundja.getNiveliRrezikut());
        
        controller.editSemundja(selectedSemundja, null, intNiveliRr);
        ruajeBtn.setOnAction(e -> {
            emriErrorLbl.setVisible(false);

            if (!validateFields(selectedSemundja.getEmri(), emriFld, null, emriErrorLbl, null)){
                return;
            }

            Semundja s = controller.updateSemundja();
            if (s != null){
                editSemundjaInTable(s);
            }
            dialog.close();
        });
        dialog.show();
        
    }
    
    private int getIntNiveliRr(String niveliRr){
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

    @FXML
    private void deleteSemundja() {
        PacientiSemundjaTableModel selectedSemundja = tabelaPS.getSelectionModel().getSelectedItem();
        if (selectedSemundja == null){
            CustomAlert.showSimpleInformationAlert(
                mainPane.getScene(), "Nuk ka semundje te selektuar!", "Ju lutem zgjidhni nga lista!"
            );
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Sistemi per kujdesin mjekësor - 2018");
        alert.setHeaderText("Fshij Semundje");
        alert.setContentText("A jeni te sigurt se doni te fshini semundjen: "+selectedSemundja.getEmri());
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("file:src/images/logo.png"));
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK){
            
            SemundjaDAO semundjaDAO = new SemundjaDAOImpl();
            try {
                semundjaDAO.delete(selectedSemundja.getIdSemundjes());
                CustomAlert.showFinishedDialog(mainStackPane, "Semundja \""+selectedSemundja.getEmri()+"\", ", "u fshi me sukses!");
                obsSemundjetList.remove(selectedSemundja);
            } catch (SpitaliDbException ex) {
                CustomAlert.showAlertError(ex, mainPane.getScene());
            }
        }
    }
    
    public static void addSemundjaToTable(Semundja s){
        semundjetList.add(s);
        obsSemundjetList.add(
            new PacientiSemundjaTableModel(s.getEmri(), getStringNiveliRrezikut(s.getNiveliRrezikut()), s.getId())
        );
    }

    public static void editSemundjaInTable(Semundja s){
        for (Semundja sd : semundjetList){
            if (sd.getId().equals(s.getId())){
                sd = s;
            }
        }
        for (PacientiSemundjaTableModel ps : obsSemundjetList){
            if (s.getId().equals(ps.getIdSemundjes())){
                ps.setEmri(s.getEmri());
                ps.setNiveliRrezikut(getStringNiveliRrezikut(s.getNiveliRrezikut()));
            }
        }
    }
    
    private static boolean validateSemundja(String text){
        for (PacientiSemundjaTableModel p : obsSemundjetList){
            if (p.getEmri().equalsIgnoreCase(text)){
                return false;
            }
        }
        return true;
    }
    
    public static boolean validateFields(String originalEmri, JFXTextField emriFld, JFXDatePicker dataKrijimitFld, Label emriErrorLbl, Label dataErrorLbl) {
        int countErrors = 0;
        if (emriFld.getText().trim().isEmpty()){
            emriErrorLbl.setText("Nuk keni shkruar asnje emer!");
            emriErrorLbl.setVisible(true);
            countErrors++;
        }

        if (dataKrijimitFld!=null){
            if (dataKrijimitFld.getValue() == null){
                dataErrorLbl.setVisible(true);
                countErrors++;
            }
        }
        if (!validateSemundja(emriFld.getText().trim())){
            if (originalEmri == null){
                emriErrorLbl.setText("Semundja ekziston!");
                emriErrorLbl.setVisible(true);
                countErrors++;
            }
            else {
                if (!emriFld.getText().trim().equalsIgnoreCase(originalEmri)){
                    emriErrorLbl.setText("Semundja ekziston!");
                    emriErrorLbl.setVisible(true);
                    countErrors++;
                }
            }
        }
        return countErrors == 0;
    }

}
