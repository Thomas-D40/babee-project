package com.example.babee_transmission_project.controller;

import com.example.babee_transmission_project.model.ActivityResource;
import com.example.babee_transmission_project.model.input.ActivityInputResource;
import com.example.babee_transmission_project.service.ActivityService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/activity")
public class ActivityController {


    private final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @GetMapping
    public List<ActivityResource> getActivities(@RequestParam(required = false) UUID babeeId, @RequestParam(required = false) LocalDate day) {
        return activityService.getActivities(babeeId, day);
    }


    @GetMapping("/{id}")
    public ActivityResource getActivityById(@PathVariable UUID id) {
        return activityService.getActivityById(id);
    }

    @PostMapping()
    public ActivityResource saveActivity(@RequestBody @Validated ActivityInputResource activityInputResource) {
        return activityService.saveActivity(activityInputResource);
    }

    @PutMapping("/{id}")
    public ActivityResource updateActivity(@PathVariable UUID id, @RequestBody @Validated ActivityInputResource activityInputResource) {
        return activityService.updateActivity(id, activityInputResource);
    }

    @DeleteMapping("/{id}")
    public void deleteActivity(@PathVariable UUID id) {
        activityService.deleteActivityById(id);
    }

}
