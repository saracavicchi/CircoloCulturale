package it.unife.cavicchidome.CircoloCulturale.models;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "docente_insegna", schema = "public")
public class DocenteInsegna {
    @EmbeddedId
    private DocenteInsegnaId id;

    @MapsId("idCorso")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_corso", nullable = false)
    private Corso idCorso;

    @MapsId("idDocente")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_docente", nullable = false)
    private Docente idDocente;

    public DocenteInsegnaId getId() {
        return id;
    }

    public void setId(DocenteInsegnaId id) {
        this.id = id;
    }

    public Corso getIdCorso() {
        return idCorso;
    }

    public void setIdCorso(Corso idCorso) {
        this.idCorso = idCorso;
    }

    public Docente getIdDocente() {
        return idDocente;
    }

    public void setIdDocente(Docente idDocente) {
        this.idDocente = idDocente;
    }

}