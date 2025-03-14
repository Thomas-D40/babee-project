package com.example.babee_transmission_project.converter;

import com.example.babee_transmission_project.entity.BabeeEntity;
import com.example.babee_transmission_project.model.BabeeResource;
import com.example.babee_transmission_project.model.input.BabeeInputResource;
import com.example.babee_transmission_project.service.FileStorageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class BabeeConverter {
	
	private final FileStorageService fileStorageService;
	
	public BabeeConverter(FileStorageService fileStorageService) {
		this.fileStorageService = fileStorageService;
	}
	
	
	public BabeeResource convertEntityToResource(BabeeEntity babeeEntity) {
		String photo = null;
		
		if (!StringUtils.isBlank(babeeEntity.getPhotoUrl())) {
			photo = fileStorageService.loadFile(babeeEntity.getPhotoUrl());
		}
		
		return BabeeResource.builder()
				.id(babeeEntity.getId())
				.firstName(babeeEntity.getFirstName())
				.lastName(babeeEntity.getLastName())
				.birthDate(babeeEntity.getBirthDate())
				.photo(photo)
				.build();
	}
	
	public BabeeEntity convertInputResourceToEntity(BabeeInputResource babeeInputResource) {
		String photoUrl = null;
		
		if (!StringUtils.isBlank(babeeInputResource.getPhoto())) {
			String fileName = UUID.randomUUID() + ".jpg";
			photoUrl = fileStorageService.storeFileFromBase64(babeeInputResource.getPhoto(), fileName);
		}
		
		return BabeeEntity.builder()
				.firstName(babeeInputResource.getFirstName())
				.lastName(babeeInputResource.getLastName())
				.birthDate(babeeInputResource.getBirthDate())
				.photoUrl(photoUrl)
				.build();
	}
}
