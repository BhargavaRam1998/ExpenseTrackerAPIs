package com.bhargavaram.expensetracker.api.service;

import com.bhargavaram.expensetracker.api.model.Expense;
import com.bhargavaram.expensetracker.api.repo.ExpenseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepo expenseRepo;

    public void addExpense(Expense expense){
        expenseRepo.save(expense);
    }

    public void deleteExpense(int id) {
       if(expenseRepo.existsById(id)) {
           expenseRepo.deleteById(id);
       }
    }

    public void updateExpense(int id, Expense expense) {
        if(expenseRepo.existsById(id)) {
            expense.setID(id);
        }
    }
}
