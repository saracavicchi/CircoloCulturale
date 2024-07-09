package it.unife.cavicchidome.CircoloCulturale.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "corso", schema = "public", uniqueConstraints = {
        @UniqueConstraint(name = "CORSO_unique1", columnNames = {"categoria", "genere", "livello"})
})
public class Corso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "descrizione", length = Integer.MAX_VALUE)
    private String descrizione;

    @Column(name = "categoria", nullable = false, length = 20)
    private String categoria;

    @Column(name = "genere", nullable = false, length = 20)
    private String genere;

    @Column(name = "livello", nullable = false, length = 20)
    private String livello;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "id_sala", nullable = false)
    private Sala idSala;

    @Column(name = "url_foto")
    private String urlFoto;

    @Column(name = "active", nullable = false)
    private Boolean active = false;

    @OneToMany(mappedBy = "idCorso", cascade = CascadeType.ALL)
    private Set<CalendarioCorso> calendarioCorso = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(name = "docente_insegna",
            joinColumns = @JoinColumn(name = "id_corso"),
            inverseJoinColumns = @JoinColumn(name = "id_docente"))
    private Set<Docente> docenti = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(name = "saggio_inerente_corso",
            joinColumns = @JoinColumn(name = "id_corso"),
            inverseJoinColumns = @JoinColumn(name = "id_saggio"))
    private Set<Saggio> saggi = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(name = "socio_frequenta",
            joinColumns = @JoinColumn(name = "id_corso"),
            inverseJoinColumns = @JoinColumn(name = "id_socio"))
    private Set<Socio> soci = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getGenere() {
        return genere;
    }

    public void setGenere(String genere) {
        this.genere = genere;
    }

    public String getLivello() {
        return livello;
    }

    public void setLivello(String livello) {
        this.livello = livello;
    }

    public Sala getIdSala() {
        return idSala;
    }

    public void setIdSala(Sala idSala) {
        this.idSala = idSala;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<CalendarioCorso> getCalendarioCorso() {
        return calendarioCorso;
    }

    public void setCalendarioCorso(Set<CalendarioCorso> calendarioCorso) {
        this.calendarioCorso = calendarioCorso;
    }

    public Set<Docente> getDocenti() {
        return docenti;
    }

    public void setDocenti(Set<Docente> docenti) {
        this.docenti = docenti;
    }

    public Set<Saggio> getSaggi() {
        return saggi;
    }

    public void setSaggi(Set<Saggio> saggi) {
        this.saggi = saggi;
    }

    public Set<Socio> getSoci() {
        return soci;
    }

    public void setSoci(Set<Socio> soci) {
        this.soci = soci;
    }

}