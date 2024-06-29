package it.unife.cavicchidome.CircoloCulturale.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class DocenteInsegnaId implements Serializable {
    private static final long serialVersionUID = 6837093130548800728L;
    @Column(name = "id_corso", nullable = false)
    private Integer idCorso;

    @Column(name = "id_docente", nullable = false)
    private Integer idDocente;

    public Integer getIdCorso() {
        return idCorso;
    }

    public void setIdCorso(Integer idCorso) {
        this.idCorso = idCorso;
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
        DocenteInsegnaId entity = (DocenteInsegnaId) o;
        return Objects.equals(this.idCorso, entity.idCorso) &&
                Objects.equals(this.idDocente, entity.idDocente);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCorso, idDocente);
    }

}