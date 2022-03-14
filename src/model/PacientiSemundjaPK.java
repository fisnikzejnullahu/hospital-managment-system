/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Fisnik Zejnullahu
 */
@Embeddable
public class PacientiSemundjaPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "pacientiId")
    private int pacientiId;
    @Basic(optional = false)
    @Column(name = "semundjaId")
    private int semundjaId;

    public PacientiSemundjaPK() {
    }

    public PacientiSemundjaPK(int pacientiId, int semundjaId) {
        this.pacientiId = pacientiId;
        this.semundjaId = semundjaId;
    }

    public int getPacientiId() {
        return pacientiId;
    }

    public void setPacientiId(int pacientiId) {
        this.pacientiId = pacientiId;
    }

    public int getSemundjaId() {
        return semundjaId;
    }

    public void setSemundjaId(int semundjaId) {
        this.semundjaId = semundjaId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) pacientiId;
        hash += (int) semundjaId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PacientiSemundjaPK)) {
            return false;
        }
        PacientiSemundjaPK other = (PacientiSemundjaPK) object;
        if (this.pacientiId != other.pacientiId) {
            return false;
        }
        if (this.semundjaId != other.semundjaId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.PacientiSemundjaPK[ pacientiId=" + pacientiId + ", semundjaId=" + semundjaId + " ]";
    }
    
}
