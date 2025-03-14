package com.example.babee_transmission_project.controller;

import com.example.babee_transmission_project.model.CareActResource;
import com.example.babee_transmission_project.model.input.CareActInputResource;
import com.example.babee_transmission_project.service.CareActService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/care-act")
public class CareActController {


    private final CareActService careActService;

    public CareActController(CareActService careActService) {
        this.careActService = careActService;
    }

    @GetMapping
    public List<CareActResource> getCareActs(@RequestParam(required = false) UUID babeeId, @RequestParam(required = false) LocalDate eventDate) {
        return careActService.getCareActs(babeeId, eventDate);
    }


    @GetMapping("/{id}")
    public CareActResource getCareActById(@PathVariable UUID id) {
        return careActService.getCareActById(id);
    }

    @PostMapping()
    public CareActResource saveCareAct(@RequestBody @Validated CareActInputResource careActInputResource) {
        return careActService.saveCareAct(careActInputResource);
    }

    @PutMapping("/{id}")
    public CareActResource updateCareAct(@PathVariable UUID id, @RequestBody @Validated CareActInputResource careActInputResource) {
        return careActService.updateCareAct(id, careActInputResource);
    }

    @DeleteMapping("/{id}")
    public void deleteCareAct(@PathVariable UUID id) {
        careActService.deleteCareActById(id);
    }

}
