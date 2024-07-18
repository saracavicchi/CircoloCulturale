package it.unife.cavicchidome.CircoloCulturale.repositories;

import it.unife.cavicchidome.CircoloCulturale.models.Tessera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.crypto.spec.OAEPParameterSpec;
import java.util.Optional;

public interface TesseraRepository extends JpaRepository<Tessera, String> {

    @Query("SELECT t FROM Tessera t WHERE t.idSocio.id = :id AND t.idSocio.deleted = false")
    Optional<Tessera> findByIdSocioActive(String id);

    @Query("SELECT t FROM Tessera t WHERE t.id = :id AND t.idSocio.deleted = false")
    Optional<Tessera> findByTesseraId(String id);
}