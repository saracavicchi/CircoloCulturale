package it.unife.cavicchidome.CircoloCulturale.models;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "socio_frequenta", schema = "public")
public class SocioFrequenta {
    @EmbeddedId
    private SocioFrequentaId id;

    @MapsId("idCorso")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_corso", nullable = false)
    private Corso idCorso;

    @MapsId("idSocio")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_socio", nullable = false)
    private Socio idSocio;

    public SocioFrequentaId getId() {
        return id;
    }

    public void setId(SocioFrequentaId id) {
        this.id = id;
    }

    public Corso getIdCorso() {
        return idCorso;
    }

    public void setIdCorso(Corso idCorso) {
        this.idCorso = idCorso;
    }

    public Socio getIdSocio() {
        return idSocio;
    }

    public void setIdSocio(Socio idSocio) {
        this.idSocio = idSocio;
    }

}