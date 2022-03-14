/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Fisnik Zejnullahu
 */
@Entity
@Table(name = "Departamenti")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Departamenti.findAll", query = "SELECT d FROM Departamenti d")
    , @NamedQuery(name = "Departamenti.findById", query = "SELECT d FROM Departamenti d WHERE d.id = :id")
    , @NamedQuery(name = "Departamenti.findByEmri", query = "SELECT d FROM Departamenti d WHERE d.emri = :emri")
    , @NamedQuery(name = "Departamenti.findByDataKrijimit", query = "SELECT d FROM Departamenti d WHERE d.dataKrijimit = :dataKrijimit")})
public class Departamenti implements Serializable {

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
    @Column(name = "dataKrijimit")
    @Temporal(TemporalType.DATE)
    private Date dataKrijimit;
    @JoinColumn(name = "spitaliId", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Spitali spitaliId;
    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "departamentiId", fetch = FetchType.EAGER)
    private Collection<Punetori> punetoriCollection;

    public Departamenti() {
    }

    public Departamenti(Integer id) {
        this.id = id;
    }

    public Departamenti(Integer id, String emri, Date dataKrijimit) {
        this.id = id;
        this.emri = emri;
        this.dataKrijimit = dataKrijimit;
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

    public Date getDataKrijimit() {
        return dataKrijimit;
    }

    public void setDataKrijimit(Date dataKrijimit) {
        this.dataKrijimit = dataKrijimit;
    }

    public Spitali getSpitaliId() {
        return spitaliId;
    }

    public void setSpitaliId(Spitali spitaliId) {
        this.spitaliId = spitaliId;
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
        if (!(object instanceof Departamenti)) {
            return false;
        }
        Departamenti other = (Departamenti) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Departamenti[ id=" + id + " ]";
    }
    
}
