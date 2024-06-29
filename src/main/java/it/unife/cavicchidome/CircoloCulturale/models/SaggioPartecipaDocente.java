package it.unife.cavicchidome.CircoloCulturale.models;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "saggio_partecipa_docente", schema = "public")
public class SaggioPartecipaDocente {
    @EmbeddedId
    private SaggioPartecipaDocenteId id;

    @MapsId("idSaggio")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_saggio", nullable = false)
    private Saggio idSaggio;

    @MapsId("idDocente")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_docente", nullable = false)
    private Docente idDocente;

    public SaggioPartecipaDocenteId getId() {
        return id;
    }

    public void setId(SaggioPartecipaDocenteId id) {
        this.id = id;
    }

    public Saggio getIdSaggio() {
        return idSaggio;
    }

    public void setIdSaggio(Saggio idSaggio) {
        this.idSaggio = idSaggio;
    }

    public Docente getIdDocente() {
        return idDocente;
    }

    public void setIdDocente(Docente idDocente) {
        this.idDocente = idDocente;
    }

}