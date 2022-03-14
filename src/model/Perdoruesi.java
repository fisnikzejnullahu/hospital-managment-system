/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import model.Punetori;

/**
 *
 * @author Fisnik Zejnullahu
 */
@Entity
@Table(name = "Perdoruesi")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Perdoruesi.findAll", query = "SELECT p FROM Perdoruesi p")
    , @NamedQuery(name = "Perdoruesi.findById", query = "SELECT p FROM Perdoruesi p WHERE p.id = :id")
    , @NamedQuery(name = "Perdoruesi.findByUsername", query = "SELECT p FROM Perdoruesi p WHERE p.username = :username")
    , @NamedQuery(name = "Perdoruesi.findByPassword", query = "SELECT p FROM Perdoruesi p WHERE p.password = :password")
    , @NamedQuery(name = "Perdoruesi.findByIsAdmin", query = "SELECT p FROM Perdoruesi p WHERE p.isAdmin = :isAdmin")})
public class Perdoruesi implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "username")
    private String username;
    @Basic(optional = false)
    @Column(name = "password")
    private String password;
    @Basic(optional = false)
    @Column(name = "isAdmin")
    private boolean isAdmin;
    @JoinColumn(name = "punetoriId", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Punetori punetoriId;

    public Perdoruesi() {
    }

    public Perdoruesi(Integer id) {
        this.id = id;
    }

    public Perdoruesi(Integer id, String username, String password, boolean isAdmin) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public Punetori getPunetoriId() {
        return punetoriId;
    }

    public void setPunetoriId(Punetori punetoriId) {
        this.punetoriId = punetoriId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Perdoruesi)) {
            return false;
        }
        Perdoruesi other = (Perdoruesi) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "controllers.Perdoruesi[ id=" + id + " ]";
    }
    
}
