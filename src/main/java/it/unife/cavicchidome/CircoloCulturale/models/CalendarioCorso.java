package it.unife.cavicchidome.CircoloCulturale.models;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalTime;

@Entity
@Table(name = "calendario_corso", schema = "public")
public class CalendarioCorso {
    @EmbeddedId
    private CalendarioCorsoId id;

    @MapsId("idCorso")
    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_corso", nullable = false)
    private Corso idCorso;

    @Column(name = "orario_inizio", nullable = false)
    private LocalTime orarioInizio;

    @Column(name = "orario_fine", nullable = false)
    private LocalTime orarioFine;

    public CalendarioCorsoId getId() {
        return id;
    }

    public void setId(CalendarioCorsoId id) {
        this.id = id;
    }

    public Corso getIdCorso() {
        return idCorso;
    }

    public void setIdCorso(Corso idCorso) {
        this.idCorso = idCorso;
    }

    public LocalTime getOrarioInizio() {
        return orarioInizio;
    }

    public void setOrarioInizio(LocalTime orarioInizio) {
        this.orarioInizio = orarioInizio;
    }

    public LocalTime getOrarioFine() {
        return orarioFine;
    }

    public void setOrarioFine(LocalTime orarioFine) {
        this.orarioFine = orarioFine;
    }

    public Weekday getGiornoSettimana() {
        return this.id.getGiornoSettimana();
    }

}