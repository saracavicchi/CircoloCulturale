package it.unife.cavicchidome.CircoloCulturale.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class SaggioPartecipaSocioId implements Serializable {
    private static final long serialVersionUID = -1891196423717229623L;
    @Column(name = "id_saggio", nullable = false)
    private Integer idSaggio;

    @Column(name = "id_socio", nullable = false)
    private Integer idSocio;

    public Integer getIdSaggio() {
        return idSaggio;
    }

    public void setIdSaggio(Integer idSaggio) {
        this.idSaggio = idSaggio;
    }

    public Integer getIdSocio() {
        return idSocio;
    }

    public void setIdSocio(Integer idSocio) {
        this.idSocio = idSocio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SaggioPartecipaSocioId entity = (SaggioPartecipaSocioId) o;
        return Objects.equals(this.idSocio, entity.idSocio) &&
                Objects.equals(this.idSaggio, entity.idSaggio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idSocio, idSaggio);
    }

}