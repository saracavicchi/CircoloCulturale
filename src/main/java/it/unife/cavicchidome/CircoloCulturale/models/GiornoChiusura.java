package it.unife.cavicchidome.CircoloCulturale.models;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "giorno_chiusura", schema = "public")
public class GiornoChiusura {
    @EmbeddedId
    private GiornoChiusuraId id;

    @MapsId("idSede")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_sede", nullable = false)
    private Sede idSede;

    public GiornoChiusuraId getId() {
        return id;
    }

    public void setId(GiornoChiusuraId id) {
        this.id = id;
    }

    public Sede getIdSede() {
        return idSede;
    }

    public void setIdSede(Sede idSede) {
        this.idSede = idSede;
    }

}