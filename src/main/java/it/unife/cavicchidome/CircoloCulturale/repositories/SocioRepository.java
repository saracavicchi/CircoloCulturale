package it.unife.cavicchidome.CircoloCulturale.repositories;

import it.unife.cavicchidome.CircoloCulturale.models.Socio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SocioRepository extends JpaRepository<Socio, Integer> {
    @Query("select s from Socio s where s.utente.cf = ?1 and s.password = ?2")
    Socio authenticateSocio(String cf, String password);
}