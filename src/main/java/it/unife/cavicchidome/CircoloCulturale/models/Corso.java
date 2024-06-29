package it.unife.cavicchidome.CircoloCulturale.models;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "corso", schema = "public", uniqueConstraints = {
        @UniqueConstraint(name = "CORSO_unique1", columnNames = {"categoria", "genere", "livello"})
})
public class Corso {
    @Id
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
    private List<String> urlFoto;

    @Column(name = "active", nullable = false)
    private Boolean active = false;

    @OneToMany(mappedBy = "idCorso")
    private Set<CalendarioCorso> calendarioCorso = new LinkedHashSet<>();

    @ManyToMany(mappedBy = "idCorso")
    private Set<Docente> docenti = new LinkedHashSet<>();

    @ManyToMany(mappedBy = "idCorso")
    private Set<Saggio> saggi = new LinkedHashSet<>();

    @ManyToMany(mappedBy = "idCorso")
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

    public List<String> getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(List<String> urlFoto) {
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