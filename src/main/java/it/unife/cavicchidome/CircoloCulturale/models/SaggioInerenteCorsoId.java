package it.unife.cavicchidome.CircoloCulturale.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class SaggioInerenteCorsoId implements Serializable {
    private static final long serialVersionUID = 2742213697946065590L;
    @Column(name = "id_saggio", nullable = false)
    private Integer idSaggio;

    @Column(name = "id_corso", nullable = false)
    private Integer idCorso;

    public Integer getIdSaggio() {
        return idSaggio;
    }

    public void setIdSaggio(Integer idSaggio) {
        this.idSaggio = idSaggio;
    }

    public Integer getIdCorso() {
        return idCorso;
    }

    public void setIdCorso(Integer idCorso) {
        this.idCorso = idCorso;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SaggioInerenteCorsoId entity = (SaggioInerenteCorsoId) o;
        return Objects.equals(this.idCorso, entity.idCorso) &&
                Objects.equals(this.idSaggio, entity.idSaggio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCorso, idSaggio);
    }

}