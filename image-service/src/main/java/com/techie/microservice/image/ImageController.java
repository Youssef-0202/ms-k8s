package com.techie.microservice.image;

import com.techie.microservice.image.service.ImageService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Map;

/**
 * @author HP
 **/

@RestController
@RequestMapping("/api/image")
public class ImageController {
    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) {
        String imageId = imageService.uploadImage(file);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("imageId", imageId));
    }
    //  Endpoint de Visionnage : Renvoie le flux de l'image brute avec le bon Content-Type de navigateur

    @GetMapping("/{id}")
    public ResponseEntity<InputStreamResource> getImage(@PathVariable("id") String id) {
        InputStream imageStream = imageService.getImage(id);

        MediaType mediaType = MediaType.IMAGE_JPEG; // Type par défaut (jpeg/jpg)
        String lowerId = id.toLowerCase();
        if (lowerId.endsWith(".png")) {
            mediaType = MediaType.IMAGE_PNG;
        } else if (lowerId.endsWith(".gif")) {
            mediaType = MediaType.IMAGE_GIF;
        } else if (lowerId.endsWith(".webp")) {
            mediaType = MediaType.parseMediaType("image/webp");
        }
        return ResponseEntity.ok()
                .contentType(mediaType)
                .body(new InputStreamResource(imageStream));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable("id") String id) {
        imageService.deleteImage(id);
        return ResponseEntity.noContent().build();
    }
}
