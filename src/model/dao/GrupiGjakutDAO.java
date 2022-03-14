/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao;

import exceptions.SpitaliDbException;
import java.util.List;
import model.GrupiGjakut;

/**
 *
 * @author Leutrim Osmani
 */
public interface GrupiGjakutDAO {
    void create(GrupiGjakut p) throws SpitaliDbException;
    void edit(GrupiGjakut p) throws SpitaliDbException;
    void delete(GrupiGjakut p) throws SpitaliDbException;
    GrupiGjakut get(String gjakuId) throws SpitaliDbException;
    List<GrupiGjakut> getGrupetGjakutList() throws SpitaliDbException;
}
