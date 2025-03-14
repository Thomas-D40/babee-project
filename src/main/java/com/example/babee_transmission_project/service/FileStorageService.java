package com.example.babee_transmission_project.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

@Service
public class FileStorageService {
	
	private final Path fileStorageLocation;
	
	public FileStorageService(@Value("${file.upload-dir}") String uploadDir) {
		try {
			// Stockage direct dans src/main/resources/uploads
			this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
			Files.createDirectories(this.fileStorageLocation);
		} catch (Exception ex) {
			throw new RuntimeException("Impossible de créer le dossier de stockage", ex);
		}
	}
	
	public String storeFileFromBase64(String base64Data, String fileName) {
		try {
			String cleanFileName = StringUtils.cleanPath(fileName);
			Path targetLocation = this.fileStorageLocation.resolve(cleanFileName);
			
			byte[] decodedBytes = Base64.getDecoder().decode(base64Data);
			
			try (FileOutputStream fos = new FileOutputStream(targetLocation.toFile())) {
				fos.write(decodedBytes);
			}
			
			return "/uploads/" + cleanFileName;
		} catch (IOException ex) {
			throw new RuntimeException("Impossible de stocker le fichier", ex);
		}
	}
	
	public String loadFile(String fileName) {
		try {
			Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
			
			if (!Files.exists(filePath)) {
				// Essai de lecture dans le classpath
				filePath = new ClassPathResource(fileName).getFile().toPath();
			}
			
			byte[] fileByteArray = Files.readAllBytes(filePath);
			return Base64.getEncoder().encodeToString(fileByteArray);
		} catch (IOException ex) {
			throw new RuntimeException("Fichier non trouvé : " + fileName, ex);
		}
	}
}
