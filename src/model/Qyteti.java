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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "Qyteti")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Qyteti.findAll", query = "SELECT q FROM Qyteti q")
    , @NamedQuery(name = "Qyteti.findById", query = "SELECT q FROM Qyteti q WHERE q.id = :id")
    , @NamedQuery(name = "Qyteti.findByEmri", query = "SELECT q FROM Qyteti q WHERE q.emri = :emri")
    , @NamedQuery(name = "Qyteti.findByZip", query = "SELECT q FROM Qyteti q WHERE q.zip = :zip")})
public class Qyteti implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "emri")
    private String emri;
    @Basic(optional = false)
    @Column(name = "zip")
    private int zip;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "qytetiId")
    private Collection<Adresa> adresaCollection;
    @JoinColumn(name = "shtetiId", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Shteti shtetiId;

    public Qyteti() {
    }

    public Qyteti(Integer id) {
        this.id = id;
    }

    public Qyteti(Integer id, String emri, int zip) {
        this.id = id;
        this.emri = emri;
        this.zip = zip;
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

    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    @XmlTransient
    public Collection<Adresa> getAdresaCollection() {
        return adresaCollection;
    }

    public void setAdresaCollection(Collection<Adresa> adresaCollection) {
        this.adresaCollection = adresaCollection;
    }

    public Shteti getShtetiId() {
        return shtetiId;
    }

    public void setShtetiId(Shteti shtetiId) {
        this.shtetiId = shtetiId;
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
        if (!(object instanceof Qyteti)) {
            return false;
        }
        Qyteti other = (Qyteti) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Qyteti[ id=" + id + " ]";
    }
    
}
