package it.unife.cavicchidome.CircoloCulturale.models;

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
    @JoinColumns({
            @JoinColumn(name = "numero_sala", referencedColumnName = "numero_sala"),
            @JoinColumn(name = "id_sede", referencedColumnName = "id_sede")
    })
    private Sala sala;

    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;

    @ManyToMany
    @JoinTable(name = "docente_insegna",
            joinColumns = @JoinColumn(name = "id_corso"),
            inverseJoinColumns = @JoinColumn(name = "id_docente"))
    private Set<Docente> docenti = new LinkedHashSet<>();

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

    public Sala getSala() {
        return sala;
    }

    public void setSala(Sala sala) {
        this.sala = sala;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

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

}