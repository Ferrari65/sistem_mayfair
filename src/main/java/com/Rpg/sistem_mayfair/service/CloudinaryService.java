package com.Rpg.sistem_mayfair.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    // =========================
    // UPLOAD
    // =========================
    public String uploadFile(MultipartFile file) {

        try {

            Map uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    Map.of()
            );

            System.out.println(uploadResult);

            return uploadResult
                    .get("secure_url")
                    .toString();

        } catch (Exception e) {

            e.printStackTrace();

            throw new RuntimeException(
                    "ERRO CLOUDINARY REAL: "
                            + e.getMessage()
            );
        }
    }

    // =========================
    // DELETE IMAGE
    // =========================
    public void deleteFile(String imageUrl) {

        try {

            if (imageUrl == null || imageUrl.isBlank()) {
                return;
            }

            String publicId =
                    extractPublicId(imageUrl);

            System.out.println(
                    "DELETANDO CLOUDINARY ID: "
                            + publicId
            );

            Map result = cloudinary.uploader().destroy(
                    publicId,
                    ObjectUtils.emptyMap()
            );

            System.out.println(result);

        } catch (Exception e) {

            e.printStackTrace();

            throw new RuntimeException(
                    "Erro ao deletar imagem antiga: "
                            + e.getMessage()
            );
        }
    }

    // =========================
    // EXTRAIR PUBLIC ID
    // =========================
    private String extractPublicId(String imageUrl) {

        try {

            // Exemplo:
            // https://res.cloudinary.com/demo/image/upload/v123456/foto.png

            String[] parts = imageUrl.split("/");

            String fileName =
                    parts[parts.length - 1];

            // remove extensão
            return fileName.substring(
                    0,
                    fileName.lastIndexOf(".")
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Erro ao extrair public_id da URL"
            );
        }
    }
}