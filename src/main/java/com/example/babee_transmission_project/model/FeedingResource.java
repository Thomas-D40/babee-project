package com.example.babee_transmission_project.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
public class FeedingResource extends BaseResource {
    private List<String> feedingInformations;
}
