package com.bhargavaram.expensetracker.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@AllArgsConstructor
@RequiredArgsConstructor
public class Expense {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID;

    private String Description;

    private int Amount;

    private String Category;

    private LocalDate Date;
}
