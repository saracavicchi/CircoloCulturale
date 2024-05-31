package it.unife.cavicchidome.CircoloCulturale.models;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;

// Definizione dell'entità "Docente"
@Entity
@Table(name = "docente") // Indica il nome della tabella nel database
public class Docente {

    // Identificatore univoco del docente
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    // Associazione uno a uno con l'entità Socio, utilizzando la stessa chiave primaria
    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE) // Azione di cancellazione in cascata
    @JoinColumn(name = "id", nullable = false) // Nome della colonna che contiene l'ID del socio
    private Socio socio;

    // Stipendio del docente
    @Column(name = "stipendio", nullable = false, precision = 7, scale = 2)
    private BigDecimal stipendio;

    // Metodo per ottenere l'ID del docente
    public Integer getId() {
        return id;
    }

    // Metodo per impostare l'ID del docente
    public void setId(Integer id) {
        this.id = id;
    }

    // Metodo per ottenere il socio associato al docente
    public Socio getSocio() {
        return socio;
    }

    // Metodo per impostare il socio associato al docente
    public void setSocio(Socio socio) {
        this.socio = socio;
    }

    // Metodo per ottenere lo stipendio del docente
    public BigDecimal getStipendio() {
        return stipendio;
    }

    // Metodo per impostare lo stipendio del docente
    public void setStipendio(BigDecimal stipendio) {
        this.stipendio = stipendio;
    }
}
