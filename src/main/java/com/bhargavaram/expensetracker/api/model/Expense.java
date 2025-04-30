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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
