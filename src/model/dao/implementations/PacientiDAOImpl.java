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
import model.Pacienti;
import model.Punetori;
import model.dao.AdresaDAO;
import model.dao.GrupiGjakutDAO;
import model.dao.PacientiDAO;
import model.dao.PacientiSemundjaDAO;
import model.dao.ProfesioniDAO;
import model.database.config.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;

/**
 *
 * @author Leutrim Osmani
 */
public class PacientiDAOImpl implements PacientiDAO{

    @Override
    public void create(Pacienti p) throws SpitaliDbException {
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
    public void edit(Pacienti p) throws SpitaliDbException {
        AdresaDAO adresaDAO = new AdresaDAOImpl();
        adresaDAO.edit(p.getAdresaId());

        ProfesioniDAO profesioniDAO = new ProfesioniDAOImpl();
        profesioniDAO.edit(p.getProfesioniId());
        
        GrupiGjakutDAO grupiGjakutDAO = new GrupiGjakutDAOImpl();
        grupiGjakutDAO.edit(p.getGrupiGjakutId());
        
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.update(p);
        session.getTransaction().commit();
    }
    
    public void delete(int pacientiId, boolean deleteSemundjet) throws SpitaliDbException {
        if (deleteSemundjet){
            PacientiSemundjaDAO psDAO = new PacientiSemundjaDAOImpl();
            psDAO.deleteAllSemundjet(pacientiId);
        }
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Pacienti p = session.get(Pacienti.class, pacientiId);
        session.getTransaction().commit();
        delete(p);
    }
    
    @Override
    public void delete(Pacienti p) throws SpitaliDbException {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.remove(p);
        session.getTransaction().commit();
    }
    
    public Pacienti get(int pacientiId) throws SpitaliDbException {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Pacienti p = session.get(Pacienti.class, pacientiId);
        session.getTransaction().commit();
        return p;
    }

    @Override
    public List<Pacienti> getPacientetList() throws SpitaliDbException {
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            List<Pacienti> resultList = session.createNamedQuery("Pacienti.findAll").getResultList();
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
