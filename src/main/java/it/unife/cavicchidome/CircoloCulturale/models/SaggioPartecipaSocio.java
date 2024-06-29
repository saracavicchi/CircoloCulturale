package it.unife.cavicchidome.CircoloCulturale.models;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "saggio_partecipa_socio", schema = "public")
public class SaggioPartecipaSocio {
    @EmbeddedId
    private SaggioPartecipaSocioId id;

    @MapsId("idSaggio")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_saggio", nullable = false)
    private Saggio idSaggio;

    @MapsId("idSocio")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_socio", nullable = false)
    private Socio idSocio;

    public SaggioPartecipaSocioId getId() {
        return id;
    }

    public void setId(SaggioPartecipaSocioId id) {
        this.id = id;
    }

    public Saggio getIdSaggio() {
        return idSaggio;
    }

    public void setIdSaggio(Saggio idSaggio) {
        this.idSaggio = idSaggio;
    }

    public Socio getIdSocio() {
        return idSocio;
    }

    public void setIdSocio(Socio idSocio) {
        this.idSocio = idSocio;
    }

}