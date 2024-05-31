package it.unife.cavicchidome.CircoloCulturale.models;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

// Questa classe rappresenta l'entità "Socio" nel contesto del circolo culturale
@Entity
@Table(name = "socio") // Specifica il nome della tabella nel database associata a questa entità
public class Socio {

    // Identificatore univoco del socio
    @Id
    @Column(name = "id", nullable = false) // Specifica il nome della colonna nell'entità associata a questo campo
    private Integer id;

    // Associazione uno a uno con l'entità Utente, utilizzando la stessa chiave primaria
    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false) // Specifica la relazione uno-a-uno con Utente
    @OnDelete(action = OnDeleteAction.CASCADE) // Azione di cancellazione in cascata: se l'utente viene eliminato, anche il socio
    @JoinColumn(name = "id", nullable = false) // Specifica la colonna nel database che mappa questa relazione
    private Utente utente;

    // Email del socio
    @Column(name = "email", nullable = false, length = 50) // Specifica il nome e le caratteristiche della colonna nel database
    private String email;

    // Password del socio
    @Column(name = "password", nullable = false, length = 50) // Specifica il nome e le caratteristiche della colonna nel database
    private String password;

    // Numero di telefono del socio
    @Column(name = "telefono", length = 10, columnDefinition = "bpchar") // Specifica il nome e le caratteristiche della colonna nel database
    private String telefono;

    // URL dell'immagine del socio
    @Column(name = "url_foto", length = 80) // Specifica il nome e le caratteristiche della colonna nel database
    private String urlFoto;

    // Relazione uno a uno con un docente
    @OneToOne(mappedBy = "socio") // Indica il nome del campo nella classe Docente che mappa questa relazione
    private Docente docente;

    // Relazione uno a uno con un segretario
    @OneToOne(mappedBy = "socio") // Indica il nome del campo nella classe Segretario1 che mappa questa relazione
    private Segretario1 segretario1;

    // Relazione uno a uno con una tessera
    @OneToOne(mappedBy = "idSocio") // Indica il nome del campo nella classe Tessera che mappa questa relazione
    private Tessera tessera;

    // Metodo per ottenere l'ID del socio
    public Integer getId() {
        return id;
    }

    // Metodo per impostare l'ID del socio
    public void setId(Integer id) {
        this.id = id;
    }

    // Metodo per ottenere l'utente associato al socio
    public Utente getUtente() {
        return utente;
    }

    // Metodo per impostare l'utente associato al socio
    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    // Metodo per ottenere l'email del socio
    public String getEmail() {
        return email;
    }

    // Metodo per impostare l'email del socio
    public void setEmail(String email) {
        this.email = email;
    }

    // Metodo per ottenere la password del socio
    public String getPassword() {
        return password;
    }

    // Metodo per impostare la password del socio
    public void setPassword(String password) {
        this.password = password;
    }

    // Metodo per ottenere il numero di telefono del socio
    public String getTelefono() {
        return telefono;
    }

    // Metodo per impostare il numero di telefono del socio
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    // Metodo per ottenere l'URL dell'immagine del socio
    public String getUrlFoto() {
        return urlFoto;
    }

    // Metodo per impostare l'URL dell'immagine del socio
    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    // Metodo per ottenere il docente associato al socio
    public Docente getDocente() {
        return docente;
    }

    // Metodo per impostare il docente associato al socio
    public void setDocente(Docente docente) {
        this.docente = docente;
    }

    // Metodo per ottenere il segretario associato al socio
    public Segretario1 getSegretario1() {
        return segretario1;
    }

    // Metodo per impostare il segretario associato al socio
    public void setSegretario1(Segretario1 segretario1) {
        this.segretario1 = segretario1;
    }

    // Metodo per ottenere la tessera associata al socio
    public Tessera getTessera() {
        return tessera;
    }

    // Metodo per impostare la tessera associata al socio
    public void setTessera(Tessera tessera) {
        this.tessera = tessera;
    }
}
