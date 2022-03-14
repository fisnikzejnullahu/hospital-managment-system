/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao;

import exceptions.SpitaliDbException;
import java.util.List;
import model.Departamenti;

/**
 *
 * @author Fisnik Zejnullahu
 */
public interface DepartamentiDAO {
    Integer create(Departamenti d) throws SpitaliDbException;
    void edit(Departamenti d) throws SpitaliDbException;
    void delete(Departamenti d) throws SpitaliDbException;
    Departamenti get(int depId) throws SpitaliDbException;
    List<Departamenti> getDepartamentetList() throws SpitaliDbException;
}
