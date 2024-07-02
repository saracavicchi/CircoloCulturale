package it.unife.cavicchidome.CircoloCulturale.models.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalTime;

@Entity
@Table(name = "orario_sede", schema = "public")
public class OrarioSede {
    @EmbeddedId
    private OrarioSedeId id;

    @MapsId("idSede")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_sede", nullable = false)
    private Sede idSede;

    @Column(name = "orario_apertura", nullable = false)
    private LocalTime orarioApertura;

    @Column(name = "orario_chiusura", nullable = false)
    private LocalTime orarioChiusura;

    public OrarioSedeId getId() {
        return id;
    }

    public void setId(OrarioSedeId id) {
        this.id = id;
    }

    public Sede getIdSede() {
        return idSede;
    }

    public void setIdSede(Sede idSede) {
        this.idSede = idSede;
    }

    public LocalTime getOrarioApertura() {
        return orarioApertura;
    }

    public void setOrarioApertura(LocalTime orarioApertura) {
        this.orarioApertura = orarioApertura;
    }

    public LocalTime getOrarioChiusura() {
        return orarioChiusura;
    }

    public void setOrarioChiusura(LocalTime orarioChiusura) {
        this.orarioChiusura = orarioChiusura;
    }

}