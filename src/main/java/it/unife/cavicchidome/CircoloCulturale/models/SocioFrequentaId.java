package it.unife.cavicchidome.CircoloCulturale.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class SocioFrequentaId implements Serializable {
    private static final long serialVersionUID = -8189281786232244820L;
    @Column(name = "id_corso", nullable = false)
    private Integer idCorso;

    @Column(name = "id_socio", nullable = false)
    private Integer idSocio;

    public Integer getIdCorso() {
        return idCorso;
    }

    public void setIdCorso(Integer idCorso) {
        this.idCorso = idCorso;
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
        SocioFrequentaId entity = (SocioFrequentaId) o;
        return Objects.equals(this.idCorso, entity.idCorso) &&
                Objects.equals(this.idSocio, entity.idSocio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCorso, idSocio);
    }

}