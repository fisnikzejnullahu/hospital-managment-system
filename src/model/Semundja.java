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
@Table(name = "Semundja")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Semundja.findAll", query = "SELECT s FROM Semundja s")
    , @NamedQuery(name = "Semundja.findById", query = "SELECT s FROM Semundja s WHERE s.id = :id")
    , @NamedQuery(name = "Semundja.findByEmri", query = "SELECT s FROM Semundja s WHERE s.emri = :emri")
    , @NamedQuery(name = "Semundja.findByNiveliRrezikut", query = "SELECT s FROM Semundja s WHERE s.niveliRrezikut = :niveliRrezikut")})
public class Semundja implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "emri")
    private String emri;
    @Column(name = "niveliRrezikut")
    private Integer niveliRrezikut;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "semundja")
    private Collection<PacientiSemundja> pacientiSemundjaCollection;

    public Semundja() {
    }

    public Semundja(Integer id) {
        this.id = id;
    }

    public Semundja(Integer id, String emri) {
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

    public Integer getNiveliRrezikut() {
        return niveliRrezikut;
    }

    public void setNiveliRrezikut(Integer niveliRrezikut) {
        this.niveliRrezikut = niveliRrezikut;
    }

    @XmlTransient
    public Collection<PacientiSemundja> getPacientiSemundjaCollection() {
        return pacientiSemundjaCollection;
    }

    public void setPacientiSemundjaCollection(Collection<PacientiSemundja> pacientiSemundjaCollection) {
        this.pacientiSemundjaCollection = pacientiSemundjaCollection;
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
        if (!(object instanceof Semundja)) {
            return false;
        }
        Semundja other = (Semundja) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Semundja[ id=" + id + " ]";
    }
    
}
