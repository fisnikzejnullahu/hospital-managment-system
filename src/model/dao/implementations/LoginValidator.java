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
import model.Perdoruesi;
import model.database.config.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

/**
 *
 * @author Fisnik Zejnullahu
 */
public class LoginValidator {
    
    public Perdoruesi validate(String username) throws SpitaliDbException{
        
        try {
            Perdoruesi perdoruesi = null;
            Session session;
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            if (!HibernateUtil.success){
                if (HibernateUtil.databaseError){
                    throw new SpitaliDbException("Ndodhi nje problem dhe nuk mund te lidhemi me databaze! Ju lutem njoftoni ekipin e IT!");
                }
                else {
                    throw new SpitaliDbException("Ndodhi nje problem dhe nuk mund te vazhdohet! Ju lutem njoftoni ekipin e IT!");
                }
            }
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
//            Query<Perdoruesi> query = session.createQuery("select p from Perdoruesi p where p.username=:usernameFld");
            Query<Perdoruesi> query = session.createQuery("select p from Perdoruesi p where p.username=:usernameFld");
            query.setParameter("usernameFld", username);

            List<Perdoruesi> resultList = query.getResultList();
            if (!resultList.isEmpty()) {
                perdoruesi = query.getResultList().get(0);
            }
            
            session.getTransaction().commit();
            return perdoruesi;
        }catch(PersistenceException e){
            Throwable cause = e.getCause();
                if (cause instanceof SQLException){
                    SQLException sqlExc = (SQLException)cause;
                    throw new SpitaliDbException(sqlExc.getErrorCode(), sqlExc.getMessage());
            }
            throw new SpitaliDbException(e.getMessage());
        }
    }
}
