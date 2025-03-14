package com.example.babee_transmission_project.service;

import com.example.babee_transmission_project.converter.BabeeConverter;
import com.example.babee_transmission_project.entity.BabeeEntity;
import com.example.babee_transmission_project.exception.NotFoundException;
import com.example.babee_transmission_project.model.BabeeResource;
import com.example.babee_transmission_project.model.input.BabeeInputResource;
import com.example.babee_transmission_project.repository.BabeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BabeeService {
	
	private final String MISSING_ERROR = "Pas de babee avec l'id ";
	
	private final BabeeRepository babeeRepository;
	private final BabeeConverter babeeConverter;
	
	public BabeeService(BabeeRepository babeeRepository, BabeeConverter babeeConverter) {
		this.babeeRepository = babeeRepository;
		this.babeeConverter = babeeConverter;
	}
	
	public List<BabeeResource> getBabees() {
		return babeeRepository.findAll()
				.stream().map(babeeConverter::convertEntityToResource)
				.toList();
	}
	
	public BabeeResource getBabeeById(UUID id) throws NotFoundException {
		return babeeRepository.findById(id)
				.map(babeeConverter::convertEntityToResource)
				.orElseThrow(() -> new NotFoundException(MISSING_ERROR + id));
	}
	
	public BabeeResource saveBabee(BabeeInputResource babeeInputResource) {
		BabeeEntity babeeEntity = babeeConverter.convertInputResourceToEntity(babeeInputResource);
		
		return babeeConverter.convertEntityToResource(babeeRepository.save(babeeEntity));
	}
	
	public BabeeResource updateBabee(UUID id, BabeeInputResource babeeInputResource) {
		babeeRepository.findById(id)
				.orElseThrow(() -> new NotFoundException(MISSING_ERROR + id));
		
		BabeeEntity updatedBabeeEntity = babeeConverter.convertInputResourceToEntity(babeeInputResource);
		updatedBabeeEntity.setId(id);
		
		return babeeConverter.convertEntityToResource(babeeRepository.saveAndFlush(updatedBabeeEntity));
	}
	
	public void deleteBabeeById(UUID id) {
		Optional<BabeeEntity> maybeBabeeEntity = babeeRepository.findById(id);
		
		if (maybeBabeeEntity.isEmpty()) {
			throw new NotFoundException(MISSING_ERROR + id);
		}
		
		BabeeEntity babeeEntity = maybeBabeeEntity.get();
		
		babeeRepository.delete(babeeEntity);
	}
}
