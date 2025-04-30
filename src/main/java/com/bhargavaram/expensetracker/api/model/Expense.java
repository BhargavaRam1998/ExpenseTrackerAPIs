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

    private String description;

    private int amount;

    private String category;

    private LocalDate date;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "ID=" + ID +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                ", category='" + category + '\'' +
                ", date=" + date +
                '}';
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
