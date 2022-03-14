/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import alerts.CustomAlert;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import exceptions.SpitaliDbException;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import javax.persistence.NoResultException;
import model.Adresa;
import model.Profesioni;
import model.GrupiGjakut;
import model.Pacienti;
import model.Punetori;
import model.Qyteti;
import model.dao.PacientiDAO;
import model.dao.PunetoriDAO;
import model.database.config.HibernateUtil;
import model.tablemodels.PacientiTableModel;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.NativeQuery;
import views.editdialogs.PacientiEditController;
import model.dao.implementations.PacientiDAOImpl;
import org.hibernate.query.Query;
import views.fshijdialogs.PunetoriDeleteController;

/**
 *
 * @author Leutrim Osmani
 */
public class PacientiController implements Initializable{
    private static int idCount;
    @FXML
    private TextField idFld;
    @FXML
    private TextField nrPersonalFld;
    @FXML
    private TextField emriFld;
    @FXML
    private TextField emriBabesFld;
    @FXML
    private TextField mbiemriFld;
    @FXML
    private TextField nrTelFld;
    @FXML
    private TextField adresaFld;
    @FXML
    private TextField shtetiFld;
    @FXML
    private ToggleGroup gjiniaToggleGroup;
    @FXML
    private TextField emailFld;
    @FXML
    private DatePicker datelindjaFld;
    @FXML
    private ComboBox<String> qytetiComboBox;
    @FXML
    private ComboBox<String> profesioniComboBox;
    @FXML
    private ComboBox<String> gjakuComboBox;
    
    private List<GrupiGjakut> grupetGjakut;
    private List<Profesioni> profesionet;
    private List<Qyteti> qytetet;
    
    private int nextId;
    
    private ArrayList<Node> nodes;
    @FXML
    private AnchorPane shtoPane;
    
    private List<ImageView> errorImages;
    @FXML
    private JFXComboBox<String> prefixTelComboBox;
    @FXML
    private Label nrPersonalLbl;
    @FXML
    private Label emriLbl;
    @FXML
    private Label adresaLbl;
    @FXML
    private Label telLbl;
    @FXML
    private Label emailLbl;
    @FXML
    private Label gjiniaLbl;
    @FXML
    private Label gjakuLbl;
    @FXML
    private Label datelindjaLbl;
    @FXML
    private Label profesioniLbl;
    @FXML
    private TableView<PacientiTableModel> tabelaPacienteve;
    @FXML
    private AnchorPane informationPane;
    @FXML
    private AnchorPane mainPane;

    
    private ObservableList<PacientiTableModel> obsPacientetList;
    
    @FXML
    private StackPane mainStackPane;
    @FXML
    private ImageView fotoFld;
    @FXML
    private ImageView photoImageView;
    @FXML
    private StackPane stackPane1;
    @FXML
    private JFXTextField searchFld;
    @FXML
    private AnchorPane dataPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initIdCount();
        buildQytetet();        
        buildGrupetGjakut();
        buildProfesionet();
        buildTabelaPacienteve();
        buildNodes();
        buildPrefixNumbersComboBox();
        buildKeyboardListeners();
        fotoFld.setImage(new Image("file:nuk_u_gjet.png"));
        errorImages = new ArrayList<>();        
    }
    
    private void buildNodes(){
        nodes = new ArrayList<>();
        nodes.add(idFld);
        nodes.add(nrPersonalFld);
        nodes.add(emriFld);
        nodes.add(emriBabesFld);
        nodes.add(mbiemriFld);
        nodes.add(nrTelFld);
        nodes.add(adresaFld);
        nodes.add(emailFld);
        nodes.add(shtetiFld);
        nodes.add(qytetiComboBox);
        nodes.add(gjakuComboBox);
        nodes.add(profesioniComboBox);
    }
    
    private void buildPrefixNumbersComboBox(){
        ObservableList<String> list = FXCollections.observableArrayList();
        list.add("+383");
        list.add("+377");
        list.add("+349");
        prefixTelComboBox.setItems(list);
        prefixTelComboBox.setValue("+383");
    }
    
    private void initIdCount(){
        Session session = HibernateUtil.getSessionFactory().openSession();
        NativeQuery<BigDecimal> queryIdCount = session.createNativeQuery("SELECT IDENT_CURRENT('Pacienti') + IDENT_INCR('Pacienti')");        
        BigDecimal bd = queryIdCount.getSingleResult();
        nextId = bd.intValue();
        idFld.setText(String.valueOf(nextId));
    }
    
    private void buildQytetet(){   
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        qytetet = (List<Qyteti>)session.createNamedQuery("Qyteti.findAll").getResultList();
        session.getTransaction().commit();
        
        ObservableList<String> qytetetList = FXCollections.observableArrayList();
        for (Qyteti q : qytetet){
            qytetetList.add(q.getEmri());
        }
        qytetiComboBox.setItems(qytetetList);
        qytetiComboBox.setValue("Zgjedh qytetin...");
    }
    
    private int getZip(String qyteti){
        switch(qyteti){
            case "Prishtine":
                return 10000;
            case "Gjakove":
                return 50000;
            case "Pejë":
                return 30000;
            case "Gjilan":
                return 60000;
            case "Prizren":
                return 20000;
            case "Ferizaj":
                return 70000;
        }
        return 2;
    }
    
    private void buildGrupetGjakut(){ 
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            grupetGjakut = (List<GrupiGjakut>) session.createNamedQuery("GrupiGjakut.findAll").getResultList();
            session.getTransaction().commit();
        }
        ObservableList<String> grupiGjakutList = FXCollections.observableArrayList();
        for (GrupiGjakut g : grupetGjakut){
            grupiGjakutList.add(g.getTipi());
        }
        gjakuComboBox.setItems(grupiGjakutList);
        gjakuComboBox.setValue("Zgjedh Grupin e gjakut...");
    }
    private void buildProfesionet(){ 
        try (Session session = HibernateUtil.getSessionFactory().getCurrentSession()) {
            session.beginTransaction();
            profesionet = (List<Profesioni>) session.createNamedQuery("Profesioni.findAll").getResultList();
            session.getTransaction().commit();
        }
        ObservableList<String> profesioniList = FXCollections.observableArrayList();
        for (Profesioni g : profesionet){
            profesioniList.add(g.getEmri());        
        }
        profesioniComboBox.setItems(profesioniList);
        profesioniComboBox.setValue("Zgjedh Profesionin...");
    }
    @FXML
    private void save(ActionEvent event) {        
        boolean validateEmptyFields = validateEmptyFields();
        if (!validateEmptyFields){
            return;
        }
        
        if (!checkAndValidateField()) {
            return;
        }
        int nrPersonal = Integer.parseInt(nrPersonalFld.getText());
        String emri = emriFld.getText();
        String emriBabes = emriBabesFld.getText();
        String mbiemri = mbiemriFld.getText();
        String nrTel = prefixTelComboBox.getValue()+nrTelFld.getText();
        String rruga = adresaFld.getText();
        String gjinia = String.valueOf(((JFXRadioButton)gjiniaToggleGroup.getSelectedToggle()).getText().charAt(0));
        String email = emailFld.getText();
        Date datelindja = java.sql.Date.valueOf(datelindjaFld.getValue());

        Adresa adresa = new Adresa();
        Qyteti q = getQytetiId(qytetiComboBox.getValue());
        adresa.setRruga(rruga);
        adresa.setQytetiId(q);
        
            System.out.println("qytet" +q.getId());
        
        GrupiGjakut gjaku  = getGjakuId(gjakuComboBox.getValue());
        Profesioni pro = getProfesioniId(profesioniComboBox.getValue());
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        gjaku = session.get(GrupiGjakut.class, 1);
        pro = session.get(Profesioni.class, 1);
        session.save(adresa);
        session.save(gjaku);
        session.save(pro);
        session.getTransaction().commit();
        
        Pacienti p = new Pacienti();
        p.setId(nextId);
        p.setNrPersonal(nrPersonal);
        p.setEmri(emri);
        p.setEmriBabes(emriBabes);
        p.setMbiemri(mbiemri);
        p.setNrTel(nrTel);
        p.setAdresaId(adresa);
        p.setGjinia(gjinia);
        p.setEmail(email);
        p.setDatelindja(datelindja); 
        p.setGrupiGjakutId(gjaku);
        p.setProfesioniId(pro);        
        
        PacientiDAO pacientiDAO = new PacientiDAOImpl();
        try {
            pacientiDAO.create(p);
        } catch (Exception ex) {
            if(ex instanceof ConstraintViolationException){
                System.out.println("PRIMARY KEY");
            }
            return;
        }
        
        addPacientiTableModel(p);
        clearFields();
        nextId++;
        idFld.setText(String.valueOf(nextId));        
    }
        
    private GrupiGjakut getGjakuId(String depName){
        for (GrupiGjakut g : grupetGjakut){
            if (g.getTipi().equalsIgnoreCase(depName)){
                return g;
            }
        }
        return null;
    }
    private Profesioni getProfesioniId(String depName){
        for (Profesioni g : profesionet){
            if (g.getEmri().equalsIgnoreCase(depName)){
                return g;
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
        
    @FXML
    private void clearFields() {
        for (Node n : nodes){
            ScaleTransition st = new ScaleTransition(Duration.millis(100), n);
            st.setFromY(0f);
            st.setByY(1f);
            st.playFromStart();
            if (!n.getId().equals("idFld") && !n.getId().equals("shtetiFld")){
                if (n instanceof TextField){
                    ((TextField)n).clear();
                }
            }
            
            shtoPane.getChildren().removeAll(errorImages);
            errorImages.clear();
            removeErrorClass(n);
        }
        gjakuComboBox.setValue("Zgjedh grupin e gjakut...");
        profesioniComboBox.setValue("Zgjedh profesionin...");
        qytetiComboBox.setValue("Zgjedh qytetin...");
        removeErrorClass(datelindjaFld);
        datelindjaFld.setValue(null);
    }
    
    private boolean validateEmptyFields(){
        shtoPane.getChildren().removeAll(errorImages);
        errorImages.clear();
        int countErrors = 0;
        if (datelindjaFld.getValue() == null){
            countErrors++;
            addErrorImage(datelindjaFld, "Ju lutem plotesoni kete fushe!");
        }else{
            removeErrorClass(datelindjaFld);
        }
        
        
        for (Node n : nodes){
            if (n instanceof TextField){
                if (!n.getId().equals("idFld") && !n.getId().equals("shtetiFld")){
                    TextField tf = (TextField)n;
                    if (tf.getText().isEmpty()){
                        countErrors++;
                        addErrorImage(n, "Ju lutem plotesoni kete fushe!");
                    }
                    else {
                        removeErrorClass(n);
                    }
                }
            }
            else if (n instanceof ComboBox){
                ComboBox<String> combo = (ComboBox<String>)n;
                if (combo.getValue().startsWith("Zgjedh") || combo.getValue().startsWith("Ju lutem")){
                    countErrors++;
                    addErrorImage(n, "Ju lutem zgjedhni nga lista!");
                }
                else {
                    removeErrorClass(n);
                }
            }
        }
        shtoPane.getChildren().addAll(errorImages);
        return countErrors == 0;
    }
    
    private boolean checkAndValidateField(){
        shtoPane.getChildren().removeAll(errorImages);
        errorImages.clear();
        
        boolean validateField = true;
        int countErrors = 0;
        
        for (Node n : nodes){
            if (n.getId().equals("idFld") || n.getId().equals("shtetiFld") || n instanceof ComboBox) continue;

            switch (n.getId()) {
                case "nrPersonalFld":
                    validateField = validateField(n, 10, true);
                    if (!validateField){
                        addErrorImage(n, "Lejohen vetem numra! Nr. personal duhet te kete (10) shifra");
                        countErrors++;
                    }else{
                        removeErrorClass(n);
                    }
                    break;
                case "nrTelFld":
                    validateField = validateField(n, 8, true);
                    if (!validateField){
                        addErrorImage(n, "Lejohen vetem numra! (8) Nr. tel. duhet te kete (8) shifra");
                        countErrors++;
                    }else{
                        removeErrorClass(n);
                    }
                    break;
                case "emailFld":
                    TextField emailtext = (TextField)n;
                    if (!emailtext.getText().contains("@") || !emailtext.getText().contains(".")){
                        countErrors++;
                        addErrorImage(n, "Nuk eshte email valid!");
                    }else{
                        removeErrorClass(n);
                    }
                    break;
                default:
                    validateField = validateField(n, 50, false);
                    if (!validateField){
                        addErrorImage(n, "Lejohet vetem tekst, ose ke kaluar limit e fjales(MAX = 50)");
                        countErrors++;
                    }else{
                        removeErrorClass(n);
                    }
            }
        }
        shtoPane.getChildren().addAll(errorImages);
        return countErrors == 0;
    }
    
    private boolean validateField(Node n, int maxLength, boolean onlyNumbers){
        if (n instanceof TextField){
            TextField tf = (TextField)n;
            if (onlyNumbers){
                try {
                    if (tf.getText().length() != maxLength) {
                        return false; 
                    }
                    int a = Integer.parseInt(tf.getText());
                }catch(NumberFormatException e){
                    return false;
                }
            }
            else {
                String text = tf.getText().toLowerCase();
                if (text.length() > maxLength) return false;
                
                for (int i=0; i<text.length(); i++){
                    if ((int)text.charAt(i) < 97 || (int)text.charAt(i) > 122){
                        return false;
                    }
                }
                return true;
                
                
            }
        }
        return true;
    }
    
    private void addErrorImage(Node n, String errorText){
        ImageView image = new ImageView("/images/error30.png");
        if (!n.getStyleClass().contains("textfield-error")){
            n.getStyleClass().add("textfield-error");
        }
        image.setLayoutX(n.getLayoutX()+285);
        image.setLayoutY(n.getLayoutY()+7);
        image.setCursor(Cursor.HAND);
        
        Tooltip tooltip = new Tooltip(errorText);
        image.setPickOnBounds(true);
        Tooltip.install(image, tooltip);
        
        errorImages.add(image);
    }
    private void removeErrorClass(Node n){
        n.getStyleClass().remove("textfield-error");
    }

    private void selectPacienti (PacientiTableModel p){
        nrPersonalLbl.setText("Nr. Personal: " + String.valueOf(p.getNrPersonal()));
        emriLbl.setText("Emri: " + p.getEmri());
        telLbl.setText("Nr. tel: " + p.getNrTel());
        adresaLbl.setText("Adresa: " + p.getAdresa());
        emailLbl.setText("Emaili: " + p.getEmail());
        gjiniaLbl.setText("Gjinia: " + (p.getGjinia().equals("M") ? "Mashkull" : "Femer"));
        gjakuLbl.setText("Grupi i gjakut: " + p.getGrupiGjakut());
        datelindjaLbl.setText("Datelindja: " + p.getDatelindja());
        profesioniLbl.setText("Profesioni: " + p.getProfesioni());
    }
    
    private void buildTabelaPacienteve () {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List<Pacienti> resultList = session.createNamedQuery("Pacienti.findAll").getResultList();
        session.getTransaction().commit();
        
        obsPacientetList = FXCollections.observableArrayList();
        
        for (Pacienti p : resultList) {

            obsPacientetList.add(
                    new PacientiTableModel(p.getId(), p.getNrPersonal(),p.getEmri()+" "+p.getMbiemri(), p.getEmriBabes(),
                                        p.getNrTel(), p.getAdresaId().getRruga()+", "+p.getAdresaId().getQytetiId().getEmri() +" " +p.getAdresaId().getQytetiId().getZip(),
                                        p.getGjinia(), p.getEmail(),
                                        p.getDatelindja().toString(), p.getGrupiGjakutId().getTipi(),
                                        p.getProfesioniId().getEmri())
            );
        }
        
        
        TableColumn<PacientiTableModel, String> idCol = new TableColumn<>("ID e Pacientit");
        idCol.setResizable(false);
        idCol.setPrefWidth(245);
        idCol.setCellValueFactory(new PropertyValueFactory<PacientiTableModel, String>("id"));
        
        TableColumn<PacientiTableModel, String> emriCol = new TableColumn<>("Emri dhe Mbiemri");
        emriCol.setResizable(false);
        emriCol.setPrefWidth(420);
        emriCol.setCellValueFactory(new PropertyValueFactory<PacientiTableModel, String>("emri"));
        
        TableColumn<PacientiTableModel, String> depCol = new TableColumn<>("Grupi i Gjakut");
        depCol.setResizable(false);
        depCol.setPrefWidth(377);
        depCol.setCellValueFactory(new PropertyValueFactory<PacientiTableModel, String>("grupiGjakut"));
        
        tabelaPacienteve.getColumns().addAll(idCol, emriCol, depCol);
        tabelaPacienteve.setItems(obsPacientetList);
        
        if (tabelaPacienteve.getItems().size() != 0){
            tabelaPacienteve.getSelectionModel().selectFirst();        
            selectPacienti(tabelaPacienteve.getSelectionModel().getSelectedItem());
        }
        tabelaPacienteve.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<PacientiTableModel>() {
            @Override
            public void changed(ObservableValue<? extends PacientiTableModel> observable, PacientiTableModel oldValue, PacientiTableModel newValue) {
                selectPacienti(newValue);
            }
        });
    }
    
    private void addPacientiTableModel(Pacienti p){
        PacientiTableModel pacientiTM = new PacientiTableModel();
        pacientiTM.setId(p.getId());
        pacientiTM.setNrPersonal(p.getNrPersonal());
        pacientiTM.setEmri(p.getEmri());
        pacientiTM.setEmriBabes(p.getEmriBabes());
        pacientiTM.setMbiemri(p.getMbiemri());
        pacientiTM.setNrTel(p.getNrTel());
        pacientiTM.setGjinia(p.getGjinia());
        pacientiTM.setEmail(p.getEmail());
        pacientiTM.setDatelindja(p.getDatelindja().toString());
        pacientiTM.setAdresa(p.getAdresaId().getRruga()+", "+p.getAdresaId().getQytetiId().getEmri() +" " +p.getAdresaId().getQytetiId().getZip());
        pacientiTM.setGrupiGjakut(p.getGrupiGjakutId().getTipi());
        pacientiTM.setProfesioni(p.getProfesioniId().getEmri());
        tabelaPacienteve.getItems().add(pacientiTM);
    }

    private void buildKeyboardListeners() {
        shtoPane.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER){
                    save(null);
                }
            }
        });
    }
    
    private Pacienti loadSemundjet(int pacientiId){
        Session currentSession = HibernateUtil.getSessionFactory().getCurrentSession();
        currentSession.beginTransaction();
        Query<Pacienti> createQuery = currentSession.createQuery("select p from Pacienti p JOIN FETCH p.pacientiSemundjaCollection where p.id=:peID", Pacienti.class);
        createQuery.setParameter("peID", pacientiId);
        Pacienti singleResult = new Pacienti();
        try {
            singleResult = createQuery.getSingleResult();
        }
        catch (NoResultException ex){}
        finally {
            currentSession.getTransaction().commit();
        }
        return singleResult;
    }
    
    @FXML
    private void deletePacienti(ActionEvent event){
        PacientiTableModel p = tabelaPacienteve.getSelectionModel().getSelectedItem();
        if (p == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Nuk ka paciente te zgjedhur!");
            alert.setContentText("Ju lutem zgjedhni nga lista");
            alert.showAndWait();
            return;
        }
        
        Pacienti pac = loadSemundjet(p.getId());
        if (pac.getPacientiSemundjaCollection() != null){
            showDeleteSemundjetAndPacientiDialog(p);
            return;
        }
                
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Ministria e Shendetesise");
        alert.setHeaderText("FSHIJ PACIENTIN");
        alert.setContentText("A jeni te sigurt se doni te fshini pacientin: " +p.getEmri());
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK){
            PacientiDAO pacientiDAO = new PacientiDAOImpl();
            try {
                pacientiDAO.delete(p.getId(), false);
                obsPacientetList.remove(p);
            } catch (SpitaliDbException ex) {
                CustomAlert.showAlertError(ex, mainStackPane.getScene());
            }
            
        }
            
    }
    
    private void showDeleteSemundjetAndPacientiDialog(PacientiTableModel pacienti){
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/views/fshijdialogs/fshijPacientdialog.fxml"));
        JFXDialogLayout dialogLayout = null;
        try {
            dialogLayout = fxmlLoader.load();
        } catch (IOException ex) {
            ex.printStackTrace();
            CustomAlert.showAlertError(ex, mainStackPane.getScene());
        }
        
        Dialog<ButtonType> dialog = new Dialog<>();

        dialog.setTitle("Fshij Pacient");
        dialog.getDialogPane().setContent(dialogLayout);
        dialog.initOwner(mainPane.getScene().getWindow());
        
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);
        Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.CLOSE);
        closeButton.setVisible(false);
        
        dialog.setHeight(200);
        AnchorPane containerPane = (AnchorPane) dialogLayout.getChildren().get(1);
        JFXButton fshijBtn = (JFXButton) containerPane.getChildren().get(2);
        JFXButton mbyllBtn = (JFXButton) containerPane.getChildren().get(3);
        
        fshijBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    PacientiDAO pacientiDAO = new PacientiDAOImpl();
                    pacientiDAO.delete(pacienti.getId(), true);
                    obsPacientetList.remove(pacienti);
                }
                catch(SpitaliDbException ex){
                    CustomAlert.showAlertError(ex, mainStackPane.getScene());
                }
                finally {
                    dialog.close();
                }
            }
        });
        
        mbyllBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialog.close();
            }
        });
        
        CheckBox confirmCheckbox = (CheckBox) containerPane.getChildren().get(4);
        confirmCheckbox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue){
                    fshijBtn.setDisable(false);
                }
                else {
                    fshijBtn.setDisable(true);
                }
            }
        });
        mainPane.setOpacity(0.3);
        dialog.showAndWait();
        mainPane.setOpacity(1);
    }
   
    @FXML
    private void editPacienti(ActionEvent event) throws IOException{
        PacientiTableModel pacientiTM = tabelaPacienteve.getSelectionModel().getSelectedItem();
        if (pacientiTM == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Nuk ka pacient te zgjedhur!");
            alert.setContentText("Ju lutem zgjedhni nga lista");
            alert.showAndWait();
            return;
        }
        
        PacientiDAO pacientiRepository = new PacientiDAOImpl();
        Pacienti selectedPacienti = null;
        try {
            selectedPacienti = pacientiRepository.get(pacientiTM.getId());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        FXMLLoader fxmlLoader = new FXMLLoader();

        JFXButton ruajeButton = null;
        
        fxmlLoader.setLocation(getClass().getResource("/views/editdialogs/pacientiedit.fxml"));
        JFXDialogLayout dialogLayout = fxmlLoader.load();
        AnchorPane anchorPane = (AnchorPane)dialogLayout.getChildren().get(1);
        
        ruajeButton = (JFXButton)anchorPane.getChildren().get(7);        
        
//        JFXDialog dialog = new JFXDialog(mainStackPane, dialogLayout, JFXDialog.DialogTransition.CENTER);
        
        PacientiEditController controller = fxmlLoader.getController();
        controller.setQytetet(qytetet);
        controller.setPacienti(selectedPacienti);
        controller.setProfesionet(profesionet);
        controller.setGrupiGjakut(grupetGjakut);
        controller.editPacienti();
        
        Dialog<ButtonType> dialog = new Dialog<>();
        
        ruajeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Pacienti updatePacienti = controller.updatePacienti();
                try {
                    pacientiRepository.edit(updatePacienti);
                    updateTableModel(pacientiTM, updatePacienti);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                dialog.close();
            }
        });
        
        dialog.setTitle("Edit pacienti");
        dialog.getDialogPane().setContent(dialogLayout);
        dialog.initOwner(mainPane.getScene().getWindow());
        
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);
        Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.CLOSE);
        closeButton.setVisible(false);
        
        mainPane.setOpacity(0.3);
        Optional<ButtonType> result = dialog.showAndWait();
        mainPane.setOpacity(1);
        
    }
    
    private void shikoVizitat(){
        
    }
    
    private void updateTableModel(PacientiTableModel pacientiTM, Pacienti p){
        pacientiTM.setNrPersonal(p.getNrPersonal());
        pacientiTM.setEmri(p.getEmri());
        pacientiTM.setEmriBabes(p.getEmriBabes());
        pacientiTM.setMbiemri(p.getMbiemri());
        pacientiTM.setNrTel(p.getNrTel());
        pacientiTM.setGjinia(p.getGjinia());
        pacientiTM.setEmail(p.getEmail());
        pacientiTM.setDatelindja(p.getDatelindja().toString());
        pacientiTM.setAdresa(p.getAdresaId().getRruga()+", "+p.getAdresaId().getQytetiId().getEmri() +" " +p.getAdresaId().getQytetiId().getZip());
        pacientiTM.setGrupiGjakut(p.getGrupiGjakutId().getTipi());
        pacientiTM.setProfesioni(p.getProfesioniId().getEmri());        
    }

    @FXML
    private void seeMedicalData(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        
        fxmlLoader.setLocation(getClass().getResource("/views/medical-data-list.fxml"));
        JFXDialogLayout dialogLayout = null;
        try {
            dialogLayout = fxmlLoader.load();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        JFXDialog dialog = new JFXDialog(stackPane1, dialogLayout, JFXDialog.DialogTransition.CENTER);
        
        AnchorPane semundjetPane = (AnchorPane) ((AnchorPane) dialogLayout.getChildren().get(1)).getChildren().get(0);
        AnchorPane simptomatPane = (AnchorPane) ((AnchorPane) dialogLayout.getChildren().get(1)).getChildren().get(1);
        AnchorPane alergjitePane = (AnchorPane) ((AnchorPane) dialogLayout.getChildren().get(1)).getChildren().get(2);
        
        semundjetPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                dialog.close();
                showSemundjetPane();
            }
        });
        
        dialog.show();
    }

    private void showSemundjetPane(){
        FXMLLoader semfxmlLoader = new FXMLLoader();
        semfxmlLoader.setLocation(getClass().getResource("/views/semundjet.fxml"));
        try {
//            TranslateTransition transition = new TranslateTransition(Duration.seconds(1));
            JFXDialogLayout dialogLayout = semfxmlLoader.load();
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.getDialogPane().setContent(dialogLayout);
            dialog.initOwner(mainPane.getScene().getWindow());
            dialog.setTitle("Semundjet");
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);
            Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.CLOSE);
            closeButton.setVisible(false);
            
            SemundjetController controller = semfxmlLoader.getController();
            PacientiTableModel pm = tabelaPacienteve.getSelectionModel().getSelectedItem();
            controller.setPacienti(pm.getId());
            
            dialog.showAndWait();
//            transition.setNode(stackPane);
//            transition.setFromX(stackPane1.getLayoutX()*2);
//            transition.setToX(stackPane1.getLayoutX());
//            transition.setCycleCount(1);
//            transition.play();
//        PacientiDAO pd = new PacientiDAOImpl();
//        PacientiSemundjaPK pk = new PacientiSemundjaPK();
//        SemundjaDAO semundjaDAO = new SemundjaDAOImpl();
//        try {
//            
//            Pacienti p = pd.get(1);
//            Session currentSession = HibernateUtil.getSessionFactory().getCurrentSession();
//            currentSession.beginTransaction();
//            Query<Pacienti> createQuery = currentSession.createQuery("select p from Pacienti p JOIN FETCH p.pacientiSemundjaCollection where p.id=:peID", Pacienti.class);
//            createQuery.setParameter("peID", p.getId());
//            
//            Pacienti singleResult = createQuery.getSingleResult();
//            currentSession.getTransaction().commit();
//            
//            Collection<PacientiSemundja> pacientiSemundjaCollection = singleResult.getPacientiSemundjaCollection();
//            for (PacientiSemundja pss : pacientiSemundjaCollection){
//                System.out.println("1>>>" + pss.getSemundja().getEmri());
//            }
//
//
//            
////            Semundja get = semundjaDAO.get(1);
////            pk.setPacientiId(p.getId());
////            pk.setSemundjaId(get.getId());
////            PacientiSemundja ps = new PacientiSemundja();
////            ps.setDataSemundjes(new Date());
////            ps.setPacienti(p);
////            ps.setSemundja(get);
////            ps.setPacientiSemundjaPK(pk);
////            Session currentSession = HibernateUtil.getSessionFactory().openSession();
////            currentSession.beginTransaction();
////            Pacienti p = currentSession.get(Pacienti.class, 1);
////            Collection<PacientiSemundja> pacientiSemundjaCollection = p.getPacientiSemundjaCollection();
////            currentSession.getTransaction().commit();
////            currentSession.close();
////            System.out.println("2>>>"+pacientiSemundjaCollection.size());
////            for (PacientiSemundja pss : pacientiSemundjaCollection){
////                System.out.println("1>>>" + pss.getSemundja().getEmri());
////            }
////            currentSession.save(ps);
//        } catch (SpitaliDbException ex) {
//            CustomAlert.showAlertError(ex, stackPane1.getScene());
//        }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    @FXML
    private void searchPaciente(ActionEvent event) {
    }
}