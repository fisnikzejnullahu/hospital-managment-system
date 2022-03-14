/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Fisnik Zejnullahu
 */
@Entity
@Table(name = "Profesioni")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Profesioni.findAll", query = "SELECT p FROM Profesioni p")
    , @NamedQuery(name = "Profesioni.findById", query = "SELECT p FROM Profesioni p WHERE p.id = :id")
    , @NamedQuery(name = "Profesioni.findByEmri", query = "SELECT p FROM Profesioni p WHERE p.emri = :emri")})
public class Profesioni implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "emri")
    private String emri;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "profesioniId")
    private Collection<Pacienti> pacientiCollection;

    public Profesioni() {
    }

    public Profesioni(Integer id) {
        this.id = id;
    }

    public Profesioni(Integer id, String emri) {
        this.id = id;
        this.emri = emri;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmri() {
        return emri;
    }

    public void setEmri(String emri) {
        this.emri = emri;
    }

    @XmlTransient
    public Collection<Pacienti> getPacientiCollection() {
        return pacientiCollection;
    }

    public void setPacientiCollection(Collection<Pacienti> pacientiCollection) {
        this.pacientiCollection = pacientiCollection;
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
        if (!(object instanceof Profesioni)) {
            return false;
        }
        Profesioni other = (Profesioni) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Profesioni[ id=" + id + " ]";
    }
    
}
