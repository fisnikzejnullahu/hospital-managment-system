/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao.implementations;

import exceptions.SpitaliDbException;
import java.sql.SQLException;
import java.util.List;
import model.Adresa;
import model.dao.AdresaDAO;
import model.database.config.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;

/**
 *
 * @author Fisnik Zejnullahu
 */
public class AdresaDAOImpl implements AdresaDAO{

    @Override
    public void create(Adresa a) throws SpitaliDbException {
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            session.save(a);
            session.getTransaction().commit();
        }catch(HibernateException e){
            Throwable cause = e.getCause();
            if (cause instanceof ConstraintViolationException) {
                ConstraintViolationException constrExc = (ConstraintViolationException) cause;
                throw new SpitaliDbException("Adresa ekziston ne databaze!");
            }
            else if (cause instanceof SQLException){
                SQLException sqlExc = (SQLException)cause;
                throw new SpitaliDbException(sqlExc.getErrorCode(), sqlExc.getMessage());
            }
            throw new SpitaliDbException(e.getMessage());
        }
    }

    @Override
    public void edit(Adresa a) throws SpitaliDbException {
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            session.saveOrUpdate(a);
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
    
    public void delete(int adresaId) throws SpitaliDbException {
        Adresa a = get(adresaId);
        delete(a);
    }
    
    @Override
    public void delete(Adresa a) throws SpitaliDbException {
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            session.remove(a);
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
    public Adresa get(int adresaId) throws SpitaliDbException {
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            Adresa a = session.get(Adresa.class, adresaId);
            if (a == null){
                throw new SpitaliDbException("Adresa me ID-n: \"" + adresaId + "\", nuk u gjet ne databaze!");
            }
            session.getTransaction().commit();
            return a;
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
    public List<Adresa> getAdresatList() throws SpitaliDbException {
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            List<Adresa> resultList = session.createNamedQuery("Adresa.findAll").getResultList();
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
