package it.unife.cavicchidome.CircoloCulturale.models;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.LocalDate;

// Questa classe rappresenta l'entità "Tessera" nel contesto del circolo culturale
@Entity
@Table(name = "tessera") // Specifica il nome della tabella nel database associata a questa entità
public class Tessera {
    // Identificatore univoco della tessera
    @Id
    @Column(name = "id", nullable = false) // Specifica il nome della colonna nell'entità associata a questo campo
    private Integer id;

    // Associazione uno a uno con l'entità Socio
    @OneToOne(fetch = FetchType.LAZY, optional = false) // Specifica la relazione uno-a-uno con Socio
    @OnDelete(action = OnDeleteAction.CASCADE) // Azione di cancellazione in cascata: se il socio viene eliminato, anche la tessera
    @JoinColumn(name = "id_socio", nullable = false) // Specifica la colonna nel database che mappa questa relazione
    private Socio idSocio;

    // Data di emissione della tessera
    @Column(name = "data_emissione", nullable = false) // Specifica il nome e le caratteristiche della colonna nel database
    private LocalDate dataEmissione;

    // Costo della tessera
    @Column(name = "costo", nullable = false, precision = 4, scale = 2) // Specifica il nome e le caratteristiche della colonna nel database
    private BigDecimal costo;

    // Stato di pagamento della tessera
    @Column(name = "stato_pagamento", nullable = false, columnDefinition = "bpchar") // Specifica il nome e le caratteristiche della colonna nel database
    private String statoPagamento;

    // Metodo per ottenere l'ID della tessera
    public Integer getId() {
        return id;
    }

    // Metodo per impostare l'ID della tessera
    public void setId(Integer id) {
        this.id = id;
    }

    // Metodo per ottenere l'ID del socio associato alla tessera
    public Socio getIdSocio() {
        return idSocio;
    }

    // Metodo per impostare l'ID del socio associato alla tessera
    public void setIdSocio(Socio idSocio) {
        this.idSocio = idSocio;
    }

    // Metodo per ottenere la data di emissione della tessera
    public LocalDate getDataEmissione() {
        return dataEmissione;
    }

    // Metodo per impostare la data di emissione della tessera
    public void setDataEmissione(LocalDate dataEmissione) {
        this.dataEmissione = dataEmissione;
    }

    // Metodo per ottenere il costo della tessera
    public BigDecimal getCosto() {
        return costo;
    }

    // Metodo per impostare il costo della tessera
    public void setCosto(BigDecimal costo) {
        this.costo = costo;
    }

    // Metodo per ottenere lo stato di pagamento della tessera
    public String getStatoPagamento() {
        return statoPagamento;
    }

    // Metodo per impostare lo stato di pagamento della tessera
    public void setStatoPagamento(String statoPagamento) {
        this.statoPagamento = statoPagamento;
    }
}
