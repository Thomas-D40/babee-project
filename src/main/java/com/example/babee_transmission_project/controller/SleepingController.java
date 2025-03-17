package com.example.babee_transmission_project.controller;

import com.example.babee_transmission_project.model.SleepingResource;
import com.example.babee_transmission_project.model.input.SleepingInputResource;
import com.example.babee_transmission_project.service.SleepingService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/sleeping")
public class SleepingController {
	
	
	private final SleepingService sleepingService;
	
	public SleepingController(SleepingService sleepingService) {
		this.sleepingService = sleepingService;
	}
	
	@GetMapping
	public List<SleepingResource> getSleepings(@RequestParam(required = false) UUID babeeId, @RequestParam(required = false) LocalDate eventDate) {
		return sleepingService.getSleepings(babeeId, eventDate);
	}
	
	
	@GetMapping("/{id}")
	public SleepingResource getSleepingById(@PathVariable UUID id) {
		return sleepingService.getSleepingById(id);
	}
	
	@PostMapping()
	public SleepingResource saveSleeping(@RequestBody @Validated SleepingInputResource sleepingInputResource) {
		return sleepingService.saveSleeping(sleepingInputResource);
	}
	
	@PutMapping("/{id}")
	public SleepingResource updateSleeping(@PathVariable UUID id, @RequestBody @Validated SleepingInputResource sleepingInputResource) {
		return sleepingService.updateSleeping(id, sleepingInputResource);
	}
	
	@DeleteMapping("/{id}")
	public void deleteSleeping(@PathVariable UUID id) {
		sleepingService.deleteSleepingById(id);
	}
	
}
