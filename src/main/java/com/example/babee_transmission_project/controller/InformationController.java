package com.example.babee_transmission_project.controller;

import com.example.babee_transmission_project.model.InformationResource;
import com.example.babee_transmission_project.model.input.InformationInputResource;
import com.example.babee_transmission_project.service.InformationService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/information")
public class InformationController {


    private final InformationService informationService;

    public InformationController(InformationService informationService) {
        this.informationService = informationService;
    }

    @GetMapping
    public List<InformationResource> getInformations(@RequestParam(required = false) UUID babeeId, @RequestParam(required = false) LocalDate day) {
        return informationService.getInformations(babeeId, day);
    }


    @GetMapping("/{id}")
    public InformationResource getInformationId(@PathVariable UUID id) {
        return informationService.getInformationById(id);
    }

    @PostMapping()
    public InformationResource saveInformation(@RequestBody @Validated InformationInputResource informationInputResource) {
        return informationService.saveInformation(informationInputResource);
    }

    @PutMapping("/{id}")
    public InformationResource updateInformation(@PathVariable UUID id, @RequestBody @Validated InformationInputResource informationInputResource) {
        return informationService.updateInformation(id, informationInputResource);
    }

    @DeleteMapping("/{id}")
    public void deleteInformation(@PathVariable UUID id) {
        informationService.deleteInformationById(id);
    }

}
