/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao;

import exceptions.SpitaliDbException;
import java.util.List;
import model.Raporti;

/**
 *
 * @author Fisnik Zejnullahu
 */
public interface RaportiDAO {
    void create(Raporti r) throws SpitaliDbException;
    void edit(Raporti r) throws SpitaliDbException;
    void delete(int raportiId) throws SpitaliDbException;
    void delete(Raporti r) throws SpitaliDbException;
    Raporti get(int raportiId) throws SpitaliDbException;
    List<Raporti> getRaportetList() throws SpitaliDbException;
}
