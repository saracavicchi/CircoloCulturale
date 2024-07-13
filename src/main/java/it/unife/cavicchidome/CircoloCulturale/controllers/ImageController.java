package it.unife.cavicchidome.CircoloCulturale.controllers;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class ImageController {

    //private final Path uploadDir = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "static", "images", "saggioPhotos");

    @GetMapping("/images/saggioPhotos/{photo:.+}")
    public ResponseEntity<Resource> serveFileSaggio(@PathVariable String photo) {
        System.out.println(photo + "ImageController");
        try {
            Path fileStorageLocation = Paths.get(System.getProperty("user.dir") + "/src/main/resources/static/images/saggioPhotos/");
            Path filePath = fileStorageLocation.resolve(photo).normalize();
            //Path filePath = uploadDir.resolve(photo).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if(resource.exists() || resource.isReadable()) {
                String contentType = Files.probeContentType(filePath);
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/images/corsoPhotos/{photo:.+}")
    public ResponseEntity<Resource> serveFileCorso(@PathVariable String photo) {
        System.out.println(photo + "ImageController");
        try {
            Path fileStorageLocation = Paths.get(System.getProperty("user.dir") + "/src/main/resources/static/images/corsoPhotos/");
            Path filePath = fileStorageLocation.resolve(photo).normalize();
            //Path filePath = uploadDir.resolve(photo).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if(resource.exists() || resource.isReadable()) {
                String contentType = Files.probeContentType(filePath);
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/images/socioProfilePhotos/{photo:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String photo) {
        System.out.println(photo + "ImageController");
        try {
            Path fileStorageLocation = Paths.get(System.getProperty("user.dir") + "/src/main/resources/static/images/socioProfilePhotos/");
            Path filePath = fileStorageLocation.resolve(photo).normalize();
            //Path filePath = uploadDir.resolve(photo).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if(resource.exists() || resource.isReadable()) {
                String contentType = Files.probeContentType(filePath);
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}