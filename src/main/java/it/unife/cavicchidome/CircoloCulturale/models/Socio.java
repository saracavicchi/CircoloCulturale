package it.unife.cavicchidome.CircoloCulturale.models;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "socio", schema = "public")
public class Socio {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;


    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id", nullable = false)
    private Utente utente;

    @Column(name = "email", nullable = false, length = 50)
    private String email;

    @Column(name = "password", nullable = false, length = 50)
    private String password;

    @Column(name = "telefono", columnDefinition = "bpchar", length = 10)
    private String telefono;

    @Column(name = "url_foto", length = 80)
    private String urlFoto;

    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;

    @OneToOne(mappedBy = "socio")
    private Docente docente;

    @OneToMany(mappedBy = "idSocio")
    private Set<PrenotazioneSala> prenotazioniSale = new LinkedHashSet<>();
    /*
    @ManyToMany
    @JoinTable(name = "saggio_partecipa_socio",
            joinColumns = @JoinColumn(name = "id_socio"),
            inverseJoinColumns = @JoinColumn(name = "id_saggio"))
    private Set<Saggio> saggi = new LinkedHashSet<>();

     */

    @OneToOne(mappedBy = "socio")
    private Segretario segretario;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "socio_frequenta",
            joinColumns = @JoinColumn(name = "id_socio"),
            inverseJoinColumns = @JoinColumn(name = "id_corso"))
    private Set<Corso> corsi = new LinkedHashSet<>();

    @OneToOne(mappedBy = "idSocio", cascade = CascadeType.ALL)
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

    public Set<PrenotazioneSala> getPrenotazioniSale() {
        return prenotazioniSale;
    }

    public void setPrenotazioniSale(Set<PrenotazioneSala> prenotazioniSale) {
        this.prenotazioniSale = prenotazioniSale;
    }
/*
    public Set<Saggio> getSaggi() {
        return saggi;
    }

    public void setSaggi(Set<Saggio> saggi) {
        this.saggi = saggi;
    }

 */

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

    public Socio() {
    }

    public Socio(String email, String password, String telefono) {
        this.email = email;
        this.password = password;
        this.telefono = telefono;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUtente(), getEmail(), getPassword(), getTelefono(), getUrlFoto(), getDeleted(), getDocente(), getPrenotazioniSale(), getSegretario(), getCorsi(), getTessera());
    }
}