package it.unife.cavicchidome.CircoloCulturale.repositories;

import it.unife.cavicchidome.CircoloCulturale.models.Socio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SocioRepository extends JpaRepository<Socio, Integer> {
    @Query("select s from Socio s where s.utente.cf = ?1 and s.password = ?2")
    Optional<Socio> authenticateSocio(String cf, String password);

    @Query("SELECT u.cf, u.nome, u.cognome, u.id FROM Socio s JOIN s.utente u WHERE NOT EXISTS (SELECT 1 FROM Segretario seg WHERE seg.socio.id = s.id)")
    List<Object[]> findSociNotSegretari();

    @Query("SELECT u.cf, u.nome, u.cognome, u.id FROM Socio s JOIN s.utente u WHERE NOT EXISTS (SELECT 1 FROM Segretario seg WHERE seg.socio.id = s.id) AND NOT EXISTS (SELECT d FROM Docente d JOIN d.corsi c WHERE d.socio.id = s.id AND c.id = ?1)")
    List<Object[]> findSociNotDocentiAndNotSegretariByIdCorso(Integer idCorso);
}