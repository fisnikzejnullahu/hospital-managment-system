/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao.implementations;

import exceptions.SpitaliDbException;
import java.util.List;
import javax.persistence.PersistenceException;
import model.Profesioni;
import model.dao.ProfesioniDAO;
import model.database.config.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;

/**
 *
 * @author Leutrim Osmani
 */
public class ProfesioniDAOImpl implements ProfesioniDAO{

    @Override
    public void create(Profesioni p) throws SpitaliDbException {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.save(p);
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
    public void edit(Profesioni p) throws SpitaliDbException {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.saveOrUpdate(p);
        session.getTransaction().commit();
    }

    @Override
    public void delete(Profesioni p) throws SpitaliDbException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Profesioni get(String proId) throws SpitaliDbException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Profesioni> getProfesionetList() throws SpitaliDbException {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List<Profesioni> resultList = session.createNamedQuery("Profesioni.findAll").getResultList();
        session.getTransaction().commit();
        
        return resultList;
    }
    
}
