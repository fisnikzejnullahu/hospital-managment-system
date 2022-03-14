/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao.implementations;

import exceptions.SpitaliDbException;
import java.sql.SQLException;
import java.util.List;
import model.Raporti;
import model.dao.RaportiDAO;
import model.database.config.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;

/**
 *
 * @author Fisnik Zejnullahu
 */
public class RaportiDAOImpl implements RaportiDAO{

    @Override
    public void create(Raporti r) throws SpitaliDbException {
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            session.save(r);
            session.getTransaction().commit();
        }catch(HibernateException e){
            Throwable cause = e.getCause();
            if (cause instanceof ConstraintViolationException) {
                throw new SpitaliDbException("Raporti ekziston ne databaze!");
            }
            else if (cause instanceof SQLException){
                SQLException sqlExc = (SQLException)cause;
                throw new SpitaliDbException(sqlExc.getErrorCode(), sqlExc.getMessage());
            }
            throw new SpitaliDbException(e.getMessage());
        }
    }

    @Override
    public void edit(Raporti r) throws SpitaliDbException {
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            session.saveOrUpdate(r);
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
    public void delete(int raportiId) throws SpitaliDbException {
        Raporti a = get(raportiId);
        delete(a);
    }
    
    @Override
    public void delete(Raporti r) throws SpitaliDbException {
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            session.remove(r);
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
    public Raporti get(int raportiId) throws SpitaliDbException {
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            Raporti r = session.get(Raporti.class, raportiId);
            if (r == null){
                throw new SpitaliDbException("Raporti me ID-n: \"" + raportiId + "\", nuk u gjet ne databaze!");
            }
            session.getTransaction().commit();
            return r;
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
    public List<Raporti> getRaportetList() throws SpitaliDbException {
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            List<Raporti> resultList = session.createNamedQuery("Raporti.findAll").getResultList();
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
