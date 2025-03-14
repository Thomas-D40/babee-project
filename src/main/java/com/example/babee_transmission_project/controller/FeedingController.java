package com.example.babee_transmission_project.controller;

import com.example.babee_transmission_project.model.FeedingResource;
import com.example.babee_transmission_project.model.input.FeedingInputResource;
import com.example.babee_transmission_project.service.FeedingService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/feeding")
public class FeedingController {


    private final FeedingService feedingService;

    public FeedingController(FeedingService feedingService) {
        this.feedingService = feedingService;
    }

    @GetMapping
    public List<FeedingResource> getFeedings(@RequestParam(required = false) UUID babeeId, @RequestParam(required = false) LocalDate eventDate) {
        return feedingService.getFeedings(babeeId, eventDate);
    }


    @GetMapping("/{id}")
    public FeedingResource getFeedingById(@PathVariable UUID id) {
        return feedingService.getFeedingById(id);
    }

    @PostMapping()
    public FeedingResource saveFeeding(@RequestBody @Validated FeedingInputResource feedingInputResource) {
        return feedingService.saveFeeding(feedingInputResource);
    }

    @PutMapping("/{id}")
    public FeedingResource updateFeeding(@PathVariable UUID id, @RequestBody @Validated FeedingInputResource feedingInputResource) {
        return feedingService.updateFeeding(id, feedingInputResource);
    }

    @DeleteMapping("/{id}")
    public void deleteFeeding(@PathVariable UUID id) {
        feedingService.deleteFeedingById(id);
    }

}
