package it.unife.cavicchidome.CircoloCulturale.models;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "tessera", schema = "public", uniqueConstraints = {
        @UniqueConstraint(name = "TESSERA_unique1", columnNames = {"id_socio"})
})
public class Tessera {
    @Id
    @Column(name = "id", columnDefinition = "bpchar", nullable = false, length = 10)
    private String id;

    @OneToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_socio", nullable = false)
    private Socio idSocio;

    @Column(name = "data_emissione", nullable = false)
    private LocalDate dataEmissione;

    @Column(name = "costo", nullable = false, precision = 4, scale = 2)
    private BigDecimal costo;

    @Column(name = "stato_pagamento", nullable = false, length = Integer.MAX_VALUE)
    private Character statoPagamento;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public Character getStatoPagamento() {
        return statoPagamento;
    }

    public void setStatoPagamento(Character statoPagamento) {
        this.statoPagamento = statoPagamento;
    }

}