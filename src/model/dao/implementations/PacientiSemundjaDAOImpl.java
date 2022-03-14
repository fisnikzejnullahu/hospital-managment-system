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
import model.PacientiSemundja;
import model.Punetori;
import model.Semundja;
import model.dao.PacientiSemundjaDAO;
import model.dao.SemundjaDAO;
import model.database.config.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.NativeQuery;

/**
 *
 * @author Fisnik Zejnullahu
 */
public class PacientiSemundjaDAOImpl implements PacientiSemundjaDAO{

    @Override
    public void create(PacientiSemundja ps) throws SpitaliDbException {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.save(ps);
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
    public void edit(PacientiSemundja ps) throws SpitaliDbException {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.update(ps);
        session.getTransaction().commit();
    }
    
    @Override
    public void delete(int semundjaId, int pacientiId) throws SpitaliDbException {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.createNativeQuery("delete from Pacienti_Semundja where semundjaId="+semundjaId + " and pacientiId="+pacientiId).executeUpdate();
        session.getTransaction().commit();
    }
    
    @Override
    public void delete(PacientiSemundja s) throws SpitaliDbException {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.remove(s);
        session.getTransaction().commit();
    }
    
    @Override
    public PacientiSemundja get(int semundjaId, int pacientiId) throws SpitaliDbException {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        NativeQuery<PacientiSemundja> query = session.createNativeQuery("select ps from Pacienti_Semundja ps where semundjaId="+semundjaId + " and pacientiId="+pacientiId, PacientiSemundja.class);
        PacientiSemundja ps = query.getSingleResult();
        session.getTransaction().commit();
        return ps;
    }

    @Override
    public List<PacientiSemundja> getSemundjetList() throws SpitaliDbException {
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            List<PacientiSemundja> resultList = session.createNamedQuery("PacientiSemundja.findAll").getResultList();
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

    @Override
    public void deleteAllSemundjet(int pacientiId) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        
        NativeQuery nativeQuery = session.createNativeQuery("delete from Pacienti_Semundja where pacientiId="+pacientiId);
        nativeQuery.executeUpdate();
        session.getTransaction().commit();
    }
    
}
