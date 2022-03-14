/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao;

import exceptions.SpitaliDbException;
import java.util.List;
import model.Adresa;

/**
 *
 * @author Fisnik Zejnullahu
 */
public interface AdresaDAO {
    void create(Adresa a) throws SpitaliDbException;
    void edit(Adresa a) throws SpitaliDbException;
    void delete(Adresa a) throws SpitaliDbException;
    Adresa get(int adresaId) throws SpitaliDbException;
    List<Adresa> getAdresatList() throws SpitaliDbException;
}
