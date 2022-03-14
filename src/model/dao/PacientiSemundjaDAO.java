/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao;

import exceptions.SpitaliDbException;
import java.util.List;
import model.PacientiSemundja;

/**
 *
 * @author Fisnik Zejnullahu
 */
public interface PacientiSemundjaDAO {
    void create(PacientiSemundja ps) throws SpitaliDbException;
    void edit(PacientiSemundja ps) throws SpitaliDbException;
    void delete(PacientiSemundja ps) throws SpitaliDbException;
    void delete(int semundjaId, int pacientiId) throws SpitaliDbException;
    PacientiSemundja get(int semundjaId, int pacientiId) throws SpitaliDbException;
    List<PacientiSemundja> getSemundjetList() throws SpitaliDbException;
    void deleteAllSemundjet(int pacientiId);
}
