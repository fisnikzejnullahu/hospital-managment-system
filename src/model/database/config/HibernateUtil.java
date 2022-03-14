/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.database.config;

import model.Adresa;
import model.Departamenti;
import model.GrupiGjakut;
import model.Pacienti;
import model.PacientiSemundja;
import model.PacientiSemundjaPK;
import model.Perdoruesi;
import model.Profesioni;
import model.Punetori;
import model.Qyteti;
import model.Raporti;
import model.Semundja;
import model.Shteti;
import model.Spitali;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Hibernate Utility class with a convenient method to get Session Factory
 * object.
 *
 * @author Fisnik Zejnullahu
 */
public class HibernateUtil {

    private static SessionFactory SESSION_FACTORY;
    public static boolean databaseError = false;
    public static boolean success = false;
    private static Configuration configuration;

    static {
        try {
            // Create the SessionFactory from standard (hibernate.cfg.xml) 
            // config file.
            configuration = new Configuration()
                    .configure("model/database/config/hibernate-cfg.xml")
                    .addAnnotatedClass(Shteti.class)
                    .addAnnotatedClass(Qyteti.class)
                    .addAnnotatedClass(Adresa.class)
                    .addAnnotatedClass(Spitali.class)
                    .addAnnotatedClass(Departamenti.class)
                    .addAnnotatedClass(Punetori.class)
                    .addAnnotatedClass(Pacienti.class)
                    .addAnnotatedClass(Profesioni.class)
                    .addAnnotatedClass(GrupiGjakut.class)
                    .addAnnotatedClass(Perdoruesi.class)
                    .addAnnotatedClass(Raporti.class)
                    .addAnnotatedClass(Semundja.class)
                    .addAnnotatedClass(PacientiSemundja.class)
                    .addAnnotatedClass(PacientiSemundjaPK.class);
        } catch (HibernateException ex) {
            // Log the exception. 
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
    
    public static SessionFactory getSessionFactory() {
        if (!success){
            try {
                SESSION_FACTORY = configuration.buildSessionFactory();
                success = true;
            }catch(Exception e){
                if (e.getCause() instanceof org.hibernate.exception.JDBCConnectionException){
                    databaseError = true;
                }
                else {
                    databaseError = false;
                }
                return null;            
            }
        }
        return SESSION_FACTORY;
    }
}
