package it.unife.cavicchidome.CircoloCulturale.models;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "utente", uniqueConstraints = {
        @UniqueConstraint(name = "UTENTE_unique1", columnNames = {"cf"})
})
public class Utente {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "cf", nullable = false, length = 16, columnDefinition = "bpchar")
    private String cf;

    @Column(name = "data_nascita", nullable = false)
    private LocalDate dataNascita;

    @Column(name = "luogo_nascita", nullable = false, length = 15)
    private String luogoNascita;

    @Column(name = "nome", nullable = false, length = 15)
    private String nome;

    @Column(name = "cognome", nullable = false, length = 15)
    private String cognome;

    @Column(name = "indirizzo", nullable = false, length = 50)
    private String indirizzo;

    @OneToOne(mappedBy = "utente")
    private Socio socio;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCf() {
        return cf;
    }

    public void setCf(String cf) {
        this.cf = cf;
    }

    public LocalDate getDataNascita() {
        return dataNascita;
    }

    public void setDataNascita(LocalDate dataNascita) {
        this.dataNascita = dataNascita;
    }

    public String getLuogoNascita() {
        return luogoNascita;
    }

    public void setLuogoNascita(String luogoNascita) {
        this.luogoNascita = luogoNascita;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public Socio getSocio() {
        return socio;
    }

    public void setSocio(Socio socio) {
        this.socio = socio;
    }

}