/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.tablemodels;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Fisnik Zejnullahu
 */
public class RaportiTableModel {
    private final IntegerProperty raportiId = new SimpleIntegerProperty();
    private final StringProperty dataLeshimit = new SimpleStringProperty();
    private final StringProperty llojiRaportit = new SimpleStringProperty();
    private final StringProperty punetori = new SimpleStringProperty();
    
    public RaportiTableModel(int raportiId, String dataLeshimit, String llojiRaportit, String punetoriId) {
        this.raportiId.set(raportiId);
        this.dataLeshimit.set(dataLeshimit);
        this.llojiRaportit.set(llojiRaportit);
        this.punetori.set(punetoriId);
    }
    
    public int getRaportiId() {
        return raportiId.get();
    }

    public void setRaportiId(int value) {
        raportiId.set(value);
    }

    public IntegerProperty idProperty() {
        return raportiId;
    }
    
    public String getDataLeshimit() {
        return dataLeshimit.get();
    }

    public void setDataLeshimit(String value) {
        dataLeshimit.set(value);
    }

    public StringProperty dataLeshimitProperty() {
        return dataLeshimit;
    }
    
    public String getLlojiRaportit() {
        return llojiRaportit.get();
    }

    public void setLlojiRaportit(String value) {
        llojiRaportit.set(value);
    }

    public StringProperty llojiRaportitProperty() {
        return llojiRaportit;
    }

    public String getPunetori() {
        return punetori.get();
    }
    
    public void setPunetori(String val) {
        punetori.set(val);
    }
    
    public StringProperty punetoriProperty() {
        return punetori;
    }
    
    
}
