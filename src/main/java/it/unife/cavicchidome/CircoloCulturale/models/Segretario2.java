package it.unife.cavicchidome.CircoloCulturale.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

// Definizione dell'entità "Segretario2"
@Entity
@Table(name = "segretario_2") // Indica il nome della tabella nel database
public class Segretario2 {

    // Flag che indica se il segretario 2 è anche un amministratore
    @Id
    @Column(name = "admin", nullable = false)
    private Boolean admin = false;

    // Stipendio del segretario 2
    @Column(name = "stpendio", nullable = false, precision = 7, scale = 2)
    private BigDecimal stipendio;

    // Metodo per ottenere il flag che indica se il segretario 2 è anche un amministratore
    public Boolean getAdmin() {
        return admin;
    }

    // Metodo per impostare il flag che indica se il segretario 2 è anche un amministratore
    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    // Metodo per ottenere lo stipendio del segretario 2
    public BigDecimal getStipendio() {
        return stipendio;
    }

    // Metodo per impostare lo stipendio del segretario 2
    public void setStipendio(BigDecimal stipendio) {
        this.stipendio = stipendio;
    }
}
