/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import alerts.CompletedReportDialog;
import alerts.CustomAlert;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import exceptions.SpitaliDbException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import javafx.util.Duration;
import javax.imageio.ImageIO;
import model.Adresa;
import model.Departamenti;
import model.Punetori;
import model.Qyteti;
import model.dao.DepartamentiDAO;
import model.dao.PunetoriDAO;
import model.dao.implementations.DepartamentiDAOImpl;
import model.dao.implementations.PunetoriDAOImpl;
import model.database.config.HibernateUtil;
import model.tablemodels.PunetoriTableModel;
import net.sf.jasperreports.engine.JRException;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import reports.ReportGenerator;
import views.editdialogs.PunetoriEditController;
import views.fshijdialogs.PunetoriDeleteController;

/**
 *
 * @author Fisnik Zejnullahu
 */
public class PunetoriController implements Initializable{
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
    private ComboBox<String> departamentiComboBox;
    private static List<Departamenti> departamentet;
    private List<Qyteti> qytetet;
    
    private int nextId;
    
    private ArrayList<Node> nodes;
    private static ArrayList<Node> JFXnodes;
    @FXML
    private AnchorPane shtoPane;
    
    private List<ImageView> errorImages;
    private List<Label> errorLabels;
    @FXML
    private JFXComboBox<String> prefixTelComboBox;
    @FXML
    private Circle uploadPhotoCircle;
    @FXML
    private Pane photoDetailsPane;
    @FXML
    private ImageView photoImageView;
    @FXML
    private Label nrPersonalLbl;
    @FXML
    private Label emailLbl;
    @FXML
    private Label adresaLbl;
    @FXML
    private Label nrtelLbl;
    @FXML
    private Label datafillimitLbl;
    @FXML
    private Label aktivLbl;
    @FXML
    private Label datambarimitLbl;
    @FXML
    private Label emriBabesLbl;
    @FXML
    private Label gjiniaLbl;
    @FXML
    private Label datelindjaLbl;
    @FXML
    private TableView<PunetoriTableModel> tabelaPunetorve;
    @FXML
    private AnchorPane mainPane;
    
    private byte[] uploadedImage;
    @FXML
    private ImageView fotoFld;
    
    private ObservableList<PunetoriTableModel> obsPunetoretList;
    
    private static ObservableList<String> departamenteList;
    
    @FXML
    private StackPane stackPane1;
    @FXML
    private StackPane stackPane2;
    @FXML
    private FontAwesomeIconView removeImgBtn;
    
    private static ArrayList<String> defaultPromptText;
    
    private FilteredList<PunetoriTableModel> punetoredFilteredList;
    private Predicate<PunetoriTableModel> seeAllPunetoretPredict;
    @FXML
    private JFXTextField searchFld;
    
    private static BooleanProperty resetProperty = new SimpleBooleanProperty(false);
    @FXML
    private AnchorPane informationPane;
    @FXML
    private AnchorPane loadingReportPane;
    
    private ReportGenerator generator;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        errorImages = new ArrayList<>();
        errorLabels = new ArrayList<>();
        initIdCount();
        buildQytetet();        
        buildDepartamentet();
        buildNodes();
        buildPrefixNumbersComboBox();
        buildTabelaPunetorve();
        buildKeyboardListeners();
        
        resetProperty.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue){
                    clearFields();
                }
            }
        });
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
        nodes.add(departamentiComboBox);
    }
    
    public static void buildJFXNodes(ArrayList<Node> list){
        JFXnodes = new ArrayList<>();
        JFXnodes.addAll(list);
        defaultPromptText = new ArrayList<>();
        for (Node n : JFXnodes){
            if (n instanceof JFXTextField){
                JFXTextField tf = (JFXTextField)n;
                defaultPromptText.add(tf.getPromptText());
            }
        }
    }
    
    private void buildPrefixNumbersComboBox(){
        ObservableList<String> list = FXCollections.observableArrayList();
        list.add("+383");
        list.add("+377");
        list.add("+386");
        prefixTelComboBox.setItems(list);
        prefixTelComboBox.setValue("+383");
    }
    
    private void initIdCount(){
        Session session = HibernateUtil.getSessionFactory().openSession();
        NativeQuery<BigDecimal> queryIdCount = session.createNativeQuery("SELECT IDENT_CURRENT('Punetori') + IDENT_INCR('Punetori')");
        
        BigDecimal bd = queryIdCount.getSingleResult();
        session.close();
        nextId = bd.intValue();
        idFld.setText(String.valueOf(nextId));
    }
    
    private void buildQytetet(){
        
/*        Shteti shtetiId;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            shtetiId = session.get(Shteti.class, 1);
            session.getTransaction().commit();
        }
        
        List<String> qytet = Arrays.asList(
                "Prishtine",
                "Gjakove",
                "Pejë",
                "Ferizaj",
                "Prizren",
                "Gjilan"
        );
        Collections.sort(qytet);
        for (String s : qytet ){
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                session.beginTransaction();
                Qyteti d = new Qyteti();
                d.setEmri(s);
                d.setShtetiId(shtetiId);
                d.setZip(getZip(s));
                session.save(d);
                session.getTransaction().commit();
            }
        }*/
        
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        qytetet = session.createNamedQuery("Qyteti.findAll", Qyteti.class).getResultList();
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
    
    private void buildDepartamentet(){ 
        /*Spitali spitaliId;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            spitaliId = session.get(Spitali.class, 1);
            session.getTransaction().commit();
        }
        List<String> deps = Arrays.asList(
                "Aksidenti dhe emergjenca",
                "Kardioligji",
                "Klerikëve",
                "Kujdesi kritik",
                "Hundë, vesh dhe fyt",
                "Gastroenterology",
                "General surgery",
                "Onkologji",
                "Farmaci",
                "Urologji"
        );
        Collections.sort(deps);
        for (String s : deps ){
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                session.beginTransaction();
                Departamenti d = new Departamenti();
                d.setEmri(s);
                d.setSpitaliId(spitaliId);
                d.setDataKrijimit(new Date());
                session.save(d);
                session.getTransaction().commit();
            }
        }*/

        DepartamentiDAO departamentiDAO = new DepartamentiDAOImpl();
        try {
            departamentet = departamentiDAO.getDepartamentetList();
        } catch (SpitaliDbException ex) {
            showAlertError(ex);
            return;
        }
                
        departamenteList = FXCollections.observableArrayList();
        for (Departamenti d : departamentet){
            departamenteList.add(d.getEmri());        
        }
        departamentiComboBox.setItems(departamenteList);
        departamentiComboBox.setValue("Zgjedh departamentin...");
    }
    
    public static void addDepartmentToComboBoxList(Departamenti dep){
        departamentet.add(dep);
        departamenteList.add(dep.getEmri());
    }
    
    public static void removeDepartmentFromComboBoxList(Departamenti dep){
        departamentet.remove(dep);
        departamenteList.remove(dep.getEmri());
    }
    
    public static void editDepartmentInComboBoxList(Departamenti dep){
        String oldName = null;
        for (Departamenti d : departamentet){
            if (d.getId().equals(dep.getId())){
                oldName = d.getEmri();
                d = dep;
            }
        }
        departamenteList.remove(oldName);
        departamenteList.add(dep.getEmri());
    }
    
    @FXML
    private void save(ActionEvent event) {
        ArrayList<String> validateEmptyFields = validateEmptyFields(nodes, datelindjaFld);
        if (!validateEmptyFields.isEmpty()){
            createErrorImages(nodes, validateEmptyFields, datelindjaFld, true);
//            shtoPane.getChildren().addAll(errorImages);
            shtoPane.getChildren().addAll(errorLabels);
            return;
        }
        
        ArrayList<String> checkAndValidateField = checkAndValidateField(nodes);
        if (!checkAndValidateField.isEmpty()){
            createErrorImages(nodes, checkAndValidateField, null, false);
//            shtoPane.getChildren().addAll(errorImages);
            shtoPane.getChildren().addAll(errorLabels);
            return;
        }
        
        long nrPersonal = Long.parseLong(nrPersonalFld.getText());
        String emri = emriFld.getText();
        String emriBabes = emriBabesFld.getText();
        String mbiemri = mbiemriFld.getText();
        String nrTel = prefixTelComboBox.getValue()+nrTelFld.getText();
        String rruga = adresaFld.getText();
        String gjinia = String.valueOf(((JFXRadioButton)gjiniaToggleGroup.getSelectedToggle()).getText().charAt(0));
        String email = emailFld.getText();
        Date datelindja = java.sql.Date.valueOf(datelindjaFld.getValue());
        Date dataFillimit = new Date();
        
        Adresa adresa = new Adresa();
        Qyteti q = getQytetiId(qytetiComboBox.getValue());
        adresa.setRruga(rruga);
        adresa.setQytetiId(q);
        
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.save(adresa);
        session.getTransaction().commit();
        
        Punetori p = new Punetori();
        p.setNrPersonal(nrPersonal);
        p.setEmri(emri);
        p.setEmriBabes(emriBabes);
        p.setMbiemri(mbiemri);
        p.setNrTel(nrTel);
        p.setAdresaId(adresa);
        p.setGjinia(gjinia);
        p.setEmail(email);
        p.setDatelindja(datelindja); 
        p.setDepartamentiId(getDepartamentiId(departamentiComboBox.getValue()));
        p.setDataFillimit(dataFillimit);
        
        if (uploadedImage == null){
            File file = new File("nuk_u_gjet.png");
            readImageFile(file);
        }
        
        p.setFoto(uploadedImage);
        
        PunetoriDAO punetoriDAO = new PunetoriDAOImpl();
        try {
            punetoriDAO.create(p);
        } catch (SpitaliDbException ex) {
            showAlertError(ex);
            return;
        }
        
        addPunetoriTableModel(p);
        clearFields();
        nextId++;
        idFld.setText(String.valueOf(nextId));
        showSavedPunetoriDialog(p);
    }
    
    private void showSavedPunetoriDialog(Punetori p){
        CustomAlert.showFinishedDialog(stackPane2, "Punetori \""+p.getEmri()+" "+p.getMbiemri()+"\", ", "u regjistrua me sukses!");
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
    
    public static void reset(){
        resetProperty.set(true);
        resetProperty.set(false);
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
            shtoPane.getChildren().removeAll(errorLabels);
            errorImages.clear();
            errorLabels.clear();
            removeErrorClass(n);
        }
        departamentiComboBox.setValue("Zgjedh departamentin...");
        qytetiComboBox.setValue("Zgjedh qytetin...");
        removeErrorClass(datelindjaFld);
        
        removeUploadedImage();
        
        datelindjaFld.setValue(null);
    }
    
    @FXML
    private void removeUploadedImage(){
        photoImageView.setImage(null);
        photoImageView.setVisible(false);
        photoDetailsPane.setVisible(true);
        uploadedImage = null;
    }
    
    private ArrayList<String> validateEmptyFields(ArrayList<Node> nodes, DatePicker datelindjaFld){
        ArrayList<String> idMeErrore = new ArrayList<>();
        
        shtoPane.getChildren().removeAll(errorImages);
        shtoPane.getChildren().removeAll(errorLabels);
        errorImages.clear();
        errorLabels.clear();
        if (datelindjaFld != null){
            if (datelindjaFld.getValue() == null){
                idMeErrore.add(datelindjaFld.getId());
            }
        }
        
        
        for (Node n : nodes){
            if (n instanceof TextField){
                if (!n.getId().equals("idFld") && !n.getId().equals("shtetiFld")){
                    TextField tf = (TextField)n;
                    if (tf.getText().isEmpty()){
                        idMeErrore.add(tf.getId());
                    }
                }
            }
            else if (n instanceof ComboBox){
                ComboBox<String> combo = (ComboBox<String>)n;
                if (combo.getValue().startsWith("Zgjedh") || combo.getValue().startsWith("Ju lutem")){
                    idMeErrore.add(combo.getId());
                }
            }
        }
        return idMeErrore;
    }
    
    private void createErrorImages(ArrayList<Node> nodes, ArrayList<String> idMeErrore, DatePicker datelindjaFld, boolean emptyValidate){
        for (String id : idMeErrore){
            if (datelindjaFld!=null){
                if (id.equals(datelindjaFld.getId())){
                    addErrorImage(datelindjaFld, "Ju lutem plotesoni kete fushe!");
                    continue;
                }
                else {
                    removeErrorClass(datelindjaFld);
                }
            }
            for (Node n : nodes){
                if (n.getId().equals(id)){
                    if (emptyValidate){
                        addErrorImage(n, "Ju lutem plotesoni kete fushe!");                    
                    }
                    else {
                        switch (n.getId()) {
                            case "nrPersonalFld":
                                addErrorImage(n, "Ky format i nr. personal nuk u gjet!");
                                break;
                            case "nrTelFld":
                                addErrorImage(n, "Lejohen vetem numra! (8) Nr. tel. duhet te kete (8) shifra");
                                break;
                            case "emailFld":
                                addErrorImage(n, "Lejohen vetem karakteret (a-z), numrat (0-9) dhe pikat (.)");
                                break;
                            default:
                                addErrorImage(n, "Lejohet vetem tekst, ose ke kaluar limit e fjales(MAX = 50)");
                        }
                    }
                }
                else {
                    removeErrorClass(n);
                }
            }
        }
    }
    
    private void createErrorLabels(ArrayList<Node> nodes, ArrayList<String> idMeErrore, boolean emptyValidate){
        removeErrorPromptText();
        
        for (String id : idMeErrore){
            for (Node n : nodes){
                if (n.getId().equals(id)){
                    if (emptyValidate){
                        addErrorImage(n, "Ju lutem plotesoni kete fushe!");
                    }
                    else {
                        switch (n.getId()) {
                            case "nrPersonalFld":
                                addErrorImage(n, "Ky format i nr. personal nuk u gjet!");
                                break;
                            case "nrTelFld":
                                addErrorImage(n, "Lejohen vetem numra! (8) Nr. tel. duhet te kete (8) shifra");
                                break;
                            case "emailFld":
                                addErrorImage(n, "Lejohen vetem karakteret (a-z), numrat (0-9) dhe pikat (.)");
                                break;
                            default:
                                addErrorImage(n, "Lejohet vetem tekst, ose ke kaluar limit e fjales(MAX = 50)");
                        }
                    }
                }
            }
        }
    }
    
    private void addErrorPromptText(Node n, String errorText){
        if (n instanceof JFXTextField){
            ((JFXTextField) n).setPromptText(errorText);
            ((JFXTextField) n).setFocusColor(Color.RED);
            ((JFXTextField) n).setUnFocusColor(Color.RED);
        }
    }
    
    private void removeErrorPromptText(){
        for (int i=0; i<JFXnodes.size(); i++){
            JFXTextField n = (JFXTextField) JFXnodes.get(i);
            
            String promptText = PunetoriController.defaultPromptText.get(i);
            
            n.setPromptText(promptText);
            n.setFocusColor(Color.valueOf("#4059a9"));
            n.setFocusColor(Color.valueOf("#4d4d4d"));
        }
        
    }
    
    private ArrayList<String> checkAndValidateField(ArrayList<Node> nodes){
        ArrayList<String> idMeErrore = new ArrayList<>();
        shtoPane.getChildren().removeAll(errorLabels);
        shtoPane.getChildren().removeAll(errorImages);
        errorImages.clear();
        errorLabels.clear();
        
        boolean validateField = true;
        
        for (Node n : nodes){
            if (n.getId().equals("idFld") || n.getId().equals("shtetiFld") || n instanceof ComboBox) continue;

            switch (n.getId()) {
                case "nrPersonalFld":
                    validateField = validateField(n, 10, true);
                    if (!validateField){
                        idMeErrore.add(n.getId());
                    }
                    break;
                case "nrTelFld":
                    validateField = validateField(n, 11, true);
                    if (!validateField){
                        idMeErrore.add(n.getId());

                    }
                    break;
                case "emailFld":
                    validateField = validateField(n, 0, false);
                    if (!validateField){
                        idMeErrore.add(n.getId());
                    }
                    break;
                default:
                    validateField = validateField(n, 50, false);
                    if (!validateField){
                        idMeErrore.add(n.getId());
                    }
            }
        }
        return idMeErrore;
    }
    
    private boolean validateField(Node n, int maxLength, boolean onlyNumbers){
        if (n instanceof TextField){
            TextField tf = (TextField)n;
            String teksti = tf.getText().trim().toLowerCase();
            
            if (onlyNumbers){
                if (tf.getId().equals("nrTelFld")){
                    if (teksti.startsWith("+")){
                        teksti = teksti.substring(1, teksti.length());
                    }
                    else {
                        teksti = prefixTelComboBox.getValue().substring(1, 4);
                        teksti += tf.getText();
                    }
                }
                if (teksti.length() != maxLength) {
                    return false; 
                }
                boolean hasNumbers = Pattern.matches("[0-9]{"+ maxLength + "}", teksti);
                return hasNumbers;
            }
            else if (n.getId().equals("emailFld")){ 
                return Pattern.matches("([0-9a-z]+[-]?[\\.]?)+[@]{1}[a-z]*[\\.]{1}[a-z]+", teksti);
            }
            else {
                if (teksti.length() > maxLength) return false;
                
                boolean hasChar = false;
                if (tf.getId().equals("adresaFld")) hasChar = Pattern.matches("[a-z0-9[\\s\\.,-]]+", teksti);
                else if (tf.getId().equals("emriFld") || tf.getId().equals("mbiemriFld") || tf.getId().equals("emriBabesFld")) hasChar = Pattern.matches("[a-z[\\s\\.-]]+", teksti);
                else                                hasChar = Pattern.matches("[a-z]+", teksti);
                return hasChar;
            }
        }
        return true;
    }
    
    private void addErrorImage(Node n, String errorText){
//        ImageView image = new ImageView("/images/error30.png");
//        if (!n.getStyleClass().contains("textfield-error")){
//            n.getStyleClass().add("textfield-error");
//        }
//        image.setLayoutX(n.getLayoutX()+285);
//        image.setLayoutY(n.getLayoutY()+7);
//        
//        errorImages.add(image);
        addErrorLabel(n, errorText);
    }
    
    private void addErrorLabel(Node n, String errorText){
        Label label = new Label(errorText);
        label.setTextFill(Color.RED);
        label.setLayoutX(n.getLayoutX()+5);
        label.setLayoutY(43+n.getLayoutY());
        errorLabels.add(label);
    }
    
    private void removeErrorClass(Node n){
        n.getStyleClass().remove("textfield-error");
    }

    @FXML
    private void uploadPhoto(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(shtoPane.getScene().getWindow());
        
        Shape circle = new Circle(uploadPhotoCircle.getCenterX(), uploadPhotoCircle.getCenterY(), uploadPhotoCircle.getRadius());
        circle.setLayoutX(uploadPhotoCircle.getLayoutX());
        circle.setLayoutY(uploadPhotoCircle.getLayoutY());
        photoImageView.setClip(circle);
        
//        photoImageView.setImage(new Image("/images/loadingPhoto.gif"));
        
        if (file != null){
            readImageFile(file);
        }
        
        removeImgBtn.setVisible(true);
        photoDetailsPane.setVisible(false);
        photoImageView.setVisible(true);
    }
    
    private void readImageFile(File file){
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(file);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Image image = SwingFXUtils.toFXImage(bufferedImage, null);

        photoImageView.setImage(image);

        uploadedImage = new byte[(int) file.length()];

        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(uploadedImage);
            fileInputStream.close();
        } catch (Exception e) {
            showAlertError(e);
        }
    }
    
    private void selectPunetori(PunetoriTableModel p){
        nrPersonalLbl.setText("Nr. Personal: " + String.valueOf(p.getNrPersonal()));
        emriBabesLbl.setText("Emri i babes: " + p.getEmriBabes());
        emailLbl.setText("Emaili: " + p.getEmail());
        adresaLbl.setText("Adresa: " + p.getAdresa());
        nrtelLbl.setText("Nr. tel: " + p.getNrTel());
        datafillimitLbl.setText("Data e fillimit te punes: " + p.getDataFillimit());
        datambarimitLbl.setText("Data e mbarimit te punes: " + p.getDataMbarimit().toString());
        if (p.getDataMbarimit().equals("-")){
                aktivLbl.setText("Aktiv: PO");
        }
        else{
            aktivLbl.setText("Aktiv: JO");
        }
        gjiniaLbl.setText("Gjinia: " + (p.getGjinia().equals("M") ? "Mashkull" : "Femer"));
        datelindjaLbl.setText("Datelindja: " + p.getDatelindja());
        
        FileOutputStream fos = null;
            try {
                File file = new File("getfoto.png");
                fos = new FileOutputStream(file);
                fos.write(p.getFoto());
                fotoFld.setImage(new Image("file:getfoto.png"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            finally {
                if (fos!= null){
                    try {
                        fos.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
    }
    
    private void buildTabelaPunetorve() {
        PunetoriDAO punetoriDAO = new PunetoriDAOImpl();
        List<Punetori> punetoretList;
        
        try {
            punetoretList = punetoriDAO.getPunetoretList();
        } catch (SpitaliDbException ex) {
            showAlertError(ex);
            return;
        }
        
        obsPunetoretList = FXCollections.observableArrayList();
        
        for (Punetori punetori : punetoretList) {
            String dataMbarimit;
            if (punetori.getDataMbarimit() == null){
                dataMbarimit = "-";
            }
            else{
                dataMbarimit = punetori.getDataMbarimit().toString();
            }
            if (punetori.getId() != 9){
                obsPunetoretList.add(
                        new PunetoriTableModel(punetori.getId(), punetori.getNrPersonal(),punetori.getEmri(), punetori.getEmriBabes(), punetori.getMbiemri(),
                                            punetori.getNrTel(), punetori.getAdresaId().getRruga()+", "+punetori.getAdresaId().getQytetiId().getEmri() +" " +punetori.getAdresaId().getQytetiId().getZip(),
                                            punetori.getGjinia(), punetori.getEmail(),
                                            punetori.getDatelindja().toString(), punetori.getDepartamentiId().getEmri(),
                                            punetori.getDataFillimit().toString(), dataMbarimit,
                                            punetori.getFoto())
                );
            }
        }
        
        TableColumn<PunetoriTableModel, String> idCol = new TableColumn<>("PUNETORI ID");
        idCol.setResizable(false);
        idCol.setPrefWidth(245);
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        
        TableColumn<PunetoriTableModel, String> emriCol = new TableColumn<>("EMRI");
        emriCol.setResizable(false);
        emriCol.setPrefWidth(420);
        
        emriCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PunetoriTableModel, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<PunetoriTableModel, String> cellData) {
                return Bindings.concat(
                        cellData.getValue().emriProperty(),
                        " ",
                        cellData.getValue().mbiemriProperty());
            }
        });
        
        TableColumn<PunetoriTableModel, String> depCol = new TableColumn<>("DEPARTAMENTI");
        depCol.setResizable(false);
        depCol.setPrefWidth(377);
        depCol.setCellValueFactory(new PropertyValueFactory<>("departamenti"));
        
        tabelaPunetorve.getColumns().addAll(idCol, emriCol, depCol);
        seeAllPunetoretPredict = new Predicate<PunetoriTableModel>() {
            @Override
            public boolean test(PunetoriTableModel t) {
                return true;
            }
        };
          
        punetoredFilteredList = new FilteredList<>(obsPunetoretList, seeAllPunetoretPredict);
        
        tabelaPunetorve.setItems(punetoredFilteredList);
        
        searchFld.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.trim().isEmpty())
                {
                    punetoredFilteredList.setPredicate(seeAllPunetoretPredict);
                    if (!tabelaPunetorve.getItems().isEmpty()){
                        tabelaPunetorve.getSelectionModel().selectFirst();        
                        selectPunetori(tabelaPunetorve.getSelectionModel().getSelectedItem());
                    }
                }
                else 
                {
                    searchPunetore();
                }
            }
        });
        
        if (!tabelaPunetorve.getItems().isEmpty()){
            tabelaPunetorve.getSelectionModel().selectFirst();
            selectPunetori(tabelaPunetorve.getSelectionModel().getSelectedItem());
            selectPunetori(tabelaPunetorve.focusModelProperty().get().getFocusedItem());
        }
        tabelaPunetorve.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<PunetoriTableModel>() {
            @Override
            public void changed(ObservableValue<? extends PunetoriTableModel> observable, PunetoriTableModel oldValue, PunetoriTableModel newValue) {
                if (newValue != null){
                    selectPunetori(newValue);
                }
            }
        });
    }
    
    private void addPunetoriTableModel(Punetori p){
        PunetoriTableModel punetoriTM = new PunetoriTableModel();
        punetoriTM.setId(p.getId());
        punetoriTM.setNrPersonal(p.getNrPersonal());
        punetoriTM.setEmri(p.getEmri());
        punetoriTM.setEmriBabes(p.getEmriBabes());
        punetoriTM.setMbiemri(p.getMbiemri());
        punetoriTM.setNrTel(p.getNrTel());
        punetoriTM.setGjinia(p.getGjinia());
        punetoriTM.setEmail(p.getEmail());
        punetoriTM.setDatelindja(p.getDatelindja().toString());
        SimpleDateFormat dt = new SimpleDateFormat("YYYY-MM-dd");
        punetoriTM.setDataFillimit(dt.format(p.getDataFillimit()));
        if (p.getDataMbarimit() == null){
            aktivLbl.setText("Aktiv: PO");
            punetoriTM.setDataMbarimit("-");
        }else{
            aktivLbl.setText("Aktiv: JO");
            punetoriTM.setDataMbarimit(dt.format(p.getDataMbarimit().toString()));
        }
        punetoriTM.setAdresa(p.getAdresaId().getRruga()+", "+p.getAdresaId().getQytetiId().getEmri() +" " +p.getAdresaId().getQytetiId().getZip());
        punetoriTM.setDepartamenti(p.getDepartamentiId().getEmri());
        punetoriTM.setFoto(p.getFoto());
        obsPunetoretList.add(punetoriTM);
        tabelaPunetorve.getSelectionModel().select(punetoriTM);
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
    
    @FXML
    private void deletePunetori(ActionEvent event) throws IOException{
        PunetoriTableModel punetoriTM = tabelaPunetorve.getSelectionModel().getSelectedItem();

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/views/fshijdialogs/fshijdialog.fxml"));
        JFXDialogLayout dialogLayout = fxmlLoader.load();
        
        PunetoriDeleteController controller = fxmlLoader.getController();
        controller.setPunetoriTableModel(punetoriTM);
        
        Dialog<ButtonType> dialog = new Dialog<>();
        
        try {
            controller.buildDialog(dialog);
        }
        catch(SpitaliDbException ex){
            showAlertError(ex);
        }        

        dialog.setTitle("Fshij Punetorin");
        dialog.getDialogPane().setContent(dialogLayout);
        dialog.initOwner(mainPane.getScene().getWindow());
        
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);
        Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.CLOSE);
        closeButton.setVisible(false);
        
        mainPane.setOpacity(0.3);
        dialog.showAndWait();
        
        dialog.setHeight(200);
        
        if (controller.uFshi()){
            if (controller.isSelectedFshijCheckBox()){
                obsPunetoretList.remove(punetoriTM);
                CustomAlert.showFinishedDialog(stackPane1, "Punetori \""+punetoriTM.getEmri()+" "+punetoriTM.getMbiemri()+"\", ", "u fshi me sukses!");
            }
            else {
                punetoriTM.setDataMbarimit(new Date().toString());
                selectPunetori(punetoriTM);
            }
        }
        mainPane.setOpacity(1);            
    }
   
    @FXML
    private void editPunetori(ActionEvent event) throws IOException{
        PunetoriTableModel punetoriTM = tabelaPunetorve.getSelectionModel().getSelectedItem();
        if (punetoriTM == null){
            CustomAlert.showSimpleInformationAlert(
                mainPane.getScene(), "Nuk ka punetore te selektuar!", "Ju lutem zgjidhni nga lista!"
            );
            return;
        }
        
        PunetoriDAO punetoriDAO = new PunetoriDAOImpl();
        Punetori selectedPunetori = null;
        try {
            selectedPunetori = punetoriDAO.get(punetoriTM.getId());
        } catch (SpitaliDbException ex) {
            showAlertError(ex);
        }
        
        FXMLLoader fxmlLoader = new FXMLLoader();

        JFXButton ruajeButton = null;
        
        fxmlLoader.setLocation(getClass().getResource("/views/editdialogs/punetoriedit.fxml"));
        JFXDialogLayout dialogLayout = fxmlLoader.load();
        AnchorPane anchorPane = (AnchorPane)dialogLayout.getChildren().get(1);
//        errorImages.clear();
        errorLabels.clear();
        ruajeButton = (JFXButton)anchorPane.getChildren().get(7);        
        
        PunetoriEditController controller = fxmlLoader.getController();
        controller.removeErrorLabels();
        controller.setDepartamentet(departamentet);
        controller.setQytetet(qytetet);
        controller.setPunetori(selectedPunetori);
        controller.editPunetori();
        
        Dialog<ButtonType> dialog = new Dialog<>();
        
        ruajeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                anchorPane.getChildren().removeAll(errorLabels);
//                anchorPane.getChildren().removeAll(errorImages);
                ArrayList<String> validateEmptyFields = validateEmptyFields(JFXnodes, null);
                if (!validateEmptyFields.isEmpty()){
                    createErrorLabels(JFXnodes, validateEmptyFields, true);
                    anchorPane.getChildren().addAll(errorLabels);
//                    anchorPane.getChildren().addAll(errorImages);
                    return;
                }
                
//                anchorPane.getChildren().removeAll(errorImages);
//                anchorPane.getChildren().removeAll(errorLabels);
//                errorImages.clear();
//                errorLabels.clear();
                
                ArrayList<String> checkAndValidateField = checkAndValidateField(JFXnodes);
                if (!checkAndValidateField.isEmpty()){
                    createErrorLabels(JFXnodes, checkAndValidateField, false);
                    anchorPane.getChildren().addAll(errorLabels);
//                    anchorPane.getChildren().addAll(errorImages);
                    return;
                }
                
                Punetori updatePunetori = controller.updatePunetori();
                
                try {
                    punetoriDAO.edit(updatePunetori);
                    updateTableModel(punetoriTM, updatePunetori);
                } catch (SpitaliDbException ex) {
                    showAlertError(ex);
                    return;
                }
                dialog.close();
            }
        });
        
        dialog.setTitle("Edit Punetori");
        dialog.getDialogPane().setContent(dialogLayout);
        dialog.initOwner(mainPane.getScene().getWindow());
        
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);
        Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.CLOSE);
        closeButton.setVisible(false);
        
        mainPane.setOpacity(0.3);
        dialog.showAndWait();
        mainPane.setOpacity(1);
        
    }
    
    private void updateTableModel(PunetoriTableModel punetoriTM, Punetori p){
        punetoriTM.setNrPersonal(p.getNrPersonal());
        punetoriTM.setEmri(p.getEmri());
        punetoriTM.setEmriBabes(p.getEmriBabes());
        punetoriTM.setMbiemri(p.getMbiemri());
        punetoriTM.setNrTel(p.getNrTel());
        punetoriTM.setGjinia(p.getGjinia());
        punetoriTM.setEmail(p.getEmail());
        punetoriTM.setDatelindja(p.getDatelindja().toString());
        SimpleDateFormat dt = new SimpleDateFormat("YYYY-MM-dd");
        punetoriTM.setDataFillimit(dt.format(p.getDataFillimit()));
        if (p.getDataMbarimit() == null){
            aktivLbl.setText("Aktiv: PO");
            punetoriTM.setDataMbarimit("-");
        }else{
            aktivLbl.setText("Aktiv: JO");
            punetoriTM.setDataMbarimit(dt.format(p.getDataMbarimit()));
        }
        punetoriTM.setAdresa(p.getAdresaId().getRruga()+", "+p.getAdresaId().getQytetiId().getEmri() +" " +p.getAdresaId().getQytetiId().getZip());
        punetoriTM.setDepartamenti(p.getDepartamentiId().getEmri());
        selectPunetori(punetoriTM);
    }
    
    @FXML
    private void searchPunetore() {
        if (!searchFld.getText().isEmpty()){
            String searched = searchFld.getText().toLowerCase();
            Predicate<PunetoriTableModel> predict = new Predicate<PunetoriTableModel>() {
                @Override
                public boolean test(PunetoriTableModel t) {
                    return t.getEmri().toLowerCase().startsWith(searched);
                }
            };

            punetoredFilteredList.setPredicate(predict);
            if (!tabelaPunetorve.getItems().isEmpty()){
                tabelaPunetorve.getSelectionModel().selectFirst();
                selectPunetori(tabelaPunetorve.getSelectionModel().getSelectedItem());
            }
        }
    }
    
    private void showAlertError(Exception exception){
        CustomAlert.showAlertError(exception, mainPane.getScene());
    }

    @FXML
    private void exportPunetoriDataToPdf(MouseEvent event) {
        showLoadingImg();
        PunetoriTableModel punetoriTM = tabelaPunetorve.getSelectionModel().getSelectedItem();
        
        class ReportBuilderThread extends Thread{
            @Override
            public void run() {
                generator = new ReportGenerator();
                generator.setPunetori(punetoriTM);
                try {
                    generator.exportPunetoriReport();
                } catch (JRException | SpitaliDbException | SQLException ex) {
                    Platform.runLater(new Runnable(){
                        @Override
                        public void run(){
                            hideLoadingImg();
                            showAlertError(ex);
                        }
                    });
                    return;
                }
                hideLoadingImg();
                Platform.runLater(new Runnable(){
                    @Override
                    public void run(){
                        showConfirmReportDialog(generator, "/downloads/raporti-"+punetoriTM.getEmri()+punetoriTM.getMbiemri()+".pdf");
                    }
                });
            }
        }
        new ReportBuilderThread().start();
//        Thread thr = new Thread(){
//            @Override
//            public void run() {
//                generator = new ReportGenerator();
//                generator.setPunetori(punetoriTM);
//                try {
//                    generator.exportPunetoriReport();
//                } catch (JRException | SpitaliDbException | SQLException ex) {
//                    Platform.runLater(new Runnable(){
//                        @Override
//                        public void run(){
//                            hideLoadingImg();
//                            showAlertError(ex);
//                        }
//                    });
//                    return;
//                }
//                hideLoadingImg();
//                Platform.runLater(new Runnable(){
//                    @Override
//                    public void run(){
//                        showConfirmReportDialog(generator, "/downloads/raporti-"+punetoriTM.getEmri()+punetoriTM.getMbiemri()+".pdf");
//                    }
//                });
//            }
//        };
//        thr.start();
    }
    
    private void showLoadingImg(){
        loadingReportPane.setVisible(true);
        informationPane.setOpacity(0.2);
        tabelaPunetorve.setOpacity(0.2);
    }
    private void hideLoadingImg(){
        loadingReportPane.setVisible(false);
        informationPane.setOpacity(1);
        tabelaPunetorve.setOpacity(1);
    }
    
    private void showConfirmReportDialog(ReportGenerator generator, String fileName){
        CompletedReportDialog.showDialog(stackPane1, generator, fileName);
//        Text successTxt = new Text("Raporti u gjenerua me sukses!");
//        successTxt.setFill(Color.valueOf("#55ba50"));
//        
//        JFXButton shikoBtn = new JFXButton("SHIKOJE");
//        shikoBtn.setStyle("-fx-button-type: RAISED; -fx-background-color: #26C6DA; -fx-font-size: 17px;-fx-text-fill: WHITE;");
//        JFXButton exportBtn = new JFXButton("EXPORTO NE PDF");
//        exportBtn.setStyle("-fx-button-type: RAISED; -fx-background-color: #26A69A; -fx-font-size: 17px;-fx-text-fill: WHITE;");
//        
//        JFXDialogLayout dialogLayout = new JFXDialogLayout();
//        
//        dialogLayout.setPrefWidth(350);
//        dialogLayout.setPrefHeight(200);
//                
//        JFXDialog dialog = new JFXDialog(stackPane1, dialogLayout, JFXDialog.DialogTransition.TOP);
//        
//        shikoBtn.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event1) {
//                generator.showReport();
//            }
//        });
//        
//        exportBtn.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event1) {
//                try {
//                    File exported = generator.exportReportToPdf();
//                    dialog.close();
//                    Desktop.getDesktop().open(exported);
//                } catch (JRException | SQLException | IOException ex) {
//                    showAlertError(ex);
//                    return;
//                }
//            }
//        });
//        
//        HBox hbox=new HBox();
//        hbox.setSpacing(10);
//        hbox.setPrefSize(200, 70);
//        hbox.setAlignment(Pos.BOTTOM_CENTER);
//        hbox.getChildren().addAll(shikoBtn, exportBtn);
//        
//        dialogLayout.setHeading(successTxt);
//        dialogLayout.setActions(hbox);
//        dialogLayout.setBody(hbox);
//        
//        dialog.show();
    }
}
