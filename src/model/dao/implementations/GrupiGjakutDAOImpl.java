/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao.implementations;

import exceptions.SpitaliDbException;
import java.util.List;
import javax.persistence.PersistenceException;
import model.GrupiGjakut;
import model.dao.GrupiGjakutDAO;
import model.database.config.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;

/**
 *
 * @author Leutrim Osmani
 */
public class GrupiGjakutDAOImpl implements GrupiGjakutDAO{

    @Override
    public void create(GrupiGjakut g) throws SpitaliDbException {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.save(g);
        try {
            session.getTransaction().commit();
        }catch(PersistenceException pe){
            Throwable t = pe.getCause();
            if (t instanceof ConstraintViolationException) {
                throw (ConstraintViolationException) t;
            }
        }
    }

    @Override
    public void edit(GrupiGjakut g) throws SpitaliDbException {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.saveOrUpdate(g);
        session.getTransaction().commit();
    }

    @Override
    public void delete(GrupiGjakut p) throws SpitaliDbException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public GrupiGjakut get(String proId) throws SpitaliDbException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<GrupiGjakut> getGrupetGjakutList() throws SpitaliDbException {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List<GrupiGjakut> resultList = session.createNamedQuery("GrupiGjakut.findAll").getResultList();
        session.getTransaction().commit();
        
        return resultList;
    }
    
}
