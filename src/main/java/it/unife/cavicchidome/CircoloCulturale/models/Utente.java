package it.unife.cavicchidome.CircoloCulturale.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "utente", schema = "public", uniqueConstraints = {
        @UniqueConstraint(name = "UTENTE_unique1", columnNames = {"cf"})
})
public class Utente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "cf", columnDefinition = "bpchar", nullable = false, length = 16)
    private String cf;

    @Column(name = "data_nascita", nullable = false)
    private LocalDate dataNascita;

    @Column(name = "luogo_nascita", nullable = false, length = 20)
    private String luogoNascita;

    @Column(name = "nome", nullable = false, length = 20)
    private String nome;

    @Column(name = "cognome", nullable = false, length = 20)
    private String cognome;

    @Column(name = "indirizzo", nullable = false, length = 80)
    private String indirizzo;

    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;

    @OneToMany(mappedBy = "idUtente")
    private Set<Biglietto> biglietti = new LinkedHashSet<>();

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

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Set<Biglietto> getBiglietti() {
        return biglietti;
    }

    public void setBiglietti(Set<Biglietto> biglietti) {
        this.biglietti = biglietti;
    }

    public Socio getSocio() {
        return socio;
    }

    public void setSocio(Socio socio) {
        this.socio = socio;
    }

    public Utente() {
    }

    public Utente(String cf, LocalDate dataNascita, String luogoNascita, String nome, String cognome, String indirizzo, Boolean deleted) {
        this.cf = cf.toUpperCase();
        this.dataNascita = dataNascita;
        this.luogoNascita = luogoNascita;
        this.nome = nome;
        this.cognome = cognome;
        this.indirizzo = indirizzo;
        this.deleted = deleted;
    }
}