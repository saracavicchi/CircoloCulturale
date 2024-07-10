package it.unife.cavicchidome.CircoloCulturale.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "saggio", schema = "public", uniqueConstraints = {
        @UniqueConstraint(name = "SAGGIO_unique1", columnNames = {"nome"}),
        @UniqueConstraint(name = "SAGGIO_unique2", columnNames = {"data"})
})
public class Saggio {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "nome", nullable = false, length = 30)
    private String nome;

    @Column(name = "data", nullable = false)
    private LocalDate data;

    @Column(name = "orario_inizio")
    private LocalTime orarioInizio;

    @Column(name = "orario_fine")
    private LocalTime orarioFine;

    @Column(name = "descrizione", length = Integer.MAX_VALUE)
    private String descrizione;

    @Column(name = "max_partecipanti", nullable = false)
    private Integer maxPartecipanti;

    @Column(name = "indirizzo", nullable = false, length = 80)
    private String indirizzo;

    @Column(name = "url_foto")
    private String urlFoto;

    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;

    @OneToMany(mappedBy = "idSaggio")
    private Set<Biglietto> biglietti = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(name = "saggio_inerente_corso",
            joinColumns = @JoinColumn(name = "id_saggio"),
            inverseJoinColumns = @JoinColumn(name = "id_corso"))
    private Set<Corso> corsi = new LinkedHashSet<>();

    /*@ManyToMany
    @JoinTable(name = "saggio_partecipa_docente",
            joinColumns = @JoinColumn(name = "id_saggio"),
            inverseJoinColumns = @JoinColumn(name = "id_docente"))
    private Set<Docente> docenti = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(name = "saggio_partecipa_socio",
            joinColumns = @JoinColumn(name = "id_saggio"),
            inverseJoinColumns = @JoinColumn(name = "id_socio"))
    private Set<Socio> soci = new LinkedHashSet<>();
    */


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getOrarioInizio() {
        return orarioInizio;
    }

    public void setOrarioInizio(LocalTime orarioInizio) {
        this.orarioInizio = orarioInizio;
    }

    public LocalTime getOrarioFine() {
        return orarioFine;
    }

    public void setOrarioFine(LocalTime orarioFine) {
        this.orarioFine = orarioFine;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Integer getMaxPartecipanti() {
        return maxPartecipanti;
    }

    public void setMaxPartecipanti(Integer maxPartecipanti) {
        this.maxPartecipanti = maxPartecipanti;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
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

    public Set<Biglietto> getBiglietti() {
        return biglietti;
    }

    public void setBiglietti(Set<Biglietto> biglietti) {
        this.biglietti = biglietti;
    }

    public Set<Corso> getCorsi() {
        return corsi;
    }

    public void setCorsi(Set<Corso> corsi) {
        this.corsi = corsi;
    }
/*
    public Set<Docente> getDocenti() {
        return docenti;
    }

    public void setDocenti(Set<Docente> docenti) {
        this.docenti = docenti;
    }

    public Set<Socio> getSoci() {
        return soci;
    }

    public void setSoci(Set<Socio> soci) {
        this.soci = soci;
    }
    */

}