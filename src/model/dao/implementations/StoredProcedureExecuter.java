/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao.implementations;

import exceptions.SpitaliDbException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import model.database.config.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

/**
 *
 * @author Fisnik Zejnullahu
 */
public class StoredProcedureExecuter {
    
    private CallableStatement ps;
    
    public CallableStatement executeGetSemundjet(LocalDate startDate, LocalDate endDate, int pacientiID) throws SpitaliDbException{
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        
        session.beginTransaction();
        
        try {
            session.doWork(new Work() {
                @Override
                public void execute(Connection connection) throws SQLException {
                    try {
                        ps = connection.prepareCall("{call getSemundjet (?,?,?)}");
                        ps.setInt(1, pacientiID);
                        if (startDate == null){
                            ps.setDate(2, null);
                            ps.setDate(3, null);
                        }
                        else{
                            ps.setString(2, startDate.toString());
                            ps.setString(3, endDate.toString());                        
                        }
                    }catch(SQLException e){
                        throw e;
                    }
                    finally {
                        session.getTransaction().commit();
                    }
                }
            });
            
        }
        catch(HibernateException ex){
            ex.printStackTrace();
            System.out.println("---------");
            System.out.println(ex.getCause());
            System.out.println("---------");
            throw new SpitaliDbException(ex.getMessage());
        }
        return ps;
        
    }
}
