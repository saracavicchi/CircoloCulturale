package it.unife.cavicchidome.CircoloCulturale.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class SaggioPartecipaDocenteId implements Serializable {
    private static final long serialVersionUID = -3678237472172026873L;
    @Column(name = "id_saggio", nullable = false)
    private Integer idSaggio;

    @Column(name = "id_docente", nullable = false)
    private Integer idDocente;

    public Integer getIdSaggio() {
        return idSaggio;
    }

    public void setIdSaggio(Integer idSaggio) {
        this.idSaggio = idSaggio;
    }

    public Integer getIdDocente() {
        return idDocente;
    }

    public void setIdDocente(Integer idDocente) {
        this.idDocente = idDocente;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SaggioPartecipaDocenteId entity = (SaggioPartecipaDocenteId) o;
        return Objects.equals(this.idSaggio, entity.idSaggio) &&
                Objects.equals(this.idDocente, entity.idDocente);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idSaggio, idDocente);
    }

}