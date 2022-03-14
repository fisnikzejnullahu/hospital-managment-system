/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alerts;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import net.sf.jasperreports.engine.JRException;
import reports.ReportGenerator;

/**
 *
 * @author Fisnik Zejnullahu
 */
public class CompletedReportDialog {
    
    public static void showDialog(StackPane stackPane, ReportGenerator generator, String fileName){
        Text successTxt = new Text("Raporti u gjenerua me sukses!");
        successTxt.setFont(Font.font("Arial", FontWeight.BOLD, 17));
        successTxt.setFill(Color.valueOf("#55ba50"));
        
        JFXButton shikoBtn = new JFXButton("SHIKOJE");
        shikoBtn.setStyle("-fx-button-type: RAISED; -fx-background-color: #26C6DA; -fx-font-size: 17px;-fx-text-fill: WHITE;");
        JFXButton exportBtn = new JFXButton("EXPORTO NE PDF");
        exportBtn.setStyle("-fx-button-type: RAISED; -fx-background-color: #26A69A; -fx-font-size: 17px;-fx-text-fill: WHITE;");
        
        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        
        dialogLayout.setPrefWidth(350);
        dialogLayout.setPrefHeight(200);
                
        JFXDialog dialog = new JFXDialog(stackPane, dialogLayout, JFXDialog.DialogTransition.TOP);
        
        shikoBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event1) {
                generator.showReport();
            }
        });
        
        exportBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event1) {
                try {
                    File exported = generator.exportReportToPdf(fileName);
                    dialog.close();
                    Desktop.getDesktop().open(exported);
                } catch (JRException | SQLException | IOException ex) {
                    CustomAlert.showAlertError(ex, stackPane.getScene());
                }
            }
        });
        
        HBox hbox=new HBox();
        hbox.setSpacing(10);
        hbox.setPrefSize(200, 70);
        hbox.setAlignment(Pos.BOTTOM_CENTER);
        hbox.getChildren().addAll(shikoBtn, exportBtn);
        
        dialogLayout.setHeading(successTxt);
        dialogLayout.setActions(hbox);
        dialogLayout.setBody(hbox);
        
        dialog.show();
    }
}
