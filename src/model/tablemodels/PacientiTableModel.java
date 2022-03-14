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
 * @author Leutrim Osmani
 */
public class PacientiTableModel {

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
    private final StringProperty grupiGjakut = new SimpleStringProperty();
    private final StringProperty profesioni = new SimpleStringProperty();
    

    public PacientiTableModel(){
        
    }
    
    public PacientiTableModel(int id, long nrPersonal, String emriMbiemri, String emriBabes, 
                            String nrTel, String adresa, String gjinia, String email,
                            String datelindja, String grupiGjakut, String profesioni) {
        this.id.set(id);
        this.nrPersonal.set(nrPersonal);
        this.emri.set(emriMbiemri);
        this.emriBabes.set(emriBabes);
        this.nrTel.set(nrTel);
        this.adresa.set(adresa);
        this.gjinia.set(gjinia);
        this.email.set(email);
        this.datelindja.set(datelindja);
        this.grupiGjakut.set(grupiGjakut);
        this.profesioni.set(profesioni);
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

    public String getGrupiGjakut() {
        return grupiGjakut.get();
    }

    public void setGrupiGjakut(String value) {
        grupiGjakut.set(value);
    }

    public StringProperty grupiGjakutProperty() {
        return grupiGjakut;
    }

    public String getProfesioni() {
        return profesioni.get();
    }

    public void setProfesioni(String value) {
        profesioni.set(value);
    }

    public StringProperty profesioniProperty() {
        return profesioni;
    }
}
