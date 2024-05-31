package it.unife.cavicchidome.CircoloCulturale.models;

import jakarta.persistence.*;
import java.time.LocalDate;

// Questa classe rappresenta l'entità "Utente" nel contesto del circolo culturale
@Entity
@Table(name = "utente", uniqueConstraints = {
        @UniqueConstraint(name = "UTENTE_unique1", columnNames = {"cf"}) // Specifica il vincolo di unicità sul codice fiscale
})
public class Utente {
    // Identificatore univoco dell'utente
    @Id
    @Column(name = "id", nullable = false) // Specifica il nome della colonna nell'entità associata a questo campo
    private Integer id;

    // Codice fiscale dell'utente
    @Column(name = "cf", nullable = false, length = 16, columnDefinition = "bpchar") // Specifica il nome e le caratteristiche della colonna nel database
    private String cf;

    // Data di nascita dell'utente
    @Column(name = "data_nascita", nullable = false) // Specifica il nome e le caratteristiche della colonna nel database
    private LocalDate dataNascita;

    // Luogo di nascita dell'utente
    @Column(name = "luogo_nascita", nullable = false, length = 15) // Specifica il nome e le caratteristiche della colonna nel database
    private String luogoNascita;

    // Nome dell'utente
    @Column(name = "nome", nullable = false, length = 15) // Specifica il nome e le caratteristiche della colonna nel database
    private String nome;

    // Cognome dell'utente
    @Column(name = "cognome", nullable = false, length = 15) // Specifica il nome e le caratteristiche della colonna nel database
    private String cognome;

    // Indirizzo dell'utente
    @Column(name = "indirizzo", nullable = false, length = 50) // Specifica il nome e le caratteristiche della colonna nel database
    private String indirizzo;

    // Relazione uno a uno con un socio
    @OneToOne(mappedBy = "utente") // Indica il nome del campo nella classe Socio che mappa questa relazione
    private Socio socio;

    // Metodo per ottenere l'ID dell'utente
    public Integer getId() {
        return id;
    }

    // Metodo per impostare l'ID dell'utente
    public void setId(Integer id) {
        this.id = id;
    }

    // Metodo per ottenere il codice fiscale dell'utente
    public String getCf() {
        return cf;
    }

    // Metodo per impostare il codice fiscale dell'utente
    public void setCf(String cf) {
        this.cf = cf;
    }

    // Metodo per ottenere la data di nascita dell'utente
    public LocalDate getDataNascita() {
        return dataNascita;
    }

    // Metodo per impostare la data di nascita dell'utente
    public void setDataNascita(LocalDate dataNascita) {
        this.dataNascita = dataNascita;
    }

    // Metodo per ottenere il luogo di nascita dell'utente
    public String getLuogoNascita() {
        return luogoNascita;
    }

    // Metodo per impostare il luogo di nascita dell'utente
    public void setLuogoNascita(String luogoNascita) {
        this.luogoNascita = luogoNascita;
    }

    // Metodo per ottenere il nome dell'utente
    public String getNome() {
        return nome;
    }

    // Metodo per impostare il nome dell'utente
    public void setNome(String nome) {
        this.nome = nome;
    }

    // Metodo per ottenere il cognome dell'utente
    public String getCognome() {
        return cognome;
    }

    // Metodo per impostare il cognome dell'utente
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    // Metodo per ottenere l'indirizzo dell'utente
    public String getIndirizzo() {
        return indirizzo;
    }

    // Metodo per impostare l'indirizzo dell'utente
    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    // Metodo per ottenere il socio associato all'utente
    public Socio getSocio() {
        return socio;
    }

    // Metodo per impostare il socio associato all'utente
    public void setSocio(Socio socio) {
        this.socio = socio;
    }
}
