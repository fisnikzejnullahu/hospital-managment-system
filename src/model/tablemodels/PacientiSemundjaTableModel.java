/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.tablemodels;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Leutrim Osmani
 */
public class PacientiSemundjaTableModel {
    private final StringProperty emri = new SimpleStringProperty();
    private final StringProperty niveliRrezikut = new SimpleStringProperty();
    private final StringProperty dataSemundjes = new SimpleStringProperty();
    private int idSemundjes;
    

    public PacientiSemundjaTableModel(){
        
    }
    
    public PacientiSemundjaTableModel(String emri, String niveliRrezikut, String dataSemundjes, int idSemundjes) {
        this.emri.set(emri);
        this.dataSemundjes.set(dataSemundjes);
        this.niveliRrezikut.set(niveliRrezikut);
        this.idSemundjes = idSemundjes;
    }
    
    public PacientiSemundjaTableModel(String emri, String niveliRrezikut, int idSemundjes) {
        this.emri.set(emri);
        this.niveliRrezikut.set(niveliRrezikut);
        this.idSemundjes = idSemundjes;
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
    
    public String getNiveliRrezikut() {
        return niveliRrezikut.get();
    }

    public void setNiveliRrezikut(String value) {
        niveliRrezikut.set(value);
    }

    public StringProperty niveliRrezikutProperty() {
        return niveliRrezikut;
    }
    
    public String getDataSemundjes() {
        return dataSemundjes.get();
    }

    public void setDataSemundjes(String value) {
        dataSemundjes.set(value);
    }

    public StringProperty dataSemundjesProperty() {
        return dataSemundjes;
    }

    public int getIdSemundjes() {
        return idSemundjes;
    }

    public void setIdSemundjes(int idSemundjes) {
        this.idSemundjes = idSemundjes;
    }
    
    
}
