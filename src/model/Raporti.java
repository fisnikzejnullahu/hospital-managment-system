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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
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
@Table(name = "Raporti")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Raporti.findAll", query = "SELECT r FROM Raporti r")
    , @NamedQuery(name = "Raporti.findById", query = "SELECT r FROM Raporti r WHERE r.id = :id")
    , @NamedQuery(name = "Raporti.findByDataLeshimit", query = "SELECT r FROM Raporti r WHERE r.dataLeshimit = :dataLeshimit")
    , @NamedQuery(name = "Raporti.findByLlojiRaportit", query = "SELECT r FROM Raporti r WHERE r.llojiRaportit = :llojiRaportit")
    , @NamedQuery(name = "Raporti.findByPunetoriId", query = "SELECT r FROM Raporti r WHERE r.punetoriId = :punetoriId")})
public class Raporti implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "dataLeshimit")
    @Temporal(TemporalType.DATE)
    private Date dataLeshimit;
    @Basic(optional = false)
    @Column(name = "llojiRaportit")
    private String llojiRaportit;
    @JoinColumn(name = "punetoriId", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Punetori punetoriId;

    public Raporti() {
    }

    public Raporti(Integer id) {
        this.id = id;
    }

    public Raporti(Integer id, Date dataLeshimit, String llojiRaportit) {
        this.id = id;
        this.dataLeshimit = dataLeshimit;
        this.llojiRaportit = llojiRaportit;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDataLeshimit() {
        return dataLeshimit;
    }

    public void setDataLeshimit(Date dataLeshimit) {
        this.dataLeshimit = dataLeshimit;
    }

    public String getLlojiRaportit() {
        return llojiRaportit;
    }

    public void setLlojiRaportit(String llojiRaportit) {
        this.llojiRaportit = llojiRaportit;
    }

    public Punetori getPunetoriId() {
        return punetoriId;
    }

    public void setPunetoriId(Punetori punetoriId) {
        this.punetoriId = punetoriId;
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
        if (!(object instanceof Raporti)) {
            return false;
        }
        Raporti other = (Raporti) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Raporti[ id=" + id + " ]";
    }
    
}
