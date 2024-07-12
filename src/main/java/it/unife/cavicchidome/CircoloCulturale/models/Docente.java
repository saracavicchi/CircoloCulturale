package it.unife.cavicchidome.CircoloCulturale.models;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "docente", schema = "public")
public class Docente {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id", nullable = false)
    private Socio socio;

    @Column(name = "stipendio", nullable = false, precision = 6, scale = 0)
    private BigDecimal stipendio;

    @Column(name = "active", nullable = false)
    private Boolean active = false;

    @ManyToMany
    @JoinTable(name = "docente_insegna",
            joinColumns = @JoinColumn(name = "id_docente"),
            inverseJoinColumns = @JoinColumn(name = "id_corso"))
    private Set<Corso> corsi = new LinkedHashSet<>();
    /*
    @ManyToMany
    @JoinTable(name = "saggio_partecipa_docente",
            joinColumns = @JoinColumn(name = "id_docente"),
            inverseJoinColumns = @JoinColumn(name = "id_saggio"))
    private Set<Saggio> saggi = new LinkedHashSet<>();

     */

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

    public Set<Corso> getCorsi() {
        return corsi;
    }

    public void setCorsi(Set<Corso> corsi) {
        this.corsi = corsi;
    }
/*
    public Set<Saggio> getSaggi() {
        return saggi;
    }

    public void setSaggi(Set<Saggio> saggi) {
        this.saggi = saggi;
    }

 */

}