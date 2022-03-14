/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import alerts.CustomAlert;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import exceptions.SpitaliDbException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Departamenti;
import model.Punetori;
import model.Spitali;
import model.dao.DepartamentiDAO;
import model.dao.implementations.DepartamentiDAOImpl;
import model.database.config.HibernateUtil;
import org.hibernate.Session;
import views.editdialogs.DepartamentiEditController;

/**
 * FXML Controller class
 *
 * @author Fisnik Zejnullahu
 */
public class DepartamentiController implements Initializable {

    private static int countRreshti = 0;
    @FXML
    private StackPane mainStackPane;

    private JFXTextField txtDepartment;
    
    @FXML
    private ScrollPane scrollPane;
    
    @FXML
    private HBox listaHBox;
    @FXML
    private VBox mainVBox;
    
    private List<Departamenti> departamentet;
    private ContextMenu contextMenu;
    private MenuItem deleteItem, editItem;
    
    private List<HBox> rreshtat;
    @FXML
    private ImageView loadingImg;
    
//    private Font quicksandFont = Font.loadFont(getClass().getResource("../styles/fonts/quicksand/Quicksand-Regular.otf").toExternalForm(), 120);
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rreshtat = new ArrayList<>();
//        System.out.println(quicksandFont.getName());
        listaHBox = buildDepartmentsHBox();
        try {
            buildDepartamentetList();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        contextMenu = new ContextMenu();
        
        deleteItem = new MenuItem("Delete department...");
        editItem = new MenuItem("Edit department...");
        contextMenu.getItems().addAll(deleteItem, editItem);
    }

    @FXML
    private void add(ActionEvent event) {
        VBox vbox = new VBox(15);
        JFXTextField idFld = new JFXTextField();
        idFld.setDisable(true);
        idFld.setPromptText("2");
        
        txtDepartment = new JFXTextField();
        txtDepartment.setPromptText("Shkruaj emrin e departamentit...");
        txtDepartment.setLabelFloat(false);
        txtDepartment.setPrefSize(150, 50);
        txtDepartment.getStyleClass().add("add-dep-txtfield");
        
        Label errorLabel = new Label("Departamenti ekziston!");
        errorLabel.setTextFill(Color.RED);
        errorLabel.setVisible(false);
        
        vbox.getChildren().addAll(txtDepartment, errorLabel);
        // Heading text
        
        vbox.setPadding(new Insets(45, 5, 10, 5));
        
        Text t = new Text("SHTO DEPARTAMENT");
        t.setStyle("-fx-font-size:20px;");

        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        
        dialogLayout.setHeading(t);
        dialogLayout.setBody(vbox);
        dialogLayout.setPrefHeight(300);

        JFXDialog dialog = new JFXDialog(mainStackPane, dialogLayout, JFXDialog.DialogTransition.CENTER);
        
        JFXButton closeButton = new JFXButton("Mbyll");
        closeButton.setStyle("-fx-button-type: RAISED; -fx-background-color: #26C6DA; -fx-font-size: 17px;-fx-text-fill: WHITE;");
        JFXButton addBtn = new JFXButton("Ruaje");
        addBtn.setStyle("-fx-button-type: RAISED; -fx-background-color: #26A69A; -fx-font-size: 17px;-fx-text-fill: WHITE;");
        
        txtDepartment.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER){
                    addBtn.fire();
                }
            }
        });
        
        closeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event1) {
                dialog.close();
                txtDepartment.setStyle("-fx-text-fill: #485460");
                txtDepartment.setFocusColor(Color.valueOf("#485460"));
                errorLabel.setVisible(false);
            }
        });
        
        addBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event1) {
                if (txtDepartment.getText().trim().isEmpty()){
                    errorLabel.setText("Nuk keni shkruar asnje emer!");
                    errorLabel.setVisible(true);
                    return;
                }
                Departamenti newDepartment = null;

                try {
                    validateDepartment(txtDepartment.getText());
                    newDepartment = shtoDepartament(txtDepartment.getText());
                    if (newDepartment == null){
                        return;
                    }
                    try {
                        saveDepartment(newDepartment);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } catch (SpitaliDbException ex) {
                    errorLabel.setText(ex.getMessage());
                    txtDepartment.setFocusColor(Color.RED);
                    txtDepartment.requestFocus();
                    txtDepartment.setStyle("-fx-text-fill: #ff0000");
                    errorLabel.setVisible(true);
                    return;
                }
                
                dialog.close();
                txtDepartment.setFocusColor(Color.valueOf("#485460"));
                errorLabel.setVisible(false);
            }
        });
        
        HBox box=new HBox();
        box.setSpacing(10);
        box.setPrefSize(200, 70);
        box.setAlignment(Pos.CENTER_RIGHT);
        box.getChildren().addAll(closeButton, addBtn);

        addEscapeKeyPressed(dialog);
        dialogLayout.setActions(box);
        
        dialog.show();
    }
    
    private boolean validateDepartment(String text) throws SpitaliDbException{
        for (Departamenti d : departamentet){
            if (d.getEmri().equalsIgnoreCase(text)){
                throw new SpitaliDbException("Departamenti ekziston!");
            }
        }
        return true;
    }
    
    private HBox buildDepartmentsHBox(){
        HBox hbox = new HBox();
        hbox.setPrefHeight(100.0);
        hbox.setPrefHeight(200.0);
        
        hbox.setStyle("-fx-background-color: transparent;");
        listaHBox.getChildren().add(hbox);
        rreshtat.add(hbox);
        countRreshti++;
        hbox.setId("rreshti"+countRreshti);
        return hbox;
    }
    
    private void addVBox(HBox hbox, Departamenti departamenti) throws IOException{
        VBox departamentiVBo = FXMLLoader.load(getClass().getResource("/views/departamentiVBox.fxml"));
        
        for (Node n : departamentiVBo.getChildren()){
            try {
                if (n.getId().equals("emriDep")){
                    Text emriDepTxt = (Text)n;
                    emriDepTxt.setText(departamenti.getEmri() +" | " + departamenti.getPunetoriCollection().size());
                }
                else if (n.getId().equals("topHBox")){
                    HBox hb = (HBox) n;
                    Text dataEKrijimitTxt = (Text)hb.getChildren().get(0);
                    dataEKrijimitTxt.setFont(Font.font("Quicksand"));
                    dataEKrijimitTxt.setText(departamenti.getDataKrijimit().toString());
                    
                    HBox detailsHB = (HBox) hb.getChildren().get(1);
                    FontAwesomeIconView detailsBtn = (FontAwesomeIconView) detailsHB.getChildren().get(0);
                    detailsBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            showDetails(departamenti, event);
                        }
                    });
                 }
                else if (n.getId().equals("depIdHbox")){
                    HBox hb = (HBox) n;
                    hb.setId(String.valueOf(departamenti.getId()));
                    hb.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            showPunetoret(hb.getId());
                        }
                    });
                }
            } catch (NullPointerException e) {

            }
            
        }
        hbox.getChildren().add(departamentiVBo);
    }
    
    private void saveDepartment(Departamenti departamenti) throws IOException{
        if(listaHBox.getChildren().size() == 3){
            listaHBox = buildDepartmentsHBox();
            addVBox(listaHBox, departamenti);
            
            mainVBox.getChildren().add(listaHBox);
            scrollPane.setContent(mainVBox);
            return;
        }
        else{
            addVBox(listaHBox, departamenti);
        }
    }

    private void buildDepartamentetList() throws IOException{
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            departamentet = (List<Departamenti>) session.createNamedQuery("Departamenti.findAll").getResultList();
            session.getTransaction().commit();
        }
        for (Departamenti d : departamentet){
            saveDepartment(d);
        }
    }

    private Departamenti shtoDepartament(String emri) throws SpitaliDbException{
        Spitali spitaliId;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            spitaliId = session.get(Spitali.class, 1);
            session.getTransaction().commit();
        }
        
        Departamenti d = new Departamenti();
        d.setEmri(emri);
        d.setSpitaliId(spitaliId);
        d.setDataKrijimit(new Date());
        
        Departamenti saved = null;
        
        DepartamentiDAO departamentiRepository = new DepartamentiDAOImpl();
        try {
            Integer nextId = departamentiRepository.create(d);
            saved = departamentiRepository.get(nextId);
            departamentet.add(saved);
            PunetoriController.addDepartmentToComboBoxList(d);
        } catch (SpitaliDbException ex) {
            showAlertError(ex);
            return null;
        }
        return saved;
    }
    
    private void showPunetoret(String depId){
        Departamenti departamenti = null;
        for (Departamenti d : departamentet){
            if (d.getId().equals(Integer.parseInt(depId))){
                departamenti = d;
            }
        }
        ObservableList<Punetori> punetoret = FXCollections.observableArrayList();

        Collection<Punetori> punetoriCollection = departamenti.getPunetoriCollection();
        
        for (Punetori p : punetoriCollection){
            punetoret.add(p);
        }
        
        ListView<Punetori> punetoretList = new ListView<>();
        punetoretList.getStyleClass().add("punetoret-list");
//        punetoretList.setStyle("-fx-background-color: transparent; .list-cell: -fx-font-size: 15px;");
        punetoretList.setItems(punetoret);
        punetoretList.setPadding(new Insets(20, 0, 2, 0));
        
        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        
        Text headingText = new Text("Punetoret e departamentit: \"" +departamenti.getEmri() + "\"");
        
        dialogLayout.setHeading(headingText);
        if (punetoretList.getItems().isEmpty()){
            Label text = new Label("Ky departament nuk ka asnje punëtorë!");
            text.setPadding(new Insets(45, 5, 10, 5));
            dialogLayout.setBody(text);
        }
        else {
            dialogLayout.setBody(punetoretList);        
        }

        JFXDialog dialog = new JFXDialog(mainStackPane, dialogLayout, JFXDialog.DialogTransition.CENTER);
        
        JFXButton closeButton = new JFXButton("Mbyll");
        closeButton.setStyle("-fx-button-type: RAISED; -fx-background-color: #26C6DA; -fx-font-size: 17px;-fx-text-fill: WHITE;");
        
        closeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event1) {
                dialog.close();
            }
        });
        
        addEscapeKeyPressed(dialog);
        
        dialogLayout.setActions(closeButton);
        dialogLayout.setPrefWidth(600);
        dialog.show();
    }
    
    private void addEscapeKeyPressed(JFXDialog dialog){
        mainStackPane.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ESCAPE){
                    dialog.close();
                }
            }
        });
    }
    
    private void showDetails(Departamenti departamenti, MouseEvent event) {
        deleteItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                deleteDepartamenti(departamenti);
            }

        });
        
        editItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                editDepartamenti(departamenti);
            }
        });
        
        contextMenu.setAutoHide(true);
        if (!contextMenu.isShowing()){
            contextMenu.show(listaHBox, event.getScreenX(), event.getScreenY());        
        }
    }

    private void deleteDepartamenti(Departamenti departamenti) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Sistemi per kujdesin mjekësor - 2018");
        alert.setHeaderText("Fshij Departament");
        alert.setContentText("A jeni te sigurt se doni te fshini departamentin: "+departamenti.getEmri());
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("file:src/images/logo.png"));
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK){
            showLoadingImg();
            DepartamentiDAO departamentiDAO = new DepartamentiDAOImpl();
            try {
                departamentiDAO.delete(departamenti);
            } catch (SpitaliDbException ex) {
                showAlertError(ex);
                return;
            }
            mainVBox.getChildren().clear();
            listaHBox = buildDepartmentsHBox();
            departamentet.remove(departamenti);
            for (Departamenti d : departamentet){
                try {
                    saveDepartment(d);
                } catch (IOException ex) {}
            }
            PunetoriController.removeDepartmentFromComboBoxList(departamenti);
            showLoadingImg();
        }
    }
    
    private void editDepartamenti(Departamenti departamenti) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        
        fxmlLoader.setLocation(getClass().getResource("/views/editdialogs/departamentiedit.fxml"));
        JFXDialogLayout dialogLayout = null;
        try {
            dialogLayout = fxmlLoader.load();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        AnchorPane anchorPane = (AnchorPane)dialogLayout.getChildren().get(1);
        
        JFXButton ruajeButton = (JFXButton)anchorPane.getChildren().get(4);
        
        DepartamentiEditController controller = fxmlLoader.getController();
        controller.setDepartamenti(departamenti);
        controller.editDepartamenti();
        
        DepartamentiDAO departamentiRepository = new DepartamentiDAOImpl();
        
        JFXDialog dialog = new JFXDialog(mainStackPane, dialogLayout, JFXDialog.DialogTransition.CENTER);
        
        ruajeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!controller.getEmriFld().getText().equals(departamenti.getEmri())){
                    try {
                        validateDepartment(controller.getEmriFld().getText());
                    } catch (SpitaliDbException ex) {
                        controller.showErrorNameLabel(ex.getMessage());
                        return;
                    }
                }
                Departamenti updateDepartamenti = controller.updateDepartamenti();
                if (updateDepartamenti == null){
                    return;
                }
                showLoadingImg();
                try {
                    departamentiRepository.edit(updateDepartamenti);
                } catch (SpitaliDbException ex) {
                    showAlertError(ex);
                    return;
                }
                mainVBox.getChildren().clear();
                listaHBox = buildDepartmentsHBox();
                for (Departamenti d : departamentet){
                    try {
                        if (d.getId().equals(departamenti.getId())){
                            d = departamenti;
                        }
                        saveDepartment(d);
                    } catch (IOException ex) {}
                }
                PunetoriController.editDepartmentInComboBoxList(updateDepartamenti);
                showLoadingImg();
                dialog.close();
            }
        });
        
        anchorPane.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getSource() == KeyCode.ENTER){
                    ruajeButton.fire();
                }
            }
        });
        
        dialog.show();        
    }

    private void reArrangeList(Departamenti deleted) {
        System.out.println("******");
        VBox vboxi = (VBox) scrollPane.getContent();
        for (Node n : vboxi.getChildren()){
            System.out.println(n.getId());
            HBox hbox = (HBox) n;
            System.out.println("------------------------------");
            for (Node nd : hbox.getChildren()){
                System.out.println(nd.getParent());
                System.out.println(nd.getId());
                System.out.println("");
            }
            System.out.println("------------------------------");
        }
        System.out.println("******");
        System.out.println("count"+countRreshti);
        
        System.out.println("");
        System.out.println("");
        System.out.println("");
        for (HBox h : rreshtat){
            System.out.println("h.get+"+h.getId());
        }
    }
    
    private void reArrange(VBox departamentiVBo) {
        HBox parent = (HBox) departamentiVBo.getParent();
        
        int countAllVBox = 0;
        for (HBox h : rreshtat){
            countAllVBox += h.getChildren().size();
        }
        System.out.println(countAllVBox);
        
        int index = 0;
        int rreshtiIndex = -99;
        
        for (int i=0; i<parent.getChildren().size(); i++){
            if (parent.getChildren().get(i).equals(departamentiVBo)){
                System.out.println("ii= "+i);
                index = i;
                System.out.println("VBOX INDEX = "+parent.getId());
                
                rreshtiIndex = Integer.parseInt(parent.getId().substring(parent.getId().length()-1));
            }
        }
        
//        HBox rreshtiMomental = rreshtat.get(rreshtiIndex-1);
//        System.out.println("rreshtimomental = "+rreshtiMomental.getId());

        for (int rreshtCount=rreshtiIndex; rreshtCount<rreshtat.size(); rreshtCount++){
                HBox rreshtiMomental = rreshtat.get(rreshtCount-1);
                System.out.println("rreshtimomental = "+rreshtiMomental.getId());
                VBox get = null;
                HBox rreshti = rreshtat.get(rreshtCount);
                if (rreshti!=null){
                System.out.println("rreshtimomental = "+rreshti.getId());
                System.out.println("--------------------------------------------");
                    get = (VBox) rreshti.getChildren().get(0);
                    if (get != null){
                        rreshti.getChildren().remove(get);                    
                    }
                }
                if (rreshtCount==rreshtiIndex){
                    rreshtiMomental.getChildren().remove(rreshtiMomental.getChildren().get(index));                
                }
                rreshtiMomental.getChildren().add(get);
        }
        
//        HBox rreshti = rreshtat.get(rreshtiIndex);
//        System.out.println("rreshti = "+rreshti.getId());
//        System.out.println("");
//        VBox get = (VBox) rreshti.getChildren().get(0);
//        rreshti.getChildren().remove(get);
//        rreshtiMomental.getChildren().remove(rreshtiMomental.getChildren().get(index));
//        rreshtiMomental.getChildren().add(get);
        
//        System.out.println(departamentiVBo.getParent().getId());
    }
    
    private void showLoadingImg(){
        if (!loadingImg.isVisible()){
            loadingImg.setVisible(true);
            scrollPane.setOpacity(0.2);
        }
        else{
            loadingImg.setVisible(false);
            scrollPane.setOpacity(1);
        }
    }
    
    private void showAlertError(Exception exception){
        CustomAlert.showAlertError(exception, mainStackPane.getScene());
    }
}


