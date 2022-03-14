/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.tablemodels;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Fisnik Zejnullahu
 */
public class PunetoriTableModel {

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final LongProperty nrPersonal = new SimpleLongProperty();
    private final StringProperty emri = new SimpleStringProperty();
    private final StringProperty emriBabes = new SimpleStringProperty();
    private final StringProperty mbiemri = new SimpleStringProperty();
    private final StringProperty nrTel = new SimpleStringProperty();
    private final StringProperty adresa = new SimpleStringProperty();
    private final StringProperty gjinia = new SimpleStringProperty();
    private final StringProperty email = new SimpleStringProperty();
    private final StringProperty datelindja = new SimpleStringProperty();
    private final StringProperty departamenti = new SimpleStringProperty();
    private final StringProperty dataFillimit = new SimpleStringProperty();
    private final StringProperty dataMbarimit = new SimpleStringProperty();
    private byte[] foto;

    public PunetoriTableModel(){
        
    }
    
    public PunetoriTableModel(int id, long nrPersonal, String emri, String emriBabes, String mbiemri,
                            String nrTel, String adresa, String gjinia, String email,
                            String datelindja, String dep, String dataFillimit, String dataMbarimit, byte[] foto) {
        this.id.set(id);
        this.nrPersonal.set(nrPersonal);
        this.emri.set(emri);
        this.emriBabes.set(emriBabes);
        this.mbiemri.set(mbiemri);
        this.nrTel.set(nrTel);
        this.adresa.set(adresa);
        this.gjinia.set(gjinia);
        this.email.set(email);
        this.datelindja.set(datelindja);
        this.departamenti.set(dep);
        this.dataFillimit.set(dataFillimit);
        this.dataMbarimit.set(dataMbarimit);
        this.foto = foto;
    }

    public int getId() {
        return id.get();
    }

    public void setId(int value) {
        id.set(value);
    }

    public IntegerProperty idProperty() {
        return id;
    }
    public long getNrPersonal() {
        return nrPersonal.get();
    }

    public void setNrPersonal(long value) {
        nrPersonal.set(value);
    }

    public LongProperty nrPersonalProperty() {
        return nrPersonal;
    }

    public String getEmri() {
        return emri.get();
    }

    public void setEmri(String value) {
        emri.set(value);
    }

    public StringProperty emriProperty() {
        return emri;
    }

    public String getEmriBabes() {
        return emriBabes.get();
    }

    public void setEmriBabes(String value) {
        emriBabes.set(value);
    }

    public StringProperty emriBabesProperty() {
        return emriBabes;
    }

    public String getMbiemri() {
        return mbiemri.get();
    }

    public void setMbiemri(String value) {
        mbiemri.set(value);
    }

    public StringProperty mbiemriProperty() {
        return mbiemri;
    }

    public String getNrTel() {
        return nrTel.get();
    }

    public void setNrTel(String value) {
        nrTel.set(value);
    }

    public StringProperty nrTelProperty() {
        return nrTel;
    }

    public String getAdresa() {
        return adresa.get();
    }

    public void setAdresa(String value) {
        adresa.set(value);
    }

    public StringProperty adresaProperty() {
        return adresa;
    }

    public String getGjinia() {
        return gjinia.get();
    }

    public void setGjinia(String value) {
        gjinia.set(value);
    }

    public StringProperty gjiniaProperty() {
        return gjinia;
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String value) {
        email.set(value);
    }

    public StringProperty emailProperty() {
        return email;
    }

    public String getDatelindja() {
        return datelindja.get();
    }

    public void setDatelindja(String value) {
        datelindja.set(value);
    }

    public StringProperty datelindjaProperty() {
        return datelindja;
    }

    public String getDepartamenti() {
        return departamenti.get();
    }

    public void setDepartamenti(String value) {
        departamenti.set(value);
    }

    public StringProperty departamentiProperty() {
        return departamenti;
    }

    public String getDataFillimit() {
        return dataFillimit.get();
    }

    public void setDataFillimit(String value) {
        dataFillimit.set(value);
    }

    public StringProperty dataFillimitProperty() {
        return dataFillimit;
    }

    public String getDataMbarimit() {
        return dataMbarimit.get();
    }

    public void setDataMbarimit(String value) {
        dataMbarimit.set(value);
    }

    public StringProperty dataMbarimitProperty() {
        return dataMbarimit;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }
    
    
}
