package com.example.babee_transmission_project.controller;

import com.example.babee_transmission_project.model.HealthActResource;
import com.example.babee_transmission_project.model.input.HealthActInputResource;
import com.example.babee_transmission_project.service.HealthActService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/health-act")
public class HealthActController {


    private final HealthActService healthActService;

    public HealthActController(HealthActService healthActService) {
        this.healthActService = healthActService;
    }

    @GetMapping
    public List<HealthActResource> getHealthActs(@RequestParam(required = false) UUID babeeId, @RequestParam(required = false) LocalDate eventDate) {
        return healthActService.getHealthActs(babeeId, eventDate);
    }


    @GetMapping("/{id}")
    public HealthActResource getHealthActById(@PathVariable UUID id) {
        return healthActService.getHealthActById(id);
    }

    @PostMapping()
    public HealthActResource saveHealthAct(@RequestBody @Validated HealthActInputResource healthActInputResource) {
        return healthActService.saveHealthAct(healthActInputResource);
    }

    @PutMapping("/{id}")
    public HealthActResource updateHealthAct(@PathVariable UUID id, @RequestBody @Validated HealthActInputResource healthActInputResource) {
        return healthActService.updateHealthAct(id, healthActInputResource);
    }

    @DeleteMapping("/{id}")
    public void deleteHealthAct(@PathVariable UUID id) {
        healthActService.deleteHealthActById(id);
    }

}
