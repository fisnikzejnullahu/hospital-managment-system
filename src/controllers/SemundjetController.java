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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.persistence.NoResultException;
import model.Pacienti;
import model.PacientiSemundja;
import model.PacientiSemundjaPK;
import model.Semundja;
import model.dao.PacientiDAO;
import model.dao.SemundjaDAO;
import model.dao.implementations.PacientiDAOImpl;
import model.dao.implementations.SemundjaDAOImpl;
import model.database.config.HibernateUtil;
import model.tablemodels.PacientiSemundjaTableModel;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import views.editdialogs.SemundjaEditController;

/**
 * FXML Controller class
 *
 * @author Fisnik Zejnullahu
 */
public class SemundjetController implements Initializable {

    @FXML
    private StackPane mainStackPane;
    
    private int pacientiId;
    private Pacienti pacienti;
    @FXML
    private Label emriPacientitLbl;
    @FXML
    private JFXDialogLayout mainPane;
    
    private ObservableList<PacientiSemundjaTableModel> obsPacientiSemundjaList;
    @FXML
    private TableView<PacientiSemundjaTableModel> tabelaPS;
    
    private JFXDialogLayout semundjaEditnoitDialog;
    private List<Semundja> semundjeList;
    public static BooleanProperty added = new SimpleBooleanProperty(false);

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        added.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue){
                    showShtoDialog();
                }
            }
        });
    }
    
    public void setPacienti(int pacientiId){
        this.pacientiId = pacientiId;
        loadSemundjet();
        buildListaSemundjeveEkz();
    }
    
    private void loadSemundjet(){
        try {
            PacientiDAO pd = new PacientiDAOImpl();
            Pacienti p = pd.get(pacientiId);
            this.pacienti = p;
            Session currentSession = HibernateUtil.getSessionFactory().getCurrentSession();
            currentSession.beginTransaction();
            Query<Pacienti> createQuery = currentSession.createQuery("select p from Pacienti p JOIN FETCH p.pacientiSemundjaCollection where p.id=:peID", Pacienti.class);
            createQuery.setParameter("peID", pacienti.getId());
            
            Pacienti singleResult = new Pacienti();
            try {
                singleResult = createQuery.getSingleResult();
            }
            catch (NoResultException ex){}

            currentSession.getTransaction().commit();
            
            emriPacientitLbl.setText(pacienti.getEmri() + " " +pacienti.getMbiemri());
            
            buildTable(singleResult.getPacientiSemundjaCollection());
        }catch(SpitaliDbException ex){
            CustomAlert.showAlertError(ex, mainPane.getScene());
        }
    }
    
    private void buildTable(Collection<PacientiSemundja> pacientiSemundjaCollection) {        
        obsPacientiSemundjaList = FXCollections.observableArrayList();
        
        if (pacientiSemundjaCollection != null){
            for (PacientiSemundja ps : pacientiSemundjaCollection) {
                String niveliRrezikut = "";
                switch (ps.getSemundja().getNiveliRrezikut()){
                    case 1:
                        niveliRrezikut = "I ulte";
                        break;
                    case 2:
                        niveliRrezikut = "I mesem";
                        break;
                    case 3:
                        niveliRrezikut = "I larte";
                }
                obsPacientiSemundjaList.add(
                    new PacientiSemundjaTableModel(ps.getSemundja().getEmri(), niveliRrezikut, ps.getDataSemundjes().toString(), ps.getSemundja().getId())
                );
            }
        }
        
        TableColumn<PacientiSemundjaTableModel, String> semundjaCol = new TableColumn<>("SEMUNDJA");
        semundjaCol.setResizable(false);
        semundjaCol.setPrefWidth(440);
        semundjaCol.setCellValueFactory(new PropertyValueFactory<>("emri"));
        
        TableColumn<PacientiSemundjaTableModel, String> niveliRrCol = new TableColumn<>("NIVELI I RREZIKUT");
        niveliRrCol.setResizable(false);
        niveliRrCol.setPrefWidth(440);
        
        niveliRrCol.setCellValueFactory(new PropertyValueFactory<>("niveliRrezikut"));
        
        TableColumn<PacientiSemundjaTableModel, String> dataCol = new TableColumn<>("DATA");
        dataCol.setResizable(false);
        dataCol.setPrefWidth(440);
        dataCol.setCellValueFactory(new PropertyValueFactory<>("dataSemundjes"));
        
        tabelaPS.getColumns().addAll(semundjaCol, niveliRrCol, dataCol);
        tabelaPS.setItems(obsPacientiSemundjaList);
        tabelaPS.getSelectionModel().selectFirst();
    }

    @FXML
    private void shtoSemundje() {
        added.set(false);
        showShtoDialog();
    }
    
    @FXML
    private void editSemundjen(ActionEvent event) {
        buildEditDialog("edit");
    }
    
    private void buildEditDialog(String action){
        PacientiSemundjaTableModel pm = tabelaPS.getSelectionModel().getSelectedItem();
        if (action.equals("edit")){
            if (pm == null){
                CustomAlert.showSimpleInformationAlert(
                    mainPane.getScene(), "Nuk ka semundje te selektuar!", "Ju lutem zgjidhni nga lista!"
                );
                return;
            }
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
        JFXButton fshijeBtn = controller.getFshijeBtn();
        fshijeBtn.setOnAction(e -> {
            boolean deleteSignal = showDeleteDialog(pm);
            if (deleteSignal){
                controller.deleteSemundjaFromPacienti(pm.getIdSemundjes(), pacienti.getId());
                obsPacientiSemundjaList.remove(pm);
                dialog.close();
            }
        });
        
        JFXTextField emriFld = controller.getEmriFld();
        JFXDatePicker dataKrijimitFld = controller.getDatakrijimitFld();
        Label emriErrorLbl = controller.getEmriErrorLbl();
        Label dataErrorLbl = controller.getDataErrorLbl();
        
        if (action.equals("edit")){
            controller.getSemIdFld().setVisible(true);
            controller.getRegjLbl().setVisible(false);
            int intNiveliRr = getIntNiveliRr(pm.getNiveliRrezikut());
            controller.editSemundja(pm, String.valueOf(pm.getIdSemundjes()), intNiveliRr);
            ruajeBtn.setOnAction(e -> {
                emriErrorLbl.setVisible(false);
                dataErrorLbl.setVisible(false);
                
                if (!validateFields(pm.getEmri(), emriFld, dataKrijimitFld, emriErrorLbl, dataErrorLbl)){
                    return;
                }
                
                Semundja s = controller.updateSemundja();
                if (s != null){
                    controller.updatePacientiSemundja(s, pacienti);
                    updateTableModel(pm, s);
                    updateListSemundja(s);
                    SemundjetTabelaController.editSemundjaInTable(s);
                    pm.setDataSemundjes(dataKrijimitFld.getValue().toString());
                }
                dialog.close();
            });
        }
        else {
            ruajeBtn.setPrefWidth(354);
            ruajeBtn.setPrefHeight(60);
            ruajeBtn.setLayoutX(51);
            ruajeBtn.setLayoutY(401);
            fshijeBtn.setVisible(false);
            controller.getSemIdFld().setVisible(false);
            controller.getRegjLbl().setVisible(true);
            ruajeBtn.setOnAction(e -> {
                emriErrorLbl.setVisible(false);
                dataErrorLbl.setVisible(false);
                if (!validateFields(null, emriFld, dataKrijimitFld, emriErrorLbl, dataErrorLbl)){
                    return;
                }
                controller.createSemundja();
                dialog.close();
            });
        }
        dialog.show();
    }
    
    private boolean validateSemundja(String text){
        for (PacientiSemundjaTableModel p : obsPacientiSemundjaList){
            if (p.getEmri().equalsIgnoreCase(text)){
                return false;
            }
        }
        return true;
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
    
    private void buildListaSemundjeveEkz(){
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        
        NativeQuery nativeQuery = session.createNativeQuery("select s.* from Semundja s "
                + "EXCEPT "
                + "select s.* from Semundja s "
                + "inner join Pacienti_Semundja ps on ps.semundjaId = s.id "
                + "inner join Pacienti p on p.id = ps.pacientiId "
                + "where p.id="+pacientiId);
        List<Object[]> rows = nativeQuery.list();
        
        semundjeList = new ArrayList<>();
        
        for(Object[] row : rows){
            Semundja s = new Semundja();
            s.setId(Integer.parseInt(row[0].toString()));
            s.setEmri(row[1].toString());
            s.setNiveliRrezikut(Integer.parseInt(row[2].toString()));
            semundjeList.add(s);
        }
        
        session.getTransaction().commit();
    }
    
    /**
     * @param added - nese nuk ka zgjedh semundje ekzistuese por ka shtu semundje te re atehere nihere u qel dialogu
     * me regjistru semundje, proces ky qi shkon te controlleri semundjaeditcontroller,
     * kur u regjistru nDB atehere ai controller e thirr qit me metod me (true) 
     * edhe kjo kallxon qi me qel dialogn me shtu semundje edhe zgjedhe qat semundje t're
     */
    private void showShtoDialog(){
        buildListaSemundjeveEkz();
        ObservableList<String> list = FXCollections.observableArrayList();
        for (Semundja s : semundjeList){
            list.add(s.getEmri());
        }
        
        ListView<String> semundjaListView = new ListView<>();
        semundjaListView.getStyleClass().add("punetoret-list");
        semundjaListView.setItems(list);
        semundjaListView.setPadding(new Insets(20, 0, 2, 0));
        if (added.get()){
            semundjaListView.getSelectionModel().selectLast();
        }
        
        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        
        Text headingText = new Text("Semundjet tjera ekzistuese:");
        
        dialogLayout.setHeading(headingText);
        if (semundjaListView.getItems().isEmpty()){
            Label text = new Label("Nuk ekziston semundje qe ky pacient nuk e ka!!!");
            text.setPadding(new Insets(45, 5, 10, 5));
            dialogLayout.setBody(text);
        }
        else {
            dialogLayout.setBody(semundjaListView);        
        }

        JFXDialog dialog = new JFXDialog(mainStackPane, dialogLayout, JFXDialog.DialogTransition.CENTER);
        
        JFXButton closeBtn = new JFXButton("Mbyll");
        closeBtn.setPrefWidth(300);
        closeBtn.setStyle("-fx-button-type: RAISED; -fx-background-color: #26C6DA; -fx-font-size: 17px;-fx-text-fill: WHITE;");
        JFXButton addBtn = new JFXButton("Shto");
        addBtn.setPrefWidth(300);
        addBtn.setStyle("-fx-button-type: RAISED; -fx-background-color: #26A69A; -fx-font-size: 17px;-fx-text-fill: WHITE;");
        
        closeBtn.setOnAction(e -> {
            dialog.close();
        });
        
        addBtn.setOnAction(e -> {
            String selected = semundjaListView.getSelectionModel().getSelectedItem();
            if (selected == null){
                CustomAlert.showSimpleInformationAlert(
                    mainPane.getScene(), "Nuk ka semundje te selektuar!", "Ju lutem zgjidhni nga lista ose krijoni semundje te re!"
                );
                return;
            }
            dialog.close();
            addSemundjen(selected);
            CustomAlert.showFinishedDialog(mainStackPane, "Semundja\""+selected+"\", ", "iu shtua pacientit me sukses!");
        });
        
        VBox actions = new VBox(5);
        HBox hbox = new HBox(5);
        hbox.getChildren().add(closeBtn);
        hbox.getChildren().add(addBtn);
        
        Label shtoLabel = new Label("Ose shto semundje te re...");
        shtoLabel.setOnMouseClicked(e-> {
            dialog.close();
            buildEditDialog("add");
        });
        shtoLabel.setCursor(Cursor.HAND);
        shtoLabel.setPadding(new Insets(15, 0, 15, 0));
        shtoLabel.setFont(Font.font(17));
        shtoLabel.setTextFill(Color.valueOf("#6100ff"));
        
        actions.getChildren().add(shtoLabel);
        actions.getChildren().add(hbox);
        dialogLayout.setActions(actions);
        dialogLayout.setPrefWidth(600);
        dialog.show();
    }

    private void addSemundjen(String emri) {
        for (Semundja s : semundjeList){
            if (s.getEmri().equals(emri)){
                PacientiSemundjaPK pk = new PacientiSemundjaPK();
                SemundjaDAO semundjaDAO = new SemundjaDAOImpl();
                try {
                    Semundja semundja = s;
                    pk.setPacientiId(pacienti.getId());
                    pk.setSemundjaId(semundja.getId());

                    PacientiSemundja ps = new PacientiSemundja();
                    ps.setDataSemundjes(new Date());
                    ps.setPacienti(pacienti);
                    ps.setSemundja(semundja);
                    ps.setPacientiSemundjaPK(pk);

                    Session session = HibernateUtil.getSessionFactory().openSession();
                    session.beginTransaction();
                    session.save(ps);
                    session.getTransaction().commit();
                    addToTableModel(ps);
                } catch (Exception ex) {
                    CustomAlert.showAlertError(ex, mainPane.getScene());
                }
            }
        }
    }
    
    private void addToTableModel(PacientiSemundja ps){
        String niveliRrezikut = getStringNiveliRrezikut(ps.getSemundja().getNiveliRrezikut());
        SimpleDateFormat dt = new SimpleDateFormat("YYYY-MM-dd"); 
        obsPacientiSemundjaList.add(
            new PacientiSemundjaTableModel(ps.getSemundja().getEmri(), niveliRrezikut, dt.format(ps.getDataSemundjes()), ps.getSemundja().getId())
        );
    }
    
    private void updateTableModel(PacientiSemundjaTableModel pm, Semundja s){
        pm.setEmri(s.getEmri());
        String niveliRrezikut = getStringNiveliRrezikut(s.getNiveliRrezikut());
        pm.setNiveliRrezikut(niveliRrezikut);
    }
    
    public static String getStringNiveliRrezikut(int nr){
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

    private boolean showDeleteDialog(PacientiSemundjaTableModel ps) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Sistemi per kujdesin mjekësor - 2018");
        alert.setHeaderText("Fshij Semundje");
        alert.setContentText("A jeni te sigurt se doni ti fshini pacientit\n \"" + emriPacientitLbl.getText() 
                + "\",\nsemundjen: "+ps.getEmri());
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("file:src/images/logo.png"));
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK){
            return true;
        }
        return false;
    }

    /**
     * 
     * @param originalEmri - osht emri qi e ka punetori qi osht tu u editu ndialog!
     */
    private boolean validateFields(String originalEmri, JFXTextField emriFld, JFXDatePicker dataKrijimitFld, Label emriErrorLbl, Label dataErrorLbl) {
        return SemundjetTabelaController.validateFields(originalEmri, emriFld, dataKrijimitFld, emriErrorLbl, dataErrorLbl);
//        int countErrors = 0;
//        if (emriFld.getText().trim().isEmpty()){
//            emriErrorLbl.setText("Nuk keni shkruar asnje emer!");
//            emriErrorLbl.setVisible(true);
//            countErrors++;
//        }
//
//        if (dataKrijimitFld.getValue() == null){
//            dataErrorLbl.setVisible(true);
//            countErrors++;
//        }
//        if (!validateSemundja(emriFld.getText().trim())){
//            if (originalEmri == null){
//                emriErrorLbl.setText("Departamenti ekziston!");
//                emriErrorLbl.setVisible(true);
//                countErrors++;
//            }
//            else {
//                if (!emriFld.getText().trim().equalsIgnoreCase(originalEmri)){
//                    emriErrorLbl.setText("Departamenti ekziston!");
//                    emriErrorLbl.setVisible(true);
//                    countErrors++;
//                }
//            }
//        }
//        return countErrors == 0;
    }

    private void updateListSemundja(Semundja s) {
        for (Semundja sm : semundjeList){
                sm = s;
//            if (sm.getId().equals(s.getId())){
//                sm.setEmri(s.getEmri());
//                sm.setNiveliRrezikut(s.getNiveliRrezikut());
//            }
        }
    }

}
