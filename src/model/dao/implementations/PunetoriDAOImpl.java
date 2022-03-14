/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao.implementations;

import exceptions.SpitaliDbException;
import java.sql.SQLException;
import java.util.List;
import model.Punetori;
import model.dao.AdresaDAO;
import model.dao.DepartamentiDAO;
import model.dao.PunetoriDAO;
import model.database.config.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;

/**
 *
 * @author Fisnik Zejnullahu
 */
public class PunetoriDAOImpl implements PunetoriDAO{

    @Override
    public void create(Punetori p) throws SpitaliDbException {
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            session.save(p);
            session.getTransaction().commit();
        }catch(HibernateException pe){
            Throwable cause = pe.getCause();
            if (cause instanceof ConstraintViolationException) {
                throw (ConstraintViolationException) cause;
            }
            else if (cause instanceof SQLException){
                SQLException sqlExc = (SQLException)cause;
                throw new SpitaliDbException(sqlExc.getErrorCode(), sqlExc.getMessage());
            }
            throw new SpitaliDbException(pe.getMessage());
        }
    }

    @Override
    public void edit(Punetori p) throws SpitaliDbException {
        
        try {
            AdresaDAO adresaDAO = new AdresaDAOImpl();
            adresaDAO.edit(p.getAdresaId());

            DepartamentiDAO departamentiDAO = new DepartamentiDAOImpl();
            departamentiDAO.edit(p.getDepartamentiId());

            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            session.update(p);
            session.getTransaction().commit();
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
    public void delete(int punetoriId) throws SpitaliDbException {
        Punetori p = get(punetoriId);
        delete(p);
    }
    
    @Override
    public void delete(Punetori p) throws SpitaliDbException {
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            session.remove(p);
            session.getTransaction().commit();
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
    public Punetori get(int punetoriId) throws SpitaliDbException {
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            Punetori p = session.get(Punetori.class, punetoriId);
            session.getTransaction().commit();
            return p;
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
    public List<Punetori> getPunetoretList() throws SpitaliDbException {
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            List<Punetori> resultList = session.createNamedQuery("Punetori.findAll").getResultList();
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
