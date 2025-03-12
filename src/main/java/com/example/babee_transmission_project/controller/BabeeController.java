package com.example.babee_transmission_project.controller;

import com.example.babee_transmission_project.model.BabeeResource;
import com.example.babee_transmission_project.model.input.BabeeInputResource;
import com.example.babee_transmission_project.service.BabeeService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/babee")
public class BabeeController {


    private final BabeeService babeeService;

    public BabeeController(BabeeService babeeService) {
        this.babeeService = babeeService;
    }

    @GetMapping
    public List<BabeeResource> getBabeeList() {
        return babeeService.getBabees();
    }


    @GetMapping("/{id}")
    public BabeeResource getBabeeById(@PathVariable UUID id) {
        return babeeService.getBabeeById(id);
    }

    @PostMapping()
    public BabeeResource saveBabee(@RequestBody @Validated BabeeInputResource babeeInputResource) {
        return babeeService.saveBabee(babeeInputResource);
    }

    @PutMapping("/{id}")
    public BabeeResource updateBabee(@PathVariable UUID id, @RequestBody @Validated BabeeInputResource babeeInputResource) {
        return babeeService.updateBabee(id, babeeInputResource);
    }

    @DeleteMapping("/{id}")
    public void deleteBabee(@PathVariable UUID id) {
        babeeService.deleteBabeeById(id);
    }

}
