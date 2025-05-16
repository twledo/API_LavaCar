package dev.Lavacar.Nego.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class WashDTO {

    private Long id;
    private String client;
    private LocalDate date;
    private double valueWash;
    private String description;

    public WashDTO() {
    }

    public WashDTO(Long id, String client, LocalDate date, double valueWash, String description) {
        this.id = id;
        this.client = client;
        this.date = date;
        this.valueWash = valueWash;
        this.description = description;
    }
}
