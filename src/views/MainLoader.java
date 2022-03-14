//C:\Windows\SysWOW64\SQLServerManager14.msc
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import java.util.Calendar;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.database.config.HibernateUtil;
import org.hibernate.SessionFactory;

/**
 *
 * @author Fisnik Zejnullahu
 */
public class MainLoader extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        Scene scene = new Scene(root);
                
        stage.setTitle("Sistemi per kujdesin mjekësor - " + Calendar.getInstance().get(Calendar.YEAR));
//        stage.widthProperty().addListener(new ChangeListener<Number>() {
//            @Override
//            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//                stage.setTitle("width="+stage.getWidth()+", height="+stage.getHeight());
//            }
//        });
        stage.getIcons().add(new Image("file:src/images/logo.png"));
        stage.centerOnScreen();
        stage.requestFocus();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void stop(){
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        if (sessionFactory != null){
            sessionFactory.close();
        }
        System.exit(0);
    }
    
}
