package com.bhargavaram.expensetracker.api.service;

import com.bhargavaram.expensetracker.api.model.Expense;
import com.bhargavaram.expensetracker.api.repo.ExpenseRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepo expenseRepo;

    public List<Expense> addExpense(Expense expense){

        if (expense.getDate() == null) {
            expense.setDate(LocalDate.now());
        }

        expenseRepo.save(expense);
        List<Expense> expenses = expenseRepo.findAll();
        return expenses;
    }

    public boolean deleteExpense(int id) {
       if(expenseRepo.existsById(id)) {
           expenseRepo.deleteById(id);
           return true;
       } else {
           return false;
       }
    }

    public Expense updateExpense(int id, Expense expense) {
        if(expenseRepo.existsById(id)){
            expense.setID(id);

            if (expense.getDate() == null) {
                expense.setDate(LocalDate.now());
            }

            return expenseRepo.save(expense);
        } else {
            return null;
        }

    }
}
