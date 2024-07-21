package com.gdsc.projectmiobackend.discord;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Embed {
    private String title;
    private String description;
    private String timestamp;
}