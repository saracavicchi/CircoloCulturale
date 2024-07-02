package it.unife.cavicchidome.CircoloCulturale.models.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "sala", schema = "public", uniqueConstraints = {
        @UniqueConstraint(name = "SALA_unique1", columnNames = {"id_sede", "numero_sala"})
})
public class Sala {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "id_sede", nullable = false)
    private Sede idSede;

    @Column(name = "numero_sala", nullable = false)
    private Integer numeroSala;

    @Column(name = "descrizione", length = Integer.MAX_VALUE)
    private String descrizione;

    @Column(name = "capienza", nullable = false)
    private Integer capienza;

    @Column(name = "prenotabile", nullable = false)
    private Boolean prenotabile = false;

    @Column(name = "active", nullable = false)
    private Boolean active = false;

    @OneToMany(mappedBy = "idSala")
    private Set<Corso> corsi = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idSala")
    private Set<PrenotazioneSala> prenotazioniSala = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Sede getIdSede() {
        return idSede;
    }

    public void setIdSede(Sede idSede) {
        this.idSede = idSede;
    }

    public Integer getNumeroSala() {
        return numeroSala;
    }

    public void setNumeroSala(Integer numeroSala) {
        this.numeroSala = numeroSala;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Integer getCapienza() {
        return capienza;
    }

    public void setCapienza(Integer capienza) {
        this.capienza = capienza;
    }

    public Boolean getPrenotabile() {
        return prenotabile;
    }

    public void setPrenotabile(Boolean prenotabile) {
        this.prenotabile = prenotabile;
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

    public Set<PrenotazioneSala> getPrenotazioniSala() {
        return prenotazioniSala;
    }

    public void setPrenotazioniSala(Set<PrenotazioneSala> prenotazioniSala) {
        this.prenotazioniSala = prenotazioniSala;
    }

}