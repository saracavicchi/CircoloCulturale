package it.unife.cavicchidome.CircoloCulturale.models;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "socio", schema = "public")
public class Socio {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id", nullable = false)
    private Utente utente;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_tessera", nullable = false)
    private Tessera idTessera;

    @Column(name = "email", nullable = false, length = 50)
    private String email;

    @Column(name = "password", nullable = false, length = 50)
    private String password;

    @Column(name = "telefono", length = 10)
    private String telefono;

    @Column(name = "url_foto", length = 80)
    private String urlFoto;

    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;

    @OneToOne(mappedBy = "id")
    private Docente docente;

    @OneToMany(mappedBy = "idSocio")
    private Set<PrenotazioneSala> prenotazioniSala = new LinkedHashSet<>();

    @ManyToMany(mappedBy = "soci")
    private Set<Saggio> saggi = new LinkedHashSet<>();

    @OneToOne(mappedBy = "id")
    private Segretario segretario;

    @ManyToMany(mappedBy = "soci")
    private Set<Corso> corsi = new LinkedHashSet<>();

    @OneToOne(mappedBy = "idSocio")
    private Tessera tessera;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public Tessera getIdTessera() {
        return idTessera;
    }

    public void setIdTessera(Tessera idTessera) {
        this.idTessera = idTessera;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Docente getDocente() {
        return docente;
    }

    public void setDocente(Docente docente) {
        this.docente = docente;
    }

    public Set<PrenotazioneSala> getPrenotazioniSala() {
        return prenotazioniSala;
    }

    public void setPrenotazioniSala(Set<PrenotazioneSala> prenotazioniSala) {
        this.prenotazioniSala = prenotazioniSala;
    }

    public Set<Saggio> getSaggi() {
        return saggi;
    }

    public void setSaggi(Set<Saggio> saggi) {
        this.saggi = saggi;
    }

    public Segretario getSegretario() {
        return segretario;
    }

    public void setSegretario(Segretario segretario) {
        this.segretario = segretario;
    }

    public Set<Corso> getCorsi() {
        return corsi;
    }

    public void setCorsi(Set<Corso> corsi) {
        this.corsi = corsi;
    }

    public Tessera getTessera() {
        return tessera;
    }

    public void setTessera(Tessera tessera) {
        this.tessera = tessera;
    }

}