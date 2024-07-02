package it.unife.cavicchidome.CircoloCulturale.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class OrarioSedeId implements Serializable {
    private static final long serialVersionUID = 4270693558481198676L;
    @Column(name = "id_sede", nullable = false)
    private Integer idSede;

    @Column(name = "giorno_settimana", columnDefinition = "weekday")
    private Object giornoSettimana;

    public Integer getIdSede() {
        return idSede;
    }

    public void setIdSede(Integer idSede) {
        this.idSede = idSede;
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
        OrarioSedeId entity = (OrarioSedeId) o;
        return Objects.equals(this.giornoSettimana, entity.giornoSettimana) &&
                Objects.equals(this.idSede, entity.idSede);
    }

    @Override
    public int hashCode() {
        return Objects.hash(giornoSettimana, idSede);
    }

}