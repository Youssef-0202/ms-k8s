package com.techie.microservice.image.service;

import com.techie.microservice.image.config.MinioConfig;
import io.minio.*;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

/**
 * @author HP
 **/

@Service
@Slf4j
public class ImageService {

    private final MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    public ImageService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @PostConstruct
    public void initBucket(){
        try{
            boolean found = minioClient.bucketExists(
                    BucketExistsArgs.builder()
                            .bucket(bucketName)
                            .build()
            );
            if(!found){
                log.info("Le bucket '{}' n'existe pas. Création en cours...", bucketName);
                minioClient.makeBucket(
                        MakeBucketArgs.builder()
                                .bucket(bucketName)
                                .build()
                );
                log.info("Bucket '{}' créé avec succès !", bucketName);
            } else {
                log.info("Le bucket '{}' existe déjà.", bucketName);
            }
        }catch (Exception ex){
            log.error("Erreur lors de l'initialisation du bucket MinIO", ex);
            throw new RuntimeException("Impossible d'initialiser le stockage MinIO", ex);
        }
    }

    public String uploadImage(MultipartFile file){
        if(file.isEmpty()){
            throw new IllegalArgumentException("Le fichier envoyé est vide");
        }
        String originalFilename = file.getOriginalFilename();
        String extension = "";

        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        String uniqueFilename = UUID.randomUUID().toString() + extension;

        try {
            log.info("Upload de l'image ({})...", uniqueFilename);
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(uniqueFilename)
                            .stream(file.getInputStream(), file.getSize(),-1)
                            .contentType(file.getContentType())
                            .build()
            );
            return uniqueFilename;
        }catch (Exception ex){
            log.error("Erreur lors de l'envoi de l'image vers MinIO", ex);
            throw new RuntimeException("Échec de l'envoi de l'image", ex);
        }
    }

    public InputStream getImage(String imageId){
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(imageId)
                            .build()
            );
        }catch (Exception ex){
            log.error("Erreur lors du téléchargement de l'image {}", imageId, ex);
            throw new RuntimeException("Impossible de lire l'image", ex);
        }
    }

    public void deleteImage(String imageId){
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(imageId)
                            .build()
            );
        }catch (Exception ex){
            log.error("Erreur lors de la suppression de l'image {}", imageId, ex);
            throw new RuntimeException("Échec de la suppression de l'image", ex);}
    }
}
