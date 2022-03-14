/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao;

import exceptions.SpitaliDbException;
import java.util.List;
import model.Profesioni;

/**
 *
 * @author Leutrim Osmani
 */
public interface ProfesioniDAO {
    void create(Profesioni p) throws SpitaliDbException;
    void edit(Profesioni p) throws SpitaliDbException;
    void delete(Profesioni p) throws SpitaliDbException;
    Profesioni get(String proId) throws SpitaliDbException;
    List<Profesioni> getProfesionetList() throws SpitaliDbException;
}
