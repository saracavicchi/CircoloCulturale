package it.unife.cavicchidome.CircoloCulturale.models;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

// Definizione dell'entità "Segretario1"
@Entity
@Table(name = "segretario_1") // Indica il nome della tabella nel database
public class Segretario1 {

    // Identificatore univoco del segretario 1
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    // Associazione uno a uno con l'entità Socio, utilizzando la stessa chiave primaria
    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE) // Azione di cancellazione in cascata
    @JoinColumn(name = "id", nullable = false) // Nome della colonna che contiene l'ID del socio
    private Socio socio;

    // Flag che indica se il segretario è anche un amministratore
    @Column(name = "admin", nullable = false)
    private Boolean admin = false;

    // Metodo per ottenere l'ID del segretario 1
    public Integer getId() {
        return id;
    }

    // Metodo per impostare l'ID del segretario 1
    public void setId(Integer id) {
        this.id = id;
    }

    // Metodo per ottenere il socio associato al segretario 1
    public Socio getSocio() {
        return socio;
    }

    // Metodo per impostare il socio associato al segretario 1
    public void setSocio(Socio socio) {
        this.socio = socio;
    }

    // Metodo per ottenere il flag che indica se il segretario è anche un amministratore
    public Boolean getAdmin() {
        return admin;
    }

    // Metodo per impostare il flag che indica se il segretario è anche un amministratore
    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

}
