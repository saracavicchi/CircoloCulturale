package it.unife.cavicchidome.CircoloCulturale.repositories;

import it.unife.cavicchidome.CircoloCulturale.models.Socio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

public interface SocioRepository extends JpaRepository<Socio, Integer> {
  @Query("select s from Socio s where s.password = ?1 and s.utente.cf = ?2")
  Socio authenticate(@NonNull String password, @NonNull String cf);
}