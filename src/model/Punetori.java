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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
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
@Table(name = "Punetori")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Punetori.findAll", query = "SELECT p FROM Punetori p")
    , @NamedQuery(name = "Punetori.findById", query = "SELECT p FROM Punetori p WHERE p.id = :id")
    , @NamedQuery(name = "Punetori.findByNrPersonal", query = "SELECT p FROM Punetori p WHERE p.nrPersonal = :nrPersonal")
    , @NamedQuery(name = "Punetori.findByEmri", query = "SELECT p FROM Punetori p WHERE p.emri = :emri")
    , @NamedQuery(name = "Punetori.findByEmriBabes", query = "SELECT p FROM Punetori p WHERE p.emriBabes = :emriBabes")
    , @NamedQuery(name = "Punetori.findByMbiemri", query = "SELECT p FROM Punetori p WHERE p.mbiemri = :mbiemri")
    , @NamedQuery(name = "Punetori.findByNrTel", query = "SELECT p FROM Punetori p WHERE p.nrTel = :nrTel")
    , @NamedQuery(name = "Punetori.findByGjinia", query = "SELECT p FROM Punetori p WHERE p.gjinia = :gjinia")
    , @NamedQuery(name = "Punetori.findByEmail", query = "SELECT p FROM Punetori p WHERE p.email = :email")
    , @NamedQuery(name = "Punetori.findByDatelindja", query = "SELECT p FROM Punetori p WHERE p.datelindja = :datelindja")
    , @NamedQuery(name = "Punetori.findByDataFillimit", query = "SELECT p FROM Punetori p WHERE p.dataFillimit = :dataFillimit")
    , @NamedQuery(name = "Punetori.findByDataMbarimit", query = "SELECT p FROM Punetori p WHERE p.dataMbarimit = :dataMbarimit")})
public class Punetori implements Serializable {

    @Lob
    @Column(name = "foto")
    private byte[] foto;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "punetoriId")
    private Collection<Perdoruesi> perdoruesiCollection;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @Column(name = "nrTel")
    private String nrTel;
    @Basic(optional = false)
    @Column(name = "gjinia")
    private String gjinia;
    @Basic(optional = false)
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @Column(name = "datelindja")
    @Temporal(TemporalType.DATE)
    private Date datelindja;
    @Basic(optional = false)
    @Column(name = "dataFillimit")
    @Temporal(TemporalType.DATE)
    private Date dataFillimit;
    @Column(name = "dataMbarimit")
    @Temporal(TemporalType.DATE)
    private Date dataMbarimit;
    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "punetoriId")
    private Collection<Raporti> raportiCollection;
    @JoinColumn(name = "adresaId", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Adresa adresaId;
    @JoinColumn(name = "departamentiId", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Departamenti departamentiId;

    public Punetori() {
    }

    public Punetori(Integer id) {
        this.id = id;
    }

    public Punetori(Integer id, long nrPersonal, String emri, String emriBabes, String mbiemri, String nrTel, String gjinia, String email, Date datelindja, Date dataFillimit) {
        this.id = id;
        this.nrPersonal = nrPersonal;
        this.emri = emri;
        this.emriBabes = emriBabes;
        this.mbiemri = mbiemri;
        this.nrTel = nrTel;
        this.gjinia = gjinia;
        this.email = email;
        this.datelindja = datelindja;
        this.dataFillimit = dataFillimit;
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

    public String getNrTel() {
        return nrTel;
    }

    public void setNrTel(String nrTel) {
        this.nrTel = nrTel;
    }

    public String getGjinia() {
        return gjinia;
    }

    public void setGjinia(String gjinia) {
        this.gjinia = gjinia;
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

    public Date getDataFillimit() {
        return dataFillimit;
    }

    public void setDataFillimit(Date dataFillimit) {
        this.dataFillimit = dataFillimit;
    }

    public Date getDataMbarimit() {
        return dataMbarimit;
    }

    public void setDataMbarimit(Date dataMbarimit) {
        this.dataMbarimit = dataMbarimit;
    }


    @XmlTransient
    public Collection<Raporti> getRaportiCollection() {
        return raportiCollection;
    }

    public void setRaportiCollection(Collection<Raporti> raportiCollection) {
        this.raportiCollection = raportiCollection;
    }

    public Adresa getAdresaId() {
        return adresaId;
    }

    public void setAdresaId(Adresa adresaId) {
        this.adresaId = adresaId;
    }

    public Departamenti getDepartamentiId() {
        return departamentiId;
    }

    public void setDepartamentiId(Departamenti departamentiId) {
        this.departamentiId = departamentiId;
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
        if (!(object instanceof Punetori)) {
            return false;
        }
        Punetori other = (Punetori) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return emri + " " +mbiemri + "[ id=" + id + " ]";
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    @XmlTransient
    public Collection<Perdoruesi> getPerdoruesiCollection() {
        return perdoruesiCollection;
    }

    public void setPerdoruesiCollection(Collection<Perdoruesi> perdoruesiCollection) {
        this.perdoruesiCollection = perdoruesiCollection;
    }
    
}
