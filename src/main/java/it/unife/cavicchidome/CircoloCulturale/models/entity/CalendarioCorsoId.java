package it.unife.cavicchidome.CircoloCulturale.models.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class CalendarioCorsoId implements Serializable {
    private static final long serialVersionUID = -7587460371530592486L;
    @Column(name = "id_corso", nullable = false)
    private Integer idCorso;

    @Column(name = "giorno_settimana", columnDefinition = "weekday")
    private Object giornoSettimana;

    public Integer getIdCorso() {
        return idCorso;
    }

    public void setIdCorso(Integer idCorso) {
        this.idCorso = idCorso;
    }

    public Object getGiornoSettimana() {
        return giornoSettimana;
    }

    public void setGiornoSettimana(Object giornoSettimana) {
        this.giornoSettimana = giornoSettimana;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CalendarioCorsoId entity = (CalendarioCorsoId) o;
        return Objects.equals(this.idCorso, entity.idCorso) &&
                Objects.equals(this.giornoSettimana, entity.giornoSettimana);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCorso, giornoSettimana);
    }

}