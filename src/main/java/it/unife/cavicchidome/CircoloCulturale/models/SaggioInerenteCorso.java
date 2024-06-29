package it.unife.cavicchidome.CircoloCulturale.models;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "saggio_inerente_corso", schema = "public")
public class SaggioInerenteCorso {
    @EmbeddedId
    private SaggioInerenteCorsoId id;

    @MapsId("idSaggio")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_saggio", nullable = false)
    private Saggio idSaggio;

    @MapsId("idCorso")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_corso", nullable = false)
    private Corso idCorso;

    public SaggioInerenteCorsoId getId() {
        return id;
    }

    public void setId(SaggioInerenteCorsoId id) {
        this.id = id;
    }

    public Saggio getIdSaggio() {
        return idSaggio;
    }

    public void setIdSaggio(Saggio idSaggio) {
        this.idSaggio = idSaggio;
    }

    public Corso getIdCorso() {
        return idCorso;
    }

    public void setIdCorso(Corso idCorso) {
        this.idCorso = idCorso;
    }

}