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
@Table(name = "GrupiGjakut")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GrupiGjakut.findAll", query = "SELECT g FROM GrupiGjakut g")
    , @NamedQuery(name = "GrupiGjakut.findById", query = "SELECT g FROM GrupiGjakut g WHERE g.id = :id")
    , @NamedQuery(name = "GrupiGjakut.findByTipi", query = "SELECT g FROM GrupiGjakut g WHERE g.tipi = :tipi")})
public class GrupiGjakut implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "tipi")
    private String tipi;
    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "grupiGjakutId")
    private Collection<Pacienti> pacientiCollection;

    public GrupiGjakut() {
    }

    public GrupiGjakut(Integer id) {
        this.id = id;
    }

    public GrupiGjakut(Integer id, String tipi) {
        this.id = id;
        this.tipi = tipi;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTipi() {
        return tipi;
    }

    public void setTipi(String tipi) {
        this.tipi = tipi;
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
        if (!(object instanceof GrupiGjakut)) {
            return false;
        }
        GrupiGjakut other = (GrupiGjakut) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.GrupiGjakut[ id=" + id + " ]";
    }
    
}
