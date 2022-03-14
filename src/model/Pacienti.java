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
@Table(name = "Pacienti")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pacienti.findAll", query = "SELECT p FROM Pacienti p")
    , @NamedQuery(name = "Pacienti.findById", query = "SELECT p FROM Pacienti p WHERE p.id = :id")
    , @NamedQuery(name = "Pacienti.findByNrPersonal", query = "SELECT p FROM Pacienti p WHERE p.nrPersonal = :nrPersonal")
    , @NamedQuery(name = "Pacienti.findByEmri", query = "SELECT p FROM Pacienti p WHERE p.emri = :emri")
    , @NamedQuery(name = "Pacienti.findByEmriBabes", query = "SELECT p FROM Pacienti p WHERE p.emriBabes = :emriBabes")
    , @NamedQuery(name = "Pacienti.findByMbiemri", query = "SELECT p FROM Pacienti p WHERE p.mbiemri = :mbiemri")
    , @NamedQuery(name = "Pacienti.findByGjinia", query = "SELECT p FROM Pacienti p WHERE p.gjinia = :gjinia")
    , @NamedQuery(name = "Pacienti.findByNrTel", query = "SELECT p FROM Pacienti p WHERE p.nrTel = :nrTel")
    , @NamedQuery(name = "Pacienti.findByEmail", query = "SELECT p FROM Pacienti p WHERE p.email = :email")
    , @NamedQuery(name = "Pacienti.findByDatelindja", query = "SELECT p FROM Pacienti p WHERE p.datelindja = :datelindja")})
public class Pacienti implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "nrPersonal")
    private long nrPersonal;
    @Basic(optional = false)
    @Column(name = "emri")
    private String emri;
    @Basic(optional = false)
    @Column(name = "emriBabes")
    private String emriBabes;
    @Basic(optional = false)
    @Column(name = "mbiemri")
    private String mbiemri;
    @Basic(optional = false)
    @Column(name = "gjinia")
    private String gjinia;
    @Basic(optional = false)
    @Column(name = "nrTel")
    private String nrTel;
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @Column(name = "datelindja")
    @Temporal(TemporalType.DATE)
    private Date datelindja;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "pacienti", fetch = FetchType.LAZY)
    private Collection<PacientiSemundja> pacientiSemundjaCollection;
    @JoinColumn(name = "adresaId", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Adresa adresaId;
    @JoinColumn(name = "grupiGjakutId", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private GrupiGjakut grupiGjakutId;
    @JoinColumn(name = "profesioniId", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Profesioni profesioniId;

    public Pacienti() {
    }

    public Pacienti(Integer id) {
        this.id = id;
    }

    public Pacienti(Integer id, long nrPersonal, String emri, String emriBabes, String mbiemri, String gjinia, String nrTel, Date datelindja) {
        this.id = id;
        this.nrPersonal = nrPersonal;
        this.emri = emri;
        this.emriBabes = emriBabes;
        this.mbiemri = mbiemri;
        this.gjinia = gjinia;
        this.nrTel = nrTel;
        this.datelindja = datelindja;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public long getNrPersonal() {
        return nrPersonal;
    }

    public void setNrPersonal(long nrPersonal) {
        this.nrPersonal = nrPersonal;
    }

    public String getEmri() {
        return emri;
    }

    public void setEmri(String emri) {
        this.emri = emri;
    }

    public String getEmriBabes() {
        return emriBabes;
    }

    public void setEmriBabes(String emriBabes) {
        this.emriBabes = emriBabes;
    }

    public String getMbiemri() {
        return mbiemri;
    }

    public void setMbiemri(String mbiemri) {
        this.mbiemri = mbiemri;
    }

    public String getGjinia() {
        return gjinia;
    }

    public void setGjinia(String gjinia) {
        this.gjinia = gjinia;
    }

    public String getNrTel() {
        return nrTel;
    }

    public void setNrTel(String nrTel) {
        this.nrTel = nrTel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDatelindja() {
        return datelindja;
    }

    public void setDatelindja(Date datelindja) {
        this.datelindja = datelindja;
    }

    @XmlTransient
    public Collection<PacientiSemundja> getPacientiSemundjaCollection() {
        return pacientiSemundjaCollection;
    }

    public void setPacientiSemundjaCollection(Collection<PacientiSemundja> pacientiSemundjaCollection) {
        this.pacientiSemundjaCollection = pacientiSemundjaCollection;
    }

    public Adresa getAdresaId() {
        return adresaId;
    }

    public void setAdresaId(Adresa adresaId) {
        this.adresaId = adresaId;
    }

    public GrupiGjakut getGrupiGjakutId() {
        return grupiGjakutId;
    }

    public void setGrupiGjakutId(GrupiGjakut grupiGjakutId) {
        this.grupiGjakutId = grupiGjakutId;
    }

    public Profesioni getProfesioniId() {
        return profesioniId;
    }

    public void setProfesioniId(Profesioni profesioniId) {
        this.profesioniId = profesioniId;
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
        if (!(object instanceof Pacienti)) {
            return false;
        }
        Pacienti other = (Pacienti) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Pacienti[ id=" + id + " ]";
    }
    
}
