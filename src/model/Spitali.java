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
@Table(name = "Spitali")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Spitali.findAll", query = "SELECT s FROM Spitali s")
    , @NamedQuery(name = "Spitali.findById", query = "SELECT s FROM Spitali s WHERE s.id = :id")
    , @NamedQuery(name = "Spitali.findByEmri", query = "SELECT s FROM Spitali s WHERE s.emri = :emri")})
public class Spitali implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "emri")
    private String emri;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "spitaliId")
    private Collection<Departamenti> departamentiCollection;
    @JoinColumn(name = "adresaId", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Adresa adresaId;

    public Spitali() {
    }

    public Spitali(Integer id) {
        this.id = id;
    }

    public Spitali(Integer id, String emri) {
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
    public Collection<Departamenti> getDepartamentiCollection() {
        return departamentiCollection;
    }

    public void setDepartamentiCollection(Collection<Departamenti> departamentiCollection) {
        this.departamentiCollection = departamentiCollection;
    }

    public Adresa getAdresaId() {
        return adresaId;
    }

    public void setAdresaId(Adresa adresaId) {
        this.adresaId = adresaId;
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
        if (!(object instanceof Spitali)) {
            return false;
        }
        Spitali other = (Spitali) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Spitali[ id=" + id + " ]";
    }
    
}
