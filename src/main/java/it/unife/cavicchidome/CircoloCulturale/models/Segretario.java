package it.unife.cavicchidome.CircoloCulturale.models;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;

@Entity
@Table(name = "segretario", schema = "public")
public class Segretario {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id", nullable = false)
    private Socio socio;

    @OneToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "sede_amministrata")
    private Sede sedeAmministrata;

    @Column(name = "admin", nullable = false)
    private Boolean admin = false;

    @Column(name = "stipendio", nullable = false, precision = 7, scale = 2)
    private BigDecimal stipendio;

    @Column(name = "active", nullable = false)
    private Boolean active = false;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Socio getSocio() {
        return socio;
    }

    public void setSocio(Socio socio) {
        this.socio = socio;
    }

    public Sede getSedeAmministrata() {
        return sedeAmministrata;
    }

    public void setSedeAmministrata(Sede sedeAmministrata) {
        this.sedeAmministrata = sedeAmministrata;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public BigDecimal getStipendio() {
        return stipendio;
    }

    public void setStipendio(BigDecimal stipendio) {
        this.stipendio = stipendio;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

}