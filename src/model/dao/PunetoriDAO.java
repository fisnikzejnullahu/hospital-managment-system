/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao;

import exceptions.SpitaliDbException;
import java.util.List;
import model.Punetori;

/**
 *
 * @author Fisnik Zejnullahu
 */
public interface PunetoriDAO {
    void create(Punetori p) throws SpitaliDbException;
    void edit(Punetori p) throws SpitaliDbException;
    void delete(Punetori p) throws SpitaliDbException;
    void delete(int punetoriId) throws SpitaliDbException;
    Punetori get(int punetoriId) throws SpitaliDbException;
    List<Punetori> getPunetoretList() throws SpitaliDbException;
}
