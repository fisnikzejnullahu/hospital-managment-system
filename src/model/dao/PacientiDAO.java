/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao;

import exceptions.SpitaliDbException;
import java.util.List;
import model.Pacienti;

/**
 *
 * @author Leutrim Osmani
 */
public interface PacientiDAO {
    void create(Pacienti p) throws SpitaliDbException;
    void edit(Pacienti p) throws SpitaliDbException;
    void delete(Pacienti p) throws SpitaliDbException;
    void delete(int pacientiId, boolean deleteSemundjet) throws SpitaliDbException;
    Pacienti get(int pacientiId) throws SpitaliDbException;
    List<Pacienti> getPacientetList() throws SpitaliDbException;
}
