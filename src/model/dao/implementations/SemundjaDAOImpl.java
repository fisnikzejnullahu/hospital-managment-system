/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao.implementations;

import exceptions.SpitaliDbException;
import java.sql.SQLException;
import java.util.List;
import javax.persistence.PersistenceException;
import model.Punetori;
import model.Semundja;
import model.dao.SemundjaDAO;
import model.database.config.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;

/**
 *
 * @author Fisnik Zejnullahu
 */
public class SemundjaDAOImpl implements SemundjaDAO{

    @Override
    public void create(Semundja s) throws SpitaliDbException {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.save(s);
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
    public void edit(Semundja s) throws SpitaliDbException {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.update(s);
        session.getTransaction().commit();
    }
    
    public void delete(int semundjaId) throws SpitaliDbException {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Semundja s = session.get(Semundja.class, semundjaId);
        session.getTransaction().commit();
        delete(s);
    }
    
    @Override
    public void delete(Semundja s) throws SpitaliDbException {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.remove(s);
        session.getTransaction().commit();
    }
    
    public Semundja get(int semundjaId) throws SpitaliDbException {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Semundja s = session.get(Semundja.class, semundjaId);
        session.getTransaction().commit();
        return s;
    }

    @Override
    public List<Semundja> getSemundjetList() throws SpitaliDbException {
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            List<Semundja> resultList = session.createNamedQuery("Semundja.findAll").getResultList();
            session.getTransaction().commit();
            return resultList;
        }
        catch(HibernateException e){
            Throwable cause = e.getCause();
            if (cause instanceof SQLException){
                SQLException sqlExc = (SQLException)cause;
                throw new SpitaliDbException(sqlExc.getErrorCode(), sqlExc.getMessage());
            }
            throw new SpitaliDbException(e.getMessage());
        }
    }
    
}
