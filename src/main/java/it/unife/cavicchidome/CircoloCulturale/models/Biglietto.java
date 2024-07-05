package it.unife.cavicchidome.CircoloCulturale.models;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Entity
@Table(name = "biglietto", schema = "public")
public class Biglietto {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_utente", nullable = false)
    private Utente idUtente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_saggio", nullable = false)
    private Saggio idSaggio;

    @Column(name = "quantita", nullable = false)
    private Integer quantita;

    @Column(name = "data_ora_acquisto", nullable = false)
    private Instant dataOraAcquisto;

    @Column(name = "stato_pagamento", nullable = false, length = Integer.MAX_VALUE)
    private Character statoPagamento;

    @Column(name = "sconto", nullable = false)
    private Boolean sconto = false;

    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Utente getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(Utente idUtente) {
        this.idUtente = idUtente;
    }

    public Saggio getIdSaggio() {
        return idSaggio;
    }

    public void setIdSaggio(Saggio idSaggio) {
        this.idSaggio = idSaggio;
    }

    public Integer getQuantita() {
        return quantita;
    }

    public void setQuantita(Integer quantita) {
        this.quantita = quantita;
    }

    public Instant getDataOraAcquisto() {
        return dataOraAcquisto;
    }

    public void setDataOraAcquisto(Instant dataOraAcquisto) {
        this.dataOraAcquisto = dataOraAcquisto;
    }

    public Character getStatoPagamento() {
        return statoPagamento;
    }

    public void setStatoPagamento(Character statoPagamento) {
        this.statoPagamento = statoPagamento;
    }

    public Boolean getSconto() {
        return sconto;
    }

    public void setSconto(Boolean sconto) {
        this.sconto = sconto;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

}