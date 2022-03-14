/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Fisnik Zejnullahu
 */
@Entity
@Table(name = "Pacienti_Semundja")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PacientiSemundja.findAll", query = "SELECT p FROM PacientiSemundja p")
    , @NamedQuery(name = "PacientiSemundja.findByPacientiId", query = "SELECT p FROM PacientiSemundja p WHERE p.pacientiSemundjaPK.pacientiId = :pacientiId")
    , @NamedQuery(name = "PacientiSemundja.findBySemundjaId", query = "SELECT p FROM PacientiSemundja p WHERE p.pacientiSemundjaPK.semundjaId = :semundjaId")
    , @NamedQuery(name = "PacientiSemundja.findByDataSemundjes", query = "SELECT p FROM PacientiSemundja p WHERE p.dataSemundjes = :dataSemundjes")})
public class PacientiSemundja implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PacientiSemundjaPK pacientiSemundjaPK;
    @Basic(optional = false)
    @Column(name = "dataSemundjes")
    @Temporal(TemporalType.DATE)
    private Date dataSemundjes;
    @JoinColumn(name = "pacientiId", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Pacienti pacienti;
    @JoinColumn(name = "semundjaId", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Semundja semundja;

    public PacientiSemundja() {
    }

    public PacientiSemundja(PacientiSemundjaPK pacientiSemundjaPK) {
        this.pacientiSemundjaPK = pacientiSemundjaPK;
    }

    public PacientiSemundja(PacientiSemundjaPK pacientiSemundjaPK, Date dataSemundjes) {
        this.pacientiSemundjaPK = pacientiSemundjaPK;
        this.dataSemundjes = dataSemundjes;
    }

    public PacientiSemundja(int pacientiId, int semundjaId) {
        this.pacientiSemundjaPK = new PacientiSemundjaPK(pacientiId, semundjaId);
    }

    public PacientiSemundjaPK getPacientiSemundjaPK() {
        return pacientiSemundjaPK;
    }

    public void setPacientiSemundjaPK(PacientiSemundjaPK pacientiSemundjaPK) {
        this.pacientiSemundjaPK = pacientiSemundjaPK;
    }

    public Date getDataSemundjes() {
        return dataSemundjes;
    }

    public void setDataSemundjes(Date dataSemundjes) {
        this.dataSemundjes = dataSemundjes;
    }

    public Pacienti getPacienti() {
        return pacienti;
    }

    public void setPacienti(Pacienti pacienti) {
        this.pacienti = pacienti;
    }

    public Semundja getSemundja() {
        return semundja;
    }

    public void setSemundja(Semundja semundja) {
        this.semundja = semundja;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pacientiSemundjaPK != null ? pacientiSemundjaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PacientiSemundja)) {
            return false;
        }
        PacientiSemundja other = (PacientiSemundja) object;
        if ((this.pacientiSemundjaPK == null && other.pacientiSemundjaPK != null) || (this.pacientiSemundjaPK != null && !this.pacientiSemundjaPK.equals(other.pacientiSemundjaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.PacientiSemundja[ pacientiSemundjaPK=" + pacientiSemundjaPK + " ]";
    }
    
}
