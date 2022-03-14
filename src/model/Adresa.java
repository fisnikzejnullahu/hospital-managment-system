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
@Table(name = "Adresa")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Adresa.findAll", query = "SELECT a FROM Adresa a")
    , @NamedQuery(name = "Adresa.findById", query = "SELECT a FROM Adresa a WHERE a.id = :id")
    , @NamedQuery(name = "Adresa.findByRruga", query = "SELECT a FROM Adresa a WHERE a.rruga = :rruga")})
public class Adresa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "rruga")
    private String rruga;
    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "adresaId")
    private Collection<Pacienti> pacientiCollection;
    @JoinColumn(name = "qytetiId", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Qyteti qytetiId;
    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "adresaId")
    private Collection<Spitali> spitaliCollection;
    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "adresaId")
    private Collection<Punetori> punetoriCollection;

    public Adresa() {
    }

    public Adresa(Integer id) {
        this.id = id;
    }

    public Adresa(Integer id, String rruga) {
        this.id = id;
        this.rruga = rruga;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRruga() {
        return rruga;
    }

    public void setRruga(String rruga) {
        this.rruga = rruga;
    }

    @XmlTransient
    public Collection<Pacienti> getPacientiCollection() {
        return pacientiCollection;
    }

    public void setPacientiCollection(Collection<Pacienti> pacientiCollection) {
        this.pacientiCollection = pacientiCollection;
    }

    public Qyteti getQytetiId() {
        return qytetiId;
    }

    public void setQytetiId(Qyteti qytetiId) {
        this.qytetiId = qytetiId;
    }

    @XmlTransient
    public Collection<Spitali> getSpitaliCollection() {
        return spitaliCollection;
    }

    public void setSpitaliCollection(Collection<Spitali> spitaliCollection) {
        this.spitaliCollection = spitaliCollection;
    }

    @XmlTransient
    public Collection<Punetori> getPunetoriCollection() {
        return punetoriCollection;
    }

    public void setPunetoriCollection(Collection<Punetori> punetoriCollection) {
        this.punetoriCollection = punetoriCollection;
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
        if (!(object instanceof Adresa)) {
            return false;
        }
        Adresa other = (Adresa) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Adresa[ id=" + id + " ]";
    }
    
}
