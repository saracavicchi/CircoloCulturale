package it.unife.cavicchidome.CircoloCulturale.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "sede", schema = "public", uniqueConstraints = {
        @UniqueConstraint(name = "SEDE_unique1", columnNames = {"nome"}),
        @UniqueConstraint(name = "SEDE_unique2", columnNames = {"indirizzo"})
})
public class Sede {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nome", nullable = false, length = 30)
    private String nome;

    @Column(name = "indirizzo", nullable = false, length = 80)
    private String indirizzo;

    @Column(name = "ristoro", nullable = false)
    private Boolean ristoro = false;

    @Column(name = "active", nullable = false)
    private Boolean active = false;

    @ElementCollection
    @CollectionTable(name = "giorno_chiusura", joinColumns = @JoinColumn(name = "id_sede", referencedColumnName = "id", nullable = false))
    @Column(name = "giorno_chiusura", nullable = false)
    private Set<LocalDate> giornoChiusura = new LinkedHashSet<LocalDate>();

    @OneToMany(mappedBy = "idSede", cascade = CascadeType.ALL)
    private Set<OrarioSede> orarioSede = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idSede")
    private Set<Sala> sale = new LinkedHashSet<>();

    @OneToOne(mappedBy = "sedeAmministrata")
    private Segretario segretario;

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

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public Boolean getRistoro() {
        return ristoro;
    }

    public void setRistoro(Boolean ristoro) {
        this.ristoro = ristoro;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<LocalDate> getGiornoChiusura() {
        return giornoChiusura;
    }

    public void setGiornoChiusura(Set<LocalDate> giornoChiusura) {
        this.giornoChiusura = giornoChiusura;
    }

    public Set<OrarioSede> getOrarioSede() {
        return orarioSede;
    }

    public void setOrarioSede(Set<OrarioSede> orarioSede) {
        this.orarioSede = orarioSede;
    }

    public Set<Sala> getSale() {
        return sale;
    }

    public void setSale(Set<Sala> sale) {
        this.sale = sale;
    }

    public Segretario getSegretario() {
        return segretario;
    }

    public void setSegretario(Segretario segretario) {
        this.segretario = segretario;
    }



}