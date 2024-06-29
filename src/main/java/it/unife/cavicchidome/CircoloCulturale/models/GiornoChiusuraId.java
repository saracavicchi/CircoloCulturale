package it.unife.cavicchidome.CircoloCulturale.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Embeddable
public class GiornoChiusuraId implements Serializable {
    private static final long serialVersionUID = -4283234164442422880L;
    @Column(name = "id_sede", nullable = false)
    private Integer idSede;

    @Column(name = "giorno_chiusura", nullable = false)
    private LocalDate giornoChiusura;

    public Integer getIdSede() {
        return idSede;
    }

    public void setIdSede(Integer idSede) {
        this.idSede = idSede;
    }

    public LocalDate getGiornoChiusura() {
        return giornoChiusura;
    }

    public void setGiornoChiusura(LocalDate giornoChiusura) {
        this.giornoChiusura = giornoChiusura;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        GiornoChiusuraId entity = (GiornoChiusuraId) o;
        return Objects.equals(this.giornoChiusura, entity.giornoChiusura) &&
                Objects.equals(this.idSede, entity.idSede);
    }

    @Override
    public int hashCode() {
        return Objects.hash(giornoChiusura, idSede);
    }

}