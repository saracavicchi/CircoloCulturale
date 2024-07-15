package it.unife.cavicchidome.CircoloCulturale.repositories;

import it.unife.cavicchidome.CircoloCulturale.models.Saggio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SaggioRepository extends JpaRepository<Saggio, Integer> {
    @Query("""
       SELECT s FROM Saggio s WHERE s.deleted = false AND s.data <= ?1
    """)
    public List<Saggio> getNextSaggi(LocalDate untilDate);

    @Query("SELECT s FROM Saggio s WHERE s.data = ?1 AND s.deleted = false")
    public Optional<Saggio> getSaggioByData(LocalDate data);

    @Query("SELECT s FROM Saggio s WHERE s.data = ?1")
    public Optional<Saggio> getSaggioByDataEvenIfDeleted(LocalDate data);

    @Query("SELECT s FROM Saggio s WHERE s.nome = ?1 AND s.deleted = false")
    public Optional<Saggio> getSaggioByName(String nome);

    @Query("SELECT s FROM Saggio s WHERE s.nome = ?1")
    public Optional<Saggio> getSaggioByNameEvenIfDeleted(String nome);

    @Query("SELECT s FROM Saggio s WHERE s.deleted = false")
    public List<Saggio> findAllNotDeleted();

    @Query("SELECT s FROM Saggio s")
    public List<Saggio> findAll();

    @Query("SELECT s FROM Saggio s WHERE s.deleted = false AND s.id = ?1")
    public Optional<Saggio> findByIdNotDeleted(Integer id);

    @Query("SELECT s FROM Saggio s WHERE s.id = ?1")
    public Optional<Saggio> findByIdAll(Integer id);

    @Query("SELECT s FROM Saggio s WHERE s.data > :date AND (s.deleted = :deleted OR s.deleted = false)")
    public List<Saggio> getSaggioAfterDateDeleted(LocalDate date, boolean deleted);
}