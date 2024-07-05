package it.unife.cavicchidome.CircoloCulturale.repositories;

import it.unife.cavicchidome.CircoloCulturale.models.OrarioSede;
import it.unife.cavicchidome.CircoloCulturale.models.OrarioSedeId;
import it.unife.cavicchidome.CircoloCulturale.models.Weekday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrarioSedeRepository extends JpaRepository<OrarioSede, OrarioSedeId> {
    @Query("SELECT os FROM OrarioSede os WHERE os.id.idSede = :idSede AND os.id.giornoSettimana = :giornoSettimana")
    Optional<OrarioSede> findByIdSedeAndGiornoSettimana(@Param("idSede") Integer idSede, @Param("giornoSettimana") Weekday giornoSettimana);
}