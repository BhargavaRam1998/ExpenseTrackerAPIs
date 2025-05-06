package com.bhargavaram.expensetracker.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@AllArgsConstructor
@RequiredArgsConstructor
public class Expense {

    public enum Category {
        GROCERIES,
        LEISURE,
        ELECTRONICS,
        UTILITIES,
        CLOTHING,
        HEALTH,
        OTHERS
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID;

    private String description;

    private int amount;

    @Enumerated(EnumType.STRING)
    private Category category;

    private LocalDate date;

}
