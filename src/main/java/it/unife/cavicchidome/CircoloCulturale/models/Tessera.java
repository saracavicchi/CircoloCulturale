package it.unife.cavicchidome.CircoloCulturale.models;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "tessera")
public class Tessera {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_socio", nullable = false)
    private Socio idSocio;

    @Column(name = "data_emissione", nullable = false)
    private LocalDate dataEmissione;

    @Column(name = "costo", nullable = false, precision = 4, scale = 2)
    private BigDecimal costo;

    @Column(name = "stato_pagamento", nullable = false, columnDefinition = "bpchar")
    private String statoPagamento;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Socio getIdSocio() {
        return idSocio;
    }

    public void setIdSocio(Socio idSocio) {
        this.idSocio = idSocio;
    }

    public LocalDate getDataEmissione() {
        return dataEmissione;
    }

    public void setDataEmissione(LocalDate dataEmissione) {
        this.dataEmissione = dataEmissione;
    }

    public BigDecimal getCosto() {
        return costo;
    }

    public void setCosto(BigDecimal costo) {
        this.costo = costo;
    }

    public String getStatoPagamento() {
        return statoPagamento;
    }

    public void setStatoPagamento(String statoPagamento) {
        this.statoPagamento = statoPagamento;
    }

}