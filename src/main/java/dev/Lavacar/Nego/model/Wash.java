package dev.Lavacar.Nego.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Wash {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String client;
    private LocalDate date;
    private double valueWash;
    private String description;

    public Wash(Long id, String client, LocalDate date, double valueWash, String description) {
        this.id = id;
        this.client = client;
        this.date = date;
        this.valueWash = valueWash;
        this.description = description;
    }

    public Wash() {
    }
}
