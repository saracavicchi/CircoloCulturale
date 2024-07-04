package it.unife.cavicchidome.CircoloCulturale.services;

import org.springframework.stereotype.Service;
import it.unife.cavicchidome.CircoloCulturale.models.Socio;
import it.unife.cavicchidome.CircoloCulturale.models.Tessera;
import it.unife.cavicchidome.CircoloCulturale.repositories.TesseraRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.random.RandomGenerator;

@Service
public class TesseraService {

    private final TesseraRepository tesseraRepository;

    public TesseraService(TesseraRepository tesseraRepository) {
        this.tesseraRepository = tesseraRepository;
    }

    @Transactional
    public Tessera newTessera(Socio socio, Optional<BigDecimal> price) {

        String tesseraId = generateTesseraId(socio, 0);

        Tessera tessera;
        if (price.isPresent()) {
            tessera = new Tessera(tesseraId, LocalDate.now(), price.get(), 'p');
        } else {
            tessera = new Tessera(tesseraId, LocalDate.now(), BigDecimal.valueOf(10.0), 'p');
        }

        tessera.setIdSocio(socio);

        return tesseraRepository.save(tessera);
    }

    public String generateTesseraId(Socio socio, Integer saltingValue) {
        int hashCode = socio.hashCode() + saltingValue;
        StringBuilder hashString = new StringBuilder(Integer.toString(hashCode, 36));

        if (hashString.length() > 10) {
            hashString = new StringBuilder(hashString.substring(0, 10));
        } else while (hashString.length() < 10) {
            hashString.append("0");
        }

        if ((tesseraRepository.findById(hashString.toString())).isPresent()) {
            return generateTesseraId(socio, RandomGenerator.getDefault().nextInt());
        } else return hashString.toString();
    }
}
