package it.unife.cavicchidome.CircoloCulturale.models;

import jakarta.persistence.*;

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
    private Integer id;

    @Column(name = "nome", nullable = false, length = 30)
    private String nome;

    @Column(name = "indirizzo", nullable = false, length = 80)
    private String indirizzo;

    @Column(name = "ristoro", nullable = false)
    private Boolean ristoro = false;

    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;

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

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
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