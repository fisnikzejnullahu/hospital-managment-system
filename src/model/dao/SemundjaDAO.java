/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao;

import exceptions.SpitaliDbException;
import java.util.List;
import model.Semundja;

/**
 *
 * 
 * @author Fisnik Zejnullahu
 */
public interface SemundjaDAO {
    void create(Semundja s) throws SpitaliDbException;
    void edit(Semundja s) throws SpitaliDbException;
    void delete(Semundja s) throws SpitaliDbException;
    void delete(int semundjaId) throws SpitaliDbException;
    Semundja get(int semundjaId) throws SpitaliDbException;
    List<Semundja> getSemundjetList() throws SpitaliDbException;
}
